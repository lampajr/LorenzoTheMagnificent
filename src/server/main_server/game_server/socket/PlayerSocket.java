package server.main_server.game_server.socket;


import api.messages.MessageAction;
import api.messages.MessageNewAction;
import api.messages.SocketProtocol;
import api.types.CardType;
import api.types.ResourceType;
import server.main_server.game_server.AbstractPlayer;
import server.main_server.model.board.DevelopmentCard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static api.messages.SocketProtocol.*;


/**
 * @author Luca
 * @author Andrea
 *
 * classe che mi identifica il giocatore connesso tramite Socket
 */
public class PlayerSocket extends AbstractPlayer implements Runnable {
    private final SocketServer socketServer;
    private Socket socketClient = null;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    PlayerSocket(String username, SocketServer socketServer) throws RemoteException {
        super(username);
        this.socketServer = socketServer;
    }

    // metodi ereditati e da implementare da ABSTRACT PLAYER //////////////////////////////////////


    /**
     * notifica al giocator che la partita è cominciata
     * @param opponents giocatori avversari
     * @param codeExcomList lista dei codici delle tessere scomunica
     */
    @Override
    public void gameIsStarted(Map<Integer, String> opponents, List<String> codeExcomList) {
        try {
            out.writeObject(SocketProtocol.IS_GAME_STARTED);
            out.flush();
            out.writeInt(getIdPlayer());
            out.flush();
            out.writeObject(opponents);
            out.flush();
            out.writeObject(codeExcomList);
            out.flush();
        }
        catch (IOException e) {
            System.out.println("sending is game started error");
        }
    }

    /**
     * notifica che è il mio turno
     */
    @Override
    public void isYourTurn() {
        try {
            out.writeObject(SocketProtocol.YOUR_TURN);
            out.flush();
        }
        catch (IOException e) {
            System.out.println("sending your turn error");
        }
    }

    /**
     * notifica al giocatore che è il suo turno nella fase scomunica
     */
    @Override
    public void isYourExcommunicationTurn() {
        try {
            out.writeObject(SocketProtocol.YOUR_EXCOMMUNICATION_TURN);
            out.flush();
        }
        catch (IOException e) {
            System.out.println("sending excommunication turn error");
        }
    }

    /**
     * notifica al giocatore che ha vinto per abbandono
     */
    @Override
    public void youWinByAbandonment() {
        try {
            out.writeObject(SocketProtocol.GAME_ENDED_BY_ABANDONMENT);
            out.flush();
            out.writeObject("YOU WON BECAUSE YOUR OPPONENTS HAVE ABANDONED!!");
            out.flush();
        }
        catch (IOException e) {
            System.out.println("sending ended by abandonment error");
        }
    }

    /**
     * notifica al giocatore che ha vinto la partita (terminata correttamente)
     * @param rankingMap classifica finale
     */
    @Override
    public void youWin(Map<String, Integer> rankingMap) {
        try {
            out.writeObject(SocketProtocol.GAME_ENDED);
            out.flush();
            out.writeObject("YOU WON, CONGRATS BUDDY!!");
            out.flush();
            out.writeObject(rankingMap);
            out.flush();
        }
        catch (IOException e) {
            System.out.println("sending you won error");
        }
    }

    /**
     * notifica al giocatore che ha perso
     * @param rankingMap classifica finale
     */
    @Override
    public void youLose(Map<String, Integer> rankingMap) {
        try {
            out.writeObject(SocketProtocol.GAME_ENDED);
            out.flush();
            out.writeObject(" YOU LOSE, SORRY");
            out.flush();
            out.writeObject(rankingMap);
            out.flush();
        }
        catch (IOException e) {
            System.out.println("sending you lose error");
        }
    }

    /**
     * notifica al giocatore che può fare una mossa aggiuntiva senza spostare il familiare
     * @param value valore dell'azione
     * @param codeAction codice che mi identifica che tipo di azione posso fare
     */
    @Override
    public void notifyNewAction(int value, char codeAction) {
        try {
            out.writeObject(NEW_ACTION);
            out.flush();
            out.writeInt(value);
            out.flush();
            out.writeChar(codeAction);
            out.flush();
        }
        catch (IOException e) {
            System.out.println("sending new action error");
        }
    }

    /**
     * notifica al giocatore che è avvenuto un errore nella sua mossa
     * @param errorMessage messaggio d'errore
     */
    @Override
    public void notifyError(String errorMessage) {
        printMsgToClient(errorMessage);
    }

    /**
     * notifica al giocatore tutto ciò che è cambiato in seguito alla propria mossa
     * inviandogli la lista delle proprie carte e delle risorse
     * @param msgAction messaggio codificato dell'azione appena andata a buon fine
     */
    @Override
    public void updateMove(MessageAction msgAction) {
        try {
            out.writeObject(SocketProtocol.UPDATE_RESOURCES);
            out.flush();
            out.writeObject(getPersonalBoard().getQtaResources());
            out.flush();
            out.writeObject(SocketProtocol.UPDATE_PERSONAL_CARDS);
            out.flush();
            out.writeObject(getPersonalBoard().getPersonalCardsMap());
            out.flush();
            if (msgAction!=null) {
                out.writeObject(SocketProtocol.MOVE_MY_FAMILY_MEMBER);
                out.flush();
            }
            if (getGame()!=null)
                getGame().notifyAllPlayers(this, getIdPlayer(), getPersonalBoard().getPersonalCardsMap(), getPersonalBoard().getQtaResources(), msgAction);
        }
        catch (IOException e) {
            System.out.println("sending update error");
        }
    }

    /**
     * notifica al giocatore le modifiche avvenute in seguito alla mossa di un suo avversario
     * @param id id del giocatore che ha mosso
     * @param personalCardsMap mappa delle carte
     * @param qtaResourcesMap mappa delle risorse
     * @param msgAction messaggio codificato dell'azione
     */
    @Override
    public void updateOpponentMove(int id, Map<CardType, List<String>> personalCardsMap, Map<ResourceType, Integer> qtaResourcesMap, MessageAction msgAction) {
        try {
            out.writeObject(SocketProtocol.OPPONENT_MOVE);
            out.flush();
            out.writeInt(id);
            out.flush();
            out.writeObject(personalCardsMap);
            out.flush();
            out.writeObject(qtaResourcesMap);
            out.flush();
            if (msgAction != null) {
                out.writeObject(SocketProtocol.OPPONENT_MEMBER_MOVE);
                out.flush();
                out.writeInt(id);
                out.flush();
                out.writeObject(msgAction);
                out.flush();
            }
        }
        catch (IOException e) {
            System.out.println("sending opponent member move error");
        }
    }

    /**
     * notifica al giocatore che deve tirare i dadi
     */
    @Override
    public void notifyRollDice() {
        try {
            out.writeObject(SocketProtocol.HAVE_TO_SHOT);
            out.flush();
        }
        catch (IOException e) {
            System.out.println("sending have to shot error");
        }
    }

    /**
     * notifica al giocatore che è stato scomunicato
     * @param id id del giocatore
     * @param period periodo
     */
    @Override
    public void excommunicate(int id, int period) {
        if (getGame()!=null)
            getGame().notifyAllPlayers(this, period);
        opponentExcommunicate(id, period);
    }

    /**
     * notifica al giocatore che il suo avversario con id idPlayer è stato scomunicato
     * @param idPlayer id del giocatore
     * @param period periodo
     */
    @Override
    public void opponentExcommunicate(int idPlayer, int period) {
        try {
            out.writeObject(SocketProtocol.EXCOMMUNICATE);
            out.flush();
            out.writeInt(idPlayer);
            out.flush();
            out.writeInt(period);
            out.flush();
        }
        catch (IOException e) {
            System.out.println("sending excommunicate error");
        }
    }

    /**
     * invia i valori dei dadi (tirati da un suo avversario)
     * @param orange valore dado arancione
     * @param white dado bianco
     * @param black dado nero
     */
    @Override
    public void sendDicesValues(int orange, int white, int black) {
        try {
            out.writeObject(SocketProtocol.DICE_VALUES);
            out.flush();
            out.writeInt(orange);
            out.flush();
            out.writeInt(white);
            out.flush();
            out.writeInt(black);
            out.flush();
        }
        catch (IOException e) {
            System.out.println("sending dice values error");
        }
    }

    /**
     * inizializza il tabellone inviando tutte le carte delle torri al client
     * @param towersCardsList lista di stringhe che mi indica i nomi delle carte pescate
     */
    @Override
    public void initializeBoard(List<DevelopmentCard> towersCardsList) {
        List<String> list = new ArrayList<>();
        towersCardsList.forEach((developmentCard -> list.add(developmentCard.getName())));
        try {
            out.writeObject(SocketProtocol.TOWERS_CARDS);
            out.flush();
            out.writeObject(list);
            out.flush();
        }
        catch (IOException e) {
            System.out.println("sending tower cards error");
        }
    }

    /**
     * notifica il temrine del turno
     */
    @Override
    public void notifyEndMove() {
        try {
            out.writeObject(SocketProtocol.END_MOVE);
            out.flush();
        }
        catch (IOException e) {
            System.out.println("sending end move error");
        }
    }

    /**
     * notifica l'ottenimento di un privilegio
     */
    @Override
    public void notifyPrivilege() {
        try {
            out.writeObject(SocketProtocol.PRIVILEGE);
            out.flush();
        }
        catch (IOException e) {
            System.out.println("sending privilege error");
        }
    }

    /**
     * invia l'ordine di gioco
     * @param playersOrderList lista dei giocatori
     */
    @Override
    public void sendOrder(List<AbstractPlayer> playersOrderList) {
        List<Integer> orderList = new ArrayList<>();
        for (AbstractPlayer player: playersOrderList) {
            orderList.add(player.getIdPlayer());
        }
        try {
            out.writeObject(SocketProtocol.ORDER);
            out.flush();
            out.writeObject(orderList);
            out.flush();
        }
        catch (IOException e) {
            System.out.println("sending order error");
        }
    }

    /**
     * notifica al giocatore che il suo avversario ha abbandonato
     * @param id id del giocatore arreso
     */
    @Override
    public void opponentSurrender(int id) {
        try {
            out.writeObject(SocketProtocol.OPPONENT_SURRENDER);
            out.flush();
            out.writeInt(id);
            out.flush();
        }
        catch (IOException e) {
            System.out.println("sending opponent surrender error");
        }
    }

    /**
     * notifica al giocatore un messaggio
     * @param content contenuto del messaggio
     */
    private void printMsgToClient(String content){
        try {
            out.writeObject(SocketProtocol.INFORMATION);
            out.flush();
            out.writeObject(content);
            out.flush();
        }
        catch (IOException e) {
            System.out.println("sending information error");
        }
    }

    /**
     * setta i canali di comunicazione e la socket
     * @param socket socket
     * @param in stream in input
     * @param out stream in output
     */
    void setSocketConnection(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        this.socketClient = socket;
        this.in = in;
        this.out = out;
    }


    /**
     * torna al serverSocket precedente per la scelta della partita
     * @throws IOException in caso di disconnessioni
     */
    private void backToMenu() throws IOException {
        new Thread(this.socketServer.new PlayerSocketRequest(socketClient, in, out)).start();
        out.writeObject(SocketProtocol.RESTART);
        out.flush();
    }

    /**
     * corpo del thread dove rimane in attesa dei messaggi provenienti dal client
     */
    @Override
    public void run() {
        boolean restart = false;
        try{
            try{
                boolean connect = true;
                while (connect) {
                    SocketProtocol msg;
                    Object obj = in.readObject();
                    if (obj instanceof SocketProtocol) {
                        msg = (SocketProtocol) obj;
                        switch (msg){
                            case NEW_ACTION:
                                MessageNewAction msgNewAction = (MessageNewAction) in.readObject();
                                doNewAction(msgNewAction);
                                break;
                            case ACTION:
                                MessageAction msgAction = (MessageAction) in.readObject();
                                doAction(msgAction);
                                break;
                            case SHOT_DICE:
                                int orange = in.readInt();
                                int white = in.readInt();
                                int black = in.readInt();
                                shotDice(orange, white, black);
                                break;
                            case EXCOMMUNICATION_CHOICE:
                                boolean choice = in.readBoolean();
                                excommunicationChoice(choice);
                                break;
                            case END_MOVE:
                                endMove();
                                break;
                            case SURRENDER:
                                surrender();
                                backToMenu();
                                restart = true;
                                connect = false;
                                break;
                            case CONVERT_PRIVILEGE:
                                int qta = in.readInt();
                                ResourceType type = (ResourceType) in.readObject();
                                convertPrivilege(qta, type);
                                break;
                            case RESTART:
                                backToMenu();
                                restart = true;
                                connect = false;
                                break;
                            case EXIT:
                                surrender();
                                connect = false;
                                out.writeObject(SocketProtocol.EXIT);
                                out.flush();
                                break;
                        }
                    }
                }
            }
            catch (IOException | ClassNotFoundException e) {
                try {
                    in.close();
                    in = null;
                    out.close();
                    out = null;
                } catch (IOException e1) {
                    System.out.println("closing error");
                }
            }
        }
        finally {
            try {
                if (in != null && out != null && !restart){
                    in.close();
                    out.close();
                }
            } catch (IOException e) {
                System.out.println("finally closing error");
            }
        }
    }
}
