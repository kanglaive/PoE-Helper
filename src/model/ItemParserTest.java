package model;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kang on 7/9/2017.
 */
public class ItemParserTest {
    private ItemParser itemParser;
    @Test
    public void initializeTest() {
        // testing creating new item parser
        itemParser = new ItemParser();
        assertNotNull("ItemParser still null after instantiation.", itemParser);
        assertNotNull("BaseTypeDatabase still null after itemParser instantiation.", itemParser.getBaseTypeDatabase());
    }

    @Test
    public void itemReadTest() {
        String itemString;
        ArrayList<String[]> itemBlocks = new ArrayList<>();
        HashMap<String, String> itemMap = new HashMap<>();
    }

    @Test
    public void parseCurrencyTest() {
        itemParser = new ItemParser();
        String[] currency = {"Rarity: Currency","Scroll of Wisdom"};
        String[] rarityResults = itemParser.parseRarityBlock(currency);
        assertEquals("parseRarityBlock does not return correct rarity.", "Currency", rarityResults[0]);
        assertEquals("parseRarityBlock does not return correct item name.", "Scroll of Wisdom", rarityResults[1]);
    }

    @Test
    public void parseQuantityBlock() {
        itemParser = new ItemParser();
        String[] quantityBlock = {"Stack Size: 15/40"};
        String result = itemParser.parseQuantityBlock(quantityBlock);
        assertNotNull("Result of quantity block parsing of currency should not be null.", result);
        assertEquals("Result of quantity parsing is not correct for currency.", "15/40", result);
    }

    @Test
    public void parseStringToBlocks() {
        itemParser = new ItemParser();
        String sampleCurrency = "Rarity: Currency\n" +
                "Scroll of Wisdom\n" +
                "--------\n" +
                "Stack Size: 15/40\n" +
                "--------\n" +
                "Identifies an item\n" +
                "--------\n" +
                "Right click this item then left click an unidentified item to apply it.\n" +
                "Shift click to unstack.";
        ArrayList<String[]> itemBlocks = itemParser.parseStringToBlocks(sampleCurrency);
        assertNotNull("Item blocks unable to be parsed by method.", itemBlocks);
        String[] expectedRarity = {"Rarity: Currency","Scroll of Wisdom"};
        String[] actualRarity = itemBlocks.get(0);
        assertEquals("Rarity block not correctly parsed", expectedRarity[0], actualRarity[0]);
        assertEquals("Rarity block not correctly parsed", expectedRarity[1], actualRarity[1]);
        String[] expectedQuantity = {"Stack Size: 15/40"};
        String[] actualQuantity = itemBlocks.get(1);
        assertEquals("Quantity block not correctly parsed", expectedQuantity[0], actualQuantity[0]);
    }

    @Test
    public void testSampleItems() {
        itemParser = new ItemParser();
        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString();
        // get directory or basetype txt files
        File[] dataFiles = new File(path + "/out/test/samples/").listFiles();
        ArrayList<String> sampleItems = new ArrayList<>();
        // iterate through txt file and add base type names to arr
        for (File file : dataFiles) {
            if (file.isFile()) {
                sampleItems.add(file.getName());
            }
        }
    }
}
