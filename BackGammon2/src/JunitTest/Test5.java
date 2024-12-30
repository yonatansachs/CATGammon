package JunitTest;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import View.Login;

public class Test5 {
		
	//טסט בדיקת שמות
	
    @Before
    public void setGame() {
    	Login.main(null);
    }
	
    @Test
	public void validPlayerNames() {
	    assertNotNull("Player 1 name not included", Login.player1);
	    assertNotNull("Player 2 name not included", Login.player1);
	}
}
