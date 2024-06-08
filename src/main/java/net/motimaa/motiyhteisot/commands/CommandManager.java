package net.motimaa.motiyhteisot.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.motimaa.motiyhteisot.MotiYhteisot;
import net.motimaa.motiyhteisot.commands.admin.ReloadCommandAdmin;
import net.motimaa.motiyhteisot.commands.common.*;
import net.motimaa.motiyhteisot.commands.member.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommandManager implements CommandExecutor {

    private final MotiYhteisot motiYhteisot = MotiYhteisot.getInstance();

    private final ArrayList<SubCommand> subcommands;

    public CommandManager() {

        subcommands = new ArrayList<>();

        subcommands.add(new CreateCommand());
        subcommands.add(new ApplyCommand());
        subcommands.add(new InfoCommand());
        subcommands.add(new ListCommand());

        subcommands.add(new GuiCommand());
        subcommands.add(new ApplicationsCommand());
        subcommands.add(new BaseCommand());
        subcommands.add(new ResignCommand());

        subcommands.add(new ApplicationActionsCommand());
        subcommands.add(new DismissCommand());
        subcommands.add(new BankCommand());
        subcommands.add(new InviteCommand());

        subcommands.add(new RenameCommand());
        subcommands.add(new RoleCommand());
        subcommands.add(new WarCommand());
        subcommands.add(new DeleteCommand());
        subcommands.add(new MembersCommand());

        subcommands.add(new PermissionsCommand());
        subcommands.add(new ReloadCommandAdmin());

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (sender instanceof Player player) {

            if (args.length > 0) {
                for (SubCommand subCommand : subcommands) {
                    if (args[0].equalsIgnoreCase(subCommand.getName())) {
                        subCommand.perform(player, args);
                        return true;
                    }
                }
                if (args.length == 1) {
                    int page;
                    try {
                        page = Integer.parseInt(args[0]);
                    } catch (NumberFormatException nfe) {
                        player.sendMessage(motiYhteisot.prefix + "Virheellinen lukuarvo");
                        sendHelp(player, 1);
                        return true;
                    }
                    sendHelp(player, page);
                    return true;
                }
            }
            sendHelp(player, 1);
        }
        return true;
    }


    public void sendHelp(Player player, int page) {

        final int showAmount = 4;

        ArrayList<SubCommand> commands = new ArrayList<>();

        boolean isAdmin = player.hasPermission("motimaa.admin");

        for (SubCommand subCommand : subcommands) {
            if (isAdmin) {
                commands.add(subCommand);
                continue;
            }
            if (subCommand.isHidden()) {
                continue;
            }
            commands.add(subCommand);
        }


        if (page <= 0) {
            player.sendMessage(motiYhteisot.prefix + "Tuollaista sivua ei ole olemassakaan");
            return;
        }

        int pagesAmount = (int) Math.ceil((float) commands.size() / showAmount);

        if (page > pagesAmount) {
            return;
        }

        player.sendMessage(motiYhteisot.prefix + "Komennot§8:");

        for (int i = 0; i < showAmount; ) {

            if (i + (showAmount * (page - 1)) >= commands.size()) {
                break;
            }

            SubCommand subCommand = commands.get(i + (showAmount * (page - 1)));
            TextComponent cmd = new TextComponent("§a§l" + subCommand.getSyntax());
            TextComponent desc = new TextComponent("§7§o   " + subCommand.getDescription());
            player.spigot().sendMessage(cmd);
            player.spigot().sendMessage(desc);
            i++;
        }

        TextComponent base = new TextComponent("");

        TextComponent prevPage = new TextComponent("§8§l◀ ");
        prevPage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§oEdellinen sivu").create()));

        TextComponent currPage = new TextComponent("§7§lSivu: §9§l" + page + "/" + pagesAmount + " ");

        TextComponent nextPage = new TextComponent("§8§l▶ ");
        nextPage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§oSeuraava sivu").create()));

        prevPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö " + (page - 1)));
        nextPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yhteisö " + (page + 1)));


        base.addExtra(prevPage);

        base.addExtra(currPage);

        base.addExtra(nextPage);

        player.spigot().sendMessage(base);


    }

    public ArrayList<SubCommand> getSubcommands() {
        return subcommands;
    }
}
