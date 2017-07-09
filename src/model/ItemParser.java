package model;


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
    private String path;
    private File[] dataFiles;
    private ArrayList<String> data = new ArrayList<String>();
    private String[] allData;
    /**
     * item parser constructor with empty item
     */
    public ItemParser() {
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
     * @return
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
            ArrayList<String[]> itemBlocks = new ArrayList<>();
            // iterate through blocks
            for (String block : blocks) {
                // split blocks by line
                String[] blockLines = block.split("\n");
                // add line to itemLines
                itemBlocks.add(blockLines);
            }
            return parseItem(new Item(itemBlocks));
        }
    }

    /**
     * parses item and populates into item
     * @param item item to parse data into
     * @return Item now parsed item
     */
    private Item parseItem(Item item) {
        // initial vars
        ArrayList<String[]> itemBlocks = item.getItemBlocks();
        String tempStr;
        String[] tempSplit;
        // iterate through item's blocks
        for (String[] block : itemBlocks) {
            // rarity test
            tempStr = block[0];
            tempSplit = tempStr.split(" ");
            if (tempSplit[0].equals("Rarity:")) {
                item.setRarity(tempSplit[1]);
            } else if (tempSplit[0].equals("Requirements:")) {
                tempSplit = block[1].split(" ");
                item.setItemReq(Integer.parseInt(tempSplit[1]));
            }
        }
        return item;
    }



    /**
     * creates object from string and calls respective constructor class
     * @param string object base name
     * @param itemBlocks item blocks struct
     */
    private Item createBaseItem(String string, ArrayList<String[]> itemBlocks) {
        int i = 0;
        // iterate through possible basetype strings
        for (String str : allData) {
            if (str.contains(string)) {
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
