package net.motimaa.motiyhteisot.listeners;

import net.motimaa.motiyhteisot.Manager;
import net.motimaa.motiyhteisot.MotiYhteisot;
import net.motimaa.motiyhteisot.organization.Application;
import net.motimaa.motiyhteisot.organization.Member;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final MotiYhteisot motiYhteisot = MotiYhteisot.getInstance();

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (Manager.LOADING) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Palvelin lataa tietoja, odota hetki (MotiYhteisot2)");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        for (Member member : motiYhteisot.getManager().getMembers()) {
            if (member.getUuid().equals(player.getUniqueId())) {
                if (!member.getName().equals(player.getName())) {
                    member.setName(player.getName());
                }
            }
        }
        for (Application application : motiYhteisot.getManager().getApplications()) {
            if (application.getUuid().equals(player.getUniqueId())) {
                if (!application.getName().equals(player.getName())) {
                    application.setName(player.getName());
                }
            }
        }
    }

}
