package plugin.MineHunt.types;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import plugin.MineHunt.Main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Team {

    static final String fileName = "teamData";
    private final List<String> members;
    private String name;
    private String alias;
    private char colour;
    private int points;


    //Constructors
    public Team(List<String> members, String name, String alias, char colour, int points) {
        this.members = members;
        this.name = name;
        this.alias = alias.toLowerCase();
        this.colour = colour;
        this.points = points;
        Main.testLog("members: " + members + ", name: " + name + ", colour: " + colour + ", points: " + points);
        saveYaml();
    }

    /**
     * Returns null if team does not exist
     */
    public static Team getTeam(String alias) {
        FileConfiguration fileC = YamlConfiguration.loadConfiguration(new File("./plugins/MineHunt/" + fileName));

        Map<String, Object> teamMap = fileC.getConfigurationSection("teams." + alias.toLowerCase()).getValues(false);
        if (teamMap == null) return null;
        Main.testLog("Team load: " + teamMap);
        List<String> members = (List<String>) teamMap.get("members");
        String name = (String) teamMap.get("name");
        char colour = (char) teamMap.get("colour");
        int points = (int) teamMap.get("points");

        Team team = new Team(members, name, alias, colour, points);
        return team;
    }

    public static String getTeamName(String alias) {
        return getTeam(alias).getName();
    }

    public static String getFileName() {
        return fileName;
    }

    public static FileConfiguration getTeamFile() {
        FileConfiguration fileC = YamlConfiguration.loadConfiguration(new File("./plugins/MineHunt/" + fileName));
        return fileC;
    }

    public static void saveTeamFile(FileConfiguration fileC) {
        File file = new File("./plugins/MineHunt/" + fileName);
        try {
            fileC.save(file);
        } catch (IOException e) {
            Main.logDiskError(e);
        }
    }

    //Modifiers
    public void removeMember(String playerUuid) {
        members.remove(playerUuid);
    }

    public void addMember(String playerUuid) {
        members.add(playerUuid);
    }

    public void addPoints(int pointDiff) {
        this.points = this.points + pointDiff;
    }

    //Getters
    public List<String> getMembers() {
        return this.members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias.toLowerCase();
    }

    public char getColourCode() {
        return colour;
    }

    public void setColourCode(char colour) {
        this.colour = colour;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    //Disk tools
    public void saveYaml() {
        FileConfiguration fileC = getTeamFile();

        Map<String, Object> team = new HashMap<>();
        team.put("members", members);
        team.put("name", name);
        team.put("colour", colour);
        team.put("int", points);
        Main.testLog("Team save: " + team);
        fileC.createSection("teams." + alias.toLowerCase(), team);

        saveTeamFile(fileC);
    }


}
