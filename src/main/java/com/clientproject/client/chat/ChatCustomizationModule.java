package com.clientproject.client.chat;

import com.clientproject.client.modules.BaseModule;
import com.clientproject.client.modules.ModuleCategory;
import java.util.ArrayList;
import java.util.List;

public final class ChatCustomizationModule extends BaseModule {
    private final List<String> highlightedNicknames = new ArrayList<>();
    private boolean colorCodeSupport = true;

    public ChatCustomizationModule() {
        super("chat_customization", "Chat Customization", ModuleCategory.CHAT, true);
    }

    public void addNickname(String nickname) {
        highlightedNicknames.add(nickname);
    }

    public List<String> highlightedNicknames() {
        return List.copyOf(highlightedNicknames);
    }

    public boolean colorCodeSupport() {
        return colorCodeSupport;
    }

    public void setColorCodeSupport(boolean colorCodeSupport) {
        this.colorCodeSupport = colorCodeSupport;
    }
}
