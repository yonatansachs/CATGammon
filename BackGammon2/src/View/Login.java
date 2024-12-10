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
        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 25)); // Bold and larger font
        usernameLabel.setStyle("-fx-text-fill: white;"); // Set text color to white

        
        TextField usernameField = new TextField();
        usernameField.setMaxWidth(300); // Set maximum width explicitly
        usernameField.setPrefHeight(30); // Set height explicitly

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 25)); // Bold and larger font
        passwordLabel.setStyle("-fx-text-fill: white;"); // Set text color to white
 
        
        PasswordField passwordField = new PasswordField();
        passwordField.setMaxWidth(300); // Set maximum width explicitly
        passwordField.setPrefHeight(30); // Set height explicitly

        Button loginButton = new Button("Login");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (validateCredentials(username, password)) {
                // Transition to Backgammon game
                Backgammon game = new Backgammon();
                game.start(primaryStage); // Call the Backgammon game
            } else {
                errorLabel.setText("Invalid username or password.");
            }
        });

        // Add all elements to the VBox
        loginLayout.getChildren().addAll(titleLabel,
                usernameLabel, usernameField,
                passwordLabel, passwordField,
                loginButton, errorLabel
        );

        // Add the background and login layout to the root
        root.getChildren().addAll(backgroundImageView, loginLayout);

        // Create and set the scene
        Scene loginScene = new Scene(root, 1000, 700); // Adjust size as needed
        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private boolean validateCredentials(String username, String password) {
        // Replace with real authentication logic
        return "user".equals(username) && "password".equals(password);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
