package mszalewicz.trygghet.Controllers;

import javafx.fxml.FXML;
import mszalewicz.trygghet.Main;
import mszalewicz.trygghet.ManagerModel;

public class InsertPasswordEntryController {

    private ManagerModel model;
    private Main.SceneManager sceneManager;
    private MainController mainController;

//    @FXML


    public InsertPasswordEntryController(ManagerModel model, Main.SceneManager sceneManager, MainController mainController) {
        this.model = model;
        this.sceneManager = sceneManager;
        this.mainController = mainController;
    }

    public void checkEntry() {

    }
}
