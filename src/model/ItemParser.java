package model;


import model.basetype.Flask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 * Created by Kang on 7/7/2017.
 */
public class ItemParser {
    private Path currentRelativePath;
    private ArrayList<String[]> itemBlocks;
    private String path;
    private File[] dataFiles;
    private ArrayList<String> data = new ArrayList<>();
    private String[] allData;
    private HashMap<String, String> hashMap = new HashMap<>();
    /**
     * item parser constructor with empty item
     */
    public ItemParser() {
        // get current path
        currentRelativePath = Paths.get("");
        path = currentRelativePath.toAbsolutePath().toString();
        dataFiles = new File(path + "/src/model/basetype/data").listFiles();
        for (File file : dataFiles) {
            if (file.isFile()) {
                data.add(file.getName());
            }
        }
        allData = new String[data.size()];
        populateDatabase();
    }

    /**
     * initialize database with basetypes
     */
    private void populateDatabase() {
        int i = 0;
        // iterate through basetype files
        for (String str : data) {
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
     * @param string string to be parsed by itemparser
     * @return item to be returned
     */
    public Item pushString(String string) {
        if (string == null) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Received null string in Item Parser.");
            return null;
        } else {
            // split item into blocks
            String[] blocks = string.split("--------");
            // initiate item as arraylist of block arrs
            itemBlocks = new ArrayList<>();
            // iterate through blocks
            for (String block : blocks) {
                // split blocks by line
                String[] blockLines = block.split("\n");
                // add line to itemLines
                itemBlocks.add(blockLines);
            }
            return parseItem();
        }
    }

    /**
     * parses item and populates into item
     * @return Item now parsed item
     */
    private Item parseItem() {
        // iterate through item's blocks
        itemBlocks.forEach(this::parseBlock);
        Item item = createBaseItem(hashMap.get("ItemName"));
        return item;
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
            case "Rarity":
                parseRarityBlock(block);
            case "Requirements:":
                parseRequirementsBlock(block);
            case "Item Level:":
                hashMap.put("ItemLevel", lineSplit[1]);
            default:
                break;
        }
    }

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
     * parses rarity block
     * @param block block containing item rarity
     */
    private void parseRarityBlock(String[] block) {
        String firstLine = block[0];
        String itemName;
        String[] firstLineSplit = firstLine.split(" ");
        String rarity = firstLineSplit[1];
        hashMap.put("Rarity", rarity);
        switch (firstLineSplit[1]) {
            case "Normal":
            case "Currency":
            case "Gem":
                itemName = block[1];
            case "Magic":
                itemName = parseMagicName(block[1]);
            case "Rare":
            case "Unique":
                itemName = block[2];
            default:
                itemName = null;
                break;
        }
        hashMap.put("ItemName", itemName);
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
                    String[] arr = data.get(i).split("\\.");
                    Class<?> clazz = Class.forName("model.basetype." + arr[0]);
                    Constructor<?> ctor = clazz.getConstructor(ArrayList.class);
                    return (Item) ctor.newInstance(new Object[] {hashMap});
                } catch (Exception e) {
                    Logger logger = Logger.getLogger(getClass().getName());
                    logger.log(Level.SEVERE, "Error creating Java Object from string.", e);
                }
                break;
            }
            i++;
        }
        return null;
    }
}
