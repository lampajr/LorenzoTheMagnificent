package server.main_server.model.fields;

import api.types.ResourceType;
import server.main_server.game_server.exceptions.LorenzoException;

import static server.main_server.model.effects.development_effects.EffectsCreator.*;

/**
 * @author  Luca
 * @author  Andrea
 *
 * classe che mi identifica le risorse di ciascun giocatore, ciascun
 * risorsa è resa diversa dall'attributo ResourceType.
 */

public class Resource implements Field {

    private int qta;
    private ResourceType type;

    public Resource(int qta) {
        this(qta, null);
    }

    public Resource(int qta, ResourceType type){
        this.qta=qta;
        this.type=type;
    }

    @Override
    public int getQta() {
        return qta;
    }

    @Override
    public ResourceType getType() {
        return type;
    }

    /**
     * modifica la quantità della risorsa
     * @param resourceEffect risorsa di modifica
     */
    @Override
    public void modify(Field resourceEffect) {
        if (resourceEffect.getType().getCode().equals(this.type.getCode())) {
            //sono dello stesso tipo
            this.qta += resourceEffect.getQta();
            if (qta < 0)
                this.qta = 0;
        }
    }

    @Override
    public void subtract(Field cost) {
        if (cost.getType().getCode().equals(this.type.getCode())) {
            //sono dello stesso tipo
            this.qta = this.qta - cost.getQta();
        }
    }

    /**
     * setta il tipo della risorsa
     * @param type tipo
     */
    @Override
    public void setType(ResourceType type) {
        if (this.type == null)
            this.type = type;
    }

    /**
     * setta la quantità della risorsa
     * @param reset valore qta
     */
    @Override
    public void setQta(int reset) {
        this.qta = reset;
    }

    /**
     * controlla se ho le risorse necessarie
     * @param cost risorsa di cui devo ferificarne la dispoinibiltà
     * @throws LorenzoException in caso di risorse non sufficicenti
     */
    @Override
    public void checkResource(Field cost) throws LorenzoException {
        int toCheck;
        if (cost instanceof MilitaryCost) {
            MilitaryCost militaryCost = (MilitaryCost) cost;
            toCheck = militaryCost.getMinValue();
        }
        else
            toCheck = cost.getQta();
        if (qta < Math.abs(toCheck))
            throw new LorenzoException("You do not have enough funds to do the action");
    }

    /**
     * mi crea la risorsa in base al codice preso dal db e che il metodo
     * riceve come parametro
     * codice = int1*char*int2[*int1*char*int2]
     * dove int1 -> indica la quantità di risorsa
     *      char -> il carattere che mi identifica la risorsa secondo
     *      int2 -> numero del periodo
     * @param cod codice
     * @return la risorsa creata in base al codice
     */
    public static Resource createResource(String cod, boolean isCost) {
        //il primo carattere mi indica il numero intero del costo, e lo rendo subito negativo
        int qta = Integer.parseInt(cod.substring(0,1));
        if (isCost)
            qta = -qta;
        //il secondo carattere mi indica la risorsa
        char charCod = cod.charAt(1);
        //inizializzo la risorsa e il punto passandogli solo la qta che sarà negativa
        Resource res = new Resource(qta);
        //in base al codice dell'effetto mi setta il tipo della risorsa
        //ancora da implementare
        switch (charCod) {
            case CHAR_COIN:
                res.setType(ResourceType.COINS);
                break;
            case CHAR_STONE:
                res.setType(ResourceType.STONE);
                break;
            case CHAR_WOOD:
                res.setType(ResourceType.WOOD);
                break;
            case CHAR_SERVANT:
                res.setType(ResourceType.SERVANTS);
                break;
            case CHAR_MILITARY:
                res.setType(ResourceType.MILITARY);
                break;
            case CHAR_FAITH:
                res.setType(ResourceType.FAITH);
                break;
            case CHAR_VICTORY:
                res.setType(ResourceType.VICTORY);
                break;
            case CHAR_PRIVILEGE:
                res.setType(ResourceType.PRIVILEGE);
                break;
            default:
                break;
        }
        return res;
    }
}
