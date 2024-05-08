package mszalewicz.trygghet.Controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import mszalewicz.trygghet.DB;
import mszalewicz.trygghet.Main;

import java.util.Random;

public class MainController {
    @FXML
    private VBox backgroundVBox;
    @FXML
    private Label welcomeText;
    @FXML
    private ScrollPane passwordList;

    private DB db;
    private Main.SceneManager sceneManager;

    public void initialize() {

//        sceneManager.setStageMaxWidth(1101);

        backgroundVBox.widthProperty().addListener((observable -> {
            welcomeText.setText(Double.toString(backgroundVBox.getWidth()));
        }));

//        backgroundVBox.setMaxWidth(1101);
//



       this.backgroundVBox.setStyle("-fx-background-color: white;");


        VBox passwordsContainer = new VBox();

        passwordsContainer.setStyle("-fx-background-color: white;");

        passwordList.setStyle("-fx-background-color: transparent;");
//        passwordList.setStyle("-fx-background-color: #fafafa;");
        passwordList.setStyle("-fx-focus-color: #f4f4f4;");

        passwordList.setStyle("-fx-focus-color: #dedede; -fx-faint-focus-color: transparent;");
//        passwordList.setStyle("-fx-background-color: white;");

        // todo set scroll pane size corresponding to the current stage size

        Button buttonShowPassword = new Button("Show");
//        buttonShowPassword.setOnMouseClicked();
        Button buttonCopyPasswordToClipboard = new Button("Copy");
//        buttonShowPassword.setOnMouseClicked();
//        Text


        for (int i = 0; i < 50; i++) {
            Label lb1 = prepareServiceName("serviceName" + i);
            Separator separator = new Separator();
            separator.setOrientation(javafx.geometry.Orientation.VERTICAL);
            Separator separator2 = new Separator();
            separator2.setOrientation(javafx.geometry.Orientation.VERTICAL);
            Label lb2 = preparePasswordPlaceholder();

            Button btn1 = new Button("COPY");
            btn1.setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-font-weight: bold;");
            btn1.setMaxWidth(100);
            btn1.setMinWidth(100);
            btn1.setFocusTraversable(false);

            Button btn2 = new Button("CHANGE");
            btn2.setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-font-weight: bold;");
            btn2.setMaxWidth(100);
            btn2.setMinWidth(100);
            btn2.setFocusTraversable(false);

            Button btn3 = new Button("DELETE");
            btn3.setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-font-weight: bold;");
            btn3.setMaxWidth(100);
            btn3.setMinWidth(100);
            btn3.setFocusTraversable(false);


            HBox hbox = new HBox();
            hbox.getChildren().addAll(
                    lb1,
                    separator,
                    getInvisibleSeparator(),
                    lb2,
                    separator2,
                    getInvisibleSeparator(),
                    btn1,
                    getInvisibleSeparator(),
                    btn2,
                    getInvisibleSeparator(),
                    btn3,
                    getInvisibleSeparator()
            );
//            hbox.setMaxWidth(Double.MAX_VALUE);
            hbox.maxWidthProperty().bind(backgroundVBox.widthProperty());


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

    public Label prepareServiceName(String serviceName) {
        int maxLabelLength = 15;
        Label serviceLabel = new Label();
        serviceLabel.setFont(new Font("Courier New", 35));
        serviceLabel.setId("serviceName");
        int whiteCharactersOffsetLength = maxLabelLength - serviceName.length();
        StringBuilder whiteCharactersOffset = new StringBuilder();
        whiteCharactersOffset.append(" ".repeat(whiteCharactersOffsetLength));

        serviceLabel.setText(serviceName + whiteCharactersOffset.toString());
        return serviceLabel;
    }

    public Label preparePasswordPlaceholder() {
        int maxLabelLength = 15;
        Label passwordLabel = new Label();
        passwordLabel.setFont(new Font("Courier New", 35));
        Random rand = new Random();
        int passwordPlaceholderLength = rand.nextInt(4, 15);
        int whiteCharactersOffsetLength = maxLabelLength - passwordPlaceholderLength;

        passwordLabel.setText("☠︎".repeat(passwordPlaceholderLength) + " ".repeat(whiteCharactersOffsetLength));
        return passwordLabel;
    }

    public Region getInvisibleSeparator() {
        Region separator = new Region();
        separator.setPrefWidth(20);
        separator.setMinHeight(0);
        separator.setMaxHeight(Double.MAX_VALUE);
        separator.setStyle("-fx-background-color: transparent;");
        return separator;
    }

    public void onViewShown() {
        this.sceneManager.setStageMaxWidth(1101);
    }

}