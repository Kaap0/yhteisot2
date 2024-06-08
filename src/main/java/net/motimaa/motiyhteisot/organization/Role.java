package net.motimaa.motiyhteisot.organization;

import net.motimaa.motiyhteisot.MotiYhteisot;
import net.motimaa.motiyhteisot.Utils;
import net.motimaa.motiyhteisot.listeners.OrganizationChat;
import net.motimaa.motiyhteisot.storage.Saveable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Role implements Saveable {

    private final UUID roleID;
    private final Organization organization;
    private final List<String> permissions;
    private String name;
    private byte weight;

    public Role(UUID roleID, String name, Organization organization, byte weight, String permissions) {
        this.roleID = roleID;
        this.name = name;
        this.organization = organization;
        this.weight = weight;
        this.permissions = Utils.stringToList(permissions);
        this.organization.getRoles().add(this);
    }

    public Role(UUID roleID, Organization organization, boolean owner) {
        this.roleID = roleID;
        this.permissions = new ArrayList<>();
        if (owner) {
            this.name = "&aOmistaja";
            this.weight = 127;
            this.permissions.add("*");
        } else {
            this.name = "Rooli";
            this.weight = 0;
        }

        this.organization = organization;
        save();
    }

    public UUID getRoleID() {
        return roleID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        save();
    }

    public String getNameColored() {
        return OrganizationChat.translateColors(name);
    }

    public byte getWeight() {
        return weight;
    }

    public void setWeight(byte weight) {
        this.weight = weight;
        save();
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public Organization getOrganization() {
        return organization;
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission) || permissions.contains("*");
    }

    public void addPermission(String permission) {
        if (Permissions.allPermissions.contains(permission)) {
            permissions.add(permission);
            save();
        }
    }

    public void removePermission(String permission) {
        if (permissions.contains(permission)) {
            permissions.remove(permission);
            save();
        }
    }


    @Override
    public void save() {
        if (!organization.getRoles().contains(this)) {
            organization.getRoles().add(this);
        }
        MotiYhteisot.getInstance().getStorage().save(this);
    }

    @Override
    public void delete() {
        organization.getRoles().remove(this);
        MotiYhteisot.getInstance().getStorage().delete(this);
    }
}
