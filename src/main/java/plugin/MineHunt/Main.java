package plugin.MineHunt;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.MineHunt.CTeam.commands.CPoints;
import plugin.MineHunt.CTeam.commands.CTeam;
import plugin.MineHunt.CTeam.completers.CPointsCompleter;
import plugin.MineHunt.CTeam.completers.CTeamCompleter;
import plugin.MineHunt.CTeam.types.Team;

import java.io.IOException;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getDataFolder().mkdir();

        ConfigurationSerialization.registerClass(Team.class, "Team");
        getCommand("cteam").setExecutor(new CTeam());
        getCommand("cteam").setTabCompleter(new CTeamCompleter());
        getCommand("cpoints").setExecutor(new CPoints());
        getCommand("cpoints").setTabCompleter(new CPointsCompleter());

        logInfo("§b(Status)§f Plugin enabled successfully");

    }

    @Override
    public void onDisable() {


        logInfo("§b(Status)§f Plugin disabled successfully");
    }



    public static Plugin getInstance() {
        return Bukkit.getPluginManager().getPlugin("MineHunt");
    }

    public static void logInfo(String msg) {
        getInstance().getLogger().log(Level.INFO, msg);
    }

    public static void logDiskError(IOException e) {
        getInstance().getLogger().log(Level.SEVERE, "§c(Error)§f Error writing to disk: \n" + e.getStackTrace().toString());
    }

    public static void testLog(String msg) {
        boolean isDebug = false;
        if (isDebug) logInfo("§cTest: " + msg);
    }

}