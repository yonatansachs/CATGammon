package JunitTest;
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

public class Test1 {
	
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