package searcher.view;

import java.util.ResourceBundle;

/**
 * Enumerates available application skins and provides helper methods for localization.
 */
public enum Skin {
    CIAN("Cian"),
    GRAY("Gray"),
    GOLD("Gold"),
    NAVY("Navy"),
    PURPLE("Purple");

    private final String repr;

    Skin(String repr) {
        this.repr = repr;
    }

    /**
     * Returns the CSS skin name.
     *
     * @return the skin identifier used in stylesheet names
     */
    public String getRepr() {
        return repr;
    }

    /**
     * Returns the localized labels for all skins.
     *
     * @param resourceBundle the bundle used for localization
     * @return the labels for each skin
     */
    public static String[] getLocaleValues(ResourceBundle resourceBundle) {
        Skin[] values = Skin.values();
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++ ) {
            result[i] = resourceBundle.getString(values[i].toString());
        }
        return result;
    }

    /**
     * Resolves a skin from its localized label.
     *
     * @param resourceBundle the bundle used for localization
     * @param value the localized label to resolve
     * @return the matching skin, or {@code null} if nothing matches
     */
    public static Skin getByLocalValue(ResourceBundle resourceBundle, String value) {
        Skin[] values = Skin.values();
        Skin result = null;
        for (int i = 0; i < values.length; i++ ) {
            String representation = resourceBundle.getString(values[i].toString());
            if (representation.equals(value)) result = values[i];
        }
        return result;
    }


    /**
     * Returns the localized label for this skin.
     *
     * @param resourceBundle the bundle used for localization
     * @return the localized skin label
     */
    public String getLocale(ResourceBundle resourceBundle) {
        return resourceBundle.getString(this.toString());
    }

    /**
     * Returns the border color for a given skin.
     *
     * @param skin the skin whose border color is requested
     * @return the corresponding CSS border color
     */
    public static String getTableBorderColor(Skin skin) {
        if (skin == CIAN) return "navy";
        else if (skin == GRAY) return "#5b5b5b";
        else if (skin == GOLD) return "#8f4407";
        else if (skin == NAVY) return "#000435";
        else if (skin == PURPLE) return "#3c3252";
        else return "navy";
    }

    /**
     * Returns the background color used for report tables.
     *
     * @param skin the skin whose background color is requested
     * @return the CSS background color
     */
    public static String getBgColor(Skin skin) {
        if (skin == CIAN) return "rgba(148, 182, 222, 0.25)";
        else if (skin == GRAY) return "rgba(192, 192, 192, 0.45)";
        else if (skin == GOLD) return "rgba(196, 156, 72, 0.2)";
        else if (skin == NAVY) return "rgba(7, 114, 130, 0.2)";
        else if (skin == PURPLE) return "rgba(119, 91, 171, 0.2)";
        else return "navy";
    }

    /**
     * Returns the text color used for report tables.
     *
     * @param skin the skin whose text color is requested
     * @return the CSS text color
     */
    public static String getTextColor(Skin skin) {
        if (skin == CIAN) return "black";
        else if (skin == GRAY) return "black";
        else if (skin == GOLD) return "#4c3a15";
        else if (skin == NAVY) return "#071e01";
        else if (skin == PURPLE) return "#2a2338";
        else return "navy";
    }

}
