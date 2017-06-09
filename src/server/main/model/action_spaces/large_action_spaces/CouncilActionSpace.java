package server.main.model.action_spaces.large_action_spaces;


import api.types.ResourceType;
import server.main.game_server.exceptions.LorenzoException;
import server.main.game_server.exceptions.NewActionException;
import server.main.model.action_spaces.Action;
import server.main.model.effects.development_effects.Effect;
import server.main.model.effects.development_effects.FixedIncrementEffect;
import server.main.model.fields.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Luca
 * @author Andrea
 *
 * classe che mi identifica il palazzo del consiglio
 */
public class CouncilActionSpace extends LargeActionSpace {
    private List<Effect> bonusEffectList;

    public CouncilActionSpace(int value){
        super(value);
        initializeBonus();
    }

    private void initializeBonus() {
        bonusEffectList = new ArrayList<>();
        bonusEffectList.add(new FixedIncrementEffect(
                new Resource(1, ResourceType.COINS)
        ));
        bonusEffectList.add(new FixedIncrementEffect(
                new Resource(1, ResourceType.PRIVILEGE)
        ));
    }

    @Override
    public void doAction(Action action) throws LorenzoException, NewActionException {
        if (getValue() > action.getValue())
            throw new LorenzoException("non hai abbastanza forza!!");

        addFamilyMember(action.getFamilyMember());
        for (Effect effect : bonusEffectList) {
            effect.active(action.getPlayer());
        }
    }


}
