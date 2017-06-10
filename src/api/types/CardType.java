package api.types;

import java.io.Serializable;

/**
 * @author Luca
 * @author Andrea
 *
 * enumerazione dei possibili tipi di carte
 * in ordine: territorio, personaggi, imprese, imprese.
 */

public enum CardType implements Serializable {
    TERRITORY("TERRITORY"),CHARACTER("CHARACTER"),BUILDING("BUILDING"),VENTURES("VENTURES");

    private String code;

    CardType(String s){
        code = s ;
    }

    @Override
    public String toString() {
        return code.toLowerCase();
    }
}
