package com.clientproject.client.modules;

import java.util.Objects;

public abstract class BaseModule implements ClientModule {
    private final String id;
    private final String displayName;
    private final ModuleCategory category;
    private boolean enabled;

    protected BaseModule(String id, String displayName, ModuleCategory category, boolean enabled) {
        this.id = Objects.requireNonNull(id, "id");
        this.displayName = Objects.requireNonNull(displayName, "displayName");
        this.category = Objects.requireNonNull(category, "category");
        this.enabled = enabled;
    }

    @Override
    public final String id() {
        return id;
    }

    @Override
    public final String displayName() {
        return displayName;
    }

    @Override
    public final ModuleCategory category() {
        return category;
    }

    @Override
    public final boolean enabled() {
        return enabled;
    }

    @Override
    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
