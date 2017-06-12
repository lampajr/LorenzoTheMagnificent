package server.test.model.action_spaces;

import org.junit.Before;
import org.junit.Test;
import server.main.game_server.exceptions.LorenzoException;
import server.main.game_server.exceptions.NewActionException;
import server.main.game_server.rmi.PlayerRMI;
import server.main.model.action_spaces.Action;
import server.main.model.action_spaces.single_action_spaces.FloorActionSpace;
import server.main.model.action_spaces.single_action_spaces.MarketActionSpace;
import server.main.model.board.DevelopmentCard;
import server.main.model.board.Tower;
import server.main.model.fields.Resource;

import java.rmi.RemoteException;
import java.util.Collections;

import static api.types.CardType.BUILDING;
import static api.types.FamilyMemberType.BLACK_DICE;
import static api.types.FamilyMemberType.ORANGE_DICE;
import static api.types.MarketActionType.BLUE;
import static api.types.ResourceType.COINS;
import static api.types.ResourceType.MILITARY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * @author Andrea
 * @author Luca
 */
public class FloorActionSpaceTest {
    private FloorActionSpace floorActionSpace;
    private MarketActionSpace marketActionSpace;
    private PlayerRMI player;
    private DevelopmentCard card;

    @Before
    public void setupPlayer() throws RemoteException {
        player = new PlayerRMI("andrea");
        player.createPersonalBoard(1);
    }

    @Before
    public void setupActionSpace() {
        floorActionSpace = new FloorActionSpace(3, BUILDING, COINS, new Tower(BUILDING, COINS));
        card = new DevelopmentCard(BUILDING, "rocca", Collections.singletonList(new Resource(-2, COINS)), null, null, 1);
        marketActionSpace = new MarketActionSpace(BLUE);
    }

    @Test
    public void setGetDevelopmentCard() {
        floorActionSpace.setDevelopmentCard(card);
        assertSame(card, floorActionSpace.getDevelopmentCard());
    }

    @Test
    public void floorDoAction() throws LorenzoException, NewActionException {
        floorActionSpace.setDevelopmentCard(card);
        floorActionSpace.doAction(new Action(null, 3, player.getFamilyMember(ORANGE_DICE), player));
        assertEquals(3, player.getPersonalBoard().getQtaResources().get(COINS).intValue());
    }

    @Test(expected = LorenzoException.class)
    public void errorMemberDoAction() throws LorenzoException, NewActionException {
        floorActionSpace.setDevelopmentCard(card);
        floorActionSpace.doAction(new Action(null, 3, player.getFamilyMember(ORANGE_DICE), player));
        floorActionSpace.doAction(new Action(null, 3, player.getFamilyMember(BLACK_DICE), player));
    }

    @Test(expected = LorenzoException.class)
    public void errorValueDoAction() throws LorenzoException, NewActionException {
        floorActionSpace.setDevelopmentCard(card);
        floorActionSpace.doAction(new Action(null, 1, player.getFamilyMember(ORANGE_DICE), player));
    }

    @Test
    public void marketDoAction() throws LorenzoException, NewActionException {
        marketActionSpace.doAction(new Action(null, 3, player.getFamilyMember(ORANGE_DICE), player));
        assertEquals(7, player.getPersonalBoard().getQtaResources().get(COINS).intValue());
        assertEquals(3, player.getPersonalBoard().getQtaResources().get(MILITARY).intValue());
    }
}