package View;

import Model.GameRecord;
import Model.SysData;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HistoryScreen extends Application {

    private final SysData sysData = SysData.getInstance();

    @Override
    public void start(Stage primaryStage) {
        // Background setup
        StackPane root = new StackPane();
        Image backgroundImage = new Image(getClass().getResourceAsStream("backgammon2.png"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(1000);
        backgroundImageView.setFitHeight(700);
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setEffect(new GaussianBlur(50));

        // Title with shadow effect
        Label title = new Label("Game History");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        title.setTextFill(Color.WHITE);
        title.setEffect(new DropShadow(5, Color.BLACK));
        title.setPadding(new Insets(10, 0, 20, 0));

     // TableView for game history
        TableView<GameRecord> historyTable = new TableView<>();
        historyTable.setPrefHeight(400);
        historyTable.setStyle("-fx-background-color: white; -fx-border-radius: 10;");

        // Table columns
        TableColumn<GameRecord, String> player1Col = new TableColumn<>("Player 1");
        player1Col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPlayer1()));
        player1Col.setMinWidth(150);
        player1Col.setStyle("-fx-alignment: CENTER; -fx-font-size: 16px;");

        TableColumn<GameRecord, String> player2Col = new TableColumn<>("Player 2");
        player2Col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPlayer2()));
        player2Col.setMinWidth(150);
        player2Col.setStyle("-fx-alignment: CENTER; -fx-font-size: 16px;");

        TableColumn<GameRecord, String> winnerCol = new TableColumn<>("Winner");
        winnerCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getWinner()));
        winnerCol.setMinWidth(150);
        winnerCol.setStyle("-fx-alignment: CENTER; -fx-font-size: 16px;");

        TableColumn<GameRecord, String> difficultyCol = new TableColumn<>("Difficulty");
        difficultyCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDifficulty()));
        difficultyCol.setMinWidth(150);
        difficultyCol.setStyle("-fx-alignment: CENTER; -fx-font-size: 16px;");

        TableColumn<GameRecord, String> durationCol = new TableColumn<>("Duration");
        durationCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDuration()));
        durationCol.setMinWidth(150);
        durationCol.setStyle("-fx-alignment: CENTER; -fx-font-size: 16px;");

        // Add columns to the table
        historyTable.getColumns().addAll(player1Col, player2Col, winnerCol, difficultyCol, durationCol);

        // Prevent the empty column from showing up
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Populate table with history data
        ObservableList<GameRecord> historyData = FXCollections.observableArrayList(sysData.getHistory());
        historyTable.setItems(historyData);


        // Back button
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        backButton.setStyle(
                "-fx-background-color: linear-gradient(#6a11cb, #2575fc);" +
                "-fx-text-fill: white; -fx-background-radius: 20; -fx-pref-width: 120;");
        backButton.setOnAction(event -> {
            Login loginScreen = new Login();
            loginScreen.start(primaryStage);
        });

        // Layout setup
        BorderPane layout = new BorderPane();
        layout.setTop(title);
        layout.setCenter(historyTable);
        layout.setBottom(backButton);
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(backButton, Pos.CENTER);
        BorderPane.setMargin(backButton, new Insets(20, 0, 20, 0));

        root.getChildren().addAll(backgroundImageView, layout);

        // Scene setup
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Game History");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
