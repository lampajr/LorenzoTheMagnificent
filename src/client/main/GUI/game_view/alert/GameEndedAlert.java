package client.main.GUI.game_view.alert;

import client.main.GUI.game_view.GUIController;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.StageStyle;

import java.util.Map;

/**
 * @author Andrea
 * @author Luca
 */
public class GameEndedAlert extends Alert{
    private String msg;
    private GUIController guiController;
    private double xOffset;
    private double yOffset;

    public GameEndedAlert(String msg, GUIController guiController) {
        super(AlertType.NONE);
        this.msg = msg;
        this.guiController = guiController;
        createAlert();
    }

    public GameEndedAlert(String msg, GUIController guiController, Map<String,Integer> rankingMap) {
        super(AlertType.NONE);
        this.msg = msg;
        this.guiController = guiController;
        createRankingAlert(rankingMap);
    }

    private void createRankingAlert(Map<String,Integer> rankingMap) {
        ListView<HBox> rankingListView = new ListView<>();
        rankingListView.setPrefHeight(150);
        rankingListView.setEditable(false);
        rankingMap.forEach((name, points) -> {
            Label usernameLabel = new Label(name);
            Label pointsLabel = new Label(points+"");
            HBox hBox = new HBox(usernameLabel, pointsLabel);
            hBox.setSpacing(50);
            rankingListView.getItems().add(hBox);
        });
        Text contentText = new Text(msg);
        contentText.setTextAlignment(TextAlignment.CENTER);
        VBox vBox = new VBox(contentText, rankingListView);
        DialogPane pane = (DialogPane) getDialogPane();
        initialize(pane);
        pane.setContent(vBox);

        getButtonTypes().addAll(ButtonType.FINISH);
        showAndWait().ifPresent((buttonType -> {
            if (buttonType == ButtonType.FINISH)
                guiController.backToMenu();
        }));
    }

    private void createAlert() {
        setContentText(msg);
        DialogPane pane = (DialogPane) getDialogPane();
        initialize(pane);

        getButtonTypes().addAll(ButtonType.OK);
        showAndWait().ifPresent((buttonType -> {
            if (buttonType == ButtonType.OK)
                guiController.backToMenu();
        }));
    }

    private void initialize(DialogPane pane) {
        setTitle("Game Ended Alert");
        setHeaderText("this game is over");
        initStyle(StageStyle.UNDECORATED);
        setResizable(false);

        pane.getStylesheets().add(getClass().getResource("alertStyle.css").toExternalForm());
        pane.setId("alert");
        //drag and drop
        pane.setCursor(Cursor.CLOSED_HAND);
        pane.setOnMousePressed(event -> {
            xOffset = getX() -event.getScreenX();
            yOffset = getY() -event.getScreenY();
            pane.setCursor(Cursor.CLOSED_HAND);
        } );
        pane.setOnMouseDragged(event -> {
            setX(event.getScreenX() + xOffset);
            setY(event.getScreenY() + yOffset);
        });

    }
}
