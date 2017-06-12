package server.main_server.model.fields;

import api.types.ResourceType;
import server.main_server.game_server.exceptions.LorenzoException;

/**
 * @author Luca
 * @author Andrea
 *
 * mi rappresenta una qualsiasi riserva in possesso del giocatore
 */
public interface Field {

    /**
     * ritorna la quantità della risorsa in oggetto
     * @return la quantità
     */
    int getQta();

    /**
     * modifica il valore dell' attributo desiderato in base a n
     */
    void modify(Field resourceEffect);

    /**
     * metodo che mi sottrae il costo alla risorsa sulla quale
     * viene eseguito
     * @param cost la risorsa che mi rappresenta il costo
     */
    void subtract(Field cost);

    /**
     * mi ritorna il tipo della mia risorsa
     * @return ResourceType
     */
    ResourceType getType();

    /**
     * metodo che mi setta il tipo della mia risorsa
     * @param type tipo
     */
    void setType(ResourceType type);

    /**
     * setta la quantità della risorsa
     * @param reset valore qta
     */
    void setQta(int reset);

    /**
     * mi controlla se ho le risorse sufficienti, in caso negativo viene lanciata un'eccezione
     * @param cost costo da verificare
     */
    void checkResource(Field cost) throws LorenzoException;
}
