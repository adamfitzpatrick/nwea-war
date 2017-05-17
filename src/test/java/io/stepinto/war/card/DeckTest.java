package io.stepinto.war.card;

import io.stepinto.war.player.Player;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author adam.fitzpatrick
 */
public class DeckTest {

    @Test
    public void testDeal() {
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        Player player3 = new Player("Player 3");
        Deck.deal(Arrays.asList(player1, player2, player3));
        assertEquals(17, player1.getHand().size());
        assertEquals(17, player2.getHand().size());
        assertEquals(17, player3.getHand().size());
    }
}
