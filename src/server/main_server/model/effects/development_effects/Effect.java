package server.main_server.model.effects.development_effects;

import server.main_server.game_server.AbstractPlayer;
import server.main_server.game_server.exceptions.NewActionException;

/**
 * @author Luca
 * @author Andrea
 *
 * interfaccia che mi rappresenta un effetto immediato, in genere
 * viene associato alle carte sviluppo ma può essere creato in qualsiasi
 * momento per effetto di altre azioni, se associato alle carte sviluppo
 * viene attivato non'appena essa viene pescata.
 */

public interface Effect {

    /**
     * metodo che mi attiva l'effetto specifico, ciascun effetto
     * che implementa questa interfaccia implementerà questo
     * metodo nella maniera propriamente corretta.
     * @param player il giocatore che sta attivando l'effetto
     */
    void active(AbstractPlayer player) throws NewActionException;


}
