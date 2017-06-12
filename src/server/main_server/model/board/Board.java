package server.main_server.model.board;

import api.messages.Message;
import api.messages.MessageAction;
import api.messages.MessageNewAction;
import api.types.CardType;
import api.types.MarketActionType;
import api.types.ResourceType;
import server.main_server.game_server.AbstractPlayer;
import server.main_server.game_server.exceptions.LorenzoException;
import server.main_server.game_server.exceptions.NewActionException;
import server.main_server.model.action_spaces.Action;
import server.main_server.model.action_spaces.ActionSpaceInterface;
import server.main_server.model.action_spaces.large_action_spaces.CouncilActionSpace;
import server.main_server.model.action_spaces.large_action_spaces.LargeHarvestActionSpace;
import server.main_server.model.action_spaces.large_action_spaces.LargeProductionActionSpace;
import server.main_server.model.action_spaces.single_action_spaces.HarvestActionSpace;
import server.main_server.model.action_spaces.single_action_spaces.MarketActionSpace;
import server.main_server.model.action_spaces.single_action_spaces.ProductionActionSpace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Luca
 * @author Andrea
 *
 * rappresenta il tabellone della partita.
 */
public class Board {
    private final int numPlayers;

    private Map<CardType, Tower> towerMap;
    private HarvestActionSpace harvestActionSpace;
    private ProductionActionSpace productionActionSpace;
    private LargeHarvestActionSpace largeHarvestActionSpace;
    private LargeProductionActionSpace largeProductionActionSpace;
    private Map<MarketActionType, MarketActionSpace> marketActionSpaceMap;
    private CouncilActionSpace councilActionSpace;

    private Excommunication excommunication;
    private DevelopmentDeck developmentDeck;

    private Action currentAction;

    public Board(int numPlayers){
        initializeTowers();
        this.numPlayers = numPlayers;
        initializeActionSpaces();
        initializeDecks();
    }

    /**
     * Metodo che inizializza gli spazi azione
     */
    private void initializeActionSpaces() {
        marketActionSpaceMap = new HashMap<>();
        harvestActionSpace = new HarvestActionSpace();
        productionActionSpace = new ProductionActionSpace();
        councilActionSpace = new CouncilActionSpace();
        marketActionSpaceMap.put(MarketActionType.YELLOW,new MarketActionSpace(MarketActionType.YELLOW));
        marketActionSpaceMap.put(MarketActionType.PURPLE,new MarketActionSpace(MarketActionType.PURPLE));
        if (numPlayers > 2) {
            largeHarvestActionSpace = new LargeHarvestActionSpace();
            largeProductionActionSpace = new LargeProductionActionSpace();
        }
//        if(numPlayers > 3) {
            marketActionSpaceMap.put(MarketActionType.BLUE,new MarketActionSpace(MarketActionType.BLUE));
            marketActionSpaceMap.put(MarketActionType.GRAY,new MarketActionSpace(MarketActionType.GRAY));
//        }
    }

    /**
     * Metodo che inizializza le torri
     */
    private void initializeTowers() {
        towerMap = new HashMap<>();
        towerMap.put(CardType.TERRITORY, new Tower(CardType.TERRITORY, ResourceType.WOOD));
        towerMap.put(CardType.CHARACTER, new Tower(CardType.CHARACTER, ResourceType.STONE));
        towerMap.put(CardType.BUILDING, new Tower(CardType.BUILDING, ResourceType.MILITARY));
        towerMap.put(CardType.VENTURES, new Tower(CardType.VENTURES, ResourceType.COINS));
    }

    private void initializeDecks(){
        excommunication = new Excommunication();
        developmentDeck = new DevelopmentDeck();
        drawStartCards();
    }

    private void drawStartCards() {
        towerMap.get(CardType.TERRITORY).setCards(developmentDeck.drawCards(1,1,CardType.TERRITORY));
        towerMap.get(CardType.CHARACTER).setCards(developmentDeck.drawCards(1,1,CardType.CHARACTER));
        towerMap.get(CardType.BUILDING).setCards(developmentDeck.drawCards(1,1,CardType.BUILDING));
        towerMap.get(CardType.VENTURES).setCards(developmentDeck.drawCards(1,1,CardType.VENTURES));
    }

    /**
     * ritorna la lista dei codici delle tessere scomunica
     * @return lista codici
     */
    public List<String> getExcomCodeList() {
        return excommunication.getCodeList();
    }

    /**
     * metodo che mi ritorna la lista di tutte le carte presenti sulle torre
     * nell'ordine territori, personaggi, edifici, imprese.
     * @return lista di carte sviluppo
     */
    public List<DevelopmentCard> getCompleteListTowersCards() {
        List<DevelopmentCard> list = new ArrayList<>();
        list.addAll(towerMap.get(CardType.TERRITORY).getCards());
        list.addAll(towerMap.get(CardType.CHARACTER).getCards());
        list.addAll(towerMap.get(CardType.BUILDING).getCards());
        list.addAll(towerMap.get(CardType.VENTURES).getCards());
        return list;
    }


    public void initializeTurn(int period, int turn){
        towerMap.forEach(((cardType, tower) -> tower.removeFamilyMembers()));
        harvestActionSpace.removeFamilyMember();
        productionActionSpace.removeFamilyMember();
        marketActionSpaceMap.forEach(((marketActionType, marketActionSpace) -> marketActionSpace.removeFamilyMember()));
        councilActionSpace.removeFamilyMembers();
        if (numPlayers > 2) {
            largeHarvestActionSpace.removeFamilyMembers();
            largeProductionActionSpace.removeFamilyMembers();
        }
        setCards(period,turn);
    }

    private void setCards(int period, int turn) {
        towerMap.get(CardType.TERRITORY).setCards(developmentDeck.drawCards(period,turn,CardType.TERRITORY));
        towerMap.get(CardType.CHARACTER).setCards(developmentDeck.drawCards(period,turn,CardType.CHARACTER));
        towerMap.get(CardType.BUILDING).setCards(developmentDeck.drawCards(period,turn,CardType.BUILDING));
        towerMap.get(CardType.VENTURES).setCards(developmentDeck.drawCards(period,turn,CardType.VENTURES));
    }

    /**
     * mi ritorna una lista di familiare in ordine di posizionamento
     * @return lista di familiari
     */
    public List<FamilyMember> getOrder() {
        return councilActionSpace.getFamilyMembers();
    }

    /**
     * metodo che mi dice se posso dare sostegno alla chiesa oppure no
     * @param player giocatore da controllare
     * @return true se posso dare sostegno, false altrimenti
     */
    public boolean canGiveSupport(int period, AbstractPlayer player) {
        int minValue = period+2;
        return player.getPersonalBoard().getQtaResources().get(ResourceType.FAITH) >= minValue;
    }

    /**
     * mi scomunica il giocatore preciso
     * @param period periodo
     * @param player giocatore
     */
    public void excommunicatePlayer(int period, AbstractPlayer player) {
        excommunication.addPlayer(period, player);
    }



    public void activeFirstPeriodExcommunication(Action action, int type) throws NewActionException {
        excommunication.activeFirtsPeriod(action.getPlayer(), type);
    }

    public void activeSecondPeriodExcommunication(Action action)throws NewActionException {
        excommunication.activeSecondPeriod(action.getPlayer());
    }

    public void activeThirdPeriodExcommunication(AbstractPlayer player) throws NewActionException {
        excommunication.activeThirdPeriod(player);
    }

    /**
     * mi crea l'azione da messaggio codificato e dopodiché mi esegue l'azione
     * sullo spazio azione corretto
     * @param msg messaggio a decodificare
     * @param familyMember familiare
     * @throws LorenzoException in caso la mosssa non vada abuon fine
     */
    public void doAction(AbstractPlayer player, MessageAction msg,  FamilyMember familyMember) throws LorenzoException, NewActionException {
        ActionSpaceInterface actionSpace = convertActionMessage(msg);
        if (actionSpace == null)
            throw new LorenzoException("YOU CAN'T DO ACTION HERE; ACTION SPACE ISN'T AVAILABLE");
        int force = familyMember.getValue() + msg.getValue();
        currentAction = new Action(actionSpace, force, familyMember, player);
        currentAction.commitAction();
    }


    public void doNewAction(AbstractPlayer player, MessageNewAction msg) throws LorenzoException, NewActionException {
        ActionSpaceInterface actionSpace = convertActionMessage(msg);
        if (actionSpace == null)
            throw new LorenzoException("YOU CAN'T DO ACTION HERE; ACTION SPACE ISN?T AVAILABLE");
        int force = msg.getValue() + msg.getAdditionalValue();
        currentAction = new Action(actionSpace, force, null, player);
        currentAction.commitAction();
    }

    /**
     * metodo che in base al messaggio come parametro mi ritorna
     * lo spazio azione corretto del mio tabellone
     * @param msg messaggio da convertire
     * @return lo spazio azione corretto.
     */
    private ActionSpaceInterface convertActionMessage(Message msg) {
        switch (msg.getActionSpacesType()) {
            case MARKET:
                return marketActionSpaceMap.get(msg.getMarketActionType());
            case TOWERS:
                int numFloor = msg.getNumFloor();
                return towerMap.get(msg.getCardType()).getFloor(numFloor);
            case COUNCIL:
                return councilActionSpace;
            case SINGLE_HARVEST:
                return harvestActionSpace;
            case LARGE_HARVEST:
                return largeHarvestActionSpace;
            case SINGLE_PRODUCTION:
                return productionActionSpace;
            case LARGE_PRODUCTION:
                return largeProductionActionSpace;
            default:
                return null;
        }
    }
}