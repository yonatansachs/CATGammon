package application;

import java.util.Random;

import javafx.animation.AnimationTimer;
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
import Control.GamePlay;
import Model.Pawns;
import Model.SysData;
import View.Firstlayer;
import View.Login;
import View.QuestionScreen;
import View.SecondLayer;
import View.SurprisePopUp;
import View.WhoStarts;

public class Backgammon extends Application {

	public static int secondsElapsed = 0; // Counter for seconds
	private static Timeline timeline;
    private String difficulty;
    public static boolean startingPlayer;
    private boolean surprisePlayed =GamePlay.surprisePlayed;
    private int counter=0;
    Label timerLabel = new Label("Time: 0s");
    public static GamePlay theGame;
    @Override
    public void start(Stage primaryStage) {
   
    	startTimer();
        //-------------------STATEMENTS-------------------------------------
        Pane pane = new Pane();
        Firstlayer first = new Firstlayer(primaryStage);
        pane.getChildren().addAll(first.getBoard());

        GridPane[] gridCols = new GridPane[30];
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

        //GamePlay theGame = new GamePlay(gridCols, primaryStage, difficulty);
        theGame = new GamePlay(gridCols, primaryStage, difficulty);

        
        WhoStarts whoStartsScreen = new WhoStarts();
        startingPlayer = whoStartsScreen.determineStartingPlayer(primaryStage);
        String startingPlayerName =" ";
        if(startingPlayer)
        {
        	startingPlayerName = Login.player1;
        }
        	
        else
        	startingPlayerName = Login.player2;
        
        // Step 2: Proceed to the game with the determined starting player
        System.out.println("Starting Player: " + startingPlayerName);

        // Initialize the game board with the starting player
        
        initializeGame(primaryStage, startingPlayerName);
        
        timerLabel.setFont(Font.font(null, FontWeight.BOLD, 18));
        timerLabel.setStyle("-fx-text-fill: white;");
        timerLabel.setLayoutX(950); // Positioned at the top-right
        timerLabel.setLayoutY(0);
        timerLabel.setPrefSize(150, 30);
        pane.getChildren().add(timerLabel);

        //-------------------BUTTON AND LABEL-------------------------------------
        Button dices = new Button("Roll Dice 🎲");
        dices.setLayoutX(10); // Positioned at the left edge
        dices.setLayoutY(5); 
        dices.setPrefSize(150, 30);
        dices.setStyle("-fx-background-color: #4CAF50; " +   // Green background
                       "-fx-text-fill: white; " +            // White text
                       "-fx-font-size: 14px; " +             // Font size
                       "-fx-background-radius: 10; " +      // Rounded corners
                       "-fx-font-weight: bold;");           // Bold font

        // Drop shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GRAY);
        shadow.setRadius(10);
        dices.setEffect(shadow);

        // Hover effect for button
        dices.setOnMouseEntered(e -> dices.setStyle("-fx-background-color: #45A049; " + 
                                                   "-fx-text-fill: white; " + 
                                                   "-fx-font-size: 14px; " +
                                                   "-fx-background-radius: 10; " +
                                                   "-fx-font-weight: bold;"));
        dices.setOnMouseExited(e -> dices.setStyle("-fx-background-color: #4CAF50; " + 
                                                  "-fx-text-fill: white; " + 
                                                  "-fx-font-size: 14px; " +
                                                  "-fx-background-radius: 10; " +
                                                  "-fx-font-weight: bold;"));

        Label one = new Label("?");
        Label two = new Label("?");
        one.setFont(Font.font(null, FontWeight.BOLD, 72));
        two.setFont(Font.font(null, FontWeight.BOLD, 72));

        one.setLayoutX(528);
        one.setLayoutY(360);
        one.setStyle("-fx-text-fill: white;");
        two.setLayoutX(528);
        two.setLayoutY(290);
        two.setStyle("-fx-text-fill: white;");

        dices.setOnAction(new EventHandler<ActionEvent>() {
           // boolean player = false;

            @Override
            public void handle(ActionEvent event) {
                theGame.setTimes(2);
                QuestionScreen.bool = false;
                if(!difficulty.equals("Easy"))
                	rollQuestionDice();
                
                Random rand = new Random();

                int diceOne = rand.nextInt(6) + 1;
                int diceTwo = rand.nextInt(6) + 1;

                one.setText(String.valueOf(diceOne));
                two.setText(String.valueOf(diceTwo));
                String player1 = Login.player1;
                String player2 = Login.player2;
                if(GamePlay.surprisePlayed&&counter==0)
                {
  
            		if(startingPlayer)
            			startingPlayer = false;
            		else
            			startingPlayer = true;
                	counter++;
                	
                }
                if (startingPlayer) {
                	
                    if (diceOne == diceTwo) theGame.setTimes(4);
                    startingPlayer = false;
                    dices.setText(player2 +"'s Turn 🎲");
                    theGame.reset();
                    theGame.bluePlays(gridCols, diceOne, diceTwo);
                    
                } else {
                    if (diceOne == diceTwo) theGame.setTimes(4);
                    startingPlayer = true;
                    dices.setText(player1 + "'s Turn 🎲");
                    theGame.reset();
                    theGame.blackPlays(gridCols, diceOne, diceTwo);
                   
                }
            }
        });
        placeQuestionMarks(gridCols,pane);
        placeSurprise(gridCols,pane);

        //-------------------SCENE AND STAGE-------------------------------------
        pane.getChildren().addAll(dices, one, two);

        Scene scene = new Scene(pane, 1100, 800); // Fixed screen size
        primaryStage.setTitle("Backgammon");
        primaryStage.setScene(scene);
        primaryStage.setMaxHeight(800);
        primaryStage.setMaxWidth(1100);
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(1100);
        primaryStage.setResizable(false);

        primaryStage.show();
    }
    public void rollQuestionDice()
    {
    	Random rand = new Random();
                int num = rand.nextInt(3)+1;
                String questiondifficulty ="";
                switch(num)
                {
                case 1:
             	   questiondifficulty = "Easy"; 
             	   break;
                case 2:
             	   questiondifficulty = "Medium";
             	   break;
                case 3:
             	   questiondifficulty = "Hard";
             	   break;
                default : break;
                }
                
                QuestionScreen questionLevel = new QuestionScreen();
                questionLevel.show(GamePlay.mainStage, questiondifficulty,"game");
        
    }
    private void placeSurprise(GridPane[] gridCols, Pane parentPane) {
    	Label surprise = new Label("🎁");
    	surprise.setFont(Font.font(null, FontWeight.BOLD, 60));
    	surprise.setStyle("-fx-text-fill: purple;");
    	 int gridIndex =GamePlay.surprise;
         double layoutX = secondLayer.cols[gridIndex];
         double layoutY;

         if (gridIndex < 12) {
             layoutY = 10; // Top row
         } else {
             layoutY = 630; // Bottom row 
         }

         // Explicitly set the layout
         surprise.setLayoutX(layoutX);
         surprise.setLayoutY(layoutY);

         // Add the label to the parent Pane
         parentPane.getChildren().add(surprise);
		
	}
	private final SecondLayer secondLayer = new SecondLayer();

    int[] questions = GamePlay.getQuestions();
    public void placeQuestionMarks(GridPane[] gridCols, Pane parentPane) {
        for (int i = 0; i < questions.length; i++) {
            if (questions[i] != -1) {
                // Create a question mark label
                Label questionMark = new Label("?");
                questionMark.setFont(Font.font(null, FontWeight.BOLD, 72));
                questionMark.setStyle("-fx-text-fill: purple;");

                // Determine layout based on the column and row
                int gridIndex = questions[i];
                double layoutX = secondLayer.cols[gridIndex];
                double layoutY;

                if (gridIndex < 12) {
                    layoutY = 10; // Top row
                } else {
                    layoutY = 630; // Bottom row 
                }

                // Explicitly set the layout
                questionMark.setLayoutX(layoutX);
                questionMark.setLayoutY(layoutY);

                // Add the label to the parent Pane
                parentPane.getChildren().add(questionMark);
            }
        }
    }

    public void startTimer() {
        // Create a KeyFrame that executes every second
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), e -> {
            secondsElapsed++;
            int minutes = secondsElapsed / 60; // Calculate minutes
            int seconds = secondsElapsed % 60; // Calculate remaining seconds
            if(seconds<10)
            {
            	 if(minutes<10)
                     timerLabel.setText(String.format("Time: 0%d : 0%d", minutes, seconds)); // Update the Label
                 else
                 	timerLabel.setText(String.format("Time: %d : 0%d", minutes, seconds)); // Update the Label
            }
            else
            {
            	if(minutes<10)
                    timerLabel.setText(String.format("Time: 0%d : %d", minutes, seconds)); // Update the Label
                else
                	timerLabel.setText(String.format("Time: %d : %d", minutes, seconds)); // Update the Label
            }
            
        });

        // Initialize the Timeline with the KeyFrame
        timeline = new Timeline(keyFrame);

        // Set the cycle count to indefinite to keep the timer running
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Start the timer
        timeline.play();
    }
    public static void stopTimer() {
        if (timeline != null) {
            timeline.stop(); // Stop the timer
            System.out.println("Final elapsed time: " + secondsElapsed + " seconds");
        }
    }
private void initializeGame(Stage primaryStage, String startingPlayer) {
        // Your existing game initialization logic here
        System.out.println("Initializing game with starting player: " + startingPlayer);

        // Placeholder for game setup
        primaryStage.setTitle("Backgammon - Player " + startingPlayer + " starts!");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setDifficulty(String selectedDifficulty) {
        this.difficulty = selectedDifficulty;
    }
}