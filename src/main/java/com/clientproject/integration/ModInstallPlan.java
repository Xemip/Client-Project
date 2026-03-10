package com.clientproject.integration;

import java.util.List;

public record ModInstallPlan(List<ModDescriptor> redistributable, List<ModDescriptor> userProvided) {
}
