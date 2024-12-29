package View;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.effect.GaussianBlur;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SurprisePopUp {

    public void show(Stage ownerStage) {
        // Create a new stage for the popup
        Stage popupStage = new Stage();

        // Set the popup properties
        popupStage.initOwner(ownerStage);
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.setTitle("Surprise!");

        // Background setup
        StackPane root = new StackPane();
        Image backgroundImage = new Image(getClass().getResourceAsStream("backgammon2.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(400);
        backgroundImageView.setFitHeight(200);
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setEffect(new GaussianBlur(50));

        // Text for the popup message
        Text message = new Text("Surprise! Another turn");
        message.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        message.setStyle("-fx-fill: white; -fx-effect: dropshadow(gaussian, black, 5, 0, 0, 0);");

        // Close button
        Button closeButton = new Button("OK");
        closeButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        closeButton.setStyle(
                "-fx-background-color: linear-gradient(#6a11cb, #2575fc);" +
                "-fx-text-fill: white; -fx-background-radius: 20; -fx-pref-width: 100;" +
                "-fx-pref-height: 30;");
        closeButton.setOnAction(e -> popupStage.close());

        // Layout setup
        BorderPane layout = new BorderPane();
        layout.setCenter(message);
        layout.setBottom(closeButton);
        BorderPane.setAlignment(message, Pos.CENTER);
        BorderPane.setAlignment(closeButton, Pos.CENTER);
        BorderPane.setMargin(closeButton, new Insets(10, 0, 10, 0));

        root.getChildren().addAll(backgroundImageView, layout);

        // Scene setup
        Scene scene = new Scene(root, 400, 200);

        // Show the popup
        popupStage.setScene(scene);
        popupStage.show();
    }
}
