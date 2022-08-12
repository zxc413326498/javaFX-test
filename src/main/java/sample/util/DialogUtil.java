package sample.util;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class DialogUtil {
    public static void showDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(new ButtonType("确认", ButtonBar.ButtonData.OK_DONE));
        dialog.setTitle("No Selection");
        dialog.setHeaderText("No Person Selected");
        dialog.setContentText("Please select a person in the table.");
        dialog.show();
    }

    public static void showDialogMessage(String title,String text,String content) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(new ButtonType("确认", ButtonBar.ButtonData.OK_DONE));
        dialog.setTitle(title);
        dialog.setHeaderText(text);
        dialog.setContentText(content);
        dialog.show();
    }
}
