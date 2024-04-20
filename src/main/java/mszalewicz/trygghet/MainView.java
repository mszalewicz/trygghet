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

        try {
            if (passwordsFile.exists()) {
                System.out.println("opened file \"" + passwordsFile.getName() + "\"");
            } else {
                if (passwordsFile.createNewFile()) {
                    System.out.println("created file:\"" + passwordsFile.getName() + "\"");
                } else {
                    System.err.println("could not create file: \"" + passwordsFile.getName() + "\"");
                }
            }
        } catch (IOException e) {
            System.err.println("could not open/create file:\"" + passwordsFile.getName() + "\"");
            e.printStackTrace();
            System.exit(1);
        }

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