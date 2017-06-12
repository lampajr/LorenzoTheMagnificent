package client.main_client.GUI.start_screen;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import client.main_client.music.Music;
import client.main_client.GUILauncher;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartScreenController implements Initializable {
    @FXML private Button closeButton;
    @FXML private Label labelClick;
    @FXML private ImageView testa;
    @FXML private Image testina;
    @FXML private Pane rootPane;
    private double xOffset,yOffset;

    private Music audio;



    ScaleTransition scaleTransition ;
    FadeTransition fadeOut, fadeIn;

    Image image;

    public void startTransition(MouseEvent mouseEvent) {
        scaleTransition.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scaleTransition = new ScaleTransition(Duration.millis(1500),testa);
        scaleTransition.setToY(2);
        scaleTransition.setToX(2);
        scaleTransition.autoReverseProperty();
        scaleTransition.setOnFinished(e -> {
            audio.play(audio.getPath() + "Occhiolino.wav");
            changePhoto();
        });
        image = new Image(getClass().getResourceAsStream("res/LorenzoIlMagnificochiuso.png"));
        fadeIn = new FadeTransition(Duration.millis(500),labelClick);
        fadeOut = new FadeTransition(Duration.millis(500),labelClick);
        fadeOut.setFromValue(0.5);
        fadeOut.setToValue(1.0);
        fadeIn.setFromValue(0.5);
        fadeOut.setToValue(1.0);
        fadeOut.play();
        fadeIn.setOnFinished( e -> fadeOut.play());
        fadeOut.setOnFinished(e -> fadeIn.play());

        rootPane.setOnMousePressed(event -> {
            xOffset = GUILauncher.getPrimaryStage().getX() -event.getScreenX();
            yOffset = GUILauncher.getPrimaryStage().getY() -event.getScreenY();
            rootPane.setCursor(Cursor.CLOSED_HAND);
        } );

        rootPane.setOnMouseDragged(event -> {
            GUILauncher.getPrimaryStage().setX(event.getScreenX() + xOffset);
            GUILauncher.getPrimaryStage().setY(event.getScreenY() + yOffset);
        });
        rootPane.setOnMouseReleased(event -> rootPane.setCursor(Cursor.DEFAULT));

        playAudio();

    }

    private void playAudio() {
        audio = new Music();
    }


    private void changePhoto() {
        testa.setImage(image);
        changeScene();
    }

    private void changeScene() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("client/main_client/GUI/login/login_view.fxml"));
        Platform.runLater(()->{
            Stage stage = (Stage)  labelClick.getScene().getWindow();
            try {
                Parent window = (Pane) fxmlLoader.load();
                Scene scene = new Scene(window);
                Thread.sleep(1000);
                stage.setScene(scene);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            stage.centerOnScreen();
            stage.setTitle("LORENZO IL MAGNIFICO!!");
        });
    }

    public void changeCursor(MouseEvent mouseEvent) {
        labelClick.setCursor(Cursor.HAND);
    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}