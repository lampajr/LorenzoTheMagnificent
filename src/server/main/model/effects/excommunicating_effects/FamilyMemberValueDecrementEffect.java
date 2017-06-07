package server.main.model.effects.excommunicating_effects;

import api.types.FamilyMemberType;
import server.main.game_server.exceptions.NewActionException;
import server.main.model.action_spaces.Action;
import server.main.model.effects.development_effects.Effect;
import server.main.game_server.AbstractPlayer;

import java.rmi.RemoteException;

/**
 * @author Luca
 * @author Andrea
 */
public class FamilyMemberValueDecrementEffect implements Effect {

    @Override
    public void active(AbstractPlayer player) throws RemoteException, NewActionException {
        Action action = player.getPersonalBoard().getCurrentAction();
        if (action.getFamilyMember() != null && action.getFamilyMember().getType() != FamilyMemberType.NEUTRAL_DICE)
            player.getPersonalBoard().getCurrentAction().modifyValue(-1);
    }
}
