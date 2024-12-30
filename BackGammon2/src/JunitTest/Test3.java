package JunitTest;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import Model.SysData;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

public class Test3 {
	
	//טסט סיס דאטה
	//Basic check that SysData ok - get SysData class
		private static SysData check;

	    @Test
	    public void testSysData() {
	        check = SysData.getInstance();
	        assertNotNull("SysData instance should not be null", check);
	    }

}
