package server.main_server.model.effects.development_effects;

import api.types.ResourceType;
import server.main_server.game_server.AbstractPlayer;
import server.main_server.game_server.exceptions.LorenzoException;
import server.main_server.game_server.exceptions.NewActionException;
import server.main_server.model.fields.Field;
import server.main_server.model.fields.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Luca
 * @author Andrea
 *
 * classe che identifica l'effetto di conversione, mi toglie un tot di risorse, se le ho
 * e me ne aggiunge altre.
 */
public class ConvertionEffect implements Effect{
    private final List<Field> fieldToIncrement;
    private final List<Field> fieldToDecrement;

    /**
     * Costruttore della classe
     * @param fieldToIncrement lista di risorse da aumentare
     * @param fieldToDecrement lista di risorse da diminuire
     */
    public ConvertionEffect(List<Field> fieldToIncrement, List<Field> fieldToDecrement){
        this.fieldToDecrement = fieldToDecrement;
        this.fieldToIncrement = fieldToIncrement;
    }


    /**
     * Attiva l' effetto della conversione andando a controllare se la conversione può essere effettuata facendo il
     * controllo sulla risorsa
     * @param player il giocatore che sta attivando l'effetto
     * @throws NewActionException in caso di nuova azione, non dovrebbe mai verificarsi
     */
    @Override
    public void active(AbstractPlayer player) throws NewActionException {
        try {
            checkActivation(player);
            for (Field f : fieldToDecrement) {
                player.getPersonalBoard().modifyResources(f);
            }
            for (Field f : fieldToIncrement) {
                if (f.getType() != ResourceType.PRIVILEGE) {
                    player.getPersonalBoard().modifyResources(f);
                    player.getPersonalBoard().setCurrentField(f);
                    player.activeExcommunicationEffects(player.getPersonalBoard().getCurrentAction(), 2);
                } else {
                    player.notifyPrivilege();
                }
            }
        } catch (LorenzoException e) {
            //non posso attivare la conversione, perché non ho abbastanza risorse
            //non faccio nulla.
        }
    }

    private void checkActivation(AbstractPlayer player) throws LorenzoException {
        for(Field toCheck : fieldToDecrement){
            player.getPersonalBoard().checkResources(toCheck);
        }
    }


    /**
     * prende le due stringhe codificate e aggiunge le risorse alle due liste da inc e da dec
     * @param increment lista di risorse da incrementare
     * @param decrement lista di risorse da decrementare
     * @return un oggetto del tipo ConvertionEffect
     */
    public static ConvertionEffect createInstance(String increment, String decrement){
        List<Field> fieldToIncrement = new ArrayList<>();
        List<Field> fieldToDecrement = new ArrayList<>();

        fieldToIncrement.add(Resource.createResource(increment.substring(0,2),false));
        if(increment.length() == 4){
            fieldToIncrement.add(Resource.createResource(increment.substring(2,4),false));
        }
        if(increment.length() == 6){
            fieldToIncrement.add(Resource.createResource(increment.substring(4,6),false));
        }

        fieldToDecrement.add(Resource.createResource(decrement.substring(0,2),true));
        if(decrement.length() == 4){
            fieldToDecrement.add(Resource.createResource(decrement.substring(2,4),true));
        }
        return new ConvertionEffect(fieldToIncrement,fieldToDecrement);

    }

}
