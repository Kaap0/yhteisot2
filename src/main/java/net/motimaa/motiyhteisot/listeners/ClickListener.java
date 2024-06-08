package net.motimaa.motiyhteisot.listeners;

import net.motimaa.motiyhteisot.MotiYhteisot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class ClickListener implements Listener {

    private final MotiYhteisot motiYhteisot = MotiYhteisot.getInstance();

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            for (Player player : motiYhteisot.getGuiManager().getActivePages().keySet()) {
                if (player.equals(event.getWhoClicked())) {
                    motiYhteisot.getGuiManager().getActivePages().get(player).executeClick(event);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player p) {
            motiYhteisot.getGuiManager().getActivePages().remove(p);
        }
    }

}
