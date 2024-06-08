package net.motimaa.motiyhteisot.gui.pages;

import net.motimaa.motiyhteisot.gui.items.Action;
import net.motimaa.motiyhteisot.gui.items.GuiItem;
import net.motimaa.motiyhteisot.listeners.OrganizationChat;
import net.motimaa.motiyhteisot.organization.Organization;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.function.BiConsumer;


public class ConfirmPage extends AbstractPage {

    private final BiConsumer<Player, Boolean> biConsumer;

    private final String title;

    public ConfirmPage(Player player, Organization organization, String title, BiConsumer<Player, Boolean> true_false) {
        super(player, organization);
        biConsumer = true_false;
        this.title = title;
        createInventory();
    }

    @Override
    public void executeClick(InventoryClickEvent inventoryClickEvent) {

        if (inventoryClickEvent.getClickedInventory() == null) {
            return;
        }
        if (inventoryClickEvent.getClickedInventory().getType() == InventoryType.PLAYER) {
            return;
        }

        inventoryClickEvent.setCancelled(true);
        GuiItem guiItem = guiItemsMap.get(inventoryClickEvent.getSlot());
        if (guiItem == null) {
            return;
        }
        if (guiItem.getAction() == null) {
            return;
        }

        switch (guiItem.getAction()) {
            case CONFIRM -> {
                biConsumer.accept(player, true);
                closeInventory();
            }
            case DENY -> {
                biConsumer.accept(player, false);
                closeInventory();
            }
        }
    }

    @Override
    protected void createInventory() {

        inventory = Bukkit.createInventory(null, 27, OrganizationChat.translateColors(title));

        for (int i = 0; i < inventory.getSize(); i++) {
            if (i == 11) {
                createGuiItem(i, new GuiItem(Material.LIME_STAINED_GLASS_PANE, "&a&lVahvista", Action.CONFIRM));
                continue;
            }
            if (i == 15) {
                createGuiItem(i, new GuiItem(Material.RED_STAINED_GLASS_PANE, "&c&lPeru", Action.DENY));
                continue;
            }
            createGuiItem(i, new GuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", null));
        }
    }
}
