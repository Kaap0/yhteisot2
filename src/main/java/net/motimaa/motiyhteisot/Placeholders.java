package net.motimaa.motiyhteisot;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.motimaa.motiyhteisot.organization.Member;
import net.motimaa.motiyhteisot.organization.Organization;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Placeholders extends PlaceholderExpansion {

    private final MotiYhteisot motiYhteisot = MotiYhteisot.getInstance();

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return motiYhteisot.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "yhteiso";
    }

    @Override
    public String getVersion() {
        return motiYhteisot.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null) {
            return "";
        }

        if (identifier.equals("short")) {
            Member member = motiYhteisot.getManager().getMember(player);
            if (member != null) {
                return member.getOrganization().getAbbreviation();
            }
            return "";
        }
        if (identifier.equals("short_space")) {
            Member member = motiYhteisot.getManager().getMember(player);
            if (member != null) {
                return member.getOrganization().getAbbreviation()+" ";
            }
            return "";
        }
        if (identifier.equals("name")) {
            Member member = motiYhteisot.getManager().getMember(player);
            if (member != null) {
                return member.getOrganization().getName();
            }
            return "";
        }
        if (identifier.equals("role_colored")) {
            Member member = motiYhteisot.getManager().getMember(player);
            if (member != null) {
                return member.getRole().getNameColored();
            }
            return "";
        }
        if (identifier.equals("role")) {
            Member member = motiYhteisot.getManager().getMember(player);
            if (member != null) {
                return member.getRole().getName();
            }
            return "";
        }
        if (identifier.contains("biggest")) {

            List<Organization> organizations = new ArrayList<>(motiYhteisot.getManager().getOrganizations());
            organizations.sort(Comparator.comparingInt(Organization::getMemberAmount).reversed());

            if (identifier.contains("biggest_name_")) {
                if (isInteger(identifier.substring(13))) {
                    int value = Integer.parseInt(identifier.substring(13)) - 1;
                    try {
                        return organizations.get(value).getName();
                    } catch (IndexOutOfBoundsException exception) {
                        return "";
                    }
                }
                return "Config diff";
            }
            if (identifier.contains("biggest_value_")) {
                if (isInteger(identifier.substring(14))) {
                    int value = Integer.parseInt(identifier.substring(14)) - 1;
                    try {
                        return String.valueOf(organizations.get(value).getMemberAmount());
                    } catch (IndexOutOfBoundsException exception) {
                        return "";
                    }
                }
                return "Config diff";
            }
        }
        if (identifier.contains("richest")) {

            List<Organization> organizations = new ArrayList<>(motiYhteisot.getManager().getOrganizations());
            organizations.sort(Comparator.comparingDouble(Organization::getBalance).reversed());

            if (identifier.contains("richest_name_")) {
                if (isInteger(identifier.substring(13))) {
                    int value = Integer.parseInt(identifier.substring(13)) - 1;
                    try {
                        return String.valueOf(organizations.get(value).getName());
                    } catch (IndexOutOfBoundsException exception) {
                        return "";
                    }
                }
                return "Config diff";
            }
            if (identifier.contains("richest_value_")) {
                if (isInteger(identifier.substring(14))) {
                    int value = Integer.parseInt(identifier.substring(14)) - 1;
                    try {
                        return organizations.get(value).getBalanceFormatted();
                    } catch (IndexOutOfBoundsException exception) {
                        return "";
                    }
                }
                return "Config diff";
            }
        }
        if (identifier.contains("wins")) {

            List<Organization> organizations = new ArrayList<>(motiYhteisot.getManager().getOrganizations());
            organizations.sort(Comparator.comparingDouble(Organization::getWins).reversed());

            if (identifier.contains("wins_name_")) {
                if (isInteger(identifier.substring(10))) {
                    int value = Integer.parseInt(identifier.substring(10)) - 1;
                    try {
                        return String.valueOf(organizations.get(value).getName());
                    } catch (IndexOutOfBoundsException exception) {
                        return "";
                    }
                }
                return "Config diff";
            }
            if (identifier.contains("wins_value_")) {
                if (isInteger(identifier.substring(11))) {
                    int value = Integer.parseInt(identifier.substring(11)) - 1;
                    try {
                        return String.valueOf(organizations.get(value).getWins());
                    } catch (IndexOutOfBoundsException exception) {
                        return "";
                    }
                }
                return "Config diff";
            }
        }

        //%yhteiso_name%
        //%yhteiso_short%
        //%yhteiso_role%
        //%yhteiso_role_colored%
        //%yhteiso_biggest_name_#%
        //%yhteiso_biggest_value_#%

        //%yhteiso_richest_name_#%
        //%yhteiso_richest_value_#%

        //%yhteiso_wins_name_#%
        //%yhteiso_wins_value_#%
        return null;
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}