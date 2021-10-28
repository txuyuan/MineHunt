package plugin.MineHunt.CTeam.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import plugin.MineHunt.CTeam.types.Team;
import plugin.MineHunt.Main;

import java.util.List;
import java.util.UUID;

public class CPoints implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player && !sender.hasPermission("cteam.admin")){
            sender.sendMessage("§c(Error)§f You do not have permission to do this");
            return true;
        }
        if(args.length < 1){
            sender.sendMessage("§c(Error)§f No arguments passed");
            return true;
        }

        String msg = switch(args[0]){
            case "add" -> addPoints(args, true);
            case "remove" -> addPoints(args, false);
            default -> "§c(Error)§f Unrecognised argument";
        };

        if(!(sender instanceof Player)) Main.logInfo(msg);
        else sender.sendMessage(msg);
        return true;
    }


    private static String addPoints(String[] args, boolean isAdd){
        String operation = isAdd ? " addition " : " subtraction ";
        String operator = isAdd ? " added to" : "removed from ";

        if(args.length < 2) return "§c(Error)§f Alias and point" + operation + "required";
        if(args.length < 3) return "§c(Error)§f Point" + operation + "required";
        String alias = args[1];
        String pointString = args[2];
        int points;
        try{points = Integer.parseInt(pointString);}
        catch(NumberFormatException e){return "§c(Error)§f " + pointString + " is invalid point" + operation;}
        if(!isAdd) points = -points;

        Team team = Team.getTeam(alias);
        if(team==null) return "§c(Error)§f " + alias + " is invalid team";

        team.addPoints(points);
        String msg = "§b(Status)§f " + Math.abs(points) + (isAdd ? " added to " : " removed from ");

        team.getMembers().forEach(uuid -> {
            OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
            if(offPlayer != null && offPlayer.isOnline())
                ((Player) offPlayer).sendMessage(msg + "your team");
        });

        return msg + team.getNameAlias();
    }



}
