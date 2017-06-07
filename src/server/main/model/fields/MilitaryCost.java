package server.main.model.fields;

import api.types.ResourceType;

/**
 * @author Andrea
 * @author Luca
 */
public class MilitaryCost extends Resource{
    private int minValue;

    public MilitaryCost(int qta) {
        super(qta, ResourceType.MILITARY);
    }

    public MilitaryCost(int qta, int minValue) {
        super(qta, ResourceType.MILITARY);
        this.minValue = minValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public static MilitaryCost createMilitaryCost(String cod) {
        int minValue = Integer.parseInt(cod.substring(0,1));
        int qta = Integer.parseInt(cod.substring(1,2));
        return new MilitaryCost(-qta, minValue);
    }
}
