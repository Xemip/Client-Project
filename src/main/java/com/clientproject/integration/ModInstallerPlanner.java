package com.clientproject.integration;

import java.util.List;

public final class ModInstallerPlanner {
    public ModInstallPlan buildPlan(List<ModDescriptor> mods) {
        List<ModDescriptor> redistributable = mods.stream().filter(ModDescriptor::redistributable).toList();
        List<ModDescriptor> userProvided = mods.stream().filter(mod -> !mod.redistributable()).toList();
        return new ModInstallPlan(redistributable, userProvided);
    }
}
