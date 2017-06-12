package server.main_server.game_server.rmi;


import api.messages.MessageAction;
import api.types.CardType;
import api.types.ResourceType;
import server.main_server.game_server.AbstractPlayer;
import server.main_server.model.board.DevelopmentCard;

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
     *
     * @param opponents giocatori avversari
     * @param codeList  codici delle tessete scomuniche
     */
    @Override
    public void gameIsStarted(Map<Integer, String> opponents, List<String> codeList) {
        try {
            if (getClientInterface() != null)
                getClientInterface().isGameStarted(getIdPlayer(), opponents, codeList);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * notifco al giocatore che è il suo turno
     */
    public void isYourTurn() {
        try {
            if (getClientInterface() != null)
                getClientInterface().notifyYourTurn();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * notifica al giocatore che è il suo turno scomunica
     */
    @Override
    public void isYourExcommunicationTurn() {
        try {
            if (getClientInterface() != null)
                getClientInterface().notifyYourExcommunicationTurn();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * notifica al giocatore che ha vinto per abbandono
     */
    public void youWinByAbandonment() {
        try {
            if (getClientInterface() != null)
                getClientInterface().gameEndedByAbandonment("YOU WON BECAUSE YOUR OPPONENTS HAVE ABANDONED!!");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * notifica al giocatore che ha vinto
     *
     * @param rankingMap classifica finale
     */
    @Override
    public void youWin(Map<String, Integer> rankingMap) {
        try {
            if (getClientInterface() != null)
                getClientInterface().gameEnded("YOU WON, CONGRATS BUDDY!!", rankingMap);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * notifica al giocatore che ha perso
     *
     * @param rankingMap classifica finale
     */
    @Override
    public void youLose(Map<String, Integer> rankingMap) {
        try {
            if (getClientInterface() != null)
                getClientInterface().gameEnded(" YOU LOSE, SORRY ", rankingMap);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * notifica al giocatore che può fare una mossa aggiuntiva senza spostare il familiare
     *
     * @param value      valore dell'azione
     * @param codeAction codice che mi identifica che tipo di azione posso fare
     */
    @Override
    public void notifyNewAction(int value, char codeAction) {
        try {
            if (getClientInterface() != null)
                getClientInterface().notifyNewAction(value, codeAction);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyError(String errorMessage) {
        try {
            if (getClientInterface() != null)
                getClientInterface().notifyMessage(errorMessage);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * notifica al giocatore tutto ciò che è cambiato in seguito alla propria mossa
     * inviandogli la lista delle proprie carte e delle risorse
     *
     * @param msg messaggio codificato dell'azione appena andata a buon fine
     */
    @Override
    public void updateMove(MessageAction msg) {
        try {
            if (getClientInterface() != null) {
                getClientInterface().updateResources(getPersonalBoard().getQtaResources());
                getClientInterface().updatePersonalCards(getPersonalBoard().getPersonalCardsMap());
                if (msg != null)
                    getClientInterface().movePersonalFamilyMember();
            }
            if (getGame() != null)
                getGame().notifyAllPlayers(this, getIdPlayer(), getPersonalBoard().getPersonalCardsMap(), getPersonalBoard().getQtaResources(), msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * notifica al giocatore le modifiche avvenute in seguito alla mossa di un suo avversario
     *
     * @param id               id del giocatore che ha mosso
     * @param personalCardsMap mappa delle carte
     * @param qtaResourcesMap  mappa delle risorse
     * @param msgAction        messaggio codificato dell'azione
     */
    @Override
    public void updateOpponentMove(int id, Map<CardType, List<String>> personalCardsMap, Map<ResourceType, Integer> qtaResourcesMap, MessageAction msgAction) {
        try {
            if (getClientInterface() != null)
                getClientInterface().opponentMove(id, personalCardsMap, qtaResourcesMap);
            if (msgAction != null)
                getClientInterface().opponentFamilyMemberMove(id, msgAction);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * mi informa il giocatore che deve tirare i dadi
     */
    @Override
    public void notifyRollDice() {
        try {
            if (getClientInterface() != null)
                getClientInterface().notifyHaveToShotDice();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * mi invia i valori dei dadi ai giocatori
     *
     * @param orange arancio
     * @param white  bianco
     * @param black  nero
     */
    @Override
    public void sendDicesValues(int orange, int white, int black) {
        try {
            if (getClientInterface() != null)
                getClientInterface().setDiceValues(orange, white, black);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * metodo che invia al giocatore la lista delle carte sulle torri
     *
     * @param towersCardsList lista di stringhe che mi indica i nomi delle carte pescate
     */
    @Override
    public void initializeBoard(List<DevelopmentCard> towersCardsList) {
        List<String> list = new ArrayList<>();
        towersCardsList.forEach((developmentCard -> list.add(developmentCard.getName())));
        try {
            if (getClientInterface() != null)
                getClientInterface().setTowersCards(list);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * notifica al giocatore che ha terminato il turno
     */
    @Override
    public void notifyEndMove() {
        try {
            if (getClientInterface() != null)
                getClientInterface().notifyEndMove();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * notifica che ha appena ottenuto un privilegio
     */
    @Override
    public void notifyPrivilege() {
        try {
            if (getClientInterface() != null)
                getClientInterface().notifyPrivilege();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * invio la lista degli id dei giocatori nell'ordine corretto
     *
     * @param playersOrderList lista dei giocatori
     */
    @Override
    public void sendOrder(List<AbstractPlayer> playersOrderList) {
        List<Integer> orderList = new ArrayList<>();
        for (AbstractPlayer player : playersOrderList) {
            orderList.add(player.getIdPlayer());
        }
        try {
            if (getClientInterface() != null)
                getClientInterface().notifyTurnOrder(orderList);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * notifica al giocatore che il suo avversario ha abbandonato
     *
     * @param id id del giocatore arreso
     */
    @Override
    public void opponentSurrender(int id) {
        try {
            if (getClientInterface() != null)
                getClientInterface().notifyOpponentSurrender(id);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * notifica al giocatore che è stato scomunicato
     *
     * @param id     id del giocatore
     * @param period periodo
     */
    @Override
    public void excommunicate(int id, int period) {
        try {
            if (getGame() != null)
                getGame().notifyAllPlayers(this, period);
            if (getClientInterface() != null)
                getClientInterface().excommunicate(id, period);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * notifica al giocatore che il suo avversario con id idPlayer è stato scomunicato
     *
     * @param idPlayer id del giocatore
     * @param period   periodo
     */
    @Override
    public void opponentExcommunicate(int idPlayer, int period) {
        try {
            if (getClientInterface() != null)
                getClientInterface().excommunicate(idPlayer, period);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
