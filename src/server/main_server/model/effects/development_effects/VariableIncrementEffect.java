package server.main_server.model.effects.development_effects;

import api.types.CardType;
import api.types.ResourceType;
import server.main_server.game_server.AbstractPlayer;
import server.main_server.model.board.DevelopmentCard;
import server.main_server.model.fields.Field;
import server.main_server.model.fields.Resource;

import java.util.List;

/**
 * @author Luca
 * @author Andrea
 *
 * classe che mi rappresenta un effetto che mi incrementa una specifica
 * risorsa in base al numero di carte (di un certo tipo) in possesso
 */
public class VariableIncrementEffect implements Effect{

    private final Field field;
    private CardType cardType = null;
    private ResourceType resourceType;

    /**
     * mi incrementa la risorsa field, di una quantità pari al numero
     * di carte del tipo cardType in possesso al giocatore
     * @param field risorsa da modificare
     * @param typeCard tipo di carta da controllarne la quantità in possesso
     */
    public VariableIncrementEffect(Field field, CardType typeCard) {
        this.field = field;
        this.cardType = typeCard;
    }

    private VariableIncrementEffect(Field field, ResourceType resType) {
        this.field = field;
        this.resourceType = resType;
    }

    @Override
    public void active(AbstractPlayer player) {
        int qta;
        if (cardType != null){
            List<DevelopmentCard> list = player.getPersonalBoard().getCardsList(cardType);
            qta = field.getQta() * list.size();
        }
        else {
            int tmp = player.getPersonalBoard().getQtaResources().get(resourceType)/2;
            qta = field.getQta() * tmp;
        }
        Resource newRes = new Resource(qta, field.getType());
        player.getPersonalBoard().setCurrentField(newRes);
        player.getPersonalBoard().modifyResources(newRes);
        player.activeExcommunicationEffects(player.getPersonalBoard().getCurrentAction(), 2);
    }

    /**
     * metodo statico che mi genera un effetto
     * @param cod codice dell'effetto
     * @return istanza di VariableIncrementEffect
     */
    public static VariableIncrementEffect createInstance(String cod) {
        int qtaToIncrement = Integer.parseInt(cod.substring(0,1));
        char resToIncrement = cod.charAt(1);
        int qtaToCheck = Integer.parseInt(cod.substring(2,3));
        char toCheck = cod.charAt(3);
        CardType cardType = null;
        ResourceType resType = null;
        switch (toCheck){
            case EffectsCreator.CHAR_BUILDINGS:
                cardType = CardType.BUILDING;
                break;
            case EffectsCreator.CHAR_CHARACTERS:
                cardType = CardType.CHARACTER;
                break;
            case EffectsCreator.CHAR_TERRITORY:
                cardType = CardType.TERRITORY;
                break;
            case EffectsCreator.CHAR_VENTURES:
                cardType = CardType.VENTURES;
                break;
            case EffectsCreator.CHAR_MILITARY:
                resType = ResourceType.MILITARY;
                break;
            default:
                break;

        }
        Resource res = null;
        switch (resToIncrement){
            case EffectsCreator.CHAR_VICTORY:
                res = new Resource(qtaToIncrement, ResourceType.VICTORY);
                break;
            case EffectsCreator.CHAR_COIN:
                res = new Resource(qtaToIncrement, ResourceType.COINS);
                break;
            default:
                break;

        }
        if (cardType != null)
            return new VariableIncrementEffect(res, cardType);
        else
            return new VariableIncrementEffect(res, resType);
    }
}