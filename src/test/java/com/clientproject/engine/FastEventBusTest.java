package com.clientproject.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

final class FastEventBusTest {
    @Test
    void publishesToAllSubscribersAndSupportsUnsubscribe() {
        FastEventBus bus = new FastEventBus();
        StringBuilder sb = new StringBuilder();

        FastEventBus.Subscription s1 = bus.subscribe(String.class, value -> sb.append("A:").append(value).append(';'));
        bus.subscribe(String.class, value -> sb.append("B:").append(value).append(';'));

        bus.publish("tick");
        s1.unsubscribe();
        bus.publish("render");

        assertEquals("A:tick;B:tick;B:render;", sb.toString());
    }
}
