package net.motimaa.motiyhteisot.commands.common;

import net.milkbowl.vault.economy.EconomyResponse;
import net.motimaa.motiyhteisot.MotiYhteisot;
import net.motimaa.motiyhteisot.commands.SubCommand;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CreateCommand extends SubCommand {

    @Override
    public String getName() {
        return "luo";
    }

    @Override
    public String getDescription() {
        return "Luo uuden yhteisön, hinta: " + MotiYhteisot.getInstance().getConfig().getDouble("price") + MotiYhteisot.getInstance().getConfig().getString("money-suffix");
    }

    @Override
    public String getSyntax() {
        return "/yhteisö luo <nimi> <lyhenne>";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void perform(Player player, String[] args) {

        if (args.length != 3) {
            player.sendMessage(motiYhteisot.prefix + getSyntax());
            return;
        }

        Member member = null;
        int nameMin = motiYhteisot.getConfig().getInt("name-min");
        int nameMax = motiYhteisot.getConfig().getInt("name-max");

        int abbMin = motiYhteisot.getConfig().getInt("abbreviation-min");
        int abbMax = motiYhteisot.getConfig().getInt("abbreviation-max");

        double price = motiYhteisot.getConfig().getDouble("price");
        double playerMoney = MotiYhteisot.getEconomy().getBalance(player);
        String moneysuffix = motiYhteisot.getConfig().getString("money-suffix");

        if (args[1].length() > nameMax || args[1].length() < nameMin || args[2].length() > abbMax || args[2].length() < abbMin) {
            player.sendMessage(motiYhteisot.prefix + "Yhteisön nimen täytyy olla §9" + nameMin + "-" + nameMax + " §7ja lyhenteen §9" + abbMin + "-" + abbMax + " §7merkkiä pitkä");
            return;
        }
        if (motiYhteisot.getManager().isReservedName(args[1]) || motiYhteisot.getManager().isReservedName(args[2])) {
            player.sendMessage(motiYhteisot.prefix + "Nimi tai lyhenne on varattu toiselle yhteisölle");
            return;
        }
        if (playerMoney < price) {
            player.sendMessage(motiYhteisot.prefix + "Sinulla ei ole tarpeeksi rahaa. Yhteisön luominen maksaa §9" + price + moneysuffix);
            return;
        }
        if (motiYhteisot.getManager().isInOrganization(player)) {
            member = motiYhteisot.getManager().getMember(player);
            if (motiYhteisot.getManager().isNotSafeToLeave(member, member.getOrganization())) {
                player.sendMessage(motiYhteisot.prefix + "Et voi luoda uutta yhteisöä koska nykyinen yhteisösi olisi muuten ilman johtajaa");
                return;
            }
        }
        EconomyResponse er = MotiYhteisot.getEconomy().withdrawPlayer(player, price);
        if (!er.transactionSuccess()) {
            player.sendMessage(motiYhteisot.prefix + "Jotain meni pieleen...");
            return;
        }

        if (member != null) {
            motiYhteisot.getManager().notify(member.getOrganization(), "Pelaaja " + member.getName() + " poistui yhteisöstänne ja loi oman yhteisön...");
            member.delete();
        }


        Organization organization = new Organization(args[1], args[2], UUID.randomUUID(), player);

        Bukkit.broadcastMessage(motiYhteisot.prefix + "Pelaaja §9" + player.getName() + " §7loi uuden yhteisön nimellä §9" + organization.getName());

    }
}
