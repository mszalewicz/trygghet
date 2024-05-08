package mszalewicz.trygghet.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import mszalewicz.trygghet.DB;
import mszalewicz.trygghet.Main;

public class MasterPasswordInsertController {
    @FXML
    private PasswordField masterPasswordFirstInput;
    @FXML
    private PasswordField masterPasswordSecondInput;
    @FXML
    private Label confirmErrorLabel;

    private DB db;
    private Main.SceneManager sceneManager;

    public void initialize() {
        Platform.runLater(() -> masterPasswordFirstInput.requestFocus());
    }

    protected enum PasswordsErrors {
        EMPTY,
        NOT_EQUAL;
    }

    @FXML
    protected void confirmMasterPasswordButtonClick() {
        String passInputOne = masterPasswordFirstInput.getText();
        String passInputTwo = masterPasswordSecondInput.getText();

        if (passInputOne.isEmpty() || passInputTwo.isEmpty()) {
            passwordsProblem(PasswordsErrors.EMPTY);
        }

        if (passInputOne.equals(passInputTwo)) {
            db.insertFirstMasterPassword(passInputOne);
            sceneManager.switchScene(Main.SceneManager.Scenes.MAIN);
        } else {
            passwordsProblem(PasswordsErrors.NOT_EQUAL);
        }
    }

    @FXML
    protected void passwordsProblem(PasswordsErrors error) {
        switch (error) {
            case EMPTY:
                confirmErrorLabel.setText("Password can not be empty.");
                confirmErrorLabel.setAlignment(Pos.CENTER);
                break;
            case NOT_EQUAL:
                confirmErrorLabel.setText("Passwords do not match.");
                confirmErrorLabel.setAlignment(Pos.CENTER);
                break;
        }
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public void setSceneManager(Main.SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}
