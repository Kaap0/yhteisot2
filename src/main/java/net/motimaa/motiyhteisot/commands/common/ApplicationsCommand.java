package net.motimaa.motiyhteisot.commands.common;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.motimaa.motiyhteisot.commands.SubCommand;
import net.motimaa.motiyhteisot.organization.Application;
import net.motimaa.motiyhteisot.organization.Member;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ApplicationsCommand extends SubCommand {

    @Override
    public String getName() {
        return "hakemukset";
    }

    @Override
    public String getDescription() {
        return "Näyttää sinun ja yhteisön hakemukset";
    }

    @Override
    public String getSyntax() {
        return "/yhteisö hakemukset";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void perform(Player player, String[] args) {

        if (args.length != 1) {
            player.sendMessage(motiYhteisot.prefix + getSyntax());
            return;
        }

        ArrayList<Application> ownApplications = new ArrayList<>();

        for (Application application : motiYhteisot.getManager().getApplications()) {
            if (application.getUuid().equals(player.getUniqueId())) {
                ownApplications.add(application);
            }
        }

        player.sendMessage(motiYhteisot.prefix + "Sinun hakemukset§8:");
        if (ownApplications.isEmpty()) {
            player.sendMessage("§7Tyhjää täynnä");
        }

        for (Application application : ownApplications) {

            TextComponent org = new TextComponent("§7Yhteisö§8: §9" + application.getOrganization().getName());
            TextComponent cancel = new TextComponent("   §c[Peruuta hakemus]");

            cancel.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö hakemus peru " + application.getOrganization().getName()));
            cancel.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§oKlikkaa peruaksesi hakemuksen").create()));

            org.addExtra(cancel);

            player.spigot().sendMessage(org);
            player.sendMessage("§7Hakemuksesi§8: §9" + application.getDescription());
            player.sendMessage("");

        }
        if (motiYhteisot.getManager().isInOrganization(player)) {
            Member member = motiYhteisot.getManager().getMember(player);

            player.sendMessage(motiYhteisot.prefix + "Yhteisön hakemukset§8: ");
            if (!member.getRole().hasPermission("applist")) {
                player.sendMessage(motiYhteisot.prefix + "Sinun roolillasi ei ole oikeutta katsoa yhteisösi hakemuksia");
                return;
            }

            ArrayList<Application> orgApplications = new ArrayList<>();
            for (Application application : motiYhteisot.getManager().getApplications()) {
                if (application.getOrganization().equals(member.getOrganization())) {
                    orgApplications.add(application);
                }
            }

            if (orgApplications.isEmpty()) {
                player.sendMessage("§7Tyhjää täynnä");
            }

            for (Application application : orgApplications) {

                TextComponent applicant = new TextComponent("§7Hakija: §9" + application.getName());
                TextComponent accept = new TextComponent("   §a[Hyväksy]");
                TextComponent deny = new TextComponent("   §c[Hylkää]");

                accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö hakemus hyväksy " + application.getName()));
                accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§oKlikkaa hyväksyäksesi hakemuksen").create()));

                deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö hakemus hylkää " + application.getName()));
                deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§oKlikkaa hylkääksesi hakemuksen").create()));

                applicant.addExtra(accept);
                applicant.addExtra(deny);


                player.spigot().sendMessage(applicant);
                player.sendMessage("§7Hakemus: §9" + application.getDescription());
                player.sendMessage("");

            }
        }
    }
}
