package plugin.MineHunt.CBoard.types;

public class ScoreboardData {

    private static ScoreboardType mode = ScoreboardType.INFO;

    public static ScoreboardType getType(){ return mode; }

    public static void setType(ScoreboardType type){ mode = type; }
    public static void toggleMode(){
        if(mode == ScoreboardType.INFO) mode = ScoreboardType.TEAMS;
        else if(mode == ScoreboardType.TEAMS) mode = ScoreboardType.INFO;

    }


    public enum ScoreboardType {
        INFO, TEAMS
    }

}
