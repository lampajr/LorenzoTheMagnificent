package server.test.game_server;

import client.main_client.client.ClientRMI;
import org.junit.BeforeClass;
import org.junit.Test;
import server.main_server.MainServer;
import server.main_server.game_server.rmi.ServerRMI;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import static org.junit.Assert.*;

/**
 * @author Andrea
 * @author Luca
 */
public class ServerRMITest {
    private static ClientRMI client;
    private static ClientRMI client2;

    @BeforeClass
    public static void setupClass() throws AlreadyBoundException, RemoteException {
        ServerRMI serverRMI = new ServerRMI();
        LocateRegistry.createRegistry(1099).bind("serverRMI", serverRMI);
        client = new ClientRMI("andrea", "lol");
        client2 = new ClientRMI("andrea", "wrvsdc");
    }

    @Test
    public void startGame() throws  NotBoundException {
        client.login();
        assertNull(MainServer.twoPlayerGamesMap.get(MainServer.counter2Players-1));
        client.startGame(2);
        assertNotNull(MainServer.twoPlayerGamesMap.get(MainServer.counter2Players-1));
    }

    @Test
    public void loginOk() throws  NotBoundException {
        assertTrue(client.login());
        assertEquals("lol", MainServer.playersMap.get("andrea"));
    }

    @Test
    public void loginError() throws NotBoundException {
        client.login();
        assertFalse(client2.login());
    }
}