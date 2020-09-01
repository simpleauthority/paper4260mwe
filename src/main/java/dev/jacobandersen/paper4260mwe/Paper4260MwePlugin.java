package dev.jacobandersen.paper4260mwe;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Paper4260MwePlugin extends JavaPlugin implements Listener {
    private final Map<UUID, Integer> tasks = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    /**
     * Starts churning a scoreboard every tick with the current time.
     * @param player the player to churn
     */
    void churnScoreboards(Player player) {
        int taskId = getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

            Objective time = scoreboard.registerNewObjective("current_time", "dummy", "Current Time");
            time.setDisplaySlot(DisplaySlot.SIDEBAR);

            player.setScoreboard(scoreboard);

            Score score = time.getScore("current_time");
            score.setScore((int) (System.currentTimeMillis() / 1000));
        }, 0L, 20L);

        tasks.put(player.getUniqueId(), taskId);
    }

    @EventHandler
    void onJoin(PlayerJoinEvent event) {
        churnScoreboards(event.getPlayer());
    }

    @EventHandler
    void onQuit(PlayerQuitEvent event) {
        getServer().getScheduler().cancelTask(tasks.get(event.getPlayer().getUniqueId()));
    }
}
