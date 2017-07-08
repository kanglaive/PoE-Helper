package model.basetype;

import model.Item;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kang on 7/7/2017.
 */
public class Flask extends Item {
    private ArrayList<String> strArr;
    public Flask() {
        this(null);
    }

    /**
     * initiates Flask object from string array
     * @param strArr arraylist containing parsed item string
     */
    public Flask(ArrayList<String> strArr) {
        if (strArr == null) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error creating Flask from null string array.");
        } else {
            this.strArr = strArr;
            String[] arr = strArr.get(0).split(" ");
            super.setRarity(arr[1]);
            super.setItemName(strArr.get(1));
            super.setBaseType("Flask");
        }
    }

    @Override
    public String toString() {
        String rarity = super.getRarity();
        StringBuilder sb = new StringBuilder();
        switch (rarity) {
            case "Normal":
                parseNormal(sb);
                break;
            case "Magic":
                parseMagic(sb);
                break;
            default:
                break;
        }
        if (sb.toString() == null || sb.toString() == "") {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error creating Flask from null string array.");
            return "Unable to parse null iitem string.";
        } else {
            return sb.toString();
        }
    }

    private void parseNormal(StringBuilder sb) {
        sb.append(strArr.get(7));
        sb.append("\n");
        sb.append(strArr.get(5) + " " + strArr.get(6));
    }

    private void parseMagic(StringBuilder sb) {
        parseNormal(sb);
    }



}
