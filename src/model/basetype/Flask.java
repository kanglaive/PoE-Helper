package model.basetype;

import model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kang on 7/7/2017.
 */
public class Flask extends Item {
    private ArrayList<String> strArr;
    private HashMap<String, String> itemMap;

    /**
     * Flask constructor
     */
    public Flask() {
        this(null);
    }

    /**
     * initiates Flask object from string array
     * @param itemMap hash map of item properties
     */
    public Flask(HashMap<String, String> itemMap) {
        if (itemMap == null) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error creating Flask from null hash map.");
        } else {
            this.itemMap = itemMap;
            super.setRarity(itemMap.get("Rarity"));
            super.setItemName(itemMap.get("ItemName"));
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
