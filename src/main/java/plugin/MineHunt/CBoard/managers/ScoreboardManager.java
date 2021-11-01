package plugin.MineHunt.CBoard.managers;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import plugin.MineHunt.CBoard.types.ScoreboardData;
import plugin.MineHunt.CTeam.types.Team;
import plugin.MineHunt.Main;
import plugin.MineHunt.bounties.BountyManager;
import plugin.MineHunt.bounties.types.ItemBounty;
import plugin.MineHunt.bounties.types.PlayerBounty;
import plugin.MineHunt.playtime.PlayTimeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ScoreboardManager {

    private static boolean scheduled = false;
    private static String divider = "§7---------------------------§f";

    public static void scheduleBoards(){
        if (!scheduled) new BukkitRunnable(){
            @Override
            public void run() {
                updateAllBoards();
                ScoreboardData.toggleMode();
                scheduled = true;
            }
        }.runTaskTimer(Main.getInstance(), 20, 100);
    }



    public static void updateAllBoards(){updateAllBoards(ScoreboardData.getType());}

    public static void updateAllBoards(ScoreboardData.ScoreboardType type){
        if(Bukkit.getOnlinePlayers().size() > 0)
            for(Player player: Bukkit.getOnlinePlayers())
                updateBoard(player, type);
    }

    /** Options: 1-Summary, 2-Bounties*/
    public static Boolean updateBoard(Player player, ScoreboardData.ScoreboardType type){
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

        List<Score> scores;
        if(type == ScoreboardData.ScoreboardType.INFO) scores = getInfoScores(player, team, summaryObj);
        else scores = getTeamsScores(team, summaryObj);

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



    private static List<Score> getTeamsScores(Team team, Objective obj){
        List<String> scores = new ArrayList<>();

        scores.add(divider);
        List<Team> teams = Team.getTeams();
        if(teams.size() > 0) {
            scores.add("§bPoints:");
            for (Team tempTeam : teams)
                scores.add("> " + tempTeam.getNameAlias() + " : " + tempTeam.getPoints());
        }else scores.add("§cNo existing teams");
        scores.add(divider + "§f");

        return scores.stream().map(obj::getScore).collect(Collectors.toList());
    }

    private static List<Score> getInfoScores(Player player, Team team, Objective obj){
        List<String> scores = new ArrayList<>();
        String elapsedPlayTime = PlayTimeManager.convertSecondsToString(PlayTimeManager.getPlayTime(player));
        String leftPlayTime = PlayTimeManager.convertSecondsToString(PlayTimeManager.getRemainingPlayTime(player));

        scores.add(divider);
        if(!player.hasPermission("minehunt.playtime")){
            scores.add("§bPlaytime elapsed§f: " + elapsedPlayTime);
            scores.add("§bPlaytime left§f: " + leftPlayTime);
        }else scores.add("§bPlaytime§f: §cBypass");

        if(team!=null)
            scores.add("§bPoints§f (" + team.getAliasColoured() + ") : " + team.getPoints());

        scores.add(divider + "§f§f");


        List<PlayerBounty> playerBounties = BountyManager.getPlayerBounties().stream().filter(bounty -> !bounty.getCompleted()).collect(Collectors.toList());
        if(playerBounties.size() > 0){
            scores.add("§bPlayer Bounties§f: ");
            playerBounties.forEach(bounty -> scores.add("> §a" + bounty.getPlayer().getName() + " §f - §c" + bounty.getReward()));
            scores.add(divider + "§f§f§f");
        }

        List<ItemBounty> itemBounties = BountyManager.getItemBounties().stream().filter(bounty -> !bounty.getCompleted()).collect(Collectors.toList());
        if(itemBounties.size() > 0){
            scores.add("§bItem Bounties§f: ");
            itemBounties.forEach(bounty -> {
                String itemName = StringUtils.capitalize(bounty.getItem().name().replace("_", " ").toLowerCase(Locale.ENGLISH));
                scores.add("> §a" + itemName + " §7(" + bounty.getQuantity() + ")§f - §c" + bounty.getReward());
            });
            scores.add(divider + "§f§f§f§f");
        }

        return scores.stream().map(obj::getScore).collect(Collectors.toList());
    }



}
