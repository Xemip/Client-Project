package com.clientproject.client;

public record ClientTargetProfile(String name, String minecraftVersion, String purpose) {
    public static ClientTargetProfile pvp189() {
        return new ClientTargetProfile("Client-Project PvP", "1.8.9", "PvP/Performance");
    }
}
