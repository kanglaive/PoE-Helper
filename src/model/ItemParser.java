package model;

import model.basetype.Currency;
import model.basetype.Gem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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

    private ArrayList<String> strArr;
    private Item item;


    public ItemParser() {
        currentRelativePath = Paths.get("");
        path = currentRelativePath.toAbsolutePath().toString();
        dataFiles = new File(path).listFiles(new FilenameFilter() {
            @Override public boolean accept(File dir, String name) { return name.endsWith(".java");
            } });
        for (File file : dataFiles) {
            if (file.isFile()) {
                data.add(file.getName());
            }
        }
        allData = new String[data.size()];
        populateDatabase();
    }

    private void populateDatabase() {
        int i = 0;
        for (String str : data) {
            try(BufferedReader br = new BufferedReader(new FileReader(path + "/src/model/basetype/data/"
                    + str + ".txt"))) {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                allData[i] = sb.toString();
            } catch (Exception e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Error populating base type database for item parsing.", e);
            }
            i++;
        }
    }

    public Item parseItem(String string) {
        if (string == null) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Received null string in Item Parser.");
        } else {
            String[] arr = string.split("\n");
            strArr = new ArrayList<String>(Arrays.asList(arr));
            strArr.remove("--------");
            arr = strArr.get(0).split(" ");
            // checks for "rarity:" substring to validate
            if (arr[0].equals("Rarity:")) {
                item = new Item(arr[1]);
                switch (arr[1]) {
                    case "Currency":
                        item = new Currency(strArr);
                        break;
                    case "Normal":
                    case "Magic":
                        parseBaseType(strArr.get(1));
                        break;
                    case "Rare":
                    case "Unique":
                        parseBaseType(strArr.get(2));
                        break;
                    case "Gem":
                        item = new Gem(strArr);
                    default:
                        break;
                }
            }
        }
        return item;
    }

    /**
     * creates object from string and calls respective constructor class
     * @param string
     */
    private void parseBaseType(String string) {
        int i = 0;
        for (String str : allData) {
            if (str.contains(string)) {
                try {
                    Class<?> clazz = Class.forName("model.basetype." + data.get(i));
                    Constructor<?> ctor = clazz.getConstructor(ArrayList.class);
                    item = (Item) ctor.newInstance(new Object[] { strArr });
                } catch (Exception e) {
                    Logger logger = Logger.getLogger(getClass().getName());
                    logger.log(Level.SEVERE, "Error creating Java Object from string.", e);
                }
                break;
            }
            i++;
        }
    }
}
