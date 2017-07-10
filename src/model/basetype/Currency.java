package model.basetype;

import model.Item;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kang on 7/7/2017.
 */
public class Currency extends Item {
    private String quantity;

    public Currency() {
        this(null);
    }

    public Currency(HashMap<String, String> itemMap) {
        super();
        populateItem(itemMap);
    }

    private void populateItem(HashMap<String, String> itemMap) {
        quantity = itemMap.get("Quantity");
    }

    public String toString() {
        String itemStr = "Quantity: " + quantity;
        return itemStr;
    }
}
