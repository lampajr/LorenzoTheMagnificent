package server.test.game_server;

import client.main.client.ClientSocket;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import server.main.MainServer;
import server.main.game_server.socket.SocketServer;

import java.rmi.RemoteException;

import static org.junit.Assert.*;

/**
 * @author Andrea
 * @author Luca
 */
public class SocketServerTest {
    private static SocketServer socketServer;
    private  ClientSocket client;
    private  ClientSocket client2;

    @BeforeClass
    public static void setupClass() throws RemoteException {
        socketServer = new SocketServer();
    }

    @Before
    public void setup() throws RemoteException {
        client = new ClientSocket("andrea", "lol");
        client2 = new ClientSocket("andrea", "lofnwd");
    }

    @Test
    public void startGame() throws RemoteException, InterruptedException {
        client.login();
        assertNull(MainServer.twoPlayerGamesMap.get(MainServer.counter2Players-1));
        client.startGame(2);
        Thread.sleep(1000);
        assertNotNull(MainServer.twoPlayerGamesMap.get(0));
    }

    @Test
    public void loginOk() throws RemoteException {
        assertTrue(client.login());
        assertEquals("lol", MainServer.playersMap.get("andrea"));
    }

    @Test
    public void loginError() throws RemoteException {
        assertFalse(client2.login());
    }

}