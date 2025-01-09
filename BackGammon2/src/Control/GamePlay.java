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
    
    // Extra turn flags
    private boolean blueExtraTurnGranted = false;
    private boolean blackExtraTurnGranted = false;
    private int counter =0;

    // Pawns in each column (0..23)
    private final int[] blueUp   = new int[24];
    private final int[] blueDown = new int[24];
    private final int[] blackUp  = new int[24];
    private final int[] blackDown= new int[24];

    // Over-5 stacks
    private final int[] sevenNum = new int[24];

    // Pawns "out"
    private int outBlue  = 0;
    private int outBlack = 0;

    private int times = 2;    
    private int countDice = 1; 
    private boolean played = true;
    private boolean workIt = true;
    private int surpriseCounter = 0;

    // Surprise / question data
    public static boolean surprisePlayed = false;
    public static int[] questions = { -1, -1, -1 };
    public static int   surprise  = -1;

    private static final String HISTORY_FILE = "src/View/game_history.json";

    // Current turn's dice
    private List<Integer> availableDice;

    // Turn-completion
    private boolean turnComplete = false;

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

        // Random question + surprise
        Set<Integer> used = new HashSet<>();
        Random rand = new Random();
        for(int i=0; i<3; i++){
            questions[i] = selectRandomSpot(rand);
            while(used.contains(questions[i])){
                questions[i] = selectRandomSpot(rand);
            }
            used.add(questions[i]);
            System.out.println("Question spot selected: " + questions[i]);
        }
        surprise = selectRandomSpot(rand);
        while(used.contains(surprise)){
            surprise = selectRandomSpot(rand);
        }
        System.out.println("Surprise spot selected: " + surprise);

        // Place initial pawns
        initializePawns(boardCols);
    }

    private int selectRandomSpot(Random rand){
        return rand.nextInt(24); 
    }

    /**
     * Example arrangement
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

    // ======================= BLUE TURN =======================
    public void bluePlays(GridPane[] board, int dOne, int dTwo) {
        promptQuestionEachTurn("turn");
        boolean wasStartingPlayer = true;
        if (wasStartingPlayer != Backgammon.startingPlayer) {
            return;
        }

        // Setup dice
        availableDice = new ArrayList<>();
        availableDice.add(dOne);
        availableDice.add(dTwo);

        if (Math.abs(dOne) == Math.abs(dTwo)) {
            // doubles => 4 moves
            availableDice.add(dOne);
            availableDice.add(dTwo);
            setTimes(4);
        } else {
            setTimes(2);
        }

        // Must re-enter if out
        if (outBlue > 0) {
            reEnterBluePawns(board);
            return;
        }

        // Possibly game over
        if (blueGameOver()) {
            endGame(Login.player1, "BLUE");
            return;
        }
        
        reset();
        highlightAllBlue(board);

        // Next turn
        if (!blueExtraTurnGranted) {
            Backgammon.startingPlayer = false;
        } else {
            blueExtraTurnGranted = false;
            System.out.println("Blue player gets an extra turn.");
            Backgammon.startingPlayer = true;
        }
    }

    // Re-enter Blue pawns if they're out
    private void reEnterBluePawns(GridPane[] board){
        clearHighlights(board);

        // Only positive dice for re-entry
        List<Integer> validDice = new ArrayList<>();
        for(Integer die : availableDice){
            if(die > 0 && isValidReEntryDieForBlue(die)){
                validDice.add(die);
            }
        }

        if(validDice.isEmpty()){
            showAlert("No Re-entry Available", 
                "No valid dice to re-enter Blue pawns. Your turn ends.");
            resetTurn();
            Backgammon.startingPlayer = false; 
            return;
        }

        // Highlight valid re-entry
        for(Integer die : validDice){
            int targetCol = die - 1;
            if(targetCol >= 0 && targetCol < 6 && isValidCaptureOrStackForBlue(targetCol)){
                board[targetCol].setStyle("-fx-border-color:red;");
                final int usedDie = die;
                final int finalToCol = targetCol;
                board[targetCol].setOnMouseClicked(e->{
                    outBlue--;
                    useDie(usedDie);
                    
                    // capture black
                    if(gatherBlack()[finalToCol] == 1){
                        if(finalToCol < 12) removeUp(board, finalToCol, false);
                        else                removeDown(board, finalToCol, false);
                        outBlack++;
                    }
                    
                    // place Blue
                    if(finalToCol < 12) setUp(board, finalToCol, 1, true);
                    else               setDown(board, finalToCol, 1, true);

                    handleQuestionSpot(board, finalToCol);
                    handleSurpriseSpot(board, finalToCol, true);

                    if(outBlue > 0 && availableDice.isEmpty()){
                        showAlert("Turn Ended", 
                            "No dice left to re-enter remaining pawns. Your turn ends.");
                        resetTurn();
                        Backgammon.startingPlayer = false; 
                        return;
                    }
                    
                    if(outBlue > 0){
                        reEnterBluePawns(board);
                    }
                    else{
                        highlightAllBlue(board);
                    }
                });
            }
        }
    }

    // Highlight all Blue (bearing off + normal moves)
    private void highlightAllBlue(GridPane[] board) {
        clearHighlights(board);

        // 1) If we can bear off, highlight
        if (canBlueBearOff()) {
            highlightBlueBearingOff(board);
        }

        // 2) Normal moves
        int[] blues = gatherBlue();
        boolean hasValidMoves = false;

        for (int col = 0; col < 24; col++) {
            int count = blues[col];
            if (count > 0) {
                // if col is 18..23 and EXACT die matches => we skip normal highlight 
                // because we want the user to remove, not move, if they click it.
                if (col >= 18 && col <= 23 && canRemoveExactDistanceBlue(col)) {
                    continue;
                }

                // otherwise highlight for normal moves
                List<Integer> validDice = getValidDiceForBlue(col);
                if (!validDice.isEmpty()) {
                    hasValidMoves = true;
                    board[col].setStyle("-fx-border-color:pink;");
                    final int chosen = col;
                    board[col].setOnMouseClicked(e -> handleBluePawnClick(board, chosen));
                }
            }
        }

        if (!hasValidMoves && !canBlueBearOff()) {
            showAlert("No Moves Available", "No valid moves available for Blue.");
            resetTurn();
            Backgammon.startingPlayer = false; 
        }
    }

    // If there's any die EXACTly matching the distance for BLUE => skip normal highlight
    private boolean canRemoveExactDistanceBlue(int col) {
        int needed = (23 - col) + 1;
        for (int die : availableDice) {
            if (die == needed) return true;
        }
        return false;
    }

    private void handleBluePawnClick(GridPane[] board, int col){
        clearHighlights(board);
        List<Integer> validDice = getValidDiceForBlue(col);

        if(validDice.isEmpty()){
            showAlert("No Valid Dice", "No valid dice available to move this pawn.");
            highlightAllBlue(board);
            return;
        }

        for(Integer die : validDice){
            final int usedDie = die;
            int toCol = col + die;
            if(toCol < 0 || toCol >=24){
                continue;
            }

            if(isValidCaptureOrStackForBlue(toCol)){
                board[toCol].setStyle("-fx-border-color:yellow;");
                final int finalToCol = toCol;
                board[toCol].setOnMouseClicked(ev->{
                    moveBlue(board, col, finalToCol, usedDie);
                });
            }
        }
    }

    private List<Integer> getValidDiceForBlue(int fromCol){
        List<Integer> validDice = new ArrayList<>();
        for(Integer die : availableDice){
            int toCol = fromCol + die;
            if(toCol >=0 && toCol <24 && isValidCaptureOrStackForBlue(toCol)){
                validDice.add(die);
            }
        }
        return validDice;
    }

    // Important: we call highlightAllBlue after using a die, so the bear-off options are recalculated
    private void moveBlue(GridPane[] board, int fromCol, int toCol, int usedDie){
        clearHighlights(board);

        // remove from old col
        if(fromCol <12) removeUp(board, fromCol, true);
        else            removeDown(board, fromCol, true);

        // capture if exactly 1 black
        if(gatherBlack()[toCol] ==1 ){
            if(toCol <12) removeUp(board, toCol, false);
            else          removeDown(board, toCol, false);
            outBlack++;
        }

        // place Blue
        if(toCol <12) setUp(board, toCol, 1, true);
        else          setDown(board, toCol, 1, true);

        handleQuestionSpot(board, toCol);
        handleSurpriseSpot(board, toCol, true);
        
        // Use the die
        useDie(usedDie);

        // Check if game ended
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
            // Re-highlight so the bear-off option is gone if we used that die for a normal move
            highlightAllBlue(board);
        }
    }

    // ======================= BLACK TURN =======================
    public void blackPlays(GridPane[] board, int dOne, int dTwo) {
        promptQuestionEachTurn("turn");
        boolean wasStartingPlayer = false;

        if (wasStartingPlayer != Backgammon.startingPlayer) {
            return;
        }

        availableDice = new ArrayList<>();
        availableDice.add(dOne);
        availableDice.add(dTwo);

        if (Math.abs(dOne) == Math.abs(dTwo)) {
            // doubles => 4 moves
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
        }
    }

    private void reEnterBlackPawns(GridPane[] board){
        clearHighlights(board);

        // only positive dice for Black re-entry
        List<Integer> validDice = new ArrayList<>();
        for(Integer die : availableDice){
            if(die > 0 && isValidReEntryDieForBlack(die)){
                validDice.add(die);
            }
        }

        if(validDice.isEmpty()){
            showAlert("No Re-entry Available", 
                "No valid dice to re-enter Black pawns. Your turn ends.");
            resetTurn();
            Backgammon.startingPlayer = true; 
            return;
        }

        for(Integer die : validDice){
            final int usedDie = die;
            int targetCol = 24 - die;
            if(targetCol >= 18 && targetCol < 24 && isValidCaptureOrStackForBlack(targetCol)){
                board[targetCol].setStyle("-fx-border-color:red;");
                final int finalToCol = targetCol;
                board[targetCol].setOnMouseClicked(e->{
                    outBlack--;
                    useDie(usedDie);
                    
                    // capture if exactly 1 blue
                    if(gatherBlue()[finalToCol] == 1){
                        if(finalToCol < 12) removeUp(board, finalToCol, true);
                        else                removeDown(board, finalToCol, true);
                        outBlue++;
                    }
                    
                    // place Black
                    if(finalToCol <12) setUp(board, finalToCol, 1, false);
                    else               setDown(board, finalToCol, 1, false);

                    handleQuestionSpot(board, finalToCol);
                    handleSurpriseSpot(board, finalToCol, false);

                    if(outBlack > 0 && availableDice.isEmpty()){
                        showAlert("Turn Ended", 
                            "No more dice to re-enter remaining pawns. Your turn ends.");
                        resetTurn();
                        Backgammon.startingPlayer = true; 
                        return;
                    }
                    
                    if(outBlack > 0){
                        reEnterBlackPawns(board);
                    }
                    else{
                        highlightAllBlack(board);
                    }
                });
            }
        }
    }

    private void highlightAllBlack(GridPane[] board) {
        clearHighlights(board);

        // 1) bearing off if possible
        if (canBlackBearOff()) {
            highlightBlackBearingOff(board);
        }

        // 2) normal moves
        int[] blacks = gatherBlack();
        boolean hasValidMoves = false;

        for (int col = 0; col < 24; col++) {
            int count = blacks[col];
            if (count > 0) {
                if (col >= 0 && col <= 5 && canRemoveExactDistanceBlack(col)) {
                    // If there's an EXACT match to remove, skip normal highlight
                    continue;
                }

                List<Integer> validDice = getValidDiceForBlack(col);
                if (!validDice.isEmpty()) {
                    hasValidMoves = true;
                    board[col].setStyle("-fx-border-color:green;");
                    final int chosen = col;
                    board[col].setOnMouseClicked(e -> handleBlackPawnClick(board, chosen));
                }
            }
        }

        if (!hasValidMoves && !canBlackBearOff()) {
            showAlert("No Moves Available", "No valid moves available for Black.");
            resetTurn();
            Backgammon.startingPlayer = true; 
        }
    }

    // If there's any die EXACTly matching the distance for Black => skip normal highlight
    private boolean canRemoveExactDistanceBlack(int col) {
        int needed = (col - 0) + 1;
        for (int die : availableDice) {
            if (die == needed) return true;
        }
        return false;
    }

    private void handleBlackPawnClick(GridPane[] board, int col){
        clearHighlights(board);

        List<Integer> validDice = getValidDiceForBlack(col);
        if(validDice.isEmpty()){
            showAlert("No Valid Dice", "No valid dice available to move this pawn.");
            highlightAllBlack(board);
            return;
        }

        for(Integer die : validDice){
            final int usedDie = die;
            int toCol = col - die;
            if(toCol < 0 || toCol >=24){
                continue;
            }

            if(isValidCaptureOrStackForBlack(toCol)){
                board[toCol].setStyle("-fx-border-color:yellow;");
                final int finalToCol = toCol;
                board[toCol].setOnMouseClicked(ev->{
                    moveBlack(board, col, finalToCol, usedDie);
                });
            }
        }
    }

    private List<Integer> getValidDiceForBlack(int fromCol){
        List<Integer> validDice = new ArrayList<>();
        for(Integer die : availableDice){
            int toCol = fromCol - die;
            if(toCol >=0 && toCol <24 && isValidCaptureOrStackForBlack(toCol)){
                validDice.add(die);
            }
        }
        return validDice;
    }

    // Important: re-run highlightAllBlack after using a die for a normal move
    private void moveBlack(GridPane[] board, int fromCol, int toCol, int usedDie){
        clearHighlights(board);

        // remove from original
        if(fromCol <12) removeUp(board, fromCol, false);
        else            removeDown(board, fromCol, false);

        // capture if exactly 1 blue
        if(gatherBlue()[toCol] ==1 ){
            if(toCol <12) removeUp(board, toCol, true);
            else          removeDown(board, toCol, true);
            outBlue++;
        }

        // place Black
        if(toCol <12) setUp(board, toCol, 1, false);
        else          setDown(board, toCol, 1, false);

        handleQuestionSpot(board, toCol);
        handleSurpriseSpot(board, toCol, false);
        
        // Use the die
        useDie(usedDie);

        if(availableDice.isEmpty()){
            if(blackGameOver()){
                endGame(Login.player2, "BLACK");
                return;
            }
            resetTurn();
            return;
        }

        if(outBlack >0){
            reEnterBlackPawns(board);
        }
        else{
            highlightAllBlack(board);
        }
    }

    // --------------------------------------------------------------------------------
    // Bearing-off logic
    // --------------------------------------------------------------------------------
    private boolean canBlueBearOff() {
        // All Blue pawns must be in columns 18..23
        for (int i = 0; i < 18; i++) {
            if (blueUp[i] > 0 || blueDown[i] > 0) {
                return false;
            }
        }
        return true; 
    }

    private boolean canBlackBearOff() {
        // All Black pawns must be in columns 0..5
        for (int i = 6; i < 24; i++) {
            if (blackUp[i] > 0 || blackDown[i] > 0) {
                return false;
            }
        }
        return true; 
    }

    /**
     * Checks if there's any Blue checker above col (col+1..23). 
     * Used if die > distance to ensure we can't remove from col if higher col is occupied.
     */
    private boolean blueHasCheckerAbove(int col) {
        // Because place 1 is col=23 (the highest),
        // we check all columns from 23 down to col+1.
        // If any is occupied, we say “yes, there’s a higher checker.”
        for (int c = 23; c > col; c--) {
            if (blueUp[c] + blueDown[c] > 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if there's any Black checker above col (col+1..5).
     */
    private boolean blackHasCheckerAbove(int col) {
        for (int c = col+1; c <= 5; c++) {
            if (blackUp[c] + blackDown[c] > 0) return true;
        }
        return false;
    }

    private void highlightBlueBearingOff(GridPane[] board) {
        // Only proceed if all Blue checkers are in home (18..23).
        if (!canBlueBearOff()) return;

        // Loop through columns 18..23
        for (int col = 18; col <= 23; col++) {
            int howMany = blueUp[col] + blueDown[col];
            if (howMany <= 0) continue; // no Blue pawns here

            // distanceNeeded = 24 - col
            //  col=18 =>6, col=19=>5, col=20=>4, col=21=>3, col=22=>2, col=23=>1
            int distanceNeeded = 24 - col;

            // Check each die
            for (Integer die : availableDice) {
                // 1) If die < distance, skip
                if (die < distanceNeeded) {
                    continue; // Not enough to remove from this column
                }

                // 2) If die == distance => highlight no matter what
                if (die == distanceNeeded) {
                    final int usedDie = die;
                    final int finalCol = col;
                    board[col].setStyle("-fx-border-color:purple;");
                    board[col].setOnMouseClicked(e -> {
                        removeBlue(board, finalCol, 1);
                        useDie(usedDie);

                        // Check if game is done
                        if (blueGameOver()) {
                            endGame(Login.player1, "BLUE");
                        } else {
                            highlightAllBlue(board);
                        }
                    });
                }
                // 3) If die > distance => highlight ONLY IF no larger-distance column is still occupied
                else {
                    // e.g. if distanceNeeded=2, but die=4/5/6 => must check distances 3..6
                    if (noFarthestColumnOccupied(distanceNeeded)) {
                        final int usedDie = die;
                        final int finalCol = col;
                        board[col].setStyle("-fx-border-color:purple;");
                        board[col].setOnMouseClicked(e -> {
                            removeBlue(board, finalCol, 1);
                            useDie(usedDie);

                            if (blueGameOver()) {
                                endGame(Login.player1, "BLUE");
                            } else {
                                highlightAllBlue(board);
                            }
                        });
                    }
                }
            }
        }
    }


    /**
     * For standard rules:
     * If 'dist' is 2, we check dist=3..6 => col=21..18 
     *   to see if they're occupied, because we must remove that piece first if our die >= (that distance).
     * If any is occupied, we *cannot* remove from the smaller-dist column.
     */
    private boolean noFarthestColumnOccupied(int dist) {
        // Distances: 6..1 => col=18..23 => col=24 - distance
        // If dist=2, we check dist=3..6 => col=21..18
        for (int d = dist + 1; d <= 6; d++) {
            int col = 24 - d;  // e.g. d=3 => col=21, d=6 => col=18
            // If there's a checker there, that means we need to remove that "farther" column first
            if (blueUp[col] + blueDown[col] > 0) {
                return false; 
            }
        }
        return true; 
    }






    /**
     * Real backgammon rule for Black
     */
    private void highlightBlackBearingOff(GridPane[] board) {
        if (!canBlackBearOff()) return;

        for (int col = 0; col <= 5; col++) {
            int count = blackUp[col] + blackDown[col];
            if (count <= 0) continue;

            int distanceNeeded = (col - 0) + 1;

            for (Integer die : availableDice) {
            	if (die < distanceNeeded) {
                    continue; 
                } 
            	if (die == distanceNeeded) {
                    final int usedDie = die;
                    final int finalCol = col;
                    board[col].setStyle("-fx-border-color:purple;");
                    board[col].setOnMouseClicked(e -> {
                        removeBlack(board, finalCol, 1);
                        useDie(usedDie);

                        if (blackGameOver()) {
                            endGame(Login.player2, "BLACK");
                        } else {
                            highlightAllBlack(board);
                        }
                    });
                }
                else if (die > distanceNeeded) {
                    if (!blackHasCheckerAbove(col)) {
                        final int usedDie = die;
                        final int finalCol = col;
                        board[col].setStyle("-fx-border-color:purple;");
                        board[col].setOnMouseClicked(e -> {
                            removeBlack(board, finalCol, 1);
                            useDie(usedDie);

                            if (blackGameOver()) {
                                endGame(Login.player2, "BLACK");
                            } else {
                                highlightAllBlack(board);
                            }
                        });
                    }
                }
            }
        }
    }

    private void removeBlue(GridPane[] board, int col, int count) {
        if (col < 12) {
            blueUp[col] -= count;
        } else {
            blueDown[col] -= count;
        }
        int childrenCount = howManyContains(board, col);
        if (childrenCount > 0) {
            board[col].getChildren().remove(childrenCount - 1);
        }
    }

    private void removeBlack(GridPane[] board, int col, int count) {
        if (col < 12) {
            blackUp[col] -= count;
        } else {
            blackDown[col] -= count;
        }
        int childrenCount = howManyContains(board, col);
        if (childrenCount > 0) {
            board[col].getChildren().remove(childrenCount - 1);
        }
    }

    // --------------------------------------------------------------------------------
    // Standard end checks
    // --------------------------------------------------------------------------------
    private boolean blueGameOver() {
        for (int i = 0; i < 24; i++) {
            if (blueUp[i] > 0 || blueDown[i] > 0) return false;
        }
        return outBlue == 0;
    }

    private boolean blackGameOver() {
        for (int i = 0; i < 24; i++) {
            if (blackUp[i] > 0 || blackDown[i] > 0) return false;
        }
        return outBlack == 0;
    }

    // --------------------------------------------------------------------------------
    // Shared methods / re-entry checks
    // --------------------------------------------------------------------------------
    private boolean isValidCaptureOrStackForBlue(int col){
        int blacks = gatherBlack()[col];
        int blues  = gatherBlue()[col];
        return (blacks <=1) || (blues >0);
    }

    private boolean isValidCaptureOrStackForBlack(int col){
        int blues  = gatherBlue()[col];
        int blacks = gatherBlack()[col];
        return (blues <=1) || (blacks >0);
    }

    private boolean isValidReEntryDieForBlue(int die){
        if(die <=0) return false;
        int targetCol = die -1;
        if(targetCol <0 || targetCol >=6) return false;
        return isValidCaptureOrStackForBlue(targetCol);
    }

    private boolean isValidReEntryDieForBlack(int die){
        if(die <=0) return false;
        int targetCol = 24 - die;
        if(targetCol <18 || targetCol >=24) return false;
        return isValidCaptureOrStackForBlack(targetCol);
    }

    private int[] gatherBlue(){
        int[] arr = new int[24];
        for(int c=0; c<24; c++){
            arr[c] = blueUp[c] + blueDown[c];
        }
        return arr;
    }

    private int[] gatherBlack(){
        int[] arr = new int[24];
        for(int c=0; c<24; c++){
            arr[c] = blackUp[c] + blackDown[c];
        }
        return arr;
    }

    // Placing / removing
    public void setUp(GridPane[] columns, int col, int howMany, boolean isBlue) {
        int contains = howManyContains(columns, col);
        int put = Math.max(0, contains);

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

    public void setDown(GridPane[] columns, int col, int howMany, boolean isBlue) {
        int contains = howManyContains(columns, col);
        int put = Math.max(0, 5 - contains);

        if (sevenNum[col] > 0) {
            sevenNum[col]--;
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

    public int howManyContains(GridPane[] columns, int col){
        int counter=0;
        for(Node n : columns[col].getChildren()){
            if(n instanceof Circle) counter++;
        }
        if(sevenNum[col]>0) return sevenNum[col]+6;
        return counter;
    }

    // Times / reset
    public void setTimes(int t){ times = t; }
    public int getTimes(){return times;}
    public void reset(){
        countDice=1;
        played=true;
        workIt=true;
    }

    public static int[] getQuestions(){return questions;}

    // Questions / difficulty
    private void promptQuestionEachTurn(String from){
        if(!"Easy".equalsIgnoreCase(difficulty)){
            Random r = new Random();
            int x = r.nextInt(3)+1;
            String diff;
            switch(x){
                case 1: diff="Easy";break;
                case 2: diff="Medium";break;
                default: diff="Hard";break;
            }
            QuestionScreen qScreen = new QuestionScreen();
            qScreen.show(mainStage, diff, from);
        }
    }

    public void handleQuestionSpot(GridPane[] columns, int col){
        for(int q : questions){
            if(q == col){
                if("Hard".equalsIgnoreCase(difficulty)){
                    promptQuestionEachTurn("spot");
                } else {
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
     * Surprise => extra turn
     */
    private void handleSurpriseSpot(GridPane[] columns, int col, boolean isBlue) {
        if (col == surprise && counter==0) {
            System.out.println("Surprise spot reached!");
			counter++;

            SurprisePopUp pop = new SurprisePopUp();
            pop.show(mainStage);

            if (isBlue) {
                blueExtraTurnGranted = true;
                System.out.println("Blue player granted an extra turn.");
            } else {
                blackExtraTurnGranted = true;
                System.out.println("Black player granted an extra turn.");
            }

            resetTurnCompletion();
            Platform.runLater(() -> waitForTurnCompletion(columns, isBlue));
        }
    }

    // Turn completion logic
    private boolean isTurnComplete() {
        return turnComplete;
    }

    private void resetTurnCompletion() {
        this.turnComplete = false;
    }

    private void setTurnComplete() {
        this.turnComplete = true;
    }

    private boolean hasPawnsToReenter(boolean isBlue) {
        return (isBlue && outBlue > 0) || (!isBlue && outBlack > 0);
    }

    private void waitForTurnCompletion(GridPane[] columns, boolean isBlue) {
        Timeline waitTimeline = new Timeline();
        waitTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(200), event -> {
            if (availableDice.isEmpty() && !hasPawnsToReenter(isBlue)) {
                setTurnComplete();
                System.out.println("Current turn completed. Granting extra turn.");

                resetTurnCompletion();
                if (isBlue && blueExtraTurnGranted) {
                    blueExtraTurnGranted = false;
                    System.out.println("Initiating Blue's extra turn.");
                    Backgammon.startingPlayer = true;
                } else if (!isBlue && blackExtraTurnGranted) {
                    blackExtraTurnGranted = false;
                    System.out.println("Initiating Black's extra turn.");
                    Backgammon.startingPlayer = false;
                }
                waitTimeline.stop();
            }
        }));
        waitTimeline.setCycleCount(Animation.INDEFINITE);
        waitTimeline.play();
    }

    // Game history
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
                "  }",
                p1, p2, winner, diff, sec);
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

    // Utility
    private void clearHighlights(GridPane[] board){
        for(int c=0; c<24; c++){
            board[c].setStyle(null);
            board[c].setOnMouseClicked(null);
        }
    }

    private void useDie(int die){
        availableDice.remove((Integer) die);
    }

    private void showAlert(String title, String message){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void resetTurn() {
        countDice = 1;
        played = true;
        workIt = true;
        setTurnComplete();
    }

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

    // Accessors
    public int[] getBlueUp() {
        return blueUp;
    }
    public int[] getBlackDown() {
        return blackDown;
    }
}
