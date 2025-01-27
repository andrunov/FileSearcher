package searcher;
/**
 * Class with main method
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

//TODO 6. Вынести все настройки в Settings
//TODO 8. Сделать рефакторинг переменных и методов
//TODO 9. Сделать разделение на comparer и searcher (не забыть про разные пути сохранения параметров)

/*Main app JavaFX class */
public class MainApp extends Application {

    /*primary app stage*/
    private Stage primaryStage;

    /*root layout element*/
    private SplitPane rootLayout;

    /*link to main controller*/
    private MainController mainController;

    private Settings settings;

    /*main method*/
    public static void main(String[] args) {
        launch(args);
    }

    /*entry JavaFX method*/
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("File searcher");
        this.primaryStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/searcher/resources/images/glass.png")));
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
     * open main window
     */
    public void initRootLayout(Locale locale) {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(ResourceBundle.getBundle("searcher.resources.bundles.Locale", locale));
            loader.setLocation(MainApp.class.getResource("view/MainView.fxml"));
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

    /*open settings window*/
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

    /*getter for primary stage*/
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void updateSkin(String skinValue) {
        String newStyle = String.format("searcher/style/%s.css", skinValue);
        this.primaryStage.getScene().getRoot().getStylesheets().clear();
        this.primaryStage.getScene().getRoot().getStylesheets().add(newStyle);

    }


}
