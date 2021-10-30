package plugin.MineHunt.bounties.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BountyCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if((sender instanceof Player) && !sender.hasPermission("cteam.admin"))
            return new ArrayList<>();

        List<String> completions = parse(args);
        String lastArg = args[args.length - 1];
        completions = completions.stream().filter(str -> str.indexOf(lastArg) == 0).sorted().collect(Collectors.toList());

        return completions;
    }


    private List<String> parse(String[] args) {
        if(args.length == 1)
            return Arrays.asList("create", "info", "complete");
        return switch(args[0]){
            case "create" -> createOptions(args);
            case "info", "complete" -> bountyInfoOptions(args);
            default -> new ArrayList<>();
        };
    }



    private List<String> createOptions(String[] args) {
        if (args.length == 2)
            return Arrays.asList("item", "player");

        if (args[1].equalsIgnoreCase("item"))
            return switch (args.length) {
                case 3 -> Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList());
                case 4 -> Arrays.asList("<amount>");
                case 5 -> Arrays.asList("<reward>");
                default -> new ArrayList<>();
            };

        if (args[1].equalsIgnoreCase("player"))
            return switch (args.length) {
                case 3 -> Arrays.asList("<player>");
                case 4 -> Arrays.asList("<reward>");
                default -> new ArrayList<>();
            };
        return new ArrayList<>();
    }



    private List<String> bountyInfoOptions(String[] args){
        if(args.length == 2)
            return Arrays.asList("item", "player");

        if(args[1].equalsIgnoreCase("item") && args.length == 3)
            return Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList());
        if(args[1].equalsIgnoreCase("player") && args.length == 3)
            return Arrays.asList("<player>");

        return new ArrayList<>();
    }



}
