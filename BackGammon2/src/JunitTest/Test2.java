package JunitTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Control.GamePlay;
import View.Login;

public class Test2 {
	
    @Before
    public void setGame() {
    	Login.main(null);
    }
	
	//טסט מיקומים להפתעות
	 //Tests placing a pawn upwards.
    @Test
    public void testSurpriseSpotSelection() {
    	//gamePlay = new GamePlay(null, GamePlay.mainStage, "Hard");
        if (GamePlay.difficulty.equals("Hard")) {
            assertTrue("Surprise spot should be in valid range (0-23)", GamePlay.surprise >= 0 && GamePlay.surprise < 24);
        }
    }

}
