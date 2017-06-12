package server.main_server.model.action_spaces.single_action_spaces;

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
 * classe che mi identifica una zona del mio tabellone
 * essa può essere di raccolta o di produzione (si usa l'enumerazione)
 */
public class HarvestActionSpace extends ActionSpace {
    private List<Effect> bonusEffectList;

    public HarvestActionSpace(){
        super(1);
        initializeBonus();
    }

    /**
     * metodo di servizio che mi inizializza il bonus della zona produzione
     */
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
        checkValue(action.getValue()); //controllo se ho la forza necessaria
        setFamilyMember(action.getFamilyMember());
        for(Effect effect : bonusEffectList)
            effect.active(action.getPlayer());

        //attivo gli effetti delle carte territorio
        action.getPlayer().getPersonalBoard().activeTerritoriesEffects(action);
    }

}