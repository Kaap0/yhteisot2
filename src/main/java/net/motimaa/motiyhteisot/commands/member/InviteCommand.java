package net.motimaa.motiyhteisot.commands.member;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.motimaa.motiyhteisot.commands.SubCommand;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class InviteCommand extends SubCommand {

    private final HashMap<UUID, Organization> invites = new HashMap<>();

    @Override
    public String getName() {
        return "kutsu";
    }

    @Override
    public String getDescription() {
        return "Kutsuu pelaajan yhteisöön";
    }

    @Override
    public String getSyntax() {
        return "/yhteisö kutsu <pelaaja>";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void perform(Player player, String[] args) {

        if (args.length == 2) {

            if (args[1].equalsIgnoreCase("hyväksy")) {

                if (!invites.containsKey(player.getUniqueId())) {
                    player.sendMessage(motiYhteisot.prefix + "Sinulla ei ole kutsua");
                    return;
                }

                Organization targetOrg = invites.get(player.getUniqueId());

                if (motiYhteisot.getManager().isInOrganization(player)) {
                    Member member = motiYhteisot.getManager().getMember(player);
                    Organization organization = member.getOrganization();
                    if (motiYhteisot.getManager().isNotSafeToLeave(member, organization)) {
                        player.sendMessage(motiYhteisot.prefix + "Et voi poistua yhteisöstäsi koska se ei jäisi ilman johtajaa");
                        motiYhteisot.getManager().notify(targetOrg, "Pelaaja §9" + player.getName() + " §7ei voinut hyväksyä kutsua yhteisöön koska hänen yhteisönsä jäisi ilman johtajaa");
                        invites.remove(player.getUniqueId());
                        return;
                    }
                    if (organization == targetOrg) {
                        player.sendMessage(motiYhteisot.prefix + "Et voi liittyä samaan yhteisöön missä olet!");
                        invites.remove(player.getUniqueId());
                        return;
                    }
                    motiYhteisot.getManager().notify(organization, "Pelaaja §9" + player.getName() + " §7poistui yhteisöstänne ja liittyi yhteisöön §9" + invites.get(player.getUniqueId()).getName());
                    motiYhteisot.getManager().notify(targetOrg, "Pelaaja §9" + player.getName() + "§7 hyväksyi kutsun ja liittyi yhteisöön");

                    member.delete();

                    new Member(player, targetOrg, motiYhteisot.getManager().getSmallestRole(targetOrg));

                    player.sendMessage(motiYhteisot.prefix + "Liityit yhteisöön §9" + invites.get(player.getUniqueId()).getName());

                } else {

                    new Member(player, targetOrg, motiYhteisot.getManager().getSmallestRole(targetOrg));

                    player.sendMessage(motiYhteisot.prefix + "Liityit yhteisöön §9" + targetOrg.getName());
                    motiYhteisot.getManager().notify(targetOrg, "Pelaaja §9" + player.getName() + "§7 hyväksyi kutsun ja liittyi yhteisöön");

                }
                invites.remove(player.getUniqueId());
            } else if (args[1].equalsIgnoreCase("hylkää")) {

                if (!invites.containsKey(player.getUniqueId())) {
                    player.sendMessage(motiYhteisot.prefix + "Sinulla ei ole kutsua");
                    return;
                }

                motiYhteisot.getManager().notify(invites.get(player.getUniqueId()), "Pelaaja §9" + player.getName() + " §7hylkäsi kutsun yhteisöönne");
                player.sendMessage(motiYhteisot.prefix + "Hylkäsit kutsun yhteisöön §9" + invites.get(player.getUniqueId()).getName());
                invites.remove(player.getUniqueId());

            } else {
                Member member = motiYhteisot.getManager().getMember(player);
                if (member == null) {
                    player.sendMessage(motiYhteisot.prefix + "Et ole yhteisössä");
                    return;
                }

                if (!member.getRole().hasPermission("invite")) {
                    player.sendMessage(motiYhteisot.prefix + "Roolillasi ei ole oikeutta kutsua pelaajia");
                    return;
                }

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(motiYhteisot.prefix + "Pelaaja ei ole paikalla");
                    return;
                }

                if (target.equals(player)) {
                    player.sendMessage(motiYhteisot.prefix + "Et voi kutsua itseäsi");
                    return;
                }

                if (invites.containsKey(target.getUniqueId())) {
                    player.sendMessage(motiYhteisot.prefix + "Pelaajalla on jo kutsu, yritä uudelleen hetken päästä");
                    return;
                }

                invites.put(target.getUniqueId(), member.getOrganization());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (invites.containsKey(target.getUniqueId())) {
                            invites.remove(target.getUniqueId());
                            motiYhteisot.getManager().notify(member.getOrganization(), "Pelaajan " + target.getName() + " kutsu aikakatkaistiin");
                            target.sendMessage(motiYhteisot.prefix + "Kutsu yhteisöön aikakatkaistiin");
                        }
                    }
                }.runTaskLater(motiYhteisot, 1200);

                player.sendMessage(motiYhteisot.prefix + "Lähetit kutsun pelaajalle §9" + target.getName());

                TextComponent invite = new TextComponent(motiYhteisot.prefix + "Sinut kutsuttiin yhteisöön §9" + member.getOrganization().getName());

                TextComponent accept = new TextComponent("   §a[Hyväksy]");
                TextComponent deny = new TextComponent("   §c[Hylkää]");

                accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö kutsu hyväksy"));
                accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§oKlikkaa hyväksyäksesi kutsun").create()));

                deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö kutsu hylkää"));
                deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§oKlikkaa hylkääksesi kutsun").create()));

                invite.addExtra(accept);
                invite.addExtra(deny);

                target.spigot().sendMessage(invite);
            }

        } else {
            player.sendMessage(motiYhteisot.prefix + getSyntax());
        }
    }

}
