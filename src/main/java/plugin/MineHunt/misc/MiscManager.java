package plugin.MineHunt.misc;

import org.bukkit.Bukkit;
import plugin.MineHunt.CBoard.managers.ScoreboardManager;
import plugin.MineHunt.misc.commands.MineHuntCommand;
import plugin.MineHunt.misc.completers.MineHuntCompleter;
import plugin.MineHunt.playtime.PlayTimeManager;

public class MiscManager {

    public static void start(){
        Bukkit.getServer().getPluginCommand("minehunt").setExecutor(new MineHuntCommand());
        Bukkit.getServer().getPluginCommand("minehunt").setTabCompleter(new MineHuntCompleter());
        PlayTimeManager.schedulePlayerTime();
        PlayTimeManager.load(true);
        ScoreboardManager.scheduleBoards();
    }



}
