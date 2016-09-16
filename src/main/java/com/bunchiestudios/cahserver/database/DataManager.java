package com.bunchiestudios.cahserver.database;

import com.bunchiestudios.cahserver.datamodel.Card;
import com.bunchiestudios.cahserver.datamodel.Game;
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

    public Game getGame(String name) {
        String query = "SELECT id, name FROM game WHERE name=?";

        try {
            ResultSet rs = database.getQuery(query, Arrays.asList(name));

            if(rs.next())
                return new Game(rs.getLong("id"), rs.getString("name"));
            else
                return null;
        } catch(SQLException e) {
            System.err.println("There was an error when getting game: " + e);
            return null;
        }
    }

    public Game getGame(long id) {
        String query = "SELECT id, name FROM game WHERE id=?";

        try {
            ResultSet rs = database.getQuery(query, Arrays.asList(id));

            if(rs.next())
                return new Game(rs.getLong("id"), rs.getString("name"));
            else
                return null;
        } catch(SQLException e) {
            System.err.println("There was an error when getting game: " + e);
            return null;
        }
    }

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

    public List<Card> drawNWhiteCards(long gameId, int n) {
        String query
    }
}
