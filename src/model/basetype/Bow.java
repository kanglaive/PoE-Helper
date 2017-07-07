package model.basetype;

import model.Item;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kang on 7/7/2017.
 */
public class Bow extends Item {

    public Bow() {
        this(null);
    }

    public Bow(ArrayList<String> strArr) {
        if (strArr == null) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Received null string in Item Parser.");
        } else {

        }
    }
}
