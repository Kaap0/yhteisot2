package net.motimaa.motiyhteisot.listeners;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.motimaa.motiyhteisot.MotiYhteisot;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class OrganizationChat implements Listener {

    private final MotiYhteisot motiYhteisot = MotiYhteisot.getInstance();

    private static final MiniMessage mm = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer lcs = LegacyComponentSerializer.legacySection();

    public static String translateColors(String str) {
        str = lcs.serialize(mm.deserialize(str.replace("§", "&")));
        str = ChatColor.translateAlternateColorCodes('&', str);
        return str;
    }

    @EventHandler
    public void aSyncChatEvent(AsyncPlayerChatEvent event) {

        if (event.getMessage().startsWith("!")) {

            if (!motiYhteisot.getManager().isInOrganization(event.getPlayer())) {
                return;
            }

            Member member = motiYhteisot.getManager().getMember(event.getPlayer());
            Organization organization = member.getOrganization();

            event.setCancelled(true);

            String message = event.getMessage().substring(1);

            for (Member loopMember : organization.getMembers()) {
                Player receiver = Bukkit.getPlayer(loopMember.getUuid());
                if (receiver != null) {
                    receiver.sendMessage(translateColors("&b[YhteisöChat]&r " + member.getRole().getName() + " " + member.getName() + ": &r" + ChatColor.AQUA + message));
                }
            }
        }
    }

}
