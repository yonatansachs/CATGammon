// File: View/QuestionScreen.java
/*
package View;

import Control.GamePlay;
import Model.Question;
import Model.SysData;
import application.Backgammon;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class QuestionScreen {

    private final SysData sysData = SysData.getInstance(); // Singleton SysData
    
    private boolean answeredCorrectly;

    public boolean showAndWaitForResult(Stage mainStage, String difficulty, String from) {
        // Set up the question UI and logic
        Stage questionStage = new Stage();
        questionStage.initOwner(mainStage);

        // Show the question and wait for the answer
        questionStage.showAndWait();

        // The result (true/false) should be set based on the user's answer
        return answeredCorrectly;
    }

    public void setAnsweredCorrectly(boolean isCorrect) {
        this.answeredCorrectly = isCorrect;
    }

    
    public boolean show(Stage owner, String difficulty, String from) {
        // Fetch a random question based on the given difficulty
        Question randomQuestion = sysData.getRandomQuestion(difficulty);
        if (randomQuestion == null) {
            showError("No questions available for the selected difficulty.");
            return true; // Allow the game to continue if no question is available
        }

        Stage questionStage = new Stage();
        questionStage.initOwner(owner);
        questionStage.initModality(Modality.APPLICATION_MODAL); // Ensures blocking of the parent stage

        // Background Image with GaussianBlur
        Image backgroundImage = new Image(getClass().getResourceAsStream("backgammon2.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(800); // Match the scene width
        backgroundImageView.setFitHeight(590); // Match the scene height
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setEffect(new GaussianBlur(50));

        // Title Label
        Label titleLabel = new Label("Question Dice Result: " + difficulty);
        titleLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 20;");
        titleLabel.setAlignment(Pos.CENTER);

        // Question Label
        Label questionLabel = new Label(randomQuestion.getQuestionText());
        questionLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");
        questionLabel.setWrapText(true);
        questionLabel.setAlignment(Pos.CENTER);

        // Options Buttons
        VBox optionsBox = new VBox(15); // Increased spacing between buttons
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-padding: 20; -fx-border-radius: 10;");

        // Adding buttons dynamically
        String[] options = randomQuestion.getOptions();
        Button[] optionButtons = new Button[options.length];
        for (int i = 0; i < options.length; i++) {
            Button optionButton = new Button(options[i]);
            optionButton.setStyle("-fx-font-size: 14px; -fx-pref-width: 300; -fx-wrap-text: true;");
            optionButton.setWrapText(true); // Allow text wrapping for long answers

            // Check answer on button click
            int selectedOption = i;
            optionButton.setOnAction(event -> {
                if (selectedOption == randomQuestion.getCorrectAnswerIndex()) {
                    showCorrectMessage(questionStage);
                } else {
                    showWrongMessage(questionStage, from, difficulty);
                }
            });

            optionsBox.getChildren().add(optionButton); // Add button to VBox
            optionButtons[i] = optionButton;
        }

        // Add ScrollPane in case options exceed visible space
        ScrollPane scrollPane = new ScrollPane(optionsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-padding: 0;");

        // Layout
        VBox rootLayout = new VBox(20, titleLabel, questionLabel, scrollPane);
        rootLayout.setAlignment(Pos.CENTER);
        rootLayout.setStyle("-fx-background-color: transparent; -fx-padding: 20;");

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, rootLayout);

        Scene scene = new Scene(root, 800, 590);
        questionStage.setTitle("Answer the Question");
        questionStage.setScene(scene);
        questionStage.setOnCloseRequest(event -> event.consume()); // Prevent closing the window manually

        // Variable to track if the answer was correct or if the game should continue
        final boolean[] canContinue = {false};

        questionStage.showAndWait(); // Block interaction with other windows

        return canContinue[0];
    }
    

    private void showCorrectMessage(Stage questionStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Correct!");
        alert.setHeaderText(null);
        alert.setContentText("Congratulations! Your answer is correct.");
        alert.showAndWait();
        questionStage.close(); // Close the question stage
    }

    
    private void showWrongMessage(Stage questionStage, String from, String difficulty) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Wrong!");
        alert.setHeaderText(null);

        if ("Hard".equalsIgnoreCase(difficulty)) {
            if ("turn".equalsIgnoreCase(from)) {
                // Player loses their turn
                alert.setContentText("Oops! That is not the correct answer. You just lost your turn!");
                alert.showAndWait();
                // Toggle the starting player
                if (Backgammon.startingPlayer) {
                    Backgammon.startingPlayer = false;
                } else {
                    Backgammon.startingPlayer = true;
                }
                questionStage.close(); // Close the question stage, turn is lost
            } else if ("spot".equalsIgnoreCase(from)) {
                // Player must answer correctly to continue
                alert.setContentText("Oops! That is not the correct answer. You're here until you answer correctly!");
                alert.showAndWait();
                // Keep the stage open for another attempt
            }
        } else if ("Medium".equalsIgnoreCase(difficulty)) {
            // Inform the user and allow the game to continue
            alert.setContentText("Oops! That is not the correct answer. You can continue your turn.");
            alert.showAndWait();
            // Allow the game to continue by closing the window
            questionStage.close();
        } else { // "Easy" or other difficulties
            // Inform the user and allow the game to try again
            alert.setContentText("Oops! That is not the correct answer. Try again!");
            alert.showAndWait();
            // Keep the stage open for another attempt
        }
    }

    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}*/
package View;

import java.util.Random;

import Control.GamePlay;
import Model.Question;
import Model.SysData;
import application.Backgammon;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class QuestionScreen {

    private final SysData sysData = SysData.getInstance(); // Singleton SysData
    private boolean answeredCorrectly;
    public String gameDifficulty = Backgammon.difficulty;

    public boolean show(Stage owner, String difficulty2, String from) {
        // Fetch a random question based on the given difficulty
    	 
        Question randomQuestion = sysData.getRandomQuestion(difficulty2);
        if (randomQuestion == null) {
            showError("No questions available for the selected difficulty.");
            return true; // Allow the game to continue if no question is available
        }

        Stage questionStage = new Stage();
        questionStage.initOwner(owner);
        questionStage.initModality(Modality.APPLICATION_MODAL); // Ensures blocking of the parent stage

        // Background Image with GaussianBlur
        Image backgroundImage = new Image(getClass().getResourceAsStream("backgammon2.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(800); // Match the scene width
        backgroundImageView.setFitHeight(600); // Match the scene height
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setEffect(new GaussianBlur(50));

        // Title Label
        Label titleLabel = new Label("Question: " + difficulty2);
        titleLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 20;");
        titleLabel.setAlignment(Pos.CENTER);

        // Question Label
        Label questionLabel = new Label(randomQuestion.getQuestionText());
        questionLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");
        questionLabel.setWrapText(true);
        questionLabel.setAlignment(Pos.CENTER);

        // Options Buttons
        VBox optionsBox = new VBox(15); // Increased spacing between buttons
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-padding: 20; -fx-border-radius: 10;");

        // Adding buttons dynamically
        String[] options = randomQuestion.getOptions();
        for (int i = 0; i < options.length; i++) {
            Button optionButton = new Button(options[i]);
            optionButton.setStyle("-fx-font-size: 14px; -fx-pref-width: 300; -fx-wrap-text: true; "
                    + "-fx-background-color: linear-gradient(to bottom, #00ccff, #0066cc); "
                    + "-fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5;");
            optionButton.setWrapText(true); // Allow text wrapping for long answers

            // Check answer on button click
            int selectedOption = i;
            optionButton.setOnAction(event -> {
                if (selectedOption == randomQuestion.getCorrectAnswerIndex()) {
                    answeredCorrectly = true;
                    showCorrectMessage(questionStage);
                } else {
                    answeredCorrectly = false;
                    showWrongMessage(questionStage, from, gameDifficulty);
                }
            });

            optionsBox.getChildren().add(optionButton); // Add button to VBox
        }

        // Add ScrollPane in case options exceed visible space
        ScrollPane scrollPane = new ScrollPane(optionsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-padding: 0;");

        // Layout
        VBox rootLayout = new VBox(20, titleLabel, questionLabel, scrollPane);
        rootLayout.setAlignment(Pos.CENTER);
        rootLayout.setStyle("-fx-background-color: transparent; -fx-padding: 20;");

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, rootLayout);

        Scene scene = new Scene(root, 800, 600);
        questionStage.setTitle("Answer the Question");
        questionStage.setScene(scene);
        questionStage.setOnCloseRequest(event -> event.consume()); // Prevent closing the window manually

        questionStage.showAndWait(); // Block interaction with other windows

        return answeredCorrectly;
    }

    private void showCorrectMessage(Stage questionStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Correct!");
        alert.setHeaderText(null);
        alert.setContentText("Congratulations! Your answer is correct.");
        alert.showAndWait();
        questionStage.close(); // Close the question stage
    }

    private void showWrongMessage(Stage questionStage, String from, String difficulty) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Wrong!");
        alert.setHeaderText(null);

    
            if ("Hard".equalsIgnoreCase(difficulty)&&"turn".equalsIgnoreCase(from)) {
                alert.setContentText("Oops! That is not the correct answer. You lose your turn.");
                if(Backgammon.startingPlayer)
                {
                	Backgammon.startingPlayer = false;
                }
                else
                	Backgammon.startingPlayer = true;
               
            } 
            else if ("spot".equalsIgnoreCase(from)) {
                alert.setContentText("Oops! That is not the correct answer. Try again.");
            
        } else 
            alert.setContentText("Oops! That is not the correct answer. You can continue your turn.");
           alert.showAndWait();
        
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

