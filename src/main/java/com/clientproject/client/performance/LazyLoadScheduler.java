package com.clientproject.client.performance;

import java.util.Comparator;
import java.util.PriorityQueue;

public final class LazyLoadScheduler {
    private final PriorityQueue<LoadTask> queue = new PriorityQueue<>(Comparator.comparingInt(LoadTask::priority));

    public void enqueue(String key, int priority) {
        queue.offer(new LoadTask(key, priority));
    }

    public String pollNext() {
        LoadTask task = queue.poll();
        return task == null ? null : task.key();
    }

    public int size() {
        return queue.size();
    }

    private record LoadTask(String key, int priority) {}
}
