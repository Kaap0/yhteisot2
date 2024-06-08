package net.motimaa.motiyhteisot.commands;

import net.motimaa.motiyhteisot.MotiYhteisot;
import net.motimaa.motiyhteisot.organization.Application;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {

    private final MotiYhteisot motiYhteisot = MotiYhteisot.getInstance();

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args) {

        if (command.getName().equalsIgnoreCase("yhteisö") || command.getName().equalsIgnoreCase("yht")) {

            if (sender instanceof Player player) {

                List<String> argList = new ArrayList<>();

                if (args.length == 1) {
                    for (SubCommand subCommand : motiYhteisot.getCommandManager().getSubcommands()) {
                        if (!subCommand.isHidden()) {
                            argList.add(subCommand.getName());
                        }
                    }
                }
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("luo")) {
                        argList.add("<nimi>");
                    }
                    if (args[0].equalsIgnoreCase("hae")) {
                        for (Organization organization : motiYhteisot.getManager().getOrganizations()) {
                            argList.add(organization.getName());
                        }
                    }
                    if (args[0].equalsIgnoreCase("tiedot")) {
                        for (Organization organization : motiYhteisot.getManager().getOrganizations()) {
                            argList.add(organization.getName());
                        }
                    }
                    if (args[0].equalsIgnoreCase("päämaja")) {
                        argList.add("aseta");
                    }
                    if (args[0].equalsIgnoreCase("roolita")) {
                        Member member = motiYhteisot.getManager().getMember(player);
                        if (member == null) {
                            argList.add("<pelaaja>");
                        } else {
                            for (Member loopMember : member.getOrganization().getMembers()) {
                                argList.add(loopMember.getName());
                            }
                        }

                    }
                    if (args[0].equalsIgnoreCase("erota")) {

                        Member member = motiYhteisot.getManager().getMember(player);
                        if (member == null) {
                            argList.add("<pelaaja>");
                        } else {
                            for (Member loopMember : member.getOrganization().getMembers()) {
                                argList.add(loopMember.getName());
                            }
                        }

                    }
                    if (args[0].equalsIgnoreCase("pankki")) {
                        argList.add("talleta");
                        argList.add("nosta");
                    }
                    if (args[0].equalsIgnoreCase("kutsu")) {
                        for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
                            argList.add(loopPlayer.getName());
                        }
                    }
                    if (args[0].equalsIgnoreCase("uudelleennimeä")) {
                        argList.add("nimi");
                        argList.add("lyhenne");
                    }
                    if (args[0].equalsIgnoreCase("hakemus")) {
                        argList.add("hyväksy");
                        argList.add("hylkää");
                        argList.add("peru");
                    }
                    return argList;
                }
                if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("luo")) {
                        argList.add("<lyhenne>");
                    }
                    if (args[0].equalsIgnoreCase("hae")) {
                        argList.add("<hakemus>");
                    }
                    if (args[0].equalsIgnoreCase("hyväksy") || (args[0].equalsIgnoreCase("hylkää"))) {
                        argList.add("<pelaaja>");
                    }
                    if (args[0].equalsIgnoreCase("peru")) {

                        for (Application application : motiYhteisot.getManager().getApplications()) {
                            if (application.getUuid().equals(player.getUniqueId())) {
                                argList.add(application.getOrganization().getName());
                            }
                        }
                    }
                    if (args[0].equalsIgnoreCase("pankki")) {
                        argList.add("<määrä>");
                    }
                    if (args[0].equalsIgnoreCase("uudelleennimeä")) {
                        argList.add("<uusi>");
                    }
                    return argList;
                }
                return argList;
            }
        }
        return null;
    }
}
