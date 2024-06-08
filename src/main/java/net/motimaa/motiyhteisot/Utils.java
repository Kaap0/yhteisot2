package net.motimaa.motiyhteisot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Utils {

    private static MiniMessage mm = MiniMessage.miniMessage();

    private static final String CONSOLE_PREFIX = "[MotiYhteisot2] ";

    public static void info(String log) {
        Bukkit.getLogger().info(CONSOLE_PREFIX + log);
    }

    public static void severe(String log) {
        Bukkit.getLogger().severe(CONSOLE_PREFIX + log);
    }

    public static String locationToString(Location location) {

        if (location.getWorld() != null) {
            String world = location.getWorld().getName();
            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();
            float pitch = location.getPitch();
            float yaw = location.getYaw();

            return world + ":" + x + ":" + y + ":" + z + ":" + pitch + ":" + yaw;
        }
        return null;
    }

    public static Location stringToLocation(String location) {

        String world = location.split(":")[0];
        String x = location.split(":")[1];
        String y = location.split(":")[2];
        String z = location.split(":")[3];
        String pitch = location.split(":")[4];
        String yaw = location.split(":")[5];

        World realWorld = Bukkit.getWorld(world);

        if (realWorld != null) {
            Location newloc = new Location(realWorld, Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
            newloc.setPitch(Float.parseFloat(pitch));
            newloc.setYaw(Float.parseFloat(yaw));
            return newloc;
        }
        return null;
    }

    public static String epochMillisToReadableDate(long millis) {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy").withZone(ZoneId.systemDefault()).format(Instant.ofEpochMilli(millis));
    }

    public static List<String> stringToList(String permissions) {
        return new ArrayList<>(Arrays.asList(permissions.split(",")));
    }

    public static String listToString(List<String> list) {
        return String.join(",", list);
    }

    public static <K, V extends Comparable<? super V>> ArrayList<K> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

        ArrayList<K> result = new ArrayList<>();
        for (Map.Entry<K, V> entry : list) {
            result.add(entry.getKey());
        }
        return result;
    }

    public static Component parseMesssage(String str){
        return mm.deserialize(str);
    }

    public static boolean between(int i, int min, int max) {
        return (i >= min && i <= max);
    }

    public static String formatMoney(double money) {
        DecimalFormat df = new DecimalFormat("#.##");
        return String.valueOf(df.format(money));
    }
}
