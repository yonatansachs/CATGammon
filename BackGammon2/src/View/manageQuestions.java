package View;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import Model.Question;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class manageQuestions extends Application {
    private List<Question> questions;
    private ComboBox<String> editComboBox;
    private ComboBox<String> deleteComboBox;
    private TextField questionTextField;
    private TextField option1Field;
    private TextField option2Field;
    private TextField option3Field;
    private TextField option4Field;
    private ComboBox<Integer> correctAnswerIndexField;
    private ComboBox<String> difficultyField;

    // Define the correct path to questions.json (same as SysData)
    private final String QUESTIONS_FILE = "questions.json"; // Ensure this is the correct path

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        questions = loadQuestions();
        primaryStage.setTitle("Manage Questions");
        VBox root = createMainLayout(primaryStage);
        Scene scene = new Scene(root, 600, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates the main layout containing all sections.
     */
    private VBox createMainLayout(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        GridPane addGrid = createAddQuestionGrid();
        VBox editBox = createEditSection();
        VBox deleteBox = createDeleteSection();
        VBox controlButtonsSection = createControlButtonsSection(primaryStage);

        root.getChildren().addAll(
                new Label("Add Question"),
                addGrid,
                editBox,
                deleteBox,
                controlButtonsSection
        );

        updateComboBoxes();
        return root;
    }

    /**
     * Creates the "Clear Fields" and "Back" buttons.
     */
    private VBox createControlButtonsSection(Stage primaryStage) {
        Button clearFieldsButton = new Button("Clear Fields");
        clearFieldsButton.setOnAction(event -> clearFields());

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            
            Login loginScreen = new Login();
            loginScreen.start(primaryStage);
            //primaryStage.close(); // Placeholder action
        });

        // Optional styling
        clearFieldsButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        backButton.setStyle("-fx-background-color: #FF6347; -fx-text-fill: white;");

        VBox buttonSection = new VBox(10, clearFieldsButton, backButton);
        buttonSection.setPadding(new Insets(10));
        buttonSection.setAlignment(Pos.CENTER);

        return buttonSection;
    }

    /**
     * Creates the grid for adding a new question.
     */
    private GridPane createAddQuestionGrid() {
        GridPane addGrid = new GridPane();
        addGrid.setHgap(10);
        addGrid.setVgap(10);

        questionTextField = new TextField();
        option1Field = new TextField();
        option2Field = new TextField();
        option3Field = new TextField();
        option4Field = new TextField();
        correctAnswerIndexField = new ComboBox<>();
        correctAnswerIndexField.getItems().addAll(0, 1, 2, 3);

        difficultyField = new ComboBox<>();
        difficultyField.getItems().addAll("Easy", "Medium", "Hard");

        addGrid.add(new Label("Question Text:"), 0, 0);
        addGrid.add(questionTextField, 1, 0);
        addGrid.add(new Label("Option 1:"), 0, 1);
        addGrid.add(option1Field, 1, 1);
        addGrid.add(new Label("Option 2:"), 0, 2);
        addGrid.add(option2Field, 1, 2);
        addGrid.add(new Label("Option 3:"), 0, 3);
        addGrid.add(option3Field, 1, 3);
        addGrid.add(new Label("Option 4:"), 0, 4);
        addGrid.add(option4Field, 1, 4);
        addGrid.add(new Label("Correct Answer Index (0-3):"), 0, 5);
        addGrid.add(correctAnswerIndexField, 1, 5);
        addGrid.add(new Label("Difficulty:"), 0, 6);
        addGrid.add(difficultyField, 1, 6);

        Button addButton = new Button("Add Question");
        addButton.setOnAction(e -> addQuestion());
        addGrid.add(addButton, 0, 7, 2, 1);

        return addGrid;
    }

    /**
     * Creates the section for editing an existing question.
     */
    private VBox createEditSection() {
        Label editLabel = new Label("Edit Question");
        editComboBox = new ComboBox<>();
        editComboBox.setOnAction(e -> loadQuestionForEdit());

        Button editButton = new Button("Edit Question");
        editButton.setOnAction(e -> editQuestion());

        return new VBox(10, editLabel, editComboBox, editButton);
    }

    /**
     * Creates the section for deleting an existing question.
     */
    private VBox createDeleteSection() {
        Label deleteLabel = new Label("Delete Question");
        deleteComboBox = new ComboBox<>();
        Button deleteButton = new Button("Delete Question");
        deleteButton.setOnAction(e -> deleteQuestion());

        return new VBox(10, deleteLabel, deleteComboBox, deleteButton);
    }

    /**
     * Loads questions from the JSON file using manual parsing.
     */
    private List<Question> loadQuestions() {
        List<Question> questionList = new ArrayList<>();
        File file = new File(QUESTIONS_FILE);
        if (!file.exists()) {
            System.err.println("Questions file not found: " + QUESTIONS_FILE);
            return questionList;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            String json = jsonContent.toString();
            questionList = parseQuestions(json);
        } catch (IOException e) {
            System.err.println("Failed to load questions: " + e.getMessage());
        }

        return questionList;
    }

    /**
     * Parses the JSON string to extract Question objects manually.
     */
    private List<Question> parseQuestions(String json) {
        List<Question> questionList = new ArrayList<>();
        if (json == null || json.isEmpty()) return questionList;

        try {
            // Split the JSON string to extract question objects
            String[] questionBlocks = json.split("\\{");

            for (String block : questionBlocks) {
                if (block.contains("questionText")) {
                    // Extract questionText
                    String questionText = extractValue(block, "questionText");

                    // Extract options array
                    String optionsBlock = block.split("\"options\":\\s*\\[")[1].split("\\]")[0];
                    String[] options = optionsBlock.split("\",");
                    for (int i = 0; i < options.length; i++) {
                        options[i] = options[i].replace("\"", "").trim();
                    }

                    // Extract correctAnswerIndex
                    String correctAnswerIndexStr = extractValue(block, "correctAnswerIndex");
                    int correctAnswerIndex = 0;
                    try {
                        correctAnswerIndex = Integer.parseInt(correctAnswerIndexStr);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid correctAnswerIndex: " + correctAnswerIndexStr);
                        continue; // Skip this question
                    }

                    // Extract difficulty
                    String difficulty = extractValue(block, "difficulty");

                    // Add question to the list
                    questionList.add(new Question(questionText, options, correctAnswerIndex, difficulty));
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing questions: " + e.getMessage());
        }

        return questionList;
    }

    /**
     * Extracts a string or numeric value for a given key from a JSON object block using split.
     */
    private String extractValue(String block, String key) {
        try {
            String[] parts = block.split("\"" + key + "\"\\s*:\\s*");
            if (parts.length < 2) return null;
            String valuePart = parts[1];
            // Now, valuePart starts with either a quote or a number
            String value;
            if (valuePart.startsWith("\"")) {
                // String value
                int endQuoteIndex = valuePart.indexOf("\"", 1);
                if (endQuoteIndex == -1) return null;
                value = valuePart.substring(1, endQuoteIndex);
            } else {
                // Numeric value
                int commaIndex = valuePart.indexOf(",");
                if (commaIndex == -1) {
                    // Last field, maybe closing brace
                    int endBraceIndex = valuePart.indexOf("}");
                    if (endBraceIndex == -1) {
                        value = valuePart.trim();
                    } else {
                        value = valuePart.substring(0, endBraceIndex).trim();
                    }
                } else {
                    value = valuePart.substring(0, commaIndex).trim();
                }
            }
            return value;
        } catch (Exception e) {
            System.err.println("Failed to extract value for key " + key + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Saves the current list of questions to the JSON file using manual serialization.
     */
    private void saveQuestions() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(QUESTIONS_FILE))) {
            writer.write(questionsToJson());
            showAlert(Alert.AlertType.INFORMATION, "Success", "Questions saved successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save questions: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save questions.");
        }
    }

    /**
     * Converts the list of questions to a JSON string manually.
     */
    private String questionsToJson() {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            json.append("  {\n")
                .append("    \"questionText\": \"").append(escapeJson(q.getQuestionText())).append("\",\n")
                .append("    \"options\": [\n")
                .append("      \"").append(escapeJson(q.getOptions()[0])).append("\",\n")
                .append("      \"").append(escapeJson(q.getOptions()[1])).append("\",\n")
                .append("      \"").append(escapeJson(q.getOptions()[2])).append("\",\n")
                .append("      \"").append(escapeJson(q.getOptions()[3])).append("\"\n")
                .append("    ],\n")
                .append("    \"correctAnswerIndex\": ").append(q.getCorrectAnswerIndex()).append(",\n")
                .append("    \"difficulty\": \"").append(escapeJson(q.getDifficulty())).append("\"\n")
                .append("  }")
                .append(i < questions.size() - 1 ? ",\n" : "\n");
        }
        json.append("]");
        return json.toString();
    }

    /**
     * Escapes special characters in JSON strings.
     */
    private String escapeJson(String str) {
        return str.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }

    /**
     * Adds a new question based on the form fields.
     */
    private void addQuestion() {
        if (!validateFields()) return;

        try {
            int index = correctAnswerIndexField.getValue();
            if (index < 0 || index > 3) throw new NumberFormatException();

            // Create a new Question object and add to the list
            Question newQuestion = new Question(
                    questionTextField.getText(),
                    new String[]{
                            option1Field.getText(),
                            option2Field.getText(),
                            option3Field.getText(),
                            option4Field.getText()
                    },
                    index,
                    difficultyField.getValue()
            );

            questions.add(newQuestion);
            saveQuestions();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Question added successfully.");
            updateComboBoxes();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Correct Answer Index must be a number between 0 and 3.");
        }
    }

    /**
     * Validates that all form fields are filled.
     */
    private boolean validateFields() {
        if (questionTextField.getText().isEmpty()
                || option1Field.getText().isEmpty()
                || option2Field.getText().isEmpty()
                || option3Field.getText().isEmpty()
                || option4Field.getText().isEmpty()
                || correctAnswerIndexField.getValue() == null
                || difficultyField.getValue() == null) {

            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled.");
            return false;
        }
        return true;
    }

    /**
     * Loads a selected question's details into the form for editing.
     */
    private void loadQuestionForEdit() {
        String selected = editComboBox.getValue();
        if (selected == null) return;

        for (Question q : questions) {
            if (q.getQuestionText().equals(selected)) {
                questionTextField.setText(q.getQuestionText());
                option1Field.setText(q.getOptions()[0]);
                option2Field.setText(q.getOptions()[1]);
                option3Field.setText(q.getOptions()[2]);
                option4Field.setText(q.getOptions()[3]);
                correctAnswerIndexField.setValue(q.getCorrectAnswerIndex());
                difficultyField.setValue(q.getDifficulty());
                break;
            }
        }
    }

    /**
     * Edits the selected question with the current form values.
     */
    private void editQuestion() {
        String selected = editComboBox.getValue();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No question selected.");
            return;
        }

        if (!validateFields()) return;

        try {
            int index = correctAnswerIndexField.getValue();
            if (index < 0 || index > 3) throw new NumberFormatException();

            for (Question q : questions) {
                if (q.getQuestionText().equals(selected)) {
                    q.setQuestionText(questionTextField.getText());
                    q.setOptions(new String[]{
                            option1Field.getText(),
                            option2Field.getText(),
                            option3Field.getText(),
                            option4Field.getText()
                    });
                    q.setCorrectAnswerIndex(index);
                    q.setDifficulty(difficultyField.getValue());
                    break;
                }
            }

            saveQuestions();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Question edited successfully.");
            updateComboBoxes();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Correct Answer Index must be a number between 0 and 3.");
        }
    }

    /**
     * Deletes the selected question from the list.
     */
    private void deleteQuestion() {
        String selected = deleteComboBox.getValue();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No question selected.");
            return;
        }

        boolean removed = questions.removeIf(q -> q.getQuestionText().equals(selected));
        if (removed) {
            saveQuestions();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Question deleted successfully.");
            updateComboBoxes();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the question.");
        }
    }

    /**
     * Updates the edit and delete combo boxes with the current list of questions.
     */
    private void updateComboBoxes() {
        editComboBox.getItems().clear();
        deleteComboBox.getItems().clear();
        for (Question q : questions) {
            editComboBox.getItems().add(q.getQuestionText());
            deleteComboBox.getItems().add(q.getQuestionText());
        }
    }

    /**
     * Clears all form fields.
     */
    private void clearFields() {
        questionTextField.clear();
        option1Field.clear();
        option2Field.clear();
        option3Field.clear();
        option4Field.clear();
        correctAnswerIndexField.setValue(null);
        difficultyField.setValue(null);
    }

    /**
     * Displays an alert dialog.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional: Remove header
        alert.setContentText(message);
        alert.showAndWait();
    }
}
