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
        return switch(args.length){
            case 1 -> Arrays.asList("add", "remove");
            case 2 -> TeamManager.getAliases();
            default -> new ArrayList<>();
        };
    }



}
