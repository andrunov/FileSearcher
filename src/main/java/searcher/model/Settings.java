package searcher.model;

import searcher.view.Skin;
import searcher.util.AppPreferences;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores application settings and persisted UI state.
 */
public class Settings {

    private String locale;

    private String[] allowedExtensions;

    private boolean exactWordMatch;

    private boolean writeHtmlReport;

    private Skin skin;

    private File initialFirstDir;

    private double mainWindowHeight;

    private double mainWindowWith;

    private double settingsWindowHeight;

    private double settingsWindowWith;

    private double splitPaneDividerPositions;

    private Map<String, Double> tableColumnWidth = new HashMap<>();

    /**
     * Loads persisted settings from the application preferences store.
     */
    public void loadFields() {
        this.locale = AppPreferences.loadLocale();
        this.mainWindowWith = AppPreferences.loadMainWindowWidth();
        this.mainWindowHeight = AppPreferences.loadMainWindowHeight();
        this.allowedExtensions = AppPreferences.loadFilterExtensions();
        this.exactWordMatch = AppPreferences.loadExactWordMatch();
        this.skin = AppPreferences.loadSkin();
        this.writeHtmlReport = AppPreferences.loadWriteHtmlReport();
        this.initialFirstDir = AppPreferences.loadDirectory("firstDirectory");
        this.settingsWindowHeight = AppPreferences.loadSettingsWindowHeight();
        this.settingsWindowWith = AppPreferences.loadSettingsWindowWidth();
        this.splitPaneDividerPositions = AppPreferences.loadSplitPaneDividerPosition();
    }

    /**
     * Saves the current settings to the application preferences store.
     */
    public void saveFields() {
        AppPreferences.saveLocale(this.locale);
        AppPreferences.saveFilterExtensions(this.allowedExtensions);
        AppPreferences.saveExactWordMatch(this.exactWordMatch);
        AppPreferences.saveSkin(this.skin);
        AppPreferences.saveWriteHtmlReport(this.writeHtmlReport);
        AppPreferences.saveDirectory(this.initialFirstDir, "firstDirectory");
        AppPreferences.saveMainWindowWidth(this.mainWindowWith);
        AppPreferences.saveMainWindowHeight(this.mainWindowHeight);
        AppPreferences.saveSettingsWindowHeight(this.settingsWindowHeight);
        AppPreferences.saveSettingsWindowWidth(this.settingsWindowWith);
        AppPreferences.saveSplitPaneDividerPosition(this.splitPaneDividerPositions);
        for (Map.Entry<String, Double> column : this.tableColumnWidth.entrySet()) {
            AppPreferences.saveTableColumnWidth(column.getKey(), column.getValue());
        }
    }


    /**
     * Returns the list of allowed extensions.
     *
     * @return the allowed extensions array
     */
    public String[] getAllowedExtensions() {
        return allowedExtensions;
    }

    /**
     * Sets the allowed extensions list.
     *
     * @param allowedExtensions the extensions to allow
     */
    public void setAllowedExtensions(String[] allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
    }

    /**
     * Indicates whether exact word matching is enabled.
     *
     * @return {@code true} if exact matching is enabled
     */
    public boolean isExactWordMatch() {
        return exactWordMatch;
    }

    /**
     * Enables or disables exact word matching.
     *
     * @param exactWordMatch {@code true} to require exact word matches
     */
    public void setExactWordMatch(boolean exactWordMatch) {
        this.exactWordMatch = exactWordMatch;
    }

    /**
     * Indicates whether HTML reports should be generated.
     *
     * @return {@code true} if HTML reports are enabled
     */
    public boolean isWriteHtmlReport() {
        return writeHtmlReport;
    }

    /**
     * Enables or disables HTML report generation.
     *
     * @param writeHtmlReport {@code true} to generate HTML reports
     */
    public void setWriteHtmlReport(boolean writeHtmlReport) {
        this.writeHtmlReport = writeHtmlReport;
    }

    /**
     * Returns the selected application skin.
     *
     * @return the active skin
     */
    public Skin getSkin() {
        return skin;
    }

    /**
     * Sets the application skin.
     *
     * @param skin the skin to use
     */
    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    /**
     * Returns the current locale code.
     *
     * @return the locale identifier such as {@code en} or {@code ru}
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Sets the current locale code.
     *
     * @param locale the locale identifier to use
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * Returns the initial directory that was previously selected.
     *
     * @return the initial directory or {@code null}
     */
    public File getInitialFirstDir() {
        return initialFirstDir;
    }

    /**
     * Sets the initial directory for the search UI.
     *
     * @param initialFirstDir the directory to remember
     */
    public void setInitialFirstDir(File initialFirstDir) {
        this.initialFirstDir = initialFirstDir;
    }

    /**
     * Returns the saved height of the main window.
     *
     * @return the height in pixels
     */
    public double getMainWindowHeight() {
        return mainWindowHeight;
    }

    /**
     * Sets the saved height of the main window.
     *
     * @param mainWindowHeight the height in pixels
     */
    public void setMainWindowHeight(double mainWindowHeight) {
        this.mainWindowHeight = mainWindowHeight;
    }

    /**
     * Returns the saved width of the main window.
     *
     * @return the width in pixels
     */
    public double getMainWindowWith() {
        return mainWindowWith;
    }

    /**
     * Sets the saved width of the main window.
     *
     * @param mainWindowWith the width in pixels
     */
    public void setMainWindowWith(double mainWindowWith) {
        this.mainWindowWith = mainWindowWith;
    }

    /**
     * Returns the saved height of the settings window.
     *
     * @return the height in pixels
     */
    public double getSettingsWindowHeight() {
        return settingsWindowHeight;
    }

    /**
     * Sets the saved height of the settings window.
     *
     * @param settingsWindowHeight the height in pixels
     */
    public void setSettingsWindowHeight(double settingsWindowHeight) {
        this.settingsWindowHeight = settingsWindowHeight;
    }

    /**
     * Returns the saved width of the settings window.
     *
     * @return the width in pixels
     */
    public double getSettingsWindowWith() {
        return settingsWindowWith;
    }

    /**
     * Sets the saved width of the settings window.
     *
     * @param settingsWindowWith the width in pixels
     */
    public void setSettingsWindowWith(double settingsWindowWith) {
        this.settingsWindowWith = settingsWindowWith;
    }

    /**
     * Returns the saved divider position for the split pane.
     *
     * @return the divider position ratio
     */
    public double getSplitPaneDividerPositions() {
        return splitPaneDividerPositions;
    }

    /**
     * Sets the divider position for the split pane.
     *
     * @param splitPaneDividerPositions the divider position ratio
     */
    public void setSplitPaneDividerPositions(double splitPaneDividerPositions) {
        this.splitPaneDividerPositions = splitPaneDividerPositions;
    }

    /**
     * Returns the preferred width for a table column.
     *
     * @param columnId the column identifier
     * @return the saved width or a default value if unavailable
     */
    public Double getTableColumnWidth(String columnId) {
        Double result = null;
        result = this.tableColumnWidth.get(columnId);
        if (result == null) {
            result = AppPreferences.loadTableColumnWidth(columnId);
            this.tableColumnWidth.put(columnId, result);
        }

        return result;
    }

    /**
     * Stores the preferred width for a table column.
     *
     * @param columnId the column identifier
     * @param columnWidth the width to persist
     */
    public void setTableColumnWidth(String columnId, Double columnWidth) {
        this.tableColumnWidth.put(columnId, columnWidth);
    }

}
