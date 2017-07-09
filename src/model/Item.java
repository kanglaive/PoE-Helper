package model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kang on 7/4/2017.
 */
public class Item {
    private String itemRarity, itemName, baseType, implicit;
    private String[] prefix, suffix;
    private Rarity rarity;
    private int lvlReq, itemLevel = 0, itemReq;
    private boolean valid = true;
    private ArrayList<String[]> itemBlocks;
    private HashMap<String, String> itemHashMap;

    /** default constructor
     */
    public Item() {
        this(null);
    }

    public Item(ArrayList<String[]> itemBlocks) {
        this.itemBlocks = itemBlocks;
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

    /**
            * sets rarity to rarity parameter
     * @param itemRarity
     */
    public void setRarity(String itemRarity) {
        this.itemRarity = itemRarity;
    }

    /**
     * sets itemName to name parameter
     * @param itemName
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * sets baseType to base type parameter
     * @param baseType
     */
    public void setBaseType(String baseType) {
        this.baseType = baseType;
    }

    /**
     * sets item req to new item req
     * @param itemReq new item req
     */
    public void setItemReq(int itemReq) {
        this.itemReq = itemReq;
    }

    /**
     * sets item level
     * @param itemLevel new itemlevel
     */
    public void setItemLevel(int itemLevel) {
        this.itemLevel = itemLevel;
    }

    /**
     * returns item's hash map
     * @return hash map of item
     */
    public HashMap<String, String> getHashMap() {
        return itemHashMap;
    }

    /**
     * sets item hash map to new one
     * @param itemHashMap new item hash map
     */
    public void setHashMap(HashMap<String, String> itemHashMap) {
        this.itemHashMap = itemHashMap;
    }

    /**
     * returns whether this item is valid
     * @return
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * set this itemblocks to new item blocks
     * @param itemBlocks
     */
    public void setItemBlocks(ArrayList<String[]> itemBlocks) {
        this.itemBlocks = itemBlocks;
    }

    /**
     * returns itemblocks data from item
     * @return itemblocks item's itemblocks data struct
     */
    public ArrayList<String[]> getItemBlocks() {
        return itemBlocks;
    }

}
