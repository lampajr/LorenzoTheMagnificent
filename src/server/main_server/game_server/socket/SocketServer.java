package server.main_server.game_server.socket;


import api.ClientInterface;
import api.PlayerInterface;
import api.messages.SocketProtocol;
import server.main_server.game_server.AbstractServer;
import server.main_server.model.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;


/**
 * @author Luca
 * @author Andrea
 *
 * classe che mi identifica il server Socket che implementa la stessa interfaccia del server RMI
 * ma qui i metodi verranno chiamati in seguito a messaggi provenienti dal client e codificati.
 */
public class SocketServer extends AbstractServer implements Runnable {
    private final SocketServer socketServer;
    private ServerSocket server = null;
    private static final int PORT = 4000;

    public SocketServer() throws RemoteException{
        this.socketServer = this;
        new Thread(this).start();
    }


    @Override
    public synchronized PlayerInterface startGame(String username, int gameMode, ClientInterface client) throws RemoteException {
        Game game = getFreeGame(gameMode); //la prima partita libera trovata, in caso ne crea una nuova
        PlayerSocket playerSocket = new PlayerSocket(username, socketServer);
        playerSocket.setGame(game);
        game.addPlayer(playerSocket);
        return playerSocket;
    }

    public void run(){
        try {
            server = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Waiting for connection in PORT:" + PORT);
        while (true) {
            try {
                System.out.println("......");
                Socket socketClient = server.accept();
                System.out.println("Connection accepted from: " + socketClient.getInetAddress()); // potremmo togliere le sysout
                PlayerSocketRequest playerRequest = new PlayerSocketRequest(socketClient);
                new Thread(playerRequest).start();
            }
            catch (IOException e) {
                System.out.println("Server socket interrupted");
                break;
            }
        }
    }

    public class PlayerSocketRequest implements Runnable{
        private final Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private boolean restart = false;

        PlayerSocketRequest(Socket socket){
            this.socket = socket;
        }

        PlayerSocketRequest(Socket socket, ObjectInputStream in, ObjectOutputStream out){
            this.socket = socket;
            this.in = in;
            this.out = out;
            this.restart = true;
        }

        @Override
        public void run() {
            String username;
            String password;
            try {
                int gameMode;
                SocketProtocol msg;
                if (!restart) {
                    in = new ObjectInputStream(socket.getInputStream());
                    msg = (SocketProtocol) in.readObject();
                    if (msg == SocketProtocol.LOGIN) {
                        username = (String) in.readObject();
                        password = (String) in.readObject();
                        out = new ObjectOutputStream(socket.getOutputStream());
                        boolean resp = login(username, password);
                        System.out.println(resp);
                        out.writeBoolean(resp);
                        out.flush();
                    }
                }
                boolean isAssociated = false;
                try{
                    while (!isAssociated) {
                        msg = (SocketProtocol) in.readObject();
                        switch (msg) {
                            case LOGIN:
                                username = (String) in.readObject();
                                password = (String) in.readObject();
                                out.writeObject(login(username, password));
                                break;
                            case START_GAME:
                                username = (String) in.readObject();
                                gameMode = in.readInt();
                                PlayerSocket player = (PlayerSocket) startGame(username, gameMode, null);
                                if (player != null) {
                                    player.setSocketConnection(socket, in, out);
                                    new Thread(player).start();
                                    isAssociated = true;
                                }
                                break;
                        }
                    }
                }
                catch (IOException e) {
                    System.out.println("player disconnected n.1");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("player disconnected n.2");
            }
        }
    }
}
