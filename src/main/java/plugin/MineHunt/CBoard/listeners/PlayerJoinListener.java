package plugin.MineHunt.CBoard.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import plugin.MineHunt.CBoard.managers.ScoreboardManager;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent e) {
        ScoreboardManager.updateBoard(e.getPlayer());
    }



}
