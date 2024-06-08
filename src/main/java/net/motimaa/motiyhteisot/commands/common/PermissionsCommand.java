package net.motimaa.motiyhteisot.commands.common;

import net.motimaa.motiyhteisot.Utils;
import net.motimaa.motiyhteisot.commands.SubCommand;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import net.motimaa.motiyhteisot.organization.Permissions;
import net.motimaa.motiyhteisot.organization.Role;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class PermissionsCommand extends SubCommand {

    @Override
    public String getName() {
        return "oikeudet";
    }

    @Override
    public String getDescription() {
        return "Näyttää yhteisön roolien oikeudet";
    }

    @Override
    public String getSyntax() {
        return "/yhteisö oikeudet <yhteisö>";
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
            sendPermissions(player, organization);
            return;

        }
        if (args.length == 2) {
            Organization organization = motiYhteisot.getManager().getOrganization(args[1]);
            if (organization == null) {
                player.sendMessage(motiYhteisot.prefix + "Tuollaista yhteisöä ei ole olemassakaan");
                return;
            }
            sendPermissions(player, organization);
        } else {
            player.sendMessage(motiYhteisot.prefix + getSyntax());
        }
    }
    private void sendPermissions(Player player, Organization organization) {
        player.sendMessage("");

        HashMap<Role, Byte> weightMap = new HashMap<>();

        for (Role role : organization.getRoles()) {
            weightMap.put(role, role.getWeight());
        }

        ArrayList<Role> sorted = Utils.sortByValue(weightMap);

        for (Role role : sorted) {

            player.sendMessage(role.getNameColored());
            String msg = "";
            for (String permission : role.getPermissions()) {
                String description = Permissions.getDescription(permission);
                if (description != null) {
                    msg = msg.concat("§7" + description + ", ");
                }
            }
            if (msg.isEmpty()) {
                player.sendMessage("§7Ei oikeuksia");
            } else {
                player.sendMessage(msg);
            }
            player.sendMessage("");
        }
    }
}
