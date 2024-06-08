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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class EditRolePage extends AbstractPage {

    private final Role role;

    public EditRolePage(Player player, Role role, Organization organization) {
        super(player, organization);
        this.organization = organization;
        if (role == null) {
            this.role = new Role(UUID.randomUUID(), organization, false);
            motiYhteisot.getManager().notify(organization, "Uusi rooli luotiin yhteisöön");
        } else {
            this.role = role;
        }
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
            case MANAGEROLES -> new RoleManagementPage(player, organization).openInventory();
            case NAME -> new NamePage(player, role, organization).openInventory();
            case WEIGHT -> {
                if (role.getWeight() == 127) {
                    closeInventory();
                    player.sendMessage(motiYhteisot.prefix + "Tämän roolin arvoa ei voi muuttaa!");
                } else {
                    new WeightPage(player, role, organization).openInventory();
                }
            }
            case PERMISSIONS -> {
                if (role.getWeight() == 127) {
                    closeInventory();
                    player.sendMessage(motiYhteisot.prefix + "Tämän roolin oikeuksia ei voi muuttaa!");
                } else {
                    new PermissionsPage(player, role, organization).openInventory();
                }
            }
        }
    }

    @Override
    protected void createInventory() {

        inventory = Bukkit.createInventory(null, 27, OrganizationChat.translateColors(role.getName() + " &rMuokkaaminen"));

        for (int i = 0; i < inventory.getSize(); i++) {
            if (i == 10) {
                createGuiItem(i, new GuiItem(Material.OAK_SIGN, "&a&lNimi", new ArrayList<>(Arrays.asList(
                        SEPARATOR,
                        "§fVoit saada värejä roolin nimeen.",
                        SEPARATOR,
                        "  §a★§f Käytä & värikoodia",
                        "  §a★§f Esimerkiksi kirjoita: &4Johtaja saat §4Johtaja",
                        "  §a★§f §0&0§r, §1&1§r, §2&2§r, §3&3§r, §4&4§r, §5&5§r, §6&6§r, §7&7§r, §8&8§r, §9&9§r",
                        "  §a★§f §a&a§r, §b&b§r, §c&c§r, §d&d§r, §e&e§r, §f&f§r,",
                        "  §a★§f &l §lLihavoi§f, &n §nAlleviivaa§f, &o §oItalic§f, &m §mYliviivaa§f, &k §kMagik, §r§f&r resetoi värin",
                        SEPARATOR,
                        "  §4★§c Voit käyttää myös §lRGB§r§c Värikoodeja! §fEsimerkiksi: <#AA0000>Johtaja saat §4Johtaja",
                        SEPARATOR

                )), Action.NAME));
                continue;
            }
            if (i == 13) {
                createGuiItem(i, new GuiItem(Material.DIAMOND, "&a&lArvo " + String.valueOf(role.getWeight()), Action.WEIGHT));
                continue;
            }
            if (i == 16) {
                createGuiItem(i, new GuiItem(Material.BOOK, "&a&lOikeudet", new ArrayList<>(Arrays.asList(
                        SEPARATOR,
                        "§fRoolien oikeudet",
                        SEPARATOR,
                        "  §a★§f Jokaisella roolilla on omat oikeudet ",
                        "  §a★§f ja ne §neivät§f periydy arvojärjestyksen mukaan",
                        SEPARATOR
                )), Action.PERMISSIONS));
                continue;
            }
            if (i == 8) {
                createGuiItem(i, new GuiItem(Material.BARRIER, "&c&lPoistu", Action.MANAGEROLES));
                continue;
            }
            inventory.setItem(i, new GuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", null).toItemStack());
        }
    }

}
