package server.test.model;

import api.types.FamilyMemberType;
import api.types.Phases;
import client.main.client.ClientRMI;
import org.junit.Before;
import org.junit.Test;
import server.main.game_server.exceptions.LorenzoException;
import server.main.game_server.rmi.PlayerRMI;
import server.main.model.Game;
import server.main.model.fields.Resource;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static api.types.ResourceType.FAITH;
import static api.types.ResourceType.VICTORY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * @author Andrea
 * @author Luca
 */
public class GameTest {
    private PlayerRMI player1;
    private PlayerRMI player2;
    private Game game;

    @Before
    public void setup() throws RemoteException, AlreadyBoundException, NotBoundException, InterruptedException {
        game = new Game(2);
        ClientRMI client1 = new ClientRMI("lampa", "lol");
        ClientRMI client2 = new ClientRMI("luca", "boss");
        player1 = new PlayerRMI("andrea");
        player1.addClientInterface(client1);
        player2 = new PlayerRMI("luca");
        player2.addClientInterface(client2);
        game.addPlayer(player1);
        game.addPlayer(player2);
        Thread.sleep(4000);
    }

    @Test
    public void getCurrentPlayer() {
        assertSame(player1, game.getCurrentPlayer());
    }

    @Test
    public void isFull() {
        assertTrue(game.isFull());
    }

    @Test
    public void shotDice() {
        game.shotDice(player1, 3, 4, 5);
        assertEquals(5, player1.getPersonalBoard().getFamilyMember(FamilyMemberType.BLACK_DICE).getValue());
        assertEquals(3, player2.getPersonalBoard().getFamilyMember(FamilyMemberType.ORANGE_DICE).getValue());
    }

    @Test
    public void getId() {
        assertEquals(1, game.getId(player1));
    }

    @Test(expected = LorenzoException.class)
    public void checkTurn() throws LorenzoException {
        game.checkTurn(player2);
    }

    @Test
    public void excommunicatePlayer() {
        game.setPhase(Phases.EXCOMMUNICATION);
        player1.getPersonalBoard().modifyResources(new Resource(2, FAITH));
        game.giveChurchSupport(player1); //mi chiama il metodo excommunicatePlayer
        assertEquals(0, player1.getPersonalBoard().getQtaResources().get(VICTORY).intValue());
        assertEquals(2, player1.getPersonalBoard().getQtaResources().get(FAITH).intValue());
    }

    @Test
    public void giveChurchSupport() throws Exception {
        game.setPhase(Phases.EXCOMMUNICATION);
        player1.getPersonalBoard().modifyResources(new Resource(5, FAITH));
        game.giveChurchSupport(player1);
        assertEquals(0, player1.getPersonalBoard().getQtaResources().get(FAITH).intValue());
        assertEquals(5, player1.getPersonalBoard().getQtaResources().get(VICTORY).intValue());
    }

    @Test
    public void doAction() {

    }

    @Test
    public void doNewAction() {
    }

    @Test
    public void endMove1() {
        game.setLap(4);
        game.setCurrentPlayer(player2);
        game.endMove(player2);
        assertEquals(2, game.getTurn());
    }

    @Test
    public void endMove2() {
        assertEquals(Phases.ACTION, game.getPhase());
        game.setLap(4);
        game.setCurrentPlayer(player2);
        game.setTurn(2);
        game.endMove(player2);
        assertEquals(3, game.getTurn());
        assertEquals(Phases.EXCOMMUNICATION, game.getPhase());
    }

    @Test
    public void endMove3() {
        game.setLap(4);
        game.setCurrentPlayer(player2);
        game.endMove(player2);
        assertEquals(2, game.getTurn());
    }

    @Test
    public void endMove4() {
        game.setLap(4);
        game.setCurrentPlayer(player2);
        game.endMove(player2);
        assertEquals(2, game.getTurn());
    }

    @Test
    public void removePlayer() {
        assertEquals(2, game.getTurnOrder().size());
        game.removePlayer(player2);
        assertEquals(1, game.getTurnOrder().size());
    }

}