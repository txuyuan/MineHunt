package plugin.MineHunt.CTeam.completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import plugin.MineHunt.CTeam.managers.TeamManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CPointsCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if((sender instanceof Player) && !sender.hasPermission("cteam.admin"))
            return new ArrayList<>();

        List<String> completions = parse(args);

        String lastArg = args[args.length - 1];
        completions = completions.stream().filter(str -> str.indexOf(lastArg) == 0).sorted().collect(Collectors.toList());
        return completions;
    }

    private static List<String> parse(String[] args){
        if(args.length == 1) return Arrays.asList("add", "remove", "bypass");

        if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")){
            if(args.length == 2) return TeamManager.getAliases();
            if(args.length == 3) return Arrays.asList("<points>");
        }

        if(args[0].equalsIgnoreCase("bypass")){
            if(args.length == 2) return Arrays.asList("true", "false");
        }

        return new ArrayList<>();
    }



}
