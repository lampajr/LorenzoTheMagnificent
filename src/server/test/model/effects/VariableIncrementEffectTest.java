package server.test.model.effects;

import api.types.CardType;
import api.types.ResourceType;
import org.junit.Before;
import org.junit.Test;
import server.main_server.game_server.rmi.PlayerRMI;
import server.main_server.model.board.DevelopmentCard;
import server.main_server.model.effects.development_effects.VariableIncrementEffect;
import server.main_server.model.fields.Resource;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;

/**
 * @author Andrea
 * @author Luca
 */
public class VariableIncrementEffectTest {
    private VariableIncrementEffect effect;
    private PlayerRMI player;

    @Before
    public void setup() throws RemoteException {
        effect = new VariableIncrementEffect(new Resource(1, ResourceType.WOOD), CardType.TERRITORY);
        player = new PlayerRMI("andrea");
        player.createPersonalBoard(1);
        player.getPersonalBoard().addCard(new DevelopmentCard(CardType.TERRITORY, "valle", null, null, null, 1));
        player.getPersonalBoard().addCard(new DevelopmentCard(CardType.TERRITORY, "macchia", null, null, null, 1));
    }

    @Test
    public void active() {
        effect.active(player);
        assertEquals(4, player.getPersonalBoard().getQtaResources().get(ResourceType.WOOD).intValue());
    }

}