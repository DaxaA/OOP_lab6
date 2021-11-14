package document;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Error {
    static void errorProcession(Exception e, String message) {
        Error.errorShow(message + ": " + e.getMessage());
    }

    private static void errorShow(String message) {
        new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).show();
    }
}
