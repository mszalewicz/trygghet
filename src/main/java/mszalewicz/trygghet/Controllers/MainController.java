package mszalewicz.trygghet.Controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import mszalewicz.trygghet.DB;
import mszalewicz.trygghet.Main;
import mszalewicz.trygghet.ManagerModel;

import java.util.List;
import java.util.Random;

public class MainController {
    @FXML
    private VBox backgroundVBox;
    @FXML
    private Label welcomeText;
    @FXML
    private ScrollPane passwordList;
    @FXML
    private HBox bottomControls;

    public double windowHeight;
    private ManagerModel model;
    private Main.SceneManager sceneManager;

    public MainController(ManagerModel model, Main.SceneManager sceneManager) {
        this.model = model;
        this.sceneManager = sceneManager;
    }

    public void initialize() {
        passwordList.setMinHeight(this.windowHeight * 0.8);
        passwordList.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        populatePasswordEntriesList();
    }

    public void populatePasswordEntriesList() {
        passwordList.setContent(null);
        bottomControls.getChildren().clear();

        VBox passwordsContainer = new VBox();

        this.backgroundVBox.setStyle("-fx-background-color: white;");
        passwordsContainer.setStyle("-fx-background-color: white;");
        passwordList.setStyle("-fx-background-color: transparent;");
        passwordList.setStyle("-fx-focus-color: #f4f4f4;");
        passwordList.setStyle("-fx-focus-color: #dedede; -fx-faint-focus-color: transparent;");

        List<String> serviceNames = this.model.getAllPasswordServiceNames();

        for (String service : serviceNames) {
            // Create label - store service name, password and white space separators in form of labels
            Label spaceLabel_1 = new Label(" ");
            spaceLabel_1.setFont(new Font("Courier New", 35));
            Label spaceLabel_2 = new Label(" ");
            spaceLabel_2.setFont(new Font("Courier New", 35));
            Label serviceNameLabel = new Label(service);
            serviceNameLabel.setFont(new Font("Courier New", 35));
            serviceNameLabel.setId("serviceName");
            Label serviceNameOffsetLabel = prepareOffsetLabel(service);
            serviceNameOffsetLabel.setFont(new Font("Courier New", 35));
            Label passwordPlaceholderLabel = preparePasswordPlaceholder();
            passwordPlaceholderLabel.setFont(new Font("Courier New", 35));

            // Create vertical separators
            Separator separator_1 = new Separator();
            separator_1.setOrientation(javafx.geometry.Orientation.VERTICAL);
            Separator separator_2 = new Separator();
            separator_2.setOrientation(javafx.geometry.Orientation.VERTICAL);

            // Create action buttons for the given password entry row
            Button copyPasswordButton = new Button("COPY");
            copyPasswordButton.setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-font-weight: bold;");
            copyPasswordButton.setMaxWidth(100);
            copyPasswordButton.setMinWidth(100);
            copyPasswordButton.setFocusTraversable(false);

            Button changePasswordButton = new Button("CHANGE");
            changePasswordButton.setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-font-weight: bold;");
            changePasswordButton.setMaxWidth(100);
            changePasswordButton.setMinWidth(100);
            changePasswordButton.setFocusTraversable(false);

            Button deletePasswordButton = new Button("DELETE");
            deletePasswordButton.setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-font-weight: bold;");
            deletePasswordButton.setMaxWidth(100);
            deletePasswordButton.setMinWidth(100);
            deletePasswordButton.setFocusTraversable(false);
            deletePasswordButton.setOnMouseClicked(this::deletePasswordEntryOnClick);

            // Create Vbox containers for buttons to center them vertically in a given row
            VBox vBox_1 = new VBox();
            VBox vBox_2 = new VBox();
            VBox vBox_3 = new VBox();

            vBox_1.getChildren().add(copyPasswordButton);
            vBox_2.getChildren().add(changePasswordButton);
            vBox_3.getChildren().add(deletePasswordButton);

            vBox_1.setAlignment(Pos.CENTER);
            vBox_2.setAlignment(Pos.CENTER);
            vBox_3.setAlignment(Pos.CENTER);

            // Pack all row ui components into container
            HBox row = new HBox();
            row.getChildren().addAll(
                    spaceLabel_1,
                    serviceNameLabel,
                    serviceNameOffsetLabel,
                    separator_1,
                    spaceLabel_2,
                    passwordPlaceholderLabel,
                    getInvisibleRegionSeparator(),
                    separator_2,
                    getInvisibleRegionSeparator(),
                    vBox_1,
                    getInvisibleRegionSeparator(),
                    vBox_2,
                    getInvisibleRegionSeparator(),
                    vBox_3,
                    getInvisibleRegionSeparator()
            );
            passwordsContainer.getChildren().addAll(row);
        }
        passwordList.setContent(passwordsContainer);

        // Create bottom controls

        Button newPasswordEntryButton = new Button("New password");
        newPasswordEntryButton.setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-font-weight: bold;");
//        newPasswordEntryButton.setPrefWidth(Double.MAX_VALUE);
        newPasswordEntryButton.setMaxWidth(Double.MAX_VALUE);
//        newPasswordEntryButton.setMinWidth(100);
        newPasswordEntryButton.setPrefWidth(Control.USE_COMPUTED_SIZE);
        newPasswordEntryButton.setOnMouseClicked(this::openCreatePasswordEntryViewOnClick);
        newPasswordEntryButton.setMaxWidth(300);
        newPasswordEntryButton.setMinWidth(300);
        newPasswordEntryButton.setMinHeight(40);
        newPasswordEntryButton.setMaxHeight(40);

        Button changeMasterPasswordButton = new Button("Change master password");
        changeMasterPasswordButton.setStyle("-fx-text-fill: white; -fx-background-color: black; -fx-font-weight: bold;");
//        changeMasterPasswordButton.setMaxWidth(Double.MAX_VALUE);
        changeMasterPasswordButton.setPrefWidth(Control.USE_COMPUTED_SIZE);
        changeMasterPasswordButton.setMaxWidth(300);
        changeMasterPasswordButton.setMinWidth(300);
        changeMasterPasswordButton.setMinHeight(40);
        changeMasterPasswordButton.setMaxHeight(40);

        bottomControls.setMinHeight(40);
        bottomControls.setSpacing(50);
        bottomControls.getChildren().addAll(newPasswordEntryButton, changeMasterPasswordButton);
//        HBox.setHgrow(newPasswordEntryButton, javafx.scene.layout.Priority.ALWAYS);
//        HBox.setHgrow(changeMasterPasswordButton, javafx.scene.layout.Priority.ALWAYS);
//        bottomControls.setStyle("-fx-background-color: blue;");


    }

    private void deletePasswordEntryOnClick(MouseEvent event) {
        Button btnDelete = (Button) event.getSource();
        Parent rowContainer = btnDelete.getParent().getParent();

        ObservableList<Node> childrenUnmodifiable = rowContainer.getChildrenUnmodifiable();
        Node serviceNameNode = childrenUnmodifiable.get(1);
        Label serviceLabel = (Label) serviceNameNode;

        model.deletePasswordEntry(serviceLabel.getText());
        populatePasswordEntriesList();
    }

    private void openCreatePasswordEntryViewOnClick(MouseEvent event) {
        // todo implement creating new password entry, including crypto + view
        this.sceneManager.switchScene(Main.SceneManager.Scenes.CREATE_PASSWORD_ENTRY);
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public Label prepareOffsetLabel(String serviceName) {
        int maxLabelLength = 15;
        Label offsetLabel = new Label();
        offsetLabel.setFont(new Font("Courier New", 35));
        offsetLabel.setId("serviceName");
        int whiteCharactersOffsetLength = maxLabelLength - serviceName.length();
        StringBuilder whiteCharactersOffset = new StringBuilder();
        whiteCharactersOffset.append(" ".repeat(whiteCharactersOffsetLength));
        offsetLabel.setText(whiteCharactersOffset.toString());
        return offsetLabel;
    }

    public Label preparePasswordPlaceholder() {
        int maxLabelLength = 15;
        Label passwordLabel = new Label();
        passwordLabel.setFont(new Font("Courier New", 35));
        Random rand = new Random();
        int passwordPlaceholderLength = rand.nextInt(4, 13);
        int whiteCharactersOffsetLength = maxLabelLength - passwordPlaceholderLength;

        passwordLabel.setText("☠︎".repeat(passwordPlaceholderLength) + " ".repeat(whiteCharactersOffsetLength));
        return passwordLabel;
    }

    public Region getInvisibleRegionSeparator() {
        Region separator = new Region();
        separator.setPrefWidth(20);
        separator.setMinHeight(0);
        separator.setMaxHeight(Double.MAX_VALUE);
        separator.setStyle("-fx-background-color: transparent;");
        return separator;
    }
}