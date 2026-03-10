package com.clientproject.client.modules;

public interface ClientModule {
    String id();
    String displayName();
    ModuleCategory category();
    boolean enabled();
    void setEnabled(boolean enabled);
}
