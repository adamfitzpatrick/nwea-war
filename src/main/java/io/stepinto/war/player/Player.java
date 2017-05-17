package io.stepinto.war.player;

import io.stepinto.war.card.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author adam.fitzpatrick
 */
public class Player {

    @Getter
    private final String name;

    @Getter
    @Setter
    private List<Card> hand;

    @Getter
    private Card currentPlay;

    @Getter
    private int cardsWon;

    @Getter
    private int roundsWon;

    @Getter
    private int gamesWon;

    @Getter
    private int totalCardsWon;

    @Getter
    private int totalRoundsWon;

    public Player(String name) {
        this.name = name;
    }

    public void play() {
        if (hand.size() > 0) {
            currentPlay = hand.remove(0);
        } else if(this.currentPlay != null) {
            currentPlay = new Card(this.currentPlay.getSuit(), null);
        }
    }

    public void resetPlay() {
        this.currentPlay = new Card(this.currentPlay.getSuit(), null);
    }

    public void recordRoundWin(int cardsWon) {
        this.cardsWon += cardsWon;
        roundsWon++;
    }

    public void recordGame(boolean victory) {
        if (victory) {
            gamesWon++;
        }
        totalCardsWon += cardsWon;
        totalRoundsWon += roundsWon;
        roundsWon = 0;
        cardsWon = 0;
    }
}
