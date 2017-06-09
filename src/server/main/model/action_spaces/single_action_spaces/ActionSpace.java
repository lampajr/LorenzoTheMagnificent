package server.main.model.action_spaces.single_action_spaces;

import server.main.game_server.exceptions.LorenzoException;
import server.main.game_server.exceptions.NewActionException;
import server.main.model.action_spaces.Action;
import server.main.model.action_spaces.ActionSpaceInterface;
import server.main.model.board.FamilyMember;
import server.main.model.effects.development_effects.Effect;


/**
 * @author Luca
 * @author Andrea
 *
 * Generalizza gli spazi azione singoli
 */
public abstract class ActionSpace implements ActionSpaceInterface {
    private FamilyMember familyMember;
    private int minValue;
    private Effect effect;

    public ActionSpace(int minValue){
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
            throw new LorenzoException("lo spazio azione è già occupato");
        this.familyMember = familyMember;
    }

    public void removeFamilyMember(){
        this.familyMember=null;
    }

     public int getMinValue() {
         return minValue;
     }

     public abstract void doAction(Action action) throws LorenzoException, NewActionException;
}
