package net.motimaa.motiyhteisot.gui;

import net.motimaa.motiyhteisot.gui.pages.AbstractPage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GuiManager {

    private final Map<Player, AbstractPage> activePages;

    public GuiManager() {
        this.activePages = new HashMap<>();
    }

    public Map<Player, AbstractPage> getActivePages() {
        return activePages;
    }

}
