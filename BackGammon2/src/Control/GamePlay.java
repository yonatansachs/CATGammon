package Control;

import Model.Pawns;
import View.Login;
import View.QuestionScreen;
import View.SurprisePopUp;
import application.Backgammon;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;

import java.awt.Label;
import java.io.*;
import java.util.*;

/**
 * GamePlay class that:
 *   1) Asks a question each turn if difficulty == "Hard".
 *   2) Forces re-entry of eaten pawns before any other moves.
 *   3) Supports negative dice, capturing single pawns, etc.
 *   4) When a pawn is clicked, only the remaining valid dice will be available as options.
 */
public class GamePlay extends Pawns {

    public static String difficulty;
    private Stage mainStage;
    private final Pawns pawn = new Pawns();
    
    // Introduce separate extra turn flags for Blue and Black
    private boolean blueExtraTurnGranted = false;
    private boolean blackExtraTurnGranted = false;

    // Pawns in each column (0..23)
    private final int[] blueUp   = new int[24];
    private final int[] blueDown = new int[24];
    private final int[] blackUp  = new int[24];
    private final int[] blackDown= new int[24];

    // Over-5 stacks
    private final int[] sevenNum = new int[24];

    // How many pawns are "out" / eaten
    private int outBlue  = 0;
    private int outBlack = 0;

    // Normally 2 moves, or 4 if doubles
    private int times = 2;    
    private int countDice = 1; 

    // Some booleans from your original code
    private boolean played = true;
    private boolean workIt = true;

    private int surpriseCounter = 0;

    // Surprise / question data
    public static boolean surprisePlayed = false;
    public static int[] questions = { -1, -1, -1 };
    public static int   surprise  = -1;

    private static final String HISTORY_FILE = "src/View/game_history.json";

    // List to hold remaining dice for the current turn
    private List<Integer> availableDice;

    public GamePlay(GridPane[] boardCols, Stage stage, String difficulty) {
        GamePlay.difficulty = difficulty;
        mainStage = stage;

        // Initialize arrays
        for(int i=0; i<24; i++){
            blueUp[i]   = 0;
            blueDown[i] = 0;
            blackUp[i]  = 0;
            blackDown[i]= 0;
            sevenNum[i] = 0;
        }

        Set<Integer> used = new HashSet<>();
        Random rand = new Random();
        // Random question spots
        for(int i=0; i<3; i++){
            questions[i] = selectRandomSpot(rand);
            while(used.contains(questions[i])){
                questions[i] = selectRandomSpot(rand);
            }
            used.add(questions[i]);
            System.out.println("Question spot selected: " + questions[i]);
        }
        // Random surprise
        surprise = selectRandomSpot(rand);
        while(used.contains(surprise)){
            surprise = selectRandomSpot(rand);
        }
        System.out.println("Surprise spot selected: " + surprise);

        initializePawns(boardCols);
    }

    private int selectRandomSpot(Random rand){
        return rand.nextInt(24); 
    }

    /**
     * Basic initial layout (similar to your code).
     */
    private void initializePawns(GridPane[] cols){
        // Example arrangement:
        setDown(cols, 18, 1, true);
        setDown(cols, 18, 1, true);
        setDown(cols, 18, 1, true);
        setDown(cols, 18, 1, true);
        setDown(cols, 18, 1, true);

        setDown(cols, 16, 1, true);
        setDown(cols, 16, 1, true);
        setDown(cols, 16, 1, true);

        setUp(cols, 11, 1, true);
        setUp(cols, 11, 1, true);
        setUp(cols, 11, 1, true);
        setUp(cols, 11, 1, true);
        setUp(cols, 11, 1, true);

        setUp(cols, 0, 1, true);
        setUp(cols, 0, 1, true);

        setDown(cols, 23, 1, false);
        setDown(cols, 23, 1, false);

        setDown(cols, 12, 1, false);
        setDown(cols, 12, 1, false);
        setDown(cols, 12, 1, false);
        setDown(cols, 12, 1, false);
        setDown(cols, 12, 1, false);

        setUp(cols, 7, 1, false);
        setUp(cols, 7, 1, false);
        setUp(cols, 7, 1, false);

        setUp(cols, 5, 1, false);
        setUp(cols, 5, 1, false);
        setUp(cols, 5, 1, false);
        setUp(cols, 5, 1, false);
        setUp(cols, 5, 1, false);
    }

    public void bluePlays(GridPane[] board, int dOne, int dTwo) {
        // Store current starting player
        boolean wasStartingPlayer = Backgammon.startingPlayer;
        promptQuestionEachTurn("turn");
        
        if (wasStartingPlayer != Backgammon.startingPlayer) return;

        availableDice = new ArrayList<>();
        availableDice.add(dOne);
        availableDice.add(dTwo);

        if (Math.abs(dOne) == Math.abs(dTwo)) {
            availableDice.add(dOne);
            availableDice.add(dTwo);
            setTimes(4);
        } else {
            setTimes(2);
        }

        if (outBlue > 0) {
            reEnterBluePawns(board);
            return;
        }

        if (blueGameOver()) {
            endGame(Login.player1, "BLUE");
            return;
        }
        
        reset();
        highlightAllBlue(board);

        if (!blueExtraTurnGranted) {
            Backgammon.startingPlayer = false;
        } else {
            blueExtraTurnGranted = false;
            System.out.println("Blue player gets an extra turn.");
            Backgammon.startingPlayer = true;
            // Next dice roll will trigger another blue turn automatically
            // since startingPlayer remains true
        }
    }




    // ============ RE-ENTER BLUE PAWNS ============
    private void reEnterBluePawns(GridPane[] board){
        // Clear highlights
        clearHighlights(board);

        // Determine valid dice for re-entry (only positive dice for Blue re-entry)
        List<Integer> validDice = new ArrayList<>();
        for(Integer die : availableDice){
            if(die > 0 && isValidReEntryDieForBlue(die)){
                validDice.add(die);
            }
        }

        if(validDice.isEmpty()){
            // No valid dice to re-enter, end turn immediately since we can't proceed
            showAlert("No Re-entry Available", 
                "No valid dice to re-enter Blue pawns. Your turn ends as you must re-enter all pawns before making other moves.");
            resetTurn();
            Backgammon.startingPlayer = false; // Switch to Black
            return;  // End turn immediately
        }

        // Highlight valid re-entry spots based on validDice
        for(Integer die : validDice){
            int targetCol = die - 1; // die=1 maps to col=0, etc.
            if(targetCol >= 0 && targetCol < 6 && isValidCaptureOrStackForBlue(targetCol)){
                board[targetCol].setStyle("-fx-border-color:red;");
                final int usedDie = die;
                final int finalToCol = targetCol;
                board[targetCol].setOnMouseClicked(e->{
                    // Perform re-entry
                    outBlue--;
                    useDie(usedDie);
                    
                    // If exactly one black, capture it
                    if(gatherBlack()[finalToCol] == 1){
                        if(finalToCol < 12) removeUp(board, finalToCol, false);
                        else removeDown(board, finalToCol, false);
                        outBlack++;
                    }
                    
                    // Place Blue
                    if(finalToCol < 12) setUp(board, finalToCol, 1, true);
                    else setDown(board, finalToCol, 1, true);

                    handleQuestionSpot(board, finalToCol);
                    handleSurpriseSpot(board, finalToCol, true); // Pass isBlue=true

                    // If we still have pawns to re-enter but no more dice, end turn
                    if(outBlue > 0 && availableDice.isEmpty()){
                        showAlert("Turn Ended", 
                            "You have no more dice to re-enter remaining pawns. Your turn ends as you must re-enter all pawns before making other moves.");
                        resetTurn();
                        Backgammon.startingPlayer = false; // Switch to Black
                        return;
                    }
                    
                    // If still have pawns to re-enter, continue re-entering
                    if(outBlue > 0){
                        reEnterBluePawns(board);
                    }
                    else{
                        // Only allow normal moves if ALL pawns are re-entered
                        highlightAllBlue(board);
                    }
                });
            }
        }
    }

    // After re-enter, highlight normal pawns for second (third, fourth) move
    private void highlightAllBlue(GridPane[] board) {
        clearHighlights(board);

        if (canBlueBearOff()) {
            highlightBlueBearingOff(board); // Highlight bearing-off options
            return;
        }

        int[] blues = gatherBlue();
        boolean hasValidMoves = false;

        for (int col = 0; col < 24; col++) {
            if (blues[col] > 0) {
                List<Integer> validDice = getValidDiceForBlue(col);
                if (!validDice.isEmpty()) {
                    hasValidMoves = true;
                    board[col].setStyle("-fx-border-color:pink;");
                    final int chosen = col;
                    board[col].setOnMouseClicked(e -> handleBluePawnClick(board, chosen));
                }
            }
        }

        if (!hasValidMoves) {
            showAlert("No Moves Available", "No valid moves available for Blue.");
            resetTurn();
            Backgammon.startingPlayer = false; // Switch to Black
        }
    }




    // Handle Blue pawn click
    private void handleBluePawnClick(GridPane[] board, int col){
        // Clear all highlights
        clearHighlights(board);

        // Determine which dice can be used to move this pawn
        List<Integer> validDice = getValidDiceForBlue(col);

        if(validDice.isEmpty()){
            showAlert("No Valid Dice", "No valid dice available to move this pawn.");
            highlightAllBlue(board);
            return;
        }

        // Highlight target columns based on validDice
        for(Integer die : validDice){
            int toCol = col + die;
            // Handle negative dice (moving backward)
            if(die < 0){
                toCol = col + die; // Move backward
            }
            else{
                toCol = col + die; // Move forward
            }

            if(toCol < 0 || toCol >=24){
                // Invalid move, skip
                continue;
            }

            if(isValidCaptureOrStackForBlue(toCol)){
                board[toCol].setStyle("-fx-border-color:yellow;");
                final int usedDie = die;
                final int finalToCol = toCol; // Make toCol effectively final
                board[toCol].setOnMouseClicked(ev->{
                    moveBlue(board, col, finalToCol, usedDie);
                });
            }
        }
    }

    // Get valid dice for moving Blue pawn from a column
    private List<Integer> getValidDiceForBlue(int fromCol){
        List<Integer> validDice = new ArrayList<>();
        for(Integer die : availableDice){
            int toCol = fromCol + die;
            // Handle negative dice
            if(die < 0){
                toCol = fromCol + die;
            }
            else{
                toCol = fromCol + die;
            }

            // Ensure the move is within bounds
            if(toCol >=0 && toCol <24 && isValidCaptureOrStackForBlue(toCol)){
                validDice.add(die);
            }
        }
        return validDice;
    }

    // Actually move a Blue pawn from fromCol to toCol using usedDie
    private void moveBlue(GridPane[] board, int fromCol, int toCol, int usedDie){
        // Clear highlights
        clearHighlights(board);
        // Remove Blue pawn from the original column
        if(fromCol <12){
            removeUp(board, fromCol, true);
        }
        else{
            removeDown(board, fromCol, true);
        }

        // Capture if exactly 1 black pawn is present
        if(gatherBlack()[toCol] ==1 ){
            if(toCol <12){
                removeUp(board, toCol, false);
            }
            else{
                removeDown(board, toCol, false);
            }
            outBlack++;
        }

        // Place Blue pawn on the target column
        if(toCol <12){
            setUp(board, toCol, 1, true);
        }
        else{
            setDown(board, toCol, 1, true);
        }

        // Handle special spots
        handleQuestionSpot(board, toCol);
        handleSurpriseSpot(board, toCol, true); // Pass isBlue=true

        // Remove the used die from availableDice
        useDie(usedDie);

        // Check if all dice are used
        if(availableDice.isEmpty()){
            if(blueGameOver()){
                endGame(Login.player1, "BLUE");
                return;
            }
            resetTurn();
            return;
        }

        // If still out pawns, re-enter
        if(outBlue >0){
            reEnterBluePawns(board);
        }
        else{
            highlightAllBlue(board);
        }
    }

    // ============ BLACK TURN ============
    public void blackPlays(GridPane[] board, int dOne, int dTwo) {
        boolean wasStartingPlayer = Backgammon.startingPlayer;
        promptQuestionEachTurn("turn");
        
        if (wasStartingPlayer != Backgammon.startingPlayer) return;

        availableDice = new ArrayList<>();
        availableDice.add(dOne);
        availableDice.add(dTwo);

        if (Math.abs(dOne) == Math.abs(dTwo)) {
            availableDice.add(dOne);
            availableDice.add(dTwo);
            setTimes(4);
        } else {
            setTimes(2);
        }

        if (outBlack > 0) {
            reEnterBlackPawns(board);
            return;
        }

        if (blackGameOver()) {
            endGame(Login.player2, "BLACK");
            return;
        }
        
        reset();
        highlightAllBlack(board);

        if (!blackExtraTurnGranted) {
            Backgammon.startingPlayer = true;
        } else {
            blackExtraTurnGranted = false;
            System.out.println("Black player gets an extra turn.");
            Backgammon.startingPlayer = false;
            // Next dice roll will trigger another black turn automatically
            // since startingPlayer remains false
        }
    }




    private void reEnterBlackPawns(GridPane[] board){
        // Clear highlights
        clearHighlights(board);

        // Determine valid dice for re-entry (only positive dice for Black re-entry)
        List<Integer> validDice = new ArrayList<>();
        for(Integer die : availableDice){
            if(die > 0 && isValidReEntryDieForBlack(die)){
                validDice.add(die);
            }
        }

        if(validDice.isEmpty()){
            // No valid dice to re-enter, end turn immediately since we can't proceed
            showAlert("No Re-entry Available", 
                "No valid dice to re-enter Black pawns. Your turn ends as you must re-enter all pawns before making other moves.");
            resetTurn();
            Backgammon.startingPlayer = true; // Switch to Blue
            return;  // End turn immediately
        }

        // Highlight valid re-entry spots based on validDice
        for(Integer die : validDice){
            int targetCol = 24 - die; // Correct mapping for Black
            if(targetCol >= 18 && targetCol < 24 && isValidCaptureOrStackForBlack(targetCol)){
                board[targetCol].setStyle("-fx-border-color:red;");
                final int usedDie = die;
                final int finalToCol = targetCol;
                board[targetCol].setOnMouseClicked(e->{
                    // Perform re-entry
                    outBlack--;
                    useDie(usedDie);
                    
                    // If exactly one blue, capture it
                    if(gatherBlue()[finalToCol] == 1){
                        if(finalToCol < 12) removeUp(board, finalToCol, true);
                        else removeDown(board, finalToCol, true);
                        outBlue++;
                    }
                    
                    // Place Black
                    if(finalToCol < 12){
                        setUp(board, finalToCol, 1, false);
                    }
                    else{
                        setDown(board, finalToCol, 1, false);
                    }

                    handleQuestionSpot(board, finalToCol);
                    handleSurpriseSpot(board, finalToCol, false); // Pass isBlue=false

                    // If we still have pawns to re-enter but no more dice, end turn
                    if(outBlack > 0 && availableDice.isEmpty()){
                        showAlert("Turn Ended", 
                            "You have no more dice to re-enter remaining pawns. Your turn ends as you must re-enter all pawns before making other moves.");
                        resetTurn();
                        Backgammon.startingPlayer = true; // Switch to Blue
                        return;
                    }
                    
                    // If still have pawns to re-enter, continue re-entering
                    if(outBlack > 0){
                        reEnterBlackPawns(board);
                    }
                    else{
                        // Only allow normal moves if ALL pawns are re-entered
                        highlightAllBlack(board);
                    }
                });
            }
        }
    }

    // After re-enter, highlight normal pawns for second (third, fourth) move
    private void highlightAllBlack(GridPane[] board) {
        clearHighlights(board);

        if (canBlackBearOff()) {
            highlightBlackBearingOff(board); // Highlight bearing-off options
            return;
        }

        int[] blacks = gatherBlack();
        boolean hasValidMoves = false;

        for (int col = 0; col < 24; col++) {
            if (blacks[col] > 0) {
                List<Integer> validDice = getValidDiceForBlack(col);
                if (!validDice.isEmpty()) {
                    hasValidMoves = true;
                    board[col].setStyle("-fx-border-color:green;");
                    final int chosen = col;
                    board[col].setOnMouseClicked(e -> handleBlackPawnClick(board, chosen));
                }
            }
        }

        if (!hasValidMoves) {
            showAlert("No Moves Available", "No valid moves available for Black.");
            resetTurn();
            Backgammon.startingPlayer = true; // Switch to Blue
        }
    }



    // Handle Black pawn click
    private void handleBlackPawnClick(GridPane[] board, int col){
        // Clear all highlights
        clearHighlights(board);

        // Determine which dice can be used to move this pawn
        List<Integer> validDice = getValidDiceForBlack(col);

        if(validDice.isEmpty()){
            showAlert("No Valid Dice", "No valid dice available to move this pawn.");
            highlightAllBlack(board);
            return;
        }

        // Highlight target columns based on validDice
        for(Integer die : validDice){
            int toCol = col - die;
            // Handle negative dice (moving backward)
            if(die < 0){
                toCol = col - die; // Move backward (which is forward for Black)
            }
            else{
                toCol = col - die; // Move forward
            }

            if(toCol < 0 || toCol >=24){
                // Invalid move, skip
                continue;
            }

            if(isValidCaptureOrStackForBlack(toCol)){
                board[toCol].setStyle("-fx-border-color:yellow;");
                final int usedDie = die;
                final int finalToCol = toCol; // Make toCol effectively final
                board[toCol].setOnMouseClicked(ev->{
                    moveBlack(board, col, finalToCol, usedDie);
                });
            }
        }
    }

    // Get valid dice for moving Black pawn from a column
    private List<Integer> getValidDiceForBlack(int fromCol){
        List<Integer> validDice = new ArrayList<>();
        for(Integer die : availableDice){
            int toCol = fromCol - die;
            // Handle negative dice
            if(die < 0){
                toCol = fromCol - die; // Move backward (which is forward for Black)
            }
            else{
                toCol = fromCol - die; // Move forward
            }

            // Ensure the move is within bounds
            if(toCol >=0 && toCol <24 && isValidCaptureOrStackForBlack(toCol)){
                validDice.add(die);
            }
        }
        return validDice;
    }

    // Actually move a Black pawn from fromCol to toCol using usedDie
    private void moveBlack(GridPane[] board, int fromCol, int toCol, int usedDie){
        // Clear highlights
        clearHighlights(board);
        
        // Remove Black pawn from the original column
        if(fromCol <12){
            removeUp(board, fromCol, false);
        }
        else{
            removeDown(board, fromCol, false);
        }

        // Capture if exactly 1 blue pawn is present
        if(gatherBlue()[toCol] ==1 ){
            if(toCol <12){
                removeUp(board, toCol, true);
            }
            else{
                removeDown(board, toCol, true);
            }
            outBlue++;
        }

        // Place Black pawn on the target column
        if(toCol <12){
            setUp(board, toCol, 1, false);
        }
        else{
            setDown(board, toCol, 1, false);
        }

        // Handle special spots
        handleQuestionSpot(board, toCol);
        handleSurpriseSpot(board, toCol, false);

        // Remove the used die from availableDice
        useDie(usedDie);

        // Check if all dice are used
        if(availableDice.isEmpty()){
            if(blackGameOver()){
                endGame(Login.player2, "BLACK");
                return;
            }
            resetTurn();
            return;
        }

        // If still out pawns, re-enter
        if(outBlack >0){
            reEnterBlackPawns(board);
        }
        else{
            highlightAllBlack(board);
        }
    }

    // --------------------------------------------------------------------------------
    // Helper Methods for Re-entry and Capturing
    // --------------------------------------------------------------------------------

    /**
     * Checks if a target column is a valid capture or stack for Blue.
     *
     * @param col The target column.
     * @return True if valid, else False.
     */
    private boolean isValidCaptureOrStackForBlue(int col){
        int blacks = gatherBlack()[col];
        int blues  = gatherBlue()[col];
        // valid if 0 or 1 black, or any number of blue
        return (blacks <=1) || (blues >0);
    }

    /**
     * Checks if a target column is a valid capture or stack for Black.
     *
     * @param col The target column.
     * @return True if valid, else False.
     */
    private boolean isValidCaptureOrStackForBlack(int col){
        int blues  = gatherBlue()[col];
        int blacks = gatherBlack()[col];
        // valid if 0 or 1 blue, or any number of black
        return (blues <=1) || (blacks >0);
    }

    /**
     * Checks if a die is valid for re-entry for Blue.
     *
     * @param die The die value.
     * @return True if valid, else False.
     */
    private boolean isValidReEntryDieForBlue(int die){
        // Re-entry uses only positive dice
        if(die <=0){
            return false;
        }
        int targetCol = die -1;
        if(targetCol <0 || targetCol >=6) return false;
        return isValidCaptureOrStackForBlue(targetCol);
    }

    /**
     * Checks if a die is valid for re-entry for Black.
     *
     * @param die The die value.
     * @return True if valid, else False.
     */
    private boolean isValidReEntryDieForBlack(int die){
        // Re-entry uses only positive dice
        if(die <=0){
            return false;
        }
        int targetCol = 24 - die;
        if(targetCol <18 || targetCol >=24) return false;
        return isValidCaptureOrStackForBlack(targetCol);
    }

    // --------------------------------------------------------------------------------
    // Board arrays
    // --------------------------------------------------------------------------------

    /**
     * Gathers the total number of Blue pawns in each column.
     *
     * @return An array representing Blue pawns count per column.
     */
    private int[] gatherBlue(){
        int[] arr = new int[24];
        for(int c=0; c<24; c++){
            arr[c] = blueUp[c] + blueDown[c];
        }
        return arr;
    }

    /**
     * Gathers the total number of Black pawns in each column.
     *
     * @return An array representing Black pawns count per column.
     */
    private int[] gatherBlack(){
        int[] arr = new int[24];
        for(int c=0; c<24; c++){
            arr[c] = blackUp[c] + blackDown[c];
        }
        return arr;
    }

    /**
     * Places a Blue pawn upwards in a column.
     *
     * @param columns The game board columns.
     * @param col     The target column.
     * @param howMany Number of pawns to place.
     * @param isBlue  True if Blue, False otherwise.
     */
    public void setUp(GridPane[] columns, int col, int howMany, boolean isBlue) {
        int contains = howManyContains(columns, col);
        int put = Math.max(0, contains); // Ensure `put` is valid

        if (sevenNum[col] > 0) {
            sevenNum[col]--;
        } else {
            if (isBlue) {
                blueUp[col] += howMany;
                if (put >= 0) {
                    columns[col].add(pawn.createPawn(true), 0, put);
                }
            } else {
                blackUp[col] += howMany;
                if (put >= 0) {
                    columns[col].add(pawn.createPawn(false), 0, put);
                }
            }
        }
    }


    /**
     * Places a Blue pawn downwards in a column.
     *
     * @param columns The game board columns.
     * @param col     The target column.
     * @param howMany Number of pawns to place.
     * @param isBlue  True if Blue, False otherwise.
     */
    public void setDown(GridPane[] columns, int col, int howMany, boolean isBlue) {
        int contains = howManyContains(columns, col);
        int put = Math.max(0, 5 - contains); // Ensure `put` is not negative

        if (sevenNum[col] > 0) {
            sevenNum[col]--;
            // Handle over-5 stacks if necessary
        } else {
            if (isBlue) {
                blueDown[col] += howMany;
                if (put >= 0) {
                    columns[col].add(pawn.createPawn(true), 0, put);
                }
            } else {
                blackDown[col] += howMany;
                if (put >= 0) {
                    columns[col].add(pawn.createPawn(false), 0, put);
                }
            }
        }
    }


    /**
     * Removes a Blue pawn from upwards in a column.
     *
     * @param columns The game board columns.
     * @param col     The column to remove from.
     * @param isBlue  True if Blue, False otherwise.
     */
    public void removeUp(GridPane[] columns, int col, boolean isBlue){
        if(sevenNum[col] >0){
            sevenNum[col]--;
        }
        else{
            int c = howManyContains(columns,col);
            if(c >0){
                columns[col].getChildren().remove(c-1);
            }
        }
        if(isBlue) blueUp[col]--;
        else       blackUp[col]--;
    }

    /**
     * Removes a Blue pawn from downwards in a column.
     *
     * @param columns The game board columns.
     * @param col     The column to remove from.
     * @param isBlue  True if Blue, False otherwise.
     */
    public void removeDown(GridPane[] columns, int col, boolean isBlue){
        if(sevenNum[col] >0){
            sevenNum[col]--;
        }
        else{
            int c = howManyContains(columns,col);
            if(c >0){
                columns[col].getChildren().remove(c-1);
            }
        }
        if(isBlue) blueDown[col]--;
        else       blackDown[col]--;
    }

    /**
     * Counts how many pawns are present in a column.
     *
     * @param columns The game board columns.
     * @param col     The column to count.
     * @return The number of pawns in the column.
     */
    public int howManyContains(GridPane[] columns, int col){
        int counter=0;
        for(Node n : columns[col].getChildren()){
            if(n instanceof Circle) counter++;
        }
        if(sevenNum[col]>0) return sevenNum[col]+6;
        return counter;
    }

    // --------------------------------------------------------------------------------
    // Checking end of game
    // --------------------------------------------------------------------------------
    private boolean blueGameOver() {
        for (int i = 0; i < 24; i++) {
            if (blueUp[i] > 0 || blueDown[i] > 0) return false;
        }
        return outBlue == 0; // Check if no pawns are "out"
    }

    private boolean blackGameOver() {
        for (int i = 0; i < 24; i++) {
            if (blackUp[i] > 0 || blackDown[i] > 0) return false;
        }
        return outBlack == 0; // Check if no pawns are "out"
    }


    // --------------------------------------------------------------------------------
    // Times / reset
    // --------------------------------------------------------------------------------
    public void setTimes(int t){ times = t; }
    public int getTimes(){return times;}
    public void reset(){
        countDice=1;
        played=true;
        workIt=true;
    }

    // --------------------------------------------------------------------------------
    // Questions & Surprises
    // --------------------------------------------------------------------------------
    public static int[] getQuestions(){return questions;}

    /**
     * Helper: If Hard, show a random question at start of turn and wait for correct answer.
     * Returns true if the turn should continue, false if the player lost the turn.
     * 
     * @param from Indicates the source of the question ("turn" or "spot").
     */
    private void promptQuestionEachTurn(String from){
        if("Hard".equalsIgnoreCase(difficulty)){
            // Show the question screen and let it handle the turn logic
            QuestionScreen qScreen = new QuestionScreen();
            qScreen.show(mainStage, "Hard", from);
            // The QuestionScreen handles the startingPlayer flag if the answer was wrong
        }
    }

    /**
     * Handle special question spot.
     */
    public void handleQuestionSpot(GridPane[] columns, int col){
        for(int q : questions){
            if(q == col){
                // Handle question spot
                if("Hard".equalsIgnoreCase(difficulty)){
                    // Blocking question
                    promptQuestionEachTurn("spot");
                    // If answered incorrectly, startingPlayer is toggled by QuestionScreen
                    // No further action needed here
                } else {
                    // For non-hard modes, show question without blocking
                    Random r = new Random();
                    int x = r.nextInt(3)+1;
                    String diff;
                    switch(x){
                        case 1: diff="Easy";break;
                        case 2: diff="Medium";break;
                        default: diff="Hard";break;
                    }
                    QuestionScreen screen = new QuestionScreen();
                    screen.show(mainStage, diff, "spot");
                }
            }
        }
    }

    /**
     * Handles the surprise spot and grants an extra turn to the current player.
     *
     * @param columns The game board columns.
     * @param col     The column where the player landed.
     * @param isBlue  True if the current player is Blue, False if Black.
     */
    /**
     * Handles the surprise spot and grants an extra turn to the current player.
     *
     * @param columns The game board columns.
     * @param col     The column where the player landed.
     * @param isBlue  True if the current player is Blue, False if Black.
     */
    private void handleSurpriseSpot(GridPane[] columns, int col, boolean isBlue) {
        if (col == surprise) {
            System.out.println("Surprise spot reached!");

            // Display the Surprise Pop-Up
            SurprisePopUp pop = new SurprisePopUp();
            pop.show(mainStage);

            // Set the extra turn flag based on the current player
            if (isBlue) {
                blueExtraTurnGranted = true;
                System.out.println("Blue player granted an extra turn.");
            } else {
                blackExtraTurnGranted = true;
                System.out.println("Black player granted an extra turn.");
            }

            // Ensure that the turn is fully completed before granting the extra turn
            resetTurnCompletion(); // Mark the turn as incomplete
            Platform.runLater(() -> waitForTurnCompletion(columns, isBlue));
        }
    }

    
    private boolean turnComplete = false;

    private boolean isTurnComplete() {
        return turnComplete;
    }

    private void resetTurnCompletion() {
        this.turnComplete = false;
    }

    private boolean hasPawnsToReenter(boolean isBlue) {
        return (isBlue && outBlue > 0) || (!isBlue && outBlack > 0);
    }

    
    private void waitForTurnCompletion(GridPane[] columns, boolean isBlue) {
        // Declare the timeline locally
        Timeline waitTimeline = new Timeline();

        // Schedule a periodic check to see if the turn is complete
        waitTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(200), event -> {
            // Check if all dice have been used and no pawns need re-entry
            if (availableDice.isEmpty() && !hasPawnsToReenter(isBlue)) {
                setTurnComplete(); // Mark the turn as complete
                System.out.println("Current turn completed. Granting extra turn.");

                // Reset the turn completion flag
                resetTurnCompletion();

                // Trigger the extra turn
                if (isBlue && blueExtraTurnGranted) {
                    blueExtraTurnGranted = false;
                    System.out.println("Initiating Blue's extra turn.");
                    // Let the player roll dice manually
                    Backgammon.startingPlayer = true;
                } else if (!isBlue && blackExtraTurnGranted) {
                    blackExtraTurnGranted = false;
                    System.out.println("Initiating Black's extra turn.");
                    // Let the player roll dice manually
                    Backgammon.startingPlayer = false;
                }

                waitTimeline.stop(); // Stop the timeline after the extra turn logic
            }
        }));

        waitTimeline.setCycleCount(Animation.INDEFINITE); // Keep checking until the turn is complete
        waitTimeline.play();
    }

    
    







    // --------------------------------------------------------------------------------
    // History
    // --------------------------------------------------------------------------------
    public static void addGameToHistory(String p1, String p2, String winner, String diff, int sec){
        List<String> lines = new ArrayList<>();
        try(BufferedReader rdr = new BufferedReader(new FileReader(HISTORY_FILE))){
            String line;
            while((line=rdr.readLine())!=null){
                lines.add(line.trim());
            }
        } catch(IOException e){
            lines.add("[");
        }
        if(!lines.isEmpty() && lines.get(lines.size()-1).equals("]")){
            lines.remove(lines.size()-1);
        }
        String newEntry = String.format(
                "  {\n" +
                "    \"player1\":\"%s\",\n" +
                "    \"player2\":\"%s\",\n" +
                "    \"winner\":\"%s\",\n" +
                "    \"difficulty\":\"%s\",\n" +
                "    \"duration\":\"%d seconds\"\n" +
                "  }",p1,p2,winner,diff,sec);
        if(lines.size()>1){
            lines.add(",");
        }
        lines.add(newEntry);
        lines.add("]");
        try(BufferedWriter w = new BufferedWriter(new FileWriter(HISTORY_FILE))){
            for(String s: lines){
                w.write(s);
                w.newLine();
            }
            System.out.println("Game history updated successfully.");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    // --------------------------------------------------------------------------------
    // Shared / Utility Methods
    // --------------------------------------------------------------------------------

    /**
     * Clears all styles and mouse click handlers from the board.
     *
     * @param board The game board.
     */
    private void clearHighlights(GridPane[] board){
        for(int c=0; c<24; c++){
            board[c].setStyle(null);
            board[c].setOnMouseClicked(null);
        }
    }

    /**
     * Removes a die from the availableDice list after it's used.
     *
     * @param die The die value to remove.
     */
    private void useDie(int die){
        availableDice.remove((Integer) die);
    }

    /**
     * Displays an alert to the user.
     *
     * @param title   The title of the alert.
     * @param message The message content.
     */
    private void showAlert(String title, String message){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    /**
     * Resets the turn by resetting dice counters and flags.
     */
    private void resetTurn() {
        countDice = 1;
        played = true;
        workIt = true;

        // Mark the turn as complete
        setTurnComplete();
    }

    private void setTurnComplete() {
        this.turnComplete = true;
    }


    /**
     * Ends the game, declaring the winner and updating history.
     *
     * @param winnerName The name of the winner.
     * @param colorStr   The color representing the winner.
     */
    private void endGame(String winnerName, String colorStr){
        try{
            Backgammon.stopTimer();
            addGameToHistory(Login.player1, Login.player2, winnerName, difficulty, Backgammon.secondsElapsed);
            Stage st = new Stage();
            Group gp = new Group();
            Scene sc = new Scene(gp, 500, 500, 
                "BLUE".equals(colorStr) ? Color.BLUE : Color.BLACK);
            st.setTitle(colorStr + " WINS!");
            st.setScene(sc);
            st.show();
            mainStage.close();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    private boolean canBlueBearOff() {
        for (int i = 0; i < 18; i++) {
            if (blueUp[i] > 0 || blueDown[i] > 0) {
                return false; // Blue pawns still outside the home area
            }
        }
        return true; // All Blue pawns are in the home area
    }

    private boolean canBlackBearOff() {
        for (int i = 6; i < 24; i++) {
            if (blackUp[i] > 0 || blackDown[i] > 0) {
                return false; // Black pawns still outside the home area
            }
        }
        return true; // All Black pawns are in the home area
    }
    
    private void highlightBlueBearingOff(GridPane[] board) {
        // Highlight only if eligible
        if (!canBlueBearOff()) return;

        for (Integer die : availableDice) {
            int targetIndex = 23 - (die - 1); // Calculate bearing-off index
            if (targetIndex >= 18 && targetIndex <= 23 && gatherBlue()[targetIndex] > 0) {
                board[targetIndex].setStyle("-fx-border-color:purple;");
                final int usedDie = die;
                final int fromCol = targetIndex;

                board[targetIndex].setOnMouseClicked(e -> {
                    // Perform bearing-off
                    removeBlue(board, fromCol, 1);
                    useDie(usedDie);

                    // Check if game is over
                    if (blueGameOver()) {
                        endGame(Login.player1, "BLUE");
                    } else {
                        // Continue turn
                        highlightAllBlue(board);
                    }
                });
            }
        }
    }
    
    private void removeBlue(GridPane[] board, int col, int count) {
        // Remove `count` pawns from the column for Blue
        if (col < 12) {
            blueUp[col] -= count;
        } else {
            blueDown[col] -= count;
        }

        // Remove the visual representation from the board
        int childrenCount = howManyContains(board, col);
        if (childrenCount > 0) {
            board[col].getChildren().remove(childrenCount - 1); // Remove topmost pawn
        }
    }
    
    private void removeBlack(GridPane[] board, int col, int count) {
        // Remove `count` pawns from the column for Black
        if (col < 12) {
            blackUp[col] -= count;
        } else {
            blackDown[col] -= count;
        }

        // Remove the visual representation from the board
        int childrenCount = howManyContains(board, col);
        if (childrenCount > 0) {
            board[col].getChildren().remove(childrenCount - 1); // Remove topmost pawn
        }
    }



    private void highlightBlackBearingOff(GridPane[] board) {
        if (!canBlackBearOff()) return;

        for (Integer die : availableDice) {
            int targetIndex = die - 1; // Calculate bearing-off index
            if (targetIndex >= 0 && targetIndex <= 5 && gatherBlack()[targetIndex] > 0) {
                board[targetIndex].setStyle("-fx-border-color:purple;");
                final int usedDie = die;
                final int fromCol = targetIndex;

                board[targetIndex].setOnMouseClicked(e -> {
                    // Perform bearing-off
                    removeBlack(board, fromCol, 1);
                    useDie(usedDie);

                    // Check if game is over
                    if (blackGameOver()) {
                        endGame(Login.player2, "BLACK");
                    } else {
                        // Continue turn
                        highlightAllBlack(board);
                    }
                });
            }
        }
    }
    
    public int[] getBlueUp() {
        return blueUp;
    }
    public int[] getBlackDown() {
        return blackDown;
    }
    

}


//THIS IS THE SHIT!!!!!!!!!!!