package net.motimaa.motiyhteisot.gui.pages;

import net.motimaa.motiyhteisot.MotiYhteisot;
import net.motimaa.motiyhteisot.gui.GuiManager;
import net.motimaa.motiyhteisot.gui.items.GuiItem;
import net.motimaa.motiyhteisot.organization.Organization;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public abstract class AbstractPage {

    protected static final MotiYhteisot motiYhteisot = MotiYhteisot.getInstance();

    protected final Player player;

    protected Organization organization;
    protected Inventory inventory;
    protected final HashMap<Integer, GuiItem> guiItemsMap;
    private final GuiManager guiManager;

    protected static final String SEPARATOR = "";

    public AbstractPage(Player player, Organization organization) {
        this.player = player;
        this.organization = organization;
        this.guiManager = motiYhteisot.getGuiManager();
        guiItemsMap = new HashMap<>();
    }

    public void openInventory() {
        this.player.openInventory(inventory);
        guiManager.getActivePages().put(player, this);
    }

    protected void closeInventory() {
        this.player.closeInventory();
        guiManager.getActivePages().remove(player);
    }

    public abstract void executeClick(InventoryClickEvent inventoryClickEvent);

    protected abstract void createInventory();

    protected void createGuiItem(int slot, GuiItem guiItem) {
        inventory.setItem(slot, guiItem.toItemStack());
        guiItemsMap.put(slot, guiItem);
    }
}
