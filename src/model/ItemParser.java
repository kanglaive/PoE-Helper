package model;


import model.basetype.Flask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private ArrayList<String> data = new ArrayList<String>();
    private String[] allData;
    private Item item;
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
        // initial vars
        item = new Item(itemBlocks);
        // iterate through item's blocks
        itemBlocks.forEach(this::parseBlock);
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
                String[] parsedRarityBlock = parseRarityBlock(block);
                item = createBaseItem(parsedRarityBlock);
                try {
                    item.setRarity(lineSplit[1]);
                } catch (NullPointerException e) {
                    Logger logger = Logger.getLogger(getClass().getName());
                    logger.log(Level.SEVERE, "ItemParser cannot find item rarity despite finding rarity tag.", e);
                }
            case "Requirements:":

            case "Item Level:":
                try {
                    item.setItemLevel(Integer.parseInt(lineSplit[1]));
                } catch (NullPointerException e) {
                    Logger logger = Logger.getLogger(getClass().getName());
                    logger.log(Level.SEVERE, "ItemParser cannot find item lvl despite finding ilvl tag.", e);
                }
            default:
                break;
        }
    }

    /**
     * parses rarity block
     * @param block block containing item rarity
     * @return item rarity
     */
    private String[] parseRarityBlock(String[] block) {
        String firstLine = block[0];
        String[] firstLineSplit = firstLine.split(" ");
        String[] parsedBlock = new String[2];
        parsedBlock[0] = firstLineSplit[1];
        switch (firstLineSplit[1]) {
            case "Normal":
            case "Magic":
            case "Currency":
            case "Gem":
                parsedBlock[1] = block[1];
            case "Rare":
            case "Unique":
                parsedBlock[1] = block[2];
            default:
                break;
        }
        return parsedBlock;
    }

    /**
     * creates class from string, populating it with global item blocks
     * @param parsedRarityBlock [rarity, itemName] rarity block result
     */
    private Item createBaseItem(String[] parsedRarityBlock) {
        int i = 0;
        // iterate through possible basetype strings
        for (String str : allData) {
            if (str.contains(parsedRarityBlock[1])) {
                try {
                    String[] arr = data.get(i).split("\\.");
                    Class<?> clazz = Class.forName("model.basetype." + arr[0]);
                    Constructor<?> ctor = clazz.getConstructor(ArrayList.class);
                    return (Item) ctor.newInstance(new Object[] {itemBlocks});
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
