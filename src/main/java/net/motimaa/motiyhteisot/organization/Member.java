package net.motimaa.motiyhteisot.organization;

import net.motimaa.motiyhteisot.MotiYhteisot;
import net.motimaa.motiyhteisot.storage.Saveable;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Member implements Saveable {

    private final UUID uuid;
    private String name;

    private final Organization organization;
    private Role role;
    private final long joined;

    public Member(Player player, Organization organization, Role role) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.organization = organization;
        this.role = role;
        this.joined = System.currentTimeMillis();
        save();
    }

    public Member(UUID uuid, String name, Organization organization, Role role, long joined) {
        this.uuid = uuid;
        this.name = name;
        this.organization = organization;
        this.role = role;
        this.joined = joined;
        this.organization.getMembers().add(this);
    }


    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public long getJoined() {
        return joined;
    }

    public void setName(String name) {
        this.name = name;
        save();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
        save();
    }

    public Organization getOrganization() {
        return organization;
    }

    @Override
    public void save() {
        if (!organization.getMembers().contains(this)) {
            organization.getMembers().add(this);
        }
        MotiYhteisot.getInstance().getStorage().save(this);
    }

    @Override
    public void delete() {
        organization.getMembers().remove(this);
        MotiYhteisot.getInstance().getStorage().delete(this);
    }

}
