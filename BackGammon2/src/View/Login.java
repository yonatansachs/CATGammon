package View;

import application.Backgammon;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Login extends Application {

	public static String player1 = "";
	public static String player2 = "";
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
        VBox loginLayout = new VBox(15); // Increased spacing for better layout
        loginLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Add a title to the login screen
        Label titleLabel = new Label("Welcome to CATGammon!");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 45)); // Larger font for the title
        titleLabel.setStyle("-fx-text-fill: white;");
        titleLabel.setEffect(new DropShadow(10, Color.BLACK)); // Shadow effect for better contrast
    
        // Add login elements
        Label player1Label = new Label("Player 1:");
        styleLabel(player1Label);

        TextField player1Field = new TextField();
        styleTextField(player1Field);

        Label player2Label = new Label("Player 2:");
        styleLabel(player2Label);

        TextField player2Field = new TextField();
        styleTextField(player2Field);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-font-style: italic;");

        // Button Styling
        String buttonStyle = "-fx-background-color: #4CAF50; " +    // Green background
                             "-fx-text-fill: white; " +             // White text
                             "-fx-font-size: 18px; " +              // Font size
                             "-fx-background-radius: 20; " +        // Rounded corners
                             "-fx-font-weight: bold; " +            // Bold text
                             "-fx-pref-width: 200; " +              // Preferred width
                             "-fx-pref-height: 40;";                // Preferred height

        String hoverStyle = "-fx-background-color: #45a049;";       // Slightly darker green for hover

        // Login Button
        Button loginButton = new Button("Login");
        loginButton.setStyle(buttonStyle);
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(buttonStyle + hoverStyle));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(buttonStyle));

        loginButton.setOnAction(event -> {
            player1 = player1Field.getText();
            player2 = player2Field.getText();
            

            if (validateCredentials(player1, player2)) {
                // Transition to Game Level Selection screen
                GameLevelScreen levelSelectionScreen = new GameLevelScreen();
                levelSelectionScreen.start(primaryStage); // Navigate to the LevelSelectionScreen
            } else {
                errorLabel.setText("Invalid username or password. Please try again!");
            }
        });

        // History Button
        Button historyButton = new Button("History");
        historyButton.setStyle(buttonStyle);
        historyButton.setOnMouseEntered(e -> historyButton.setStyle(buttonStyle + hoverStyle));
        historyButton.setOnMouseExited(e -> historyButton.setStyle(buttonStyle));

        historyButton.setOnAction(event -> {
            HistoryScreen historyScreen = new HistoryScreen();
            historyScreen.start(primaryStage); // Switch to the History screen
        });

        // Add all elements to the VBox
        loginLayout.getChildren().addAll(titleLabel,
                player1Label, player1Field,
                player2Label, player2Field,
                loginButton, errorLabel,
                historyButton);

        // Add the background and login layout to the root
        root.getChildren().addAll(backgroundImageView, loginLayout);

        // Create and set the scene
        Scene loginScene = new Scene(root, 1000, 700); // Adjust size as needed
        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
        
        
     // Instructions Button
        Button instructionsButton = new Button("Instructions");
        instructionsButton.setStyle(buttonStyle);
        instructionsButton.setOnMouseEntered(e -> instructionsButton.setStyle(buttonStyle + hoverStyle));
        instructionsButton.setOnMouseExited(e -> instructionsButton.setStyle(buttonStyle));

        // Button Action: Open the Instructions Screen
        instructionsButton.setOnAction(event -> {
            BackgammonInstructions instructionsScreen = new BackgammonInstructions();
            instructionsScreen.start(primaryStage); // Navigate to the Instructions screen
        });

        // Add the Instructions Button to the VBox layout
        loginLayout.getChildren().addAll(instructionsButton);

    }

    private boolean validateCredentials(String player1, String player2) {
        // Replace with real authentication logic
        return player1 != null && !player1.trim().isEmpty() &&
               player2 != null && !player2.trim().isEmpty();
    }

    // Style method for Labels
    private void styleLabel(Label label) {
        label.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 22));
        label.setStyle("-fx-text-fill: white; -fx-padding: 5px;");
    }

    // Style method for TextFields
    private void styleTextField(TextField textField) {
        textField.setMaxWidth(300); // Set maximum width explicitly
        textField.setPrefHeight(30); // Set height explicitly
        textField.setStyle("-fx-border-color: #4CAF50; " +          // Green border
                           "-fx-border-radius: 15; " +              // Rounded corners for border
                           "-fx-background-radius: 15; " +          // Rounded corners for background
                           "-fx-padding: 5px; " +                   // Padding inside the field
                           "-fx-font-size: 16px;");                 // Font size
    }
    
    
    

    public static void main(String[] args) {
        launch(args);

    }
}
