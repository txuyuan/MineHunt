package plugin.MineHunt.kits;

import org.bukkit.Bukkit;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import plugin.MineHunt.kits.commands.KitCommand;
import plugin.MineHunt.kits.commands.KitCompleter;

public class KitManager {

    public static void giveItems(Container container, Player player) {
        Inventory inv = container.getInventory();
        Inventory playerInv = player.getInventory();
        playerInv.clear();
        for (ItemStack item: inv.getContents())
            if(item!=null) playerInv.addItem(item);
    }

    public static void start(){
        Bukkit.getPluginCommand("ckit").setExecutor(new KitCommand());
        Bukkit.getPluginCommand("ckit").setTabCompleter(new KitCompleter());
    }

}
