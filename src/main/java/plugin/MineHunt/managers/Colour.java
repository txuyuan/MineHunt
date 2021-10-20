package plugin.MineHunt.managers;

import plugin.MineHunt.Main;

public class Colour {

    public static Character getColourCode(String colourName){
        switch(colourName){
            case "dark_aqua":
                return '3';
            case "dark_blue":
                return '1';
            case "dark_gray":
                return '8';
            case "dark_purple":
                return '5';
            case "dark_green":
                return '2';
            case "yellow":
                return 'e';
            case "red":
                return 'c';
            case "aqua":
                return 'b';
            case "blue":
                return '9';
            case "gold":
                return '6';
            case "gray":
                return '7';
            case "black":
                return '0';
            case "green":
                return 'a';
            case "white":
                return 'f';
            case "light_purple":
                return 'd';
            case "dark_red":
                return '4';
            default:
                Main.logInfo("Error deciphering colour: " + colourName);
                return null;
        }
    }

    public static String getColourName(char colourCode){
        switch(colourCode){
            case '0':
                return "black";
            case '1':
                return "dark_blue";
            case '2':
                return "dark_green";
            case '3':
                return "dark_aqua";
            case '4':
                return "dark_red";
            case '5':
                return "dark_purple";
            case '6':
                return "gold";
            case '7':
                return "gray";
            case '8':
                return "dark_gray";
            case '9':
                return "blue";
            case 'a':
                return "green";
            case 'b':
                return "aqua";
            case 'c':
                return "red";
            case 'd':
                return "light_purple";
            case 'e':
                return "yellow";
            case 'f':
                return "white";
            default:
                Main.logInfo("Error deciphering colour code: " + colourCode);
                return null;
        }
    }

}
