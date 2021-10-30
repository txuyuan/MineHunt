package plugin.MineHunt.misc.completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MineHuntCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if((sender instanceof Player) && !sender.hasPermission("cteam.admin"))
            return new ArrayList<>();

        if(args.length == 1)
            return Arrays.asList("reload");

        return new ArrayList<>();
    }



}
