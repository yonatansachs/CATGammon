package JunitTest;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import Control.GamePlay;
import View.Login;
import application.Backgammon;

public class Test4 {
	
    @Before
    public void setGame() {
    	Login.main(null);
    }
	
	//טסט אתחול משחק
	//Ensures GamePlay initializes correctly with the specified difficulty.
    @Test
    public void testInitialization() {
        assertNotNull("GamePlay object should not be null", Backgammon.theGame);
        assertEquals("Difficulty should be set to Medium", "Medium", GamePlay.difficulty);
    }


}
