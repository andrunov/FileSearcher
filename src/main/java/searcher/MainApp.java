package searcher;

/**
 * Entry point for the JavaFX desktop application.
 * Loads the main window, manages application settings, and opens the settings dialog.
 */
import searcher.controller.MainController;
import searcher.controller.SettingsController;
import searcher.model.Settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

//TODO 8. Сделать рефакторинг переменных и методов

/**
 * Main JavaFX application class.
 */
public class MainApp extends Application {

    /*primary app stage*/
    private Stage primaryStage;

    /*root layout element*/
    private SplitPane rootLayout;

    /*link to main controller*/
    private MainController mainController;

    private Settings settings;

    /**
     * Launches the application.
     *
     * @param args command-line arguments passed to JavaFX
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the primary stage and loads the persisted settings.
     *
     * @param primaryStage the window used by the application
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("File main.java.searcher");
        this.primaryStage.getIcons().add(new Image(String.valueOf(MainApp.class.getResource("/searcher/images/glass.png"))));
        this.settings = new Settings();
        this.settings.loadFields();
        this.initRootLayout(new Locale("ru","RU"));
        this.primaryStage.setWidth(this.settings.getMainWindowWith());
        this.primaryStage.setHeight(this.settings.getMainWindowHeight());
        this.primaryStage.heightProperty().addListener(mainController.stageSizeListener);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        this.mainController.saveSettings();
        this.settings.setMainWindowHeight(this.getPrimaryStage().getHeight());
        this.settings.setMainWindowWith(this.getPrimaryStage().getWidth());
        this.settings.saveFields();
    }

    /**
     * Loads the main window layout from FXML and wires the controller to the application.
     *
     * @param locale the locale used for the UI resources
     */
    public void initRootLayout(Locale locale) {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("view/MainView.fxml"));
            loader.setResources(ResourceBundle.getBundle("searcher/bundles/Locale", locale));
            rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Give the mainController access to the main app.
            mainController = loader.getController();
            mainController.loadSettings(this.settings);
            mainController.setupResultTable();
            mainController.setupPagination();
            mainController.setMainApp(this);
            mainController.setLocale(this.settings.getLocale());

            this.updateSkin(this.settings.getSkin().getRepr());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the settings dialog for the application.
     *
     * @param resourceBundle localized resources used by the dialog
     */
    public void showSettingsEditDialog(ResourceBundle resourceBundle) {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resourceBundle);
            loader.setLocation(MainApp.class.getResource("view/SettingsView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create dialog window Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Settings");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // create and adjust controller
            SettingsController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setSettings(this.settings);
            controller.setResourceBundle(resourceBundle);
            controller.setFieldsValues();

            dialogStage.heightProperty().addListener(controller.stageSizeListener);
            dialogStage.setWidth(this.settings.getSettingsWindowWith());
            dialogStage.setHeight(this.settings.getSettingsWindowHeight());

            // open dialog stage and wait till user close it
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the primary application window.
     *
     * @return the primary stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void updateSkin(String skinValue) {
        String newStyle = String.format("searcher/style/%s.css", skinValue);
        this.primaryStage.getScene().getRoot().getStylesheets().clear();
        this.primaryStage.getScene().getRoot().getStylesheets().add(newStyle);

    }


}
