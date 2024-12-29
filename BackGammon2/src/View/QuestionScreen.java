package View;

import Model.Question;
import Model.SysData;
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

    /*public void show(Stage owner, String difficulty) {
        // Fetch a random question based on the given difficulty
        Question randomQuestion = sysData.getRandomQuestion(difficulty);

        if (randomQuestion == null) {
            showError("No questions available for the selected difficulty.");
            return;
        }

        Stage questionStage = new Stage();
        questionStage.initOwner(owner);
        questionStage.initModality(Modality.APPLICATION_MODAL);

        // Background Image with GaussianBlur
        Image backgroundImage = new Image(getClass().getResourceAsStream("backgammon2.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(400); 
        backgroundImageView.setFitHeight(300);
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setEffect(new GaussianBlur(50));

        // Question Label
        Label questionLabel = new Label(randomQuestion.getQuestionText());
        questionLabel.setStyle("-fx-font-size: 16px; -fx-wrap-text: true; -fx-text-fill: white;");
        questionLabel.setWrapText(true);
        questionLabel.setAlignment(Pos.CENTER);

        // Options Buttons
        VBox optionsBox = new VBox(10); // Vertical box with spacing
        optionsBox.setAlignment(Pos.CENTER); // Center align
        optionsBox.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-padding: 10;");

        // Adding buttons
        String[] options = randomQuestion.getOptions();
        for (int i = 0; i < options.length; i++) {
            Button optionButton = new Button(options[i]);
            optionButton.setStyle("-fx-font-size: 14px; -fx-pref-width: 250; -fx-wrap-text: true;");
            optionButton.setWrapText(true); // Allow text wrapping for long answers

            // Lambda to check answer
            int selectedOption = i;
            optionButton.setOnAction(event -> {
                if (selectedOption == randomQuestion.getCorrectAnswerIndex()) {
                    showCorrectMessage(questionStage);
                } else {
                    showWrongMessage();
                }
            });

            optionsBox.getChildren().add(optionButton); // Add button to VBox
        }


        // Layout
        VBox rootLayout = new VBox(20, questionLabel, optionsBox);
        rootLayout.setAlignment(Pos.CENTER);

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, rootLayout);
        root.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(root, 800, 590);
        questionStage.setTitle("Answer the Question");
        questionStage.setScene(scene);
        questionStage.showAndWait();
    }*/
    
    public void show(Stage owner, String difficulty) {
        // Fetch a random question based on the given difficulty
        Question randomQuestion = sysData.getRandomQuestion(difficulty);

        if (randomQuestion == null) {
            showError("No questions available for the selected difficulty.");
            return;
        }

        Stage questionStage = new Stage();
        questionStage.initOwner(owner);
        questionStage.initModality(Modality.APPLICATION_MODAL);

        // Background Image with GaussianBlur
        Image backgroundImage = new Image(getClass().getResourceAsStream("backgammon2.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(800); // Match the scene width
        backgroundImageView.setFitHeight(590); // Match the scene height
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setEffect(new GaussianBlur(50));

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
                    showCorrectMessage(questionStage);
                } else {
                    showWrongMessage();
                }
            });

            optionsBox.getChildren().add(optionButton); // Add button to VBox
        }

        // Add ScrollPane in case options exceed visible space
        ScrollPane scrollPane = new ScrollPane(optionsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-padding: 0;");

        // Layout
        VBox rootLayout = new VBox(20, questionLabel, scrollPane);
        rootLayout.setAlignment(Pos.CENTER);
        rootLayout.setStyle("-fx-background-color: transparent; -fx-padding: 20;");

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, rootLayout);

        Scene scene = new Scene(root, 800, 590);
        questionStage.setTitle("Answer the Question");
        questionStage.setScene(scene);
        questionStage.showAndWait();
    }
    

    // Display a success message
    private void showCorrectMessage(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Correct!");
        alert.setHeaderText(null);
        alert.setContentText("Congratulations! Your answer is correct.");
        alert.showAndWait();
        stage.close();
    }

    // Display a failure message
    private void showWrongMessage() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Wrong!");
        alert.setHeaderText(null);
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