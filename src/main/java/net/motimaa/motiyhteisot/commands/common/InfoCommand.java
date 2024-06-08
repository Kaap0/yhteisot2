package net.motimaa.motiyhteisot.commands.common;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.motimaa.motiyhteisot.Utils;
import net.motimaa.motiyhteisot.commands.SubCommand;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import org.bukkit.entity.Player;

public class InfoCommand extends SubCommand {

    @Override
    public String getName() {
        return "tiedot";
    }

    @Override
    public String getDescription() {
        return "Näyttää tiedot yhteisöstä";
    }

    @Override
    public String getSyntax() {
        return "/yhteisö tiedot (yhteisö)";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void perform(Player player, String[] args) {

        if (args.length == 1) {

            Member member = motiYhteisot.getManager().getMember(player);

            if (member == null) {
                player.sendMessage(motiYhteisot.prefix + "Et ole yhteisössä. Tarkastellaksesi muita yhteisöjä käytä: " + getSyntax());
                return;
            }
            sendInfo(member.getOrganization(), player);
        } else if (args.length == 2) {
            Organization organization = motiYhteisot.getManager().getOrganization(args[1]);
            if (organization == null) {
                player.sendMessage(motiYhteisot.prefix + "Yhteisöä ei ole olemassa");
                return;
            }
            sendInfo(organization, player);
        } else {
            player.sendMessage(motiYhteisot.prefix + getSyntax());
        }

    }

    private void sendInfo(Organization organization, Player player) {

        String moneysuffix = motiYhteisot.getConfig().getString("money-suffix");

        player.sendMessage(motiYhteisot.prefix);
        player.sendMessage("");
        player.sendMessage("§7Nimi: §9" + organization.getName());
        player.sendMessage("§7Lyhenne: §9" + organization.getAbbreviation());
        player.sendMessage("§7Perustettu: §9" + Utils.epochMillisToReadableDate(organization.getCreated()));
        player.sendMessage("§7Pankki: §9" + organization.getBalanceFormatted() + moneysuffix);
        player.sendMessage("");

        player.sendMessage("");
        TextComponent members = new TextComponent("§9§lJäsenlista");
        members.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö jäsenet " + organization.getName()));
        members.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§oPaina nähdäksesi yhteisön jäsenlista").create()));
        player.spigot().sendMessage(members);

        player.sendMessage("");
        TextComponent permissions = new TextComponent("§9§lRoolien oikeudet");
        permissions.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö oikeudet " + organization.getName()));
        permissions.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§oPaina nähdäksesi yhteisön roolien oikeudet").create()));
        player.spigot().sendMessage(permissions);
    }
}
