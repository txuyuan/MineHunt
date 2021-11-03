package plugin.MineHunt.kits.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import plugin.MineHunt.Main;
import plugin.MineHunt.kits.KitManager;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if((sender instanceof Player) && !sender.hasPermission("minehunt.admin")){
            sender.sendMessage("§c(Error)§f You do not have permission to do this");
            return true;
        }
        if(args.length < 3){
            sender.sendMessage("§c(Error)§f XYZ location of container required");
            return true;
        }

        int x,y,z;
        try{x = Integer.parseInt(args[0]);}
        catch(NumberFormatException e){
            sender.sendMessage("§c(Error)§f Invalid integer " + args[0]);
            return true;
        }
        try{y = Integer.parseInt(args[1]);}
        catch(NumberFormatException e){
            sender.sendMessage("§c(Error)§f Invalid integer " + args[1]);
            return true;
        }
        try{z = Integer.parseInt(args[2]);}
        catch(NumberFormatException e){
            sender.sendMessage("§c(Error)§f Invalid integer " + args[2]);
            return true;
        }

        sender.sendMessage(parseKit(sender, args, x, y, z));
        return true;
    }


    private static String parseKit(CommandSender sender, String[] args, int x, int y, int z){
        Block block;
        if(sender instanceof Player){
            Player player = (Player) sender;
            block = player.getWorld().getBlockAt(x,y,z);
        }else if(sender instanceof BlockCommandSender){
            BlockCommandSender blockCommandSender = (BlockCommandSender) sender;
            block = blockCommandSender.getBlock().getWorld().getBlockAt(x,y,z);
        }else return "§c(Error)§f You must be a player or a command block to use this command";

        Container container = (Container) block.getState();

        // No specified target
        if(args.length < 4){
            if(sender instanceof Player) { //Assume sender is target
                KitManager.giveItems(container, (Player) sender);
                return "§b(Status)§f Kit from specified block has been given to you";
            }
            if(!(sender instanceof BlockCommandSender)) return "§b(Status)§f Target player required";

            // Cmdblock sender
            BlockCommandSender cmdSender = (BlockCommandSender) sender;
            Location loc = cmdSender.getBlock().getLocation();
            World world = loc.getWorld();

            if(world.getPlayers().size() == 0)return "§c(Error)§f No players in the current world";

            List<Player> players = loc.getWorld().getNearbyEntities(loc, 100, 100, 100).stream().filter(e -> e instanceof Player).map(e -> (Player)e).collect(Collectors.toList());
            Main.testLog(players.toString());
            Player player = players.stream().sorted(Comparator.comparing(p -> loc.distance(p.getLocation()))).findFirst().orElse(null);
            Main.testLog(player.getName());

            KitManager.giveItems(container, player);
            return "§b(Status)§f Kit from specified block given to " + player.getName();
        }

        String playerName = args[3];
        if(!Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()).contains(playerName)) return "§c(Error)§f Invalid player " + args[3];
        Player player = Bukkit.getPlayer(playerName);
        KitManager.giveItems(container, player);

        return "§b(Status)§f Kit from specified block to " + player.getName();
    }



}
