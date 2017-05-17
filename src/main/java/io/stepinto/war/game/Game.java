package io.stepinto.war.game;

import io.stepinto.war.card.Deck;
import io.stepinto.war.player.Player;
import io.stepinto.war.player.PlayerList;
import io.stepinto.war.ui.UIService;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author adam.fitzpatrick
 */
public class Game {

    @Getter
    private PlayerList playerList;

    @Getter
    private boolean complete;

    @Getter
    @Setter
    private boolean autoRun;

    private int round;

    private Player winner;

    private UIService uiService;

    public Game(UIService uiService, PlayerList playerList) {
        this.uiService = uiService;
        this.playerList = playerList;
        this.uiService.setPlayerList(playerList);
        Deck.deal(playerList.getPlayers());
    }

    public void playGame() {
        while (!this.complete) {
            this.playRound();
            uiService.writeRoundResults(this.round, this.getPlayerList(), this.winner.getName());

            if (!this.autoRun) {
                uiService.advanceRound(this);
            }

            if (playerList.availablePlayers() <= 1) {
                this.endGame();
            }
        }
    }

    private void playRound() {
        this.round++;
        uiService.writeRoundHeader(this.round);

        uiService.writePlayHeaders();
        int playedCards = playerList.playAll();

        List<Player> leaders = playerList.getRoundLeaders();
        uiService.writePlay(playerList, leaders.size() > 1);

        while (leaders.size() > 1) {
            playerList.resetPlays();
            playedCards += playerList.playList(leaders);
            uiService.writePlay(playerList, false, true);

            playedCards += playerList.playList(leaders);
            leaders = playerList.getRoundLeaders();
            uiService.writePlay(playerList, leaders.size() > 1);
        }
        this.winner = leaders.get(0);
        this.winner.recordRoundWin(playedCards);
    }

    private void endGame() {
        Player winner = playerList.getPointsLeader();
        winner.recordGame(true);
        playerList.getPlayers().stream()
                .filter(player -> player != winner)
                .forEach(player -> player.recordGame(false));
        complete = true;
    }
}
