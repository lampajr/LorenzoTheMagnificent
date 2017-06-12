package server.main_server.model.action_spaces.large_action_spaces;

import server.main_server.game_server.exceptions.LorenzoException;
import server.main_server.game_server.exceptions.NewActionException;
import server.main_server.model.action_spaces.Action;
import server.main_server.model.action_spaces.ActionSpaceInterface;
import server.main_server.model.board.FamilyMember;
import server.main_server.model.effects.development_effects.Effect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Luca
 * @author Andrea
 *
 * Generalizza gli spazi azione multipli
 */
public abstract class LargeActionSpace implements ActionSpaceInterface {
    private final int minValue;
    private List<FamilyMember> familyMembers;
    private List<Effect> effects;

    LargeActionSpace(){
        this.minValue = 1;
        this.familyMembers = new ArrayList<>();
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public void setEffect(List<Effect> effect) {
        this.effects = effect;
    }

    public List<FamilyMember> getFamilyMembers(){
        return familyMembers;
    }

    public void addFamilyMember(FamilyMember familyMember){
        this.familyMembers.add(familyMember);
    }

    public void removeFamilyMembers(){
        this.familyMembers = new ArrayList<>();
    }

    private int getMinValue() {
        return minValue;
    }

    /**
     * metodo che mi controlla se l'azione ha forza sufficiente per attivare lo spazio azione.
     * @param actionValue valore dell'azione
     * @throws LorenzoException in caso non ce l'abbia viene lanciata una Lorenzo exception
     */
    void checkValue(int actionValue) throws LorenzoException {
        if (getMinValue() > actionValue)
            throw new LorenzoException("You haven't enough force to do this action!!");
    }

    @Override
    public abstract void doAction(Action action) throws LorenzoException, NewActionException;
}
