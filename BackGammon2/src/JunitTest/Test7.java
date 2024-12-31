package JunitTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import View.Login;
import application.Backgammon;

public class Test7 {

		 @Before
		    public void setGame() {
		    	Login.main(null);
		    }

	    // בדיקה שכל קוביה מחזירה ערך בין 1 ל-6
	    @Test
	    public void testDiceRoll() {
	        for (int i = 0; i < 100; i++) { // להריץ מספר בדיקות לוודא מקרים שונים
	            int dicefirst = Backgammon.dice1;
	            int dicesec = Backgammon.dice2;
	            assertTrue("Dice roll should be between 1 and 6", dicefirst >= 1 && dicefirst <= 6);
	            assertTrue("Dice roll should be between 1 and 6", dicesec >= 1 && dicesec <= 6);

	        }
	    }
}