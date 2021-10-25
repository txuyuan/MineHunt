package plugin.MineHunt.types;

import plugin.MineHunt.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeamList {

    private List<Team> teams;

    public TeamList(List<Team> teams){
        this.teams = teams;
    }
    public TeamList(){}


    public List<Team> getTeams(){
        if(teams==null) return new ArrayList<>();
        return teams;
    }

    public void setTeams(List<Team> teams){
        this.teams = teams;
    }



    public boolean addTeam(Team team){
        try{
            teams.add(team);
        }catch(Exception e){e.printStackTrace(); return false;}
        return true;
    }

    public boolean removeTeam(Team team){
        List<String> teamNames = teams.stream().map(teamTemp -> teamTemp.getAlias()).collect(Collectors.toList());
        if(!teamNames.contains(team.getAlias())) return false;
        teams.remove(team); Main.testLog(team.getName() + " removed");
        return true;
    }



}
