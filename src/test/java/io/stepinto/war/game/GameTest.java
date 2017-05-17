package io.stepinto.war.game;

import io.stepinto.war.card.Card;
import io.stepinto.war.card.Face;
import io.stepinto.war.card.Suit;
import io.stepinto.war.player.Player;
import io.stepinto.war.player.PlayerList;
import io.stepinto.war.ui.UIService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.partialMockBuilder;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

/**
 * @author adam.fitzpatrick
 */
public class GameTest {
    Game game;
    UIService uiService;
    Player player1;
    Player player2;
    Player player3;

    @Before
    public void setup() {
        uiService = partialMockBuilder(UIService.class).addMockedMethod("advanceRound").createMock();
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        player3 = new Player("Player 3");
        PlayerList playerList = new PlayerList();
        playerList.getPlayers().add(player1);
        playerList.getPlayers().add(player2);
        playerList.getPlayers().add(player3);
        game = new Game(uiService, playerList);
        player1.setHand(new ArrayList<>(Arrays.asList(new Card(Suit.Clubs, Face.FIVE))));
        player2.setHand(new ArrayList<>(Arrays.asList(new Card(Suit.Clubs, Face.SIX))));
        player3.setHand(new ArrayList<>(Arrays.asList(new Card(Suit.Clubs, Face.FOUR))));
    }

    @Test
    public void testPlayGame() {
        uiService.advanceRound(anyObject(Game.class));
        expectLastCall();
        replay(uiService);

        game.playGame();
        assertEquals(1, player2.getGamesWon());
        verify(uiService);
    }

    @Test
    public void testPlayGameTie() {
        player2.setHand(new ArrayList<>(Arrays.asList(new Card(Suit.Clubs, Face.SIX), new Card(Suit.Clubs, Face.TWO))));
        player3.setHand(new ArrayList<>(Arrays.asList(new Card(Suit.Clubs, Face.SIX), new Card(Suit.Clubs, Face.A))));

        uiService.advanceRound(anyObject(Game.class));
        expectLastCall();

        game.playGame();
        assertEquals(1, player3.getGamesWon());
    }

    @Test
    public void testPlayGameAutoRun() {
        game.playGame();
        assertEquals(1, player2.getGamesWon());
    }
}
