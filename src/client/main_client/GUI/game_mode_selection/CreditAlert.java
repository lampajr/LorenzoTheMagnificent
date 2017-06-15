package client.main_client.GUI.game_mode_selection;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;

import java.awt.*;

/**
 * @author Andrea
 * @author Luca
 */
class CreditAlert extends Alert {
    private double WIDTH = 320, HEGHT = 155;
    ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);


    CreditAlert() {
        super(AlertType.NONE);
        this.setOnCloseRequest(e -> this.close());
        initializeAlert();
    }

    private void initializeAlert() {
        DialogPane pane = getDialogPane();
        pane.getStylesheets().add(getClass().getResource("res/style.css").toExternalForm());
        pane.setId("privilegeAlert");
        pane.setPrefSize(WIDTH, HEGHT);
        Text text1 = new Text("AUTHORS : (In alphabetic order, no discrimination ;) ");
        Text text2 = new Text("BONALI LUCA");
        Text text3 = new Text("LAMPARELLI ANDREA");
        Text text4 = new Text("And that' s it, there's no other Credits.");
        text1.setFont(Font.font("sans-serif",FontWeight.BOLD,11));
        text2.setFont(Font.font("sans-serif",FontWeight.BOLD,11));
        text3.setFont(Font.font("sans-serif",FontWeight.BOLD,11));
        text4.setFont(Font.font("sans-serif",FontWeight.BOLD,11));

        getDialogPane().setContent(new VBox(text1,text2,text3,text4));
        getButtonTypes().setAll(okButton);
        showAndWait().ifPresent(response -> {
               if (response == okButton) {
                    this.close();
               }
        });

    }


}
