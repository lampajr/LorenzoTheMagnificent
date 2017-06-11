package server.main.model.effects.development_effects;

import server.main.game_server.AbstractPlayer;
import server.main.game_server.exceptions.NewActionException;

/**
 * @author Luca
 * @author Andrea
 *
 * classe che mi rappresenta un effetto che mi consente
 * di eseguire una nuova azione senza dover spostare un
 * mio familiare.
 */
public class ActionEffect implements Effect{
    private final int value;
    private final char codActionSpace;

    private ActionEffect(char codActionSpace, int value) {
        this.codActionSpace = codActionSpace;
        this.value = value;
    }


    /**
     * metodo che mi esegue una nuova azione chiedendo al client quale
     * @param player giocatore che esegue l'azione
     */
    @Override
    public void active(AbstractPlayer player) throws NewActionException {
        player.notifyNewAction(value, codActionSpace);
        throw new NewActionException();
    }

    public static ActionEffect createInstance(String code){
        int value = Integer.parseInt(code.substring(0,1));
        return new ActionEffect(code.charAt(1), value);
    }
}
