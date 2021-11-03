package plugin.MineHunt.kits.commands;

import org.bukkit.Bukkit;
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

public class KitCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if((sender instanceof Player) && !sender.hasPermission("minehunt.admin")){
            sender.sendMessage("§c(Error)§f You do not have permission to do this");
            return new ArrayList<>();
        }

        List<String> completions = parse(sender, args);
        String lastArg = args[args.length - 1];
        completions = completions.stream().filter(str -> str.indexOf(lastArg) == 0).sorted().collect(Collectors.toList());

        return completions;
    }

    private List<String> parse(CommandSender sender, String[] args){
        return switch(args.length){
            case 1 -> Arrays.asList("<x>");
            case 2 -> Arrays.asList("<y>");
            case 3 -> Arrays.asList("<z>");
            case 4 -> Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            default -> new ArrayList<>();
        };
    }



}
