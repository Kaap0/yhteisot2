package net.motimaa.motiyhteisot.commands.common;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.motimaa.motiyhteisot.MotiYhteisot;
import net.motimaa.motiyhteisot.commands.SubCommand;
import net.motimaa.motiyhteisot.organization.Organization;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;

import static net.motimaa.motiyhteisot.commands.common.ListCommand.Type.*;


public class ListCommand extends SubCommand {

    @Override
    public String getName() {
        return "lista";
    }

    @Override
    public String getDescription() {
        return "Näyttää listan yhteisöistä";
    }

    @Override
    public String getSyntax() {
        return "/yhteisö lista";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void perform(Player player, String[] args) {

        if (args.length == 1) {
            TextComponent order = new TextComponent(motiYhteisot.prefix + "Klikkaa järjestyksestä§8: ");

            TextComponent richest = new TextComponent("§9§lRikkain,  ");
            richest.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö lista richest 1"));

            TextComponent biggest = new TextComponent("§9§lIsoin,  ");
            biggest.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö lista biggest 1"));

            TextComponent wins = new TextComponent("§9§lVoitot");
            wins.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö lista wins 1"));

            order.addExtra(richest);
            order.addExtra(biggest);
            order.addExtra(wins);

            player.spigot().sendMessage(order);

        } else if (args.length == 3) {

            int page;

            try {
                page = Integer.parseInt(args[2]);
            } catch (NumberFormatException nfe) {
                player.sendMessage(motiYhteisot.prefix + "Virheellinen sivu");
                return;
            }

            if (args[1].equalsIgnoreCase("richest")) {
                showTop(player, RICHEST, page, motiYhteisot);
            } else if (args[1].equalsIgnoreCase("biggest")) {
                showTop(player, BIGGEST, page, motiYhteisot);
            } else if (args[1].equalsIgnoreCase("wins")) {
                showTop(player, WINS, page, motiYhteisot);
            }

        } else {
            player.sendMessage(motiYhteisot.prefix + getSyntax());
        }

    }

    private void showTop(Player player, Type type, int page, MotiYhteisot motiYhteisot) {

        final int showAmount = 10;

        String moneysuffix = motiYhteisot.getConfig().getString("money-suffix");

        List<Organization> yhteisot = motiYhteisot.getManager().getOrganizations();

        if (type.equals(BIGGEST)) {
            yhteisot.sort(Comparator.comparingInt(Organization::getMemberAmount).reversed());
        } else if (type.equals(RICHEST)) {
            yhteisot.sort(Comparator.comparingDouble(Organization::getBalance).reversed());
        } else if (type.equals(WINS)) {
            yhteisot.sort(Comparator.comparingInt(Organization::getWins).reversed());
        }


        if (yhteisot.isEmpty()) {
            player.sendMessage(motiYhteisot.prefix + "Tyhjää täynnä");
            return;
        }
        if (page <= 0) {
            player.sendMessage(motiYhteisot.prefix + "Tuollaista sivua ei ole olemassakaan");
            return;
        }

        int pagesAmount = (int) Math.ceil((float) yhteisot.size() / showAmount);

        if (page > pagesAmount) {
            return;
        }

        if (type.equals(RICHEST)) {
            player.sendMessage(motiYhteisot.prefix + "Järjestys§8: §9Rikkain");
        } else if (type.equals(BIGGEST)) {
            player.sendMessage(motiYhteisot.prefix + "Järjestys§8: §9Isoin");
        } else if (type.equals(WINS)) {
            player.sendMessage(motiYhteisot.prefix + "Järjestys§8: §9Voitot");
        }

        for (int i = 0; i < showAmount; ) {

            if (i + (showAmount * (page - 1)) >= yhteisot.size()) {
                break;
            }

            Organization organization = yhteisot.get(i + (showAmount * (page - 1)));
            TextComponent org = null;

            if (type.equals(RICHEST)) {
                org = new TextComponent("§8§l" + (yhteisot.indexOf(organization) + 1) + ". §9" + organization.getName() + " §7" + organization.getBalanceFormatted() + moneysuffix);

            } else if (type.equals(BIGGEST)) {
                org = new TextComponent("§8§l" + (yhteisot.indexOf(organization) + 1) + ". §9" + organization.getName() + " §7" + organization.getMembers().size() + " Jäsentä");

            } else if (type.equals(WINS)) {
                org = new TextComponent("§8§l" + (yhteisot.indexOf(organization) + 1) + ". §9" + organization.getName() + " §7" + organization.getWins() + " Voittoa");

            }
            org.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö tiedot " + organization.getName()));
            org.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§oPaina nähdäksesi yhteisön tiedot").create()));
            player.spigot().sendMessage(org);
            i++;
        }

        TextComponent base = new TextComponent("");

        TextComponent prevPage = new TextComponent("§8§l◀ ");
        prevPage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§oEdellinen sivu").create()));

        TextComponent currPage = new TextComponent("§7§lSivu: §9§l" + page + "/" + pagesAmount + " ");

        TextComponent nextPage = new TextComponent("§8§l▶ ");
        nextPage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§oSeuraava sivu").create()));

        if (type.equals(RICHEST)) {
            prevPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö lista richest " + (page - 1)));
            nextPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö lista richest " + (page + 1)));
        } else if (type.equals(BIGGEST)) {
            prevPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö lista biggest " + (page - 1)));
            nextPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö lista biggest " + (page + 1)));
        } else if (type.equals(WINS)) {
            prevPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö lista wins " + (page - 1)));
            nextPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö lista wins " + (page + 1)));
        }

        base.addExtra(prevPage);

        base.addExtra(currPage);

        base.addExtra(nextPage);

        player.spigot().sendMessage(base);


    }

    protected enum Type {
        RICHEST, BIGGEST, WINS
    }


}

