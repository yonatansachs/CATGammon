package application;

import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import Control.GamePlay;
import Model.Pawns;
import View.Firstlayer;
import View.SecondLayer;

public class Backgammon extends Application {

    private String difficulty;

    @Override
    public void start(Stage primaryStage) {

        //-------------------STATEMENTS-------------------------------------
        Pane pane = new Pane();

        Firstlayer first = new Firstlayer(primaryStage);
        pane.getChildren().addAll(first.getBoard());

        GridPane[] gridCols = new GridPane[30];
        for (int i = 0; i < 12; i++) {
            SecondLayer up = new SecondLayer();
            gridCols[i] = up.setLayoutUp(i);
            pane.getChildren().add(gridCols[i]);
        }
        for (int i = 12; i < 24; i++) {
            SecondLayer down = new SecondLayer();
            gridCols[i] = down.setLayoutDown(i);
            pane.getChildren().add(gridCols[i]);
        }

        GamePlay theGame = new GamePlay(gridCols, primaryStage, difficulty);

        //-------------------BUTTON AND LABEL-------------------------------------
        Button dices = new Button("Roll Dice 🎲");
        dices.setLayoutX(10); // Positioned at the left edge
        dices.setLayoutY(5); 
        dices.setPrefSize(150, 30);
        dices.setStyle("-fx-background-color: #4CAF50; " +   // Green background
                       "-fx-text-fill: white; " +            // White text
                       "-fx-font-size: 14px; " +             // Font size
                       "-fx-background-radius: 10; " +      // Rounded corners
                       "-fx-font-weight: bold;");           // Bold font

        // Drop shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GRAY);
        shadow.setRadius(10);
        dices.setEffect(shadow);

        // Hover effect for button
        dices.setOnMouseEntered(e -> dices.setStyle("-fx-background-color: #45A049; " + 
                                                   "-fx-text-fill: white; " + 
                                                   "-fx-font-size: 14px; " +
                                                   "-fx-background-radius: 10; " +
                                                   "-fx-font-weight: bold;"));
        dices.setOnMouseExited(e -> dices.setStyle("-fx-background-color: #4CAF50; " + 
                                                  "-fx-text-fill: white; " + 
                                                  "-fx-font-size: 14px; " +
                                                  "-fx-background-radius: 10; " +
                                                  "-fx-font-weight: bold;"));

        Label one = new Label("?");
        Label two = new Label("?");
        one.setFont(Font.font(null, FontWeight.BOLD, 72));
        two.setFont(Font.font(null, FontWeight.BOLD, 72));

        one.setLayoutX(528);
        one.setLayoutY(360);
        one.setStyle("-fx-text-fill: white;");
        two.setLayoutX(528);
        two.setLayoutY(290);
        two.setStyle("-fx-text-fill: white;");

        dices.setOnAction(new EventHandler<ActionEvent>() {
            boolean player = true;

            @Override
            public void handle(ActionEvent event) {
                theGame.setTimes(2);
                Random rand = new Random();

                int diceOne = rand.nextInt(6) + 1;
                int diceTwo = rand.nextInt(6) + 1;

                one.setText(String.valueOf(diceOne));
                two.setText(String.valueOf(diceTwo));

                if (player) {
                    if (diceOne == diceTwo) theGame.setTimes(4);
                    player = false;
                    dices.setText("Black's Turn 🎲");
                    theGame.reset();
                    theGame.bluePlays(gridCols, diceOne, diceTwo);
                } else {
                    if (diceOne == diceTwo) theGame.setTimes(4);
                    player = true;
                    dices.setText("Blue's Turn 🎲");
                    theGame.reset();
                    theGame.blackPlays(gridCols, diceOne, diceTwo);
                }
            }
        });

        //-------------------SCENE AND STAGE-------------------------------------
        pane.getChildren().addAll(dices, one, two);

        Scene scene = new Scene(pane, 1100, 800); // Fixed screen size
        primaryStage.setTitle("Backgammon");
        primaryStage.setScene(scene);
        primaryStage.setMaxHeight(800);
        primaryStage.setMaxWidth(1100);
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(1100);
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setDifficulty(String selectedDifficulty) {
        this.difficulty = selectedDifficulty;
    }
}
