package io.stepinto.war.player;

import io.stepinto.war.card.Card;
import io.stepinto.war.card.Face;
import io.stepinto.war.card.Suit;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author adam.fitzpatrick
 */
public class PlayerListTest {
    PlayerList playerList;
    Player player1;
    Player player2;

    @Before
    public void setup() {
        player1 = new Player("Player 1");
        player1.setHand(new ArrayList<>(Arrays.asList(new Card(Suit.Clubs, Face.FIVE))));
        player2 = new Player("Player 2");
        player2.setHand(new ArrayList<>(Arrays.asList(new Card(Suit.Clubs, Face.THREE))));
        playerList = new PlayerList();
        playerList.getPlayers().add(player1);
        playerList.getPlayers().add(player2);
    }

    @Test
    public void testPlayAll() {
        assertEquals(2, playerList.playAll());
        assertNotNull(playerList.getPlayers().get(0).getCurrentPlay());
        assertNotNull(playerList.getPlayers().get(1).getCurrentPlay());
    }

    @Test
    public void testPlayAllEmptyHand() {
        player1.setHand(new ArrayList<>());
        assertEquals(1, playerList.playAll());
    }

    @Test
    public void testPlayList() {
        assertEquals(1, playerList.playList(Collections.singletonList(player1)));
        assertNotNull(playerList.getPlayers().get(0).getCurrentPlay());
        assertNull(playerList.getPlayers().get(1).getCurrentPlay());
    }

    @Test
    public void testGetRoundLeaders() {
        playerList.playAll();
        assertEquals(1, playerList.getRoundLeaders().size());
        assertEquals(player1, playerList.getRoundLeaders().get(0));
    }

    @Test
    public void testGetRoundLeadersTie() {
        Player player3 = new Player("Player 3");
        player3.setHand(new ArrayList(Arrays.asList(new Card(Suit.Diamonds, Face.FIVE))));
        playerList.getPlayers().add(player3);
        playerList.playAll();
        assertEquals(2, playerList.getRoundLeaders().size());
        assertEquals(player1, playerList.getRoundLeaders().get(0));
        assertEquals(player3, playerList.getRoundLeaders().get(1));
    }

    @Test
    public void testGetRoundLeadersNullCurrent() {
        Player player3 = new Player("Player 3");
        player3.setHand(new ArrayList<>());
        playerList.getPlayers().add(player3);
        playerList.playAll();
        assertEquals(1, playerList.getRoundLeaders().size());
        assertEquals(player1, playerList.getRoundLeaders().get(0));
    }

    @Test
    public void testAvailablePlayers() {
        assertEquals(2, playerList.availablePlayers());
        player1.setHand(new ArrayList<>());
        assertEquals(1, playerList.availablePlayers());
    }

    @Test
    public void testGetPointsLeader() {
        player1.recordRoundWin(10);
        player1.recordGame(true);
        player2.recordRoundWin(5);
        player2.recordGame(true);
        assertEquals(player1, playerList.getPointsLeader());
    }
}
