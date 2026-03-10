package com.clientproject.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.clientproject.client.hud.HudTracker;
import org.junit.jupiter.api.Test;

final class HudTrackerTest {
    @Test
    void tracksClicksComboAndReach() {
        HudTracker tracker = new HudTracker();

        tracker.recordClick(100);
        tracker.recordClick(200);
        tracker.recordHit(3.12);
        tracker.recordHit(2.98);

        assertEquals(2, tracker.cps());
        assertEquals(2, tracker.comboCounter());
        assertEquals(2.98, tracker.lastReach(), 0.001);

        tracker.resetCombo();
        assertEquals(0, tracker.comboCounter());
    }
}
