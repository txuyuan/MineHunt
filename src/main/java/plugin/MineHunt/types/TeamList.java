package plugin.MineHunt.types;

import java.util.ArrayList;
import java.util.List;

public class TeamList {

    private List<Team> teams;

    public TeamList(List<Team> teams){
        this.teams = (ArrayList<Team>) teams;
    }

    public TeamList(){this.teams = new ArrayList<>();}

    public List<Team> getTeams(){return teams;}

    public void addTeam(Team team){teams.add(team);}
    public void removeTeam(Team team){teams.remove(team);}

}
