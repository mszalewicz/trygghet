package mszalewicz.trygghet.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mszalewicz.trygghet.DB;
import mszalewicz.trygghet.Main;

public class MainController {
    @FXML
    private Label welcomeText;
    @FXML
    private ScrollPane passwordList;

    private DB db;
    private Main.SceneManager sceneManager;

    public void initialize() {
        VBox passwordsContainer = new VBox();

        passwordList.setStyle("-fx-background-color: #fafafa;");
        passwordList.setStyle("-fx-focus-color: #f4f4f4;");
        passwordList.setStyle("-fx-focus-color: #dedede; -fx-faint-focus-color: transparent;");
        // todo set scroll pane size corresponding to the current stage size

        Button buttonShowPassword = new Button("Show");
//        buttonShowPassword.setOnMouseClicked();
        Button buttonCopyPasswordToClipboard= new Button("Copy");
//        buttonShowPassword.setOnMouseClicked();
//        Text


        for (int i = 0; i < 50; i++) {
            Button btn1 = new Button("a" + i);
            btn1.setFocusTraversable(false);
            Button btn2 = new Button("a" + i);
            btn2.setFocusTraversable(false);

            HBox hbox = new HBox();
            hbox.getChildren().addAll(btn1, btn2);

            passwordsContainer.getChildren().addAll(hbox);
        }
        passwordList.setContent(passwordsContainer);
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public void setSceneManager(Main.SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}