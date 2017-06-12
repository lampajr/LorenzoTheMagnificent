package server.test.model.board;

import api.types.CardType;
import api.types.FamilyMemberType;
import api.types.ResourceType;
import org.junit.Before;
import org.junit.Test;
import server.main_server.game_server.exceptions.LorenzoException;
import server.main_server.game_server.rmi.PlayerRMI;
import server.main_server.model.action_spaces.Action;
import server.main_server.model.action_spaces.single_action_spaces.HarvestActionSpace;
import server.main_server.model.board.DevelopmentCard;
import server.main_server.model.board.PersonalBoard;
import server.main_server.model.effects.development_effects.ActionValueModifyingEffect;
import server.main_server.model.effects.development_effects.AreaActivationEffect;
import server.main_server.model.effects.development_effects.Effect;
import server.main_server.model.effects.development_effects.FixedIncrementEffect;
import server.main_server.model.fields.Field;
import server.main_server.model.fields.Resource;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static api.types.ResourceType.COINS;
import static api.types.ResourceType.VICTORY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author Andrea
 * @author Luca
 */
public class PersonalBoardTest {
    private PersonalBoard personalBoard;
    private DevelopmentCard card;
    private DevelopmentCard characterCard;
    private PlayerRMI player;

    @Before
    public void setupPlayer() throws RemoteException {
        player = new PlayerRMI("andrea");
        player.createPersonalBoard(1);
        personalBoard = player.getPersonalBoard();
    }

    @Before
    public void setupTerritoryCard() {
        List<Field> cost = new ArrayList<>();
        cost.add(new Resource(-3, ResourceType.SERVANTS));
        List<Effect> quick = new ArrayList<>();
        quick.add(new FixedIncrementEffect(new Resource(4, ResourceType.STONE)));
        List<Effect> permanent = new ArrayList<>();
        permanent.add(new AreaActivationEffect(new FixedIncrementEffect(new Resource(2, COINS)), 6));
        card = new DevelopmentCard(CardType.TERRITORY, "valle", cost, quick, permanent, 1);
    }

    @Before
    public void setupCharacterCard() {
        List<Effect> permanent = new ArrayList<>();
        permanent.add(new ActionValueModifyingEffect(new HarvestActionSpace(), 2));
        characterCard = new DevelopmentCard(CardType.CHARACTER, "valle", null, null, permanent, 1);
    }

    @Test
    public void setDiceValues() {
        personalBoard.setDiceValues(5,4,3);
        assertEquals(5L, personalBoard.getFamilyMember(FamilyMemberType.ORANGE_DICE).getValue());
        assertEquals(4L, personalBoard.getFamilyMember(FamilyMemberType.WHITE_DICE).getValue());
        assertEquals(3L, personalBoard.getFamilyMember(FamilyMemberType.BLACK_DICE).getValue());
    }

    @Test
    public void addCard() {
        personalBoard.addCard(card);
        assertEquals(card, personalBoard.getCardsList(card.getType()).get(0));
    }

    @Test
    public void modifyResources() {
        personalBoard.modifyResources(new Resource(5, ResourceType.SERVANTS));
        personalBoard.modifyResources(new Resource(-1, ResourceType.WOOD));
        assertEquals(8L, personalBoard.getQtaResources().get(ResourceType.SERVANTS).intValue());
        assertEquals(1L, personalBoard.getQtaResources().get(ResourceType.WOOD).intValue());
    }

    @Test
    public void resetResource() {
        personalBoard.resetResource(ResourceType.SERVANTS);
        assertEquals(0, personalBoard.getQtaResources().get(ResourceType.SERVANTS).intValue());
    }

    @Test(expected = LorenzoException.class)
    public void checkResourcesError() throws Exception {
        personalBoard.checkResources(new Resource(4, ResourceType.SERVANTS));
    }

    @Test
    public void getFamilyMember() {
        assertEquals(FamilyMemberType.ORANGE_DICE, personalBoard.getFamilyMember(FamilyMemberType.ORANGE_DICE).getType());
    }

    @Test
    public void removeAllFamilyMembers() {
        personalBoard.getFamilyMember(FamilyMemberType.ORANGE_DICE).setPositioned(true);
        personalBoard.removeAllFamilyMembers();
        assertFalse(personalBoard.getFamilyMember(FamilyMemberType.ORANGE_DICE).isPositioned());
    }

    @Test
    public void getQtaResources() {
        assertEquals(2L, personalBoard.getQtaResources().get(ResourceType.WOOD).intValue());
    }

    @Test
    public void getPersonalCardsMap() throws LorenzoException {
        card.setPlayer(player);
        assertEquals(card.getName(), personalBoard.getPersonalCardsMap().get(card.getType()).get(0));
    }

    @Test
    public void activeCardsEffects() throws LorenzoException {
        card.setPlayer(player);
        personalBoard.activeTerritoriesEffects(new Action(null, 6, null, player));
        assertEquals(7, personalBoard.getQtaResources().get(COINS).intValue());
    }

    @Test
    public void noActiveCardsEffects() throws LorenzoException {
        card.setPlayer(player);
        personalBoard.activeTerritoriesEffects(new Action(null, 4, null, player));
        assertEquals(5, personalBoard.getQtaResources().get(COINS).intValue());
    }

    @Test
    public void activeCharacterEffects() throws LorenzoException {
        characterCard.setPlayer(player);
        card.setPlayer(player);
        personalBoard.activeCharacterEffects(new Action(new HarvestActionSpace(), 2,null, player));
        assertEquals(4, personalBoard.getCurrentAction().getValue());
    }

    @Test
    public void calculateVictoryPoints() {
        // + 6
        personalBoard.addCard(characterCard);
        personalBoard.addCard(characterCard);
        personalBoard.addCard(characterCard);
        // + 1
        personalBoard.addCard(card);
        personalBoard.addCard(card);
        personalBoard.addCard(card);
        // + 5
        personalBoard.modifyResources(new Resource(5, VICTORY));
        // + 2 dal totale delle risorse
        assertEquals("victory",14, personalBoard.calculateVictoryPoints());
    }
}