package plugin.MineHunt.bounties.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import plugin.MineHunt.CTeam.types.Team;
import plugin.MineHunt.Main;
import plugin.MineHunt.bounties.BountyManager;
import plugin.MineHunt.bounties.types.ItemBounty;
import plugin.MineHunt.bounties.types.PlayerBounty;

import java.util.List;
import java.util.stream.Collectors;

public class BountyCommand implements CommandExecutor {

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
        Main.testLog("Args: " + String.join(" ", args));

        String msg = switch(args[0]){
            case "create" -> createBounty(args);
            case "info" -> infoBounty(args);
            case "complete" -> completeBounty(args);
            default -> "§c(Error)§f Unrecognised argument " + args[0];
        };

        sender.sendMessage(msg);
        return true;
    }



    private String createBounty(String[] args){
        if(args.length < 2) return "§c(Error)§f Bounty type required";
        return switch(args[1]){
            case "item" -> createItemBounty(args);
            case "player" -> createPlayerBounty(args);
            default -> "§c(Error)§f Unrecognised bounty type " + args[1];
        };
    }

    private String createItemBounty(String[] args){
        if(args.length < 3) return "§c(Error)§f Item name required";
        if(args.length < 4) return "§c(Error)§f Item amount required";
        if(args.length < 5) return "§c(Error)§f Bounty reward required";

        Material item;
        int amount;
        int reward;
        try{item = Material.valueOf(args[2].toUpperCase());}
        catch(IllegalArgumentException e){return "§c(Error)§f " + args[2] + " is not valid material";}
        try{amount = Integer.parseInt(args[3]);}
        catch(NumberFormatException e){return "§c(Error)§f " + args[3] + " is not a valid number";}
        try{reward = Integer.parseInt(args[4]);}
        catch(NumberFormatException e){return "§c(Error)§f " + args[4] + " is not a valid number";}

        BountyManager.createItemBounty(item, amount, reward);
        return "§b(Status)§f Bounty of " + reward + " for " + amount + " " + item.toString() + " created";
    }
    private String createPlayerBounty(String[] args){
        if(args.length < 3) return "§c(Error)§f Player name required";
        if(args.length < 4) return "§c(Error)§f Bounty reward required";

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
        if(player==null) return "§c(Error)§f " + args[2] + " is invalid player";
        int reward;
        try{reward = Integer.parseInt(args[3]);}
        catch(NumberFormatException e){return "§c(Error)§f " + args[3] + " is not a valid number";}

        BountyManager.createPlayerBounty(player, reward);
        return "§b(Status)§f Bounty of " + reward + " for " + player.getName() + " created";
    }



    private String infoBounty(String[] args){
        Main.testLog("Parsing info");
        if(args.length < 2) return "§c(Error)§f Bounty type required";
        return switch(args[1]){
            case "item" -> infoItemBounty(args);
            case "player" -> infoPlayerBounty(args);
            default -> "§c(Error)§f Unrecognised bounty type " + args[1];
        };
    }
    private String infoItemBounty(String[] args){
        Main.testLog("Bounty type: info");
        if(args.length < 3) return "§c(Error)§f Item name required";

        Material item;
        try{item = Material.valueOf(args[2].toUpperCase());}
        catch(IllegalArgumentException e){return "§c(Error)§f " + args[2] + " is not valid material";}
        Main.testLog("Item: " + item.toString());

        ItemBounty bounty = BountyManager.getItemBounty(item);
        if(bounty==null) return "§c(Error)§f No bounty found";

        String msg = "§3(Info)§f - Item Bounty Information" +
                "\n§aItem§f >> " + item.toString() +
                "\n§aAmount§f >> " + bounty.getQuantity() +
                "\n§aReward§f >> " + bounty.getReward() +
                "\n§aIsCompleted§f >>  " + bounty.getCompleted();
        return msg;
    }
    private String infoPlayerBounty(String[] args){
        Main.testLog("Bounty type: player");
        if(args.length < 3) return "§c(Error)§f Player name required";

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
        if(player==null) return "§c(Error)§f " + args[2] + " is invalid player";
        Main.testLog("Player: " + player.getName());

        PlayerBounty bounty = BountyManager.getPlayerBounty(player);
        if(bounty==null) return "§c(Error)§f No bounty found";

        String msg = "§3(Info)§f - Player Bounty Information" +
                "\n§aPlayer§f >> " + player.getName() +
                "\n§aReward§f >> " + bounty.getReward() +
                "\n§aIsCompleted§f >> " + bounty.getCompleted();
        return msg;
    }




    private String completeBounty(String[] args){
        if(args.length < 2) return "§c(Error)§f Bounty type required";

        return switch(args[1]){
            case "item" -> completeItemBounty(args);
            case "player" -> completePlayerBounty(args);
            default -> "§c(Error)§f Unrecognised bounty type " + args[1];
        };
    }
    private String completeItemBounty(String[] args){
        if(args.length < 3) return "§c(Error)§f Item type and target team alias required";
        if(args.length < 4) return "§c(Error)§f Target team alias required";

        // Get item
        Material item;
        try{item = Material.valueOf(args[2].toUpperCase());}
        catch(IllegalArgumentException e){ return "§c(Error)§f " + args[2] + " is not valid material";}

        ItemBounty bounty = BountyManager.getItemBounty(item);
        if(bounty==null) return "§c(Error)§f No bounty found";

        // Get team
        String alias = args[3];
        List<String> teamNames = Team.getTeams().stream().map(Team::getAlias).collect(Collectors.toList());
        if(!teamNames.contains(args[3].toUpperCase())) return "§c(Error)§f " + args[3] + " is not a team alias";
        Team team = Team.getTeam(alias);

        bounty.complete(team);
        return "§b(Status)§f Bounty of " + bounty.getReward() + " for " + bounty.getQuantity() + " " + item.toString() + " completed";
    }

    private String completePlayerBounty(String[] args){
        if(args.length < 3) return "§c(Error)§f Player name required";

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
        if(player==null) return "§c(Error)§f " + args[2] + " is invalid player";
        PlayerBounty bounty = BountyManager.getPlayerBounty(player);
        if(bounty==null) return "§c(Error)§f No bounty found";

        String alias = args[3];
        List<String> teamNames = Team.getTeams().stream().map(Team::getAlias).collect(Collectors.toList());
        if(!teamNames.contains(args[3].toUpperCase())) return "§c(Error)§f " + args[3] + " is not a team alias";
        Team team = Team.getTeam(alias);

        bounty.complete(team);
        return "§b(Status)§f Bounty of " + bounty.getReward() + " for " + player.getName() + " completed";
    }



}
