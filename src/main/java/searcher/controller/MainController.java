package searcher.controller;


import searcher.MainApp;
import searcher.view.ReportTableRow;
import searcher.model.FileSearcher;
import searcher.model.Settings;
import searcher.util.ColorController;
import searcher.util.Formatter;
import searcher.util.Message;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller for the main search window.
 */
public class MainController implements Initializable {

    private static final int ROWS_RER_PAGE = 15;

    @FXML
    private SplitPane splitPane;

    @FXML
    private TextField fileNameTextField;

    /**
     * Label displaying the first selected directory.
     */
    @FXML
    private Label firstDirLbl;

    @FXML
    private Button searchBtn;

    /**
     * Button to select the first directory.
     */
    @FXML
    private Button firstDirSelectBtn;

    /**
     * Button to change the application language.
     */
    @FXML
    private Button changeLocalButton;

    /**
     * Button to open the search result file.
     */
    @FXML
    private Button openResultBtn;

    @FXML
    private ProgressBar progressBar;

    /**
     * Button to clear all fields to their default state.
     */
    @FXML
    private Button clearBtn;

    /**
     * Button to open the settings dialog.
     */
    @FXML
    private Button settingsBtn;

    /**
     * Button to show application information.
     */
    @FXML
    private Button aboutBtn;

    /**
     * Button to exit the application.
     */
    @FXML
    private Button exitBtn;

    @FXML
    private TableView tableResult;

    @FXML
    private Pagination pagination;

    /**
     * Resource bundle for localized text.
     */
    private ResourceBundle resourceBundle;

    /**
     * The first directory selected for searching.
     */
    private File firstDirectory;

    /**
     * The second directory for comparison (unused in current version).
     */
    private File secondDirectory;

    /**
     * Reference to the main application instance.
     */
    private MainApp mainApp;

    /**
     * Desktop instance for opening files from the application.
     */
    private Desktop desktop;

    private  List<ReportTableRow> reportTableRowList;

    private Settings settings;

    private String reportName;

    private int searchAttemptNumber;

    /**
     * Creates a controller instance and initializes the desktop helper.
     */
    public MainController() {
        this.reportTableRowList = new ArrayList<>();
        if (Desktop.isDesktopSupported()) {
            this.desktop = Desktop.getDesktop();
        }
    }

    /**
     * Stores a reference to the main application instance.
     *
     * @param mainApp the main application instance
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Selects the first directory via a dialog.
     */
    @FXML
    private void choseFirstDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File initialFirstDir = null;
        if (this.firstDirectory != null) {
            initialFirstDir = this.firstDirectory;
        } else {
            initialFirstDir = this.settings.getInitialFirstDir();
        }
        if ((initialFirstDir != null)&&(initialFirstDir.exists())) {
            directoryChooser.setInitialDirectory(initialFirstDir.getParentFile());
        }
        File selected =  directoryChooser.showDialog(null);
        if (selected != null) {
            this.firstDirectory = selected;
            this.firstDirLbl.setText(getDirInfo(selected));
        }
        System.out.println(this.splitPane.getStylesheets().get(0));

    }

    /**
     * Executes the search when the user presses Enter.
     */
    @FXML
    private void onEnter() {
        this.executeSearch();
    }

    /**
     * Initiates the file search procedure.
     */
    @FXML
    private void executeSearch(){
        if (this.checkFields()) {
            this.searchAttemptNumber = 0;
            this.startSearchTask(false);
        }
    }

    /**
     * Starts the search task asynchronously.
     *
     * @param deepCompare {@code true} to perform a deeper fuzzy comparison
     */
    public void startSearchTask(boolean deepCompare) {


        this.tableResult.getItems().clear();
        this.progressBar.setVisible(true);
        FileSearcher comparer = FileSearcher.createForSearch(this);
        if (deepCompare) {
            comparer.setExactWordMatch(false);
        }

        try{
            Thread thread = new Thread(comparer);
            thread.setDaemon(true);
            thread.start();
            this.progressBar.progressProperty().bind(comparer.progressProperty());
     //     this.firstDirLbl.setAlignment(Pos.CENTER);
     //     this.firstDirLbl.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            this.firstDirLbl.textProperty().bind(comparer.messageProperty());
            this.fileNameTextField.setVisible(false);
            this.searchAttemptNumber++;

        }
        catch (Exception e){
            Message.errorAlert(this.resourceBundle,"Error: ", e);
            e.printStackTrace();
        }
    }

    private boolean checkFields() {
        boolean result = false;
        if ((this.fileNameTextField.getText().trim().isEmpty())
            && (this.firstDirectory == null)) {
                Message.warningAlert(this.resourceBundle,"SelectDirAndWordAlert");
        } else  if (this.firstDirectory == null){
            Message.warningAlert(this.resourceBundle,"SelectDirAlert");
        } else if (this.fileNameTextField.getText().trim().isEmpty()){
            Message.warningAlert(this.resourceBundle,"SelectWordAlert");
        } else result = true;
        return result;
    }

    /**
     * Displays the search results and optionally starts a deeper search.
     *
     * @param report the search result rows to display
     */
    public void showResult(List<ReportTableRow> report) {
        if (report.size() == 0
            && this.searchAttemptNumber < 2
            && this.getSettings().isExactWordMatch()) {
            if(Message.confirmationAlert(this.resourceBundle, "DeepSearchMessage")) {
                this.startSearchTask(true);
            } else {
                this.updateTable(report);
            }
        } else {
            this.updateTable(report);
        }
    }

    private void updateTable(List<ReportTableRow> report) {
        if (this.getSettings().isWriteHtmlReport()) {
            this.openResultBtn.setVisible(true);
        }
        this.progressBar.progressProperty().unbind();
        this.progressBar.setProgress(0);
        this.firstDirLbl.textProperty().unbind();
       // this.firstDirLbl.setAlignment(Pos.CENTER_LEFT);
       // this.firstDirLbl.setStyle(null);
        this.firstDirLbl.setText(getDirInfo(firstDirectory));
        this.fileNameTextField.setVisible(true);
        this.pagination.setPageCount(report.size()/ROWS_RER_PAGE + 1);
        this.pagination.setMaxPageIndicatorCount(15);
        this.pagination.setCurrentPageIndex(0);
        int toIndex = Math.min(ROWS_RER_PAGE, report.size());
        this.tableResult.setItems(FXCollections.observableArrayList(report.subList(0, toIndex)));
        this.reportTableRowList = report;
        this.progressBar.setVisible(false);
    }

    /**
     * Initializes the controller and sets up the resource bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        this.openResultBtn.setVisible(false);
    }

    /**
     * Changes the application locale between available languages.
     */
    @FXML
    public void changeLocale(){
        if (this.settings.getLocale().equals("ru")){
            this.settings.setLocale("en");
            this.resourceBundle = ResourceBundle.getBundle("searcher.bundles.Locale",new Locale("en"));
        } else if (this.settings.getLocale().equals("en")){
            this.settings.setLocale("ru");
            this.resourceBundle = ResourceBundle.getBundle("searcher.bundles.Locale",new Locale("ru"));
        }
        updateLocalText();
    }

    public void setLocale(String locale){
        this.resourceBundle = ResourceBundle.getBundle("searcher.bundles.Locale",new Locale(locale));
        updateLocalText();
    }

    /**
     * Updates UI text after a locale change.
     */
    private void updateLocalText(){
        if (this.firstDirectory != null) {
            this.firstDirLbl.setText(getDirInfo(firstDirectory));
        } else {
            this.firstDirLbl.setText(this.resourceBundle.getString("FirstDirectory"));
        }
        this.firstDirSelectBtn.setText(this.resourceBundle.getString("Select"));
        this.searchBtn.setText(this.resourceBundle.getString("Search"));
        this.changeLocalButton.setText(this.resourceBundle.getString("ChangeLocal"));
        this.clearBtn.setText(this.resourceBundle.getString("Clear"));
        this.openResultBtn.setText(this.resourceBundle.getString("Open"));
        this.settingsBtn.setText(this.resourceBundle.getString("Settings"));
        this.aboutBtn.setText(this.resourceBundle.getString("AppInfo"));
        this.exitBtn.setText(this.resourceBundle.getString("Exit"));
        this.fileNameTextField.setPromptText(this.resourceBundle.getString("FileName"));
        this.updateResultTable();
    }

    /**
     * Converts a directory path to a display string.
     *
     * @param directory the directory to format
     * @return the directory path or empty string
     */
    private String getDirInfo(File directory){
        String result = "";
        if (directory != null) {
            result = directory.getPath();
        }
        return result;
    }

    /**
     * Returns the parent directory and file name from a file path.
     *
     * @param filePath the absolute file path
     * @return the formatted file info string
     */
    private String getFileInfo(String filePath){
        String result = "";
        File file = new File(filePath);
        if (file.exists()){
            result = ": " + file.getParentFile().getPath() + "\\" + file.getName();
        }
        return result;
    }


    /**
     * Injects the application settings into the controller.
     *
     * @param settings the settings object to use
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    /**
     * Returns the application settings used by the controller.
     *
     * @return the settings object
     */
    public Settings getSettings() {
        return this.settings;
    }

    /**
     * Stores the generated report name.
     *
     * @param reportName the report file name
     */
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    /**
     * Returns the selected first directory.
     *
     * @return the selected directory or {@code null}
     */
    public File getFirstDirectory() {
        return firstDirectory;
    }

    /**
     * Returns the currently active resource bundle.
     *
     * @return the active resource bundle
     */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * Returns the current search phrase entered by the user.
     *
     * @return the trimmed search phrase
     */
    public String getSearchPhrase() {
        return this.fileNameTextField.getText().trim();
    }

    /**
     * Opens the saved report file using the system default application.
     */
    @FXML
    private void openResult(){
        try {
            assert this.desktop != null;
            this.desktop.open(new File(this.reportName));
        } catch (Exception e) {
            Message.errorAlert(this.resourceBundle, "Error in MainController.openResult() ", e);
        }
    }

    /**
     * Clears all search fields and results to the default state.
     */
    @FXML
    private void clear(){
        this.firstDirectory = null;
        this.secondDirectory = null;
        this.reportTableRowList.clear();
        this.tableResult.getItems().clear();
        this.pagination.setPageCount(1);
        this.fileNameTextField.clear();
        this.firstDirLbl.setText(this.resourceBundle.getString("FirstDirectory"));
        this.openResultBtn.setVisible(false);
    }

    /**
     * Opens the settings dialog.
     */
    @FXML
    private void openSettings(){
        mainApp.showSettingsEditDialog(this.resourceBundle);
    }

    /**
     * Shows the application information dialog.
     */
    @FXML
    private void showAppInfo(){
        Message.info(this.resourceBundle,"AboutApp");
    }

    /**
     * Exits the application.
     */
    @FXML
    private void doExitApp(){
        this.mainApp.getPrimaryStage().close();
    }

    /**
     * Listener that adjusts font sizes when the window is resized.
     */
    public ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
    {
        double newHeight = this.mainApp.getPrimaryStage().getHeight();
        double newWidth = Formatter.getTextSize(newHeight);
        String newSize = "-fx-font-size:" +  String.valueOf(newWidth) + ";";
        this.fileNameTextField.setStyle(newSize);
        this.searchBtn.setStyle(newSize);
        this.firstDirLbl.setStyle(newSize);
        this.firstDirSelectBtn.setStyle(newSize);
        this.changeLocalButton.setStyle(newSize);
        this.openResultBtn.setStyle(newSize);
        this.clearBtn.setStyle(newSize);
        this.settingsBtn.setStyle(newSize);
        this.aboutBtn.setStyle(newSize);
        this.exitBtn.setStyle(newSize);
        this.tableResult.setStyle(newSize);
        this.pagination.setStyle(newSize);
    };

    /**
     * Configures the results table columns and row rendering.
     */
    public void setupResultTable() {

        this.tableResult.setPlaceholder(new Label(this.resourceBundle.getString("TableViewPlaceholder")));

        ObservableList<TableColumn<ReportTableRow, String>> columns = this.tableResult.getColumns();
        for (TableColumn<ReportTableRow, String> column : columns) {
            if (column.getId().equals("rowSimilar")) {
                column.setCellValueFactory(new PropertyValueFactory<>("PercSimilarity"));
            }
            if (column.getId().equals("rowFolderName")) {
                column.setCellValueFactory(new PropertyValueFactory<>("BaseFolderPathFormatted"));
            }
            if (column.getId().equals("rowFileName")) {
                column.setCellValueFactory(new PropertyValueFactory<>("Name"));
            }
            if (column.getId().equals("rowFileSize")) {
                column.setCellValueFactory(new PropertyValueFactory<>("SizeFormatted"));
                column.setStyle("-fx-alignment: CENTER_RIGHT;");
            }
        }

        this.tableResult.setRowFactory( tv -> {
            TableRow<ReportTableRow> row = new TableRow<ReportTableRow>() {
                @Override
                protected void updateItem(ReportTableRow rowTableData, boolean empty) {
                    super.updateItem(rowTableData, empty);
                    if (rowTableData == null) {
                        setStyle("-fx-background-color: white;");
                    } else {
                        int similarity = rowTableData.getSimilarity();
                        String bgRGB = ColorController.getBgRGB(similarity);
                        String cssFormatString = String.format("-fx-background-color: rgba(%s,0.05);", bgRGB);
                        if (rowTableData.isDirectory()) {
                            cssFormatString = cssFormatString + "-fx-font-weight: bold;";
                        }
                        setStyle(cssFormatString);
                    }
                }
            };

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    ReportTableRow reportTableRow = row.getItem();
                    String columnID = row.getTableView().getSelectionModel().getSelectedCells().get(0).getTableColumn().getId();
                    if (columnID.equals("rowFolderName")) {
                        try {
                            assert this.desktop != null;
                            this.desktop.open(new File(reportTableRow.getBaseFolderPath()));
                        } catch (Exception e) {
                            Message.errorAlert(this.resourceBundle, "Error in MainController.openResult() ", e);
                        }
                    } else if (columnID.equals("rowFileName")) {
                        try {
                            assert this.desktop != null;
                            this.desktop.open(new File(reportTableRow.getAbsolutePath()));
                        } catch (Exception e) {
                            Message.errorAlert(this.resourceBundle, "Error in MainController.openResult() ", e);
                        }
                    }
                }
            });

            return row ;
        });

    }

    /**
     * Updates the table labels after a locale change.
     */
    public void updateResultTable() {
        this.tableResult.setPlaceholder(new Label(this.resourceBundle.getString("TableViewPlaceholder")));
        ObservableList<TableColumn<ReportTableRow, String>> columns = this.tableResult.getColumns();
        for (TableColumn<ReportTableRow, String> column : columns) {
            if (column.getId().equals("rowSimilar")) {
                column.setText(this.resourceBundle.getString("Similar"));
            }
            if (column.getId().equals("rowFolderName")) {
                column.setText(this.resourceBundle.getString("Folder"));
            }
            if (column.getId().equals("rowFileName")) {
                column.setText(this.resourceBundle.getString("FileName"));
            }
            if (column.getId().equals("rowFileSize")) {
                column.setText(this.resourceBundle.getString("FileSize"));
            }
        }
    }

    /**
     * Configures the pagination control for the results view.
     */
    public void setupPagination() {
        this.pagination.setPageCount(1);
        this.pagination.setPageFactory(this::createPage);
    }

    /**
     * Applies persisted settings to the main UI.
     *
     * @param settings the settings instance containing layout and column preferences
     */
    public void loadSettings(Settings settings) {
        this.settings = settings;
        this.splitPane.setDividerPosition(0, this.settings.getSplitPaneDividerPositions());
        ObservableList<TableColumn<ReportTableRow, String>> columns = this.tableResult.getColumns();
        for (TableColumn<ReportTableRow, String> column : columns) {
            if (column.isVisible()) {
                double prefWidth = this.settings.getTableColumnWidth(column.getId());
                column.setPrefWidth(prefWidth);
            }
        }
    }

    /**
     * Persists the current UI state into the settings object.
     */
    public void saveSettings() {
        this.settings.setInitialFirstDir(this.firstDirectory);
        this.settings.setSplitPaneDividerPositions(this.splitPane.getDividerPositions()[0]);
        ObservableList<TableColumn<ReportTableRow, String>> columns = this.tableResult.getColumns();
        for (TableColumn<ReportTableRow, String> column : columns) {
            if (column.isVisible()) {
                this.settings.setTableColumnWidth(column.getId(), column.getWidth());
            }
        }
    }


    /**
     * Creates a page for the results pagination.
     *
     * @param pageIndex the page index to render
     * @return the bordered pane containing the table for this page
     */
    private Node createPage(int pageIndex) {

        int fromIndex = pageIndex * ROWS_RER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_RER_PAGE, this.reportTableRowList.size());
        if (this.reportTableRowList.size() > 0) {
            tableResult.setItems(FXCollections.observableArrayList(this.reportTableRowList.subList(fromIndex, toIndex)));
        } else {
            tableResult.setItems(FXCollections.observableArrayList(this.reportTableRowList));
        }

        return new BorderPane(tableResult);
    }

}
