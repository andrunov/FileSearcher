package searcher.model;

import searcher.style.Skin;
import searcher.util.AppPreferences;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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


    public String[] getAllowedExtensions() {
        return allowedExtensions;
    }

    public void setAllowedExtensions(String[] allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
    }

    public boolean isExactWordMatch() {
        return exactWordMatch;
    }

    public void setExactWordMatch(boolean exactWordMatch) {
        this.exactWordMatch = exactWordMatch;
    }

    public boolean isWriteHtmlReport() {
        return writeHtmlReport;
    }

    public void setWriteHtmlReport(boolean writeHtmlReport) {
        this.writeHtmlReport = writeHtmlReport;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public File getInitialFirstDir() {
        return initialFirstDir;
    }

    public void setInitialFirstDir(File initialFirstDir) {
        this.initialFirstDir = initialFirstDir;
    }

    public double getMainWindowHeight() {
        return mainWindowHeight;
    }

    public void setMainWindowHeight(double mainWindowHeight) {
        this.mainWindowHeight = mainWindowHeight;
    }

    public double getMainWindowWith() {
        return mainWindowWith;
    }

    public void setMainWindowWith(double mainWindowWith) {
        this.mainWindowWith = mainWindowWith;
    }

    public double getSettingsWindowHeight() {
        return settingsWindowHeight;
    }

    public void setSettingsWindowHeight(double settingsWindowHeight) {
        this.settingsWindowHeight = settingsWindowHeight;
    }

    public double getSettingsWindowWith() {
        return settingsWindowWith;
    }

    public void setSettingsWindowWith(double settingsWindowWith) {
        this.settingsWindowWith = settingsWindowWith;
    }

    public double getSplitPaneDividerPositions() {
        return splitPaneDividerPositions;
    }

    public void setSplitPaneDividerPositions(double splitPaneDividerPositions) {
        this.splitPaneDividerPositions = splitPaneDividerPositions;
    }

    public Double getTableColumnWidth(String columnId) {
        Double result = null;
        result = this.tableColumnWidth.get(columnId);
        if (result == null) {
            result = AppPreferences.loadTableColumnWidth(columnId);
            this.tableColumnWidth.put(columnId, result);
        }

        return result;
    }

    public void setTableColumnWidth(String columnId, Double columnWidth) {
        this.tableColumnWidth.put(columnId, columnWidth);
    }

}
