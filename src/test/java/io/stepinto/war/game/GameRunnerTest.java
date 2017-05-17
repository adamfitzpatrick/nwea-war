package io.stepinto.war.game;

import io.stepinto.war.player.Player;
import io.stepinto.war.player.PlayerList;
import io.stepinto.war.ui.UIService;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.partialMockBuilder;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

/**
 * @author adam.fitzpatrick
 */
public class GameRunnerTest {
    UIService uiService;
    GameRunner gameRunner;

    @Before
    public void setup() {
        uiService = partialMockBuilder(UIService.class)
                .addMockedMethod("readPlayerCount")
                .addMockedMethod("readPlayerName", int.class)
                .addMockedMethod("advanceRound")
                .addMockedMethod("readPlayAgain")
                .createMock();
        PlayerList playerList = new PlayerList();
        playerList.getPlayers().add(new Player("Player"));
        uiService.setPlayerList(playerList);
        gameRunner = new GameRunner(uiService);
    }

    @Test
    public void testRunGames() {
        GameOptions.setPlayerCount(2);
        uiService.readPlayerCount();
        expectLastCall();
        expect(uiService.readPlayerName(anyInt())).andReturn("player").times(2);
        uiService.advanceRound(anyObject(Game.class));
        expectLastCall().anyTimes();
        expect(uiService.readPlayAgain()).andReturn(false);
        replay(uiService);

        gameRunner.runGames();
        verify(uiService);
    }
}
