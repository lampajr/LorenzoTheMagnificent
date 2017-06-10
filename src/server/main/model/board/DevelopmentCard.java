package server.main.model.board;

import api.types.CardType;
import server.main.game_server.AbstractPlayer;
import server.main.game_server.exceptions.LorenzoException;
import server.main.game_server.exceptions.NewActionException;
import server.main.model.effects.development_effects.Effect;
import server.main.model.fields.Field;

import java.util.List;

/**
 * @author Luca
 * @author Andrea
 *
 * classe che mi rappresenta una singola carta sviluppo
 */

public class DevelopmentCard {
    private AbstractPlayer player;
    private final CardType type;
    private final String name;
    private final List<Field> costs;
    private final List<Effect> quickEffects;
    private final List<Effect> permanentEffects;
    private final int period;

    public DevelopmentCard(CardType type, String name, List<Field> costs,
                           List<Effect> qeffs, List<Effect> peffs, int period){
        this.type = type;
        this.name = name;
        this.costs = costs;
        this.quickEffects = qeffs;
        this.permanentEffects = peffs;
        this.period = period;
    }

    public CardType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<Field> getCosts() {
        return costs;
    }

    public AbstractPlayer getPlayer() {
        return player;
    }

    /**
     * metodo che controlla se il giocatore ha abbastanza risorse per poter prendere la carta
     * @param player giocatore che vuole prendere la carte
     * @throws LorenzoException lancia se non possiede le risorse sufficienti
     */
    public void setPlayer(AbstractPlayer player) throws LorenzoException {
         if (costs!=null){
            for (Field cost : costs)
                player.getPersonalBoard().checkResources(cost);
         }
         player.getPersonalBoard().checkNumberOfCards(type);
         //se ho abbastanza risorse posso pescare e quindi pago il costo e attivo l'effetto immediato
         this.player = player;
         this.player.getPersonalBoard().addCard(this);
         activeCosts();
    }

    public void checkDrawn() throws LorenzoException {
        if (this.player != null)
            throw new LorenzoException("carta già pescata in questa posizione");
    }

    public int getPeriod() {
        return period;
    }

    /**
     * chiama il metodo che modifica le risorse nella player
     * passandogli la lista dei costi che sarà tutto negativo
     */
    private void activeCosts(){
        if (costs != null) {
            costs.forEach(field -> player.getPersonalBoard().modifyResources(field));
        }
    }

    /**
     * metodo che attiva tutti gli effetti immediati chiamando il metodo
     * active(PersonalBoard) di ciascun effetto.
     */
    public void activeQuickEffects() throws NewActionException {
        if (quickEffects != null) {
            for (Effect effect : quickEffects)
                effect.active(player);
        }
    }

    void activePermanentEffects() throws NewActionException {
        if (permanentEffects != null) {
            for (Effect effect : permanentEffects)
                effect.active(player);
        }
    }
}
