import static org.junit.Assert.*;
import org.junit.Before;
import Control.GamePlay;
import Model.SysData;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import static org.junit.Assert.assertNotNull;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestFirst {

    //Basic check that SysData ok - get SysData class
	private static SysData check;

    @BeforeClass
    public static void setUpClass() throws InterruptedException {
        // Initialize JavaFX Toolkit
        new JFXPanel(); // Ensures JavaFX platform initializes
        // Run on JavaFX Thread
        Platform.runLater(() -> check = SysData.getInstance());
        Stage stage = new Stage();
        // Wait for JavaFX Thread to complete
        Thread.sleep(1000);
    }

    @Test
    public void testSysData() {
        assertNotNull("SysData instance should not be null", check);
    }


    /*SysData check = SysData.getInstance();
    @Test
    public void testSysData() {
    	assertNotNull(check);
    }*/
    
    //need to fill this test
	@Test
	public void validRollDice() {
		fail("Not yet implemented");
	}
	
    //need to fill this test
	public void validPlayerNames() {
		fail("Not yet implemented");
	}
	
	    private GamePlay gamePlay;
	    private GridPane[] gridPanes;
	    private Stage stage;

	    @Before
	    public void setUp() {
	        // Mocking Stage and GridPane array
	        stage = new Stage();
	        gridPanes = new GridPane[24];
	        for (int i = 0; i < 24; i++) {
	            gridPanes[i] = new GridPane();
	        }
	        gamePlay = new GamePlay(gridPanes, stage, "Medium");
	    }

	    //Ensures GamePlay initializes correctly with the specified difficulty.
	    @Test
	    public void testInitialization() {
	        assertNotNull("GamePlay object should not be null", gamePlay);
	        assertEquals("Difficulty should be set to Medium", "Medium", GamePlay.difficulty);
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

	    //Tests placing a pawn upwards.
	    @Test
	    public void testSurpriseSpotSelection() {
	        if (GamePlay.difficulty.equals("Hard")) {
	            assertTrue("Surprise spot should be in valid range (0-23)", GamePlay.surprise >= 0 && GamePlay.surprise < 24);
	        }
	    }

	    //Tests placing a pawn upwards.
	    @Test
	    public void testSetUp() {
	        gamePlay.setUp(gridPanes, 5, 1, true);
	        int[] blueUp = gamePlay.getBlueUp();
	        assertEquals("BlueUp at column 5 should have 1 pawn", 1, blueUp[5]);
	    }

	    //Tests placing a pawn downwards.
	    @Test
	    public void testSetDown() {
	        gamePlay.setDown(gridPanes, 7, 1, false);
	        int[] blackDown = gamePlay.getBlackDown();
	        assertEquals("BlackDown at column 7 should have 1 pawn", 1, blackDown[7]);
	    }

	    //Ensures a pawn is correctly removed upwards.
	    @Test
	    public void testRemoveUp() {
	        gamePlay.setUp(gridPanes, 10, 1, true);
	        gamePlay.removeUp(gridPanes, 10, true);
	        int[] blueUp = gamePlay.getBlueUp();
	        assertEquals("BlueUp at column 10 should have 0 pawns after removal", 0, blueUp[10]);
	    }

	    //Ensures a pawn is correctly removed downwards.
	    @Test
	    public void testRemoveDown() {
	        gamePlay.setDown(gridPanes, 12, 1, false);
	        gamePlay.removeDown(gridPanes, 12, false);
	        int[] blackDown = gamePlay.getBlackDown();
	        assertEquals("BlackDown at column 12 should have 0 pawns after removal", 0, blackDown[12]);
	    }

	    //Checks how many pawns are in a specific column.
	    @Test
	    public void testHowManyContains() {
	        gamePlay.setUp(gridPanes, 15, 2, true);
	        int count = gamePlay.howManyContains(gridPanes, 15);
	        assertEquals("Column 15 should have 2 pawns", 2, count);
	    }
	    
	    
	}