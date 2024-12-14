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

    @Override
    public void start(Stage primaryStage) {
        // Create a StackPane for the layout to layer the background and content
        StackPane root = new StackPane();

        // Add the background image with GaussianBlur
        Image backgroundImage = new Image(getClass().getResourceAsStream("backgammon2.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(1000); // Adjust to your scene width
        backgroundImageView.setFitHeight(700); // Adjust to your scene height
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setEffect(new GaussianBlur(50)); // Apply Gaussian blur effect

        // Create a VBox for the level selection layout
        VBox levelLayout = new VBox(20);
        levelLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Add title to the screen
        Label titleLabel = new Label("Select Game Level");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 40)); // Set font size and weight
        titleLabel.setStyle("-fx-text-fill: white;"); // Set text color to white

        // Create buttons for each level
        Button easyButton = new Button("Easy");
        Button mediumButton = new Button("Medium");
        Button hardButton = new Button("Hard");

        // Style the buttons
        easyButton.setStyle("-fx-font-size: 18; -fx-pref-width: 200; -fx-pref-height: 50;");
        mediumButton.setStyle("-fx-font-size: 18; -fx-pref-width: 200; -fx-pref-height: 50;");
        hardButton.setStyle("-fx-font-size: 18; -fx-pref-width: 200; -fx-pref-height: 50;");

        // Add actions for each button to open the Backgammon screen
        easyButton.setOnAction(event -> openBackgammon(primaryStage, "Easy"));
        mediumButton.setOnAction(event -> openBackgammon(primaryStage, "Medium"));
        hardButton.setOnAction(event -> openBackgammon(primaryStage, "Hard"));

        // Add a "Back" button to return to the login screen
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 18; -fx-pref-width: 200; -fx-pref-height: 50;");
        backButton.setOnAction(event -> {
            Login loginScreen = new Login();
            loginScreen.start(primaryStage); // Go back to the login screen
        });

        // Add all elements to the VBox
        levelLayout.getChildren().addAll(titleLabel, easyButton, mediumButton, hardButton, backButton);

        // Add the background and level layout to the root
        root.getChildren().addAll(backgroundImageView, levelLayout);

        // Create and set the scene
        Scene levelScene = new Scene(root, 1000, 700); // Adjust size as needed
        primaryStage.setTitle("Game Level Selection");
        primaryStage.setScene(levelScene);
        primaryStage.show();
    }

    private void openBackgammon(Stage primaryStage, String level) {
        System.out.println("Starting Backgammon game with level: " + level); // Log the level selected
        Backgammon game = new Backgammon(); // Assuming Backgammon is your game class
        game.start(primaryStage); // Transition to the Backgammon game
    }

    public static void main(String[] args) {
        launch(args);
    }
}
