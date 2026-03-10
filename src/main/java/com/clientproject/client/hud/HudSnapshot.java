package com.clientproject.client.hud;

public record HudSnapshot(
        int fps,
        int pingMs,
        int cps,
        double reach,
        int combo,
        String keystrokes,
        String armor,
        String effects,
        String tntCountdown
) {}
