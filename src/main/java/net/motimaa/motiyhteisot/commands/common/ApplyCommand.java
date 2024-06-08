package net.motimaa.motiyhteisot.commands.common;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.motimaa.motiyhteisot.commands.SubCommand;
import net.motimaa.motiyhteisot.organization.Application;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ApplyCommand extends SubCommand {

    @Override
    public String getName() {
        return "hae";
    }

    @Override
    public String getDescription() {
        return "Lähettää hakemuksen yhteisöön";
    }

    @Override
    public String getSyntax() {
        return "/yhteisö hae <yhteisö> <hakemus>";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void perform(Player player, String[] args) {

        if (args.length < 3) {
            player.sendMessage(motiYhteisot.prefix + getSyntax());
            return;
        }

        Organization organization = motiYhteisot.getManager().getOrganization(args[1]);

        if (organization == null) {
            player.sendMessage(motiYhteisot.prefix + "Yhteisöä ei ole");
            return;
        }

        for (Application application : motiYhteisot.getManager().getApplications()) {
            if (application.getUuid().equals(player.getUniqueId())) {
                if (organization.equals(application.getOrganization())) {
                    player.sendMessage(motiYhteisot.prefix + "Olet jo hakenut tähän yhteisöön");
                    return;
                }
            }
        }

        StringBuilder description = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            description.append(" ").append(args[i]);
        }

        Application application = new Application(player, organization, description.toString(), UUID.randomUUID());
        player.sendMessage(motiYhteisot.prefix + "Hakemus lähetetty yhteisöön §9" + application.getOrganization().getName());
        motiYhteisot.getManager().notify(organization, "Vastaanotitte hakemuksen pelaajalta §9" + player.getName());
        motiYhteisot.getManager().notify(organization, application.getDescription());

        for (Member member : organization.getMembers()) {
            Player orgPlayer = Bukkit.getPlayer(member.getUuid());
            if (orgPlayer != null) {
                TextComponent msg = new TextComponent("");
                TextComponent accept = new TextComponent("   §a[Hyväksy]");
                TextComponent deny = new TextComponent("   §c[Hylkää]");

                accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö hakemus hyväksy " + application.getName()));
                accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§oKlikkaa hyväksyäksesi hakemuksen").create()));

                deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö hakemus hylkää " + application.getName()));
                deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§oKlikkaa hylkääksesi hakemuksen").create()));

                msg.addExtra(accept);
                msg.addExtra(deny);


                orgPlayer.spigot().sendMessage(msg);
            }
        }
    }
}
