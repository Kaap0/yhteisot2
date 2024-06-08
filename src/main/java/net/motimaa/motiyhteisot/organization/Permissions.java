package net.motimaa.motiyhteisot.organization;

import java.util.ArrayList;
import java.util.Arrays;

public class Permissions {

    public static final ArrayList<String> allPermissions = new ArrayList<>(Arrays.asList("*", "base", "setbase", "withdraw", "deposit", "invite",
            "dismiss", "applicationlist", "accept", "deny", "delete", "changename", "changeabbreviation", "createrole", "modifyrole", "setrole", "war", "warjoin"));

    public static String getDescription(String permission) {

        return switch (permission) {
            case "*" -> "Kaikki oikeudet";
            case "base" -> "Käyttää päämajaa";
            case "setbase" -> "Asettaa päämaja";
            case "withdraw" -> "Nostaa rahaa pankkitililtä";
            case "deposit" -> "Tallettaa rahaa pankkitilille";
            case "invite" -> "Kutsua pelaajia yhteisöön";
            case "setrole" -> "Asettaa muiden rooleja (Ei kuitenkaan itseään ylemmäs)";
            case "dismiss" -> "Erottaa alempia jäseniä";
            case "war" -> "Aloita taisto!";
            case "warjoin" -> "Antaa oikeuden liittyä taistoon!";
            case "applicationlist" -> "Nähdä hakemuslista";
            case "accept" -> "Hyväksyä hakemuksia";
            case "deny" -> "Hylätä hakemuksia";
            case "changename" -> "Muuttaa yhteisön nimeä";
            case "changeabbreviation" -> "Muuttaa yhteisön lyhennettä";
            case "delete" -> "Poistaa yhteisö";
            case "createrole" -> "Luoda rooleja";
            case "modifyrole" -> "Muokata rooleja";
            default -> null;
        };
    }
}
