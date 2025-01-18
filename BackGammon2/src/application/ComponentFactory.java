package application;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

class ComponentFactory {

    // Creates a Label with the given text.
    public static Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");
        return label;
    }
    
    // Creates a Button with the given text.
    public static Button createButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: lightblue; -fx-font-size: 14px;");
        return button;
    }
}