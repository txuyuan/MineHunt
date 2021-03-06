package plugin.MineHunt.CTeam.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugin.MineHunt.CTeam.managers.Colour;
import plugin.MineHunt.CTeam.managers.TeamManager;
import plugin.MineHunt.CTeam.types.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CTeam implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if((sender instanceof Player) && !sender.hasPermission("cteam.admin")){
            sender.sendMessage("§c(Error)§f You do not have permission to do this");
            return true;
        }
        if(args.length < 1){
            sender.sendMessage("§c(Error)§f No arguments passed");
            return true;
        }

        String msg = switch(args[0]){
            case "create" -> createGroup(args);
            case "delete" -> deleteGroup(args);
            case "add" -> addMember(args, true);
            case "remove" -> addMember(args, false);
            case "info" -> listInfo(args);
            case "update" -> updateTeam();
            default -> "§c(Error)§f Unrecognised argument " + args[0];
        };

        sender.sendMessage(msg);
        return true;
    }



    private static String updateTeam(){
        List<Team> teams = Team.getTeams();
        if(teams!=null && teams.size() > 0) teams.forEach(team -> team.saveTeam());
        return "§b(Status)§f Teams updated";
    }


    private static String listInfo(String[] args){
        if(args.length < 2)
            return "§c(Error)§f Alias required";
        String alias = args[1];
        Team team = Team.getTeam(alias);
        if(team==null) return "§c(Error)§f " + alias + " is not a valid team";

        return TeamManager.getTeamInfo(team);
    }


    private static String addMember(String[] args, boolean isAdd){
        if(args.length < 2)
            return "§c(Error)§f Alias and player name required";
        if(args.length < 3)
            return "§c(Error)§f Player name required";
        String alias = args[1];
        String playerName = args[2];

        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
        if(player == null)
            return "§c(Error)§f " + playerName + " has not logged on before";

        String error = isAdd ? TeamManager.addMember(alias, player) : TeamManager.removeMember(alias, player);
        if(error != null) return error;
        return "§b(Status)§f " + playerName + (isAdd ? " added to " : " removed from ") +  Team.getTeamName(alias) + " (" + alias + ")";
    }


    private static String deleteGroup(String[] args){
        if(args.length < 2)
            return "§c(Error)§f Alias required";
        String alias = args[1];
        Team team = Team.getTeam(alias);
        if(team==null) return "§c(Error)§f A team with alias " + alias + " does not exist ";

        team.deleteTeam();
        String nameAlias = team.getNameAlias();

        return "§b(Status)§f " + nameAlias + " deleted successfully";
    }


    private static String createGroup(String[] args){

        switch (args.length){
            case 1: return "§c(Error)§f Name, alias, colour and players required";
            case 2: return "§c(Error)§f Alias, colour and players required";
            case 3: return "§c(Error)§f Colour and players required";
            case 4: return "§c(Error)§f Players required";
        }

        String name = args[1];
        String alias = args[2].toUpperCase();
        if(alias.length()!=3)
            return "§c(Error)§f Specified alias invalid. Must be length 3";

        Character colourChar = Colour.getColourCode(args[3]);
        if(colourChar==null)
            return "§c(Error)§f Specified colour invalid";
        char colour = colourChar;

        List<Team> teams = Team.getTeams();
        List<String> names = teams.stream().map(team -> team.getName()).collect(Collectors.toList());
        if(names.contains(name)) return "§c(Error)§f Name unavailable";
        List<String> aliases = teams.stream().map(team -> team.getAlias()).collect(Collectors.toList());
        if(aliases.contains(alias)) return "§c(Error)§f Alias unavailable";

        List<String> playerUuids = new ArrayList<>();
        teams.forEach(team -> team.getMembers().forEach(member -> playerUuids.add(member)));

        List<String> playerNames = new ArrayList<>();
        List<String> players = new ArrayList<>();
        List<String> errorPlayers = new ArrayList<>();
        List<String> unavailPlayers = new ArrayList<>();

        for(int i = 4; i<=args.length-1; i++){
            String playerName = args[i];
            OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
            if(player == null) {
                errorPlayers.add(playerName); continue;}
            if(playerUuids.contains(player.getUniqueId().toString())) {
                unavailPlayers.add(playerName); continue;}

            players.add(player.getUniqueId().toString());
            playerNames.add(player.getName());
        }
        if(errorPlayers.size() > 0)
            return "§c(Error)§f " + String.join(", ", errorPlayers) + (errorPlayers.size()>1 ? " have" : " has") + " not logged on before";
        if(unavailPlayers.size() > 0)
            return "§c(Error)§f " + String.join(", ", unavailPlayers) + (unavailPlayers.size()>1 ? "are" : "is") + " already in another group";
        if(players.size()==0)
            return "§c(Error)§f Specified players invalid";

        Team team = TeamManager.createTeam(name, alias, colour, players);

        return "§b(Status)§f Team created\n" + TeamManager.getTeamInfo(team);
    }



}
