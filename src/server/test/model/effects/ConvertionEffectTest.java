package server.test.model.effects;

import org.junit.Before;
import org.junit.Test;
import server.main_server.game_server.exceptions.NewActionException;
import server.main_server.game_server.rmi.PlayerRMI;
import server.main_server.model.effects.development_effects.ConvertionEffect;
import server.main_server.model.fields.Field;
import server.main_server.model.fields.Resource;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import static api.types.ResourceType.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Andrea
 * @author Luca
 */
public class ConvertionEffectTest {
    private ConvertionEffect effect;
    private ConvertionEffect effectError;
    private PlayerRMI player;

    @Before
    public void setup() throws RemoteException {
        List<Field> toDecrement = Arrays.asList(new Resource(-1, WOOD), new Resource(-4,COINS));
        List<Field> toIncrement = Arrays.asList(new Resource(1, STONE), new Resource(2,SERVANTS));
        effect = new ConvertionEffect(toIncrement, toDecrement);
        List<Field> toDecrement2 = Arrays.asList(new Resource(-4, WOOD), new Resource(-4,COINS));
        effectError = new ConvertionEffect(toIncrement, toDecrement2);
        player = new PlayerRMI("andrea");
        player.createPersonalBoard(1);
    }

    @Test
    public void active() throws NewActionException {
        effect.active(player);
        assertEquals("wood",1, player.getPersonalBoard().getQtaResources().get(WOOD).intValue());
        assertEquals("coins",1, player.getPersonalBoard().getQtaResources().get(COINS).intValue());
        assertEquals("stone",3, player.getPersonalBoard().getQtaResources().get(STONE).intValue());
        assertEquals("servants",5, player.getPersonalBoard().getQtaResources().get(SERVANTS).intValue());
    }

    @Test
    public void activeError() throws NewActionException {
        effectError.active(player);
        assertEquals("wood",2, player.getPersonalBoard().getQtaResources().get(WOOD).intValue());
        assertEquals("coins",5, player.getPersonalBoard().getQtaResources().get(COINS).intValue());
        assertEquals("stone",2, player.getPersonalBoard().getQtaResources().get(STONE).intValue());
        assertEquals("servants",3, player.getPersonalBoard().getQtaResources().get(SERVANTS).intValue());
    }

}