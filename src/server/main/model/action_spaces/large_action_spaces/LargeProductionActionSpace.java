package server.main.model.action_spaces.large_action_spaces;

import api.types.ResourceType;
import server.main.game_server.exceptions.LorenzoException;
import server.main.game_server.exceptions.NewActionException;
import server.main.model.action_spaces.Action;
import server.main.model.effects.development_effects.Effect;
import server.main.model.effects.development_effects.FixedIncrementEffect;
import server.main.model.fields.Resource;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Luca
 * @author Andrea
 *
 * classe che mi identifica le zone multiple del mio tabellone
 * che possono essere di produzione e di raccolta
 */
public class LargeProductionActionSpace extends LargeActionSpace {
    private List<Effect> bonusEffectList;

    public LargeProductionActionSpace(int value){
        super(value+3);
        initializeBonus();
    }

    /**
     * metodo che mi inizializza il bonus della zona produzione
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

    @Override
    public void doAction(Action action) throws LorenzoException, RemoteException, NewActionException {
        if(getValue() > action.getValue())
            throw new LorenzoException("la tua azione non ha abbastanza forza!!");

        addFamilyMember(action.getFamilyMember());
        for (Effect effect : bonusEffectList){
            effect.active(action.getPlayer());
        }

        //attivo gli effetti delle carte territorio
        action.getPlayer().getPersonalBoard().activeBuildingsEffects(action);
    }

}
