package application;

import javafx.scene.control.Label;

class DiceObserver implements EventListener {
    private Label diceLabelOne;
    private Label diceLabelTwo;

    public DiceObserver(Label diceLabelOne, Label diceLabelTwo) {
        this.diceLabelOne = diceLabelOne;
        this.diceLabelTwo = diceLabelTwo;
    }

    @Override
    public void update(String eventType, Object data) {
        if ("diceRolled".equals(eventType) && data instanceof int[]) {
            int[] diceValues = (int[]) data;
            diceLabelOne.setText(String.valueOf(diceValues[0]));
            diceLabelTwo.setText(String.valueOf(diceValues[1]));
        }
    }
}