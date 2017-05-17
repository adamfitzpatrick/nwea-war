package io.stepinto.war.player;

import io.stepinto.war.card.Card;
import io.stepinto.war.card.Face;
import io.stepinto.war.card.Suit;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author adam.fitzpatrick
 */
public class PlayerTest {
    List<Card> spoils;
    Player player;

    @Before
    public void setup() {
        Card card1 = new Card(Suit.Spades, Face.A);
        Card card2 = new Card(Suit.Clubs, Face.TEN);
        spoils = new ArrayList<>(Arrays.asList(card1, card2));
        player = new Player("Player1");
        player.setHand(spoils);
    }

    @Test
    public void testPlay() {
        player.play();
        assertEquals(1, player.getHand().size());
        assertEquals("A of Spades", player.getCurrentPlay().toString());
        assertEquals("10 of Clubs", player.getHand().get(0).toString());
    }

    @Test
    public void testRecordRoundWin() {
        player.recordRoundWin(1);
        assertEquals(1, player.getCardsWon());
        assertEquals(1, player.getRoundsWon());
    }

    @Test
    public void testRecordGameVictory() {
        player.recordRoundWin(1);
        player.recordGame(true);
        assertEquals(0, player.getRoundsWon());
        assertEquals(0, player.getCardsWon());
        assertEquals(1, player.getGamesWon());
        assertEquals(1, player.getTotalCardsWon());
        assertEquals(1, player.getTotalRoundsWon());
    }

    @Test
    public void testRecordGameLoss() {
        player.recordRoundWin(1);
        player.recordGame(false);
        assertEquals(0, player.getRoundsWon());
        assertEquals(0, player.getGamesWon());
        assertEquals(1, player.getTotalCardsWon());
        assertEquals(1, player.getTotalRoundsWon());
    }

    private List<Card> getCards() {
        return Collections.singletonList(new Card(Suit.Diamonds, Face.J));
    }
}
