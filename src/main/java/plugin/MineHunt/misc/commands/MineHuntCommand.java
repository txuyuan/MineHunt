package plugin.MineHunt.misc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import plugin.MineHunt.CTeam.types.Team;
import plugin.MineHunt.playtime.PlayTimeManager;

public class MineHuntCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if((sender instanceof Player) && !sender.hasPermission("minehunt.admin")){
            sender.sendMessage("§c(Error)§f You do not have permission to do this");
            return true;
        }
        if(args.length < 1){
            sender.sendMessage("§c(Error)§f No arguments passed");
            return true;
        }

        String msg = switch(args[0]){
            case "reload" -> reload(args);
            default -> "§c(Error)§f Unrecognised argument";
        };
        sender.sendMessage(msg);

        return true;
    }

    private String reload(String[] args){
        PlayTimeManager.load(true);
        Team.load();
        return "§b(Status)§f Reloaded";
    }


}
