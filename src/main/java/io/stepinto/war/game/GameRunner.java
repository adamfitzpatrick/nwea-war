package io.stepinto.war.game;

import io.stepinto.war.player.Player;
import io.stepinto.war.player.PlayerList;
import io.stepinto.war.ui.UIService;

/**
 * @author adam.fitzpatrick
 */
public class GameRunner {
    private UIService uiService;

    public GameRunner(UIService uiService) {
        this.uiService = uiService;
    }

    public void runGames() {
        uiService.showGameIntro();
        GameOptions.setPlayingAgain(true);

        while (GameOptions.isPlayingAgain()) {
            startNewGame();
            GameOptions.reset();
            uiService.writeLeaderBoard();

            GameOptions.setPlayingAgain(uiService.readPlayAgain());
        }
    }

    private void startNewGame() {
        GameOptions.setNextGamePlayers(new PlayerList());

        uiService.readPlayerCount();
        for (int k = 0; k < GameOptions.playerCount; k++) {
            if (GameOptions.getAvailableGlobalPlayers().size() > 0) {
                uiService.listExistingPlayers();
                uiService.selectExistingPlayer();
            } else {
                GameOptions.getNextGamePlayers().getPlayers().add(new Player(uiService.readPlayerName(k)));
            }
        }
        GameOptions.updateGlobalList();

        Game game = new Game(uiService, GameOptions.getNextGamePlayers());
        uiService.writeGameFlavor();

        game.playGame();
    }
}
