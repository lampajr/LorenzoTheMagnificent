package server.main.model.board;

import api.types.CardType;
import api.types.FamilyMemberType;
import api.types.ResourceType;
import server.main.game_server.exceptions.LorenzoException;
import server.main.model.action_spaces.single_action_spaces.ActionSpace;
import server.main.model.action_spaces.single_action_spaces.FloorActionSpace;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Luca
 * @author Andrea
 *
 * classe che mi rappresenta una singola torre del mio tabellone
 * esso sarà composto da 4 piani ciascuno dei quali sarà uno
 * spazio azione, ciascuna torre avrà carte sviluppo di un solo tipo
 */
public class Tower {
    private FloorActionSpace[] floorActionSpaces;
    private CardType towerType;

    public Tower(CardType type, ResourceType resourceTypeQuickEffect){
        this.towerType = type;
        this.floorActionSpaces = new FloorActionSpace[4];
        int value = 1;
        for(int i = 0 ; i<4 ; i++){
            //devo aggiungere gli effetti ai piani 3 e 4 di ciascuna torre
            floorActionSpaces[i] = new FloorActionSpace(value, towerType, resourceTypeQuickEffect, this);
            value += 2;
        }
    }

    /**
     * setta le carte di ogni piano
     * @param DevelopmentCards lista delle carte da settare
     */
    public void setCards(List<DevelopmentCard> DevelopmentCards){
        for(int i = 3 ; i>=0 ; i--){
            floorActionSpaces[i].setDevelopmentCard(DevelopmentCards.get(i));
        }
    }

    /**
     * metodo che elimina tutti i familiari alla fine di ogni turno
     */
    public void removeFamilyMembers(){
        for(int i = 0 ; i<4 ; i++){
            floorActionSpaces[i].removeFamilyMember();
        }
    }

    public ActionSpace getFloor(int numFloor) {
        return floorActionSpaces[numFloor];
    }

    public List<DevelopmentCard> getCards() {
        List<DevelopmentCard> list = new ArrayList<>();
        for (int i=3; i >= 0; i--){
            list.add(floorActionSpaces[i].getDevelopmentCard());
        }
        return list;
    }

    public void checkOtherMyFamilyMember(FloorActionSpace floor, FamilyMember familyMember) throws LorenzoException {
        if (familyMember != null) {
            for (FloorActionSpace element : floorActionSpaces) {
                if (floor != element) {
                    if (element.getFamilyMember() != null){
                        if (element.getFamilyMember().getPersonalBoard().getId() == familyMember.getPersonalBoard().getId()){
                            if (!(element.getFamilyMember().getType() == FamilyMemberType.NEUTRAL_DICE || familyMember.getType() == FamilyMemberType.NEUTRAL_DICE))
                                throw new LorenzoException("hai già posizionato un familiare su questa torre!");
                        }
                    }
                }
            }
        }
    }
}
