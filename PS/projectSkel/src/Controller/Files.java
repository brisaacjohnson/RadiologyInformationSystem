package Controller;
import Controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Files {
    @FXML
    private Label lblText;

    public void handleButton() {
        lblText.setText("");
    }
}
