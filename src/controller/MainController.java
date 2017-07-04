package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import model.Item;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.prefs.*;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;

/**
 * Main Controller class
 * Created by Kang on 7/4/2017.
 */
public class MainController  implements NativeKeyListener {
    @FXML private CheckBox itemCheckerCheckBox;
    private boolean itemPriceEnabled = false;
    private boolean itemCheckEnabled = false;
    private boolean key_ctrl = false;
    private boolean key_c = false;
    private boolean key_f = false;
    /**
     * initialize() method called automatically when instantiated
     */
    public void initialize() {
        GlobalScreen.addNativeKeyListener(this);
    }

    /**
     * handles close button in menu
     * @param event on close action
     */
    @FXML protected void onClose(ActionEvent event) {
        Stage stage = (Stage) itemCheckerCheckBox.getScene().getWindow();
        stage.close();
    }

    @FXML protected void toggleItemChecker(ActionEvent event) {
        itemCheckEnabled = !itemCheckEnabled;
    }

    @FXML protected void toggleFilterUpdate(ActionEvent event) {

    }

    @FXML protected void toggleItemPricer(ActionEvent event) {
        itemPriceEnabled = !itemPriceEnabled;
    }

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
        if (item == null) {
            System.out.println("Error parsing item from clipboard.");
        }
    }

    /**
     * checks item in clipboard
     * @throws Exception
     * @return Item item parsed from clipboard
     */
    private void itemCheck() {
        Item item = parseClipboard();
        if (item == null) {
            System.out.println("Error parsing item from clipboard.");
        }
    }

    private Item parseClipboard() {
        try {
            String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            System.out.println(data);
            Item newItem = new Item(data);
            return newItem;
        } catch (Exception e) {
            System.out.println("Unable to parse string from clipboard.");
            e.printStackTrace();
        }
        return null;
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
     * @param e key has depressed
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

}
