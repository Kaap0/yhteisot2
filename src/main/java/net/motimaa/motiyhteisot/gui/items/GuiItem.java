package net.motimaa.motiyhteisot.gui.items;

import net.motimaa.motiyhteisot.listeners.OrganizationChat;
import net.motimaa.motiyhteisot.organization.Role;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GuiItem {

    private final Material material;

    private List<String> lore;

    private String permission;

    private final String name;

    private final Action action;

    private Role role;

    public GuiItem(Material material, String name, Action action) {
        this.material = material;
        this.name = OrganizationChat.translateColors(name);
        this.action = action;
    }

    public GuiItem(Material material, String name, List<String> lore, Action action) {
        this.material = material;
        this.name = OrganizationChat.translateColors(name);
        this.lore = lore;
        this.action = action;
    }

    public GuiItem(Material material, String name, Action action, Role role) {
        this.material = material;
        this.name = OrganizationChat.translateColors(name);
        this.action = action;
        this.role = role;
    }

    public GuiItem(Material material, String name, List<String> lore, Action action, Role role) {
        this.material = material;
        this.name = OrganizationChat.translateColors(name);
        this.lore = lore;
        this.action = action;
        this.role = role;
    }

    public GuiItem(Material material, String name, String permission, Action action) {
        this.material = material;
        this.name = OrganizationChat.translateColors(name);
        this.permission = permission;
        this.action = action;
    }

    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public Action getAction() {
        return action;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getPermission() {
        return permission;
    }
}
