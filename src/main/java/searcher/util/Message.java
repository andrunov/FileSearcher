package searcher.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Displays localized alerts for the application.
 */
public class Message {

    /**
     * Shows an informational alert.
     *
     * @param resourceBundle the bundle used for localized text
     * @param message the message key to resolve
     */
    public static void info(ResourceBundle resourceBundle, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resourceBundle.getString("InfoTitle"));
        alert.setHeaderText(resourceBundle.getString("InfoHeaderTex"));
        alert.setContentText(resourceBundle.getString(message));
        alert.showAndWait();
    }

    /**
     * Shows a warning alert.
     *
     * @param resourceBundle the bundle used for localized text
     * @param message the message key to resolve
     */
    public static void warningAlert(ResourceBundle resourceBundle, String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(resourceBundle.getString("AlertTitle"));
        alert.setHeaderText(resourceBundle.getString("WarningAlertHeaderTex"));
        alert.setContentText(resourceBundle.getString(message));
        alert.showAndWait();
    }

    /**
     * Shows an error alert for a throwable.
     *
     * @param resourceBundle the bundle used for localized text
     * @param message the message prefix to show
     * @param throwable the exception whose message will be included
     */
    public static void errorAlert(ResourceBundle resourceBundle, String message, Throwable throwable){
        if (throwable.getMessage()!=null) {
            errorAlert(resourceBundle,message + " " + throwable.getMessage());
        }else {
            errorAlert(resourceBundle, message + " " + throwable.toString());
        }
    }

    /**
     * Shows an error alert.
     *
     * @param resourceBundle the bundle used for localized text
     * @param message the message text to show
     */
    public static void errorAlert(ResourceBundle resourceBundle, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resourceBundle.getString("ErrorTitle"));
        alert.setHeaderText(resourceBundle.getString("ErrorAlertHeaderTex"));
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a confirmation alert and returns the user's choice.
     *
     * @param resourceBundle the bundle used for localized text
     * @param message the message key to resolve
     * @return {@code true} if the user accepted the confirmation
     */
    public static boolean confirmationAlert(ResourceBundle resourceBundle, String message){
        boolean result = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(resourceBundle.getString("ConfirmTitle"));
        alert.setHeaderText(resourceBundle.getString("ConfirmHeaderTex"));
        alert.setContentText(resourceBundle.getString(message));
        Optional<ButtonType> button = alert.showAndWait();
        if(button.isPresent() && button.get().equals(ButtonType.OK)) {
            result = true;
        }
        return result;
    }

}
