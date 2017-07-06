package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.dispatcher.SwingDispatchService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Remove extra logging features besides warnings
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);

        // Don't forget to disable the parent handlers.
        logger.setUseParentHandlers(false);
        // Wrapping access to javafx components using SwingDispatchService.
        GlobalScreen.setEventDispatcher(new SwingDispatchService());

        // Instantiate state using main FXML as root
        Parent root = FXMLLoader.load(getClass().getResource("../view/main.fxml"));
        primaryStage.setTitle("PoE Helper");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.show();

        // register native hook
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * overrides application stop function to unhook native key hook before stopping
     */
    @Override
    public void stop() {
        // unregister native hook before closing
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
