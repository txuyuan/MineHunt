package plugin.MineHunt.playtime;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.MineHunt.CBoard.managers.ScoreboardManager;
import plugin.MineHunt.Main;
import plugin.MineHunt.bounties.managers.PlayerKill;
import plugin.MineHunt.playtime.commands.PlayTimeCommand;
import plugin.MineHunt.playtime.commands.PlayTimeCompleter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PlayTimeManager {

    public static String fileName = "playtime.yml";
    public static int playTimeLimit = Main.getInstance().getConfig().getInt("playtime-limit");
    public static File file = new File(Main.getInstance().getDataFolder(), fileName);
    public static FileConfiguration config = YamlConfiguration.loadConfiguration(file);


    public static void start(){
        schedulePlayerTime();
        load(true);
        Bukkit.getServer().getPluginCommand("playtime").setExecutor(new PlayTimeCommand());
        Bukkit.getServer().getPluginCommand("playtime").setTabCompleter(new PlayTimeCompleter());
    }



    // Scheduler
    public static void schedulePlayerTime(){
        new BukkitRunnable(){
            @Override
            public void run() {
                for(Player player: Bukkit.getOnlinePlayers()){
                    if(playTimeCheck(player)) addPlayTime(player, 1);
                    PlayerKill.checkPlayerSurvival(player);
                }
                ScoreboardManager.updateAllBoards();
            }
        }.runTaskTimer(Main.getInstance(), 20, 20);
    }

    /** Returns whether player should have timer proceed*/
    public static boolean playTimeCheck(Player player){
        if(player.hasPermission("minehunt.playtime")) return false;
        if(getPlayTime(player) >= playTimeLimit){
            player.kickPlayer("§cYou have exhausted your maximum playtime");
            return false;
        }
        return true;
    }



    public static void load(boolean loadConfig) {
        if(!file.exists()){
            try {file.createNewFile();}
            catch (IOException e) {Main.logError(e);}
        }
        if(loadConfig){
            try { config.load(file);}
            catch (FileNotFoundException e) {Main.logError(e);}
            catch (IOException e) {Main.logError(e);}
            catch(InvalidConfigurationException e){Main.logError(e);}
        }
        if(!Main.getInstance().getConfig().getKeys(false).contains("playtime-limit")) {
            Main.getInstance().getConfig().set("playtime-limit", 9000);
            Main.getInstance().saveConfig();
        }
        playTimeLimit = Main.getInstance().getConfig().getInt("playtime-limit");
    }
    public static void save() {
        load(false);
        try {
            config.save(file);
        } catch (IOException e) {Main.logError(e);}
    }


    public static void resetPlayTime(){
        config.getKeys(false).forEach(key -> config.set(key, 0));
        save();
    }


    public static void addPlayTime(Player player, int timeDiff) {
        if (config.getKeys(false).contains(player.getUniqueId().toString())) {
            long playTime = config.getLong(player.getUniqueId().toString());
            playTime += timeDiff;
            config.set(player.getUniqueId().toString(), playTime);
        } else
            config.set(player.getUniqueId().toString(), timeDiff);
        save();
    }

    public static long getPlayTime(Player player) {
        load(false);
        if (config.contains(player.getUniqueId().toString()))
            return config.getLong(player.getUniqueId().toString());
        else return 0;
    }

    public static void resetPlayTime(Player player) {
        config.set(player.getUniqueId().toString(), 0);
        save();
    }
    public static void resetAllPlayTimes() {
        config.set("", 0);
        save();
    }

    public static long getRemainingPlayTime(Player player){
        return playTimeLimit - getPlayTime(player);
    }
    public static long getPlayTimeLimit(){
        return playTimeLimit;
    }



    //Time utils
    public static String convertSecondsToString(long time){
        long hours = time / 3600;
        long minutes = (time % 3600) / 60;
        long seconds = (time % 3600) % 60;

        String msg = "";
        if(hours!=0) msg += hours + "h ";
        if(minutes!=0 || hours!=0) msg += minutes + "m ";
        msg += seconds + "s";
        return msg;
    }



}