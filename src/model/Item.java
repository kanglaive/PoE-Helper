package model;

/**
 * Created by Kang on 7/4/2017.
 */
public class Item {
    private Rarity rarity;

    /**
     * instantiates item
     * @param string
     */
    public Item(String string) {
        String[] str_arr = string.split("/n");
        try {
            rarity.parseRarity(str_arr[0]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
