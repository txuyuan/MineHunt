package plugin.MineHunt.bounties.types;


import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import plugin.MineHunt.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Bounty implements ConfigurationSerializable {
    //TODO: serialize

    int reward;
    boolean completed;

    static String fileName = "bounties.yml";
    static File file = new File(Main.getInstance().getDataFolder(), fileName);
    static FileConfiguration config = YamlConfiguration.loadConfiguration(file);


    public Bounty(int reward){
        this.reward = reward;
        this.completed = false;
    }

    public Integer getReward(){return reward;}
    public boolean getCompleted() {return completed;}

    public void setReward (int reward){this.reward = reward;}
    public void setCompleted(boolean completed){this.completed = completed;}



    // Disk Tools
    public static void loadFile(){
        if(!file.exists()) {
            try {file.createNewFile();}
            catch(IOException e){Main.logError(e);}
        }
        try { config.load(file);}
        catch (FileNotFoundException e) {Main.logError(e);}
        catch (IOException e) {Main.logError(e);}
        catch(InvalidConfigurationException e){Main.logError(e);}
    }
    public static Boolean saveFile(FileConfiguration fileC){
        try{fileC.save(file); return true;}
        catch(IOException e){return false;}
    }



    // Serialization
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("reward", reward);
        map.put("completed", completed);
        return map;
    }
    public Bounty deserialize(Map<String, Object> map){return new Bounty(map);}
    public Bounty valueOf(Map<String, Object> map){return new Bounty(map);}
    public Bounty(Map<String, Object> map){
        reward = (int) map.get("reward");
        completed = (boolean) map.get("completed");
    }

}
