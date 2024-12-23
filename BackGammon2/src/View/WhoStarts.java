package View;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Random;

public class WhoStarts {

    private final Random random = new Random();
    public static boolean startingPlayer;

    public boolean determineStartingPlayer(Stage owner) {
        Stage whoStartsStage = new Stage();
        whoStartsStage.initOwner(owner);
        whoStartsStage.initModality(Modality.APPLICATION_MODAL);

        // Background Image with GaussianBlur
        Image backgroundImage = new Image(getClass().getResourceAsStream("backgammon2.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(400);
        backgroundImageView.setFitHeight(300);
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setEffect(new GaussianBlur(50));

        // Create UI components
        Label titleLabel = new Label("Who Starts?");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label resultLabel = new Label("Roll the dice to see who starts!");
        resultLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        // Buttons for rolling the dice
        Button rollButton = new Button("Roll Dice");
        rollButton.setStyle("-fx-font-size: 14px; -fx-pref-width: 150; -fx-background-radius: 10;");

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-font-size: 14px; -fx-pref-width: 150; -fx-background-radius: 10;");
        closeButton.setVisible(false); // Initially hidden, will be shown after rolls
        closeButton.setOnAction(event -> whoStartsStage.close()); // Closes the window

        rollButton.setOnAction(event -> {
            // Generate dice rolls
            int player1Roll, player2Roll;

            do {
                player1Roll = random.nextInt(6) + 1;
                player2Roll = random.nextInt(6) + 1;

                // Update result label to show dice rolls
                String player1 = Login.player1;
                String player2 = Login.player2;
                String resultMessage = String.format(player1 + " rolled: %d\n" + player2 +" rolled: %d\n", player1Roll, player2Roll);
               
                if (player1Roll > player2Roll) {
                    resultMessage += player1 + " starts!";
                    startingPlayer = true;
                } else if (player2Roll > player1Roll) {
                    resultMessage += player2 + " starts!";
                    startingPlayer = false;
                } else {
                    resultMessage += "It's a tie! Roll again.";
                }

                resultLabel.setText(resultMessage);

            } while (player1Roll == player2Roll); // Keep rolling until there's no tie

            // Show the "Close" button after determining the winner
            closeButton.setVisible(true);
        });

        VBox rootLayout = new VBox(20, titleLabel, resultLabel, rollButton, closeButton);
        rootLayout.setAlignment(Pos.CENTER);
        rootLayout.setStyle("-fx-padding: 20; -fx-background-color: rgba(0, 0, 0, 0.5);");

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, rootLayout);

        Scene scene = new Scene(root, 400, 300);
        whoStartsStage.setTitle("Who Starts?");
        whoStartsStage.setScene(scene);
        whoStartsStage.showAndWait();

        return startingPlayer;
    }
}