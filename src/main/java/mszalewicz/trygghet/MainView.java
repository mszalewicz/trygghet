package mszalewicz.trygghet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainView extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Settings settings = new Settings("./settings.toml");
        File passwordsFile = new File("./passwords");


        var currentWrkDir = System.getProperty("user.dir");

        // Currently using db local to the program itself - to be channged to conform to given os specification where app files should be stored
        // DB db = new DB(settings.entries.dbFilePath);
        DB db = new DB(currentWrkDir + "/application.sqlite");

        // TODO Read passwords

        var initialScale = 0.7;
        FXMLLoader fxmlLoader = new FXMLLoader(MainView.class.getResource("hello-view.fxml"));
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        Scene scene = new Scene(fxmlLoader.load(), screenBounds.getMaxX() * initialScale, screenBounds.getMaxY() * initialScale);
        stage.setTitle("Trygghet");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}