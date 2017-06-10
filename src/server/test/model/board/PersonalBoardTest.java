package server.test.model.board;

import api.types.CardType;
import api.types.FamilyMemberType;
import api.types.ResourceType;
import server.main.game_server.exceptions.LorenzoException;
import server.main.model.action_spaces.Action;
import server.main.model.board.DevelopmentCard;
import server.main.model.board.PersonalBoard;
import server.main.model.effects.development_effects.Effect;
import server.main.model.effects.development_effects.FixedIncrementEffect;
import server.main.model.fields.Field;
import server.main.model.fields.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Andrea
 * @author Luca
 */
public class PersonalBoardTest {
    private PersonalBoard personalBoard;
    private DevelopmentCard card;
    private Action territoryAction;
    private Action buildingAction;

    @Before
    public void setup() {
        personalBoard = new PersonalBoard(1);
        List<Field> cost = new ArrayList<>();
        cost.add(new Resource(-3, ResourceType.SERVANTS));
        List<Effect> quick = new ArrayList<>();
        quick.add(new FixedIncrementEffect(new Resource(4, ResourceType.STONE)));
        List<Effect> permanent = new ArrayList<>();
        permanent.add(new FixedIncrementEffect(new Resource(2, ResourceType.COINS)));
        card = new DevelopmentCard(CardType.TERRITORY, "valle", cost, quick, permanent, 1);
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
    public void getPersonalCardsMap() {
        personalBoard.addCard(card);
        assertEquals(card.getName(), personalBoard.getPersonalCardsMap().get(card.getType()).get(0));
    }

    @Test
    public void activeTerritoriesEffects() {
    }

    @Test
    public void activeBuildingsEffects() {
    }

    @Test
    public void activeCharacterEffects() {
    }

    @Test
    public void calculateVictoryPoints() {
    }
}