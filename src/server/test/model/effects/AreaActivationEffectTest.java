package server.test.model.effects;

import org.junit.Before;
import org.junit.Test;
import server.main.game_server.exceptions.NewActionException;
import server.main.game_server.rmi.PlayerRMI;
import server.main.model.action_spaces.Action;
import server.main.model.effects.development_effects.AreaActivationEffect;
import server.main.model.effects.development_effects.FixedIncrementEffect;
import server.main.model.fields.Resource;

import java.rmi.RemoteException;

import static api.types.ResourceType.WOOD;
import static org.junit.Assert.assertEquals;

/**
 * @author Andrea
 * @author Luca
 */
public class AreaActivationEffectTest {
    private AreaActivationEffect areaEffect;
    private PlayerRMI player;

    @Before
    public void setup() throws RemoteException {
        FixedIncrementEffect effect = new FixedIncrementEffect(new Resource(5, WOOD));
        areaEffect = new AreaActivationEffect(effect, 4);
        player = new PlayerRMI("andrea");
        player.createPersonalBoard(1);
    }

    @Test
    public void active() throws NewActionException {
        player.getPersonalBoard().setCurrentAction(new Action(null, 5, null, player));
        areaEffect.active(player);
        assertEquals(7, player.getPersonalBoard().getQtaResources().get(WOOD).intValue());
    }

    @Test
    public void activeError() throws NewActionException {
        player.getPersonalBoard().setCurrentAction(new Action(null, 3, null, player));
        areaEffect.active(player);
        assertEquals(2, player.getPersonalBoard().getQtaResources().get(WOOD).intValue());
    }

}