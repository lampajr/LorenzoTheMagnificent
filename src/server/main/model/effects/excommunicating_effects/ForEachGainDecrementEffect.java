package server.main.model.effects.excommunicating_effects;

import api.types.ResourceType;
import server.main.game_server.AbstractPlayer;
import server.main.game_server.exceptions.NewActionException;
import server.main.model.effects.development_effects.Effect;
import server.main.model.effects.development_effects.EffectsCreator;
import server.main.model.fields.Field;
import server.main.model.fields.Resource;

/**
 * @author Luca
 * @author Andrea
 */
public class ForEachGainDecrementEffect implements Effect {
    private final Field resource;

    private ForEachGainDecrementEffect(ResourceType resourceType){
        this.resource = new Resource(-1, resourceType);
    }

    @Override
    public void active(AbstractPlayer player) throws NewActionException {
        Field resource = player.getPersonalBoard().getCurrentField();
        if (resource != null && resource.getType() == this.resource.getType()) {
            player.getPersonalBoard().modifyResources(this.resource);
        }
    }

    static Effect createExcomInstance(String codEffect) {
        switch (codEffect.charAt(0)){
            case EffectsCreator.CHAR_COIN:
                return new ForEachGainDecrementEffect(ResourceType.COINS);
            case EffectsCreator.CHAR_MILITARY:
                return new ForEachGainDecrementEffect(ResourceType.MILITARY);
            case EffectsCreator.CHAR_SERVANT:
                return new ForEachGainDecrementEffect(ResourceType.SERVANTS);
        }
        return null;
    }
}
