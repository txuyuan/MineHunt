package plugin.MineHunt.bounties;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import plugin.MineHunt.CBoard.managers.ScoreboardManager;
import plugin.MineHunt.CTeam.types.Team;
import plugin.MineHunt.Main;
import plugin.MineHunt.bounties.commands.BountyCommand;
import plugin.MineHunt.bounties.commands.BountyCompleter;
import plugin.MineHunt.bounties.listeners.PlayerDeathListener;
import plugin.MineHunt.bounties.types.Bounty;
import plugin.MineHunt.bounties.types.ItemBounty;
import plugin.MineHunt.bounties.types.PlayerBounty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BountyManager {

    private static String playerFileName = "player_bounties.yml";
    public static File playerFile = new File(Main.getInstance().getDataFolder(), playerFileName);
    public static FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);

    private static String itemFileName = "item_bounties.yml";
    public static File itemFile = new File(Main.getInstance().getDataFolder(), itemFileName);
    public static FileConfiguration itemConfig = YamlConfiguration.loadConfiguration(itemFile);



    public static void start(){
        Bukkit.getServer().getPluginCommand("cbounty").setExecutor(new BountyCommand());
        Bukkit.getServer().getPluginCommand("cbounty").setTabCompleter(new BountyCompleter());
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), Main.getInstance());
        ConfigurationSerialization.registerClass(Bounty.class);
        ConfigurationSerialization.registerClass(ItemBounty.class);
        ConfigurationSerialization.registerClass(PlayerBounty.class);
        load();
    }



    public static void load(){
        if(!playerFile.exists()){
            try{playerFile.createNewFile();}
            catch(Exception e){e.printStackTrace();}
        }
        try { playerConfig.load(playerFile);}
        catch (FileNotFoundException e) {Main.logError(e);}
        catch (IOException e) {Main.logError(e);}
        catch(InvalidConfigurationException e){Main.logError(e);}

        if(!itemFile.exists()){
            try{itemFile.createNewFile();}
            catch(Exception e){e.printStackTrace();}
        }
        try { itemConfig.load(itemFile);}
        catch (FileNotFoundException e) {Main.logError(e);}
        catch (IOException e) {Main.logError(e);}
        catch(InvalidConfigurationException e){Main.logError(e);}
    }

    public static void save(){
        try {
            playerConfig.save(playerFile);
            itemConfig.save(itemFile);
            ScoreboardManager.updateAllBoards();
        }
        catch (IOException e) {Main.logError(e);}
        ScoreboardManager.updateAllBoards();
    }



    public static ItemBounty getItemBounty(Material material){
        Main.testLog("Material name: " + material.name());
        for(ItemBounty bounty : getItemBounties()) {
            Main.testLog("Bounty material name: " + bounty.getItem().name());
            if(bounty.getItem().name().equals(material.name()))
                return bounty;
        }
        return null;
    }
    public static ItemBounty getItemBounty(UUID id){
        return (ItemBounty) playerConfig.get(id.toString());
    }
    public static PlayerBounty getPlayerBounty(OfflinePlayer player){
        for(PlayerBounty bounty : getPlayerBounties())
            if(bounty.getPlayer().getUniqueId() == player.getUniqueId())
                return bounty;
        return null;
    }
    public static PlayerBounty getPlayerBounty(UUID id){
        return (PlayerBounty) itemConfig.get(id.toString());
    }



    public static void createItemBounty(Material material, int amount, int reward){
        ItemBounty bounty = new ItemBounty(material, amount, reward);
        bounty.save();
    }

    public static void createPlayerBounty(OfflinePlayer player, int reward){
        PlayerBounty bounty = new PlayerBounty(player.getUniqueId().toString(), reward);
        bounty.save();
    }



    public static List<ItemBounty> getItemBounties(){
        if(itemConfig.getKeys(false).size() > 0)
            return itemConfig.getKeys(false).stream().map(key -> (ItemBounty) itemConfig.get(key)).collect(Collectors.toList());
        return new ArrayList<>();
    }
    public static List<PlayerBounty> getPlayerBounties(){
        if(playerConfig.getKeys(false).size() > 0)
            return playerConfig.getKeys(false).stream().map(key -> (PlayerBounty) playerConfig.get(key)).collect(Collectors.toList());
        return new ArrayList<>();
    }





    public static void checkPlayerBounty(PlayerDeathEvent event){
        if (event.getEntity().getKiller() == null) return;
        Player player = event.getEntity();
        Player killer = event.getEntity().getKiller();

        if(getPlayerBounty(player) != null) return;
        PlayerBounty bounty = getPlayerBounty(player);
        Team killteam = Team.getTeam(killer);
        if(killteam != null) bounty.complete(killteam);
    }



}
