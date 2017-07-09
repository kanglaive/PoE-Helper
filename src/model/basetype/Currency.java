package model.basetype;

import model.Item;

import java.util.ArrayList;

/**
 * Created by Kang on 7/7/2017.
 */
public class Currency extends Item {
    private String quantity;

    public Currency() {
        this(null);
    }

    public Currency(ArrayList<String> strArr) {
        super();
        populateItem(strArr);
    }

    public void populateItem(String[] strArr) {
        String[] line4 = strArr[3].split(" ");
        String[] quant = line4[2].split("/");
        quantity = line4[2];
    }

    public void populateItem(ArrayList<String> strArr) {
        String[] arr = strArr.get(2).split(" ");
        quantity = arr[2];
    }

    public String toString() {
        String itemStr = "Quantity: " + quantity;
        return itemStr;
    }
}
