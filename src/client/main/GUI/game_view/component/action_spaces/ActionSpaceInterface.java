package client.main.GUI.game_view.component.action_spaces;

import api.types.ActionSpacesType;
import client.main.GUI.game_view.component.GuiFamilyMember;

/**
 * @author Luca
 * @author Andrea
 */
public interface ActionSpaceInterface {

    /**
     * metodo che aggiunge un familiare allo spazio azione
     * @param familyMember
     */
    void addFamilyMember(GuiFamilyMember familyMember);

    /**
     * rimuove tutti i familiari, eventualmente uno solo se
     * spazio azione singolo
     */
    void removeAllFamilyMembers();

    /**
     * mi ritorna l'enumerazione corrispondente al tipo dello spazio azione
     * @return type
     */
    ActionSpacesType getType();

    /**
     * mi setta lo spazio azione corrente
     */
    void setCurrentActionSpace();

    /**
     * rimuove uno specifico familiare solo se presente
     * @param familyMemberToRemove familiare da rimuovere
     */
    void removeFamilyMember(GuiFamilyMember familyMemberToRemove);
}
