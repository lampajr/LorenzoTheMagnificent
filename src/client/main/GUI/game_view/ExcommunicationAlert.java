package client.main.GUI.game_view;

import client.main.client.AbstractClient;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * @author Luca
 * @author Andrea
 */
class ExcommunicationAlert extends Alert{
    private AbstractClient client;

    ExcommunicationAlert() {
        super(AlertType.NONE);
        client = AbstractClient.getInstance();
        createAlert();
    }

    private void createAlert() {
        setTitle("Excommunication Alert");
        setHeaderText("It is the excommunicating turn, what do you want to do?");
        setContentText("Choose your option.");

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
