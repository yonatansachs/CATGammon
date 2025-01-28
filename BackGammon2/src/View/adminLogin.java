package View;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class adminLogin extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Background Image with GaussianBlur
        Image backgroundImage = new Image(getClass().getResourceAsStream("backgammon2.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(600);
        backgroundImageView.setFitHeight(400);
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setEffect(new GaussianBlur(50));

        // Semi-transparent overlay for contrast
        Rectangle overlay = new Rectangle(600, 400);
        overlay.setFill(Color.rgb(0, 0, 0, 0.6));

        // Title
        Label titleLabel = new Label("Admin Login");
        titleLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: white;");
        titleLabel.setEffect(new DropShadow(5, Color.BLACK));

        // Username Label and Field
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setStyle("-fx-font-size: 14px; -fx-background-radius: 10; -fx-padding: 5;");

        // Password Label and Field
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle("-fx-font-size: 14px; -fx-background-radius: 10; -fx-padding: 5;");

        // Login Button
        Button loginButton = new Button("Login");
        loginButton.setStyle(
                "-fx-font-size: 16px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10;");
        loginButton.setPrefWidth(150);

        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle(
                "-fx-font-size: 16px; -fx-background-color: #FF6347; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10;");
        backButton.setPrefWidth(150);
        backButton.setOnAction(event -> {
            Login loginScreen = new Login();
            loginScreen.start(primaryStage);
        });

        // Status Label
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: red;");

        // Login Button Action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if ("admin".equals(username) && "admin".equals(password)) {
                statusLabel.setText("Login successful!");
                statusLabel.setStyle("-fx-text-fill: green;");
                openManageQuestionsScreen(primaryStage);
            } else {
                statusLabel.setText("Invalid username or password!");
            }
        });

        // Layout
        VBox loginLayout = new VBox(15, titleLabel, usernameLabel, usernameField, passwordLabel, passwordField, loginButton, backButton, statusLabel);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setStyle("-fx-padding: 20;");
        loginLayout.setMaxWidth(350);

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, overlay, loginLayout);

        // Scene
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Admin Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Open Manage Questions Screen
    private void openManageQuestionsScreen(Stage primaryStage) {
        manageQuestions manageQuestionsScreen = new manageQuestions();
        try {
            manageQuestionsScreen.start(primaryStage); // Navigate to manageQuestions screen
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
