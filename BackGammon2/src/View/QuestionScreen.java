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
    public static boolean bool = false;
    public static String from = "";// to know if the question came from stepping on spot or just dice roll

    public void show(Stage owner, String difficulty,String from) {
        // Fetch a random question based on the given difficulty
        Question randomQuestion = sysData.getRandomQuestion(difficulty);
        this.from = from;
        if (randomQuestion == null) {
            showError("No questions available for the selected difficulty.");
            return;
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
        for (int i = 0; i < options.length; i++) {
            Button optionButton = new Button(options[i]);
            optionButton.setStyle("-fx-font-size: 14px; -fx-pref-width: 300; -fx-wrap-text: true;");
            optionButton.setWrapText(true); // Allow text wrapping for long answers

            // Check answer on button click
            int selectedOption = i;
            optionButton.setOnAction(event -> {
                if (selectedOption == randomQuestion.getCorrectAnswerIndex()) {
                    showCorrectMessage(questionStage); // Close stage only when correct
                } else {
                    showWrongMessage(); // Keep the stage open for incorrect answers
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

        Scene scene = new Scene(root, 800, 590);
        questionStage.setTitle("Answer the Question");
        questionStage.setScene(scene);
        questionStage.setOnCloseRequest(event -> event.consume()); // Prevent closing the window manually
        questionStage.showAndWait(); // Block interaction with other windows
    }

    // Display a success message and close the stage only if the answer is correct
    private void showCorrectMessage(Stage questionStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Correct!");
        alert.setHeaderText(null);
        alert.setContentText("Congratulations! Your answer is correct.");
        alert.showAndWait();
        questionStage.close(); // Close the question stage
    }

    // Display a failure message
    private void showWrongMessage() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Wrong!");
        alert.setHeaderText(null);
		if (GamePlay.difficulty.equals("Hard")&&from.equals("game")) // if question is from the dice roll there is an option to loose your turn
        {
            alert.setContentText("Oops! That is not the correct answer. You just lost your turn!");
            if(Backgammon.startingPlayer)
            {
            	Backgammon.startingPlayer = false;
            }
            else
            	Backgammon.startingPlayer = true;

            	
        }
		else if (GamePlay.difficulty.equals("Hard")&&from.equals("spot"))// question from stepping on question mark
		{
            alert.setContentText("Oops! That is not the correct answer. you're here until you answer correctly!");

		}
        
        else
            alert.setContentText("Oops! That is not the correct answer. Try again!");
        alert.showAndWait();
    }

    // Display an error message
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
