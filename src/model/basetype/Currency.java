package model.basetype;

import model.Item;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kang on 7/7/2017.
 */
public class Currency extends Item {
    private String quantity;
    private HashMap<String, String> itemMap;

    public Currency() {
        this(null);
    }

    public Currency(HashMap<String, String> itemMap) {
        super(itemMap);
        this.itemMap = itemMap;
        populateCurrency(itemMap);
    }

    private void populateCurrency(HashMap<String, String> itemMap) {
        quantity = itemMap.get("Quantity");
    }

    public String toString() {
        String itemStr = "Quantity: " + itemMap.get("Quantity");
        return itemStr;
    }
}
