package client.main.GUI.game_view.component.action_spaces;

import api.types.ActionSpacesType;
import client.main.GUI.game_view.GUIController;
import client.main.GUI.game_view.component.GuiFamilyMember;
import javafx.application.Platform;
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

    @Override
    public void addFamilyMember(GuiFamilyMember familyMember) {
        setFamilyMember(familyMember);
//        familyMember.setTranslateX(20 + (45*counter));
        Platform.runLater(() -> getContainer().getChildren().add(familyMember));
        counter++;
    }

    @Override
    public void removeAllFamilyMembers() {
        Platform.runLater(() -> {
            for (int i = 0; i < getFamilyMemberList().size(); i++) {
                GuiFamilyMember familyMember = getFamilyMemberList().get(i);
//                familyMember.setTranslateX(0);
                getContainer().getChildren().remove(familyMember);
            }
            setFamilyMemberList();
        });
    }

}
