package net.motimaa.motiyhteisot.commands.member;

import net.motimaa.motiyhteisot.commands.SubCommand;
import net.motimaa.motiyhteisot.gui.pages.GiveRolePage;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import org.bukkit.entity.Player;

public class RoleCommand extends SubCommand {
    @Override
    public String getName() {
        return "roolita";
    }

    @Override
    public String getDescription() {
        return "Asettaa pelaajalle roolin";
    }

    @Override
    public String getSyntax() {
        return "/yhteisö roolita <pelaaja>";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void perform(Player player, String[] args) {

        if (args.length == 2) {
            if (!motiYhteisot.getManager().isInOrganization(player)) {
                player.sendMessage(motiYhteisot.prefix + "Et ole yhteisössä");
                return;
            }

            Organization organization = motiYhteisot.getManager().getMember(player).getOrganization();

            Member giver = organization.getMember(player.getUniqueId());
            Member target = organization.getMember(args[1]);

            if (target == null) {
                player.sendMessage(motiYhteisot.prefix + "Pelaaja ei ole yhteisössänne");
                return;
            }
            if (!giver.getRole().hasPermission("giverole")) {
                player.sendMessage(motiYhteisot.prefix + "Sinun roolillasi ei ole oikeutta antaa rooleja");
                return;
            }
            new GiveRolePage(player, organization, giver, target).openInventory();
        } else {
            player.sendMessage(motiYhteisot.prefix + getSyntax());
        }
    }

}
