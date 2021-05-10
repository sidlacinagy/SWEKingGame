package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class KingTest {

    @Test
    void testHashCode(){
        Assertions.assertEquals(new King(5,6).hashCode(),46293);
    }


}
