package net.motimaa.motiyhteisot.gui.pages;

import net.motimaa.motiyhteisot.Utils;
import net.motimaa.motiyhteisot.gui.items.Action;
import net.motimaa.motiyhteisot.gui.items.GuiItem;
import net.motimaa.motiyhteisot.listeners.OrganizationChat;
import net.motimaa.motiyhteisot.organization.Organization;
import net.motimaa.motiyhteisot.organization.Permissions;
import net.motimaa.motiyhteisot.organization.Role;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class PermissionsPage extends AbstractPage {

    private final Role role;

    public PermissionsPage(Player player, Role role, Organization organization) {
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
            case MODIFY -> new EditRolePage(player, role, organization).openInventory();
            case REMOVEPERMISSION -> {
                role.removePermission(guiItem.getPermission());
                new PermissionsPage(player, role, organization).openInventory();
                motiYhteisot.getManager().notify(organization, "Roolilta " + role.getNameColored() + " §7poistettiin oikeus §9" + guiItem.getName());
            }
            case GIVEPERMISSION -> {
                role.addPermission(guiItem.getPermission());
                new PermissionsPage(player, role, organization).openInventory();
                motiYhteisot.getManager().notify(organization, "Roolille " + role.getNameColored() + " §7lisättiin oikeus §9" + guiItem.getName());
            }
        }

    }

    @Override
    protected void createInventory() {

        inventory = Bukkit.createInventory(null, 36, OrganizationChat.translateColors(role.getName() + " &rOikeudet"));

        int permissionIndex = 0;

        for (int i = 0; i < inventory.getSize(); i++) {

            if (Utils.between(i, 0, 7)) {
                inventory.setItem(i, new GuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", null).toItemStack());
                continue;
            }
            if (i == 8) {
                createGuiItem(i, new GuiItem(Material.BARRIER, "&c&lPoistu", Action.MODIFY));
                continue;
            }

            if (permissionIndex < Permissions.allPermissions.size()) {
                String permission = Permissions.allPermissions.get(permissionIndex);
                if (role.hasPermission(permission)) {
                    createGuiItem(i, new GuiItem(Material.LIME_STAINED_GLASS_PANE, Permissions.getDescription(permission), permission, Action.REMOVEPERMISSION));
                } else {
                    createGuiItem(i, new GuiItem(Material.RED_STAINED_GLASS_PANE, Permissions.getDescription(permission), permission, Action.GIVEPERMISSION));
                }
                permissionIndex++;
                continue;

            }
            inventory.setItem(i, new GuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", null).toItemStack());
        }
    }

}
