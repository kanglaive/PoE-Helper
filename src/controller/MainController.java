package controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Item;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.*;
import java.awt.datatransfer.DataFlavor;

/**
 * Main Controller class
 * Created by Kang on 7/4/2017.
 */
public class MainController implements NativeKeyListener, NativeMouseInputListener {
    @FXML private CheckBox itemCheckerCheckBox;
    @FXML private TextArea itemText;
    @FXML private TextField rarityField;
    @FXML private TextField itemNameField;
    @FXML private TextField baseTypeField;
    @FXML private TextField line4;
    @FXML private TextField line5;
    @FXML private TextField line6;
    private boolean itemPriceEnabled = false;
    private boolean itemCheckEnabled = false;
    private boolean key_ctrl = false;
    private boolean key_c = false;
    private boolean key_f = false;
    private Stage alertStage;
    
    /**
     * initialize() method called automatically when instantiated
     */
    public void initialize() {
        GlobalScreen.addNativeKeyListener(this);
        GlobalScreen.addNativeMouseListener(this);
        GlobalScreen.addNativeMouseMotionListener(this);
    }

    /**
     * handles close button in menu
     * @param event on close action
     */
    @FXML protected void onClose(ActionEvent event) {
        Stage stage = (Stage) itemCheckerCheckBox.getScene().getWindow();
        stage.close();
    }

    /**
     * toggles item check hotkey action
     * @param event
     */
    @FXML protected void toggleItemChecker(ActionEvent event) {itemCheckEnabled = !itemCheckEnabled;
    }

    /**
     * toggles constant filter updating action
     * @param event
     */
    @FXML protected void toggleFilterUpdate(ActionEvent event) {

    }

    /**
     * toggles item pricing hotkey action
     * @param event
     */
    @FXML protected void toggleItemPricer(ActionEvent event) {itemPriceEnabled = !itemPriceEnabled;
    }

    /**
     * directs to github project page
     * @param event
     */
    @FXML protected void linkGithub(ActionEvent event) {

    }

    /**
     * sends application to back of windows
     * @param event
     */
    @FXML protected void sendToBack(ActionEvent event) {
        Stage stage = (Stage) itemCheckerCheckBox.getScene().getWindow();
        stage.toBack();
    }

    /**
     * estimates/checks item price
     */
    private void itemPrice() {
        Item item = parseClipboard();
    }

    /**
     * checks item in clipboard
     * @throws Exception
     * @return Item item parsed from clipboard
     */
    private void itemCheck() {
        Item item = parseClipboard();
        createItemAlert(item);

    }

    /**
     * creates item alert window
     * @param item item to display in window
     */
    private void createItemAlert(Item item) {
        try {
            Point mousePos = MouseInfo.getPointerInfo().getLocation();
            FXMLLoader alertLoader = new FXMLLoader();
            alertLoader.setLocation(getClass().getResource("../view/alert.fxml"));
            Scene alertScene = new Scene(alertLoader.load(), 320, 240);
            Platform.runLater(() -> {
                alertStage = new Stage();
                alertStage.setScene(alertScene);
                alertStage.setX(mousePos.x);
                alertStage.setY(mousePos.y);
                alertStage.show();
            });
        } catch (java.io.IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new window due to missing fxml file.", e);
        }
    }

    /**
     * parses string into item object
     * @return item with parsed string data
     */
    private Item parseClipboard() {
        String data = null;
        try {
            data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Unable to parse string from clipboard.", e);
        }
        if (data == null) {
            return null;
        }
        Item parsedItem = new Item(data);
        if (parsedItem.isValid()) {
            itemText.setText(data);
            rarityField.setText(parsedItem.getRarity());
            baseTypeField.setText(parsedItem.getBaseType());
            itemNameField.setText(parsedItem.getItemName());
            switch (parsedItem.getRarity()) {
                case "Currency":
                    line4.setText(parsedItem.getQuantity());
                    break;
                default:
                    break;
            }
            return parsedItem;
        } else {
            return null;
        }
    }

    /**
     * overrides jnativehook functions
     * @param e key is pressed
     */
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
            key_ctrl = true;
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_C) {
            key_c = true;
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_F) {
            key_f = true;
        }
    }

    /**
     * overrides jnativehook type function
     * @param e key has been typed
     */
    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }

    /**
     * overrides jnativehook key release function
     * @param e event where key has depressed
     */
    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        // check for hotkey
        if (key_c && key_ctrl && itemCheckEnabled) {
            itemCheck();
        }
        if (key_f && key_ctrl && itemPriceEnabled) {
            itemPrice();
        }
        // reset key vars
        if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
            key_ctrl = false;
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_C) {
            key_c = false;
        }

        if (e.getKeyCode() == NativeKeyEvent.VC_F) {
            key_f = false;
        }
    }

    public void nativeMouseClicked(NativeMouseEvent e) {
    }

    public void nativeMousePressed(NativeMouseEvent e) {
    }

    public void nativeMouseReleased(NativeMouseEvent e) {
    }

    /**
     * removes item alert on mouse movement
     * @param e
     */
    public void nativeMouseMoved(NativeMouseEvent e) {
        if (alertStage != null) {
            Platform.runLater(() -> {
                alertStage.hide();
            });
        }
    }

    public void nativeMouseDragged(NativeMouseEvent e) {
    }
}
