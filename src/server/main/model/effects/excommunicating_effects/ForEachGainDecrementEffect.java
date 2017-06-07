package server.main.model.effects.excommunicating_effects;

import api.types.ResourceType;
import server.main.game_server.exceptions.NewActionException;
import server.main.model.effects.development_effects.Effect;
import server.main.model.effects.development_effects.EffectsCreator;
import server.main.model.fields.Field;
import server.main.model.fields.Resource;
import server.main.game_server.AbstractPlayer;

import java.rmi.RemoteException;

/**
 * @author Luca
 * @author Andrea
 */
public class ForEachGainDecrementEffect implements Effect {
    private Field resource;

    private ForEachGainDecrementEffect(ResourceType resourceType){
        this.resource = new Resource(-1, resourceType);
    }

    @Override
    public void active(AbstractPlayer player) throws RemoteException, NewActionException {
        Field resource = player.getPersonalBoard().getCurrentField();
        if (resource != null && resource.getType() == this.resource.getType()) {
            player.getPersonalBoard().modifyResources(this.resource);
        }
    }

    public static Effect createExcomInstance(String codEffect) {
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