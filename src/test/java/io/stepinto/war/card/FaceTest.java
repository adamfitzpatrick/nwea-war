package io.stepinto.war.card;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author adam.fitzpatrick
 */
public class FaceTest {

    @Test
    public void testGetValue() {
        assertEquals(10, Face.TEN.getValue());
        assertEquals(13, Face.K.getValue());
    }

    @Test
    public void testToString() {
        assertEquals("A", Face.A.toString());
        assertEquals("3", Face.THREE.toString());
    }
}
