package com.clientproject.client.hud;

public final class HudTracker {
    private int clicksInSecond;
    private long clickWindowStartNanos;
    private int comboCounter;
    private double lastReach;

    public void recordClick(long nowNanos) {
        if (clickWindowStartNanos == 0L) {
            clickWindowStartNanos = nowNanos;
        }
        if (nowNanos - clickWindowStartNanos >= 1_000_000_000L) {
            clicksInSecond = 0;
            clickWindowStartNanos = nowNanos;
        }
        clicksInSecond++;
    }

    public int cps() {
        return clicksInSecond;
    }

    public void recordHit(double reach) {
        lastReach = reach;
        comboCounter++;
    }

    public void resetCombo() {
        comboCounter = 0;
    }

    public int comboCounter() {
        return comboCounter;
    }

    public double lastReach() {
        return lastReach;
    }

    public HudSnapshot snapshot(int fps, int pingMs, String keystrokes, String armor, String effects, String tntCountdown) {
        return new HudSnapshot(fps, pingMs, clicksInSecond, lastReach, comboCounter, keystrokes, armor, effects, tntCountdown);
    }
}
