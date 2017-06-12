package server.main_server.model.fields;

import api.types.ResourceType;

/**
 * @author Andrea
 * @author Luca
 */
public class MilitaryCost extends Resource{
    private final int minValue;

    private MilitaryCost(int qta, int minValue) {
        super(qta, ResourceType.MILITARY);
        this.minValue = minValue;
    }

    int getMinValue() {
        return minValue;
    }

    /**
     * metodo di factory che mi genera un costo militare
     * @param cod codice
     * @return istanza di questa classe
     */
    public static MilitaryCost createMilitaryCost(String cod) {
        int minValue = Integer.parseInt(cod.substring(0,1));
        int qta = Integer.parseInt(cod.substring(1,2));
        return new MilitaryCost(-qta, minValue);
    }
}
