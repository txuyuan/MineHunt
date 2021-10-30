package plugin.MineHunt.CBoard.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import plugin.MineHunt.CTeam.types.Team;
import plugin.MineHunt.playtime.PlayTimeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardManager {

    static String divider = "§7----------------------§f";

    public static void updateAllBoards(){
        if(Bukkit.getOnlinePlayers().size() > 0)
            for(Player player: Bukkit.getOnlinePlayers())
                updateBoard(player);
    }

    /** Options: 1-Summary, 2-Bounties*/
    public static Boolean updateBoard(Player player){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective summaryObj = scoreboard.registerNewObjective("§a§lMine§c§lHunt", "dummy");
        summaryObj.setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);


        // Get player's team
        Team team = null;
        String playerUuid = player.getUniqueId().toString();
        for(Team tempTeam: Team.getTeams()) {
            if(tempTeam.getMembers().contains(playerUuid)){
                team = tempTeam;break;
            }
        }

        //TEST TODO: REMOVE
        List<Score> scores = getScores(player, team, summaryObj);
        setScores(scores);
        player.setScoreboard(scoreboard);
        return true;
    }



    private static void setScores(List<Score> scores){
        int i = scores.size();
        for(Score score: scores){
            if(i<1) break;
            score.setScore(i);
            i--;
        }
    }



    private static List<Score> getScores(Player player, Team team, Objective obj){
        List<String> scores = new ArrayList<>();
        String elapsedPlayTime = PlayTimeManager.convertSecondsToString(PlayTimeManager.getPlayTime(player));
        String leftPlayTime = PlayTimeManager.convertSecondsToString(PlayTimeManager.getRemainingPlayTime(player));

        scores.add(divider);
        if(!player.hasPermission("minehunt.playtime")){
            scores.add("§bPlaytime elapsed§f: " + elapsedPlayTime);
            scores.add("§bPlaytime left§f: " + leftPlayTime);
        }else scores.add("§bPlaytime§f: §cBypass/r");

        if(team!=null)
            scores.add("§bPoints§f (" + team.getAliasColoured() + ") : " + team.getPoints());

        scores.add(divider + "§f§f");

        scores.add("§bPlayer Bounties§f: ");
        scores.add("> §aPlayer 1");
        scores.add("> §aPlayer 2");
        scores.add("> §aPlayer 3");
        scores.add(divider + "§f§f§f");
        scores.add("§bItem Bounties§f: ");
        scores.add("> Lime Concrete §7(60)§f");
        scores.add("> Wither Skeleton Skull §7(2)§f");
        scores.add("> Nether Quartz Ore §7(47)§f");

        return scores.stream().map(obj::getScore).collect(Collectors.toList());
    }



}