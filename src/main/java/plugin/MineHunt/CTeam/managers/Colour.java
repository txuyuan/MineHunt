package plugin.MineHunt.CTeam.managers;

import java.util.HashMap;
import java.util.Map;

public class Colour {

    public static Map<String, Character> getColourMap(){
        Map<String, Character> colourMap = new HashMap<String, Character>(){{
            put("black", '0');
            put("dark_blue", '1');
            put("dark_green", '2');
            put("dark_aqua", '3');
            put("dark_red", '4');
            put("dark_purple", '5');
            put("gold", '6');
            put("gray", '7');
            put("dark_gray", '8');
            put("blue", '9');
            put("green", 'a');
            put("aqua", 'b');
            put("red", 'c');
            put("light_purple", 'd');
            put("yellow", 'e');
            put("white", 'f');
        }};
        return colourMap;
    }

    public static Character getColourCode(String colourName){
        Map<String, Character> colourMap = getColourMap();
        if(!colourMap.keySet().contains(colourName)) return null;
        return colourMap.get(colourName);
    }

    public static String getColourName(char colourCode){
        Map<String, Character> colourMap = getColourMap();
        String returnKey = null;
        for(String key: colourMap.keySet())
            if(colourMap.get(key) == colourCode) returnKey = key;
        return returnKey;
    }

}
