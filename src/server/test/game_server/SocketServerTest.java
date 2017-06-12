package server.test.game_server;

import client.main_client.client.ClientSocket;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import server.main_server.MainServer;
import server.main_server.game_server.socket.SocketServer;

import java.rmi.RemoteException;

import static org.junit.Assert.*;

/**
 * @author Andrea
 * @author Luca
 */
public class SocketServerTest {
    private  ClientSocket client;
    private  ClientSocket client2;

    @BeforeClass
    public static void setupClass() throws RemoteException {
        SocketServer socketServer = new SocketServer();
    }

    @Before
    public void setup() throws RemoteException {
        client = new ClientSocket("andrea", "lol");
        client2 = new ClientSocket("andrea", "lofnwd");
    }

    @Test
    public void startGame() throws InterruptedException {
        client.login();
        assertNull(MainServer.twoPlayerGamesMap.get(MainServer.counter2Players-1));
        client.startGame(2);
        Thread.sleep(1000);
        assertNotNull(MainServer.twoPlayerGamesMap.get(0));
    }

    @Test
    public void loginOk() {
        assertTrue(client.login());
        assertEquals("lol", MainServer.playersMap.get("andrea"));
    }

    @Test
    public void loginError() {
        assertFalse(client2.login());
    }
}