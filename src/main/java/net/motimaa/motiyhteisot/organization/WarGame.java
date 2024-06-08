package net.motimaa.motiyhteisot.organization;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.motimaa.motiyhteisot.MotiYhteisot;
import net.motimaa.motiyhteisot.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.motimaa.motiyhteisot.commands.member.WarCommand.minPlayers;
import static net.motimaa.motiyhteisot.commands.member.WarCommand.warGame;
import static net.motimaa.motiyhteisot.organization.WarGame.GameStatus.*;

public class WarGame {

    private final long created;

    private long end_time;

    private final List<Player> team1;
    private final List<Player> team2;

    protected static final MotiYhteisot motiYhteisot = MotiYhteisot.getInstance();

    private final Organization organization1;

    private final Organization organization2;

    private BossBar bossBar;

    private double bet;

    private int id;

    private final FileConfiguration config = MotiYhteisot.getInstance().getConfig();

    private GameStatus status = PREPARATION;


    public WarGame(Organization organization1, Organization organization2, Double bet) {
        this.bet = bet;
        this.created = System.currentTimeMillis();
        this.organization1 = organization1;
        this.organization2 = organization2;
        this.team1 = new ArrayList<>();
        this.team2 = new ArrayList<>();
    }

    public enum GameStatus {
        PREPARATION,
        RUNNING,
        ENDED
    }

    public void init() {
        end_time = System.currentTimeMillis() + (30 * 60000);
        String join_message = "逐 <gray> Sinut on <blue>kutsuttu <gray>yhteisö taistoon <hover:show_text:'<blue>Klikkaa tästä!'><click:run_command:/yht haasta hyväksy><bold><blue>Klikkaa <reset><gray>tästä liittyäksesi peliin potti yhteensä on <blue>" + bet * 2 + config.getString("money-suffix") + "<gray>!";
        bossBar = BossBar.bossBar(Component.text("aika"), 1.0f, BossBar.Color.YELLOW, BossBar.Overlay.PROGRESS);
        status = PREPARATION;
        for (Member loopMember : organization1.getMembers()) {
            Player receiver = Bukkit.getPlayer(loopMember.getUuid());
            if (receiver != null && !team1.contains(receiver)) {
                receiver.sendMessage(Utils.parseMesssage(join_message));

            }
        }
        for (Member loopMember : organization2.getMembers()) {
            Player receiver = Bukkit.getPlayer(loopMember.getUuid());
            if (receiver != null && !team2.contains(receiver)) {
                receiver.sendMessage(Utils.parseMesssage(join_message));
            }
        }

        id = motiYhteisot.getServer().getScheduler().scheduleSyncRepeatingTask(motiYhteisot, () -> {
            double preparation_time = warGame.getCreated() + config.getLong("war.prep-time") - System.currentTimeMillis();
            // double preparation_time = warGame.getCreated()+5000  - System.currentTimeMillis();
            double time = ((warGame.getEndTime() + preparation_time) - System.currentTimeMillis());

            String message;
            if (preparation_time <= 0) {
                //start game preparation time is ended
                if (time <= 0) {
                    Utils.info("Cancelled game!");
                    warGame.end();
                    return;
                } else {
                    if (!warGame.getGameStatus().equals(WarGame.GameStatus.ENDED)){
                        if (warGame.getTeam1Members().size() < minPlayers || warGame.getTeam2Members().size() < minPlayers) {
                            Bukkit.broadcast(Utils.parseMesssage("逐 <gray>Ei tarpeeksi pelaajia taiston alkamiseen!"));
                            warGame.end();
                            return;
                        }
                        if(warGame.getGameStatus().equals(PREPARATION)){
                            balanceTeams(warGame.getBossBar());
                            warGame.start();
                        }
                    }
                    message = "逐 <gray>Pelin loppumiseen <blue>" + Math.round((time / 60000) * 100.0) / 100.0 + "m";
                }
            } else {
                message = "逐 <gray>Pelin alkamiseen <blue>" + (preparation_time / 1000) + "s";
            }
            warGame.getBossBar().name(Utils.parseMesssage(message));
        }, 0L, 2L);

    }

    public void start() {
        if(organization1.getBalance() < bet || organization2.getBalance() < bet){
            Utils.info("Jommalla kummalla yhteisöllä ei ollukkaan tarpeeks massikkaa taistoon. end();");
            end();
            return;
        }
        status = RUNNING;
        Bukkit.broadcast(Utils.parseMesssage("逐 <gray>Taisto yhteisöjen <blue>" + organization1.getName() + " / " + organization2.getName() + " <gray>välillä on alkanut"));

        Location team1Spawn = new Location(Bukkit.getWorld(config.getString("war.team1.world")), config.getInt("war.team1.x"), config.getInt("war.team1.y"), config.getInt("war.team1.z"), config.getInt("war.team1.yaw"), config.getInt("war.team1.pitch"));
        Location team2Spawn = new Location(Bukkit.getWorld(config.getString("war.team2.world")), config.getInt("war.team2.x"), config.getInt("war.team2.y"), config.getInt("war.team2.z"), config.getInt("war.team2.yaw"), config.getInt("war.team2.pitch"));

        organization1.setBalance(organization1.getBalance() - bet);
        organization2.setBalance(organization2.getBalance() - bet);
        team1.forEach(player -> {
            player.teleport(team1Spawn);
            Bukkit.getWorld(team1Spawn.getWorld().getName()).playSound(team1Spawn, Sound.EVENT_RAID_HORN, 50, 1);
        });
        team2.forEach(player -> {
            player.teleport(team2Spawn);
            Bukkit.getWorld(team2Spawn.getWorld().getName()).playSound(team2Spawn, Sound.EVENT_RAID_HORN, 50, 1);
        });

    }

    public void end() {
        final FileConfiguration config = MotiYhteisot.getInstance().getConfig();
        final Location spawn = new Location(Bukkit.getWorld(config.getString("war.spawn.world")), config.getInt("war.spawn.x"), config.getInt("war.spawn.y"), config.getInt("war.spawn.z"), config.getInt("war.spawn.yaw"), config.getInt("war.spawn.pitch"));

        if(status == PREPARATION){
            if (getTeam1Members().size() < minPlayers || getTeam2Members().size() < minPlayers) {
                Utils.info("Ending war not enough players");
                team1.forEach(player -> {
                    player.sendMessage(Utils.parseMesssage("逐 <gray>Ei tarpeeksi pelaajia taiston aloittamiseen tarvitaan " + minPlayers + " per puoli!"));
                });
                team2.forEach(player -> {
                    player.sendMessage(Utils.parseMesssage("逐 <gray>Ei tarpeeksi pelaajia taiston aloittamiseen tarvitaan " + minPlayers + " per puoli!"));
                });
            }
        }

        if (status.equals(RUNNING)) {

            Utils.info("Ended war with " + organization1.getName() + " / " + organization2.getName());

            if (team1.isEmpty()) {
                Bukkit.broadcast(Utils.parseMesssage("逐 <blue>" + organization2.getName() + " <gray>on voittanut taiston ja kerännyt itselleen <blue>" + bet * 2 + config.getString("money-suffix") + " <gray>taskuun"));
                organization2.setBalance(organization2.getBalance() + bet * 2);
                organization2.setWins(organization2.getWins() + 1);

            } else if (team2.isEmpty()) {
                Bukkit.broadcast(Utils.parseMesssage("逐 <blue>" + organization1.getName() + " <gray>on voittanut taiston ja kerännyt itselleen <blue>" + bet * 2 + config.getString("money-suffix") + " <gray>taskuun"));
                organization1.setBalance(organization1.getBalance() + bet * 2);

                organization1.setWins(organization1.getWins() + 1);
            } else {
                Bukkit.broadcast(Utils.parseMesssage("逐 <gray>Taisto yhteisöjen <blue>" + organization1.getName() + " / " + organization2.getName() + " <gray>välillä on loppunut tasapeliin"));
            }
        }

        if (!team1.isEmpty()) {
            team1.forEach(player -> {
                player.hideBossBar(bossBar);
                if (status.equals(RUNNING)) {
                    player.teleport(spawn);
                }
            });
            team1.clear();
        }
        if (!team2.isEmpty()) {
            team2.forEach(player -> {
                player.hideBossBar(bossBar);
                if (status.equals(RUNNING)) {
                    player.teleport(spawn);
                }
            });
            team2.clear();
        }

        Bukkit.getScheduler().cancelTask(id);

        status = ENDED;
        // delete it self
        warGame = null;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public long getCreated() {
        return created;
    }

    public double getBet() {
        return bet;
    }

    public double getEndTime() {
        return end_time;
    }

    public void setBet(double balance) {
        this.bet = bet;
    }

    public GameStatus getGameStatus() {
        return status;
    }

    public void setGameStatus(GameStatus status) {
        this.status = status;
    }

    public void setEnd_time(long endtime) {
        this.end_time = endtime;
    }

    public List<Player> getTeam1Members() {
        return this.team1;
    }

    public List<Player> getTeam2Members() {
        return this.team2;
    }

    public List<Player> getPlayer(Player player) {
        if (team1.contains(player)) {
            return team1;
        } else if (team2.contains(player)) {
            return team2;
        }
        return null;
    }

    private void balanceTeams(BossBar bossBar) {
        int teamOneSize = warGame.getTeam1Amount();
        int teamTwoSize = warGame.getTeam2Amount();
        int diff = Math.abs(teamOneSize - teamTwoSize);
        while (diff > 0) {
            int randomIndex;
            if (teamOneSize > teamTwoSize) {
                randomIndex = warGame.getTeam1Members().size() - 1;
                warGame.getTeam1Members().get(randomIndex).sendMessage(Utils.parseMesssage("逐 <gray>Sinut <blue>potkittiin <gray>tiimistä tasoituksen takia!"));
                warGame.getTeam1Members().get(randomIndex).hideBossBar(bossBar);
                warGame.getTeam1Members().remove(randomIndex);
                teamOneSize--;
            } else {
                randomIndex = warGame.getTeam2Members().size() - 1;
                warGame.getTeam1Members().get(randomIndex).sendMessage(Utils.parseMesssage("逐 <gray>Sinut <blue>potkittiin <gray>tiimistä tasoituksen takia!"));
                warGame.getTeam1Members().get(randomIndex).hideBossBar(bossBar);
                warGame.getTeam2Members().remove(randomIndex);
                teamTwoSize--;
            }
            diff = Math.abs(teamOneSize - teamTwoSize);
        }
        Utils.info("Balanced teams " + (teamTwoSize == teamOneSize ? "§aCorrectly" : "§cNot Correctly"));
    }

    public boolean containsPlayer(Player p) {
        return team1.contains(p) || team2.contains(p);
    }

    public Organization getOrganization1() {
        return organization1;
    }

    public Organization getOrganization2() {
        return organization2;
    }

    public int getTeam1Amount() {
        return this.team1.size();
    }

    public int getTeam2Amount() {
        return this.team2.size();
    }

}

