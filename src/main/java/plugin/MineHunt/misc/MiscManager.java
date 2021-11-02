package plugin.MineHunt.misc;

import org.bukkit.Bukkit;
import plugin.MineHunt.CBoard.managers.ScoreboardManager;
import plugin.MineHunt.misc.commands.MineHuntCommand;
import plugin.MineHunt.misc.completers.MineHuntCompleter;

public class MiscManager {

    public static void start(){
        Bukkit.getServer().getPluginCommand("minehunt").setExecutor(new MineHuntCommand());
        Bukkit.getServer().getPluginCommand("minehunt").setTabCompleter(new MineHuntCompleter());
        ScoreboardManager.scheduleBoards();
    }



}
