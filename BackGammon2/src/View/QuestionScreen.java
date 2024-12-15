package View;

import Model.Question;
import Model.SysData;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class QuestionScreen {

    private final SysData sysData = SysData.getInstance();

    public void show(Stage owner, String difficulty) {
        // Fetch a random question based on difficulty
        Question randomQuestion = sysData.getRandomQuestion(difficulty);
        if (randomQuestion == null) {
            showError("No questions available for the selected difficulty.");
            return;
        }

        Stage questionStage = new Stage();
        questionStage.initOwner(owner);
        questionStage.initModality(Modality.APPLICATION_MODAL);

        // Create the UI components
        Label questionLabel = new Label(randomQuestion.getQuestionText());
        questionLabel.setStyle("-fx-font-size: 16px; -fx-wrap-text: true; -fx-text-fill: black;");
        questionLabel.setAlignment(Pos.CENTER);
        questionLabel.setWrapText(true);

        VBox optionsBox = new VBox(10);
        optionsBox.setAlignment(Pos.CENTER);

        // Buttons for the answer options
        String[] options = randomQuestion.getOptions(); // Ensure all options are fetched correctly
        Button[] optionButtons = new Button[options.length];

        for (int i = 0; i < options.length; i++) {
            optionButtons[i] = new Button(options[i]);
            optionButtons[i].setStyle("-fx-font-size: 14px; -fx-pref-width: 250;");
            int selectedOption = i; // Capture the current index for the lambda

            optionButtons[i].setOnAction(event -> {
                if (selectedOption == randomQuestion.getCorrectAnswerIndex()) {
                    showCorrectMessage(questionStage); // Correct answer clicked
                } else {
                    showWrongMessage(); // Incorrect answer clicked
                }
            });
        }

        optionsBox.getChildren().addAll(optionButtons);

        // Layout
        VBox root = new VBox(20, questionLabel, optionsBox);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: lightblue; -fx-padding: 20;");

        // Scene
        Scene scene = new Scene(root, 400, 300);
        questionStage.setTitle("Answer the Question");
        questionStage.setScene(scene);
        questionStage.showAndWait();
    }

    private void showCorrectMessage(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Correct!");
        alert.setHeaderText(null);
        alert.setContentText("Congratulations! Your answer is correct.");
        alert.showAndWait();
        stage.close();
    }

    private void showWrongMessage() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Wrong!");
        alert.setHeaderText(null);
        alert.setContentText("Oops! That is not the correct answer. Try again!");
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


/*
package View;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class QuestionScreen {

    public void show(Stage owner) {
        Stage questionStage = new Stage();
        questionStage.initOwner(owner);
        questionStage.initModality(Modality.APPLICATION_MODAL);
        
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: lightblue;");

        Scene scene = new Scene(root, 300, 200);
        questionStage.setTitle("Question Screen");
        questionStage.setScene(scene);

        questionStage.showAndWait();
    }
}*/