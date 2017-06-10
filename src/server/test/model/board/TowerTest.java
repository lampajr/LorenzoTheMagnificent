package server.test.model.board;

import api.types.CardType;
import api.types.ResourceType;
import client.main.client.ClientRMI;
import server.main.game_server.rmi.ServerRMI;
import server.main.model.Game;
import server.main.model.board.DevelopmentCard;
import server.main.model.board.Tower;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea
 * @author Luca
 */
public class TowerTest {
    private Tower tower;
    private List<DevelopmentCard> list;
    private Game game;

    @BeforeClass
    public void setupClass() throws RemoteException, AlreadyBoundException, NotBoundException {
        ServerRMI serverRMI = new ServerRMI();
        LocateRegistry.createRegistry(1099).bind("serverRMI", serverRMI);
        ClientRMI client = new ClientRMI("andrea", "lol");
        ClientRMI client2 = new ClientRMI("luca", "boss");
        client.login();
        client.startGame(2);
        client2.login();
        client2.startGame(2);
    }

    @Before
    public void setup() {
        tower = new Tower(CardType.TERRITORY, ResourceType.COINS);
        list = new ArrayList<>();
        DevelopmentCard card1 = new DevelopmentCard(CardType.TERRITORY, "uno", null, null, null, 1);
        DevelopmentCard card2 = new DevelopmentCard(CardType.TERRITORY, "due", null, null, null, 1);
        DevelopmentCard card3 = new DevelopmentCard(CardType.TERRITORY, "tre", null, null, null, 1);
        DevelopmentCard card4 = new DevelopmentCard(CardType.TERRITORY, "quattro", null, null, null, 1);
        list.add(card1);
        list.add(card2);
        list.add(card3);
        list.add(card4);
    }

    @Test
    public void setGetCards() throws Exception {
        tower.setCards(list);
        Assert.assertEquals("uno", tower.getCards().get(3).getName());
        Assert.assertEquals("due", tower.getCards().get(2).getName());
        Assert.assertEquals("tre", tower.getCards().get(1).getName());
        Assert.assertEquals("quattro", tower.getCards().get(0).getName());
    }

    @Test
    public void removeFamilyMembers() throws Exception {

    }

    @Test
    public void getFloor() throws Exception {
        Assert.assertEquals(1, tower.getFloor(0).getMinValue());
        Assert.assertEquals(3, tower.getFloor(1).getMinValue());
        Assert.assertEquals(5, tower.getFloor(2).getMinValue());
        Assert.assertEquals(7, tower.getFloor(3).getMinValue());
    }

    @Test
    public void checkOtherMyFamilyMember() throws Exception {

    }
}