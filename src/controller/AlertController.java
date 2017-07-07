package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Item;

/**
 * Created by Kang on 7/6/2017.
 */
public class AlertController {
    @FXML private Label itemName;


    /**
     * populates alert with respective item properties
     * @param item
     */
    public void populateAlert(Item item) {
        if (item.isValid()) {
            itemName.setText(item.getItemName());
            switch (item.getRarity()) {
                case "Normal":
                    break;
                default:
                    break;
            }
        }
    }

}
