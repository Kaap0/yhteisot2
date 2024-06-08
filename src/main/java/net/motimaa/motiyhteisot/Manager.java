package net.motimaa.motiyhteisot;

import net.motimaa.motiyhteisot.organization.Application;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import net.motimaa.motiyhteisot.organization.Role;
import net.motimaa.motiyhteisot.storage.sql.MySQL;
import net.motimaa.motiyhteisot.storage.sql.SQL;
import net.motimaa.motiyhteisot.storage.sql.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Manager {

    public static boolean LOADING = true;
    private final MotiYhteisot motiYhteisot = MotiYhteisot.getInstance();

    private final List<Organization> organizations;
    private final List<Application> applications;

    public Manager() {

        organizations = new ArrayList<>();
        applications = new ArrayList<>();

        String storageMethod = motiYhteisot.getConfig().getString("storage");

        if (storageMethod != null) {
            if (storageMethod.equalsIgnoreCase("mysql")) {
                Utils.info("Storage: MySQL");
                SQL sql = new SQL(new MySQL());
                motiYhteisot.setStorage(sql);
            } else if (storageMethod.equalsIgnoreCase("sqlite")) {
                Utils.info("Storage: SQLite");
                SQL sql = new SQL(new SQLite());
                motiYhteisot.setStorage(sql);
            }
        }
    }


    public boolean isInOrganization(Player player) {
        for (Member member : getMembers()) {
            if (member.getUuid().equals(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isInOrganization(UUID uuid) {
        for (Member member : getMembers()) {
            if (member.getUuid().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public Member getMember(Player player) {
        for (Member member : getMembers()) {
            if (member.getUuid().equals(player.getUniqueId())) {
                return member;
            }
        }
        return null;
    }

    public Member getMember(UUID uuid) {
        for (Member member : getMembers()) {
            if (member.getUuid().equals(uuid)) {
                return member;
            }
        }

        return null;
    }

    public boolean isReservedName(String string) {
        for (Organization organization : organizations) {
            if (organization.getName().equalsIgnoreCase(string) || organization.getAbbreviation().equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }

    public Role getSmallestRole(Organization organization) {
        Role smallestRole = organization.getRoles().get(0);

        for (Role loopRole : organization.getRoles()) {
            if (loopRole.getWeight() < smallestRole.getWeight())
                smallestRole = loopRole;
        }
        return smallestRole;
    }

    public boolean isNotSafeToLeave(Member member, Organization organization) {

        Role role = member.getRole();

        if (role.getWeight() == 127) {
            int count = 0;
            for (Member loopMember : organization.getMembers()) {
                if (loopMember.getRole().getWeight() == 127) {
                    count++;
                }
            }
            return count < 2;
        }
        return false;
    }

    public Organization getOrganization(String name) {
        for (Organization organization : organizations) {
            if (organization.getName().equalsIgnoreCase(name)) {
                return organization;
            }
        }
        return null;
    }

    public Organization getOrganization(UUID uuid) {
        for (Organization organization : organizations) {
            if (organization.getOrganizationID().equals(uuid)) {
                return organization;
            }
        }
        return null;
    }

    public Role getRole(UUID uuid) {
        for (Role role : getRoles()) {
            if (role.getRoleID().equals(uuid)) {
                return role;
            }
        }
        return null;
    }

    public Application getApplication(UUID applicationID) {
        for (Application loopApplication : getApplications()) {
            if (loopApplication.getApplicationID().equals(applicationID)) {
                return loopApplication;
            }
        }
        return null;
    }

    public void notify(Organization organization, String message) {
        for (Member member : organization.getMembers()) {
            Player player = Bukkit.getPlayer(member.getUuid());
            if (player != null) {
                player.sendMessage(motiYhteisot.prefix + message);
            }
        }
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public List<Member> getMembers() {

        List<Member> members = new ArrayList<>();

        for (Organization organization : organizations) {
            members.addAll(organization.getMembers());
        }
        return members;
    }

    public List<Role> getRoles() {

        List<Role> roles = new ArrayList<>();

        for (Organization organization : organizations) {
            roles.addAll(organization.getRoles());
        }
        return roles;
    }
}
