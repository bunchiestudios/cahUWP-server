package com.bunchiestudios.cahserver.requests;

import com.bunchiestudios.cahserver.database.DataManager;
import com.bunchiestudios.cahserver.datamodel.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * This class will test the GetAllCardsRequest class
 */
public class GetAllCardsRequestTest {
    DataManager mock;

    @Before
    public void setUp() throws Exception {
        mock = Mockito.mock(DataManager.class);
        Mockito.when(mock.authenticateUser(Mockito.anyLong(), Mockito.anyString())).thenReturn(new Player(1, "me", "hax0r", 1L));
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void perform() throws Exception {

    }

}