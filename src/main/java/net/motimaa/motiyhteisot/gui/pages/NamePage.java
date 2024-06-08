package net.motimaa.motiyhteisot.gui.pages;

import net.motimaa.motiyhteisot.listeners.OrganizationChat;
import net.motimaa.motiyhteisot.organization.Organization;
import net.motimaa.motiyhteisot.organization.Role;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.Collections;

public class NamePage extends AbstractPage {

    private final int MAX_LENGTH = motiYhteisot.getConfig().getInt("role-max-length");
    private final Role role;

    public NamePage(Player player, Role role, Organization organization) {
        super(player, organization);
        this.role = role;
    }

    @Override
    public void openInventory() {

        new AnvilGUI.Builder()

                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    String newName = stateSnapshot.getText();
                    if (newName.length() == 0) {
                        return Collections.emptyList();
                    }

                    if (stateSnapshot.getText().length() > MAX_LENGTH) {
                        newName = stateSnapshot.getText().substring(0, MAX_LENGTH);
                        player.sendMessage(motiYhteisot.prefix + "Nimi on aika pitkä, sitä leikattiin hieman, (max " + MAX_LENGTH + " merkkiä)");
                    }
                    motiYhteisot.getManager().notify(organization, "Roolin§8:§7 " + role.getNameColored() + " §7nimi muutetiin, uusi nimi on: " + OrganizationChat.translateColors(newName));
                    role.setName(newName);
                    new EditRolePage(player, role, organization).openInventory();
                    return Arrays.asList(AnvilGUI.ResponseAction.close());
                })
                .text(role.getName())
                .title("§f§lKirjoita Roolin Nimi")
                .plugin(motiYhteisot)
                .open(player);
    }

    @Override
    public void executeClick(InventoryClickEvent inventoryClickEvent) {
    }

    @Override
    protected void createInventory() {
    }

}
