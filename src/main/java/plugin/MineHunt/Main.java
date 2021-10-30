package plugin.MineHunt;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.MineHunt.CBoard.listeners.PlayerJoinListener;
import plugin.MineHunt.CBoard.managers.ScoreboardManager;
import plugin.MineHunt.CTeam.CTeamManager;
import plugin.MineHunt.bounties.BountyManager;
import plugin.MineHunt.misc.MiscManager;

import java.util.logging.Level;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getDataFolder().mkdir();

        MiscManager.start();
        CTeamManager.start();
        BountyManager.start();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        ScoreboardManager.updateAllBoards();

        logInfo("§b(Status)§f MineHunt enabled successfully");

    }

    @Override
    public void onDisable() {

        HandlerList.unregisterAll(new PlayerJoinListener());

        logInfo("§b(Status)§f MineHunt disabled successfully");
    }



    public static Plugin getInstance() {
        return Bukkit.getPluginManager().getPlugin("MineHunt");
    }

    public static void logInfo(String msg) {
        getInstance().getLogger().log(Level.INFO, msg);
    }

    public static void logError(Exception e){
        getInstance().getLogger().log(Level.SEVERE, "§c(Error)§f Error: \n" + e.getStackTrace().toString());
    }

    public static void testLog(String msg) {
        boolean isDebug = true;
        if (isDebug) logInfo("§cTest: " + msg);
    }



}