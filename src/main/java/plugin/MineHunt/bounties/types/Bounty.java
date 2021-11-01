package plugin.MineHunt.bounties.types;


import org.bukkit.configuration.serialization.ConfigurationSerializable;
import plugin.MineHunt.CBoard.managers.ScoreboardManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Bounty implements ConfigurationSerializable {

    int reward;
    boolean completed;
    UUID id;

    public Bounty(int reward){
        this.reward = reward;
        this.completed = false;
        id = UUID.randomUUID();
    }

    public Integer getReward(){return reward;}
    public boolean getCompleted() {return completed;}
    public UUID getUniqueId() {return id;}

    public void setReward (int reward){this.reward = reward; save();}
    public void setCompleted(boolean completed){this.completed = completed; save();}



    public void save(){
        ScoreboardManager.updateAllBoards();
        if(this instanceof ItemBounty) ((ItemBounty) this).save();
        if(this instanceof PlayerBounty) ((PlayerBounty) this).save();
    }

    // Serialization
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("reward", reward);
        map.put("completed", completed);
        map.put("id", id.toString());
        return map;
    }
    public Bounty deserialize(Map<String, Object> map){return new Bounty(map);}
    public Bounty valueOf(Map<String, Object> map){return new Bounty(map);}
    public Bounty(Map<String, Object> map){
        reward = (int) map.get("reward");
        completed = (boolean) map.get("completed");
        id = UUID.fromString( (String) map.get("id") );
    }

}
