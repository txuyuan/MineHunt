package plugin.MineHunt.completers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import plugin.MineHunt.managers.Colour;
import plugin.MineHunt.managers.TeamManager;
import plugin.MineHunt.types.Team;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CTeamCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        
        if((sender instanceof Player) && !sender.hasPermission("minehunt.admin"))
            return Arrays.asList();

        List<String> completions = parse(args);
        String lastArg = args[args.length - 1];
        completions = completions.stream().filter(str -> str.indexOf(lastArg) == 0).sorted().collect(Collectors.toList());

        return completions;
    }


    private static List<String> parse(String[] args){
        if(args.length == 1)
            return Arrays.asList("create", "delete", "add", "remove", "info");
        if(args.length >= 2){
            return switch(args[0]){
                case "delete", "info" -> TeamManager.getAliases();
                case "create" -> createOptions(args);
                case "add" -> addOptions(args);
                case "remove" -> removeOptions(args);
                default -> Arrays.asList();
            };
        }else return Arrays.asList();
    }



    private static List<String> createOptions(String[] args){
        return switch(args.length){
            case 2 -> Arrays.asList("<name>");
            case 3 -> Arrays.asList("<alias>");
            case 4 -> Colour.getColourMap().keySet().stream().sorted().toList();
            default -> Arrays.asList("<playerName>");
        };
    }

    private static List<String> addOptions(String[] args){
        if(args.length == 2)
            return TeamManager.getAliases();
        return Arrays.asList("<playerName>");
    }

    private static List<String> removeOptions(String[] args){
        if(args.length == 2)
            return TeamManager.getAliases();

        String alias = args[1];
        Team team = Team.getTeam(alias);
        if(team == null) return Arrays.asList();

        List<String> playerUuids = team.getMembers();
        List<String> playerNames = Arrays.asList();
        if(playerUuids!=null)
            playerNames = playerUuids.stream().map(uuid -> Bukkit.getOfflinePlayer(uuid).getName()).collect(Collectors.toList());
        return playerNames;
    }



}
