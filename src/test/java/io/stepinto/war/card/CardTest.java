package io.stepinto.war.card;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author adam.fitzpatrick
 */
public class CardTest {
    Card card;

    @Before
    public void setup() {
        card = new Card(Suit.Clubs, Face.FIVE);
    }

    @Test
    public void testCompareTo() {
        assertTrue(card.compareTo(new Card(Suit.Clubs, Face.A)) < 0);
        assertTrue(card.compareTo(new Card(Suit.Clubs, Face.TWO)) > 0);
        assertTrue(card.compareTo(new Card(Suit.Clubs, Face.FIVE)) == 0);
    }
    
    @Test
    public void testToString() {
        assertEquals("5 of Clubs", card.toString());
    }
}
