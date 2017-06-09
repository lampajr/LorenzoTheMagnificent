package client.main.GUI.game_view.alert;

import client.main.client.AbstractClient;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.StageStyle;

/**
 * @author Luca
 * @author Andrea
 */
public class ExcommunicationAlert extends Alert{
    private AbstractClient client;
    private double xOffset;
    private double yOffset;

    public ExcommunicationAlert() {
        super(AlertType.NONE);
        client = AbstractClient.getInstance();
        createAlert();
    }

    private void createAlert() {
        setTitle("Excommunication Alert");
        setHeaderText("It is the excommunicating turn, what do you want to do?");
        setContentText("Choose your option.");
        initStyle(StageStyle.UNDECORATED);

        DialogPane pane = getDialogPane();
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


        ButtonType excommunicationButton = new ButtonType("Excommunicate");
        ButtonType giveSupportButton = new ButtonType("Give Support");

        getButtonTypes().setAll(excommunicationButton, giveSupportButton);
        showAndWait().ifPresent(response -> {
            if (response == excommunicationButton) {
                client.excommunicationChoice(true);

            }
            else {
                client.excommunicationChoice(false);
            }
        });
    }
}
