package net.motimaa.motiyhteisot.commands.member;

import net.motimaa.motiyhteisot.commands.SubCommand;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import org.bukkit.entity.Player;

public class ResignCommand extends SubCommand {

    @Override
    public String getName() {
        return "eroa";
    }

    @Override
    public String getDescription() {
        return "Eroaa yhteisöstä";
    }

    @Override
    public String getSyntax() {
        return "/yhteisö eroa";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void perform(Player player, String[] args) {

        if (args.length != 1) {
            player.sendMessage(getSyntax());
            return;
        }

        Member member = motiYhteisot.getManager().getMember(player);

        if (!motiYhteisot.getManager().isInOrganization(player)) {
            player.sendMessage("Et ole yhteisössä");
            return;
        }

        Organization organization = member.getOrganization();

        if (motiYhteisot.getManager().isNotSafeToLeave(member, organization)) {
            player.sendMessage(motiYhteisot.prefix + "Et voi lähteä yhteisöstä koska se jäisi ilman johtajaa");
            return;
        }

        motiYhteisot.getManager().notify(organization, "§9" + member.getName() + " §7erosi yhteisöstä");
        player.sendMessage(motiYhteisot.prefix + "Erosit yhteisöstä §9" + organization.getName());
        member.delete();
    }

}
