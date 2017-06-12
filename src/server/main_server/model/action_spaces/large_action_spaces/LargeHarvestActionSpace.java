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
 * classe che mi rappresenta lo spazio azione raccolta grande.
 */
public class LargeHarvestActionSpace extends LargeActionSpace{
    private static final int decrement = -3;
    private List<Effect> bonusEffectList;

    public LargeHarvestActionSpace() {
        super();
        initializeBonus();
    }

    private void initializeBonus() {
        bonusEffectList = new ArrayList<>();
        bonusEffectList.add(new FixedIncrementEffect(
                new Resource(1, ResourceType.WOOD)
        ));
        bonusEffectList.add(new FixedIncrementEffect(
                new Resource(1, ResourceType.STONE)
        ));
        bonusEffectList.add(new FixedIncrementEffect(
                new Resource(1, ResourceType.SERVANTS)
        ));
    }

    @Override
    public void doAction(Action action) throws LorenzoException, NewActionException {
        action.modifyValue(decrement); //decremento la forza dell'azione di 3, effetto
        checkValue(action.getValue()); //controllo se ho la forza sufficiente
        addFamilyMember(action.getFamilyMember());
        for (Effect effect : bonusEffectList){
            effect.active(action.getPlayer());
        }

        //attivo gli effetti delle carte territorio
        action.getPlayer().getPersonalBoard().activeTerritoriesEffects(action);
    }
}
