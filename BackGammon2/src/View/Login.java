package View;

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
        StackPane root = new StackPane();

        // Background image with Gaussian blur
        Image backgroundImage = new Image(getClass().getResourceAsStream("backgammon2.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(1000);
        backgroundImageView.setFitHeight(700);
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setEffect(new GaussianBlur(50));

        VBox loginLayout = new VBox(15);
        loginLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label titleLabel = new Label("Welcome to CATGammon!");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 45));
        titleLabel.setStyle("-fx-text-fill: white;");
        titleLabel.setEffect(new DropShadow(10, Color.BLACK));

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

        // Button styles
        String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 18px; " +
                             "-fx-background-radius: 20; -fx-font-weight: bold; -fx-pref-width: 200; -fx-pref-height: 40;";
        String hoverStyle = "-fx-background-color: #45a049;";

        Button loginButton = new Button("Login");
        loginButton.setStyle(buttonStyle);
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(buttonStyle + hoverStyle));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(buttonStyle));
        loginButton.setOnAction(event -> {
            player1 = player1Field.getText();
            player2 = player2Field.getText();

            if (validateCredentials(player1, player2)) {
                GameLevelScreen levelSelectionScreen = new GameLevelScreen();
                levelSelectionScreen.start(primaryStage);
            } else {
                errorLabel.setText("Invalid username or password. Please try again!");
            }
        });

        Button historyButton = new Button("History");
        historyButton.setStyle(buttonStyle);
        historyButton.setOnMouseEntered(e -> historyButton.setStyle(buttonStyle + hoverStyle));
        historyButton.setOnMouseExited(e -> historyButton.setStyle(buttonStyle));
        historyButton.setOnAction(event -> {
            HistoryScreen historyScreen = new HistoryScreen();
            historyScreen.start(primaryStage);
        });

        // Instructions button
        Button instructionsButton = new Button("Instructions");
        instructionsButton.setStyle(buttonStyle);
        instructionsButton.setOnMouseEntered(e -> instructionsButton.setStyle(buttonStyle + hoverStyle));
        instructionsButton.setOnMouseExited(e -> instructionsButton.setStyle(buttonStyle));
        instructionsButton.setOnAction(event -> {
            BackgammonInstructions instructionsScreen = new BackgammonInstructions();
            instructionsScreen.start(primaryStage);
        });

        // Manage Questions button
        Button manageQuestionsButton = new Button("Manage Questions");
        manageQuestionsButton.setStyle(buttonStyle);
        manageQuestionsButton.setOnMouseEntered(e -> manageQuestionsButton.setStyle(buttonStyle + hoverStyle));
        manageQuestionsButton.setOnMouseExited(e -> manageQuestionsButton.setStyle(buttonStyle));
        manageQuestionsButton.setOnAction(event -> {
            manageQuestions manageQuestionsScreen = new manageQuestions();
            manageQuestionsScreen.start(primaryStage);
        });

        loginLayout.getChildren().addAll(
                titleLabel, player1Label, player1Field, player2Label, player2Field,
                loginButton, errorLabel, historyButton, instructionsButton, manageQuestionsButton);

        root.getChildren().addAll(backgroundImageView, loginLayout);

        Scene loginScene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private boolean validateCredentials(String player1, String player2) {
        return player1 != null && !player1.trim().isEmpty() && player2 != null && !player2.trim().isEmpty();
    }

    private void styleLabel(Label label) {
        label.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 22));
        label.setStyle("-fx-text-fill: white; -fx-padding: 5px;");
    }

    private void styleTextField(TextField textField) {
        textField.setMaxWidth(300);
        textField.setPrefHeight(30);
        textField.setStyle("-fx-border-color: #4CAF50; -fx-border-radius: 15; -fx-background-radius: 15; " +
                           "-fx-padding: 5px; -fx-font-size: 16px;");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
