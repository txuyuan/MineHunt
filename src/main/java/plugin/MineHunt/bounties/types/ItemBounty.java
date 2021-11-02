package plugin.MineHunt.bounties.types;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import plugin.MineHunt.CBoard.managers.ScoreboardManager;
import plugin.MineHunt.CTeam.types.Team;
import plugin.MineHunt.bounties.managers.BountyManager;

import java.util.Map;

public class ItemBounty extends Bounty implements ConfigurationSerializable {

    Material item;
    int quantity;

    public ItemBounty(Material item, int quantity, int reward){
        super(reward);
        this.item = item;
        this.quantity = quantity;
    }

    public Boolean complete(Team team){
        if(getCompleted()) return false;

        if(team!=null)team.addPoints(getReward());
        setCompleted(true);
        save();
        return true;
    }

    public Material getItem(){return item;}
    public int getQuantity(){return quantity;}
    public ItemStack getItemStack(){return new ItemStack(item, quantity);}



    @Override
    public void save(){
        ScoreboardManager.updateAllBoards();
        BountyManager.itemConfig.set(getUniqueId().toString(), this);
        BountyManager.save();
    }

    //Serialization
    @Override
    public Map<String, Object> serialize(){
        Map<String, Object> map = super.serialize();
        map.put("item", item.name());
        map.put("quantity", quantity);
        return map;
    }
    @Override public PlayerBounty deserialize(Map<String, Object> map){return new PlayerBounty(map);}
    @Override public PlayerBounty valueOf(Map<String, Object> map){return new PlayerBounty(map);}
    public ItemBounty(Map<String, Object> map){
        super(map);
        item = Material.valueOf((String) map.get("item"));
        quantity = (int)map.get("quantity");
    }
}
