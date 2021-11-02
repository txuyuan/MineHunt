package plugin.MineHunt.CTeam.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import plugin.MineHunt.Main;

import java.util.ArrayList;
import java.util.List;

public class MCTeamManager {

    public static boolean updateTeams(List<plugin.MineHunt.CTeam.types.Team> fileTeams){
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        List<Team> boardTeams = board.getTeams().stream().toList();

        boardTeams.forEach(Team::unregister);
        fileTeams.forEach(team -> createTeam(team.getName(), team.getMembers(), team.getColourCode()));
        return true;
    }


    public static boolean createTeam(String name, @NotNull List<String> playerUuids, char color) {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.registerNewTeam(name);
        team.setColor(ChatColor.getByChar(color));

        List<OfflinePlayer> players = new ArrayList<>();
        playerUuids.forEach(uuid -> {
            if(Bukkit.getOfflinePlayerIfCached(uuid) != null) {
                OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(uuid);
                players.add(player);
                Main.logInfo("UUID: " + uuid + " is invalid to add to vanilla team");
            }
        });

        players.forEach(team::addPlayer);
        team.setCanSeeFriendlyInvisibles(true);
        team.setAllowFriendlyFire(false);
        return true;
    }

    public static Team getTeam(String name){
        return Bukkit.getScoreboardManager().getMainScoreboard().getTeam(name);
    }

    public static List<Team> getTeams(){
        return Bukkit.getScoreboardManager().getMainScoreboard().getTeams().stream().toList();
    }



}
