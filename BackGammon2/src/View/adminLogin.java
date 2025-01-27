package View;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class adminLogin extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Background Image with GaussianBlur
        Image backgroundImage = new Image(getClass().getResourceAsStream("backgammon2.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(400);
        backgroundImageView.setFitHeight(300);
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setEffect(new GaussianBlur(50));

        // Create UI components
        Label titleLabel = new Label("Admin Login");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-size: 14px; -fx-pref-width: 150; -fx-background-radius: 10;");

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

        VBox loginLayout = new VBox(10, titleLabel, usernameLabel, usernameField, passwordLabel, passwordField, loginButton, statusLabel);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setStyle("-fx-padding: 20; -fx-background-color: rgba(0, 0, 0, 0.5);");

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, loginLayout);

        Scene scene = new Scene(root, 400, 300);
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
