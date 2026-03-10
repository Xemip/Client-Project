package com.clientproject.integration;

import java.net.URI;

public record ModDescriptor(String id, String displayName, String minecraftVersion, URI downloadUri, boolean redistributable) {
}
