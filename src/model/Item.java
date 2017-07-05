package model;

/**
 * Created by Kang on 7/4/2017.
 */
public class Item {
    private Rarity itemRarity;
    private String itemName;

    /**
     * instantiates item
     * @param string
     */
    public Item(String string) {
        // split item string by lines
        String[] str_arr = string.split("/n");
        // try to parse lines
        try {
            // parse rarity on first line
            itemRarity.parseRarity(str_arr[0]);
            // parse name on second line
            itemName = str_arr[1];

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
