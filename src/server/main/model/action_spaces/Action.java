package server.main.model.action_spaces;

import server.main.game_server.AbstractPlayer;
import server.main.game_server.exceptions.LorenzoException;
import server.main.game_server.exceptions.NewActionException;
import server.main.model.board.FamilyMember;

/**
 * @author Luca
 * @author Andrea
 *
 * classe che mi rappresenta l'azione che possono effettuare
 * i vari giocatori.
 */
public class Action {
    private final FamilyMember familyMember;
    private final ActionSpaceInterface actionSpace;
    private int value;
    private final AbstractPlayer player;

    public Action(ActionSpaceInterface actionSpace, int value, FamilyMember familyMember, AbstractPlayer player){
        this.actionSpace = actionSpace;
        this.value = value;
        this.familyMember = familyMember;
        this.player = player;
    }

    public FamilyMember getFamilyMember() {
        return familyMember;
    }

    public ActionSpaceInterface getActionSpace() {
        return actionSpace;
    }

    public void modifyValue(int modify) {
        this.value += modify;
    }

    public int getValue() {
        return value;
    }

    public AbstractPlayer getPlayer() {
        return player;
    }

    /**
     * metodo che mi esegue l'azione, chiamando il metodo sullo spazio
     * azione che ha come attributo.
     */
    public void commitAction() throws LorenzoException, NewActionException {
        player.getPersonalBoard().activeCharacterEffects(this);
        player.activeExcommunicationEffects(this, 1);
        player.activeExcommunicationEffects(this);
        //dopo che ho attivato gli effetti delle carte personaggio
        this.actionSpace.doAction(this);
    }
}
