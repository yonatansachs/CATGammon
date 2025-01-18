package application;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class BoardBuilder {
    private Pane boardPane;
    private GridPane[] gridCols;
    private Color boardColor = Color.BEIGE;
    private int gridSize = 24;

    public BoardBuilder() {
        boardPane = new Pane();
        gridCols = new GridPane[gridSize];
    }

    public BoardBuilder setBoardColor(Color color) {
        this.boardColor = color;
        return this;
    }

    public BoardBuilder setGridSize(int size) {
        this.gridSize = size;
        gridCols = new GridPane[size];
        return this;
    }

    public BoardBuilder addBackground() {
        Rectangle background = new Rectangle(1100, 800, boardColor);
        boardPane.getChildren().add(background);
        return this;
    }

    public BoardBuilder buildGrid() {
        for (int i = 0; i < gridSize / 2; i++) {
            GridPane up = new GridPane();
            up.setLayoutX(10 + i * 45);
            up.setLayoutY(10);
            gridCols[i] = up;
            boardPane.getChildren().add(up);
        }
        for (int i = gridSize / 2; i < gridSize; i++) {
            GridPane down = new GridPane();
            down.setLayoutX(10 + (i - gridSize / 2) * 45);
            down.setLayoutY(700);
            gridCols[i] = down;
            boardPane.getChildren().add(down);
        }
        return this;
    }

    public Pane build() {
        return boardPane;
    }

	public GridPane[] getGridColumns() {
		return gridCols;
	}
	
    public BoardBuilder addTimerLabel(Label timerLabel) {
        timerLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18;");
        timerLabel.setLayoutX(950);
        timerLabel.setLayoutY(0);
        boardPane.getChildren().add(timerLabel);
        return this;
    }
    
    public BoardBuilder addDiceLabels(Label one, Label two) {
        one.setStyle("-fx-text-fill: white; -fx-font-size: 72;");
        one.setLayoutX(528);
        one.setLayoutY(360);

        two.setStyle("-fx-text-fill: white; -fx-font-size: 72;");
        two.setLayoutX(528);
        two.setLayoutY(290);

        boardPane.getChildren().addAll(one, two);
        return this;
    }

	public BoardBuilder addRollDiceButton(Button rollDiceButton) {
		    rollDiceButton.setText("Roll Dice");
		    rollDiceButton.setOnAction(e -> Backgammon.rollDice(Backgammon.difficulty));
		    rollDiceButton.setLayoutX(500); // Adjust X position
		    rollDiceButton.setLayoutY(350); // Adjust Y position
		    boardPane.getChildren().add(rollDiceButton);
		    return this;
	}
}
