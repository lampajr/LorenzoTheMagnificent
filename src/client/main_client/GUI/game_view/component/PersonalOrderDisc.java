package client.main_client.GUI.game_view.component;

import javafx.scene.shape.Circle;
import client.main_client.GUI.Service;

/**
 * @author Luca
 * @author Andrea
 */
public class PersonalOrderDisc extends Circle{
    private static final int RADIUS = 10;
    private int id;

    public PersonalOrderDisc(int id) {
        super(RADIUS, Service.getColorById(id));
        this.id = id;
    }
}
