package client.main.client;

import api.ClientInterface;
import api.messages.MessageAction;
import api.messages.MessageNewAction;
import api.types.*;
import client.main.CLI.InterfaceController;
import client.main.GUI.Service;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe che rappresenta il client generico, che avrà metodi in comune tra entrambi i tipi
 * di connessione e metodi che verranno implementati in modo diverso nelle sottoclassi
 * ma con gli stessi risultati( alcuni di questi metodi saranno richiamati dal server attraverso
 * il passaggio di un istanza di playerRMI)
 * @author Andrea
 * @author Luca
 */
public abstract class AbstractClient extends UnicastRemoteObject implements ClientInterface {
    private static boolean logged = false;
    private static AbstractClient instance;
    private String username,password;
    private int id;

    private List<Integer> opponentsIdList;
    private InterfaceController interfaceController; //controller che potrà essere GUIController se della GUI, oppure CLIController se per la cli
    private Phases phase;
    private ActionSpacesType actionSpacesType;
    private CardType cardType;
    private int numFloor;
    private FamilyMemberType familyMemberType;
    private MarketActionType marketActionType;
    private int currentNewActionValue;
    private ActionSpacesType currentNewActionActionSpaceType;
    private CardType currentNewActionCardType;

    private Map<ResourceType, Integer> qtaResourcesMap = new HashMap<>();
    private Map<Integer, Map<ResourceType, Integer>> opponentQtaResourcesMap = new HashMap<>();
    private Map<CardType, List<String>> myCardsList = new HashMap<>();
    private Map<Integer, Map<CardType, List<String>>> opponentsCardsMap = new HashMap<>();


    public AbstractClient(String username, String password) throws RemoteException {
        super();
        this.username = username;
        this.password = password;
    }


    /// METODI EREDITATI DALL'INTERFACCIA CLIENT INTERFACE ////////////////

    /**
     * mi salva il mio id e l'id dei giocatori
     * @param id mio id
     * @param opponents l'id dei giocatori
     */
    @Override
    public void isGameStarted(int id, Map<Integer, String> opponents, List<String> codeExcomList) {
        this.id = id;
        opponentsIdList = new ArrayList<>(opponents.keySet());
        if (interfaceController!=null) {
            interfaceController.startGame(id, username);
            interfaceController.showExcomCards(codeExcomList);
        }
        initializeQtaResourcesMap();
        opponents.forEach(((idValue, name) -> {
            if (interfaceController!=null)
                interfaceController.createOpponentDiscs(idValue, name);
            opponentQtaResourcesMap.put(idValue, new HashMap<>());
            opponentsCardsMap.put(idValue, new HashMap<>());
            initializeOpponentsResources(idValue);
            initializeOpponentsCardsList(idValue);
        }));
        phase = Phases.ACTION;
    }

    private void initializeOpponentsCardsList(int idPlayer) {
        opponentsCardsMap.get(idPlayer).put(CardType.TERRITORY, new ArrayList<>());
        opponentsCardsMap.get(idPlayer).put(CardType.CHARACTER, new ArrayList<>());
        opponentsCardsMap.get(idPlayer).put(CardType.BUILDING, new ArrayList<>());
        opponentsCardsMap.get(idPlayer).put(CardType.VENTURES , new ArrayList<>());
    }

    private void initializeOpponentsResources(int idPlayer) {
        opponentQtaResourcesMap.get(idPlayer).put(ResourceType.COINS, 4);
        opponentQtaResourcesMap.get(idPlayer).put(ResourceType.WOOD, 2);
        opponentQtaResourcesMap.get(idPlayer).put(ResourceType.STONE, 2);
        opponentQtaResourcesMap.get(idPlayer).put(ResourceType.SERVANTS, 3);
        opponentQtaResourcesMap.get(idPlayer).put(ResourceType.MILITARY, 0);
        opponentQtaResourcesMap.get(idPlayer).put(ResourceType.FAITH, 0);
        opponentQtaResourcesMap.get(idPlayer).put(ResourceType.VICTORY, 0);
    }

    private void initializeQtaResourcesMap() {
        qtaResourcesMap.put(ResourceType.COINS, 4);
        qtaResourcesMap.put(ResourceType.WOOD, 2);
        qtaResourcesMap.put(ResourceType.STONE, 2);
        qtaResourcesMap.put(ResourceType.SERVANTS, 3);
        qtaResourcesMap.put(ResourceType.MILITARY, 0);
        qtaResourcesMap.put(ResourceType.FAITH, 0);
        qtaResourcesMap.put(ResourceType.VICTORY, 0);
    }

    /**
     * metodo che mi setta sul tabellone tutte le carte ricevute dal server
     * @param list lista dei nomi delle carte
     */
    public synchronized void setTowersCards(List<String> list) {
        if (interfaceController!=null)
            interfaceController.setBoardCards(list);
    }

    /**
     * metodo che aggiorna le mie risorse
     * @param qtaResourcesMap lista di risorse
     */
    @Override
    public void updateResources(Map<ResourceType, Integer> qtaResourcesMap) {
        this.qtaResourcesMap = qtaResourcesMap;
        if (interfaceController!=null)
            interfaceController.modifyResources(qtaResourcesMap);
    }

    /**
     * metodo che mi aggiorna le mie carte nella personal board
     * @param personalCardsMap mapp delle carte
     */
    @Override
    public synchronized void updatePersonalCards(Map<CardType, List<String>> personalCardsMap) {
        myCardsList = personalCardsMap;
        if (interfaceController!=null){
            interfaceController.removeDrawnCards(personalCardsMap);
            interfaceController.updateMyCards(personalCardsMap);
        }
    }

    @Override
    public void movePersonalFamilyMember() {
        interfaceController.moveFamilyMember(actionSpacesType, cardType, numFloor, marketActionType, familyMemberType);
    }

    /**
     * notifica al client che un suo avversatio ha mosso qualcosa, e cosa..
     * @param id id del giocatore che ha mosso
     * @param personalcardsMap mappa delle carte personali del giocatore che ha mosso
     * @param qtaResourcesMap mappa delle qta delle risorse del giocatore che ha mosso
     */
    @Override
    public void opponentMove(int id, Map<CardType, List<String>> personalcardsMap, Map<ResourceType, Integer> qtaResourcesMap) {
        if (interfaceController!=null)
            interfaceController.removeDrawnCards(personalcardsMap); //rimuovo le carte che ha pescato
        //aggiorno le risorse e le carte del giocatore che ha appena mosso
        opponentQtaResourcesMap.put(id, qtaResourcesMap);
        opponentsCardsMap.put(id, personalcardsMap);
        Map<ResourceType, Integer> pointMap = new HashMap<>();
        Map<ResourceType, Integer> resourcesMap = new HashMap<>();
        qtaResourcesMap.forEach(((resourceType, integer) -> {
            if (resourceType == ResourceType.VICTORY || resourceType == ResourceType.MILITARY || resourceType == ResourceType.FAITH)
                pointMap.put(resourceType,integer);
            else
                resourcesMap.put(resourceType, integer);
        }));
        if (interfaceController!=null) {
            interfaceController.updateOpponentPersonalBoard(personalcardsMap, resourcesMap, id);
            interfaceController.modifyOpponentPoints(pointMap, id);
        }
    }

    /**
     * chiama il metodo del controller che mi sposta il familiare nello spazio azione corretto
     * @param id id del giocatore che muove
     * @param msgAction messaggio codificato della mossa
     */
    @Override
    public void opponentFamilyMemberMove(int id, MessageAction msgAction) {
        if (interfaceController!=null)
            interfaceController.updateOpponentFamilyMemberMove(id, msgAction);
    }

    /**
     * metodo che mi notifica al client un messaggio provenitente dal server
     * @param msg messaggio da notificare
     */
    @Override
    public void notifyMessage(String msg) {
        if (interfaceController!=null)
            interfaceController.notifyMessage(msg);
    }

    /**
     * metodo che serve per notificare al server di lanciare i dadi
     * @param orange dado arancione
     * @param white bianco
     * @param black nero
     */
    @Override
    public void setDiceValues(int orange, int white, int black) {
        if (interfaceController!=null) {
            interfaceController.notifyMessage("First player has rolled the dices!");
            interfaceController.setDices(orange, white, black);
        }
    }

    /**
     * mi informa il giocatore che deve tirare i dadi
     */
    @Override
    public void notifyHaveToShotDice() {
        if (interfaceController!=null) {
            interfaceController.notifyMessage("You have to roll the dices!");
            interfaceController.showDices();
        }
    }

    /**
     * notifica l'ottenimento di un nuovo privielgio
     */
    public void notifyPrivilege() {
        if (interfaceController!=null)
            interfaceController.showPrivilegeAlert();
    }

    /**
     * notifica al client che deve fare un'altra azione
     * @param value valore dell'azione
     * @param codeAction codice dell'azione (spazio azione)
     */
    @Override
    public void notifyNewAction(int value, char codeAction) {
        if (interfaceController!=null)
            interfaceController.notifyMessage("You can do a new Action");
        currentNewActionValue = value;
        currentNewActionActionSpaceType = Service.getActionSpaceType(codeAction);
        currentNewActionCardType = Service.getCardType(codeAction);
        phase = Phases.NEW_ACTION;
    }

    /**
     * notifica al client che è il suo turno nella fase azione
     */
    @Override
    public void notifyYourTurn() {
        if (interfaceController!=null)
            interfaceController.notifyMessage("Is your turn");
        phase = Phases.ACTION;
    }

    /**
     * notifica al client che è il suo turno nella fase scomunica
     */
    @Override
    public void notifyYourExcommunicationTurn() {
        if (interfaceController!=null)
            interfaceController.showExcommunicatingAlert();
        phase = Phases.EXCOMMUNICATION;
    }

    /**
     * mi notifica che ho terminato il turno
     */
    @Override
    public void notifyEndMove() {
        if (interfaceController!=null)
            interfaceController.notifyMessage("You have ended your turn, please wait for your opponents");
        if (phase == Phases.NEW_ACTION)
            phase = Phases.ACTION;
    }

    /**
     * notifica che sono stato scomunicato nel periodo passato come parametro
     * @param period periodo
     */
    @Override
    public void excommunicate(int id, int period) {
        if (interfaceController!=null)
            interfaceController.excommunicate(id, period);
    }

    /**
     * notifica che la partita è terminata, con l'esito
     * @param msg messaggio (esito)
     * @param rankingMap classifica finale
     */
    @Override
    public void gameEnded(String msg, Map<String, Integer> rankingMap) {
        if (interfaceController!=null)
            interfaceController.showGameEndedAlert(msg, rankingMap);
    }

    /**
     * temrine della partita per abbandono
     * @param msg messaggio di vittoria
     */
    @Override
    public void gameEndedByAbandonment(String msg) {
        if (interfaceController!=null)
            interfaceController.showGameEndedAlert(msg);
    }

    /**
     * notifica al controller che deve esporre la lista dei giocatori
     * @param orderList lista degli id
     */
    @Override
    public void notifyTurnOrder(List<Integer> orderList) {
        if (interfaceController!=null)
            interfaceController.showOrderList(orderList);
    }

    /**
     * notifica e aggiorna l'interfaccia eleiminando ogni traccia del giocatore che ha abbandonato
     * @param surrenderId id del giocatore che ha abbandonato
     */
    @Override
    public void notifyOpponentSurrender(int surrenderId) {
        if (interfaceController!=null)
            interfaceController.opponentSurrender(surrenderId);
    }


    /// METODI AGGIUNTI DALLA CLASSE ASTRATTA E GIA' IMPLEMENTATI /////////////////////////////////////////////////////////



    public Map<ResourceType, Integer> getQtaResourcesMap() {
        return qtaResourcesMap;
    }

    public Map<CardType, List<String>> getMyCardsList() {
        return myCardsList;
    }

    public Map<ResourceType, Integer> getOpponentQtaResourcesMap(int id) {
        return opponentQtaResourcesMap.get(id);
    }

    public Map<Integer, Map<CardType, List<String>>> getOpponentsCardsMap() {
        return opponentsCardsMap;
    }

    public List<Integer> getOpponentsIdList() {
        return opponentsIdList;
    }

    public void setActionSpacesType(ActionSpacesType actionSpacesType) {
        this.actionSpacesType = actionSpacesType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public void setNumFloor(int numFloor) {
        this.numFloor = numFloor;
    }

    public void setMarketActionType(MarketActionType marketActionType) {
        this.marketActionType = marketActionType;
    }

    public void setFamilyMemberType(FamilyMemberType familyMemberType) {
        this.familyMemberType = familyMemberType;
    }

    public int getId() {
        return id;
    }

    public void setLogged(){
        logged = true;
    }

    public void setInterfaceController(InterfaceController controller) {
        this.interfaceController = controller;
    }

    int getQtaResource(ResourceType type) {
        return qtaResourcesMap.get(type);
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    public void encodingAndSendingMessage(int servantsToPay) {
        if (phase == Phases.NEW_ACTION) {
            MessageNewAction msgNewAction = encondingMessageNewAction();
            if (msgNewAction!=null)
                doNewAction(msgNewAction, servantsToPay);
        }
        else {
            MessageAction msgAction = encondingMessageAction();
            if (msgAction != null)
                doAction(msgAction, servantsToPay);
        }
    }

    /**
     * metodo che mi codifica il messaggio azione
     * @return ritorna il MessageAction corretto da inviare al server
     */
    public MessageAction encondingMessageAction() {
        if (actionSpacesType == null) {
            if (interfaceController != null)
                interfaceController.notifyMessage("You haven't selected the Action Space");
        }
        else if (familyMemberType == null) {
            if (interfaceController!=null)
                interfaceController.notifyMessage("You haven't selected the family member");
        }
        else
            return new MessageAction(actionSpacesType, cardType, numFloor, marketActionType, familyMemberType);
        return null;
    }

    /**
     * metodo che mi codifica il messaggio nuova azione, verificando se è quella corretta richiesta dal server
     * @return ritorna il MessageAction corretto da inviare al server
     */
    private MessageNewAction encondingMessageNewAction() {
        if (currentNewActionActionSpaceType == ActionSpacesType.SINGLE_HARVEST){
            if (actionSpacesType == ActionSpacesType.SINGLE_HARVEST || actionSpacesType == ActionSpacesType.LARGE_HARVEST)
                return new MessageNewAction(actionSpacesType, cardType, numFloor, marketActionType, currentNewActionValue);
        }
        else if (currentNewActionActionSpaceType == ActionSpacesType.SINGLE_PRODUCTION){
            if (actionSpacesType == ActionSpacesType.SINGLE_PRODUCTION || actionSpacesType == ActionSpacesType.LARGE_PRODUCTION)
                return new MessageNewAction(actionSpacesType, cardType, numFloor, marketActionType, currentNewActionValue);
        }
        else { //non è in zona produzione o raccolta, cioè potrà essere solo nelle torri
            if (actionSpacesType != currentNewActionActionSpaceType) {
                if (interfaceController != null)
                    interfaceController.notifyMessage("You haven't selected the correct Action Space");
                return null;
            }
            else if (currentNewActionCardType == null || cardType == currentNewActionCardType) {
                //o posso farlo in qualsiasi torre o comunque ho selezionato la torre giusta
                return new MessageNewAction(actionSpacesType, cardType, numFloor, marketActionType, currentNewActionValue);
            }
            else {
                if (interfaceController != null)
                    interfaceController.notifyMessage("You haven't selected the correct Tower");
                return null;
            }
        }
        if (interfaceController!=null)
            interfaceController.notifyMessage("You haven't selected the correct Action Space");
        return null;
    }


    /// METODI AGGIUNTI DA QUESTA CLASSE CHE VERRANNO IMPLEMENTATI DALLE SOTTO-CLASSI  ///////////////////////////////////////


    /**
     * metodo che mi tenta il login al server
     */
    public abstract boolean login() throws NotBoundException;

    /**
     * metodo che manda un messaggio al server dicendo che voglio giocare.
     */
    public abstract void startGame(int gameMode);


    /**
     * metodo che invia al server un'azione, verrà implementato nella maniera
     * corretta dalle due sottoclassi.
     * @param msg messaggio già codificato
     * @param servantsToPay  servitori che voglio pagare
     */
    public abstract void doAction(MessageAction msg, int servantsToPay);

    /**
     * metodo che invia un messa di nuova azione al server
     * @param msg messaggio già codificato
     * @param servantsToPay servitori che voglio pagare
     */
    public abstract void doNewAction(MessageNewAction msg, int servantsToPay) ;

    /**
     * il giocatore lancia i dadi, e invia i risultati al server!!
     * @param orange dado arancione
     * @param white bianco
     * @param black nero
     */
    public abstract void shotDice(int orange, int white, int black) ;

    /**
     * metodo che mi identifica la scelta di dare sostegno o meno alla chiesa
     * @param choice true accetto la scomunica, false do sostegno
     */
    public abstract void excommunicationChoice(boolean choice) ;


    /**
     * metyodo che viene chiamato dal client che notifica al server che la mossa è finita
     */
    public abstract void endMove() ;

    /**
     * manda un messaggio al server dicendo in cosa voglio convertire il mio privilegio
     * @param qta qta in cui convertire
     * @param type tipo in cui convertire
     */
    public abstract void convertPrivilege(int qta, ResourceType type) ;

    /**
     * mi dice al server che voglio abbandonare la partita
     */
    public abstract void surrender() ;

    /**
     * esce totalmente dal gioco
     */
    public abstract void exit() ;


    /**
     * metodo utilizzato per ritornarmi ogni volta l'istanza di giocatore corretta
     * @return AbstractClient
     */
    public static AbstractClient getInstance() {
        return instance;
    }

    /**
     * metodo di factory che mi genera un'istanza del giocatore corretto in base alla scelta di connessione
     * @param connection true = RMI, false = Socket.
     * @param username username
     * @param password password
     * @return l'istanza di giocatore corretta
     */
    public static AbstractClient createInstance(boolean connection, String username, String password) {
        try {
            if (!logged){
                if (connection)
                    instance = new ClientRMI(username, password);
                else
                    instance = new ClientSocket(username, password);
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        return instance;
    }
}