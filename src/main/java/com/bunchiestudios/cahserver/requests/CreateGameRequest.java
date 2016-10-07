package com.bunchiestudios.cahserver.requests;

import com.bunchiestudios.cahserver.RequestIdentifier;
import com.bunchiestudios.cahserver.ServerRequest;
import com.bunchiestudios.cahserver.database.DataManager;
import com.bunchiestudios.cahserver.database.InvalidQueryException;
import com.bunchiestudios.cahserver.datamodel.Card;
import com.bunchiestudios.cahserver.datamodel.Game;
import com.bunchiestudios.cahserver.datamodel.GameCard;
import com.bunchiestudios.cahserver.datamodel.Player;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.twitter.finagle.http.Method;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the request made by the user when they want to create a new game. Provided a game name, an optional password,
 * and some authentication, the appropriate data structures should be set up.
 */
public class CreateGameRequest extends ServerRequest {

    public CreateGameRequest(DataManager mgr) throws ProcessingException {
        super(mgr, "resource:json/schema/request/CreateGame.json", "resource:json/schema/response/CreateGame.json");
    }

    @Override
    public JSONObject perform(JSONObject message) {
        try {
            if (!requestValid(message))
                return new JSONObject("{\"error\": \"Message format invalid\"}");

            DataManager mgr = getMgr();

            JSONObject user = message.getJSONObject("user"); // All user authentication data is here
            long id = user.getLong("id");
            String token = user.getString("token");
            String gameName = message.getString("gameName");

            Player player = mgr.authenticateUser(id, token);

            if (player == null)
                return new JSONObject("{\"error\": \"User token is invalid or user does not exist.\"}");

            Game game = mgr.addGame(gameName);  // Obtain game information

            if (game == null)
                return new JSONObject("{\"error\": \"There was an error creating the game\"}");

            List<Card> cards = mgr.getCards();                 // Get list of cards to insert into gamecard list
            List<GameCard> gameCards = new ArrayList<>();

            /* Iterate over every card to be added and insert them into the gameCards array */
            for(int i = 0; i < cards.size(); i++) {
                Card nextCard = (i < cards.size() - 1) ? cards.get(i + 1) : null;
                GameCard gc = new GameCard(cards.get(i).getId(), game.getId(), null, nextCard != null ? nextCard.getId() : null, (short)0);
                gameCards.add(gc);
            }

            mgr.addGameCards(gameCards);   // Add all said cards to list of GameCards
            mgr.joinGame(game.getId(), id); // Add player to the game they just created

            /* Form the appropriate JSON response */
            JSONObject response = new JSONObject();
            JSONObject userReponse = new JSONObject();
            JSONObject gameResponse = new JSONObject();

            userReponse.append("id", player.getId());
            gameResponse.append("id", game.getId());

            response.append("user", userReponse);
            response.append("game", gameResponse);

            if(!responseValid(response))
                System.err.println("Error! Reponse for createGameRequest does not follow JSON schema: " + response.toString());

            return response;
        } catch(InvalidQueryException e) {
            System.err.println("There was an unknown error creating game: " + e);
            return new JSONObject("{\"error\": \"There was an unknown error\"}");
        }
    }

    @Override
    public RequestIdentifier getIdentifier() {
        return new RequestIdentifier("/game", "POST");
    }
}
