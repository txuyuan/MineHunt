package plugin.MineHunt.CBoard.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import plugin.MineHunt.CBoard.managers.ScoreboardManager;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        ScoreboardManager.updateBoard(e.getPlayer());
    }



}
