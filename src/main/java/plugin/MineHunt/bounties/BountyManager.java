package plugin.MineHunt.bounties;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import plugin.MineHunt.Main;
import plugin.MineHunt.bounties.commands.BountyCommand;
import plugin.MineHunt.bounties.commands.BountyCompleter;
import plugin.MineHunt.bounties.types.Bounty;
import plugin.MineHunt.bounties.types.ItemBounty;
import plugin.MineHunt.bounties.types.PlayerBounty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BountyManager {

    private static String playerFileName = "player_bounties";
    private static File playerFile = new File(Main.getInstance().getDataFolder(), playerFileName);
    private static FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);

    private static String itemFileName = "item_bounties";
    private static File itemFile = new File(Main.getInstance().getDataFolder(), itemFileName);
    private static FileConfiguration itemConfig = YamlConfiguration.loadConfiguration(itemFile);



    public static void start(){
        Bukkit.getServer().getPluginCommand("cbounty").setExecutor(new BountyCommand());
        Bukkit.getServer().getPluginCommand("cbounty").setTabCompleter(new BountyCompleter());
        load();
        Bounty.loadFile();
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
            itemConfig.save(itemFile);}
        catch (IOException e) {Main.logError(e);}
    }



    public static ItemBounty getItemBounty(Material material){
        for(ItemBounty bounty : getItemBounties())
            if(bounty.getItem().equals(material))
                return bounty;
        return null;
    }
    public static PlayerBounty getPlayerBounty(OfflinePlayer player){
        for(PlayerBounty bounty : getPlayerBounties())
            if(bounty.getPlayer().equals(player))
                return bounty;
        return null;
    }



    public static void createItemBounty(Material material, int amount, int reward){
        ItemBounty bounty = new ItemBounty(material, amount, reward);
        List<ItemBounty> bounties = new ArrayList<>();
        if(!(itemConfig.getList("itemBounties") == null))
            bounties = itemConfig.getList("itemBounties").stream().filter(b -> b instanceof ItemBounty).map(b -> (ItemBounty) b).collect(Collectors.toList());
        bounties.add(bounty);
        itemConfig.set("itemBounties", bounties);
        save();
    }

    public static void createPlayerBounty(OfflinePlayer player, int reward){
        PlayerBounty bounty = new PlayerBounty(player.getUniqueId().toString(), reward);
        List<PlayerBounty> bounties = new ArrayList<>();
        if(!(playerConfig.getList("playerBounties") == null))
            bounties = playerConfig.getList("playerBounties").stream().filter(b -> b instanceof PlayerBounty).map(b -> (PlayerBounty) b).collect(Collectors.toList());
        bounties.add(bounty);
        playerConfig.set("playerBounties", bounties);
        save();
    }

    public static List<ItemBounty> getItemBounties(){
        if(!(playerConfig.getList("itemBounties") == null))
            return itemConfig.getList("itemBounties").stream().filter(b -> b instanceof ItemBounty).map(b -> (ItemBounty) b).collect(Collectors.toList());
        return new ArrayList<>();
    }
    public static List<PlayerBounty> getPlayerBounties(){
        if(!(playerConfig.getList("playerBounties") == null))
            return playerConfig.getList("playerBounties").stream().filter(b -> b instanceof PlayerBounty).map(b -> (PlayerBounty) b).collect(Collectors.toList());
        return new ArrayList<>();
    }

}
