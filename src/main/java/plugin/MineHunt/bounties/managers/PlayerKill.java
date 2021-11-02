package plugin.MineHunt.bounties.managers;

import org.bukkit.entity.Player;
import plugin.MineHunt.CTeam.managers.TeamManager;
import plugin.MineHunt.CTeam.types.Team;
import plugin.MineHunt.bounties.types.PlayerBounty;
import plugin.MineHunt.playtime.PlayTimeManager;

public class PlayerKill {

    public static void playerKillPoints(Player player, Player killer){
        Team playerTeam = Team.getTeam(player);
        Team killerTeam = Team.getTeam(killer);

        if(playerTeam == null || killerTeam == null) return;
        if(playerTeam.getAlias().equalsIgnoreCase(killerTeam.getAlias())) return;

        killerTeam.addPoints(50);
    }

    public static void checkPlayerSurvival(Player player){
        long playTime = PlayTimeManager.getPlayTime(player);
        PlayerBounty bounty = BountyManager.getPlayerBounty(player);
        if(bounty == null || bounty.getCompleted() || playTime < 3600) return;

        bounty.setCompleted(true);

        Team team = Team.getTeam(player);
        if(team!=null) team.addPoints(25);
    }



}
