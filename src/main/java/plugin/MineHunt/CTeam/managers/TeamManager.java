package plugin.MineHunt.CTeam.managers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import plugin.MineHunt.CTeam.types.Team;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TeamManager {

    public static Team createTeam(String name, String alias, char colour, List<String> players){

        if(name.length()==0){
            new InvalidParameterException("Invalid name " + name).printStackTrace();
            return null;
        }
        if(alias.length()==0) {
            new InvalidParameterException("Invalid alias: " + alias).printStackTrace();
            return null;
        }

        Team team = new Team(players, name, alias, colour, 0);

        return team;
    }


    public static void deleteTeam(String alias){
        Team team = Team.getTeam(alias);
        if(team==null) new IOException("Team of alias: " + alias + " cannot be deleted (does not exist)").printStackTrace();
        team.deleteTeam();
    }


    /** Returns null if success */
    public static String removeMember(String alias, OfflinePlayer player){
        Team team = Team.getTeam(alias);
        if(team == null)
            return "§c(Error)§f A team with alias " + alias + " does not exist ";
        if(!team.getMembers().contains(player.getUniqueId().toString()))
            return "§c(Error)§f " + alias + " does not have " + player.getName() + " as a member";
        team.removeMember(player.getUniqueId().toString());
        return null;
    }


    /** Returns null if success */
    public static String addMember(String alias, OfflinePlayer player){
        Team team = Team.getTeam(alias);
        if(team == null)
            return "§c(Error)§f A team with alias " + alias + " does not exist ";
        team.addMember(player.getUniqueId().toString());
        return null;
    }

    public static String getTeamInfo(Team team){
        List<String> playerNames = team.getMembers().stream().map(uuid -> Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName()).collect(Collectors.toList());

        String msg = "§3(Info)§f --- Team Information" +
                "\n§aAlias§f >> §" + team.getAliasColoured() +
                "\n§aName§f >> §" + team.getNameColoured() +
                "\n§aPoints§f >> " + team.getPoints() +
                "\n§aMembers§f >> " + String.join(", ", playerNames);
        return msg;
    }



    public static List<String> getAliases(){
        List<Team> teams = Team.getTeams();
        if(teams==null) return new ArrayList<>();
        return teams.stream().map(team -> team.getAlias()).collect(Collectors.toList());
    }



}