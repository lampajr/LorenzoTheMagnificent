package server.main_server.model.action_spaces.single_action_spaces;

import server.main_server.game_server.exceptions.LorenzoException;
import server.main_server.game_server.exceptions.NewActionException;
import server.main_server.model.action_spaces.Action;
import server.main_server.model.action_spaces.ActionSpaceInterface;
import server.main_server.model.board.FamilyMember;
import server.main_server.model.effects.development_effects.Effect;


/**
 * @author Luca
 * @author Andrea
 *
 * Generalizza gli spazi azione singoli
 */
public abstract class ActionSpace implements ActionSpaceInterface {
    private FamilyMember familyMember;
    private final int minValue;
    private Effect effect;

    ActionSpace(int minValue){
        this.minValue = minValue;
        familyMember = null;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public FamilyMember getFamilyMember(){
        return familyMember;
    }

    public void setFamilyMember(FamilyMember familyMember) throws LorenzoException {
        if (this.familyMember != null)
            throw new LorenzoException("This action space is full!!");
        this.familyMember = familyMember;
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

    public void removeFamilyMember(){
        this.familyMember=null;
    }

    public int getMinValue() {
         return minValue;
     }

     @Override
    public abstract void doAction(Action action) throws LorenzoException, NewActionException;
}
