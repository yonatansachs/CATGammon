package View;

import application.Backgammon;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Login extends Application {

	
    @Override
    public void start(Stage primaryStage) {
        // Create a StackPane for the layout to layer the background and login content
        StackPane root = new StackPane();

        // Add the background image with GaussianBlur
        Image backgroundImage = new Image(getClass().getResourceAsStream("backgammon2.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(1000); // Adjust to your scene width
        backgroundImageView.setFitHeight(700); // Adjust to your scene height
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setEffect(new GaussianBlur(50)); // Apply Gaussian blur effect

        // Create a VBox for the login layout
        VBox loginLayout = new VBox(10);
        loginLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Add a title to the login screen
        Label titleLabel = new Label("Welcome to CATGammon!");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 40)); // Set font size and weight
        titleLabel.setStyle("-fx-text-fill: white;"); // Set text color to white

        // Add login elements
        Label player1Label = new Label("Player 1:");
        player1Label.setFont(Font.font("Arial", FontWeight.BOLD, 25)); // Bold and larger font
        player1Label.setStyle("-fx-text-fill: white;"); // Set text color to white

        TextField player1Field = new TextField();
        player1Field.setMaxWidth(300); // Set maximum width explicitly
        player1Field.setPrefHeight(30); // Set height explicitly

        Label player2Label = new Label("Player 2:");
        player2Label.setFont(Font.font("Arial", FontWeight.BOLD, 25)); // Bold and larger font
        player2Label.setStyle("-fx-text-fill: white;"); // Set text color to white

        TextField player2Field = new TextField();
        player2Field.setMaxWidth(300); // Set maximum width explicitly
        player2Field.setPrefHeight(30); // Set height explicitly

        Button loginButton = new Button("Login");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        loginButton.setOnAction(event -> {
            String player1 = player1Field.getText();
            String player2 = player2Field.getText();

            if (validateCredentials(player1, player2)) {
                // Transition to Game Level Selection screen
                GameLevelScreen levelSelectionScreen = new GameLevelScreen();
                levelSelectionScreen.start(primaryStage); // Navigate to the LevelSelectionScreen
            } else {
                errorLabel.setText("Invalid username or password.");
            }
        });

        // Add "History" button
        Button historyButton = new Button("History");
        historyButton.setOnAction(event -> {
            HistoryScreen historyScreen = new HistoryScreen();
            historyScreen.start(primaryStage); // Switch to the History screen
        });

        // Add all elements to the VBox
        loginLayout.getChildren().addAll(titleLabel,
                player1Label, player1Field,
                player2Label, player2Field,
                loginButton, errorLabel,
                historyButton // Add History button
        );

        // Add the background and login layout to the root
        root.getChildren().addAll(backgroundImageView, loginLayout);

        // Create and set the scene
        Scene loginScene = new Scene(root, 1000, 700); // Adjust size as needed
        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
    
    public void openBackgammon(Stage primaryStage, String selectedDifficulty) {
        Backgammon game = new Backgammon();
        game.setDifficulty(selectedDifficulty); // Add a setter method to Backgammon
        game.start(primaryStage);
    }


    private boolean validateCredentials(String player1, String player2) {
        // Replace with real authentication logic
        return player1 != null && !player1.trim().isEmpty() &&
               player2 != null && !player2.trim().isEmpty();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
