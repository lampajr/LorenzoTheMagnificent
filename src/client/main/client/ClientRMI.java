package client.main.client;

import api.PlayerInterface;
import api.ServerInterface;
import api.messages.MessageAction;
import api.messages.MessageNewAction;
import api.types.ResourceType;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Classe che sarà condivisa i cui metodi saranno chiamati dal server per notificare e modificare l' interfaccia utente
 * @author Andrea
 * @author Luca
 */
public class ClientRMI extends AbstractClient {
    private static ClientRMI instanceRMI;
    private static final String SERVER = "serverRMI";
    private ServerInterface server;
    private PlayerInterface serverGame;
    private Registry registry;


    /**
     * costruttore che prende in ingresso username e password del giocatore
     * @param username username del giocatore
     * @param password password del giocatore
     * @throws RemoteException
     */
    public ClientRMI(String username, String password) throws RemoteException {
        super(username, password);
    }

    /**
     * Metodo che notifica al server che l' utente sta compiendo un azione su uno spazio azione, sotto forma
     * di messaggio, chiamando il metodo doAction dell' intefaccia serverGame
     * (Va parametrizzato per scegliere quale azione compiere e con quale familiare)
     */
    @Override
    public void doAction(MessageAction msg, int servantsToPay) {
        try {
            if (servantsToPay <= getQtaResource(ResourceType.SERVANTS)) {
                msg.setValue(servantsToPay);
                serverGame.doAction(msg);
            }
            else
                notifyMessage("YOU HAVEN'T ENOUGH SERVANTS");
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void doNewAction(MessageNewAction msg, int servantsToPay) {
        try {
            if (servantsToPay <= getQtaResource(ResourceType.SERVANTS)) {
                msg.setAdditionalValue(servantsToPay);
                serverGame.doNewAction(msg);
            }
            else
                notifyMessage("YOU HAVEN'T ENOUGH SERVANTS");
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * metodo che esegue il login del giocatore, scaricando l' intefaccia del server per poter chiamare il metodo
     * di login su di essa (attraverso getRegistry e Lookup)
     * @return true se il login va a buon fine.
     */
    @Override
    public boolean login() throws NotBoundException {
        try {
            registry = LocateRegistry.getRegistry(1099);
            server = (ServerInterface) registry.lookup(SERVER);
            return server.login(getUsername(), getPassword());
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * richiede al server di accoppiarlo ad una partita, darà sempre esito positivo
     */
    @Override
    public void startGame(int gameMode) {
        try {
            serverGame = (PlayerInterface) server.startGame(getUsername(), gameMode, this);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * metyodo che viene chiamato dal client che notifica al server che la mossa è finita, chiamando
     * il metodo endMove sull' interfacci di tipo PlayerInterface (serverGame)
     */
    @Override
    public void endMove() {
        try {
            serverGame.endMove();
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * informo il server in cosa voglio convertire i miei privilegi
     * @param qta qta in cui convertire
     * @param type tipo in cui convertire
     */
    @Override
    public void convertPrivilege(int qta, ResourceType type) {
        try {
            serverGame.convertPrivilege(qta, type);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * abbandono la partita, informo il server
     */
    @Override
    public void surrender() {
        try {
            serverGame.surrender();
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exit() {
        surrender();
        System.exit(0);
    }

    /**
     * metodo che invia al server i risutlati del lancio dei dadi, il quale si occuperà di inviarlo
     * a tutti gli altri giocatori
     * @param orange dado arancione
     * @param white bianco
     * @param black nero
     */
    @Override
    public void shotDice(int orange, int white, int black) {
        try {
            serverGame.shotDice(orange, white, black);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * metodo che rappresenta la scelta riguardante la decisione di farsi scomunicare
     * oppure dare sostegno alla chiesa
     * @param choice true accetto la scomunica, false do sostegno
     */
    @Override
    public void excommunicationChoice(boolean choice) {
        try {
            serverGame.excommunicationChoice(choice);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
