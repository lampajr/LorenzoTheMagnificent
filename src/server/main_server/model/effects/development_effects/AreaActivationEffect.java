package server.main_server.model.effects.development_effects;

import server.main_server.game_server.AbstractPlayer;
import server.main_server.game_server.exceptions.NewActionException;

/**
 * @author Luca
 * @author Andrea
 *
 * mi identifa gli effetti permanenti degli edifici e dei territori
 * che vengono attivati solo quando si va in zona raccolto/produzione
 * e solo se l'azione ha un valore superiore al valore minimo
 * di questo effetto.
 */
public class AreaActivationEffect implements Effect{
    private final Effect effect;
    private final int minValue;

    public AreaActivationEffect(Effect effect, int minValue){
        this.effect = effect;
        this.minValue = minValue;
    }

    /**
     * Permette l' attivazione dell' azione
     * @param player il giocatore che sta attivando l'effetto
     * @throws NewActionException in caso di nuova azione, non dovrebbe mai verificarsi
     */
    @Override
    public void active(AbstractPlayer player) throws NewActionException {
        if (player.getPersonalBoard().getCurrentAction().getValue() >= minValue)
            effect.active(player);
    }


    /**
     * metodo che crea un effetto in base al tipo di area produzione false e harvest true
     * @param c caratte, indicante
     * @param code codice
     * @return istanza delll'effetto corretta
     */
    public static Effect createInstance(char c, String code) {
        int minValue = Integer.parseInt(c+"");
        return new AreaActivationEffect(FixedIncrementEffect.createInstance(code), minValue);
    }

    /**
     * overloading del metodo precedente che viene chiamato quando ho un effetto permanente che incrementa
     * le risorse in base al numero di carte
     * @param cod codice dell'effetto
     * @return l'istanza corretta dell'effetto
     */
    public static Effect createInstance(String cod) {
        int minValue = Integer.parseInt(cod.charAt(0)+"");
        return new AreaActivationEffect(VariableIncrementEffect.createInstance(cod.substring(2)),minValue);
    }


    /**
     * ulteriore overloading del metodo precedente che crea l' effetto di conversione di due o più risorse,
     * quando richiamato da una zona produzione o raccolta
     * @param increment codice relativo alle risorse da incrementare
     * @param decrement codice relativo alle risorse da decrementare
     * @return istanza di AreaActiovationEffect
     */
    public static AreaActivationEffect createInstance(String increment, String decrement) {
        int minValue = Integer.parseInt(increment.charAt(0)+"");
        return new AreaActivationEffect(ConvertionEffect.createInstance(increment.substring(1),decrement), minValue);
    }

}
