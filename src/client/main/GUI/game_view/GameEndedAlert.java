package client.main.GUI.game_view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.util.Map;

/**
 * @author Andrea
 * @author Luca
 */
class GameEndedAlert extends Alert{
    private String msg;
    private GUIController guiController;

    GameEndedAlert(String msg, GUIController guiController) {
        super(AlertType.NONE);
        this.msg = msg;
        this.guiController = guiController;
        createAlert();
    }

    GameEndedAlert(String msg, GUIController guiController, Map<String,Integer> rankingMap) {
        super(AlertType.NONE);
        this.msg = msg;
        this.guiController = guiController;
        createRankingAlert(rankingMap);
    }

    private void createRankingAlert(Map<String,Integer> rankingMap) {
        setTitle("Game Ended Alert");
        setHeaderText("this game is over");
        setContentText(msg);
        ListView<HBox> rankingListView = new ListView<>();
        rankingMap.forEach((name, points) -> {
            Label usernameLabel = new Label(name);
            Label pointsLabel = new Label(points+"");
            HBox hBox = new HBox(usernameLabel, pointsLabel);
            hBox.setSpacing(50);
            rankingListView.getItems().add(hBox);
        });
        getDialogPane().setContent(rankingListView);
        getButtonTypes().addAll(ButtonType.OK);
        showAndWait().ifPresent((buttonType -> {
            if (buttonType == ButtonType.OK)
                guiController.backToMenu();
        }));
    }

    private void createAlert() {
        setTitle("Game Ended Alert");
        setHeaderText("this game is over");
        setContentText(msg);
        getButtonTypes().addAll(ButtonType.OK);
        showAndWait().ifPresent((buttonType -> {
            if (buttonType == ButtonType.OK)
                guiController.backToMenu();
        }));
    }
}
