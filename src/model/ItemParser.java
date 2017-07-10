package model;


import model.basetype.Flask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 * Created by Kang on 7/7/2017.
 */
public class ItemParser {
    private Path currentRelativePath;
    private String path;
    private File[] dataFiles;
    private ArrayList<String> baseTypes = new ArrayList<>();
    private String[] allData;
    private HashMap<String, String> hashMap;
    /**
     * item parser constructor with empty item
     */
    public ItemParser() {
        // get current path
        currentRelativePath = Paths.get("");
        path = currentRelativePath.toAbsolutePath().toString();
        // get directory or basetype txt files
        dataFiles = new File(path + "/src/model/basetype/data").listFiles();
        // iterate through txt file and add base type names to arr
        for (File file : dataFiles) {
            if (file.isFile()) {
                baseTypes.add(file.getName());
            }
        }
        // create data struct holding all base types
        allData = new String[baseTypes.size()];
        populateDatabase();
    }

    /**
     * initialize database with basetypes
     */
    private void populateDatabase() {
        int i = 0;
        // iterate through basetype files
        for (String str : baseTypes) {
            // read each file to string
            try(BufferedReader br = new BufferedReader(new FileReader(path + "/src/model/basetype/data/"
                    + str))) {
                // build string
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                // iterate through file and add line to stringbuilder
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                // save string to alldata string array
                allData[i] = sb.toString();
            } catch (Exception e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Error populating base type database for item parsing.", e);
            }
            i++;
        }
    }

    /**
     * parse item based on item string
     * @param itemString string to be parsed by itemparser
     * @return initially parsed item
     */
    public Item parseItemString(String itemString) {
        if (itemString == null) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Received null string in Item Parser.");
            return null;
        } else {
            // split item into blocks
            String[] blockStrings = itemString.split("--------");
            // initiate item as arraylist of block arrs
            ArrayList<String[]> itemBlocks = new ArrayList<>();
            // iterate through blocks
            for (String block : blockStrings) {
                // split blocks by line
                String[] blockLines = block.split("\n");
                // add line to itemLines
                itemBlocks.add(blockLines);
            }
            return parseItemBlocks(itemBlocks);
        }
    }

    /**
     * parses item and populates into item
     * @return Item now parsed item
     */
    private Item parseItemBlocks(ArrayList<String[]> itemBlocks) {
        hashMap = new HashMap<>();
        // iterate through item's blocks
        for (String[] block : itemBlocks) {
            parseBlock(block);
        }
        String itemName = hashMap.get("ItemName");
        return createBaseItem(itemName);
    }

    /**
     * parses a block of item data
     * @param block item data block separated by "--------" lines
     */
    private void parseBlock(String[] block) {
        // get first line of block
        String tempStr = block[0];
        String[] lineSplit = tempStr.split(" ");
        // choose first word for tag to parse
        switch (lineSplit[0]) {
            case "Rarity:":
                parseRarityBlock(block);
            case "Requirements:":
                parseRequirementsBlock(block);
            case "Stack":
                parseQuantityBlock(block);
            case "Item Level:":
                hashMap.put("ItemLevel", lineSplit[1]);
            default:
                break;
        }
    }

    /**
     * parses rarity block
     * @param block block containing item rarity
     */
    private void parseRarityBlock(String[] block) {
        String firstLine = block[0];
        String itemName = null;
        String[] firstLineSplit = firstLine.split(" ");
        String rarity = firstLineSplit[1];
        hashMap.put("Rarity", rarity);
        // find item name
        switch (firstLineSplit[1]) {
            case "Normal":
            case "Currency":
            case "Gem":
                itemName = block[1];
                break;
            case "Magic":
                itemName = parseMagicName(block[1]);
                break;
            case "Rare":
            case "Unique":
                itemName = block[2];
                break;
            default:
                break;
        }
        // add item name to hash map
        hashMap.put("ItemName", itemName);
    }

    /**
     * parses quantity block for currency items
     * @param block quantity block string array to be parsed
     */
    private void parseQuantityBlock(String[] block) {
        String[] lineSplit = block[0].split(" ");
        hashMap.put("Quantity",lineSplit[lineSplit.length - 1]);
    }

    /**
     * parses requirements block and adds data to item hash map
     * @param block requirements block to be parsed
     */
    private void parseRequirementsBlock(String[] block) {
        for (String blockLine : block) {
            String[] parsedLine = blockLine.split(" ");
            switch(parsedLine[0]) {
                case "Level:":
                    hashMap.put("LevelReq",parsedLine[1]);
                    break;
                case "Str":
                    hashMap.put("StrReq",parsedLine[1]);
                    break;
                case "Int":
                    hashMap.put("IntReq",parsedLine[1]);
                    break;
                case "Dex:":
                    hashMap.put("DexReq",parsedLine[1]);
                    break;
                default:
                    break;
            }
        }
        /*
        Alternative requirements parsing
        for (String blockLine : block) {
            String[] parsedLine = blockLine.split(" ");
            hashMap.put(parsedLine[0], parsedLine[1]);
        */
    }

    /**
     * find base type name in magic item name
     * @param itemName entire magic item name
     * @return base type of magic item
     */
    private String parseMagicName(String itemName) {
        // return null if empty item name
        if (itemName == null || itemName.equals("")) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error parsing magic item name.");
            return null;
        } else {
            // iterate through base types
            for (String type : allData) {
                String[] splitStr = type.split("\n");
                for (String base : splitStr) {
                    // if magic item name contains base type
                    if (itemName.contains(base)) {
                        return base;
                    }
                }
            }
            // return null if unable to be found
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Cannot find base in magic item name");
            return null;
        }
    }

    /**
     * creates class from string, populating it with global item blocks
     * @param itemName itemName to search for
     */
    private Item createBaseItem(String itemName) {
        int i = 0;
        // iterate through possible basetype strings
        for (String str : allData) {
            if (str.contains(itemName)) {
                try {
                    String[] arr = baseTypes.get(i).split("\\.");
                    Class<?> clazz = Class.forName("model.basetype." + arr[0]);
                    Constructor<?> ctor = clazz.getConstructor(HashMap.class);
                    return (Item) ctor.newInstance(new Object[] {hashMap});
                } catch (Exception e) {
                    Logger logger = Logger.getLogger(getClass().getName());
                    logger.log(Level.SEVERE, "Error creating new Java Object from string.", e);
                }
                break;
            }
            i++;
        }
        // should never return null Item
        return null;
    }
}
