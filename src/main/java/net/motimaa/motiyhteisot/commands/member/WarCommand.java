package net.motimaa.motiyhteisot.commands.member;

import net.motimaa.motiyhteisot.MotiYhteisot;
import net.motimaa.motiyhteisot.Utils;
import net.motimaa.motiyhteisot.commands.SubCommand;
import net.motimaa.motiyhteisot.gui.pages.ConfirmPage;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import net.motimaa.motiyhteisot.organization.Role;
import net.motimaa.motiyhteisot.organization.WarGame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static net.motimaa.motiyhteisot.organization.WarGame.GameStatus.PREPARATION;

public class WarCommand extends SubCommand {

    @Override
    public String getName() {
        return "haasta";
    }

    @Override
    public String getDescription() {
        return "Yhteisö sotaan liittyvät komennot";
    }

    @Override
    public String getSyntax() {
        return "/yhteisö haasta [yhteisö/hyväksy/luovuta] [panos]";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    public static WarGame warGame;

    public static int minPlayers = MotiYhteisot.getInstance().getConfig().getInt("war.min-players");

    @Override
    public void perform(Player player, String[] args) {

        Organization organization;
        Member member = motiYhteisot.getManager().getMember(player);
        Role role = member.getRole();

        // check if war is enabled
        if (!motiYhteisot.getConfig().getBoolean("war.enabled")) {
            player.sendMessage(Utils.parseMesssage("<gray>Taisto ei ole tällä hetkellä käytössä!"));
            return;
        }

        // return syntax
        if (args.length == 1) {
            player.sendMessage(getSyntax());
            return;
        }

        if (args.length == 2) {

            if ((args[1].equalsIgnoreCase("hyväksy"))) {
                if (warGame.getGameStatus().equals(PREPARATION) && role.hasPermission("warjoin")) {
                    if (!warGame.containsPlayer(player) && !warGame.getGameStatus().equals(WarGame.GameStatus.RUNNING)) {
                        if(member.getOrganization().getBalance() < warGame.getBet()){
                            player.sendMessage(Utils.parseMesssage("逐 <gray>Yhteisöllä ei ole tarpeeksi rahaa taiston aloittamiseen!"));
                            return;
                        }
                        if (member.getOrganization() == warGame.getOrganization1()) {
                            warGame.getTeam1Members().add(player);
                            Bukkit.broadcast(Utils.parseMesssage("逐 <blue>" + player.getName() + " <gray>liittyi taistoon <blue>" + warGame.getTeam1Members().size() + "/" + warGame.getTeam2Members().size()));
                            player.showBossBar(warGame.getBossBar());
                            return;
                        }
                        if (member.getOrganization() == warGame.getOrganization2()) {
                            warGame.getTeam2Members().add(player);
                            Bukkit.broadcast(Utils.parseMesssage("逐 <blue>" + player.getName() + " <gray>liittyi taistoon <blue>" + warGame.getTeam1Members().size() + "/" + warGame.getTeam2Members().size()));
                            player.showBossBar(warGame.getBossBar());
                            return;
                        }
                    } else {
                        player.sendMessage(motiYhteisot.prefix + "Peli on jo käynnissä yritä hetken päästä uudelleen!");
                        return;
                    }
                } else {
                    player.sendMessage(motiYhteisot.prefix + "Peli ei ole vielä päällä yritä myöhemmin uudelleen tai sinulla ei ole oikeutta liittyä sotaan!");
                }
            }

            if ((args[1].equalsIgnoreCase("luovuta"))) {
//                if (warGame.getGameStatus().equals(RUNNING) && !warGame.containsPlayer(player)) {
//                    if (member.getOrganization() == warGame.getOrganization1()) {
//                        warGame.getTeam1Members().clear();
//                        warGame.end();
//                    } else if (member.getOrganization() == warGame.getOrganization2()){
//                        warGame.getTeam2Members().clear();
//                        warGame.end();
//                    }
//                }
                player.sendMessage(motiYhteisot.prefix + "Luovuttaminen ei ole vaihtoehto!");
                return;
            }
            player.sendMessage(getSyntax());

        } else if (args.length == 3) {

            organization = motiYhteisot.getManager().getOrganization(args[1]);
            if (organization == null || member.getOrganization().equals(organization)) {
                player.sendMessage(motiYhteisot.prefix + "Yhteisöä ei ole olemassa tai yrität haastaa itseäsi");
                return;
            }
            if (role.hasPermission("war")) {
                    try {
                        double bet = Double.parseDouble(args[2]);
                        if (organization.getBalance() >= bet || member.getOrganization().getBalance() >= bet) {
                            if(warGame != null){
                                player.sendMessage(motiYhteisot.prefix + "Peli on jo käynnissä yritä myöhemmin uudelleen!");
                                return;
                            }
                            start(player, member.getOrganization(), organization, bet);
                            return;
                        }
                        player.sendMessage(motiYhteisot.prefix + "Sinun yhteisöllä tai haastettavalla ei ole tarpeeksi rahaa tilillä!");
                    } catch (NumberFormatException exp) {
                        player.sendMessage(motiYhteisot.prefix + "Luku on laitettu väärin!");
                    }
            } else {
                player.sendMessage(motiYhteisot.prefix + "Sinun roolillasi ei ole oikeutta haastaa yhteisöä");
            }
        } else {
            player.sendMessage(motiYhteisot.prefix + getSyntax());
        }
    }

    private void start(Player player, Organization organization, Organization organization2, double bet) {
        new ConfirmPage(player, organization, "&f&lVahvista haastaminen?", (p, bool) -> {
            if (bool) {
                warGame = new WarGame(organization, organization2, bet);
                warGame.init();
            } else {

            }
        }).openInventory();
    }

}
