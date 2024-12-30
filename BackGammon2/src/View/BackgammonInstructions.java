package View;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BackgammonInstructions extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Background setup
        StackPane root = new StackPane();
        Image backgroundImage = new Image(getClass().getResourceAsStream("backgammon2.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(1000);
        backgroundImageView.setFitHeight(700);
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setEffect(new GaussianBlur(30));

        // Title with shadow effect
        Text title = new Text("Game Instructions - Backgammon");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 36));
        title.setFill(Color.WHITE);
        title.setEffect(new DropShadow(10, Color.BLACK));

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
                        + "Good luck, and enjoy the game!");
        instructionsArea.setWrapText(true);
        instructionsArea.setEditable(false);
        instructionsArea.setFont(Font.font("Verdana", 18));
        instructionsArea.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-border-radius: 15; -fx-padding: 15; -fx-border-color: #4CAF50; -fx-border-width: 2;");

        // Scroll Pane for Instructions
        ScrollPane scrollPane = new ScrollPane(instructionsArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-padding: 10;");
        scrollPane.setMaxHeight(500);

        // Back button
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        backButton.setStyle(
                "-fx-background-color: linear-gradient(#ff7e5f, #feb47b);" +
                        "-fx-text-fill: white; -fx-background-radius: 30; -fx-pref-width: 150; -fx-pref-height: 40;");
        backButton.setOnAction(event -> {
            Login loginScreen = new Login();
            loginScreen.start(primaryStage);
        });

        // Layout setup
        VBox contentBox = new VBox(20, title, scrollPane, backButton);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(20));

        root.getChildren().addAll(backgroundImageView, contentBox);

        // Scene setup
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Game Instructions - Backgammon");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}