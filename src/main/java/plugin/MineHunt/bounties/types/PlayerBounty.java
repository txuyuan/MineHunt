package plugin.MineHunt.bounties.types;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import plugin.MineHunt.CTeam.types.Team;

import java.util.Map;

public class PlayerBounty extends Bounty implements ConfigurationSerializable {

    String playerId;

    public PlayerBounty(String playerId, int reward) {
        super(reward);
        this.playerId = playerId;
    }

    public Boolean complete(Team team){
        if(completed) return false;

        if(team!=null) team.addPoints(reward);
        completed = true;
        return true;
    }

    public OfflinePlayer getPlayer(){
        return Bukkit.getServer().getOfflinePlayer(playerId);
    }



    //Serialization
    @Override
    public Map<String, Object> serialize(){
        Map<String, Object> map = super.serialize();
        map.put("playerId", playerId);
        return map;
    }
    @Override public PlayerBounty deserialize(Map<String, Object> map){return new PlayerBounty(map);}
    @Override public PlayerBounty valueOf(Map<String, Object> map){return new PlayerBounty(map);}
    public PlayerBounty(Map<String, Object> map){
        super(map);
        playerId = (String) map.get("playerId");
    }


}
