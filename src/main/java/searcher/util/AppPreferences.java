package searcher.util;

import searcher.view.Skin;

import java.io.File;
import java.util.prefs.Preferences;

/**
 * Persists application settings in the Java preferences store.
 */
public class AppPreferences {

    /**
     * Loads a previously selected directory from preferences.
     *
     * @param key the preferences key
     * @return the stored directory or {@code null}
     */
    public static File loadDirectory(String key) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        String filePath = prefs.get(key, null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Stores a selected directory in preferences.
     *
     * @param file the directory to persist
     * @param key the preferences key
     */
    public static void saveDirectory(File file, String key) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        if (file != null) {
            prefs.put(key, file.getPath());
        }
    }

    /**
     * Loads the configured file extensions for filtering.
     *
     * @return the allowed extensions
     */
    public static String[] loadFilterExtensions() {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        String extensions = prefs.get("filterExtensions", null);
        if (!Formatter.stringIsEmpty(extensions)) {
            return extensions.split(" ");
        } else {
            return new String[]{};
        }
    }

    /**
     * Stores the configured file extensions for filtering.
     *
     * @param extensions the allowed extensions to persist
     */
    public static void saveFilterExtensions(String[] extensions) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("filterExtensions", Formatter.getArrayAsString(extensions));
    }


    /**
     * Stores the main window width.
     *
     * @param width the width to persist
     */
    public static void saveMainWindowWidth(Double width){
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("mainWindowWidth", String.valueOf(width));
    }

    /**
     * Loads the main window width.
     *
     * @return the saved width, defaulting to 650.00
     */
    public static double loadMainWindowWidth(){
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        return Double.parseDouble(prefs.get("mainWindowWidth", "650.00"));
         //600 for comparer
    }

    /**
     * Stores the main window height.
     *
     * @param height the height to persist
     */
    public static void saveMainWindowHeight(Double height){
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("mainWindowHeight", String.valueOf(height));
    }

    /**
     * Loads the main window height.
     *
     * @return the saved height, defaulting to 600.00
     */
    public static double loadMainWindowHeight(){
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        return Double.parseDouble(prefs.get("mainWindowHeight", "600.00"));
        //200 for comparer
    }

    /**
     * Stores the settings window height.
     *
     * @param height the height to persist
     */
    public static void saveSettingsWindowHeight(double height) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("settingsWindowHeight", String.valueOf(height));
    }

    /**
     * Loads the settings window height.
     *
     * @return the saved height, defaulting to 150.00
     */
    public static double loadSettingsWindowHeight() {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        return Double.parseDouble(prefs.get("settingsWindowHeight", "150.00"));
    }

    /**
     * Stores the settings window width.
     *
     * @param width the width to persist
     */
    public static void saveSettingsWindowWidth(double width) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("settingsWindowWidth", String.valueOf(width));
    }

    /**
     * Loads the settings window width.
     *
     * @return the saved width, defaulting to 600.00
     */
    public static double loadSettingsWindowWidth() {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        return Double.parseDouble(prefs.get("settingsWindowWidth", "600.00"));
    }


    /**
     * Stores whether exact word matching is enabled.
     *
     * @param toShow {@code true} to enable exact matching
     */
    public static void saveExactWordMatch(boolean toShow){
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("exactWordMatch", String.valueOf(toShow));
    }

    /**
     * Loads whether exact word matching is enabled.
     *
     * @return {@code true} if exact matching is enabled
     */
    public static boolean loadExactWordMatch() {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        return Boolean.parseBoolean(prefs.get("exactWordMatch", "FALSE"));
    }

    /**
     * Stores the split pane divider position.
     *
     * @param position the divider position to persist
     */
    public static void saveSplitPaneDividerPosition(Double position) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("splitPaneDividerPosition", String.valueOf(position));
    }

    /**
     * Loads the split pane divider position.
     *
     * @return the saved divider position, defaulting to 0.18
     */
    public static double loadSplitPaneDividerPosition() {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        return Double.parseDouble(prefs.get("splitPaneDividerPosition", "0.18"));
    }

    /**
     * Loads the preferred width for a table column.
     *
     * @param columnId the column identifier
     * @return the saved width or a default based on the column
     */
    public static double loadTableColumnWidth(String columnId) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        String key = String.format("tableColumnWidth_%s", columnId);
        String def = null;
        if (columnId.equals("rowSimilar")) {
            def = "50";
        } else if (columnId.equals("rowFolderName")) {
            def = "215";
        } else if (columnId.equals("rowFileName")) {
            def = "275";
        } else if (columnId.equals("rowFileSize")) {
            def = "90";
        }
        return Double.parseDouble(prefs.get(key, def));
    }

    /**
     * Stores the preferred width for a table column.
     *
     * @param columnId the column identifier
     * @param width the width to persist
     */
    public static void saveTableColumnWidth(String columnId, double width) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        String key = String.format("tableColumnWidth_%s", columnId);
        prefs.put(key, String.valueOf(width));
    }

    /**
     * Loads the selected application skin.
     *
     * @return the saved skin or {@link Skin#CIAN} by default
     */
    public static Skin loadSkin() {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        String saved = prefs.get("skin", "CIAN");
        return Skin.valueOf(saved);
    }

    /**
     * Stores the selected application skin.
     *
     * @param skin the skin to persist
     */
    public static void saveSkin(Skin skin) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("skin", String.valueOf(skin));
    }

    /**
     * Loads whether HTML reports should be generated.
     *
     * @return {@code true} if HTML reports are enabled
     */
    public static boolean loadWriteHtmlReport() {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        String saved = prefs.get("HtmlReport", "true");
        return Boolean.valueOf(saved);
    }

    /**
     * Stores whether HTML reports should be generated.
     *
     * @param isWrite {@code true} to generate HTML reports
     */
    public static void saveWriteHtmlReport(boolean isWrite) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("HtmlReport", String.valueOf(isWrite));
    }

    /**
     * Loads the current locale code.
     *
     * @return the saved locale identifier
     */
    public static String loadLocale() {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        return prefs.get("locale", "ru");
    }

    /**
     * Stores the current locale code.
     *
     * @param locale the locale identifier to persist
     */
    public static void saveLocale(String locale) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("locale", locale);
    }
}
