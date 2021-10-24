package plugin.MineHunt.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import plugin.MineHunt.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Team {

    static final String fileName = "teamData";

    private String alias;
    private final List<String> members;
    private String name;
    private char colourCode;
    private int points;


    //Constructors
    public Team(List<String> members, String name, String alias, char colourCode, int points) {
        this.members = members;
        this.name = name;
        this.alias = alias.toUpperCase();
        this.colourCode = colourCode;
        this.points = points;
        Main.testLog("members: " + members + ", name: " + name + ", colour: " + colourCode + ", points: " + points);
        saveTeam();
    }
    //Default constructor for Jackson
    public Team(){
        members = new ArrayList<>();
        name = "placeholder";
        alias = "placeholder";
        colourCode = 'f';
        points = 0;
    }



    //Modifiers
    public void removeMember(String playerUuid) {members.remove(playerUuid); saveTeam();}
    public void addMember(String playerUuid) {members.add(playerUuid); saveTeam();}
    public void addPoints(int pointDiff) {this.points = this.points + pointDiff; saveTeam();}
    public void setName(String name) {this.name = name; saveTeam();}
    public void setAlias(String alias) {this.alias = alias.toUpperCase(); saveTeam();}
    public void setColourCode(char colour) {this.colourCode = colour; saveTeam();}
    public void setPoints(int points) {this.points = points; saveTeam();}



    //Getters
    public String getAlias() {return alias;}
    public String getName() {return name;}
    public List<String> getMembers() {return this.members;}
    public char getColourCode() {return colourCode;}
    public int getPoints() {return points;}
    @JsonIgnore public String getNameColoured(){return "§" + colourCode + name;}
    @JsonIgnore public String getAliasColoured(){return "§" + colourCode + alias;}

    public static String getTeamName(String alias) {return getTeam(alias).getName();}
    public static String getFileName() {return fileName;}



    //Disk tools
    private void saveDeleteTeam(boolean isSave){
        File file = getTeamFile();
        ObjectMapper om = getMapper();
        TeamList teamList;
        try{
            try{
                teamList = om.readValue(file, TeamList.class);
            }catch(FileNotFoundException e){
                teamList = new TeamList();
            }

            if(isSave) teamList.addTeam(this);
            else teamList.removeTeam(this);

            om.writeValue(file, teamList);
        }catch(Exception e){e.printStackTrace();}
    }
    public void saveTeam(){saveDeleteTeam(true);}
    public void removeTeam(){saveDeleteTeam(false);}


    public static Team getTeam(String alias){
        List<Team> teams = getTeams();
        Main.testLog("Teams: " + String.join(", ", teams.stream().map(team -> team.getAlias()).collect(Collectors.toList()))); Main.testLog(alias);
        List<Team> matches = teams.stream().filter(team -> team.getAlias().equalsIgnoreCase(alias)).collect(Collectors.toList());
        Main.testLog(matches.toString());
        if(matches.size() != 1) return null;

        return matches.get(0);
    }





    //Utils
    public static List<Team> getTeams(){
        File file = getTeamFile();
        ObjectMapper om = getMapper();
        TeamList teamList;
        try{teamList = om.readValue(file, TeamList.class);}
        catch(Exception e){e.printStackTrace(); return null;}

        return teamList.getTeams();
    }

    public static File getTeamFile() {
        File file = new File("./plugins/MineHunt/" + fileName + ".yml");
        if(file==null) {
            try{file.createNewFile();}
            catch(IOException e){Main.logDiskError(e);}
        }
        return file;
    }
    public static ObjectMapper getMapper(){
        return new ObjectMapper(new YAMLFactory()).registerModule(new ParameterNamesModule());
    }



}
