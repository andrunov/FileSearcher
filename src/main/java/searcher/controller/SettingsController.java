package searcher.controller;

import searcher.MainApp;
import searcher.model.Settings;
import searcher.view.Skin;
import searcher.util.Formatter;
import searcher.util.Message;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.ResourceBundle;

/**
 * Controller for the settings dialog.
 */
public class SettingsController {

    /**
     * The dialog stage.
     */
    private Stage dialogStage;

    /**
     * The resource bundle for localization.
     */
    private ResourceBundle resourceBundle;

    private Settings settings;

    private Skin skin;

    /**
     * Text field for configurable filter.
     */
    @FXML
    private TextField filterTextField;

    /**
     * Button to save settings and close.
     */
    @FXML
    private Button saveBtn;

    /**
     * Button to cancel changes and close.
     */
    @FXML
    private Button cancelBtn;

    /**
     * Button to show help for the filter field.
     */
    @FXML
    private Button questionFilter;

    /**
     * Button to show help for word matching options.
     */
    @FXML
    private Button exactWordMatchBtn;

    @FXML
    private Button saveHtmlBtn;

    /**
     * Label for the filter field.
     */
    @FXML
    private Label filterLbl;

    /**
     * Label for the first option group.
     */
    @FXML
    private Label option1Lbl;

    @FXML
    private Label option2Lbl;

    /**
     * Checkbox for enabling exact word matching.
     */
    @FXML
    private CheckBox exactWordMatchLbl;

    @FXML
    private CheckBox saveHtmlChBox;

    @FXML
    private Label skinLbl;

    @FXML
    private ComboBox<String> skinChoiceBox;


    /**
     * Assigns the localized resources used by the dialog.
     *
     * @param resourceBundle the resource bundle to use
     */
    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    /**
     * Sets the main application reference.
     *
     * @param mainApp the main application instance
     */
    public void setMainApp(MainApp mainApp) {
    }

    /**
     * Populates the dialog controls with the current settings values.
     */
    public void setFieldsValues(){
        this.filterTextField.setText(Formatter.getArrayAsString(this.settings.getAllowedExtensions()));
        this.exactWordMatchLbl.setSelected(this.settings.isExactWordMatch());
        this.saveHtmlChBox.setSelected(this.settings.isWriteHtmlReport());

        ObservableList<String> langs = FXCollections.observableArrayList(Skin.getLocaleValues(this.resourceBundle));
        this.skinChoiceBox.setItems(langs);
        this.skinChoiceBox.setValue(this.settings.getSkin().getLocale(this.resourceBundle));
        this.skinChoiceBox.setOnAction(event -> {
            this.skin = Skin.getByLocalValue(this.resourceBundle, this.skinChoiceBox.getValue());
            this.updateSkin(this.skin.getRepr());
        });
        this.skin = this.settings.getSkin();
        this.updateSkin(this.skin.getRepr());
    }

    private void updateSkin(String skinValue) {
        String newStyle = String.format("searcher/style/%s.css", skinValue);
        this.dialogStage.getScene().getRoot().getStylesheets().clear();
        this.dialogStage.getScene().getRoot().getStylesheets().add(newStyle);
    }


    /**
     * Sets the dialog stage for this window.
     *
     * @param dialogStage the dialog window stage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    /**
     * Injects the settings object used by the dialog.
     *
     * @param settings the settings instance to edit
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    /**
     * Handles the cancel button click.
     */
    @FXML
    private void cancel() {

        this.settings.setSettingsWindowHeight(this.dialogStage.getHeight());
        this.settings.setSettingsWindowWith(this.dialogStage.getWidth());
        dialogStage.close();
    }

    /**
     * Handles the save button click.
     */
    @FXML
    private void save() {
        if (isInputValid()) {
            String[] extensions = new String[0];
            if (!Formatter.stringIsEmpty(this.filterTextField.getText())){
                extensions = this.filterTextField.getText().split(" ");
            }
            this.settings.setAllowedExtensions(extensions);
            this.settings.setExactWordMatch(this.exactWordMatchLbl.isSelected());
            this.settings.setWriteHtmlReport(this.saveHtmlChBox.isSelected());
            this.settings.setSkin(this.skin);

            //update main stage skin
            Window primaryStage = this.dialogStage.getOwner();
            String newStyle = String.format("searcher/style/%s.css", this.skin.getRepr());
            primaryStage.getScene().getRoot().getStylesheets().clear();
            primaryStage.getScene().getRoot().getStylesheets().add(newStyle);

            this.settings.setSettingsWindowHeight(this.dialogStage.getHeight());
            this.settings.setSettingsWindowWith(this.dialogStage.getWidth());
            dialogStage.close();
        }
    }

    /**
     * Shows the help dialog for the filter field.
     */
    @FXML
    private void showFilterInfo(){
        Message.info(this.resourceBundle,"FilterInfo");
    }

    /**
     * Shows the help dialog for word matching options.
     */
    @FXML
    private void showWordMatchInfo(){
        Message.info(this.resourceBundle,"WordMatchInfo");
    }

    @FXML
    public void showSaveHtmlInfo() {
        Message.info(this.resourceBundle,"SaveHtmlInfo");
    }


    /**
     * Validates user input for the filter field.
     *
     * @return {@code true} if the input is valid
     */
    private boolean isInputValid() {
        String filterExtensions = this.filterTextField.getText();
        if ((!filterExtensions.matches("[a-zA-Z0-9\\s]+"))&&(!filterExtensions.isEmpty())){
            Message.errorAlert(this.resourceBundle,"FilterExtensionException");
            return false;
        }
        return true;
    }

    /**
     * Listener that adjusts font sizes when the window is resized.
     */
    public ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
    {
        double newHeight = this.dialogStage.getHeight() * 3.5;
        double newWidth = Formatter.getTextSize(newHeight);
        String newSize = "-fx-font-size:" +  String.valueOf(newWidth) + ";";
        this.filterTextField.setStyle(newSize);
        this.exactWordMatchBtn.setStyle(newSize);
        this.saveBtn.setStyle(newSize);
        this.cancelBtn.setStyle(newSize);
        this.saveHtmlBtn.setStyle(newSize);
        this.questionFilter.setStyle(newSize);
        this.filterLbl.setStyle(newSize);
        this.option1Lbl.setStyle(newSize);
        this.option2Lbl.setStyle(newSize);
        this.exactWordMatchLbl.setStyle(newSize);
        this.skinLbl.setStyle(newSize);
        this.skinChoiceBox.setStyle(newSize);
        this.saveHtmlChBox.setStyle(newSize);
    };

}
