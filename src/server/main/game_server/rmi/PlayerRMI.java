package server.main.game_server.rmi;


import api.messages.MessageAction;
import api.types.CardType;
import api.types.ResourceType;
import server.main.game_server.AbstractPlayer;
import server.main.model.board.DevelopmentCard;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author Luca
 * @author Andrea
 *
 * Classe che identifica il giocatore connesso tramite RMI
 */
public class PlayerRMI extends AbstractPlayer {

    public PlayerRMI(String username) throws RemoteException {
        super(username);

    }

    // OVERRIDE DEI METODI EREDITATI DA ABSTRACT PLAYER /////////////////////////////////////////////////


    /**
     * notifica l'inizio della partita passando i giocatori avversarie le tessere scomunica
     * @param opponents giocatori avversari
     * @param codeList codici delle tessete scomuniche
     * @throws RemoteException
     */
    @Override
    public void gameIsStarted(Map<Integer, String> opponents, List<String> codeList) throws RemoteException {
        if (getClientInterface()!= null)
            getClientInterface().isGameStarted(getIdPlayer(), opponents, codeList);
    }

    /**
     * notifco al giocatore che è il suo turno
     * @throws RemoteException
     */
    public void isYourTurn() throws RemoteException {
        if (getClientInterface()!= null)
            getClientInterface().notifyYourTurn();
    }

    @Override
    public void isYourExcommunicationTurn() throws RemoteException {
        if (getClientInterface()!= null)
            getClientInterface().notifyYourExcommunicationTurn();
    }

    public void youWin() throws RemoteException {
        if (getClientInterface()!= null)
            getClientInterface().gameEnded("YOU WON, CONGRATS BUDDY!!");
    }

    public void youLose() throws RemoteException {
        if (getClientInterface()!= null)
            getClientInterface().gameEnded(" YOU LOSE, SORRY ");
    }

    @Override
    public void notifyNewAction(int value, char codeAction) throws RemoteException {
        if (getClientInterface()!= null)
            getClientInterface().notifyNewAction(value, codeAction);
    }

    @Override
    public void notifyError(String errorMessage) throws RemoteException {
        if (getClientInterface()!= null)
            getClientInterface().notifyMessage(errorMessage);
    }

    @Override
    public void updateMove(MessageAction msg) throws RemoteException {
        if (getClientInterface()!= null) {
            getClientInterface().updateResources(getPersonalBoard().getQtaResources());
            getClientInterface().updatePersonalCards(getPersonalBoard().getPersonalCardsMap());
        }
        if (getGame()!= null)
            getGame().notifyAllPlayers(this, getIdPlayer(), getPersonalBoard().getPersonalCardsMap(), getPersonalBoard().getQtaResources(), msg);
    }

    @Override
    public void updateOpponentMove(int id, Map<CardType, List<String>> personalcardsMap, Map<ResourceType, Integer> qtaResourcesMap, MessageAction msgAction) throws RemoteException {
        if (getClientInterface()!= null)
            getClientInterface().opponentMove(id, personalcardsMap, qtaResourcesMap);
        if (msgAction != null)
            getClientInterface().opponentFamilyMemberMove(id, msgAction);
    }

    /**
     * mi informa il giocatore che deve tirare i dadi
     * @throws RemoteException
     */
    @Override
    public void notifyRollDice() throws RemoteException {
        if (getClientInterface()!= null)
            getClientInterface().notifyHaveToShotDice();
    }

    /**
     * mi invia i valori dei dadi ai giocatori
     * @param orange arancio
     * @param white bianco
     * @param black nero
     * @throws RemoteException
     */
    @Override
    public void sendDicesValues(int orange, int white, int black) throws RemoteException {
        if (getClientInterface()!= null)
            getClientInterface().setDiceValues(orange, white, black);
    }

    /**
     * metodo che invia al giocatore la lista delle carte sulle torri
     * @param towersCardsList lista di stringhe che mi indica i nomi delle carte pescate
     * @throws RemoteException
     */
    @Override
    public void initializeBoard(List<DevelopmentCard> towersCardsList) throws RemoteException {
        List<String> list = new ArrayList<>();
        towersCardsList.forEach((developmentCard -> list.add(developmentCard.getName())));
        //if (getClientInterface()!= null)
            getClientInterface().setTowersCards(list);
    }

    @Override
    public void notifyEndMove() throws RemoteException {
        if (getClientInterface()!= null)
            getClientInterface().notifyEndMove();
    }

    /**
     * notifica che ha appena ottenuto un privilegio
     * @throws RemoteException
     */
    @Override
    public void notifyPrivilege() throws RemoteException {
        if (getClientInterface()!= null)
            getClientInterface().notifyPrivilege();
    }

    /**
     * invio la lista degli id dei giocatori nell'ordine corretto
     * @param playersOrderList lista dei giocatori
     * @throws RemoteException
     */
    @Override
    public void sendOrder(List<AbstractPlayer> playersOrderList) throws RemoteException {
        List<Integer> orderList = new ArrayList<>();
        for (AbstractPlayer player: playersOrderList) {
            orderList.add(player.getIdPlayer());
        }
        if (getClientInterface()!= null)
            getClientInterface().notifyTurnOrder(orderList);
    }

    @Override
    public void opponentSurrender(int id) throws RemoteException {
        if (getClientInterface()!= null)
            getClientInterface().notifyOpponentSurrender(id);
    }

    @Override
    public void excommunicate(int id, int period) throws RemoteException {
        if (getGame()!=null)
            getGame().notifyAllPlayers(this, period);
        if (getClientInterface()!= null)
            getClientInterface().excommunicate(id, period);
    }

    @Override
    public void opponentExcommunicate(int idPlayer, int period) throws RemoteException {
        if (getClientInterface()!= null)
            getClientInterface().excommunicate(idPlayer, period);
    }


    //metodi erediati da PLAYER INTERFACE /////////////////////////////////////////////////////////


}
