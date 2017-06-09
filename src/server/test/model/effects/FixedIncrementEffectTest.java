package server.test.model.effects;

import org.junit.Before;
import org.junit.Test;
import server.main.game_server.rmi.PlayerRMI;
import server.main.model.effects.development_effects.FixedIncrementEffect;
import server.main.model.fields.Resource;

import java.rmi.RemoteException;

import static api.types.ResourceType.WOOD;
import static org.junit.Assert.assertEquals;

/**
 * @author Andrea
 * @author Luca
 */
public class FixedIncrementEffectTest {
    private FixedIncrementEffect effect;
    private PlayerRMI player;

    @Before
    public void setup() throws RemoteException {
        effect = new FixedIncrementEffect(new Resource(5, WOOD));
        player = new PlayerRMI("andrea");
        player.createPersonalBoard(1);
    }

    @Test
    public void active() {
        effect.active(player);
        assertEquals("wood",7, player.getPersonalBoard().getQtaResources().get(WOOD).intValue());
    }
}