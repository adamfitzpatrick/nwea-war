package io.stepinto.war.card;

import lombok.Getter;

/**
 * @author adam.fitzpatrick
 */
public class Card implements Comparable<Card> {

    @Getter
    private final Suit suit;

    @Getter
    private final Face face;

    public Card(Suit suit, Face face) {
        this.suit = suit;
        this.face = face;
    }

    @Override
    public int compareTo(Card other) {
        if (face == null) {
            return -1;
        } else if (other.getFace() == null) {
            return 1;
        }
        return face.getValue() - other.getFace().getValue();
    }

    @Override
    public String toString() {
        if (face == null) {
            return "----";
        }
        return face.toString() + " of " + suit.name();
    }
}
