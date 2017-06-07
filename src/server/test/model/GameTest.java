package server.test.model;

import api.types.FamilyMemberType;
import client.main.client.ClientRMI;
import org.junit.Before;
import org.junit.Test;
import server.main.game_server.rmi.PlayerRMI;
import server.main.model.Game;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;
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
    public void setupClass() throws RemoteException, AlreadyBoundException, NotBoundException, InterruptedException {
        game = new Game(2);
        ClientRMI client1 = new ClientRMI("lampa", "lol");
        ClientRMI client2 = new ClientRMI("luca", "boss");
        player1 = new PlayerRMI("andrea");
        player1.addClientInterface(client1);
        player2 = new PlayerRMI("luca");
        player2.addClientInterface(client2);
        game.addPlayer(player1);
        game.addPlayer(player2);
        Thread.sleep(3500);
    }

    @Before
    public void setup() {

    }

    @Test
    public void getCurrentPlayer() throws Exception {
    }

    @Test
    public void isFull() throws Exception {
        assertTrue(game.isFull());
    }

    @Test
    public void shotDice() throws Exception {
        game.shotDice(player1, 3, 4, 5);
        assertEquals(5, player1.getPersonalBoard().getFamilyMember(FamilyMemberType.BLACK_DICE).getValue());
        assertEquals(3, player2.getPersonalBoard().getFamilyMember(FamilyMemberType.ORANGE_DICE).getValue());
    }

    @Test
    public void getId() throws Exception {
    }

    @Test
    public void checkTurn() throws Exception {
    }

    @Test
    public void excommunicatePlayer() throws Exception {
    }

    @Test
    public void giveChurchSupport() throws Exception {
    }

    @Test
    public void activeFirstPeriodExcommunication() throws Exception {
    }

    @Test
    public void activeSecondPeriodExcommunication() throws Exception {
    }

    @Test
    public void activeThirdPeriodExcommunication() throws Exception {
    }

    @Test
    public void doAction() throws Exception {
    }

    @Test
    public void notifyAllPlayers() throws Exception {
    }

    @Test
    public void notifyAllPlayers1() throws Exception {
    }

    @Test
    public void doNewAction() throws Exception {
    }

    @Test
    public void endMove() throws Exception {
    }

    @Test
    public void removePlayer() throws Exception {
    }

}