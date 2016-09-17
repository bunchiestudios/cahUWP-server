package com.bunchiestudios.cahserver.datamodel;

/**
 * Created by rdelfin on 9/11/16.
 */
public class Card {
    private long id;
    private String message;
    private short pickN;
    private boolean black;

    public Card(long id, String message, short pickN, boolean black) {
        this.id = id;
        this.message = message;
        this.pickN = pickN;
        this.black = black;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public short getPickN() {
        return pickN;
    }

    public void setPickN(short pickN) {
        this.pickN = pickN;
    }

    public boolean isBlack() {
        return black;
    }

    public void setBlack(boolean black) {
        this.black = black;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        return id == card.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
