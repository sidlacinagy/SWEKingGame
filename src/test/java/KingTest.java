import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {

    @Test
    void testHashCode(){
        assertEquals(new King(5,6).hashCode(),46293);
    }


}
