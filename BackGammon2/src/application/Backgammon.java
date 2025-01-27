
package application;

import java.awt.Shape;
import java.util.Optional;
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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


/**
 * Main Backgammon class. Uses Builder Pattern for board construction.
 */
public class Backgammon extends Application {


    public static String difficulty = "Easy"; // default
    public static boolean startingPlayer = true; // who starts (true = player1)
    private Pane gameBoard; // Game board pane
    private Stage primaryStage;
    Label timerLabel = new Label("Time: 0s");
    public static GamePlay theGame;
    
    private static Label one = ComponentFactory.createLabel("?");
    private static Label two = ComponentFactory.createLabel("?");
    private MediaPlayer mediaPlayer; // Declare the MediaPlayer at the class level

 

    public static Label getOne() {
        return one;
    }

    public static Label getTwo() {
        return two;
    }

    private void playMusic(String filePath) {
        try {
            Media media = new Media(getClass().getResource(filePath).toExternalForm()); // Load the file
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the music
            mediaPlayer.play(); // Start playing
        } catch (Exception e) {
            System.out.println("Error playing music: " + e.getMessage());
        }
    }    
    private void playMusic2(String filePath) {
        try {
            Media media = new Media(getClass().getResource(filePath).toExternalForm()); // Load the file
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play(); // Start playing
        } catch (Exception e) {
            System.out.println("Error playing music: " + e.getMessage());
        }
    }    
    private void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    } 
    private void resumeMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Create "Game" menu
        Menu gameMenu = new Menu("Game");
        MenuItem restartGameItem = new MenuItem("Restart Game");
        restartGameItem.setOnAction(e -> restartGame());
        MenuItem endGameItem = new MenuItem("End Game");
        endGameItem.setOnAction(e -> endGame());
        gameMenu.getItems().addAll(restartGameItem, endGameItem);

        // Create "Settings" menu
        Menu settingsMenu = new Menu("Settings");
        MenuItem changeColorItem = new MenuItem("Change Board Color");
        changeColorItem.setOnAction(e -> changeBoardColor());
        settingsMenu.getItems().addAll(changeColorItem);
       
       
        
        Menu musicSettings = new Menu("Music");
        MenuItem stopMusicItem = new MenuItem("Stop Music");
        MenuItem resumeMusicItem = new MenuItem("Resume Music");
        stopMusicItem.setOnAction(e -> stopMusic());
        musicSettings.getItems().add(stopMusicItem);
        resumeMusicItem.setOnAction(e -> resumeMusic());
        musicSettings.getItems().add(resumeMusicItem);
        // Add menus to the MenuBar
        menuBar.getMenus().addAll(gameMenu, settingsMenu,musicSettings);

        // Apply custom CSS styling to the menu bar
        menuBar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        gameMenu.setStyle("-fx-text-fill: white;");
        settingsMenu.setStyle("-fx-text-fill: white;");
        for (Menu menu : menuBar.getMenus()) {
            menu.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        }

        return menuBar;
    }

    
   


    private void endGame() {
        // 1. Stop the timer if you're using it
        stopTimer();
        stopMusic();
        one = ComponentFactory.createLabel("?");
        two = ComponentFactory.createLabel("?");
        secondsElapsed = 0; // reset or clear as needed

        // 2. Close the current Backgammon stage
        primaryStage.close();

        // 3. Show the Login screen in a new Stage
        Stage loginStage = new Stage();
        Login login = new Login();
        try {
            login.start(loginStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


	private void restartGame() {
    // Optionally, you could prompt the user for confirmation here
    // using an Alert or Dialog.
     stopMusic();
     one = ComponentFactory.createLabel("?");
     two = ComponentFactory.createLabel("?");
    // 1. Stop the timer
    stopTimer();
    secondsElapsed = 0;

    // 2. Close the current primary stage
    primaryStage.close();

    // 3. Create a new instance of Backgammon and show it
    Stage newStage = new Stage();
    Backgammon newGame = new Backgammon(); 
    try {
        newGame.start(newStage);
    } catch (Exception e) {
        e.printStackTrace();
    }
}


	private void changeBoardColor() {
        // Create a dialog to select a theme
        Dialog<String> themeDialog = new Dialog<>();
        themeDialog.setTitle("Change Board Background");
        themeDialog.setHeaderText("Select a new theme for the game board:");

        // Options for different themes
        ChoiceBox<String> themeSelector = new ChoiceBox<>();
        themeSelector.getItems().addAll("Default", "Black and White", "Yellow and White", "Green and White");
        themeSelector.setValue("Default");

        themeDialog.getDialogPane().setContent(themeSelector);

        // Add OK and Cancel buttons
        themeDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Handle the result
        themeDialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return themeSelector.getValue();
            }
            return null;
        });

        themeDialog.showAndWait().ifPresent(selectedTheme -> {
            // Call the `changeBackground` method from `Firstlayer` with the selected theme
            Firstlayer.changeBackground(selectedTheme);
        });
    }


    private static EventManager eventManager = new EventManager("diceRolled", "timerUpdated");

    public static int secondsElapsed = 0;
    private static Timeline timeline;
    
    @Override
    public void start(Stage primaryStage) {
        System.out.println("ENTERING START METHOD (difficulty=" + difficulty + ")");
        

        // Create MenuBar
        this.primaryStage = primaryStage;

        MenuBar menuBar = createMenuBar();

        // Timer and Dice Observers
        TimerObserver timerObserver = new TimerObserver(timerLabel);
        eventManager.subscribe("timerUpdated", timerObserver);

        DiceObserver diceObserver = new DiceObserver(one, two);
        eventManager.subscribe("diceRolled", diceObserver);

        // Start the timer
        startTimer();
        
        // Build the game board using Builder Pattern
        BoardBuilder boardBuilder = new BoardBuilder();
        Pane board = boardBuilder
                .setBoardColor(Color.LIGHTBLUE)
                .addBackground()
                .buildGrid()
                .build();

        // Add first layer (board design)
        Firstlayer first = new Firstlayer(primaryStage);
        board.getChildren().addAll(first.getBoard());
       

        // Build GridPane array for 24 columns
        GridPane[] gridCols = new GridPane[24];
        for (int i = 0; i < 12; i++) {
            SecondLayer up = new SecondLayer();
            gridCols[i] = up.setLayoutUp(i);
            board.getChildren().add(gridCols[i]);
        }
        for (int i = 12; i < 24; i++) {
            SecondLayer down = new SecondLayer();
            gridCols[i] = down.setLayoutDown(i);
            board.getChildren().add(gridCols[i]);
        }

        // Create the GamePlay logic
        theGame = new GamePlay(gridCols, primaryStage, difficulty);
       
        // Determine who starts via WhoStarts screen (dice)
        WhoStarts whoStartsScreen = new WhoStarts();
        startingPlayer = whoStartsScreen.determineStartingPlayer(primaryStage);
        playMusic("/View/music.wav");
        // Figure out actual names or fallback
        String p1Name = (Login.player1 == null || Login.player1.trim().isEmpty())
                ? "Player1" : Login.player1;
        String p2Name = (Login.player2 == null || Login.player2.trim().isEmpty())
                ? "Player2" : Login.player2;

        String starter = startingPlayer ? p1Name : p2Name;
        System.out.println("Starting Player: " + starter);

        initializeGame(primaryStage, starter);

        // Timer label
        timerLabel.setLayoutX(950);
        timerLabel.setLayoutY(0);
        timerLabel.setPrefSize(150, 30);
        board.getChildren().add(timerLabel);

        // Dice button
        Button diceBtn = createDiceButton(p1Name, p2Name, gridCols);
        board.getChildren().addAll(diceBtn, one, two);

        // Place question/surprise visuals
        placeQuestionMarks(gridCols, board);
        placeSurprise(gridCols, board);

        // Create a BorderPane and add the MenuBar and the board
        BorderPane root = new BorderPane();
        root.setTop(menuBar); // Add MenuBar at the top
        root.setCenter(board); // Add the game board in the center

        // Set up the Scene and Stage
        Scene scene = new Scene(root, 1100, 800);
        primaryStage.setTitle("Backgammon");
        primaryStage.setScene(scene);
        primaryStage.setMaxHeight(800);
        primaryStage.setMaxWidth(1100);
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(1100);
        primaryStage.setResizable(false);
        primaryStage.show();
    }



   
    
    private Button createDiceButton(String p1Name, String p2Name, GridPane[] gridCols) {
        Button diceBtn = new Button("Roll Dice 🎲");
        diceBtn.setLayoutX(10);
        diceBtn.setLayoutY(5);
        diceBtn.setPrefSize(150, 30);
        diceBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");

        one.setFont(Font.font(null, FontWeight.BOLD, 72));
        two.setFont(Font.font(null, FontWeight.BOLD, 72));

        one.setLayoutX(528);
        one.setLayoutY(360);
        one.setStyle("-fx-text-fill: white;");
        two.setLayoutX(528);
        two.setLayoutY(290);
        two.setStyle("-fx-text-fill: white;");

        diceBtn.setOnAction(event -> {
            int dice1 = rollDice(difficulty);
            int dice2 = rollDice(difficulty);

            eventManager.notify("diceRolled", new int[]{dice1, dice2});

            if (startingPlayer) {
                if (dice1 == dice2) 
                	{
                	theGame.setTimes(4);
                	playMusic2("/View/bomboclat.wav");
                	
                	}
                diceBtn.setText(p2Name + "'s Turn 🎲");
                theGame.reset();
                theGame.bluePlays(gridCols, dice1, dice2);
                startingPlayer = false;
            } else {
                if (dice1 == dice2)
                	{
                    	theGame.setTimes(4);
                    	playMusic2("/View/bomboclat.wav");
                    }
                
                diceBtn.setText(p1Name + "'s Turn 🎲");
                theGame.reset();
                theGame.blackPlays(gridCols, dice1, dice2);
                startingPlayer = true;
            }
        });

        return diceBtn;
    }

    private void initializeGame(Stage primaryStage, String starterName) {
        System.out.println("Initializing game with starting player: " + starterName);
        primaryStage.setTitle("Backgammon - " + starterName + " starts!");
    }

    private static final int[] HARD_DICE_VALUES = {-3, -2, -1, 1, 2, 3, 4, 5, 6};

    public static int rollDice(String difficulty) {
        Random rand = new Random();
        if ("Hard".equalsIgnoreCase(difficulty)) {
            int index = rand.nextInt(HARD_DICE_VALUES.length);
            return HARD_DICE_VALUES[index];
        } else {
            return rand.nextInt(6) + 1;
        }
    }

    public void placeQuestionMarks(GridPane[] gridCols, Pane parentPane) {
        int[] questions = GamePlay.getQuestions();
        for (int spot : questions) {
            if (spot != -1) {
                Label qm = new Label("?");
                qm.setStyle("-fx-text-fill: purple; -fx-font-size: 72;");

                double layoutX = SecondLayer.cols[spot];
                double layoutY = (spot < 12) ? 10 : 630;
                qm.setLayoutX(layoutX);
                qm.setLayoutY(layoutY);

                parentPane.getChildren().add(qm);
            }
        }
    }

    public void placeSurprise(GridPane[] gridCols, Pane parentPane) {
        Label surprise = new Label("🎁");
        surprise.setStyle("-fx-text-fill: purple; -fx-font-size: 60;");

        int gridIndex = GamePlay.surprise;
        double layoutX = SecondLayer.cols[gridIndex];
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
            String timeString = String.format("Time: %02d:%02d", mins, secs);
            eventManager.notify("timerUpdated", timeString);
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
