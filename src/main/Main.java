package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class Main extends Application implements NativeKeyListener {

    @Override
    public void start(Stage primaryStage) throws Exception {
        GlobalScreen.setEventDispatcher(new SwingDispatchService());

        GlobalScreen.addNativeKeyListener(this);
        Parent root = FXMLLoader.load(getClass().getResource("../view/main.fxml"));
        primaryStage.setTitle("PoE Helper");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.show();

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * overrides jnativehook functions
     * @param event key is pressed
     */
    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
    }

    /**
     * overrides jnativehook type function
     * @param event key has been typed
     */
    @Override
    public void nativeKeyTyped(NativeKeyEvent event) {
    }

    /**
     * overrides jnativehook key release function
     * @param event key has depressed
     */
    @Override
    public void nativeKeyReleased(NativeKeyEvent event) {
        if (event.getKeyCode() == NativeKeyEvent.VC_CONTROL) {

        }
        if (event.getKeyCode() == NativeKeyEvent.VC_C) {

        }
    }

    /**
     * overrides application stop function to unhook native key hook before stopping
     */
    @Override
    public void stop() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
