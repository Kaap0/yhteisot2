package net.motimaa.motiyhteisot.commands.member;

import net.motimaa.motiyhteisot.commands.SubCommand;
import net.motimaa.motiyhteisot.gui.pages.MainPage;
import org.bukkit.entity.Player;

public class GuiCommand extends SubCommand {

    @Override
    public String getName() {
        return "gui";
    }

    @Override
    public String getDescription() {
        return "Yhteisön Hallintapaneeli";
    }

    @Override
    public String getSyntax() {
        return "/yhteisö gui";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void perform(Player player, String[] args) {

        if (args.length == 1) {
            if (!motiYhteisot.getManager().isInOrganization(player)) {
                player.sendMessage(motiYhteisot.prefix + "Et ole yhteisössä");
                return;
            }

            new MainPage(player, motiYhteisot.getManager().getMember(player).getOrganization()).openInventory();

        } else {
            player.sendMessage(motiYhteisot.prefix + getSyntax());
        }

    }
}
