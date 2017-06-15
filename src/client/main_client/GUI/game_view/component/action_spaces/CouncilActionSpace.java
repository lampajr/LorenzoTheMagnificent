package client.main_client.GUI.game_view.component.action_spaces;

import api.types.ActionSpacesType;
import client.main_client.GUI.game_view.GUIController;
import javafx.scene.layout.HBox;

/**
 * @author lampa
 */
public class CouncilActionSpace extends LargeActionSpace{
    private static final int WIDTH = 240, HEIGHT = 55;
    private int counter;

    public CouncilActionSpace(ActionSpacesType type, HBox container, GUIController guiController) {
        super(type, container, guiController);
        setMaxSize(WIDTH, HEIGHT);
        setPrefSize(WIDTH, HEIGHT);
        setOnMouseClicked(event -> setCurrentActionSpace());
    }
}
