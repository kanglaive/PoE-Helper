package model.basetype;

import model.Item;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kang on 7/7/2017.
 */
public class Flask extends Item {
    public Flask() {
        this(null);
    }

    public Flask(ArrayList<String> strArr) {
        if (strArr == null) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error creating Life Flask from null string array.");
        } else {
            String[] arr = strArr.get(0).split(" ");
            super.setRarity(arr[1]);
            super.setItemName(strArr.get(1));
            super.setBaseType("Flask");
        }
    }

    public String toString() {
        return super.getItemName();
    }
}
