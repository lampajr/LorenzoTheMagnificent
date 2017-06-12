package server.test.model.board;

import api.types.FamilyMemberType;
import server.main_server.model.board.FamilyMember;
import server.main_server.model.board.PersonalBoard;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Luca
 * @author Andrea
 */
public class FamilyMemberTest {
    private FamilyMember familyMember;

    @Before
    public void setup() {
        familyMember = new FamilyMember(new PersonalBoard(1), FamilyMemberType.ORANGE_DICE);
    }

    @Test
    public void setValue() throws Exception {
        familyMember.setValue(3);
        assertEquals(3, familyMember.getValue());
    }

    @Test
    public void modifyValue() throws Exception {
        familyMember.setValue(3);
        familyMember.modifyValue(4);
        familyMember.modifyValue(-2);
        assertEquals(5, familyMember.getValue());
    }

}