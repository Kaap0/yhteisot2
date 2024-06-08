package net.motimaa.motiyhteisot.organization;

import net.motimaa.motiyhteisot.MotiYhteisot;
import net.motimaa.motiyhteisot.storage.Saveable;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Application implements Saveable {

    private final UUID applicationID;
    private final UUID uuid;
    private String name;
    private final Organization organization;
    private final String description;

    public Application(Player player, Organization organization, String description, UUID applicationID) {
        this.applicationID = applicationID;
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.organization = organization;
        this.description = description;
        save();
    }

    public Application(UUID applicationID, UUID uuid, String name, Organization organization, String description) {
        this.applicationID = applicationID;
        this.uuid = uuid;
        this.name = name;
        this.organization = organization;
        this.description = description;
        MotiYhteisot.getInstance().getManager().getApplications().add(this);
    }

    public UUID getApplicationID() {
        return applicationID;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Organization getOrganization() {
        return organization;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
        save();
    }

    @Override
    public void save() {
        if (!MotiYhteisot.getInstance().getManager().getApplications().contains(this)) {
            MotiYhteisot.getInstance().getManager().getApplications().add(this);
        }
        MotiYhteisot.getInstance().getStorage().save(this);
    }

    @Override
    public void delete() {
        MotiYhteisot.getInstance().getManager().getApplications().remove(this);
        MotiYhteisot.getInstance().getStorage().delete(this);
    }
}
