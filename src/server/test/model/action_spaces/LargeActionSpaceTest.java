package server.test.model.action_spaces;

import org.junit.Before;
import org.junit.Test;
import server.main_server.game_server.exceptions.LorenzoException;
import server.main_server.game_server.exceptions.NewActionException;
import server.main_server.game_server.rmi.PlayerRMI;
import server.main_server.model.action_spaces.Action;
import server.main_server.model.action_spaces.large_action_spaces.CouncilActionSpace;
import server.main_server.model.action_spaces.large_action_spaces.LargeHarvestActionSpace;
import server.main_server.model.action_spaces.large_action_spaces.LargeProductionActionSpace;

import java.rmi.RemoteException;

import static api.types.FamilyMemberType.ORANGE_DICE;
import static api.types.FamilyMemberType.WHITE_DICE;
import static api.types.ResourceType.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Andrea
 * @author Luca
 */
public class LargeActionSpaceTest {
    private LargeHarvestActionSpace largeHarvestActionSpace;
    private LargeProductionActionSpace largeProductionActionSpace;
    private CouncilActionSpace councilActionSpace;
    private PlayerRMI player;
    private Action action3;
    private Action action4;

    @Before
    public void setUpPlayer() throws RemoteException {
        player = new PlayerRMI("luca");
        player.createPersonalBoard(1);
        action3 = new Action(null, 3, player.getFamilyMember(ORANGE_DICE), player);
        action4 = new Action(null, 4, player.getFamilyMember(WHITE_DICE), player);
    }

    @Before
    public void setUp() {
        largeHarvestActionSpace = new LargeHarvestActionSpace();
        largeProductionActionSpace = new LargeProductionActionSpace();
        councilActionSpace = new CouncilActionSpace();
    }

    @Test
    public void harvestDoAction() throws LorenzoException, NewActionException {
        largeHarvestActionSpace.doAction(action4);
        assertEquals(3, player.getPersonalBoard().getQtaResources().get(WOOD).intValue());
        assertEquals(3, player.getPersonalBoard().getQtaResources().get(STONE).intValue());
        assertEquals(4, player.getPersonalBoard().getQtaResources().get(SERVANTS).intValue());
    }

    @Test
    public void productionDoAction() throws LorenzoException, NewActionException {
        largeProductionActionSpace.doAction(action4);
        assertEquals(1, player.getPersonalBoard().getQtaResources().get(MILITARY).intValue());
        assertEquals(6, player.getPersonalBoard().getQtaResources().get(COINS).intValue());
    }

    @Test
    public void councilDoAction() throws LorenzoException, NewActionException {
        councilActionSpace.doAction(action4);
        assertEquals(6, player.getPersonalBoard().getQtaResources().get(COINS).intValue());
    }

    @Test(expected = LorenzoException.class)
    public void errorAreaDoAction() throws LorenzoException, NewActionException {
        largeProductionActionSpace.doAction(action3);
    }
}