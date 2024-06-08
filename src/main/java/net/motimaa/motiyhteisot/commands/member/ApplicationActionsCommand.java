package net.motimaa.motiyhteisot.commands.member;

import net.motimaa.motiyhteisot.commands.SubCommand;
import net.motimaa.motiyhteisot.organization.Application;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ApplicationActionsCommand extends SubCommand {

    @Override
    public String getName() {
        return "hakemus";
    }

    @Override
    public String getDescription() {
        return "Hyväksyy, hylkää tai peruu hakemuksen";
    }

    @Override
    public String getSyntax() {
        return "/yhteisö hakemus <hyväksy/hylkää/peru> <nimi/yhteisö>";
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public void perform(Player player, String[] args) {

        if (args.length != 3) {
            player.sendMessage(motiYhteisot.prefix + getSyntax());
            return;
        }

        if (args[1].equalsIgnoreCase("peru")) {
            String stringOrg = args[2];
            for (Application loopApp : motiYhteisot.getManager().getApplications()) {
                if (loopApp.getUuid().equals(player.getUniqueId())) {
                    if (loopApp.getOrganization().getName().equalsIgnoreCase(stringOrg)) {
                        motiYhteisot.getManager().notify(loopApp.getOrganization(), "Pelaaja §9" + loopApp.getName() + " §7perui hakemuksen yhteisöönne");
                        player.sendMessage(motiYhteisot.prefix + "Peruit hakemuksen yhteisöön §9" + loopApp.getOrganization().getName());
                        loopApp.delete();
                        return;
                    }
                }
            }
            player.sendMessage(motiYhteisot.prefix + "Et ole hakenut kyseiseen yhteisöön");
            return;
        }

        Member member = motiYhteisot.getManager().getMember(player);

        if (member != null) {

            Organization organization = motiYhteisot.getManager().getMember(player).getOrganization();
            Application application = null;


            if (organization != null) {

                for (Application loopApp : motiYhteisot.getManager().getApplications()) {
                    if (loopApp.getName().equalsIgnoreCase(args[2])) {
                        application = loopApp;
                    }
                }
                if (application == null) {
                    player.sendMessage(motiYhteisot.prefix + "Pelaaja ei ole hakenut yhteisöönne");
                    return;
                }

                if (args[1].equalsIgnoreCase("hyväksy")) {
                    if (!motiYhteisot.getManager().getMember(player).getRole().hasPermission("accept")) {
                        player.sendMessage(motiYhteisot.prefix + "Sinun roolillasi ei ole oikeutta hyväksyä hakemuksia");
                        return;
                    }

                    Member applicant = null;
                    if (motiYhteisot.getManager().isInOrganization(application.getUuid())) {
                        Member occupiedMember = motiYhteisot.getManager().getMember(application.getUuid());
                        applicant = occupiedMember;
                        if (motiYhteisot.getManager().isNotSafeToLeave(occupiedMember, occupiedMember.getOrganization())) {
                            player.sendMessage(motiYhteisot.prefix + "Et voi hyväksyä hakemusta koska hakijan yhteisö olisi muuten ilman johtajaa");
                            return;
                        }
                    }
                    if (applicant != null) {
                        motiYhteisot.getManager().notify(applicant.getOrganization(), "Pelaaja §9" + application.getName() + " §7poistui yhteisöstänne ja liittyi yhteisöön §9" + application.getOrganization().getName());
                        applicant.delete();
                    }

                    new Member(application.getUuid(), application.getName(), organization, motiYhteisot.getManager().getSmallestRole(organization), System.currentTimeMillis()).save();

                    motiYhteisot.getManager().notify(organization, "Pelaajan §9" + application.getName() + "§7 hakemus hyväksyttiin pelaajan §9" + player.getName() + "§7 toimesta!");
                    Player beginnerPlayer = Bukkit.getPlayer(application.getUuid());
                    if (beginnerPlayer != null) {
                        beginnerPlayer.sendMessage(motiYhteisot.prefix + "Hakemuksesi hyväksyttiin yhteisössä §9" + organization.getName());
                    }
                    application.delete();

                } else if (args[1].equalsIgnoreCase("hylkää")) {
                    if (!motiYhteisot.getManager().getMember(player).getRole().hasPermission("deny")) {
                        player.sendMessage(motiYhteisot.prefix + "Sinun roolillasi ei ole oikeutta hylätä hakemuksia");
                        return;
                    }

                    motiYhteisot.getManager().notify(organization, "Pelaajan §9" + application.getName() + " §7hakemus hylättiin yhteisöönne pelaajan §9" + player.getName() + " §7toimesta!");
                    Player applicant = Bukkit.getPlayer(application.getUuid());
                    if (applicant != null) {
                        applicant.sendMessage(motiYhteisot.prefix + "Hakemuksesi hylättiin yhteisössä §9" + organization.getName());
                    }
                    application.delete();
                }
            }
        } else {
            player.sendMessage(motiYhteisot.prefix + getSyntax());
        }
    }

}
