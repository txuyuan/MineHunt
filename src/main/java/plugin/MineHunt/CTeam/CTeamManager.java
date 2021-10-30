package plugin.MineHunt.CTeam;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import plugin.MineHunt.CTeam.commands.CPoints;
import plugin.MineHunt.CTeam.commands.CTeam;
import plugin.MineHunt.CTeam.completers.CPointsCompleter;
import plugin.MineHunt.CTeam.completers.CTeamCompleter;
import plugin.MineHunt.CTeam.types.Team;

public class CTeamManager {

    public static void start(){
        ConfigurationSerialization.registerClass(Team.class, "Team");
        Bukkit.getServer().getPluginCommand("cteam").setExecutor(new CTeam());
        Bukkit.getServer().getPluginCommand("cteam").setTabCompleter(new CTeamCompleter());
        Bukkit.getServer().getPluginCommand("cpoints").setExecutor(new CPoints());
        Bukkit.getServer().getPluginCommand("cpoints").setTabCompleter(new CPointsCompleter());
        Team.load();
    }



}
