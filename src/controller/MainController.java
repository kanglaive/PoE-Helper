package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import model.Item;

import java.util.prefs.*;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Main Controller class
 * Created by Kang on 7/4/2017.
 */
public class MainController {
    private boolean itemCheckEnabled = false;
    /**
     * initialize() method called automatically when instantiated
     */
    public void initialize() {


    }

    /**
     * handles close button in menu
     * @param event on close action
     */
    @FXML protected void onClose(ActionEvent event) {
        Platform.exit();
    }

    @FXML protected void toggleItemChecker(ActionEvent event) {
        itemCheckEnabled = !itemCheckEnabled;
    }

    @FXML protected void toggleFilterUpdate(ActionEvent event) {

    }

    @FXML protected void toggleItemPricer(ActionEvent event) {

    }

    @FXML protected void linkGithub(ActionEvent event) {

    }

    @FXML protected void itemCheck(ActionEvent event) throws Exception {
        String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        Item newItem = new Item(data);
    }
}
