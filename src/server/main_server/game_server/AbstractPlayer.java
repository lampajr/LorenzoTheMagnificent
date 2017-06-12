package server.main_server.game_server;


import api.ClientInterface;
import api.PlayerInterface;
import api.messages.MessageAction;
import api.messages.MessageNewAction;
import api.types.CardType;
import api.types.FamilyMemberType;
import api.types.ResourceType;
import server.main_server.game_server.exceptions.NewActionException;
import server.main_server.model.Game;
import server.main_server.model.action_spaces.Action;
import server.main_server.model.board.DevelopmentCard;
import server.main_server.model.board.FamilyMember;
import server.main_server.model.board.PersonalBoard;
import server.main_server.model.fields.Field;
import server.main_server.model.fields.Resource;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

/**
 * @author Luca
 * @author Andrea
 */
public abstract class AbstractPlayer extends UnicastRemoteObject implements PlayerInterface {
    private ClientInterface clientInterface;
    private String username;
    private PersonalBoard personalBoard;
    private int idPlayer;
    private Game game;

    public AbstractPlayer(String username) throws RemoteException {
        super();
        this.username = username;
    }

    public void setGame(Game game) {
        this.game = game;
        idPlayer = game.getId(this);
    }

    public int calculateVictoryPoints() {
        return personalBoard.calculateVictoryPoints();
    }

    /**
     * metodo che mi setta il valore a tutti i miei familiari
     * @param orange valore dell'arancione
     * @param white valore del bianco
     * @param black valore del nero
     */
    public void setDiceValues(int orange, int white, int black) {
        personalBoard.setDiceValues(orange,white,black);
        sendDicesValues(orange, white, black);
    }

    public FamilyMember getFamilyMember(FamilyMemberType type) {
        return personalBoard.getFamilyMember(type);
    }

    public Game getGame() {
        return game;
    }

    public void createPersonalBoard(int id) {
        this.idPlayer = id;
        personalBoard = new PersonalBoard(id);
    }

    public PersonalBoard getPersonalBoard() {
        return this.personalBoard;
    }

    protected ClientInterface getClientInterface() {
        return this.clientInterface;
    }

    public int getIdPlayer() {
        return idPlayer;
    }

    public String getUsername() {
        return this.username;
    }

    /**
     * metodo che mi rimuove tutti i familiari dalle loro postazione
     * cioè mi setterà a false il boolean isPositioned di ciascuno.
     */
    public void removeAllFamilyMembers(){
        personalBoard.removeAllFamilyMembers();
    }

    public void activeExcommunicationEffects(Action action, int type) {
        personalBoard.setCurrentAction(action);
        try {
            if (game!=null)
                game.activeFirstPeriodExcommunication(action, type);
        }
        catch (NewActionException e) {
            //non dovrebbe mai verificrsi
            e.printStackTrace();
        }
    }

    public void activeExcommunicationEffects(Action action) {
        personalBoard.setCurrentAction(action);
        try {
            if (game!=null)
                game.activeSecondPeriodExcommunication(action);
        }
        catch (NewActionException e) {
            //non dovrebbe mai verificrsi
            e.printStackTrace();
        }
    }


    /// METODI ASTRATTI AGGIUNTI DA QUESTA CLASSE E CHE VERRANNO IMPLEMENTATI DALLE SUE DUE DIRETTE SOTTOCLASSI ///////////////


    /**
     * mi notifica che la partita è cominciata
     * @param opponents giocatori avversari
     * @param codeList codici delle tessete scomuniche
     */
    public abstract void gameIsStarted(Map<Integer, String> opponents, List<String> codeList);

    /**
     * mi notifica che è il mio turno
     */
    public abstract void isYourTurn();

    /**
     * metodo che notifica al client che è il suo turno di scelta se scomunicarsi o no.
     */
    public abstract void isYourExcommunicationTurn();

    /**
     * mi notifica che ho vinto in seguito ad un abbandono
     */
    public abstract void youWinByAbandonment();

    /**
     * notifica che ho vinto passandomi la classifica finale
     * @param rankingMap classifica finale
     */
    public abstract void youWin(Map<String, Integer> rankingMap);

    /**
     * mi notifica che ho perso passandomi la classifica finale
     * @param rankingMap classifica finale
     */
    public abstract void youLose(Map<String, Integer> rankingMap);

    /**
     * mi notifica che devo fare un'altra azione
     * @param value valore dell'azione
     * @param codeAction codice che mi identifica che tipo di azione posso fare
     */
    public abstract void notifyNewAction(int value, char codeAction);

    /**
     * mi notifica un messaggio di errore
     * @param message messaggio
     */
    public abstract void notifyError(String message);

    /**
     * mi notifica i cambiamenti nella mia plancia in seguito alla mia mossa
     * @param msgAction messaggio codificato dell'azione appena andata a buon fine
     */
    public abstract void updateMove(MessageAction msgAction);

    /**
     * notifica a tutti i giocatori che cosa ha mosso il giocatore che ha appena effettuato la mossa
     */
    public abstract void updateOpponentMove(int id, Map<CardType, List<String>> personalCardsMap, Map<ResourceType, Integer> qtaResourcesMap, MessageAction msg);

    /**
     * notifica al giocatore che deve tirare i dadi
     */
    public abstract void notifyRollDice();

    /**
     * notifica al giocatore che è stato scomunicato
     * @param id id del giocatore
     * @param period periodo
     */
    public abstract void excommunicate(int id, int period);

    /**
     * notifica che un altro giocatore è stato scomunicato
     * @param idPlayer id del giocatore
     * @param period periodo
     */
    public abstract void opponentExcommunicate(int idPlayer, int period);

    /**
     * metodo che invia al client i risultati del tiro del dado
     * @param orange valore dado arancione
     * @param white dado bianco
     * @param black dado nero
     */
    public abstract void sendDicesValues(int orange, int white, int black);

    /**
     * metodo che viene chiamato per inizializzare il turno, cioè mi invia al
     * client tutte le carte che sono state pescate in questo turno
     * @param towersCardsList lista di stringhe che mi indica i nomi delle carte pescate
     */
    public abstract void initializeBoard(List<DevelopmentCard> towersCardsList);

    /**
     * notifica al client che ha terminato il suo turno
     */
    public abstract void notifyEndMove();

    /**
     * metodo che notifica il guadagno di un privilegio
     */
    public abstract void notifyPrivilege();

    /**
     * invia al client l'ordine dei giocatori
     * @param playersOrderList lista dei giocatori
     */
    public abstract void sendOrder(List<AbstractPlayer> playersOrderList);

    /**
     * notifica al giocatore che il giocatore con id (id) si è arreso
     * @param id id del giocatore arreso
     */
    public abstract void opponentSurrender(int id);



    /// METODI IMPLEMENTATI DALL'INTERFACCIA PLAYER INTERFACE ///////////////////////////////////////////////



    @Override
    public void shotDice(int orange, int white, int black){
        if (game!=null)
            game.shotDice(this, orange, white, black);
    }


    @Override
    public void addClientInterface(ClientInterface clientInterface){
        this.clientInterface = clientInterface;
    }

    @Override
    public synchronized void doAction(MessageAction msg){
        FamilyMember familyMember = personalBoard.getFamilyMember(msg.getFamilyMemberType());
        if (game!=null)
            game.doAction(this, msg, familyMember);
    }

    @Override
    public void endMove() {
        if (game!=null)
            game.endMove(this);
    }

    @Override
    public synchronized void surrender(){
        if (game!=null)
            game.removePlayer(this);
    }

    @Override
    public void doNewAction(MessageNewAction msg)  {
        if (game!=null)
            game.doNewAction(this, msg);
    }

    /**
     * identifica la scelta nella fase di scomunica.
     * @param choice true accetto la scomunica, false do sostegno
     */
    @Override
    public synchronized void excommunicationChoice(boolean choice) {
        if (game!=null){
            if (choice){
                game.excommunicatePlayer(this);
            }
            else {
                game.giveChurchSupport(this);
            }
        }
    }

    @Override
    public void convertPrivilege(int qta, ResourceType type) {
        Field res = new Resource(qta, type);
        personalBoard.modifyResources(res);
        personalBoard.setCurrentField(res);
        activeExcommunicationEffects(new Action(null, 0, null, this), 2);
        updateMove(null);
        if (game!=null)
            game.notifyAllPlayers(this, idPlayer, personalBoard.getPersonalCardsMap(), personalBoard.getQtaResources(), null);
    }
}
