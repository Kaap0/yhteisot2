package net.motimaa.motiyhteisot.organization;

import net.motimaa.motiyhteisot.MotiYhteisot;
import net.motimaa.motiyhteisot.Utils;
import net.motimaa.motiyhteisot.storage.Saveable;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Organization implements Saveable {

    private final UUID organizationID;
    private final long created;
    private final List<Member> members;
    private final List<Role> roles;
    private String name;
    private String abbreviation;
    private double balance;
    private Location base;
    private int wins;

    public Organization(String name, String abbreviation, UUID organizationID, Player player) {
        this.organizationID = organizationID;
        this.name = name;
        this.abbreviation = abbreviation;
        this.balance = 0;
        this.created = System.currentTimeMillis();
        this.wins = 0;

        this.members = new ArrayList<>();
        this.roles = new ArrayList<>();

        new Member(player, this, new Role(UUID.randomUUID(), this, true));
        new Role(UUID.randomUUID(), this, false);

        save();
    }

    public Organization(UUID organizationID, String name, String abbreviation, long created, double balance, String base, int wins) {
        this.organizationID = organizationID;
        this.name = name;
        this.abbreviation = abbreviation;
        this.created = created;
        this.balance = balance;
        if (base != null) {
            this.base = Utils.stringToLocation(base);
        }
        this.wins = wins;
        this.members = new ArrayList<>();
        this.roles = new ArrayList<>();

        MotiYhteisot.getInstance().getManager().getOrganizations().add(this);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        save();
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
        save();
    }

    public long getCreated() {
        return created;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
        save();
    }

    public String getBalanceFormatted() {
        DecimalFormat df = new DecimalFormat("#.##");
        return String.valueOf(df.format(balance));
    }

    public Location getBase() {
        return base;
    }

    public void setBase(Location base) {
        this.base = base;
        save();
    }

    public UUID getOrganizationID() {
        return organizationID;
    }

    public Member getMember(UUID uuid) {
        for (Member member : members) {
            if (member.getUuid().equals(uuid)) {
                return member;
            }
        }
        return null;
    }

    public Member getMember(String name) {
        for (Member member : members) {
            if (member.getName().equalsIgnoreCase(name)) {
                return member;
            }
        }
        return null;
    }

    public List<Member> getMembers() {
        return this.members;
    }

    public int getMemberAmount() {
        return this.members.size();
    }

    public List<Role> getRoles() {
        return this.roles;
    }

    @Override
    public void save() {
        if (!MotiYhteisot.getInstance().getManager().getOrganizations().contains(this)) {
            MotiYhteisot.getInstance().getManager().getOrganizations().add(this);
        }
        MotiYhteisot.getInstance().getStorage().save(this);
    }

    @Override
    public void delete() {
        MotiYhteisot.getInstance().getManager().getOrganizations().remove(this);
        MotiYhteisot.getInstance().getStorage().delete(this);
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
        save();
    }
}

