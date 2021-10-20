package plugin.MineHunt.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import plugin.MineHunt.Main;
import plugin.MineHunt.managers.Colour;
import plugin.MineHunt.managers.TeamManager;
import plugin.MineHunt.types.Team;

import java.util.ArrayList;
import java.util.List;

public class CTeam implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if((sender instanceof Player) && !sender.hasPermission("minehunt.admin")){
            sender.sendMessage("§c(Error)§f You do not have permission to do this");
            return true;
        }
        if(args.length < 1){
            sender.sendMessage("§c(Error)§f No arguments passed");
            return true;
        }

        String msg = "";
        switch(args[0]){
            case "create":
                msg = createGroup(args);
                break;
            case "delete":
                msg = deleteGroup(args);
                break;
            case "add":
                msg = addMember(args, true);
                break;
            case "remove":
                msg = addMember(args, false);
                break;
            case "info":
                msg = listInfo(args);
                break;
            default:
                sender.sendMessage("§c(Error)§f Unrecognised argument " + args[0]);
                return true;
        }
        Main.logInfo(msg);
        sender.sendMessage(msg);
        return true;
    }




    private static String listInfo(String[] args){
        if(args.length < 2)
            return "§c(Error)§f Alias required";
        String alias = args[1];
        Team team = Team.getTeam(alias);
        if(team==null) return "§c(Error)§f " + alias + " is not a valid team";

        String msg = "§3(Info)§f --- §" + team.getColourCode() + team.getAlias();
        msg.concat("\n§aName§f >> " + team.getName());
        msg.concat("\n§aMembers§f >>");
        team.getMembers().forEach(member -> msg.concat("\n- " + member));
        return msg;
    }


    private static String addMember(String[] args, boolean isAdd){
        if(args.length < 2)
            return "§c(Error)§f Alias and player name required";
        if(args.length < 3)
            return "§c(Error)§f Player name required";
        String alias = args[1];
        String playerName = args[2];

        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
        if(player == null)
            return "§c(Error)§f " + playerName + " has not logged on before";
        String playerUuid = player.getUniqueId().toString();


        String error = isAdd ? TeamManager.addMember(alias, playerUuid) : TeamManager.removeMember(alias, playerUuid);
        if(error != null) return error;
        return "§b(Status)§f " + playerName + (isAdd ? " added to " : " removed from ") +  Team.getTeamName(alias) + " (" + alias + ")";
    }


    private static String deleteGroup(String[] args){
        if(args.length < 2)
            return "§c(Error)§f Alias required";
        String alias = args[1];
        if(Team.getTeam(alias) == null)
            return "§c(Error)§f A team with alias " + alias + " does not exist ";
        TeamManager.deleteTeam(alias);

        return "§b(Status)§f Team " + Team.getTeamName(alias) + " (" + alias + ") deleted successfully";
    }


    private static String createGroup(String[] args){

        switch(args.length){
            case 1:
                return "§c(Error)§f Name, alias, colour and players required";
            case 2:
                return "§c(Error)§f Alias, colour and players required";
            case 3:
                return "§c(Error)§f Colour and players required";
            case 4:
                return "§c(Error)§f Players required";
            default: break;
        }

        String name = args[1];
        String alias = args[2].toUpperCase();
        if(alias.length()!=3)
            return "§c(Error)§f Specified alias invalid. Must be length 3";

        Character colourChar = Colour.getColourCode(args[3]);
        if(colourChar==null)
            return "§c(Error)§f Specified colour invalid";
        char colour = colourChar;

        List<String> playerNames = new ArrayList<>();
        List<String> players = new ArrayList<>();
        for(int i = 4; i>=args.length; i++){
            String playerName = args[i];
            OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
            if(player == null) continue;
            players.add(player.getUniqueId().toString());
            playerNames.add(player.getName());
        }
        if(players.size()==0)
            return "§c(Error)§f Specified players invalid";

        TeamManager.createTeam(name, alias, colour, players);

        return "§b(Status)§f Team created. Name: " + name + ", alias: " + alias + ", colour: " + args[3] + ", players: " + String.join(", " + playerNames);
    }


}
