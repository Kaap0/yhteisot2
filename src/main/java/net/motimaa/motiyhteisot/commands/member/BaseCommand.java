package net.motimaa.motiyhteisot.commands.member;

import net.motimaa.motiyhteisot.commands.SubCommand;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import org.bukkit.entity.Player;

public class BaseCommand extends SubCommand {

    @Override
    public String getName() {
        return "päämaja";
    }

    @Override
    public String getDescription() {
        return "Teleporttaa tai asettaa yhteisön päämajan";
    }

    @Override
    public String getSyntax() {
        return "/yhteisö päämaja (aseta)";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void perform(Player player, String[] args) {

        if (!motiYhteisot.getManager().isInOrganization(player)) {
            player.sendMessage(motiYhteisot.prefix + "Et ole yhteisössä");
            return;
        }

        Member member = motiYhteisot.getManager().getMember(player);

        Organization organization = member.getOrganization();

        if (args.length == 1) {
            if (!member.getRole().hasPermission("base")) {
                player.sendMessage(motiYhteisot.prefix + "Roolillasi ei ole oikeutta käyttää päämajaa");
                return;
            }
            if (organization.getBase() == null) {
                player.sendMessage(motiYhteisot.prefix + "Yhteisöllä ei ole asetettuna päämajaa");
                return;
            }
            player.teleport(organization.getBase());
            player.sendMessage(motiYhteisot.prefix + "Sinut teleportattiin yhteisön päämajaan");
        } else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("aseta")) {
                if (!member.getRole().hasPermission("setbase")) {
                    player.sendMessage(motiYhteisot.prefix + "Roolillasi ei ole oikeutta asettaa päämajaa");
                } else {
                    organization.setBase(player.getLocation());
                    motiYhteisot.getManager().notify(organization, "Yhteisön päämaja asetettiin pelaajan §9" + player.getName() + " §7sijaintiin");
                }

            }
        } else {
            player.sendMessage(motiYhteisot.prefix + getSyntax());
        }

    }

}
