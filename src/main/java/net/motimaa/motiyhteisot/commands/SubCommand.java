package net.motimaa.motiyhteisot.commands;

import net.motimaa.motiyhteisot.MotiYhteisot;
import org.bukkit.entity.Player;

public abstract class SubCommand {

    protected static final MotiYhteisot motiYhteisot = MotiYhteisot.getInstance();

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract boolean isHidden();

    public abstract void perform(Player player, String[] args);

}
