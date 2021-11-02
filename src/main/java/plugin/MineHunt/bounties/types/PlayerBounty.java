package plugin.MineHunt.bounties.types;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import plugin.MineHunt.CBoard.managers.ScoreboardManager;
import plugin.MineHunt.CTeam.types.Team;
import plugin.MineHunt.bounties.managers.BountyManager;

import java.util.Map;
import java.util.UUID;

public class PlayerBounty extends Bounty implements ConfigurationSerializable {

    String playerId;

    public PlayerBounty(String playerId, int reward) {
        super(reward);
        this.playerId = playerId;
    }

    public Boolean complete(Team team){
        if(getCompleted()) return false;

        if(team!=null) team.addPoints(getReward());
        setCompleted(true);
        save();
        return true;
    }

    public OfflinePlayer getPlayer(){
        return Bukkit.getServer().getOfflinePlayer(UUID.fromString(playerId));
    }



    @Override
    public void save(){
        ScoreboardManager.updateAllBoards();
        BountyManager.playerConfig.set(getUniqueId().toString(), this);
        BountyManager.save();
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
