package client.main_client.GUI.game_view.component.action_spaces;

import api.types.ActionSpacesType;
import client.main_client.GUI.game_view.GUIController;
import client.main_client.GUI.game_view.component.GuiFamilyMember;
import client.main_client.client.AbstractClient;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Luca
 * @author Andrea
 */
public class LargeActionSpace extends Pane implements ActionSpaceInterface{
    private static final int WIDTH = 185, HEIGHT = 39;
    private int counter;
    private List<GuiFamilyMember> familyMemberList;
    private ActionSpacesType type;
    private Rectangle rectangle;
    private HBox container;
    private GUIController guiController;

    public LargeActionSpace(ActionSpacesType type, HBox container, GUIController guiController) {
        this.type = type;
        this.container = container;
        this.guiController = guiController;
        familyMemberList = new ArrayList<>();
        setMaxSize(WIDTH, HEIGHT);
        setPrefSize(WIDTH, HEIGHT);
        setCursor(Cursor.HAND);
        setOnMouseClicked(event -> setCurrentActionSpace());
    }

    List<GuiFamilyMember> getFamilyMemberList() {
        return familyMemberList;
    }

    void setFamilyMemberList() {
        familyMemberList = new ArrayList<>();
    }

    public HBox getContainer() {
        return container;
    }

    public void setFamilyMember(GuiFamilyMember familyMember) {
        familyMemberList.add(familyMember);
    }

    @Override
    public void addFamilyMember(GuiFamilyMember familyMember) {
        familyMemberList.add(familyMember);
        familyMember.setTranslateX(25 + (25*counter));
        Platform.runLater(() -> container.getChildren().add(familyMember));
        counter++;
    }

    @Override
    public void removeAllFamilyMembers() {
        Platform.runLater(() -> {
            for (int i=0; i<familyMemberList.size(); i++) {
                GuiFamilyMember familyMember = familyMemberList.get(i);
                familyMember.setTranslateX(-25 - (25*i));
                getContainer().getChildren().remove(familyMember);
            }
            familyMemberList = new ArrayList<>();
        });
    }

    @Override
    public ActionSpacesType getType() {
        return type;
    }

    @Override
    public void setCurrentActionSpace() {
        AbstractClient.getInstance().setActionSpacesType(type);
        AbstractClient.getInstance().encodingAndSendingMessage(guiController.getServantsToPay());
    }

    @Override
    public void removeFamilyMember(GuiFamilyMember familyMemberToRemove) {
        if (container.getChildren().contains(familyMemberToRemove))
            Platform.runLater(() -> container.getChildren().remove(familyMemberToRemove));
    }
}
