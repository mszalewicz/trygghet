package mszalewicz.trygghet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mszalewicz.trygghet.Controllers.MainController;
import mszalewicz.trygghet.Controllers.MasterPasswordInsertController;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;

public class Main extends Application {


    public class SceneManager {

        private Stage stage;

        SceneManager(Stage stage) {
           this.stage = stage;
        }

        public enum Scenes {
            MAIN,
            MASTER_PASSWORD_INSERT_FIRST;
        }

        private EnumMap<Scenes, Scene> allScenes = new EnumMap<>(Scenes.class);

        public void getStageSize() {
            this.stage.getWidth();
            this.stage.getHeight();
        }

        public void setScene(Scenes enumScenes, Scene scene) {
            allScenes.put(enumScenes, scene);
        }

        public Scene getScene(Scenes enumScenes) {
            return allScenes.get(enumScenes);
        }

        public void switchScene(SceneManager.Scenes enumScene) {
            stage.setTitle("Trygghet");
            stage.setScene(getScene(enumScene));
            stage.show();
            stage.centerOnScreen();
        }

    }

    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        Settings settings = new Settings("./settings.toml");
        File passwordsFile = new File("./passwords");
        SceneManager sceneManager = new SceneManager(this.stage);

        var currentWrkDir = System.getProperty("user.dir");

        // Currently using db local to the program itself - to be changed to conform to given os specification where app files should be stored
        // DB db = new DB(settings.entries.dbFilePath);
        DB db = new DB(currentWrkDir + "/application.sqlite");
        initScenes(sceneManager, db);

        ManagerModel model = new ManagerModel(db);
        model.checkIfNewDBAndPopulate();

        if (model.masterPasswordExists()) {
            sceneManager.switchScene(SceneManager.Scenes.MAIN);
        } else {
            sceneManager.switchScene(SceneManager.Scenes.MASTER_PASSWORD_INSERT_FIRST);

        }


        CipherTransformations ct = new CipherTransformations((int) settings.entries.PBEIterationCount);

//        try {
//            SecretKey secretKey = ct.generateKey();
//            String key = ct.convertSecretKeyToString(secretKey);
//            System.out.println(key);
//            SecretKey keyDecoded = ct.convertStringToSecretKey(key);
//            String key2 = ct.convertSecretKeyToString(keyDecoded);
//            System.out.println(key2);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//


        // TODO Read passwords


    }

    protected void initScenes(SceneManager sceneManager, DB db) throws IOException {
        var initialScale = 0.7;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Parent root = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        mainController.setDb(db);
        mainController.setSceneManager(sceneManager);
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        Scene mainScene = new Scene(root, screenBounds.getMaxX() * initialScale, screenBounds.getMaxY() * initialScale);

        sceneManager.setScene(SceneManager.Scenes.MAIN, mainScene);

        fxmlLoader = new FXMLLoader(Main.class.getResource("master-password-insert-first.fxml"));
        root = fxmlLoader.load();
        MasterPasswordInsertController masterPasswordInsertController = fxmlLoader.getController();
        masterPasswordInsertController.setDb(db);
        masterPasswordInsertController.setSceneManager(sceneManager);
        screenBounds = Screen.getPrimary().getBounds();
        Scene masterPasswordInsertFirstScene = new Scene(root, 503,481);

        sceneManager.setScene(SceneManager.Scenes.MASTER_PASSWORD_INSERT_FIRST, masterPasswordInsertFirstScene);
    }

    public static void main(String[] args) {
        launch();
    }
}