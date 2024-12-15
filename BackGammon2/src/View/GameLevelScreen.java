package View;

import application.Backgammon;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GameLevelScreen extends Application {

    private String selectedDifficulty; // To hold the selected difficulty level

    @Override
    public void start(Stage primaryStage) {
        // Background image with Gaussian blur
        Image backgroundImage = new Image(getClass().getResourceAsStream("backgammon2.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(1000);
        backgroundImageView.setFitHeight(700);
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setEffect(new GaussianBlur(50));

        // Main layout
        StackPane root = new StackPane();
        VBox levelLayout = new VBox(20);
        levelLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Title Label
        Label titleLabel = new Label("Select Game Difficulty");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        titleLabel.setStyle("-fx-text-fill: white;");

        // Difficulty Buttons
        Button easyButton = createStyledButton("Easy");
        Button mediumButton = createStyledButton("Medium");
        Button hardButton = createStyledButton("Hard");

        // Button Actions
        easyButton.setOnAction(event -> startBackgammon(primaryStage, "Easy"));
        mediumButton.setOnAction(event -> startBackgammon(primaryStage, "Medium"));
        hardButton.setOnAction(event -> startBackgammon(primaryStage, "Hard"));

        // Back Button
        Button backButton = createStyledButton("Back");
        backButton.setOnAction(event -> {
            Login loginScreen = new Login();
            loginScreen.start(primaryStage);
        });

        // Add elements to layout
        levelLayout.getChildren().addAll(titleLabel, easyButton, mediumButton, hardButton, backButton);
        root.getChildren().addAll(backgroundImageView, levelLayout);

        // Scene setup
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Game Level Selection");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Helper method to create styled buttons.
     */
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 18; -fx-pref-width: 200; -fx-pref-height: 50;");
        return button;
    }

    /**
     * Opens the Backgammon game and passes the selected difficulty.
     */
    private void startBackgammon(Stage primaryStage, String difficulty) {
        System.out.println("Starting Backgammon game with level: " + difficulty);
        Backgammon game = new Backgammon();
        game.setDifficulty(difficulty); // Pass difficulty to Backgammon
        game.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
