package com.bunchiestudios.cahserver.datamodel;

/**
 * Created by rdelfin on 9/11/16.
 */
public class GameCard {
    private long cardId;
    private long gameId;
    private Long playerId;
    private Long daisyChain;
    private short status;

    public GameCard(long cardId, long gameId, Long playerId, Long daisyChain, short status) {
        this.cardId = cardId;
        this.gameId = gameId;
        this.playerId = playerId;
        this.daisyChain = daisyChain;
        this.status = status;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public long getDaisyChain() {
        return daisyChain;
    }

    public void setDaisyChain(long daisyChain) {
        this.daisyChain = daisyChain;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameCard gameCard = (GameCard) o;

        if (cardId != gameCard.cardId) return false;
        return gameId == gameCard.gameId;

    }

    @Override
    public int hashCode() {
        int result = (int) (cardId ^ (cardId >>> 32));
        result = 31 * result + (int) (gameId ^ (gameId >>> 32));
        return result;
    }
}
