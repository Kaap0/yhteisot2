package net.motimaa.motiyhteisot.listeners;

import net.motimaa.motiyhteisot.MotiYhteisot;
import net.motimaa.motiyhteisot.Utils;
import net.motimaa.motiyhteisot.commands.member.WarCommand;
import net.motimaa.motiyhteisot.organization.WarGame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class WarListener implements Listener {

    private final FileConfiguration config = MotiYhteisot.getInstance().getConfig();
    private final Location spawn = new Location(Bukkit.getWorld(config.getString("war.spawn.world")), config.getInt("war.spawn.x"), config.getInt("war.spawn.y"), config.getInt("war.spawn.z"), config.getInt("war.spawn.yaw"), config.getInt("war.spawn.pitch"));


    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if (e.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase(config.getString("war.team1.world"))) {
            e.setRespawnLocation(spawn);
        }
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        if (WarCommand.warGame != null) {

            if (WarCommand.warGame.containsPlayer(e.getPlayer()) && WarCommand.warGame.getGameStatus().equals(WarGame.GameStatus.RUNNING)) {

                // keep inv
                e.setKeepInventory(true);
                e.setKeepLevel(true);
                e.getDrops().clear();
                e.setDroppedExp(0);

                // remove player from game bossbar
                e.getPlayer().hideBossBar(WarCommand.warGame.getBossBar());

                // remove player from team
                WarCommand.warGame.getPlayer(e.getPlayer()).remove(e.getPlayer());

                // check if one of the teams is empty and end the game
                if (WarCommand.warGame.getTeam1Members().isEmpty() || WarCommand.warGame.getTeam2Members().isEmpty()) {
                    // end game!
                    Utils.info("All died ending war!");
                    WarCommand.warGame.end();
                    e.deathMessage(null);
                } else {
                    if (e.getPlayer().getKiller() != null) {
                        String death = "逐  <blue>" + e.getPlayer().getName() + " <gray> kuoli pelaajan <blue>" + e.getPlayer().getKiller().getName() + "<gray> toimesta tilanne on <blue>" + WarCommand.warGame.getTeam1Amount() + " / " + WarCommand.warGame.getTeam2Amount();
                        e.deathMessage(Utils.parseMesssage(death));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (WarCommand.warGame != null) {
            if (WarCommand.warGame.containsPlayer(e.getPlayer())) {

                // teleport to spawn
                e.getPlayer().teleport(spawn);

                // remove player from game bossbar
                e.getPlayer().hideBossBar(WarCommand.warGame.getBossBar());

                // remove player from team
                WarCommand.warGame.getPlayer(e.getPlayer()).remove(e.getPlayer());

                // send message that player died!
                Utils.info("Player left middle of battle, removing!");
                // check if one of the teams is empty and end the game
                if (WarCommand.warGame.getTeam1Members().isEmpty() || WarCommand.warGame.getTeam2Members().isEmpty()) {
                    // end game!
                    Utils.info("All died ending war!");
                    WarCommand.warGame.end();
                }
            }
        }
    }


}
