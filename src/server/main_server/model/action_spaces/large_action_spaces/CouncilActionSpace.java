package server.main_server.model.action_spaces.large_action_spaces;


import api.types.ResourceType;
import server.main_server.game_server.exceptions.LorenzoException;
import server.main_server.game_server.exceptions.NewActionException;
import server.main_server.model.action_spaces.Action;
import server.main_server.model.effects.development_effects.Effect;
import server.main_server.model.effects.development_effects.FixedIncrementEffect;
import server.main_server.model.fields.Resource;

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

    public CouncilActionSpace(){
        super();
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
        checkValue(action.getValue()); //controllo se ho la forza sufficiente
        addFamilyMember(action.getFamilyMember());
        for (Effect effect : bonusEffectList) {
            effect.active(action.getPlayer());
        }
    }


}
