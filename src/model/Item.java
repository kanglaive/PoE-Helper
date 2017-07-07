package model;


import model.basetype.flasks.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kang on 7/4/2017.
 */
public class Item {
    private String itemRarity, itemName, baseType, implicit;
    private String[] strArr, prefix, suffix;
    private Rarity rarity;
    private int lvlReq, ilvl;
    private boolean valid = true;

    /** default constructor
     */
    public Item() {
        this(null);
    }

    public Item(String rarity) {
        this.itemRarity = rarity;
    }

    public Item(String itemName, String rarity, String baseType) {
        this.itemName = itemName;
        this.itemRarity = rarity;
        this.baseType = baseType;
    }

    /**
     * populate item as normal item
     */
    private void createNormal() {
        String[] line2 = strArr[1].split(" ");
        if (line2[0].equals("Superior")) {

        }
        itemName = strArr[1];

        // get last word in name
        this.baseType = line2[line2.length - 1];
        switch(baseType) {
            case "Amulet":
            case "Ring":
            case "Belt":
                String[] line5 = strArr[4].split(" ");
                lvlReq = Integer.parseInt(line5[1]);
                String[] line7 = strArr[6].split(" ");
                ilvl = Integer.parseInt(line7[2]);
                implicit = strArr[7];
                break;
            case "Sash":
                String[] line4 = strArr[3].split(" ");
                ilvl = Integer.parseInt(line4[2]);
                implicit = strArr[5];
                break;
            case "Flask":

                break;
            default:
                break;
        }
    }

    private void createMagic() {
        itemName = strArr[1];
        String[] line2 = strArr[1].split(" ");
    }

    /**
     * returns rarity as string
     * @return rarity parsed as string
     */
    public String getRarity() {
        return itemRarity;
    }

    /**
     * returns name of item
     * @return item name string
     */
    public String getItemName() {
        return itemName;
    }

    public void setRarity(String itemRarity) {
        this.itemRarity = itemRarity;
    }

    /**
     * returns whether this item is valid
     * @return
     */
    public boolean isValid() {
        return valid;
    }

}
