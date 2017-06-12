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

    public PlayerSocket(String username, SocketServer socketServer ) throws RemoteException {
        super(username);
        this.socketServer = socketServer;
    }

    // metodi ereditati e da implementare da ABSTRACT PLAYER //////////////////////////////////////

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
            e.printStackTrace();
        }
    }

    @Override
    public void isYourTurn() {
        try {
            out.writeObject(SocketProtocol.YOUR_TURN);
            out.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void isYourExcommunicationTurn() {
        try {
            out.writeObject(SocketProtocol.YOUR_EXCOMMUNICATION_TURN);
            out.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void youWinByAbandonment() {
        try {
            out.writeObject(SocketProtocol.GAME_ENDED_BY_ABANDONMENT);
            out.flush();
            out.writeObject("YOU WON BECAUSE YOUR OPPONENTS HAVE ABANDONED!!");
            out.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void youWin(Map<String, Integer> rankingMap) {
        try {
            out.writeObject(SocketProtocol.GAME_ENDED_BY_ABANDONMENT);
            out.flush();
            out.writeObject("YOU WON, CONGRATS BUDDY!!");
            out.flush();
            out.writeObject(rankingMap);
            out.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
    }

    @Override
    public void notifyNewAction(int value, char codeAction) {
        try {
            out.writeObject(SocketProtocol.NEW_ACTION);
            out.flush();
            out.writeInt(value);
            out.flush();
            out.writeChar(codeAction);
            out.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyError(String errorMessage) {
        printMsgToClient(errorMessage);
    }

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
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
    }

    @Override
    public void notifyRollDice() {
        try {
            out.writeObject(SocketProtocol.HAVE_TO_SHOT);
            out.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void excommunicate(int id, int period) {
        if (getGame()!=null)
            getGame().notifyAllPlayers(this, period);
        opponentExcommunicate(id, period);
    }

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
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
    }

    @Override
    public void notifyEndMove() {
        try {
            out.writeObject(SocketProtocol.END_MOVE);
            out.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyPrivilege() {
        try {
            out.writeObject(SocketProtocol.PRIVILEGE);
            out.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
    }

    @Override
    public void opponentSurrender(int id) {
        try {
            out.writeObject(SocketProtocol.OPPONENT_SURRENDER);
            out.flush();
            out.writeInt(id);
            out.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printMsgToClient(String content){
        try {
            out.writeObject(SocketProtocol.INFORMATION);
            out.flush();
            out.writeObject(content);
            out.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // metodi ereditati dalla PLAYER INTERFACE ////////////////////////////////////////


    public void setSocketConnection(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        this.socketClient = socket;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        try{
            try{
                boolean connect = true;
                while (connect) {
                    SocketProtocol msg = (SocketProtocol) in.readObject();
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
                            new Thread(this.socketServer.new PlayerSocketRequest(socketClient)).start();
                            out.writeObject(SocketProtocol.SURRENDER);
                            out.flush();
                            connect = false;
                            break;
                        case CONVERT_PRIVILEGE:
                            int qta = in.readInt();
                            ResourceType type = (ResourceType) in.readObject();
                            convertPrivilege(qta, type);
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
            catch (IOException | ClassNotFoundException e) {
                try {
                    in.close();
                    in = null;
                    out.close();
                    out = null;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        finally {
            try {
                if (in != null && out != null ){
                    in.close();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
