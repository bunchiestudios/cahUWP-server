package com.bunchiestudios.cahserver.datamodel;

/**
 * Created by rdelfin on 9/11/16.
 */
public class Player {
    private long id;
    private String name;
    private String token;
    private long gameId;

    public Player(long id, String name, String token, long gameId) {
        this.id = id;
        this.name = name;
        this.token = token;
        this.gameId = gameId;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return id == player.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
