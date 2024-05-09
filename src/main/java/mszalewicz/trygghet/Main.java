package mszalewicz.trygghet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mszalewicz.trygghet.Controllers.MainController;
import mszalewicz.trygghet.Controllers.MasterPasswordInsertController;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;

//import static jdk.xml.internal.SecuritySupport.getClassLoader;

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
//            Scene newScene = getScene(enumScene);
//            newScen
            stage.setScene(getScene(enumScene));
            stage.show();
            stage.centerOnScreen();
            stage.setResizable(false);
        }

        public void setStageMaxWidth(double newMaxWidth) {
            this.stage.setMaxWidth(newMaxWidth);
        }

    }

    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {

        //todo add icon to maven pom.xml


//        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Alessio");
        System.setProperty("apple.awt.application.name", "Trygghet");
        this.stage = stage;

        // Set application icon on the window bar
        this.stage.getIcons().add(new Image(Main.class.getClassLoader().getResourceAsStream("mszalewicz/trygghet/static/trygghet_main.png")));





//        this.stage.getIcons().add(new Image(<yourclassname>.class.getClassLoader().getResourceAsStream("static/trygghet_main.png")));
        Settings settings = new Settings("./settings.toml");



        //Set icon on the taskbar/dock
        if (Taskbar.isTaskbarSupported()) {
            var taskbar = Taskbar.getTaskbar();

            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                var dockIcon = defaultToolkit.getImage(Main.class.getClassLoader().getResource("mszalewicz/trygghet/static/trygghet_main4.png"));
                taskbar.setIconImage(dockIcon);
            }
        }









        File passwordsFile = new File("./passwords");
//        InputStream tmp = Main.class.getClassLoader().getResourceAsStream("mszalewicz/trygghet/static/trygghet_main.png");
        SceneManager sceneManager = new SceneManager(this.stage);

        var currentWrkDir = System.getProperty("user.dir");

        // Currently using db local to the program itself - to be changed to conform to given os specification where app files should be stored
        // DB db = new DB(settings.entries.dbFilePath);
        DB db = new DB(currentWrkDir + "/application.sqlite");

        ManagerModel model = new ManagerModel(db);
        model.checkIfNewDBAndPopulate();

        initScenes(model, sceneManager);

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

    protected void initScenes(ManagerModel model, SceneManager sceneManager) throws IOException {
        var initialScale = 0.7;

        double mainSceneWidth = 1101;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
        MainController mainController = new MainController(model, sceneManager);
        fxmlLoader.setController(mainController);
        Parent root = fxmlLoader.load();
//        MainController mainController = fxmlLoader.getController();
//        mainController.setDb(db);
//        mainController.setSceneManager(sceneManager);
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        Scene mainScene = new Scene(root, mainSceneWidth, screenBounds.getMaxY() * initialScale);



        sceneManager.setScene(SceneManager.Scenes.MAIN, mainScene);

        fxmlLoader = new FXMLLoader(Main.class.getResource("master-password-insert-first.fxml"));
        MasterPasswordInsertController masterPasswordInsertController = new MasterPasswordInsertController(model, sceneManager);
        fxmlLoader.setController(masterPasswordInsertController);
        root = fxmlLoader.load();
        screenBounds = Screen.getPrimary().getBounds();
        Scene masterPasswordInsertFirstScene = new Scene(root, 503,481);

        sceneManager.setScene(SceneManager.Scenes.MASTER_PASSWORD_INSERT_FIRST, masterPasswordInsertFirstScene);
    }

    public static void main(String[] args) {
        launch();
    }
}