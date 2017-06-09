package server.main.model.effects.excommunicating_effects;

import api.types.ResourceType;
import server.main.game_server.AbstractPlayer;
import server.main.game_server.exceptions.NewActionException;
import server.main.model.effects.development_effects.Effect;
import server.main.model.fields.Resource;

import java.util.ArrayList;
import java.util.List;

import static server.main.model.effects.development_effects.EffectsCreator.*;

/**
 * @author Luca
 * @author Andrea
 */
public class VictoryPointsDecrementEffect implements Effect{
    private List<ResourceType> resourceTypes;

    public VictoryPointsDecrementEffect(List<ResourceType> resourceTypes) {
        this.resourceTypes = resourceTypes;
    }

    @Override
    public void active(AbstractPlayer player) throws NewActionException {
        resourceTypes.forEach((resourceType -> {
            int decrement = player.getPersonalBoard().getQtaResources().get(resourceType);
            if (resourceType == ResourceType.VICTORY)
                decrement = decrement/5;
            player.getPersonalBoard().modifyResources(new Resource(-decrement, ResourceType.VICTORY));
        }));
    }

    public static Effect createExcomInstance(String codEffect) {
        List<ResourceType> resourceTypes = new ArrayList<>();
        switch (codEffect.charAt(0)){
            case CHAR_RESOURCE:
                resourceTypes.add(ResourceType.COINS);
                resourceTypes.add(ResourceType.SERVANTS);
                resourceTypes.add(ResourceType.STONE);
                resourceTypes.add(ResourceType.WOOD);
                return new VictoryPointsDecrementEffect(resourceTypes);
            case CHAR_MILITARY:
                resourceTypes.add(ResourceType.MILITARY);
                return new VictoryPointsDecrementEffect(resourceTypes);
            case CHAR_VICTORY:
                resourceTypes.add(ResourceType.VICTORY);
                return new VictoryPointsDecrementEffect(resourceTypes);
        }
        return null;
    }
}
