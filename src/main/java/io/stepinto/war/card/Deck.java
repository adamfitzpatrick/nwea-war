package io.stepinto.war.card;

import io.stepinto.war.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author adam.fitzpatrick
 */
public class Deck {

    public static void deal(List<Player> players) {
        List<Card> cards = Deck.composeCards();
        Collections.shuffle(cards, new Random(System.nanoTime()));
        int cardCount = 52 / players.size();
        IntStream.range(0, players.size()).forEach(index -> {
            int start = index * cardCount;
            players.get(index).setHand(new ArrayList<>(cards.subList(start, start + cardCount)));
        });
    }

    private static List<Card> composeCards() {
        return Arrays.stream(Suit.values())
                .map(Deck::composeSuit)
                .flatMap(suitStack -> suitStack.stream())
                .collect(Collectors.toList());
    }

    private static List<Card> composeSuit(Suit suit) {
        return Arrays.stream(Face.values()).map(face -> new Card(suit, face)).collect(Collectors.toList());
    }
}
