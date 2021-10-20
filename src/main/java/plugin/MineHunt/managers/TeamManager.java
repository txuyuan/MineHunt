package plugin.MineHunt.managers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import plugin.MineHunt.types.Team;

import java.security.InvalidParameterException;
import java.util.List;

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
        FileConfiguration fileC = Team.getTeamFile();
        fileC.set("team." + alias, null);
        Team.saveTeamFile(fileC);
    }


    /** Returns null if success */
    public static String removeMember(String alias, String playerUuid){
        Team team = Team.getTeam(alias);
        if(team == null)
            return "§c(Error)§f A team with alias " + alias + " does not exist ";
        if(!team.getMembers().contains(playerUuid))
            return "§c(Error)§f " + alias + " does not have " + Bukkit.getOfflinePlayer(playerUuid).getName() + " as a member";
        team.removeMember(playerUuid);
        return null;
    }


    /** Returns null if success */
    public static String addMember(String alias, String playerUuid){
        Team team = Team.getTeam(alias);
        if(team == null)
            return "§c(Error)§f A team with alias " + alias + " does not exist ";
        team.addMember(playerUuid);
        return null;
    }



}
