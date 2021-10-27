package plugin.MineHunt.CTeam.types;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
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
    public void addPoints(int pointDiff) {this.points = this.points + pointDiff; saveTeam();}
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
    public String getNameColoured(){return "§" + colourCode + name;}
    public String getAliasColoured(){return "§" + colourCode + alias;}
    public String getNameAlias(){return getNameColoured() + " (" + getAliasColoured() + ")";}

    public static String getTeamName(String alias) {return getTeam(alias).getName();}
    public static String getFileName() {return fileName;}



    //Data tools
    private Boolean saveDeleteTeam(boolean isSave){
        FileConfiguration fileC = getTeamFileConfig();

        if(isSave) fileC.set("teams." + alias, this);
        else fileC.set("teams." + alias, null);
        return saveTeamFile(fileC);
    }
    public Boolean saveTeam(){ return saveDeleteTeam(true);}
    public Boolean deleteTeam(){return saveDeleteTeam(false);}

    public static Team getTeam(String alias){
        FileConfiguration fileC = getTeamFileConfig();
        Team team = (Team) fileC.getObject("teams." + alias, (Class) Team.class);
        return team;
    }
    public static List<Team> getTeams(){

        List<Team> teams= new ArrayList<>();
        FileConfiguration fileC = getTeamFileConfig();
        ConfigurationSection configSection = fileC.getConfigurationSection("teams");
        if(configSection==null) return teams;

        List<String> keys = configSection.getKeys(false).stream().toList();
        if(keys!=null) keys.forEach(key ->
                teams.add((Team) fileC.getObject("teams." + key, (Class)Team.class)) );

        return teams;
    }




    //Disk Utils
    private static File getTeamFile() {
        File file = new File("./plugins/MineHunt/" + fileName);
        try{file.createNewFile();}
        catch(IOException e){Main.logDiskError(e);}
        return file;
    }
    public static FileConfiguration getTeamFileConfig(){
        FileConfiguration fileC = YamlConfiguration.loadConfiguration(getTeamFile());
        if(fileC==null) new FileNotFoundException("MineHunt" + fileName + " returned invalid data");
        return fileC;
    }
    public static Boolean saveTeamFile(FileConfiguration fileC){
        try{fileC.save(getTeamFile()); return true;}
        catch(IOException e){return false;}
    }


    @Override
    public Map<String, Object> serialize(){
        Map<String, Object> serialised = new HashMap<String, Object>();
        serialised.put("alias", alias);
        serialised.put("name", name);
        serialised.put("colour", colourCode);
        serialised.put("points", points);
        serialised.put("members", members);
        return serialised;
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
