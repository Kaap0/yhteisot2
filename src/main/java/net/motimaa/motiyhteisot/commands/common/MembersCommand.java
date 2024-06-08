package net.motimaa.motiyhteisot.commands.common;

import net.motimaa.motiyhteisot.Utils;
import net.motimaa.motiyhteisot.commands.SubCommand;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import net.motimaa.motiyhteisot.organization.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class MembersCommand extends SubCommand {

    @Override
    public String getName() {
        return "jäsenet";
    }

    @Override
    public String getDescription() {
        return "Näyttää yhteisön jäsenet ja niiden roolit";
    }

    @Override
    public String getSyntax() {
        return "/yhteisö jäsenet <yhteisö>";
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public void perform(Player player, String[] args) {

        if (args.length == 1) {
            if (!motiYhteisot.getManager().isInOrganization(player)) {
                player.sendMessage(motiYhteisot.prefix + "Et ole yhteisössä, käytä: " + getSyntax());
                return;
            }
            Member member = motiYhteisot.getManager().getMember(player);
            Organization organization = member.getOrganization();
            sendRolesAndMembersList(player, organization);
            return;

        }
        if (args.length == 2) {
            Organization organization = motiYhteisot.getManager().getOrganization(args[1]);
            if (organization == null) {
                player.sendMessage(motiYhteisot.prefix + "Tuollaista yhteisöä ei ole olemassakaan");
                return;
            }
            sendRolesAndMembersList(player, organization);
        } else {
            player.sendMessage(motiYhteisot.prefix + getSyntax());
        }
    }

    private void sendRolesAndMembersList(Player player, Organization organization) {

        HashMap<Role, Byte> weightMap = new HashMap<>();

        for (Role role : organization.getRoles()) {
            weightMap.put(role, role.getWeight());
        }

        ArrayList<Role> sortedRoles = Utils.sortByValue(weightMap);

        for (Role role : sortedRoles) {
            player.sendMessage(role.getNameColored() + ":");
            HashMap<Member, Long> joinedMap = new HashMap<>();
            for (Member member : organization.getMembers()) {
                if (member.getRole().equals(role)) {
                    joinedMap.put(member, member.getJoined());
                }
            }
            ArrayList<Member> sortedMembers = Utils.sortByValue(joinedMap);
            ArrayList<String> sortedMembersWithOnlineStatus = new ArrayList<>();
            for (Member member : sortedMembers) {
                Player onlinePlayer = Bukkit.getPlayer(member.getUuid());
                if (onlinePlayer != null) {
                    sortedMembersWithOnlineStatus.add("§a" + member.getName());
                } else {
                    sortedMembersWithOnlineStatus.add("§c" + member.getName());
                }
            }
            if (sortedMembersWithOnlineStatus.isEmpty()) {
                player.sendMessage("§7  Tyhjää täynnä");
                player.sendMessage("");
            } else {
                player.sendMessage("  " + sortedMembersWithOnlineStatus.toString().replace("[", "").replace("]", ""));
                player.sendMessage("");
            }
        }
    }
}
