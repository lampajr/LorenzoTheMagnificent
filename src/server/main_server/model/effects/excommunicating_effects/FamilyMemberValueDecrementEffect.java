package server.main_server.model.effects.excommunicating_effects;

import api.types.FamilyMemberType;
import server.main_server.game_server.AbstractPlayer;
import server.main_server.game_server.exceptions.NewActionException;
import server.main_server.model.action_spaces.Action;
import server.main_server.model.effects.development_effects.Effect;

/**
 * @author Luca
 * @author Andrea
 */
public class FamilyMemberValueDecrementEffect implements Effect {

    @Override
    public void active(AbstractPlayer player) throws NewActionException {
        Action action = player.getPersonalBoard().getCurrentAction();
        if (action.getFamilyMember() != null && action.getFamilyMember().getType() != FamilyMemberType.NEUTRAL_DICE)
            player.getPersonalBoard().getCurrentAction().modifyValue(-1);
    }
}
