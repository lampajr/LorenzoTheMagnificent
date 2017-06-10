package server.main.model;

import api.PlayerInterface;
import api.messages.MessageAction;
import api.messages.MessageNewAction;
import api.types.CardType;
import api.types.Phases;
import api.types.ResourceType;
import server.main.MainServer;
import server.main.game_server.AbstractPlayer;
import server.main.game_server.exceptions.LorenzoException;
import server.main.game_server.exceptions.NewActionException;
import server.main.model.action_spaces.Action;
import server.main.model.board.Board;
import server.main.model.board.FamilyMember;
import server.main.model.fields.Resource;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author Luca
 * @author Andrea
 *
 * Classe che gestisce il comportamento della singola partita
 */
public class Game {
    private boolean isStarted;
    private int numPlayers;
    private int period=1,turn=1,lap=1;
    private Board board;
    private Map<Integer, AbstractPlayer> playerMap;
    private List<AbstractPlayer> turnOrder;
    private AbstractPlayer currentPlayer;
    private Phases phase = Phases.ACTION;
    //1->random; 2->2giocatori; 3->3giocatori; 4->4giocatori
    private int gameMode; //automaticamente mi indica il numero di giocatori che devo attendere


    public Game(int gameMode) {
        this.gameMode = gameMode;
        this.numPlayers = 0;
        playerMap = new HashMap<>();
        turnOrder = new ArrayList<>();
    }

    public AbstractPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * metodo che mi aggiunge un giocatore alla partita.
     * @param abstractPlayer giocatore da aggiungere alla partita
     */
    public void addPlayer(AbstractPlayer abstractPlayer) {
        numPlayers++;
        playerMap.put(numPlayers , abstractPlayer);
        abstractPlayer.createPersonalBoard(numPlayers);
        turnOrder.add(abstractPlayer);
        //currentPlayer = turnOrder.get(0);
        if(numPlayers == 2 && gameMode == MainServer.RANDOM)
            new Timer(10); //il parametro indica i secondi di attesa
        if(checkMaxNumberReached())
            new Timer(3);
    }

    private boolean checkMaxNumberReached(){
        if (gameMode != MainServer.RANDOM){
            if (numPlayers == gameMode)
                return true;
        }
        else {
            if (numPlayers == 4)
                return true;
        }
        return false;
    }


    /**
     * metodo che mi fa partire la partita
     */
    private void startGame() {
        this.isStarted = true;
        board = new Board(numPlayers);
        phase = Phases.ACTION;
        playerMap.forEach(((integer, player) -> {
            player.initializeBoard(board.getCompleteListTowersCards());
            Map<Integer, String> opponentsMap = new HashMap<>();
            playerMap.forEach((opponentId, opponent) -> {
                if (opponent != player) {
                    opponentsMap.put(opponentId, opponent.getUsername());
                }
            });
            player.gameIsStarted(opponentsMap, board.getExcomCodeList());
            player.sendOrder(turnOrder);
        }));
        currentPlayer = turnOrder.get(0);
        currentPlayer.notifyRollDice();
        currentPlayer.isYourTurn();
    }

    /**
     * metodo per il lancio dei dadi, controlla se è il tuo turno..
     * @param player giocatore che ha appena tirato i dadi
     */
    public void shotDice(AbstractPlayer player, int orange, int white, int black) {
        try{
            checkTurn(player);
            if((player != turnOrder.get(0)) || (lap != 1) || (phase != Phases.ACTION))
                player.notifyError("DICES HAVE ALREADY BEEN ROLLED");
            else {
                turnOrder.forEach(abstractPlayer -> abstractPlayer.setDiceValues(orange, white, black));
                currentPlayer.isYourTurn();
            }
        }
        catch (LorenzoException e) {
            player.notifyError(e.getMessage());
        }
    }


    /**
     * mi ottiene l'id del giocatore
     * @param player giocatore di cui voglio conoscere l'id
     * @return l'id del giocatore, eventualmente -1 se il giocatore non è della partita
     */
    public int getId(PlayerInterface player){
        for(int i = 1; i<=numPlayers ; i++){
            if(player == playerMap.get(i))
                return i;
        }
        return -1;
    }

    /**
     * mi dice se la partita è al completo o comunque se è già cominciata!
     * @return true se la partita è al completo, false altrimenti
     */
    public boolean isFull(){
        return isStarted;
    }

    /**
     * metodo che controlla se è il turno del giocatore passato come parametro
     * @param player giocatore da controllare
     * @throws LorenzoException
     */
    public void checkTurn(AbstractPlayer player) throws LorenzoException {
        if (player != currentPlayer)
            throw new LorenzoException("IS NOT YOUR TURN");
    }

    /**
     * controlla se il familiare è già posizionato
     * @param familyMember familiare da controllare
     * @throws LorenzoException
     */
    private void isAlreadyPositioned(FamilyMember familyMember) throws LorenzoException {
        if (familyMember.isPositioned())
            throw new LorenzoException("FAMLY MEMBER ALREADY POSITIONED");
    }

    /**
     * controlla se la partita è cominciata
     * @throws LorenzoException
     */
    private void isStarted() throws LorenzoException {
        if (!isStarted)
            throw new LorenzoException("GAME NOT STARTED YET");
    }

    /**
     * mi scomunica il giocatore preciso
     * @param player giocatore
     */
    public void excommunicatePlayer(AbstractPlayer player) {
        if (phase == Phases.EXCOMMUNICATION){
            try {
                checkTurn(player);
                board.excommunicatePlayer(period, player);
                player.updateMove(null);
                endMove(player);
            }
            catch (LorenzoException e) {
               player.notifyError(e.getMessage());
            }
        }
        else {
            player.notifyError("YOU AREN'T IN THE EXCOMMUNICATION TURN");
        }
    }

    /**
     * metodo che rappresenta l'azione di dare sostegno alla chiesa, se può, se no
     * mi scomunica automaticamente il giocatore
     * @param player giocatore
     */
    public void giveChurchSupport(AbstractPlayer player) {
        if (phase == Phases.EXCOMMUNICATION){
            try {
                checkTurn(player);
                if (board.canGiveSupport(period, player)) {
                    int faithPoints = player.getPersonalBoard().getQtaResources().get(ResourceType.FAITH);
                    Resource res = new Resource(faithPoints, ResourceType.VICTORY);
                    player.getPersonalBoard().modifyResources(res);
                    player.getPersonalBoard().resetResource(ResourceType.FAITH);
                    player.updateMove(null);
                    endMove(player);
                }
                else {
                    excommunicatePlayer(player);
                }
            } catch (LorenzoException e) {
                player.notifyError(e.getMessage());
            }
        }
        else {
            player.notifyError("YOU AREN'T IN THE EXCOMMUNICATION TURN");
        }
    }

    /**
     * attiva il primo periodo
     * @param action azione
     * @param type tipo di effetto da verificare
     * @throws NewActionException
     */
    public void activeFirstPeriodExcommunication(Action action, int type) throws NewActionException {
        board.activeFirstPeriodExcommunication(action, type);
    }

    /**
     * attiva effetti del secondo periodo
     * @param action azione
     * @throws NewActionException
     */
    public void activeSecondPeriodExcommunication(Action action) throws NewActionException {
        board.activeSecondPeriodExcommunication(action);
    }

    /**
     * attiva effetti delle tessere scomunica del terzo periodo
     * @param player giocatore su cui attivare la scomunica
     */
    public void activeThirdPeriodExcommunication(AbstractPlayer player) {
        try {
            board.activeThirdPeriodExcommunication(player);
        }
        catch (NewActionException e) {
            e.printStackTrace();
        }
    }

    /**
     * metodo che mi controlla se è il turno del mio giocatore e se il familiare
     * è già stato posizionato o meno
     * @param player giocatore che la esegue
     * @param msg messggio da decodificare
     * @param familyMember familiare da spostare, già ricavato dall classe che lo invoca
     */
    public void doAction(AbstractPlayer player, MessageAction msg, FamilyMember familyMember) {
        if (phase == Phases.ACTION){
            try {
                isStarted();
                checkTurn(player);
                isAlreadyPositioned(familyMember);
                board.doAction(player, msg, familyMember);
                player.getPersonalBoard().modifyResources(new Resource(-msg.getValue(), ResourceType.SERVANTS)); //mi toglie gli eventuali servitori che ho pagato
                familyMember.setPositioned(true);
                player.updateMove(msg);
                endMove(player); //mi esegue la fine de turno
            }
            catch (NewActionException e) {
                player.getPersonalBoard().modifyResources(new Resource(-msg.getValue(), ResourceType.SERVANTS)); //mi toglie gli eventuali servitori che ho pagato
                familyMember.setPositioned(true);familyMember.setPositioned(true);
                player.updateMove(msg);
                //ho attivato un effetto che mi fa fare una nuova azione, perciò non è finito il mio turno
                phase = Phases.NEW_ACTION;
            }
            catch (LorenzoException e) {
                player.notifyError(e.getMessage());
            }
        }
        else {
            player.notifyError("YOU AREN'T IN THE ACTION PHASE");
        }
    }

    /**
     * notifica a tutti i giocatori ,tranne a quello che ha eseguito la mossa, che cosa ha modificato la mossa appena
     * eseguita sul tabellone
     * @param player giocatore che ha eseguito la mossa
     * @param id id dello stesso giocatore
     * @param personalcardsMap mappa delle carte personali
     * @param qtaResourcesMap mappa delle risorse
     */
    public void notifyAllPlayers(AbstractPlayer player, int id, Map<CardType, List<String>> personalcardsMap, Map<ResourceType, Integer> qtaResourcesMap, MessageAction msg) {
        turnOrder.stream().filter(differentFrom(player))
                .forEach(opponent -> opponent.updateOpponentMove(id, personalcardsMap, qtaResourcesMap, msg));
    }

    /**
     * notifica tutti i giocatori che il player è stato scomunicato
     * @param player giocatore che è stato scomunicato
     * @param period periodo
     */
    public void notifyAllPlayers(AbstractPlayer player, int period) {
        turnOrder.stream().filter(differentFrom(player))
                .forEach(opponent -> opponent.opponentExcommunicate(player.getIdPlayer(), period));
    }

    /**
     * rappresenta l'esecuzione di una nuova azione (senza familiare)
     * mi controlla se sono nella fase corretta e se è il mio turno, dopodiché passa
     * l'esecuzione della nuova azione al tabellone
     * @param player giocatore che sta eseguendo la nuova azione
     * @param msg messaggio codificato dell'azione
     */
    public void doNewAction(AbstractPlayer player, MessageNewAction msg) {
        if (phase == Phases.NEW_ACTION) {
            try {
                checkTurn(player);
                board.doNewAction(player, msg);
                //mi toglie gli eventuali servitori che ho pagato
                player.getPersonalBoard().modifyResources(new Resource(-msg.getAdditionalValue(), ResourceType.SERVANTS));
                player.updateMove(null);
                phase = Phases.ACTION;
                endMove(player);
            }
            catch (LorenzoException e) {
                player.notifyError(e.getMessage());
            }
            catch (NewActionException e) {
                //mi toglie gli eventuali servitori che ho pagato
                player.getPersonalBoard().modifyResources(new Resource(-msg.getValue(), ResourceType.SERVANTS));
                player.updateMove(null);
                //ho attivato un effetto che mi fa fare una nuova azione, perciò non è finito il mio turno
                phase = Phases.NEW_ACTION;
            }
        }
        else {
            player.notifyError("YOU AREN'T IN THE NEW ACTION PHASE");
        }
    }

    /**
     * viene chiamato dopo che il giocatore ha eseguito la mossa
     * controlla se è finito il giro o no
     * @param player il giocatore che ha terminato la mossa
     */
    public void endMove(AbstractPlayer player) {
        try {
            isStarted();
            checkTurn(player);
            player.notifyEndMove();
            if (phase == Phases.NEW_ACTION)
                phase = Phases.ACTION;
            for(int i = 0 ; i < numPlayers ; i++){
                if(currentPlayer == turnOrder.get(i)) {
                    if (i == numPlayers - 1) {
                        System.out.println("END " + lap + "^ LAP");
                        endLap();
                        return;
                    }
                    else{
                        currentPlayer = turnOrder.get(i+1);
                        if (phase == Phases.ACTION)
                            currentPlayer.isYourTurn();
                        else
                            currentPlayer.isYourExcommunicationTurn();
                        return;
                    }
                }
            }
        }
        catch (NewActionException e) {
            e.printStackTrace();
            //non dovrebbe mai verificarsi
        }
        catch (LorenzoException e) {
            player.notifyError(e.getMessage());
        }

    }

    /**
     * controlla se è finito il turno.
     */
    private void endLap() throws NewActionException {
        if (lap == 1 && phase == Phases.EXCOMMUNICATION){
            lap = 1;
            System.out.println("END EXCOMMUNICATION TURN");
            endTurn();
        }
        else if (lap == 4 && phase == Phases.ACTION){
            lap = 1;
            System.out.println("fine turno " + turn);
            endTurn();
        }
        else{
            //sempre nella fase azione
            lap++;
            currentPlayer = turnOrder.get(0);
            currentPlayer.isYourTurn();
        }
    }

    /**
     * controlla se è finito il periodo, cioè questo è l'ultimo turno di costui
     */
    private void endTurn() throws NewActionException {
        if(turn == 2 && phase == Phases.ACTION){
            turn++;
            phase = Phases.EXCOMMUNICATION;
            currentPlayer = turnOrder.get(0);
            currentPlayer.isYourExcommunicationTurn();
        }
        else if(turn == 3 && phase == Phases.EXCOMMUNICATION) {
            turn = 1;
            phase = Phases.ACTION;
            System.out.println("fine periodo " + period);
            endPeriod();
        }
        else{
            //siamo nella fase azione, nel primo turno
            turn++;
            sortPlayerOrder();
        }
    }


    /**
     * controlla se è l'ultimo periodo, cioè è finita la partita.
     */
    private void endPeriod() throws NewActionException {
        if(period == 3){
            endGame();
        }
        else{
            period ++;
            sortPlayerOrder();
        }
    }


    /**
     * questo metodo controlla lo spazio azione del consiglio e riposiziona nella
     * lista turnOrder il nuovo ordine correto per il nuovo turno.
     */
    private void sortPlayerOrder() {
        List<FamilyMember> familyMembersList = board.getOrder();
        if(!familyMembersList.isEmpty()){
            List<AbstractPlayer> newTurnOrder = new ArrayList<>();
            for(FamilyMember f : familyMembersList){
                for(AbstractPlayer p : turnOrder){
                    if(f == p.getFamilyMember(f.getType())) {
                        boolean isPresent = false;
                        for(PlayerInterface p1 : newTurnOrder){
                            if (p1 == p){
                                isPresent = true;
                            }
                        }
                        if(!isPresent){
                            newTurnOrder.add(p);
                        }
                    }
                }
            }
            if(!(newTurnOrder.size() == turnOrder.size())) {
                for (AbstractPlayer p : turnOrder) {
                    boolean isPresent = false;
                    for (AbstractPlayer newP : newTurnOrder) {
                        if (newP == p)
                            isPresent = true;
                    }
                    if (!isPresent)
                        newTurnOrder.add(p);

                }
            }
            turnOrder = newTurnOrder;
        }
        //inizializza il turno sul tabellone
        board.initializeTurn(period, turn);
        playerMap.values().forEach(((player) -> {
            player.removeAllFamilyMembers();
            player.initializeBoard(board.getCompleteListTowersCards());
            player.sendOrder(turnOrder);
        }));
        currentPlayer = turnOrder.get(0);
        currentPlayer.notifyRollDice();
        currentPlayer.isYourTurn();
    }

    /**
     * metodo chiamato al termine della gara, non fa altro che calcolare
     * tutti i punti vittoria di ciascun giocatore e decretare il vincitore.
     */
    private void endGame() {
        //military points
        Map<AbstractPlayer, Integer> militaryMap = new HashMap<>();
        for (AbstractPlayer player: turnOrder){
            militaryMap.put(player, player.getPersonalBoard().getQtaResources().get(ResourceType.MILITARY));
        }

        List<AbstractPlayer> militaryWinners = new LinkedList<>();
        List<Integer> militaryValues = new LinkedList<>(militaryMap.values());
        Collections.sort(militaryValues);
        for (AbstractPlayer player : turnOrder){
            for(int i=0; i<numPlayers; i++) {
                if (player.getPersonalBoard().getQtaResources().get(ResourceType.MILITARY) == militaryValues.get(i).intValue()) {
                    militaryWinners.add(player);
                    break;
                }
            }
        }
        turnOrder.forEach(this::activeThirdPeriodExcommunication);

        militaryWinners.get(0).getPersonalBoard().modifyResources(new Resource(5, ResourceType.VICTORY));
        militaryWinners.get(1).getPersonalBoard().modifyResources(new Resource(2, ResourceType.VICTORY));

        //victory points
        Map<AbstractPlayer, Integer> victoryMap = new HashMap<>();
        turnOrder.forEach(player -> victoryMap.put(player, player.calculateVictoryPoints()));
        AbstractPlayer winner = turnOrder.get(0);
        for (AbstractPlayer player: turnOrder){
            if(victoryMap.get(winner) < victoryMap.get(player))
                winner = player;
        }
        Map<String, Integer> rankingMap = new HashMap<>();
        victoryMap.forEach((p, point) -> rankingMap.put(p.getUsername(), point));
        winner.youWin(rankingMap);
        turnOrder.stream().filter(differentFrom(winner))
                .forEach(player -> player.youLose(rankingMap));
    }

    /**
     * mi rimuove il giocatore passato come paramentro dalla partita
     * e verifica se la parittiaha ancora un numero sufficiente di giocatori
     * @param player il giocatore che ha abbandonato
     */
    public void removePlayer(AbstractPlayer player) {
        playerMap.remove(player.getIdPlayer());
        turnOrder.remove(player);
        numPlayers--;
        if (isStarted) {
            if (numPlayers==1){
                turnOrder.get(0).youWinByAbandonment();
            }
            else {
                turnOrder.forEach((abstractPlayer -> abstractPlayer.opponentSurrender(player.getIdPlayer())));
            }
        }
    }



    private class Timer extends Thread{
        private final long MIN_INTERVAL_TO_START;
        private int seconds = 0;

        /**
         * costruttore che prende un parametro il quale indica i secondi che devo aspettare prima
         * di far cominciare la partita
         * @param seconds secondi
         */
        public Timer (long seconds) {
            this.MIN_INTERVAL_TO_START = seconds;
            this.start();
        }

        public void run(){
            while (seconds < MIN_INTERVAL_TO_START){
                try {
                    Thread.sleep(1000);
                    seconds++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            startGame();
        }

    }

    /**
     * metodo statico che ritorna un predicato per filtrare la mia lista in modo
     * che mi tenga il giocatore solo se è diverso da quello corrente
     * @param current gioctore corrente da non tenere
     * @return il predicato
     */
    private static Predicate<AbstractPlayer> differentFrom(AbstractPlayer current) {
        return player -> player != current;
    }



    /////// METODI SET e GET AGGIUNTI PER I TEST


    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getLap() {
        return lap;
    }

    public void setLap(int lap) {
        this.lap = lap;
    }

    public Phases getPhase() {
        return phase;
    }

    public void setPhase(Phases phase) {
        this.phase = phase;
    }

    public void setCurrentPlayer(AbstractPlayer player) {
        currentPlayer = player;
    }

    public List<AbstractPlayer> getTurnOrder() {
        return turnOrder;
    }
}
