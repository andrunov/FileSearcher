package searcher.util;

import searcher.style.Skin;

import java.io.File;
import java.util.prefs.Preferences;

/**
 * Class for save app's settings outside
 */
public class AppPreferences {

    /*get last selected directory*/
    public static File loadDirectory(String key) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        String filePath = prefs.get(key, null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /*set last selected directory*/
    public static void saveDirectory(File file, String key) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        if (file != null) {
            prefs.put(key, file.getPath());
        }
    }

    /*get files extensions for FileFilter*/
    public static String[] loadFilterExtensions() {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        String extensions = prefs.get("filterExtensions", null);
        if (!Formatter.stringIsEmpty(extensions)) {
            return extensions.split(" ");
        } else {
            return new String[]{};
        }
    }

    /*set files extensions for FileFilter*/
    public static void saveFilterExtensions(String[] extensions) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("filterExtensions", Formatter.getArrayAsString(extensions));
    }


    public static void saveMainWindowWidth(Double width){
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("mainWindowWidth", String.valueOf(width));
    }

    public static double loadMainWindowWidth(){
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        return Double.parseDouble(prefs.get("mainWindowWidth", "650.00"));
         //600 for comparer
    }

    public static void saveMainWindowHeight(Double height){
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("mainWindowHeight", String.valueOf(height));
    }

    public static double loadMainWindowHeight(){
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        return Double.parseDouble(prefs.get("mainWindowHeight", "600.00"));
        //200 for comparer
    }

    public static void saveSettingsWindowHeight(double height) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("settingsWindowHeight", String.valueOf(height));
    }

    public static double loadSettingsWindowHeight() {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        return Double.parseDouble(prefs.get("settingsWindowHeight", "150.00"));
    }

    public static void saveSettingsWindowWidth(double width) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("settingsWindowWidth", String.valueOf(width));
    }

    public static double loadSettingsWindowWidth() {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        return Double.parseDouble(prefs.get("settingsWindowWidth", "600.00"));
    }


    public static void saveExactWordMatch(boolean toShow){
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("exactWordMatch", String.valueOf(toShow));
    }

    public static boolean loadExactWordMatch() {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        return Boolean.parseBoolean(prefs.get("exactWordMatch", "FALSE"));
    }

    public static void saveSplitPaneDividerPosition(Double position) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("splitPaneDividerPosition", String.valueOf(position));
    }

    public static double loadSplitPaneDividerPosition() {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        return Double.parseDouble(prefs.get("splitPaneDividerPosition", "0.18"));
    }

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

    public static void saveTableColumnWidth(String columnId, double width) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        String key = String.format("tableColumnWidth_%s", columnId);
        prefs.put(key, String.valueOf(width));
    }

    public static Skin loadSkin() {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        String saved = prefs.get("skin", "CIAN");
        return Skin.valueOf(saved);
    }

    public static void saveSkin(Skin skin) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("skin", String.valueOf(skin));
    }

    public static boolean loadWriteHtmlReport() {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        String saved = prefs.get("HtmlReport", "true");
        return Boolean.valueOf(saved);
    }

    public static void saveWriteHtmlReport(boolean isWrite) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("HtmlReport", String.valueOf(isWrite));
    }

    public static String loadLocale() {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        return prefs.get("locale", "ru");
    }

    public static void saveLocale(String locale) {
        Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);
        prefs.put("locale", locale);
    }
}
