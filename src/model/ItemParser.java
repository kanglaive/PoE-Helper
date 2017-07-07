package model;

import model.basetype.currency.Currency;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 * Created by Kang on 7/7/2017.
 */
public class ItemParser {
    private ArrayList<String> strArr;
    private Item item;


    public ItemParser() {
        this(null);
    }

    public ItemParser(String string) {
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
                    default:
                        break;
                }

            }
        }
    }

    public Item getItem() {
        return item;
    }
}
