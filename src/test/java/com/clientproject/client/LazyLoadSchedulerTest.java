package com.clientproject.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.clientproject.client.performance.LazyLoadScheduler;
import org.junit.jupiter.api.Test;

final class LazyLoadSchedulerTest {
    @Test
    void pollsTasksByPriority() {
        LazyLoadScheduler scheduler = new LazyLoadScheduler();
        scheduler.enqueue("front_chunk", 1);
        scheduler.enqueue("far_chunk", 10);
        scheduler.enqueue("mid_chunk", 5);

        assertEquals("front_chunk", scheduler.pollNext());
        assertEquals("mid_chunk", scheduler.pollNext());
        assertEquals("far_chunk", scheduler.pollNext());
    }
}
