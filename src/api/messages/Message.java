package api.messages;


import api.types.ActionSpacesType;
import api.types.CardType;
import api.types.MarketActionType;

/**
 * @author Luca
 * @author Andrea
 */
public interface Message {

    ActionSpacesType getActionSpacesType();

    CardType getCardType();

    int getNumFloor();

    MarketActionType getMarketActionType();

    int getValue();

    void setValue(int value);
}
