package model;


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
    private String path;
    private File[] dataFiles;
    private ArrayList<String> baseTypes = new ArrayList<>();
    private String[] baseTypeDatabase;
    private HashMap<String, String> hashMap;
    /**
     * item parser constructor with empty item
     */
    public ItemParser() {
        // get current path
        Path currentRelativePath = Paths.get("");
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
        baseTypeDatabase = new String[baseTypes.size()];
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
                baseTypeDatabase[i] = sb.toString();
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
        ArrayList<String[]> itemBlocks = parseStringToBlocks(itemString);
        hashMap = new HashMap<>();
        // iterate through item's blocks
        for (String[] block : itemBlocks) {
            parseBlock(block);
        }
        String baseType = hashMap.get("BaseType");
        String rarity = hashMap.get("Rarity");
        if (rarity.equals("Currency") || rarity.equals("Gem")) {
            return createBaseItem(rarity);
        } else {
            return createBaseItem(baseType);
        }
    }

    public ArrayList<String[]> parseStringToBlocks(String itemString) {
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
        return itemBlocks;
    }

    /**
     * parses a block of item data
     * @param block item data block separated by "--------" lines
     */
    public void parseBlock(String[] block) {
        // get first line of block
        String tempStr = block[0];
        String[] lineSplit = tempStr.split(" ");
        // choose first word for tag to parse
        switch (lineSplit[0]) {
            case "Rarity:":
                String[] rarityData = parseRarityBlock(block);
                hashMap.put("Rarity", rarityData[0]);
                if (rarityData[0].equals("Currency") || rarityData[0].equals("Gem")) {
                    hashMap.put("itemName", rarityData[1]);
                } else {
                    hashMap.put("BaseType", rarityData[1]);
                }
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
     * @return rarity and item name from first block
     */
    public String[] parseRarityBlock(String[] block) {
        String[] results = new String[2];
        String firstLine = block[0];
        String[] firstLineSplit = firstLine.split(" ");
        results[0] = firstLineSplit[1];
        // find item name
        switch (firstLineSplit[1]) {
            case "Normal":
            case "Currency":
            case "Gem":
                results[1] = block[1];
                break;
            case "Magic":
                results[1] = parseMagicName(block[1]);
                break;
            case "Rare":
            case "Unique":
                results[1] = block[2];
                break;
            default:
                break;
        }
        // add item name to hash map
        return results;
    }

    /**
     * parses quantity block for currency items
     * @param block quantity block string array to be parsed
     */
    public void parseQuantityBlock(String[] block) {
        String[] lineSplit = block[0].split(" ");
        hashMap.put("Quantity",lineSplit[lineSplit.length - 1]);
    }

    /**
     * parses requirements block and adds data to item hash map
     * @param block requirements block to be parsed
     */
    public void parseRequirementsBlock(String[] block) {
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
    public String parseMagicName(String itemName) {
        // return null if empty item name
        if (itemName == null || itemName.equals("")) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error parsing magic item name.");
            return null;
        } else {
            // iterate through base types
            for (String type : baseTypeDatabase) {
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
    public Item createBaseItem(String itemName) {
        int i = 0;
        // iterate through possible basetype strings
        for (String str : baseTypeDatabase) {
            if (str.contains(itemName)) {
                try {
                    String[] arr = baseTypes.get(i).split("\\.");
                    String className = "model.basetype." + arr[0];
                    Class myClass = Class.forName(className);
                    Class[] types = {HashMap.class};
                    Constructor constructor = myClass.getConstructor(types);
                    Object[] parameters = {hashMap};
                    Item item = (Item)constructor.newInstance(parameters);
                    return item;
                } catch (Exception e) {
                    Logger logger = Logger.getLogger(getClass().getName());
                    logger.log(Level.SEVERE, "Error creating new Java Object from string.", e);
                }
                break;
            }
            i++;
        }
        // should never return null Item
        Logger logger = Logger.getLogger(getClass().getName());
        logger.log(Level.SEVERE, "Cannot find base item in base item database.");
        return null;
    }

    /**
     * gets all base type data as string array
     * @return string array of all base types
     */
    public String[] getBaseTypeDatabase() {
        return baseTypeDatabase;
    }
}
