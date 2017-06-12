package server.main_server.game_server.rmi;


import api.ClientInterface;
import api.PlayerInterface;
import server.main_server.game_server.AbstractServer;
import server.main_server.model.Game;

import java.rmi.RemoteException;

/**
 * @author Luca
 * @author Andrea
 *
 * classe che mi identifica l'oggetto remoto che il client RMI userà direttamente per la connessione
 * scaricando l'oggetto dal registro.
 */
public class ServerRMI extends AbstractServer {

    public ServerRMI() throws RemoteException {
    }

    @Override
    public synchronized PlayerInterface startGame(String username, int gameMode, ClientInterface client) throws RemoteException {
        Game game = getFreeGame(gameMode); //la prima partita libera trovata
        PlayerRMI playerRMI = new PlayerRMI(username);
        playerRMI.addClientInterface(client);
        playerRMI.setGame(game);
        game.addPlayer(playerRMI);
        return playerRMI;
    }
}
