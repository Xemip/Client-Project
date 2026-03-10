package com.clientproject.client.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ModuleRegistry {
    private final List<ClientModule> modules = new ArrayList<>();

    public void register(ClientModule module) {
        Objects.requireNonNull(module, "module");
        if (findById(module.id()).isPresent()) {
            throw new IllegalArgumentException("Duplicate module id: " + module.id());
        }
        modules.add(module);
    }

    public Optional<ClientModule> findById(String id) {
        return modules.stream().filter(module -> module.id().equals(id)).findFirst();
    }

    public List<ClientModule> all() {
        return Collections.unmodifiableList(modules);
    }

    public List<ClientModule> byCategory(ModuleCategory category) {
        return modules.stream().filter(module -> module.category() == category).toList();
    }
}
