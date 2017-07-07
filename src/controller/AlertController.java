package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import model.Item;

/**
 * Created by Kang on 7/6/2017.
 */
public class AlertController {
    @FXML private Label itemName;
    @FXML private Label info;


    /**
     * populates alert with respective item properties
     * @param item
     */
    public void populateAlert(Item item) {
        if (item.isValid()) {
            itemName.setText(item.getItemName());
            switch (item.getRarity()) {
                case "Normal":
                    itemName.setTextFill(Color.rgb(200, 200, 200));
                    break;
                case "Magic":
                    itemName.setTextFill(Color.rgb(136, 136, 255));
                    break;
                case "Rare":
                    itemName.setTextFill(Color.rgb(255, 255, 119));
                    break;
                case "Currency":
                    itemName.setTextFill(Color.rgb(170,158,130));
                    break;
                case "Unique":
                    itemName.setTextFill(Color.rgb(175,96,37));
                    break;
                case "Gem":
                    itemName.setTextFill(Color.rgb(27,162,155));
                    break;
                default:
                    break;
            }
            info.setText(item.toString());
        }
    }

}
