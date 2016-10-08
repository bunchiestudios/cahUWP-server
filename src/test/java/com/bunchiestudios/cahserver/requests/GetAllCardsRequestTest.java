package com.bunchiestudios.cahserver.requests;

import com.bunchiestudios.cahserver.database.DataManager;
import com.bunchiestudios.cahserver.datamodel.Card;
import com.bunchiestudios.cahserver.datamodel.Player;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * This class will test the GetAllCardsRequest class
 */
public class GetAllCardsRequestTest {
    DataManager mgr;
    GetAllCardsRequest req;
    JSONObject cardsResponse;

    @Before
    public void setUp() throws Exception {
        // All, of course, actual CAH cards
        List<Card> cards;
        cards = new ArrayList<>();
        cards.add(new Card(1, "Cards Against Humanity.", (short)1, false));
        cards.add(new Card(2, "Three dicks at the same time.", (short)1, false));
        cards.add(new Card(3, "Not wearing pants.", (short)1, false));
        cards.add(new Card(4, "How did I lose my virginity?", (short)1, true));

        // Generate the appropriate JSON from list of cards
        StringBuilder cardsResponseBuilder = new StringBuilder();
        cardsResponseBuilder.append("{\"user\":{\"id\": 4242}, \"cards\":[");
        for(Card c : cards) {  // Generate each card object
            cardsResponseBuilder.append("{\"id\": ").append(c.getId()).append(", \"message\": \"")
                                .append(c.getMessage()).append("\", pick_n: ").append(c.getPickN())
                                .append(", black:").append(c.isBlack() ? "true" : "false")
                                .append("}");
        }
        cardsResponseBuilder.append("]}");
        cardsResponse = new JSONObject(cardsResponseBuilder.toString());

        // Mockito: Lets you fake method calls. Basically intercepts calls to mgr and returns whatever you specify
        mgr = Mockito.mock(DataManager.class);
        Mockito.when(mgr.authenticateUser(Mockito.anyLong(), Mockito.anyString())).thenReturn(new Player(1, "me", "hax0r", 1L));
        Mockito.when(mgr.getCards()).thenReturn(cards);

        // What we actually use
        req = new GetAllCardsRequest(mgr);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void performTest() throws Exception {
        JSONObject res1 = req.perform(new JSONObject("{\"user\": {\"id\": 4242, \"token\": \"watwat\"}}"));    // Valid request
        JSONObject res2 = req.perform(new JSONObject(""));                                                     // Empty request
        JSONObject res3 = req.perform(new JSONObject("{\"valid\": true}"));                                    // Valid JSON, invalid request
        JSONObject res4 = req.perform(new JSONObject("{\"user\": {\"id\": \"100\", \"token\":\"wat\"}}"));     // Correct structure, incorrect types
        JSONObject res5 = req.perform(new JSONObject("{\"user\": {\"id\": 100, \"token\":4242}}"));            // Correct structure, incorrect types

        // First should give correct response. All others should error out
        assertTrue(res1.equals(cardsResponse));
        assertTrue(!res2.equals(cardsResponse));
        assertTrue(!res3.equals(cardsResponse));
        assertTrue(!res4.equals(cardsResponse));
        assertTrue(!res5.equals(cardsResponse));

        // Ensure they all have an error field
        // ...somewhere
        assertNotNull(res2.get("error"));
        assertNotNull(res3.get("error"));
        assertNotNull(res4.get("error"));
        assertNotNull(res5.get("error"));



    }

}