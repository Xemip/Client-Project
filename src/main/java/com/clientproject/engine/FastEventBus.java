package com.clientproject.engine;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Minimal allocation event bus for hot client loops.
 *
 * Listener dispatch is array-based to avoid iterator allocations during frame/tick updates.
 */
public final class FastEventBus {
    private final ConcurrentMap<Class<?>, ListenerList<?>> listeners = new ConcurrentHashMap<>();

    public <T> Subscription subscribe(Class<T> eventType, EventListener<T> listener) {
        Objects.requireNonNull(eventType, "eventType");
        Objects.requireNonNull(listener, "listener");

        ListenerList<T> list = getOrCreateList(eventType);
        list.add(listener);

        return () -> list.remove(listener);
    }

    public <T> void publish(T event) {
        if (event == null) {
            return;
        }

        @SuppressWarnings("unchecked")
        Class<T> eventType = (Class<T>) event.getClass();

        @SuppressWarnings("unchecked")
        ListenerList<T> list = (ListenerList<T>) listeners.get(eventType);

        if (list == null) {
            return;
        }

        list.dispatch(event);
    }

    @SuppressWarnings("unchecked")
    private <T> ListenerList<T> getOrCreateList(Class<T> eventType) {
        return (ListenerList<T>) listeners.computeIfAbsent(eventType, ignored -> new ListenerList<>());
    }

    @FunctionalInterface
    public interface EventListener<T> {
        void onEvent(T event);
    }

    @FunctionalInterface
    public interface Subscription {
        void unsubscribe();
    }

    private static final class ListenerList<T> {
        private volatile EventListener<T>[] snapshot = emptyArray();

        synchronized void add(EventListener<T> listener) {
            EventListener<T>[] current = snapshot;
            EventListener<T>[] next = Arrays.copyOf(current, current.length + 1);
            next[current.length] = listener;
            snapshot = next;
        }

        synchronized void remove(EventListener<T> listener) {
            EventListener<T>[] current = snapshot;
            int index = -1;
            for (int i = 0; i < current.length; i++) {
                if (current[i] == listener) {
                    index = i;
                    break;
                }
            }

            if (index < 0) {
                return;
            }

            EventListener<T>[] next = emptyArray();
            if (current.length > 1) {
                next = Arrays.copyOf(current, current.length - 1);
                System.arraycopy(current, index + 1, next, index, current.length - index - 1);
            }
            snapshot = next;
        }

        void dispatch(T event) {
            EventListener<T>[] current = snapshot;
            for (EventListener<T> listener : current) {
                listener.onEvent(event);
            }
        }

        @SuppressWarnings("unchecked")
        private EventListener<T>[] emptyArray() {
            return (EventListener<T>[]) new EventListener<?>[0];
        }
    }
}
