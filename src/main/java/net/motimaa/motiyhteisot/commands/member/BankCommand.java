package net.motimaa.motiyhteisot.commands.member;

import net.milkbowl.vault.economy.EconomyResponse;
import net.motimaa.motiyhteisot.MotiYhteisot;
import net.motimaa.motiyhteisot.commands.SubCommand;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class BankCommand extends SubCommand {

    @Override
    public String getName() {
        return "pankki";
    }

    @Override
    public String getDescription() {
        return "Tallettaa tai nostaa rahaa yhteisön pankista";
    }

    @Override
    public String getSyntax() {
        return "/yhteisö pankki <talleta/nosta> <määrä>";
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
        String moneysuffix = motiYhteisot.getConfig().getString("money-suffix");

        if (args.length != 3) {
            player.sendMessage(motiYhteisot.prefix + "Yhteisön Pankki: §9" + organization.getBalanceFormatted() + moneysuffix);
            player.sendMessage(motiYhteisot.prefix + getSyntax());
            return;
        }

        double amount;
        double playerBal = MotiYhteisot.getEconomy().getBalance(player);
        double orgBal = organization.getBalance();

        try {
            amount = Double.parseDouble(args[2]);
        } catch (NumberFormatException nfe) {
            player.sendMessage(motiYhteisot.prefix + "Virheellinen luku");
            return;
        }

        if (amount == 0.0 || amount < 0.0) {
            player.sendMessage(motiYhteisot.prefix + "Luku ei voi olla 0 tai vähemmän");
            return;
        }
        if (BigDecimal.valueOf(amount).scale() > 2) {
            player.sendMessage(motiYhteisot.prefix + "Anna luku maksimissaan 2 desimaalin tarkkuudella");
            return;
        }

        if (args[1].equalsIgnoreCase("talleta")) {
            if (!member.getRole().hasPermission("deposit")) {
                player.sendMessage(motiYhteisot.prefix + "Roolillasi ei ole oikeutta tallettaa rahaa yhteisön pankkiin");
                return;
            }
            if (playerBal >= amount) {
                EconomyResponse er = MotiYhteisot.getEconomy().withdrawPlayer(player, amount);
                if (er.transactionSuccess()) {
                    organization.setBalance(orgBal + amount);
                    player.sendMessage(motiYhteisot.prefix + "Tililtäsi veloitettiin §9" + amount + moneysuffix);
                    motiYhteisot.getManager().notify(organization, "Yhteisön pankkiin talletettiin §9" + amount + moneysuffix + " §7pelaajan §9" + member.getName() + " §7toimesta");
                    return;
                }
                player.sendMessage(motiYhteisot.prefix + "Jotain meni pieleen...");
                return;
            }
            player.sendMessage(motiYhteisot.prefix + "Sinulla ei ole tarpeeksi rahaa");
        } else if (args[1].equalsIgnoreCase("nosta")) {
            if (!member.getRole().hasPermission("withdraw")) {
                player.sendMessage(motiYhteisot.prefix + "Roolillasi ei ole oikeutta nostaa rahaa yhteisön pankkiin");
                return;
            }
            if (orgBal >= amount) {
                EconomyResponse er = MotiYhteisot.getEconomy().depositPlayer(player, amount);
                if (er.transactionSuccess()) {
                    organization.setBalance(orgBal - amount);
                    player.sendMessage(motiYhteisot.prefix + "Nostit itsellesi yhteisön tililtä §9" + amount + moneysuffix);
                    motiYhteisot.getManager().notify(organization, "Yhteisön pankista nostettiin §9" + amount + moneysuffix + " §7pelaajan §9" + member.getName() + " §7toimesta");
                    return;
                }
                player.sendMessage(motiYhteisot.prefix + "Jotain meni pieleen...");
                return;
            }
            player.sendMessage(motiYhteisot.prefix + "Yhteisön tilillä ei ole tarpeeksi rahaa");
        } else {
            player.sendMessage(motiYhteisot.prefix + getSyntax());
        }
    }

}
