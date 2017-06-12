package server.main_server.model.board;

import server.main_server.game_server.AbstractPlayer;
import server.main_server.game_server.exceptions.NewActionException;
import server.main_server.model.effects.development_effects.ActionValueModifyingEffect;
import server.main_server.model.effects.development_effects.Effect;
import server.main_server.model.effects.excommunicating_effects.ExcommunicatingEffectCreator;
import server.main_server.model.effects.excommunicating_effects.FamilyMemberValueDecrementEffect;
import server.main_server.model.effects.excommunicating_effects.ForEachGainDecrementEffect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Luca
 * @author Andrea
 *
 * rappresenta la parte del tabellone della scomunica,gli effetti vengono presi dal db,
 * in base al periodo di appartenenza, poichè
 * per la partia ne serviranno solo una per periodo, la classe mi estrae un
 * numero casuale per tabella e me la crea.
 */
class Excommunication {
    private static final String QUERY_PERIOD_1 = "SELECT * FROM excommunicatingcards WHERE period=1 AND id=";
    private static final String QUERY_PERIOD_2 = "SELECT * FROM excommunicatingcards WHERE period=2 AND id=";
    private static final String QUERY_PERIOD_3 = "SELECT * FROM excommunicatingcards WHERE period=3 AND id=";
    private final ConnectionDB connectionDB;
    private final List<Effect> excomEffectList;
    private final List<String> codeList;
    private final Map<Integer,List<AbstractPlayer>> excomPlayerMap;

    Excommunication(){
        connectionDB = new ConnectionDB();
        excomEffectList = new ArrayList<>();
        codeList = new ArrayList<>();
        excomPlayerMap = new HashMap<>();
        excomPlayerMap.put(1, new ArrayList<>());
        excomPlayerMap.put(2, new ArrayList<>());
        excomPlayerMap.put(3, new ArrayList<>());
        prelameCardFirstPeriodFromDB();
        prelameCardSecondPeriodFromDB();
        prelameCardThirdPeriodFromDB();
        connectionDB.closeConnection();
    }

    /**
     * metodo che mi preleva una carta a caso nella tabella del primo periodo,
     * le carte in ciascuna tabella sono univoche per id
     * iniziale che va da 1 a 7.
     */
    private void prelameCardFirstPeriodFromDB() {
        Random rand = new Random();
        ResultSet rs = connectionDB.executeQuery(QUERY_PERIOD_1 + (rand.nextInt(5)+1));
        try {
            rs.next();
            codeList.add(rs.getInt("period") + "" + rs.getInt("id"));
            String codEffect = rs.getString("effect_code");
            excomEffectList.add(ExcommunicatingEffectCreator.createInstanceFirstPeriod(codEffect));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * metodo che mi preleva una carta a caso nella tabella del secondo periodo,
     * le carte in ciascuna tabella sono univoche per id
     * iniziale che va da 1 a 7.
     */
    private void prelameCardSecondPeriodFromDB() {
        Random rand = new Random();
        ResultSet rs = connectionDB.executeQuery(QUERY_PERIOD_2 + (rand.nextInt(3)+1));
        try {
            rs.next();
            codeList.add(rs.getInt("period") + "" + rs.getInt("id"));
            String codEffect = rs.getString("effect_code");
            excomEffectList.add(ExcommunicatingEffectCreator.createInstanceSecondPeriod(codEffect));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * metodo che mi preleva una carta a caso nella tabella del terzo periodo,
     * le carte in ciascuna tabella sono univoche per id
     * iniziale che va da 1 a 7.
     */
    private void prelameCardThirdPeriodFromDB() {
        Random rand = new Random();
        ResultSet rs = connectionDB.executeQuery(QUERY_PERIOD_3 + (rand.nextInt(5)+1));
        try {
            rs.next();
            codeList.add(rs.getInt("period") + "" + rs.getInt("id"));
            String codEffect = rs.getString("effect_code");
            excomEffectList.add(ExcommunicatingEffectCreator.createInstanceThirdPeriod(codEffect));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * ritorna la lista dei codici delle tessere scomunica pescate
     * @return lista codici
     */
    List<String> getCodeList() {
        return codeList;
    }

    /**
     * mi attiva la scomunica al giocatore passato come parametro solo se risulta essere
     * nella lista degli scomunicati per il periodo 1
     * @param player giocatore
     * @throws NewActionException in caso di nuova azione
     */
    void activeFirtsPeriod(AbstractPlayer player, int type) throws NewActionException {
        if (excomPlayerMap.get(1).contains(player)){
            if (type == 1 && (excomEffectList.get(0) instanceof ActionValueModifyingEffect ||
                                excomEffectList.get(0) instanceof FamilyMemberValueDecrementEffect)){
                excomEffectList.get(0).active(player);
            }
            else if (type == 2 && excomEffectList.get(0) instanceof ForEachGainDecrementEffect) {
                excomEffectList.get(0).active(player);
            }
        }
    }

    /**
     * mi attiva la scomunica al giocatore passato come parametro solo se risulta essere
     * nella lista degli scomunicati per il periodo 2
     * @param player giocatore
     * @throws NewActionException in caso di nuova azione
     */
    void activeSecondPeriod(AbstractPlayer player) throws NewActionException {
        if (excomPlayerMap.get(2).contains(player)){
            excomEffectList.get(1).active(player);
            System.out.println("ho attivato l'effetto scomunica 2 periodo per " + player.getIdPlayer());
        }
    }

    /**
     * mi attiva la scomunica al giocatore passato come parametro solo se risulta essere
     * nella lista degli scomunicati per il periodo 3
     * @param player giocatore
     * @throws NewActionException in caso di nuova azione
     */
    void activeThirdPeriod(AbstractPlayer player) throws  NewActionException {
        if (excomPlayerMap.get(3).contains(player)){
            excomEffectList.get(2).active(player);
        }
    }

    /**
     * mi scomunica il giocatore passato come parametro
     * @param period periodo della scomunica
     * @param player giocatore da scomunicare
     */
    void addPlayer(int period, AbstractPlayer player) {
        excomPlayerMap.get(period).add(player);
        player.excommunicate(player.getIdPlayer(), period);
    }
}