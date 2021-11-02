package plugin.MineHunt.CTeam.types;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import plugin.MineHunt.CBoard.managers.ScoreboardManager;
import plugin.MineHunt.CTeam.managers.MCTeamManager;
import plugin.MineHunt.CTeam.managers.TeamManager;
import plugin.MineHunt.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Team implements ConfigurationSerializable {

    static final String fileName = "teamData.yml";
    static File file = new File(Main.getInstance().getDataFolder(), fileName);
    static FileConfiguration fileC = YamlConfiguration.loadConfiguration(file);

    private String alias;
    private final ArrayList<String> members;
    private String name;
    private char colourCode;
    private int points;


    //Constructors
    public Team(List<String> members, String name, String alias, char colourCode, int points) {
        this.members = new ArrayList<String>(members);
        this.name = name;
        this.alias = alias.toUpperCase();
        this.colourCode = colourCode;
        this.points = points;
        saveTeam();
    }



    //Modifiers
    public void removeMember(String playerUuid) {members.remove(playerUuid); saveTeam();}
    public void addMember(String playerUuid) {members.add(playerUuid); saveTeam();}
    public void addPoints(int pointDiff) { if(!TeamManager.bypass) this.points = this.points + pointDiff; saveTeam();}
    public void removePoints(int pointDiff){ addPoints(-pointDiff); }
    public void setName(String name) {this.name = name; saveTeam();}
    public void setAlias(String alias) {this.alias = alias.toUpperCase(); saveTeam();}
    public void setColourCode(char colour) {this.colourCode = colour; saveTeam();}
    public void setPoints(int points) {this.points = points; saveTeam();}

    //Getters
    public String getAlias() {return alias;}
    public String getName() {return name;}
    public ArrayList<String> getMembers() {return this.members;}
    public char getColourCode() {return colourCode;}
    public int getPoints() {return points;}
    public String getNameColoured(){return "§" + colourCode + name + "§r";}
    public String getAliasColoured(){return "§" + colourCode + alias + "§r";}
    public String getNameAlias(){return getNameColoured() + " (" + getAliasColoured() + ")";}

    public static String getTeamName(String alias) {return getTeam(alias).getName();}
    public static String getFileName() {return fileName;}





    //Data tools
    private Boolean saveDeleteTeam(boolean isSave){
        fileC.set("teams." + alias, (isSave ? this : null));
        return save(fileC);
    }
    public Boolean saveTeam(){ return saveDeleteTeam(true);}
    public Boolean deleteTeam(){return saveDeleteTeam(false);}

    public static Team getTeam(String alias){
        return (Team) fileC.getObject("teams." + alias, (Class) Team.class);
    }
    public static List<Team> getTeams(){
        List<Team> teams= new ArrayList<>();
        ConfigurationSection configSection = fileC.getConfigurationSection("teams");
        if(configSection==null) return teams;

        List<String> keys = configSection.getKeys(false).stream().toList();
        if(keys!=null) keys.forEach(key ->
                teams.add((Team) fileC.getObject("teams." + key, (Class)Team.class)) );

        return teams;
    }

    /** Returns null if no team*/
    public static Team getTeam(OfflinePlayer player){
        Team team = null;
        for(Team allTeam: getTeams())
            if(allTeam.getMembers().contains(player.getUniqueId().toString())){
                team = allTeam;
                break;
            }
        return team;
    }




    //Disk Utils
    public static void load (){
        if(!file.exists()) {
            try {file.createNewFile();}
            catch(IOException e){Main.logError(e);}
        }
        try { fileC.load(file);}
        catch (FileNotFoundException e) {Main.logError(e);}
        catch (IOException e) {Main.logError(e);}
        catch(InvalidConfigurationException e){Main.logError(e);}
    }
    public static Boolean save(FileConfiguration fileC){
        ScoreboardManager.updateAllBoards();
        MCTeamManager.updateTeams(getTeams());
        try{fileC.save(file); return true;}
        catch(IOException e){return false;}
    }



    //Serialization
    @Override
    public Map<String, Object> serialize(){
        Map<String, Object> serialized = new HashMap<String, Object>();
        serialized.put("alias", alias);
        serialized.put("name", name);
        serialized.put("colour", colourCode);
        serialized.put("points", points);
        serialized.put("members", members);
        return serialized;
    }
    public static Team deserialize(Map<String, Object> map){return new Team(map);}
    public static Team valueOf(Map<String, Object> map){return new Team(map);}
    public Team(Map<String, Object> map){
        alias = map.get("alias").toString();
        name = map.get("name").toString();
        colourCode = map.get("colour").toString().charAt(0);
        points = Integer.valueOf(map.get("points").toString());
        members = (ArrayList<String>) map.get("members");
    }



}
