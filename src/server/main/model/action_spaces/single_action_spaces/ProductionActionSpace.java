package server.main.model.action_spaces.single_action_spaces;

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
 * @author lampa
 */
public class ProductionActionSpace extends ActionSpace{
    private List<Effect> bonusEffectList;

    public ProductionActionSpace(int value){
        super(value);
        initializeBonus();
    }

    /**
     * metodo di servizio che mi inizializza il bonus della zona produzione
     */
    private void initializeBonus() {
        bonusEffectList = new ArrayList<>();
        bonusEffectList.add(new FixedIncrementEffect(
                new Resource(1, ResourceType.MILITARY)
        ));
        bonusEffectList.add(new FixedIncrementEffect(
                new Resource(1, ResourceType.COINS)
        ));
    }

    /**
     * metodo di servizio che richiamo quando creo lo spazio azione raccolta
     */
    private void initializeHarvestBonus(){
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
        if (getMinValue() > action.getValue())
            throw new LorenzoException("non hai abbastanza forza per eseguire l'azione");

        setFamilyMember(action.getFamilyMember());
        for(Effect effect : bonusEffectList)
            effect.active(action.getPlayer());


        //attivo gli effetti delle carte territorio
        action.getPlayer().getPersonalBoard().activeBuildingsEffects(action);
    }
}
