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

import java.util.HashMap;
import java.util.List;

public class GiveRolePage extends AbstractPage {

    private final Member giver;
    private final Member target;

    public GiveRolePage(Player player, Organization organization, Member giver, Member target) {
        super(player, organization);
        this.giver = giver;
        this.target = target;
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
                if (giver.getRole().getWeight() != 127) {
                    if (giver.getRole().getWeight() <= target.getRole().getWeight()) {
                        closeInventory();
                        player.sendMessage(motiYhteisot.prefix + "Et voi antaa roolia koska pelaajan roolin arvo on sama tai isompi kuin sinun");
                        break;
                    }
                    if (guiItem.getRole().getWeight() >= giver.getRole().getWeight()) {
                        closeInventory();
                        player.sendMessage(motiYhteisot.prefix + "Et voi antaa roolia koska roolin arvo on sama tai isompi kuin sinun");
                        break;
                    }
                }
                if (giver.equals(target)) {
                    closeInventory();
                    player.sendMessage(motiYhteisot.prefix + "Et voi alentaa arvoasi tuolla roolilla");
                    break;
                }
                target.setRole(guiItem.getRole());
                motiYhteisot.getManager().notify(organization, "Pelaaja §9" + giver.getName() + " §7asetti pelaajan §9" + target.getName() + " §7roolin " + guiItem.getRole().getNameColored());
            }
            case EXIT -> closeInventory();
        }
    }

    @Override
    protected void createInventory() {

        inventory = Bukkit.createInventory(null, 27, OrganizationChat.translateColors("Pelaajan &9" + target.getName() + " rooli on nyt " + target.getRole().getName()));

        HashMap<Role, Byte> roleMap = new HashMap<>();
        for (Role role : organization.getRoles()) {
            roleMap.put(role, role.getWeight());
        }

        List<Role> roles = Utils.sortByValue(roleMap);

        int roleIndex = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            if (Utils.between(i, 0, 7)) {
                createGuiItem(i, new GuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", null));
            } else if (i == 8) {
                createGuiItem(i, new GuiItem(Material.BARRIER, "&c&lPoistu", Action.EXIT));
            } else {
                if (roleIndex < roles.size()) {
                    Role role = roles.get(roleIndex);
                    createGuiItem(i, new GuiItem(Material.PLAYER_HEAD, role.getNameColored(), Action.SELECTROLE, role));
                    roleIndex++;
                } else {
                    createGuiItem(i, new GuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", null));
                }
            }
        }
    }

}
