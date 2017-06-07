package api.messages;

/**
 * @author Luca
 * @author Andrea
 */
public enum SocketProtocol {
    SHOT_DICE, LOGIN, START_GAME, EXCOMMUNICATION_CHOICE, INFORMATION, END_MOVE, CONVERT_PRIVILEGE, SURRENDER,
    GAME_ENDED, EXCOMMUNICATE, YOUR_EXCOMMUNICATION_TURN, YOUR_TURN, PRIVILEGE, NEW_ACTION, ACTION, HAVE_TO_SHOT,
    DICE_VALUES, OPPONENT_MOVE, UPDATE_PERSONAL_CARDS, UPDATE_RESOURCES, TOWERS_CARDS, IS_GAME_STARTED,
    ORDER, OPPONENT_MEMBER_MOVE, EXIT, OPPONENT_SURRENDER;
}