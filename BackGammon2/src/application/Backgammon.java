package application;

import java.util.Random;

import Control.GamePlay;
import View.Firstlayer;
import View.Login;
import View.QuestionScreen;
import View.SecondLayer;
import View.SurprisePopUp;
import View.WhoStarts;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Main Backgammon class. Calls GamePlay for board logic and
 * sets up UI. Dice in Hard mode can be -3..6 (negative means move backward).
 */
public class Backgammon extends Application {

    public static int secondsElapsed = 0;
    private static Timeline timeline;
    

    private String difficulty = "Easy"; // default
    public static boolean startingPlayer = true; // who starts (true = player1)

    Label timerLabel = new Label("Time: 0s");
    public static GamePlay theGame;
    private static Label one = new Label("?");
    private static Label two = new Label("?");
    
    public static Label getOne() {
    	return one;
    }
    
    public static Label getTwo() {
    	return two;
    }
    
    @Override
    public void start(Stage primaryStage) {
        System.out.println("ENTERING START METHOD (difficulty=" + difficulty + ")");

        startTimer();

        // Create the main Pane
        Pane pane = new Pane();
        Firstlayer first = new Firstlayer(primaryStage);
        pane.getChildren().addAll(first.getBoard());

        // Build GridPane array for 24 columns
        GridPane[] gridCols = new GridPane[24];
        for (int i = 0; i < 12; i++) {
            SecondLayer up = new SecondLayer();
            gridCols[i] = up.setLayoutUp(i);
            pane.getChildren().add(gridCols[i]);
        }
        for (int i = 12; i < 24; i++) {
            SecondLayer down = new SecondLayer();
            gridCols[i] = down.setLayoutDown(i);
            pane.getChildren().add(gridCols[i]);
        }

        // Create the GamePlay logic
        theGame = new GamePlay(gridCols, primaryStage, difficulty);

        // Determine who starts via WhoStarts screen (dice)
        WhoStarts whoStartsScreen = new WhoStarts();
        startingPlayer = whoStartsScreen.determineStartingPlayer(primaryStage);

        // Figure out actual names or fallback
        String p1Name = (Login.player1 == null || Login.player1.trim().isEmpty())
                ? "Player1" : Login.player1;
        String p2Name = (Login.player2 == null || Login.player2.trim().isEmpty())
                ? "Player2" : Login.player2;

        String starter = startingPlayer ? p1Name : p2Name;
        System.out.println("Starting Player: " + starter);

        initializeGame(primaryStage, starter);

        // Timer label
        timerLabel.setFont(Font.font(null, FontWeight.BOLD, 18));
        timerLabel.setStyle("-fx-text-fill: white;");
        timerLabel.setLayoutX(950);
        timerLabel.setLayoutY(0);
        timerLabel.setPrefSize(150, 30);
        pane.getChildren().add(timerLabel);

        // Dice button
        Button diceBtn = new Button("Roll Dice 🎲");
        diceBtn.setLayoutX(10);
        diceBtn.setLayoutY(5);
        diceBtn.setPrefSize(150, 30);
        diceBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; "
                + "-fx-font-size: 14px; -fx-background-radius: 10; -fx-font-weight: bold;");

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GRAY);
        shadow.setRadius(10);
        diceBtn.setEffect(shadow);

        diceBtn.setOnMouseEntered(e -> diceBtn.setStyle("-fx-background-color: #45A049; -fx-text-fill: white; "
                + "-fx-font-size: 14px; -fx-background-radius: 10; -fx-font-weight: bold;"));
        diceBtn.setOnMouseExited(e -> diceBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; "
                + "-fx-font-size: 14px; -fx-background-radius: 10; -fx-font-weight: bold;"));

        // Dice result labels
        //Label one = new Label("?");
        //Label two = new Label("?");
        one.setFont(Font.font(null, FontWeight.BOLD, 72));
        two.setFont(Font.font(null, FontWeight.BOLD, 72));

        one.setLayoutX(528);
        one.setLayoutY(360);
        one.setStyle("-fx-text-fill: white;");
        two.setLayoutX(528);
        two.setLayoutY(290);
        two.setStyle("-fx-text-fill: white;");

        // On dice click
        diceBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Dice roll button clicked!");

                int dice1 = rollDice(difficulty);
                int dice2 = rollDice(difficulty);

                System.out.println("Dice One: " + dice1);
                System.out.println("Dice Two: " + dice2);

                one.setText(String.valueOf(dice1));
                two.setText(String.valueOf(dice2));

                if (startingPlayer) {
                    // If doubles => times=4
                    if (dice1 == dice2) theGame.setTimes(4);
                    startingPlayer = false;
                    diceBtn.setText(p2Name + "'s Turn 🎲");
                    theGame.reset();
                    theGame.bluePlays(gridCols, dice1, dice2);
                } else {
                    if (dice1 == dice2) theGame.setTimes(4);
                    startingPlayer = true;
                    diceBtn.setText(p1Name + "'s Turn 🎲");
                    theGame.reset();
                    theGame.blackPlays(gridCols, dice1, dice2);
                }
            }
        });

        // Place question/surprise visuals
        placeQuestionMarks(gridCols, pane);
        placeSurprise(gridCols, pane);

        pane.getChildren().addAll(diceBtn, one, two);

        Scene scene = new Scene(pane, 1100, 800);
        primaryStage.setTitle("Backgammon");
        primaryStage.setScene(scene);
        primaryStage.setMaxHeight(800);
        primaryStage.setMaxWidth(1100);
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(1100);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * rollDice with Hard mode = -3..6 (no re-roll).
     */
    private static final int[] HARD_DICE_VALUES = {-3, -2, -1, 1, 2, 3, 4, 5, 6};
    public static int rollDice(String difficulty) {
    	Random rand = new Random();
        if ("Hard".equalsIgnoreCase(difficulty)) {
            // Select a random index from the HARD_DICE_VALUES array
            int index = rand.nextInt(HARD_DICE_VALUES.length);
            int rolledValue = HARD_DICE_VALUES[index];
            System.out.println("Hard difficulty roll: " + rolledValue);
            return rolledValue;
        } else {
            // Standard die roll: 1 to 6
            int rolledValue = rand.nextInt(6) + 1;
            System.out.println("Standard difficulty roll: " + rolledValue);
            return rolledValue;
        }
    }

    // Place question marks
    private final SecondLayer secondLayer = new SecondLayer();
    public void placeQuestionMarks(GridPane[] gridCols, Pane parentPane) {
        int[] questions = GamePlay.getQuestions();
        for (int spot : questions) {
            if (spot != -1) {
                Label qm = new Label("?");
                qm.setFont(Font.font(null, FontWeight.BOLD, 72));
                qm.setStyle("-fx-text-fill: purple;");

                double layoutX = secondLayer.cols[spot];
                double layoutY = (spot < 12) ? 10 : 630;
                qm.setLayoutX(layoutX);
                qm.setLayoutY(layoutY);

                parentPane.getChildren().add(qm);
            }
        }
    }

    // Place surprise
    public void placeSurprise(GridPane[] gridCols, Pane parentPane) {
        Label surprise = new Label("🎁");
        surprise.setFont(Font.font(null, FontWeight.BOLD, 60));
        surprise.setStyle("-fx-text-fill: purple;");

        int gridIndex = GamePlay.surprise;
        double layoutX = secondLayer.cols[gridIndex];
        double layoutY = (gridIndex < 12) ? 10 : 630;

        surprise.setLayoutX(layoutX);
        surprise.setLayoutY(layoutY);

        parentPane.getChildren().add(surprise);
        
    }

    public void startTimer() {
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), e -> {
            secondsElapsed++;
            int mins = secondsElapsed / 60;
            int secs = secondsElapsed % 60;
            if (mins < 10 && secs < 10) {
                timerLabel.setText(String.format("Time: 0%d : 0%d", mins, secs));
            } else if (mins < 10) {
                timerLabel.setText(String.format("Time: 0%d : %d", mins, secs));
            } else if (secs < 10) {
                timerLabel.setText(String.format("Time: %d : 0%d", mins, secs));
            } else {
                timerLabel.setText(String.format("Time: %d : %d", mins, secs));
            }
        });
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public static void stopTimer() {
        if (timeline != null) {
            timeline.stop();
            System.out.println("Final elapsed time: " + secondsElapsed + " seconds");
        }
    }

    private void initializeGame(Stage primaryStage, String starterName) {
        System.out.println("Initializing game with starting player: " + starterName);
        primaryStage.setTitle("Backgammon - " + starterName + " starts!");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setDifficulty(String selectedDifficulty) {
        if (selectedDifficulty != null && !selectedDifficulty.trim().isEmpty()) {
            this.difficulty = selectedDifficulty;
        }
    }
    
    public String getDifficulty() {
    	return difficulty;
    }
}
