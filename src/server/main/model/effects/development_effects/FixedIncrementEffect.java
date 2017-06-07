package server.main.model.effects.development_effects;

import api.types.ResourceType;
import server.main.model.fields.Field;
import server.main.model.fields.Resource;
import server.main.game_server.AbstractPlayer;

import java.rmi.RemoteException;

/**
 * @author Luca
 * @author Andrea
 *
 * classe che mi rappresenta un effetto che mi incrementa
 * di un valore prefissato una risorsa specifica passata come
 * parametro nel costruttore
 */
public class FixedIncrementEffect implements Effect{
    private Field field;

    public FixedIncrementEffect(Field field){
        this.field = field;
    }

    @Override
    public void active(AbstractPlayer player) throws RemoteException {
        player.getPersonalBoard().setCurrentField(field);
        if (field  != null) {
            if (field.getType() != ResourceType.PRIVILEGE)
                player.getPersonalBoard().modifyResources(field);
            else
                player.notifyPrivilege();
        }
        player.activeExcommunicationEffects(player.getPersonalBoard().getCurrentAction(), 2);
    }

    public static FixedIncrementEffect createInstance(String code){
        return new FixedIncrementEffect(Resource.createResource(code, false));
    }

    public static Effect createInstance(String substring, boolean incDec) {
        if(incDec)
            return FixedIncrementEffect.createInstance(substring);
        else
            return new FixedIncrementEffect(Resource.createResource(substring, true));
    }
}