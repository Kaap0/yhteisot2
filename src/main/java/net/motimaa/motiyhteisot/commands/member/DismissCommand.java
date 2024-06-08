package net.motimaa.motiyhteisot.commands.member;

import net.motimaa.motiyhteisot.commands.SubCommand;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DismissCommand extends SubCommand {

    @Override
    public String getName() {
        return "erota";
    }

    @Override
    public String getDescription() {
        return "Erottaa jäsenen";
    }

    @Override
    public String getSyntax() {
        return "/yhteisö erota <pelaaja>";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void perform(Player player, String[] args) {

        if (args.length != 2) {
            player.sendMessage(motiYhteisot.prefix + getSyntax());
            return;
        }

        Member member = motiYhteisot.getManager().getMember(player);

        if (!motiYhteisot.getManager().isInOrganization(player)) {
            player.sendMessage("Et ole yhteisössä");
            return;
        }

        Organization organization = member.getOrganization();

        if (!member.getRole().hasPermission("dismiss")) {
            player.sendMessage(motiYhteisot.prefix + "Roolillasi ei ole oikeutta erottaa");
            return;
        }

        String stringTarget = args[1];

        Member target = null;

        for (Member loopTarget : organization.getMembers()) {
            if (loopTarget.getName().equalsIgnoreCase(stringTarget)) {
                target = loopTarget;
            }
        }

        if (target != null) {
            if (target.getRole().getWeight() < member.getRole().getWeight()) {
                target.delete();
                motiYhteisot.getManager().notify(organization, "§9" + target.getName() + " §7erotettiin yhteisöstä pelaajan §9" + player.getName() + " §7toimesta");

                Player targetPlayer = Bukkit.getPlayer(target.getUuid());
                if (targetPlayer != null) {
                    targetPlayer.sendMessage(motiYhteisot.prefix + "Sinut erotettiin yhteisöstä §9" + organization.getName());
                }

            } else {
                player.sendMessage(motiYhteisot.prefix + "Et voi erottaa jäsentä koska hänen roolinsa on isompi tai yhtä korkea kuin sinun");

            }
        } else {
            player.sendMessage(motiYhteisot.prefix + "Pelaaja ei ole yhteisössänne");
        }
    }

}
