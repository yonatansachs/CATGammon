package View;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.GaussianBlur;
import Control.GamePlay;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class QuestionLevel {
	static String questionDifficulty2 = "";
    public void show(Stage ownerStage, String questionDifficulty) {
        // Declare the stage first to ensure it's accessible
        Stage stage = new Stage();
        questionDifficulty2 = questionDifficulty;
        // Background setup
        StackPane root = new StackPane();
        Image backgroundImage = new Image(getClass().getResourceAsStream("backgammon2.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(400);
        backgroundImageView.setFitHeight(300);
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setEffect(new GaussianBlur(50));

        // Text to display the difficulty level
        Text questionLevelText = new Text("Your question level is " + questionDifficulty);
        questionLevelText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        questionLevelText.setStyle("-fx-fill: white; -fx-effect: dropshadow(gaussian, black, 5, 0, 0, 0);");

        // Close button
        Button closeButton = new Button("Close");
        closeButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        closeButton.setStyle(
                "-fx-background-color: linear-gradient(#6a11cb, #2575fc);" +
                "-fx-text-fill: white; -fx-background-radius: 20; -fx-pref-width: 120;");

        // Set the button action to transition to QuestionScreen and close this stage
        closeButton.setOnAction(e -> {
            // Create a new instance of the QuestionScreen
            QuestionScreen questionScreen = new QuestionScreen();

            // Show the QuestionScreen using GamePlay's main stage and difficulty
            questionScreen.show(GamePlay.mainStage, GamePlay.difficulty);

            // Close the current QuestionLevel screen
            stage.close();
        });

        // Layout setup
        BorderPane layout = new BorderPane();
        layout.setCenter(questionLevelText);
        layout.setBottom(closeButton);
        BorderPane.setAlignment(questionLevelText, Pos.CENTER);
        BorderPane.setAlignment(closeButton, Pos.CENTER);
        BorderPane.setMargin(closeButton, new Insets(20, 0, 20, 0));

        root.getChildren().addAll(backgroundImageView, layout);

        // Scene setup
        Scene scene = new Scene(root, 400, 300);

        // Configure the stage
        stage.initOwner(ownerStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Question Level");
        stage.setScene(scene);
        stage.show();
    }
}
