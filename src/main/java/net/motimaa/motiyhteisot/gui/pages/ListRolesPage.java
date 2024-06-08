package net.motimaa.motiyhteisot.gui.pages;

import net.motimaa.motiyhteisot.Utils;
import net.motimaa.motiyhteisot.gui.items.Action;
import net.motimaa.motiyhteisot.gui.items.GuiItem;
import net.motimaa.motiyhteisot.listeners.OrganizationChat;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import net.motimaa.motiyhteisot.organization.Role;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.Arrays;

public class ListRolesPage extends AbstractPage {

    public ListRolesPage(Player player, Organization organization) {
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
            case SELECTROLE -> {
                if (inventoryClickEvent.isLeftClick()) {
                    if (organization.getMember(player.getUniqueId()).getRole().hasPermission("modifyrole")) {
                        new EditRolePage(player, guiItem.getRole(), organization).openInventory();
                    } else {
                        closeInventory();
                        player.sendMessage(motiYhteisot.prefix + "Roolillasi ei ole oikeutta muokata rooleja");
                    }

                } else if (inventoryClickEvent.isRightClick()) {
                    if (inventoryClickEvent.isShiftClick()) {
                        if (organization.getRoles().size() > 2) {
                            if (guiItem.getRole().getWeight() != 127) {
                                if (organization.getMember(player.getUniqueId()).getRole().hasPermission("modifyrole")) {
                                    new EditRolePage(player, guiItem.getRole(), organization).openInventory();
                                } else {
                                    closeInventory();
                                    player.sendMessage(motiYhteisot.prefix + "Roolillasi ei ole oikeutta poistaa rooleja");
                                    break;
                                }
                                guiItem.getRole().delete();
                                Role smallest = motiYhteisot.getManager().getSmallestRole(organization);
                                for (Member member : organization.getMembers()) {
                                    if (member.getRole().equals(guiItem.getRole())) {
                                        member.setRole(smallest);
                                    }
                                }
                                motiYhteisot.getManager().notify(organization, "Rooli " + guiItem.getRole().getNameColored() + " §7poistettiin, tämän roolin jäsenet asetettiin arvoltaan pienimpään rooliin:§r " + smallest.getNameColored());
                                new ListRolesPage(player, organization).openInventory();
                                break;
                            }
                        }
                        closeInventory();
                        player.sendMessage(motiYhteisot.prefix + "Et voi poistaa tuota roolia koska se ei olisi turvallista");
                    }
                }
            }
            case MANAGEROLES -> new RoleManagementPage(player, organization).openInventory();
        }
    }


    @Override
    protected void createInventory() {

        inventory = Bukkit.createInventory(null, 27, OrganizationChat.translateColors("Rooli Lista"));

        int roleIndex = 0;
        for (int i = 0; i < inventory.getSize(); i++) {

            if (Utils.between(i, 0, 7)) {
                createGuiItem(i, new GuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", null));
            } else if (i == 8) {
                createGuiItem(i, new GuiItem(Material.BARRIER, "&c&lPoistu", Action.MANAGEROLES));
            } else {
                if (roleIndex < organization.getRoles().size()) {
                    Role role = organization.getRoles().get(roleIndex);
                    createGuiItem(i, new GuiItem(Material.PLAYER_HEAD, role.getName(), new ArrayList<>(Arrays.asList(
                            SEPARATOR,
                            "§a★§f Vasen-klikkaa muokkaaksesi roolia",
                            "§a★§f §cShift Oikea-klikkaa poistaaksesi roolin",
                            SEPARATOR
                    )), Action.SELECTROLE, role));
                    roleIndex++;
                } else {
                    createGuiItem(i, new GuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", null));
                }
            }
        }
    }

}
