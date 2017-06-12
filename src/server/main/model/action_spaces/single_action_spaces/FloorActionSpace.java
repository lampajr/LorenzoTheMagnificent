package server.main.model.action_spaces.single_action_spaces;

import api.types.CardType;
import api.types.ResourceType;
import server.main.game_server.exceptions.LorenzoException;
import server.main.game_server.exceptions.NewActionException;
import server.main.model.action_spaces.Action;
import server.main.model.board.DevelopmentCard;
import server.main.model.board.Tower;
import server.main.model.effects.development_effects.FixedIncrementEffect;
import server.main.model.fields.Resource;

/**
 * @author Luca
 * @author Andrea
 *
 * classe che mi rappresenta un singolo piano della torre, essa
 * è una dei 5 tipi diversi di spazi azione
 */
public class FloorActionSpace extends ActionSpace {
    private DevelopmentCard developmentCard;
    private final CardType cardType;
    private final Tower towerReference;

    public FloorActionSpace(int value, CardType cardType, ResourceType resourceTypeQuickEffect, Tower towerReference) {
        super(value);
        this.cardType = cardType;
        this.towerReference = towerReference;
        Resource resource = null;
        if (value == 5)
            resource = new Resource(1, resourceTypeQuickEffect);
        else if (value == 7)
            resource = new Resource(2, resourceTypeQuickEffect);
        super.setEffect(new FixedIncrementEffect(resource)); //eventualmente null
    }

    public void setDevelopmentCard(DevelopmentCard DevelopmentCard){
        this.developmentCard = DevelopmentCard;
    }

    public DevelopmentCard getDevelopmentCard(){
        return developmentCard;
    }

    public CardType getCardType() {
        return cardType;
    }

    /**
     * Metodo che raccoglie la carta dalla torre , e la assegna al giocatore che ha attivato lo spazio azione
     * attraverso il piazzamento legittimo del familiare
     * @param action l'azione da eseguire
     * @throws LorenzoException in caso di problemi
     * @throws NewActionException in caso di effetto che mi consente una nuova mossa
     */
    @Override
    public void doAction(Action action) throws LorenzoException, NewActionException {
        checkValue(action.getValue()); //controllo se ho abbastanza forza per eseguire l'azione
        towerReference.checkOtherMyFamilyMember(this, action.getFamilyMember()); //controlla se ho già un familiare su questa torre
        developmentCard.checkDrawn(); //controlla se è già stata pescata o meno
        developmentCard.setPlayer(action.getPlayer()); //controlla se ho le risorse sufficienti, e se le ho mi setta il giocatore
        setFamilyMember(action.getFamilyMember()); //setta il familiare, eventualmente null se si tratta di una nuova azione.
        if (getEffect() != null)
            getEffect().active(action.getPlayer());
        developmentCard.activeQuickEffects();
    }
}
