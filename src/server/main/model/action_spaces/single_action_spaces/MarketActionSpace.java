package server.main.model.action_spaces.single_action_spaces;

import api.types.MarketActionType;
import api.types.ResourceType;
import server.main.game_server.exceptions.LorenzoException;
import server.main.game_server.exceptions.NewActionException;
import server.main.model.action_spaces.Action;
import server.main.model.effects.development_effects.Effect;
import server.main.model.effects.development_effects.FixedIncrementEffect;
import server.main.model.fields.Resource;

/**
 * @author Luca
 * @author Andrea
 *
 * classe che mi identifica il mercato, il quale potrà essere
 * di 4 tipi identificati da una enumerazione
 */
public class MarketActionSpace extends ActionSpace {
    private MarketActionType type;
    private Effect additionalEffect;

    public MarketActionSpace(MarketActionType marketActionType){
        super(1);
        this.type = marketActionType;
        //aggiungo gli effetti rapidi in baso al tipo di mercato
        Resource resource = null;
        Resource additionalResource = null;
        switch (marketActionType) {
            case YELLOW:
                resource = new Resource(5, ResourceType.COINS);
                break;
            case PURPLE:
                resource = new Resource(5, ResourceType.SERVANTS);
                break;
            case BLUE:
                resource = new Resource(3, ResourceType.MILITARY);
                additionalResource = new Resource(2, ResourceType.COINS);
                break;
            case GRAY:
                resource = new Resource(1, ResourceType.PRIVILEGE);
                additionalResource = new Resource(1, ResourceType.PRIVILEGE);
                break;
        }
        setEffect(new FixedIncrementEffect(resource));
        if (additionalResource != null)
            additionalEffect = new FixedIncrementEffect(additionalResource);
    }

    public MarketActionType getType() {
        return type;
    }

    @Override
    public void doAction(Action action) throws LorenzoException, NewActionException {
        if (getMinValue() > action.getValue())
            throw new LorenzoException("non hai abbastanza forza per effettuare l'azione");

        setFamilyMember(action.getFamilyMember());
        getEffect().active(action.getPlayer());
        if (additionalEffect != null)
            additionalEffect.active(action.getPlayer());
    }
}
