package server.test.model.effects;

import api.types.FamilyMemberType;
import org.junit.Before;
import org.junit.Test;
import server.main.game_server.rmi.PlayerRMI;
import server.main.model.action_spaces.Action;
import server.main.model.action_spaces.large_action_spaces.CouncilActionSpace;
import server.main.model.action_spaces.single_action_spaces.HarvestActionSpace;
import server.main.model.effects.development_effects.ActionValueModifyingEffect;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;

/**
 * @author Andrea
 * @author Luca
 */
public class ActionValueModifyingEffectTest {
    private ActionValueModifyingEffect effect;
    private Action action1;
    private Action action2;
    private PlayerRMI player;

    @Before
    public void setup() throws RemoteException {
        effect = new ActionValueModifyingEffect(new HarvestActionSpace(1), 2);
        player = new PlayerRMI("andrea");
        player.createPersonalBoard(1);
        action2 = new Action(new CouncilActionSpace(1), 2, player.getFamilyMember(FamilyMemberType.ORANGE_DICE), player);
        action1 = new Action(new HarvestActionSpace(1), 2, player.getFamilyMember(FamilyMemberType.ORANGE_DICE), player);
    }

    @Test
    public void active() {
        player.getPersonalBoard().setCurrentAction(action1);
        effect.active(player);
        assertEquals(4, player.getPersonalBoard().getCurrentAction().getValue());
    }

    @Test
    public void noActive() throws RemoteException{
        player.getPersonalBoard().setCurrentAction(action2);
        effect.active(player);
        assertEquals(2, player.getPersonalBoard().getCurrentAction().getValue());
    }
}