package io.stepinto.war.ui;

import io.stepinto.war.card.Card;
import io.stepinto.war.card.Face;
import io.stepinto.war.card.Suit;
import io.stepinto.war.game.Game;
import io.stepinto.war.game.GameOptions;
import io.stepinto.war.player.Player;
import io.stepinto.war.player.PlayerList;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author adam.fitzpatrick
 */
public class UIServiceTest {
    Scanner scanner;
    UIService uiService;
    Player player1;
    Player player2;
    ByteArrayOutputStream outputStream;

    @Before
    public void setup() {
        scanner = new Scanner(System.in);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        player1 = new Player("Player 1");
        player1.setHand(new ArrayList<>(Arrays.asList(new Card(Suit.Clubs, Face.FIVE))));
        player2 = new Player("Player 2");
        player2.setHand(new ArrayList<>(Arrays.asList(new Card(Suit.Clubs, Face.SIX))));
        uiService = new UIService(scanner);
        uiService.setPlayerList(getPlayerList());
        GameOptions.setNextGamePlayers(getPlayerList());
    }

    @Test
    public void testShowGameIntro() {
        uiService.showGameIntro();
        assertTrue(outputStream.toString().length() > 0);
    }

    @Test
    public void testWriteGameFlavor() {
        uiService.writeGameFlavor();
        assertTrue(outputStream.toString().length() > 0);
    }

    @Test
    public void testReadPlayerCountValid() {
        simulateInput("4\n");
        uiService.readPlayerCount();
        assertEquals(4, GameOptions.getPlayerCount());
    }

    @Test
    public void testReadPlayerCountBelow2() {
        simulateInput("1\n2\n");
        uiService.readPlayerCount();
        assertTrue(outputStream.toString().contains("Please enter a value from 2 to 6."));
    }

    @Test
    public void testReadPlayerCountBelow4() {
        simulateInput("7\n2\n");
        uiService.readPlayerCount();
        assertTrue(outputStream.toString().contains("Please enter a value from 2 to 6."));
    }

    @Test
    public void testReadPlayerCountNonNumeric() {
        simulateInput("q\n2\n");
        uiService.readPlayerCount();
        assertTrue(outputStream.toString().contains("Please enter a value from 2 to 6."));
    }

    @Test
    public void testReadPlayerName() {
        simulateInput("name\n");
        assertEquals("name", uiService.readPlayerName());
    }

    @Test
    public void testReadPlayerNameInt() {
        simulateInput("name\n");
        assertEquals("name", uiService.readPlayerName(1));
    }

    @Test
    public void testReadPlayerNameNoEntry() {
        simulateInput("\nname\n");
        GameOptions.setNextGamePlayers(getPlayerList());
        assertEquals("name", uiService.readPlayerName());
    }

    @Test
    public void testListExistingPlayers() {
        uiService.listExistingPlayers();
        assertTrue(outputStream.toString().contains("Select an existing player or type 0 to enter a new player:"));
    }

    @Test
    public void testSelectExistingPlayer() {
        simulateInput("1\n");
        GameOptions.setGlobalPlayerList(getPlayerList());
        GameOptions.setNextGamePlayers(new PlayerList());
        uiService.selectExistingPlayer();
        assertEquals(1, GameOptions.getNextGamePlayers().getPlayers().size());
    }

    @Test
    public void testSelectExistingPlayerNonNumeric() {
        simulateInput("q\n1\n");
        GameOptions.setGlobalPlayerList(getPlayerList());
        GameOptions.setNextGamePlayers(new PlayerList());
        uiService.selectExistingPlayer();
        assertEquals(1, GameOptions.getNextGamePlayers().getPlayers().size());
    }

    @Test
    public void testSelectExistingPlayerOutOfBounds() {
        simulateInput("20\n1\n");
        GameOptions.setGlobalPlayerList(getPlayerList());
        GameOptions.setNextGamePlayers(new PlayerList());
        uiService.selectExistingPlayer();
        assertEquals(1, GameOptions.getNextGamePlayers().getPlayers().size());
    }

    @Test
    public void testSelectExistingPlayerCreateNew() {
        simulateInput("0\nname\n");
        uiService.selectExistingPlayer();
        assertEquals("name", GameOptions.getNextGamePlayers().getPlayers().get(2).getName());
    }

    @Test
    public void testAdvanceRound() {
        simulateInput("\n");
        Game game = new Game(uiService, getPlayerList());
        uiService.advanceRound(game);
        assertFalse(game.isAutoRun());
    }

    @Test
    public void testAdvanceRoundSetAutoRun() {
        simulateInput("a\n");
        Game game = new Game(uiService, getPlayerList());
        uiService.advanceRound(game);
        assertTrue(game.isAutoRun());
    }

    @Test
    public void testWriteRoundHeader() {
        uiService.writeRoundHeader(3);
        assertTrue(outputStream.toString().contains("Round 3"));
        assertFalse(outputStream.toString().contains("Results:"));
    }

    @Test
    public void testWriteRoundHeaderResults() {
        uiService.writeRoundHeader(3, "winner");
        assertTrue(outputStream.toString().contains("Round 3"));
        assertTrue(outputStream.toString().contains("Results:"));
        assertTrue(outputStream.toString().contains("WINNER"));
    }

    @Test
    public void testWriteLeaderboard() {
        uiService.writeLeaderBoard();
        assertTrue(outputStream.toString().contains("LEADERBOARD"));
    }

    @Test
    public void testReadPlayAgainY() {
        simulateInput("y\n");
        assertTrue(uiService.readPlayAgain());
    }

    @Test
    public void testReadPlayAgainN() {
        simulateInput("n\n");
        assertFalse(uiService.readPlayAgain());
    }

    @Test
    public void testWritePlayHeaders() {
        uiService.writePlayHeaders();
        assertTrue(outputStream.toString().contains("Player 1"));
    }

    @Test
    public void testWritePlay() {
        playAll();
        uiService.writePlay(getPlayerList(), false, false);
        assertTrue(outputStream.toString().contains(player1.getCurrentPlay().toString()));
    }

    @Test
    public void testWritePlayFaceDown() {
        playAll();
        uiService.writePlay(getPlayerList(), false, true);
        assertTrue(outputStream.toString().contains("****"));
    }

    @Test
    public void testWritePlayTie() {
        playAll();
        uiService.writePlay(getPlayerList(), true);
        assertTrue(outputStream.toString().contains("TIE"));
    }

    @Test
    public void testWriteRoundResults() {
        playAll();
        uiService.writeRoundResults(3, getPlayerList(), "Player 1");
        assertTrue(outputStream.toString().contains("Player 1"));
    }

    private PlayerList getPlayerList() {
        PlayerList playerList = new PlayerList();
        playerList.getPlayers().add(player1);
        playerList.getPlayers().add(player2);
        return playerList;
    }

    private void simulateInput(String string) {
        scanner = new Scanner(new ByteArrayInputStream(string.getBytes()));
        uiService = new UIService(scanner);
    }

    private void playAll() {
        player1.play();
        player2.play();
    }
}
