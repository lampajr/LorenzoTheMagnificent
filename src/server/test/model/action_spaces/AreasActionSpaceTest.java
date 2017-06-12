package server.test.model.action_spaces;

import org.junit.Before;
import org.junit.Test;
import server.main_server.game_server.exceptions.LorenzoException;
import server.main_server.game_server.exceptions.NewActionException;
import server.main_server.game_server.rmi.PlayerRMI;
import server.main_server.model.action_spaces.Action;
import server.main_server.model.action_spaces.single_action_spaces.HarvestActionSpace;
import server.main_server.model.action_spaces.single_action_spaces.ProductionActionSpace;

import java.rmi.RemoteException;

import static api.types.FamilyMemberType.ORANGE_DICE;
import static api.types.ResourceType.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Andrea
 * @author Luca
 */
public class AreasActionSpaceTest {
    private HarvestActionSpace harvestActionSpace;
    private ProductionActionSpace productionActionSpace;
    private PlayerRMI player;

    @Before
    public void setUpPlayer() throws RemoteException {
        player = new PlayerRMI("luca");
        player.createPersonalBoard(1);
    }

    @Before
    public void setUp() throws Exception {
        harvestActionSpace = new HarvestActionSpace();
        productionActionSpace = new ProductionActionSpace();
    }

    @Test
    public void harvestDoAction() throws LorenzoException, NewActionException {
        harvestActionSpace.doAction(new Action(null, 4, player.getFamilyMember(ORANGE_DICE), player));
        assertEquals(3, player.getPersonalBoard().getQtaResources().get(WOOD).intValue());
        assertEquals(3, player.getPersonalBoard().getQtaResources().get(STONE).intValue());
        assertEquals(4, player.getPersonalBoard().getQtaResources().get(SERVANTS).intValue());
    }

    @Test
    public void productionDoAction() throws LorenzoException, NewActionException {
        productionActionSpace.doAction(new Action(null, 4, player.getFamilyMember(ORANGE_DICE), player));
        assertEquals(1, player.getPersonalBoard().getQtaResources().get(MILITARY).intValue());
        assertEquals(6, player.getPersonalBoard().getQtaResources().get(COINS).intValue());
    }
}