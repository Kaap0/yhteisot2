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

public class RoleManagementPage extends AbstractPage {

    //CHANGING THIS REQUIRES SOME GUI REWORK
    public static final int MAX_ROLES = 18;

    public RoleManagementPage(Player player, Organization organization) {
        super(player, organization);
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
            case CREATE -> {
                if (organization.getMember(player.getUniqueId()).getRole().hasPermission("createrole")) {
                    if (organization.getRoles().size() < MAX_ROLES) {
                        new EditRolePage(player, null, organization).openInventory();
                    } else {
                        closeInventory();
                        player.sendMessage(motiYhteisot.prefix + "Eiköhän teillä ole jo tarpeeksi rooleja...");
                    }
                    break;

                } else {
                    closeInventory();
                    player.sendMessage(motiYhteisot.prefix + "Roolillasi ei ole oikeutta luoda uusia rooleja!");
                }
            }
            case LISTROLES -> new ListRolesPage(player, organization).openInventory();
            case MAINPAGE -> new MainPage(player, organization).openInventory();
            case HINTGIVE -> {
                player.sendMessage(motiYhteisot.prefix + "Tuohon toimintoon käytä komentoa:§9 /yhteisö roolita <jäsen>");
                closeInventory();
            }
        }
    }

    @Override
    protected void createInventory() {

        inventory = Bukkit.createInventory(null, 27, OrganizationChat.translateColors("Roolien Hallinta"));

        for (int i = 0; i < inventory.getSize(); i++) {
            if (i == 11) {
                createGuiItem(i, new GuiItem(Material.CRAFTING_TABLE, "&a&lLuo uusi rooli", Action.CREATE));
                continue;
            }
            if (i == 13) {
                createGuiItem(i, new GuiItem(Material.FILLED_MAP, "&a&lVaihda Jäsenen Roolia", Action.HINTGIVE));
                continue;
            }
            if (i == 15) {
                createGuiItem(i, new GuiItem(Material.PAPER, "&a&lMuokkaa tai poista rooleja", Action.LISTROLES));
                continue;
            }
            if (i == 8) {
                createGuiItem(i, new GuiItem(Material.BARRIER, "&c&lPoistu", Action.MAINPAGE));
                continue;
            }
            createGuiItem(i, new GuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", null));
        }
    }

}
