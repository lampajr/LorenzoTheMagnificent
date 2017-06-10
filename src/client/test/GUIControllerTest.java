package client.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Andrea
 * @author Luca
 */
public class GUIControllerTest extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("client/main/GUI/game_view/game_view.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
