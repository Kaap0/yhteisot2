package net.motimaa.motiyhteisot.commands.member;

import net.motimaa.motiyhteisot.MotiYhteisot;
import net.motimaa.motiyhteisot.commands.SubCommand;
import net.motimaa.motiyhteisot.gui.pages.ConfirmPage;
import net.motimaa.motiyhteisot.organization.Application;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import net.motimaa.motiyhteisot.organization.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class DeleteCommand extends SubCommand {

    @Override
    public String getName() {
        return "poista";
    }

    @Override
    public String getDescription() {
        return "Poistaa yhteisön";
    }

    @Override
    public String getSyntax() {
        return "/yhteisö poista";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void perform(Player player, String[] args) {

        Organization organization;

        if (args.length == 1) {
            if (!motiYhteisot.getManager().isInOrganization(player)) {
                player.sendMessage(motiYhteisot.prefix + "Et ole yhteisössä");
                return;
            }
            Member member = motiYhteisot.getManager().getMember(player);
            Role role = member.getRole();
            organization = member.getOrganization();

            if (role.hasPermission("delete")) {
                delete(player, motiYhteisot, organization);
            } else {
                player.sendMessage(motiYhteisot.prefix + "Sinun roolillasi ei ole oikeutta poistaa yhteisöä");
            }
            return;
        }
        if (args.length == 2) {
            if (!player.hasPermission("motimaa.mod")) {
                player.sendMessage(motiYhteisot.prefix + "Et voi poistaa toista yhteisöä, käytä: §9" + getSyntax());
                return;
            }
            organization = motiYhteisot.getManager().getOrganization(args[1]);
            if (organization == null) {
                player.sendMessage(motiYhteisot.prefix + "Yhteisöä ei ole olemassa");
                return;
            }
            delete(player, motiYhteisot, organization);
        } else {
            player.sendMessage(motiYhteisot.prefix + getSyntax());
        }
    }

    private void delete(Player player, MotiYhteisot motiYhteisot, Organization organization) {

        new ConfirmPage(player, organization, "&f&lVahvista yhteisön " + organization.getName() + " poistaminen", (p, bool) -> {
            if (bool) {
                Bukkit.broadcastMessage(motiYhteisot.prefix + "Yhteisö §9" + organization.getName() + " §7poistettiin");

                ArrayList<Role> roles = new ArrayList<>(organization.getRoles());
                ArrayList<Member> members = new ArrayList<>(organization.getMembers());
                ArrayList<Application> applications = new ArrayList<>(motiYhteisot.getManager().getApplications());

                for (Role loopRole : roles) {
                    motiYhteisot.getManager().getRole(loopRole.getRoleID()).delete();
                }
                for (Member loopMember : members) {
                    motiYhteisot.getManager().getMember(loopMember.getUuid()).delete();
                }

                for (Application loopApplication : applications) {
                    if (loopApplication.getOrganization().equals(organization)) {
                        Player applicant = Bukkit.getPlayer(loopApplication.getUuid());
                        if (applicant != null) {
                            applicant.sendMessage(motiYhteisot.prefix + "Hakemuksesi yhteisöön §9" + organization.getName() + " §7hylättiin koska yhteisö poistettiin");
                        }
                        motiYhteisot.getManager().getApplication(loopApplication.getApplicationID()).delete();
                    }
                }
                organization.delete();
            }
        }).openInventory();

    }
}
