package io.stepinto.war.card;

/**
 * Created by adam.fitzpatrick on 5/15/17.
 */
public enum Face {
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    J(11),
    Q(12),
    K(13),
    A(14);

    private final int value;

    Face(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        switch (this.value) {
            case 11:
            case 12:
            case 13:
            case 14:
                return this.name();
            default:
                return Integer.toString(this.value);
        }
    }
}
