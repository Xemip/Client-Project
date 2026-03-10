package com.clientproject.client.chat;

import com.clientproject.client.modules.BaseModule;
import com.clientproject.client.modules.ModuleCategory;

public final class AutoMessageModule extends BaseModule {
    private String ggMessage = "gg";
    private String thankMessage = "thanks";

    public AutoMessageModule() {
        super("auto_message", "Auto-GG/Auto-Thank", ModuleCategory.CHAT, true);
    }

    public String ggMessage() {
        return ggMessage;
    }

    public void setGgMessage(String ggMessage) {
        this.ggMessage = ggMessage;
    }

    public String thankMessage() {
        return thankMessage;
    }

    public void setThankMessage(String thankMessage) {
        this.thankMessage = thankMessage;
    }
}
