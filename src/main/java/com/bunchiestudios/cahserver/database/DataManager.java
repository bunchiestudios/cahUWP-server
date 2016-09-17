package com.bunchiestudios.cahserver.database;

import com.bunchiestudios.cahserver.datamodel.Card;
import com.bunchiestudios.cahserver.datamodel.Game;
import com.bunchiestudios.cahserver.datamodel.GameCard;
import com.twitter.util.Future;
import scala.runtime.AbstractFunction1;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This class converts query results into datamodel objects, and posts/updates entries based on datamodel objects.
 */
public class DataManager {
    private HerokuDatabase database;

    public DataManager(HerokuDatabase database) {
        this.database = database;
    }

    /**
     * Obtains a complete list of all cards that can be used in the game.
     * @throws InvalidQueryException This is thrown when the query gets an unexpected result, such as a SQL error or
     *                               an invalid type when getting value.
     */
    public List<Card> getCards() throws InvalidQueryException {
        return getCards(new ArrayList<>());
    }


    /**
     * Obtains a list of all cards with a given set of ID's.
     * @param ids The list of card ID's to query for.
     * @throws InvalidQueryException This is thrown when the query gets an unexpected result, such as a SQL error or
     *                               an invalid type when getting value.
     */
    public List<Card> getCards(List<Long> ids) throws InvalidQueryException {
        StringBuilder query = new StringBuilder();

        if(ids.size() == 0) {
            query.append("SELECT id, message, pick_n, black FROM cards");
        }
        else {
            query = new StringBuilder("SELECT id, message, pick_n, black FROM cards WHERE id IN (");
            for (int i = 0; i < ids.size(); i++) {
                query.append('?');
                if (i < ids.size() - 1)
                    query.append(", ");
            }
            query.append(')');
        }

        List<Object> params = new ArrayList<>(ids.size());
        params.addAll(ids);

        try {
            List<Card> result = new ArrayList<>();
            ResultSet rs = database.getQuery(query.toString(), params);

            while(rs.next())
                result.add(new Card(rs.getLong("id"), rs.getString("message"), rs.getShort("pick_n"), rs.getBoolean("black")));

            return result;
        } catch (SQLException e) {
            throw new InvalidQueryException(query.toString(), e);
        }
    }

    /**
     * Creates a new game.
     * @param name Name (unique) of the game
     * @return The new game representation, with the unique identifier and name.
     */
    public Game addGame(String name) {
        String query = "INSERT INTO game (name) VALUES (?)";

        try {
            database.executeQuery(query, Arrays.asList(name));
            return getGame(name);
        } catch(SQLException e) {
            System.err.println("There was an error when adding game: " + e);
            return null;
        }
    }

    /**
     * Obtains game uniquely identified by the name parameter.
     * @return A game object if it exists, null otherwise
     */
    public Game getGame(String name) {
        String query = "SELECT id, name, black_deck, white_deck FROM game WHERE name=?";

        try {
            ResultSet rs = database.getQuery(query, Arrays.asList(name));

            if(rs.next())
                return new Game(rs.getLong("id"),
                        rs.getString("name"),
                        (Long)rs.getObject("white_deck"),
                        (Long)rs.getObject("black_deck"));
            else
                return null;
        } catch(SQLException e) {
            System.err.println("There was an error when getting game: " + e);
            return null;
        }
    }

    /**
     * Obtains game uniquely identified by the id provided.
     * @return A game object if it exists, null otherwise
     */
    public Game getGame(long id) {
        String query = "SELECT id, name, white_deck, black_deck FROM game WHERE id=?";

        try {
            ResultSet rs = database.getQuery(query, Arrays.asList(id));

            if(rs.next())
                return new Game(rs.getLong("id"),
                        rs.getString("name"),
                        (Long)rs.getObject("white_deck"),
                        (Long)rs.getObject("black_deck"));
            else
                return null;
        } catch(SQLException e) {
            System.err.println("There was an error when getting game: " + e);
            return null;
        }
    }

    /**
     * Adds a player to a given game.
     * @param gameId ID uniquely identifying the game to join.
     * @param playerId ID identifying the player who wants to join.
     * @return True if successful, false otherwise
     */
    public boolean joinGame(long gameId, long playerId) {
        Game game = getGame(gameId);

        if(game == null)
            return false;

        String query = "UPDATE player SET game_id=? WHERE player_id=?";

        try {
            database.executeQuery(query, Arrays.asList(gameId, playerId));
            return true;
        } catch(SQLException e) {
            System.err.println("There was an error when joining game: " + e);
            return false;
        }
    }

    /**
     * Obtains the next n cards to be drawn from the white card deck and sets the game's white_card property to
     * the next card to draw.
     * @param gameId Game's unique identifier used to find the deck
     * @param n Number of cards to draw
     * @return A list of n GameCards drawn, or an empty list if an error occured.
     */
    public List<GameCard> drawNWhiteCards(long gameId, int n) {
        return drawNCards(gameId, n, "white_deck");
    }

    /**
     * Draws a single, black card from the deck and updates the black_deck property accordingly.
     * @param gameId Game's unique identifier used to find the deck
     * @return A game card object, which is optionally null if no card was found.
     */
    public GameCard drawBlackCard(long gameId) {
        List<GameCard> cards = drawNCards(gameId, 1, "black_deck");
        return (cards.size() == 0 ? null : cards.get(0));
    }

    /**
     * Assigns a drawn card to a given user.
     * @param playerId The unique identifier for the user getting the card
     * @param gameId The game's unique identifier.
     * @param cardId The card's unique identifier.
     * @return True if operation was successful, false otherwise.
     */
    public boolean assignCard(long playerId, long gameId, long cardId) {
        String query = "UPDATE game_cards SET player_id=?, daisy_chain=NULL WHERE game_id=? AND card_id=?";
        try {
            database.executeQuery(query, Arrays.asList(playerId, gameId, cardId));
        } catch (SQLException e) {
            System.err.println("There was an error when assigning card to user: " + e);
            return false;
        }

        return true;
    }

    /**
     * Sets the card as played by setting status to 1.
     * @return True if successful, false otherwise.
     */
    public boolean playCard(long gameId, long cardId) {
        String query = "UPDATE game_cards SET status=1 WHERE game_id=? AND card_id=?";
        try {
            database.executeQuery(query, Arrays.asList(gameId, cardId));
        } catch (SQLException e) {
            System.err.println("Error while setting cared as played: " + e);
            return false;
        }

        return true;
    }

    /**
     * Indicates whether a given card is in the player's hand
     * @param playerId Player's unique identifier.
     * @param cardId Card's unique identifier.
     * @return True if present. False otherwise.
     */
    public boolean inHand(long playerId, long cardId) {
        String query = "SELECT * FROM game_cards WHERE player_id=? AND card_id=? and game_id IN (" +
                    "SELECT game_id FROM player WHERE player_id=?" +
                ")";
        try {
            ResultSet rs = database.getQuery(query, Arrays.asList(playerId, cardId, playerId));
            return rs.next();
        } catch(SQLException e) {
            System.err.println("There was an error when checking card in hand: " + e);
            return false;
        }
    }

    public List<GameCard> getHand(long playerId, long gameId) {
        String query = "SELECT card_id, game_id, player_id, daisy_chain, status FROM game_cards WHERE player_id=? AND game_id=?";
        List<GameCard> result = new ArrayList<>();

        try {
            ResultSet rs = database.getQuery(query, Arrays.asList(playerId, gameId));

            while(rs.next()) {
                GameCard c = new GameCard(rs.getLong("card_id"),
                        rs.getLong("game_id"),
                        (Long)rs.getObject("player_id"),    // Handles NULL case
                        (Long)rs.getObject("daisy_chain"),  // Handles NULL case
                        rs.getShort("status"));
                result.add(c);
            }
        } catch (SQLException e) {
            System.err.println("There was an error fetching hand: " + e);
            return new ArrayList<>();
        }

        return result;
    }

    /**
     * Discards a card by setting player_id to NULL and status to 0.
     * @param gameId The game's unique identifier.
     * @param cardId The card's unique identifier.
     * @return True if the operation was successful, false otherwise.
     */
    public boolean discardCard(long gameId, long cardId) {
        String query = "UPDATE game_cards SET player_id=NULL, status=0 WHERE game_id=? AND card_id=?";
        try {
            database.executeQuery(query, Arrays.asList(gameId, cardId));
        } catch (SQLException e) {
            System.err.println("There was an error discarding card: " + e);
            return false;
        }

        return true;
    }

    /**
     * Discards all cards marked as currently played in a given game.
     * @param gameId Game identifier.
     * @return True if successful, false otherwise.
     */
    public boolean discardAllPlayed(long gameId) {
        String query = "UPDATE game_cards SET player_id=NULL, status=0 WHERE game_id=? AND status=1 AND player_id IS NOT NULL";
        try {
            database.executeQuery(query, Arrays.asList(gameId));
        } catch (SQLException e) {
            System.err.println("There was an error discarding all played cards: " + e);
            return false;
        }

        return true;
    }


    /**
     * Helper method to arbitrarily pick out n cards from either the white or black deck. It also updates the
     * deck so that the next picked card gets updated in the object.
     * @param gameId Unique identifier for the game
     * @param n Number of cards to draw
     * @param deck Deck name. Either white_deck or black_deck. Any other value will probably result in a SQLException
     *             and an empty result list.
     * @return A list with n cards if they were all extracted successfully or an empty list otherwise.
     */
    private List<GameCard> drawNCards(long gameId, int n, String deck) {
        String getFirstQuery =
                "SELECT card_id, game_id, player_id, daisy_chain, status FROM game_cards " +
                        "WHERE game_id=? AND card_id IN (" +
                        "SELECT " + deck + " FROM game WHERE id=?" +
                        ")";
        String getNthQuery = "SELECT card_id, game_id, player_id, daisy_chain, status FROM game_cards " +
                "WHERE game_id=? AND card_id=?";
        String updateNextCard = "UPDATE game SET " + deck + "=? WHERE id=?";

        List<GameCard> result = new ArrayList<>();

        // First, attempt to fetch first card using the white_deck property of game.
        try {
            ResultSet rs = database.getQuery(getFirstQuery, Arrays.asList(gameId, gameId));

            if(rs.next()) {
                GameCard c = new GameCard(rs.getLong("card_id"),
                        rs.getLong("game_id"),
                        (Long)rs.getObject("player_id"),
                        (Long)rs.getObject("daisy_chain"),
                        rs.getShort("status"));
                result.add(c);
            } else {
                // In case no cards are found, return an empty list
                return new ArrayList<>();
            }
        } catch (SQLException e) {
            System.err.println("There was an error when drawing the first card: " + e);
            return new ArrayList<>();
        }

        // Proceed to try and fetch the other n - 1 cards.
        for(int i = n - 1; i > 0; i--) {
            try {
                ResultSet rs = database.getQuery(getNthQuery, Arrays.asList(gameId, result.get(result.size() - 1).getDaisyChain()));
                if(rs.next()) {
                    GameCard c = new GameCard(rs.getLong("card_id"),
                            rs.getLong("game_id"),
                            (Long)rs.getObject("player_id"),
                            (Long)rs.getObject("daisy_chain"),
                            rs.getShort("status"));
                    result.add(c);
                } else {
                    // Again, if next card does not exist, abort
                    return new ArrayList<>();
                }

            } catch (SQLException e) {
                System.err.println("There was an error when drawing card #" + (n - i + 1) + ": " + e);
                return new ArrayList<>();
            }
        }

        // Finally, try and update the game's white_deck property to the new next card.
        try {
            database.executeQuery(updateNextCard, Arrays.asList(result.get(result.size() - 1).getDaisyChain(), gameId));
        } catch (SQLException e) {
            System.err.println("There was an error updating the " + deck + " property: " + e);
            return new ArrayList<>();
        }

        return result;
    }
}
