import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.junit.Test;


public class Testing {

	@Test
	public void testDice() {
		Random random = new Random();
	    Map<Integer, Integer> distribution = new HashMap<>();
	    for (int i = 1; i <= 6; i++) {
	        distribution.put(i, 0);
	    }

	    for (int i = 0; i < 1000; i++) {
	        int roll = random.nextInt(6) + 1;
	        assertTrue("Dice roll out of range", roll >= 1 && roll <= 6);
	        distribution.put(roll, distribution.get(roll) + 1);
	    }

	    // Check that all values were rolled
	    for (int count : distribution.values()) {
	        assertTrue("Distribution is not uniform enough", count > 100);
	    }
	}
}

