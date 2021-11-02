package plugin.MineHunt.bounties.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.MineHunt.Main;
import plugin.MineHunt.bounties.managers.BountyManager;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        new BukkitRunnable(){
            @Override
            public void run() {
                BountyManager.checkPlayerBounty(event);
            }
        }.runTaskLater(Main.getInstance(), 1);
    }



}
