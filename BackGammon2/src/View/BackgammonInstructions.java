package View;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class BackgammonInstructions extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Title
        Text title = new Text("Game Instructions - Backgammon");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextAlignment(TextAlignment.CENTER);

        // Game Instructions
        String instructions = " Objective of the Game:\n"
                + "Each player must move all their pieces to their 'home' and remove them from the board before their opponent.\n"
                + "How to Play:\n"
                + "1. Players roll dice and move pieces according to the numbers rolled.\n"
                + "2. Players move in opposite directions:\n"
                + "   - Black moves counterclockwise.\n"
                + "   - White moves clockwise.\n"
                + "3. Players may land on special stations like Question or Surprise stations.\n"
                + "Special Stations:\n"
                + "   - Question Station (❓): Answer a question to proceed.\n"
                + "   - Surprise Station (!): Gain a special bonus.\n"
                + "Difficulty Levels:\n"
                + "   - Easy: Roll 2 standard dice.\n"
                + "   - Medium: Roll 2 standard dice and 1 question die.\n"
                + "   - Hard: Roll 2 enhanced dice and 1 question die.\n"
                + "End of the Game:\n"
                + "The first player to remove all their pieces from the board is declared the winner.\n"
                + "Good luck, and enjoy the game!";

        // Instructions Text Area
        TextArea instructionsArea = new TextArea(
                  " Objective of the Game:\n"
                + "Each player must move all their pieces to their 'home' and remove them from the board before their opponent.\n"
                + "\n"
                + "How to Play:\n"
                + "1. Players roll dice and move pieces according to the numbers rolled.\n"
                + "2. Players move in opposite directions:\n"
                + "   - Black moves counterclockwise.\n"
                + "   - White moves clockwise.\n"
                + "\n"
                + "3. Players may land on special stations like Question or Surprise stations.\n"
                + "\n"
                + "Special Stations:\n"
                + "   - Question Station (❓): Answer a question to proceed.\n"
                + "   - Surprise Station (!): Gain a special bonus.\n"
                + "\n"
                + "Difficulty Levels:\n"
                + "   - Easy: Roll 2 standard dice.\n"
                + "   - Medium: Roll 2 standard dice and 1 question die.\n"
                + "   - Hard: Roll 2 enhanced dice and 1 question die.\n"
                + "\n"
                + "End of the Game:\n"
                + "The first player to remove all their pieces from the board is declared the winner.\n"
                + "\n"
                + "Good luck, and enjoy the game!"
        );
        instructionsArea.setWrapText(true);
        instructionsArea.setEditable(false);
        instructionsArea.setFont(Font.font("Arial", 14));
        instructionsArea.setPrefHeight(400);

        // Scroll Pane for Instructions
        ScrollPane scrollPane = new ScrollPane(instructionsArea);
        scrollPane.setFitToWidth(true);

        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 15;");
        backButton.setOnAction(e -> {
            Login loginScreen = new Login();
            loginScreen.start(primaryStage); // Return to the Login screen
        });

        // HBox for Back Button (align to center)
        HBox backButtonContainer = new HBox(backButton);
        backButtonContainer.setPadding(new Insets(10, 0, 0, 0));
        backButtonContainer.setStyle("-fx-alignment: center;");

        // Layout
        VBox root = new VBox(20, title, scrollPane, backButtonContainer);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Scene and Stage Setup
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setTitle("Game Instructions - Backgammon");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
