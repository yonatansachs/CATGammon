package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HistoryScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a StackPane for the layout to layer the background and history content
        StackPane root = new StackPane();

        // Add the background image with GaussianBlur
        Image backgroundImage = new Image(getClass().getResourceAsStream("backgammon2.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(1000); // Adjust to your scene width
        backgroundImageView.setFitHeight(700); // Adjust to your scene height
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setEffect(new GaussianBlur(50)); // Apply Gaussian blur effect

        // Create a VBox for the history layout
        VBox historyLayout = new VBox(20);
        historyLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Add title and content to the history screen
        Label historyTitle = new Label("Game History");
        historyTitle.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: white;");

        Label historyContent = new Label("Game history will be displayed here.");
        historyContent.setStyle("-fx-font-size: 18; -fx-text-fill: white;");

        // Add a "Back" button to return to the login screen
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 18; -fx-pref-width: 200; -fx-pref-height: 50;");
        backButton.setOnAction(event -> {
            Login loginScreen = new Login();
            loginScreen.start(primaryStage); // Go back to the login screen
        });

        // Add all elements to the VBox
        historyLayout.getChildren().addAll(historyTitle, historyContent, backButton);

        // Add the background and history layout to the root
        root.getChildren().addAll(backgroundImageView, historyLayout);

        // Create and set the scene
        Scene historyScene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Game History");
        primaryStage.setScene(historyScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
