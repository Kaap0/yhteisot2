package net.motimaa.motiyhteisot.gui.pages;

import net.motimaa.motiyhteisot.Utils;
import net.motimaa.motiyhteisot.gui.items.Action;
import net.motimaa.motiyhteisot.gui.items.GuiItem;
import net.motimaa.motiyhteisot.listeners.OrganizationChat;
import net.motimaa.motiyhteisot.organization.Organization;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.Arrays;

public class MainPage extends AbstractPage {

    public MainPage(Player player, Organization organization) {
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
            case MANAGEROLES -> new RoleManagementPage(player, organization).openInventory();
            case BANK -> {
                closeInventory();
                player.performCommand("yhteisö pankki");
            }
            case RENAME -> {
                closeInventory();
                player.performCommand("yhteisö uudelleennimeä");
            }
            case BASE -> {
                if (inventoryClickEvent.isLeftClick()) {
                    if (motiYhteisot.getManager().getMember(player).getRole().hasPermission("base")) {
                        closeInventory();
                        player.performCommand("yhteisö päämaja");
                    } else {
                        player.sendMessage(motiYhteisot.prefix + "Roolillasi ei ole oikeutta käyttää päämajaa");
                    }
                } else if (inventoryClickEvent.isRightClick()) {
                    if (inventoryClickEvent.isShiftClick()) {
                        if (motiYhteisot.getManager().getMember(player).getRole().hasPermission("setbase")) {
                            closeInventory();
                            player.performCommand("yhteisö päämaja aseta");
                        } else {
                            closeInventory();
                            player.sendMessage(motiYhteisot.prefix + "Roolillasi ei ole oikeutta asettaa päämajaa");
                        }
                    }
                }
            }
            case EXIT -> closeInventory();
            case PL_LIST -> {
                player.sendMessage(motiYhteisot.prefix + "Yhteisön Jäsenet:");
                player.sendMessage("");
                player.performCommand("yhteisö jäsenet");
                closeInventory();
            }
            case APPLIST -> {
                player.performCommand("yhteisö hakemukset");
                closeInventory();
            }
            case ROLELIST -> {
                player.sendMessage(motiYhteisot.prefix + "Yhteisön Roolit:");
                player.performCommand("yhteisö oikeudet");
                closeInventory();
            }
            case CMDLIST -> {
                player.performCommand("yhteisö gröfa");
                closeInventory();
            }
        }
    }

    @Override
    protected void createInventory() {

        inventory = Bukkit.createInventory(null, 45, OrganizationChat.translateColors("Yhteisön Hallintapaneeli"));

        for (int i = 0; i < inventory.getSize(); i++) {
            if (i == 8) {
                createGuiItem(i, new GuiItem(Material.BARRIER, "&c&lPoistu", Action.EXIT));
                continue;
            }
            if (i == 10) {
                createGuiItem(i, new GuiItem(Material.GLOW_ITEM_FRAME, "&a&lNimi: " + organization.getName(), new ArrayList<>(Arrays.asList(
                        SEPARATOR,
                        "§a★§f Voit muokata nimeä klikkaamalla",
                        SEPARATOR
                )), Action.RENAME));
                continue;
            }
            if (i == 12) {
                createGuiItem(i, new GuiItem(Material.ITEM_FRAME, "&a&lLyhenne: " + organization.getAbbreviation(), new ArrayList<>(Arrays.asList(
                        SEPARATOR,
                        "§a★§f Voit muokata lyhennettä klikkaamalla",
                        SEPARATOR
                )), Action.RENAME));
                continue;
            }
            if (i == 14) {
                createGuiItem(i, new GuiItem(Material.LECTERN, "&a&lPerustettu: " + Utils.epochMillisToReadableDate(organization.getCreated()), new ArrayList<>(Arrays.asList(
                        SEPARATOR,
                        "§a★§f Yhteisö on §9" + (System.currentTimeMillis() - organization.getCreated()) / 86400000L + " §fpäivää vanha",
                        "",
                        "§a★§f Liityit yhteisöön §9" + Utils.epochMillisToReadableDate(motiYhteisot.getManager().getMember(player).getJoined()),
                        "§a★§f Olet ollut yhteisön jäsen §9" + (System.currentTimeMillis() - motiYhteisot.getManager().getMember(player).getJoined()) / 86400000L + " §fpäivää",
                        SEPARATOR
                )), null));
                continue;
            }
            if (i == 16) {
                createGuiItem(i, new GuiItem(Material.RAW_GOLD, "&a&lPankki: " + organization.getBalanceFormatted() + motiYhteisot.getConfig().getString("money-suffix"), new ArrayList<>(Arrays.asList(
                        SEPARATOR,
                        "§a★§f Voit tallettaa tai nostaa rahaa klikkaamalla",
                        SEPARATOR
                )), Action.BANK));
                continue;
            }
            if (i == 20) {
                createGuiItem(i, new GuiItem(Material.BELL, "&a&lYhteisöChat: ", new ArrayList<>(Arrays.asList(
                        SEPARATOR,
                        "§a★§f Puhua YhteisöChattiin kirjoittamalla ! viestisi eteen",
                        "§a★§f Esimerkiksi: !Terve Yhteisöläiset",
                        SEPARATOR
                )), Action.CHAT));
                continue;
            }
            if (i == 22) {
                createGuiItem(i, new GuiItem(Material.COMPASS, "&a&lPäämaja", new ArrayList<>(Arrays.asList(
                        SEPARATOR,
                        "§a★§f Vasen-klikkaa teleportataksesi päämajaan",
                        "§a★§f §cShift Oikea-klikkaa asettaaksesi päämajan",
                        SEPARATOR
                )), Action.BASE));
                continue;
            }
            if (i == 24) {
                createGuiItem(i, new GuiItem(Material.CRAFTING_TABLE, "&a&lRoolien Hallinta: " + organization.getRoles().size() + "/" + RoleManagementPage.MAX_ROLES, new ArrayList<>(Arrays.asList(
                        SEPARATOR,
                        "§fTässä valikossa voit:",
                        SEPARATOR,
                        "  §a★§f Luoda uusia rooleja",
                        "  §a★§f Muokata tai poistaa rooleja",
                        "  §a★§f Antaa jäsenille rooleja",
                        SEPARATOR
                )), Action.MANAGEROLES));
                continue;
            }
            if (i == 28) {
                createGuiItem(i, new GuiItem(Material.PLAYER_HEAD, "&a&lJäsenlista", new ArrayList<>(Arrays.asList(
                        SEPARATOR,
                        "§a★§f Klikkaa nähdäksesi jäsenlistan",
                        SEPARATOR
                )), Action.PL_LIST));
                continue;
            }
            if (i == 30) {
                createGuiItem(i, new GuiItem(Material.PAPER, "&a&lHakemuslista", new ArrayList<>(Arrays.asList(
                        SEPARATOR,
                        "§a★§f Klikkaa nähdäksesi hakemuslistan",
                        SEPARATOR
                )), Action.APPLIST));
                continue;
            }
            if (i == 32) {
                createGuiItem(i, new GuiItem(Material.BOOK, "&a&lRoolien oikeudet", new ArrayList<>(Arrays.asList(
                        SEPARATOR,
                        "§a★§f Klikkaa nähdäksesi roolilistan ja niiden oikeudet",
                        SEPARATOR
                )), Action.ROLELIST));
                continue;
            }
            if (i == 34) {
                createGuiItem(i, new GuiItem(Material.NAME_TAG, "&a&lKomentolista", new ArrayList<>(Arrays.asList(
                        SEPARATOR,
                        "§a★§f Klikkaa nähdäksesi kaikki yhteisökomennot",
                        SEPARATOR
                )), Action.CMDLIST));
                continue;
            }
            createGuiItem(i, new GuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", null));
        }
    }

}
