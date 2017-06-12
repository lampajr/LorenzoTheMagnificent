package client.main_client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


/**
 * @author Luca
 * @author Andrea
 */
public class GUILauncher extends Application {
    private static Stage primaryStage;
    /**
     * Metodo Main del client, che lancia il metodo start di questa classe
     * @param args eventuali argomenti passati in input
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Metodo che visualizza la schermata di login del gioco
     * @param stage finestra iniziale
     */

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("GUI/start_screen/start_screen.fxml"));
            stage.setScene(new Scene(root, 356, 542));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.centerOnScreen();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setTitle("Lorenzo The Magnificent");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("res/Icona_testa.png")));
        stage.show();

    }

   public static Stage getPrimaryStage(){
        return primaryStage;
    }


}
