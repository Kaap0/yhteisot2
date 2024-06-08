package net.motimaa.motiyhteisot.gui.pages;

import net.motimaa.motiyhteisot.gui.items.Action;
import net.motimaa.motiyhteisot.gui.items.GuiItem;
import net.motimaa.motiyhteisot.listeners.OrganizationChat;
import net.motimaa.motiyhteisot.organization.Organization;
import net.motimaa.motiyhteisot.organization.Role;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class WeightPage extends AbstractPage {

    private final Role role;

    public WeightPage(Player player, Role role, Organization organization) {
        super(player, organization);
        this.role = role;
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
            case DOWN -> {
                if (role.getWeight() > 0) {
                    role.setWeight((byte) (role.getWeight() - 1));
                }
                new WeightPage(player, role, organization).openInventory();
            }
            case UP -> {
                if (role.getWeight() < 126) {
                    role.setWeight((byte) (role.getWeight() + 1));
                }
                new WeightPage(player, role, organization).openInventory();
            }
            case MODIFY -> new EditRolePage(player, role, organization).openInventory();
        }

    }

    @Override
    protected void createInventory() {

        inventory = Bukkit.createInventory(null, 27, OrganizationChat.translateColors(role.getName() + " &rarvo: " + role.getWeight()));

        for (int i = 0; i < inventory.getSize(); i++) {
            if (i == 4) {
                createGuiItem(i, new GuiItem(Material.DIAMOND, "&a&lArvo &f" + String.valueOf(role.getWeight()), null));
                continue;
            }
            if (i == 11) {
                createGuiItem(i, new GuiItem(Material.RED_WOOL, "&c&lVähennä arvoa", Action.DOWN));
                continue;
            }
            if (i == 15) {
                createGuiItem(i, new GuiItem(Material.LIME_WOOL, "&a&lNosta arvoa", Action.UP));
                continue;
            }
            if (i == 8) {
                createGuiItem(i, new GuiItem(Material.BARRIER, "&c&lPoistu", Action.MODIFY));
                continue;
            }
            inventory.setItem(i, new GuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", null).toItemStack());
        }
    }

}
