package JunitTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import application.Backgammon;

public class Test7 {

    private Backgammon backgammon;

    @Before
    public void setUp() {
        backgammon = new Backgammon();
        backgammon.setDifficulty("Easy"); // Set the difficulty (optional)
    }

    @Test
    public void testDiceRoll() {
        // Simulate rolling the dice by calling the instance method rollDice
        int diceFirst = backgammon.rollDice("Easy");
        int diceSecond = backgammon.rollDice("Easy");

        // Assert that dice rolls are within the valid range
        assertTrue("Dice roll should be between 1 and 6", diceFirst >= 1 && diceFirst <= 6);
        assertTrue("Dice roll should be between 1 and 6", diceSecond >= 1 && diceSecond <= 6);

        // Optionally print the rolled values
        System.out.println("Dice 1: " + diceFirst);
        System.out.println("Dice 2: " + diceSecond);
    }
}
