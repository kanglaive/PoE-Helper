package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Main Controller class
 * Created by Kang on 7/4/2017.
 */
public class MainController {


    /**
     * initialize() method called automatically when instantiated
     */
    public void initialize() {

    }

    /**
     * handles close button in menu
     * @param event on close action
     */
    @FXML
    protected void onClose(ActionEvent event) {
        Platform.exit();
    }
}
