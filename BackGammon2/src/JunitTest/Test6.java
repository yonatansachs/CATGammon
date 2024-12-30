package JunitTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Control.GamePlay;
import View.Login;

public class Test6 {

	 @Before
	    public void setGame() {
	    	Login.main(null);
	    }
	    //Verifies random question spots are selected within a valid range.
	    @Test
	    public void testQuestionSpotSelection() {
	        int[] questions = GamePlay.getQuestions();
	        assertEquals("There should be 3 question spots selected", 3, questions.length);
	        for (int question : questions) {
	            assertTrue("Question spot should be in valid range (0-23)", question >= 0 && question < 24);
	        }
	    }

}
