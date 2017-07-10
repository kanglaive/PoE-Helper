package model;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

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
    public void parseCurrencyTest() {
        itemParser = new ItemParser();
        String[] currency = {"Rarity: Currency\nScroll of Wisdom"};
        String[] rarityResults = itemParser.parseRarityBlock(currency);
        assertEquals("parseRarityBlock does not return correct rarity.", "Currency", rarityResults[0]);
        assertEquals("parseRarityBlock does not return correct item name.", "Scroll of Wisdom", rarityResults[1]);
    }
}
