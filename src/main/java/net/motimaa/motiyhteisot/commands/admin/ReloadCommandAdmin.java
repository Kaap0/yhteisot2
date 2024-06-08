package net.motimaa.motiyhteisot.commands.admin;

import net.motimaa.motiyhteisot.Manager;
import net.motimaa.motiyhteisot.commands.SubCommand;
import org.bukkit.entity.Player;

public class ReloadCommandAdmin extends SubCommand {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Pluginin uudelleenlataus";
    }

    @Override
    public String getSyntax() {
        return "/yhteisö reload (debug)";
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public void perform(Player player, String[] args) {

        if (player.hasPermission("motimaa.admin")) {
            if (args.length == 1 || args.length == 2) {

                motiYhteisot.reloadConfig();

                motiYhteisot.setManager(new Manager());

                motiYhteisot.getStorage().load(args.length == 2 && args[1].equalsIgnoreCase("debug"));
                player.sendMessage(motiYhteisot.prefix + "Config ja Data uudelleenladattu!");
            } else {
                player.sendMessage(motiYhteisot.prefix + getSyntax());
            }
        } else {
            player.sendMessage(motiYhteisot.prefix + "Sinulla ei ole oikeutta tähän");
        }
    }
}
