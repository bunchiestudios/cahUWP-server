package com.bunchiestudios.cahserver.datamodel;

/**
 * Created by rdelfin on 9/11/16.
 */
public class Game {
    private long id;
    private String name;
    private Long whiteDeck;
    private Long blackDeck;

    public Game(long id, String name, Long whiteDeck, Long blackDeck) {
        this.id = id;
        this.name = name;
        this.blackDeck = blackDeck;
        this.whiteDeck = whiteDeck;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getWhiteDeck() {
        return whiteDeck;
    }

    public void setWhiteDeck(Long whiteDeck) {
        this.whiteDeck = whiteDeck;
    }

    public Long getBlackDeck() {
        return blackDeck;
    }

    public void setBlackDeck(Long blackDeck) {
        this.blackDeck = blackDeck;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return id == game.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
