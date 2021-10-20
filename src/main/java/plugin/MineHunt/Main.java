package plugin.MineHunt;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        getDataFolder().mkdir();



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
        if (isDebug) logInfo("§aTest: " + msg);
    }

}