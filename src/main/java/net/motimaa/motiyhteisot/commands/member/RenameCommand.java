package net.motimaa.motiyhteisot.commands.member;

import net.motimaa.motiyhteisot.commands.SubCommand;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RenameCommand extends SubCommand {

    @Override
    public String getName() {
        return "uudelleennimeä";
    }

    @Override
    public String getDescription() {
        return "Nimeää uudelleen yhteisön nimen tai lyhenteen";
    }

    @Override
    public String getSyntax() {
        return "/yhteisö uudelleennimeä <nimi/lyhenne> <uusi>";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void perform(Player player, String[] args) {


        int nameMin = motiYhteisot.getConfig().getInt("name-min");
        int nameMax = motiYhteisot.getConfig().getInt("name-max");

        int abbMin = motiYhteisot.getConfig().getInt("abbreviation-min");
        int abbMax = motiYhteisot.getConfig().getInt("abbreviation-max");

        double price = motiYhteisot.getConfig().getDouble("rename-price");
        String moneysuffix = motiYhteisot.getConfig().getString("money-suffix");

        if (!motiYhteisot.getManager().isInOrganization(player)) {
            player.sendMessage(motiYhteisot.prefix + "Et ole yhteisössä");
            return;
        }

        Member member = motiYhteisot.getManager().getMember(player);
        Organization organization = member.getOrganization();

        if (organization.getBalance() < price) {
            player.sendMessage(motiYhteisot.prefix + "Yhteisön tilillä ei ole tarpeeksi rahaa. Uudelleennimeäminen maksaa §9" + price + moneysuffix);
            return;
        }
        if (args.length == 3) {

            if (motiYhteisot.getManager().isReservedName(args[2])) {
                player.sendMessage(motiYhteisot.prefix + "Nimi tai lyhenne on varattu toiselle yhteisölle");
                return;
            }

            if (args[1].equalsIgnoreCase("nimi")) {

                if (!member.getRole().hasPermission("changename")) {
                    player.sendMessage(motiYhteisot.prefix + "Sinun roolillasi ei ole oikeutta uudelleennimetä nimeä");
                    return;
                }

                if (args[2].length() > nameMax || args[2].length() < nameMin) {
                    player.sendMessage(motiYhteisot.prefix + "Yhteisön nimen täytyy olla §9" + nameMin + "-" + nameMax + " §7merkkiä pitkä");
                    return;
                }
                Bukkit.broadcastMessage(motiYhteisot.prefix + "Yhteisö §9" + organization.getName() + " §7uudelleennimettiin, uusi nimi on §9" + args[2]);

                organization.setName(args[2]);
                organization.setBalance(organization.getBalance() - price);

            } else if (args[1].equalsIgnoreCase("lyhenne")) {

                if (!member.getRole().hasPermission("changeabbreviation")) {
                    player.sendMessage(motiYhteisot.prefix + "Sinun roolillasi ei ole oikeutta uudelleennimetä lyhennettä");
                    return;
                }

                if (args[2].length() > abbMax || args[2].length() < abbMin) {
                    player.sendMessage(motiYhteisot.prefix + "Yhteisön lyhenteen täytyy olla §9" + abbMin + "-" + abbMax + " §7merkkiä pitkä");
                    return;
                }
                Bukkit.broadcastMessage(motiYhteisot.prefix + "Yhteisön §9" + organization.getName() + " §7lyhenne uudelleennimettiin, uusi lyhenne on §9" + args[2]);

                organization.setAbbreviation(args[2]);
                organization.setBalance(organization.getBalance() - price);
            }
        } else {
            player.sendMessage(motiYhteisot.prefix + getSyntax());
        }
    }

}
