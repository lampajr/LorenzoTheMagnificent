package server.main_server.model.effects.excommunicating_effects;

import api.types.CardType;
import server.main_server.game_server.AbstractPlayer;
import server.main_server.game_server.exceptions.NewActionException;
import server.main_server.model.effects.development_effects.Effect;
import server.main_server.model.effects.development_effects.EffectsCreator;

/**
 * @author lampa
 */
public class NotGainVictoryEffect implements Effect {
    private final CardType cardType;

    private NotGainVictoryEffect(CardType cardType){
        this.cardType = cardType;
    }

    @Override
    public void active(AbstractPlayer player) throws NewActionException {
        player.getPersonalBoard().resetList(cardType);
    }

    static Effect createExcomInstance(String codEffect) {
        switch (codEffect.charAt(0)){
            case EffectsCreator.CHAR_TERRITORY:
                return new NotGainVictoryEffect(CardType.TERRITORY);
            case EffectsCreator.CHAR_CHARACTERS:
                return new NotGainVictoryEffect(CardType.CHARACTER);
            case EffectsCreator.CHAR_VENTURES:
                return new NotGainVictoryEffect(CardType.VENTURES);
        }
        return null;
    }
}
