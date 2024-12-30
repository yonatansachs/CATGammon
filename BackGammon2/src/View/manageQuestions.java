package View;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import Model.Question;

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
    private ComboBox<String> difficultyField = new ComboBox<>();

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

    private VBox createMainLayout(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        
        GridPane addGrid = createAddQuestionGrid();
        VBox editBox = createEditSection(addGrid);
        VBox deleteBox = createDeleteSection();
        VBox controlButtonsSection = createControlButtonsSection(primaryStage);


        
        root.getChildren().addAll(new Label("Add Question"), addGrid, editBox, deleteBox,controlButtonsSection);
        updateComboBoxes();
        return root;
    }

    private VBox createControlButtonsSection(Stage primaryStage) {
        // Create the "Clear Fields" button
        Button clearFieldsButton = new Button("Clear Fields");
        clearFieldsButton.setOnAction(event -> clearFields());

        // Create the "Back" button
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            Login loginScreen = new Login(); // Replace with your Login class
            loginScreen.start(primaryStage); // Navigate back to the Login screen
        });

        // Style buttons (optional)
        clearFieldsButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        backButton.setStyle("-fx-background-color: #FF6347; -fx-text-fill: white;");

        // Add buttons to a VBox
        VBox buttonSection = new VBox(10, clearFieldsButton, backButton); // Spacing between buttons
        buttonSection.setPadding(new Insets(10));
        buttonSection.setAlignment(Pos.CENTER);

        return buttonSection;
    }
    private GridPane createAddQuestionGrid() {
        GridPane addGrid = new GridPane();
        addGrid.setHgap(10);
        addGrid.setVgap(10);

        questionTextField = new TextField();
        option1Field = new TextField();
        option2Field = new TextField();
        option3Field = new TextField();
        option4Field = new TextField();
        correctAnswerIndexField = new ComboBox();
        correctAnswerIndexField.getItems().add(0);
        correctAnswerIndexField.getItems().add(1);
        correctAnswerIndexField.getItems().add(2);
        correctAnswerIndexField.getItems().add(3);
        
        difficultyField.getItems().add("Easy");
        difficultyField.getItems().add("Medium");
        difficultyField.getItems().add("Hard");
        
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

    private VBox createEditSection(GridPane addGrid) {
        Label editLabel = new Label("Edit Question");
        editComboBox = new ComboBox<>();
        editComboBox.setOnAction(e -> loadQuestionForEdit());
        Button editButton = new Button("Edit Question");
        editButton.setOnAction(e -> editQuestion());
        return new VBox(10, editLabel, editComboBox, addGrid, editButton);
    }
    

    private VBox createDeleteSection() {
        Label deleteLabel = new Label("Delete Question");
        deleteComboBox = new ComboBox<>();
        Button deleteButton = new Button("Delete Question");
        deleteButton.setOnAction(e -> deleteQuestion());
        return new VBox(10, deleteLabel, deleteComboBox, deleteButton);
    }
    private List<Question> loadQuestions() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/View/questions.json"))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            return parseQuestions(json.toString());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private List<Question> parseQuestions(String json) {
        List<Question> questionList = new ArrayList<>();
        if (json == null || json.isEmpty()) return questionList;

        try {
            // Extract questions array
            int startIndex = json.indexOf("\"questions\":");
            if (startIndex == -1) return questionList;
            
            json = json.substring(json.indexOf("[", startIndex), json.lastIndexOf("]") + 1);
            
            // Split into individual question objects
            String[] entries = json.split("(?<=}),\\s*(?=\\{)");
            
            for (String entry : entries) {
                entry = entry.trim().replaceAll("^\\[?\\{|\\}\\]?$", "");
                
                // Extract values using more robust methods
                String questionText = extractJsonValue(entry, "questionText");
                String[] options = extractJsonArray(entry, "options");
                String correctAnswerStr = extractJsonValue(entry, "correctAnswerIndex");
                String difficulty = extractJsonValue(entry, "difficulty");
                
                // Parse correct answer index with validation
                int correctAnswerIndex = 0;
                if (correctAnswerStr != null && !correctAnswerStr.isEmpty()) {
                    correctAnswerIndex = Integer.parseInt(correctAnswerStr);
                }
                
                if (questionText != null && options != null && difficulty != null) {
                    questionList.add(new Question(questionText, options, correctAnswerIndex, difficulty));
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing questions: " + e.getMessage());
        }
        
        return questionList;
    }

    private String[] extractJsonArray(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\\[(.*?)\\]";
        java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern, java.util.regex.Pattern.DOTALL);
        java.util.regex.Matcher m = r.matcher(json);

        if (m.find()) {
            String arrayContent = m.group(1);
            String[] elements = arrayContent.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            String[] result = new String[4];

            for (int i = 0; i < 4 && i < elements.length; i++) {
                // Trim spaces, remove quotes, and unescape characters
                result[i] = elements[i].trim()
                                       .replaceAll("^\"|\"$", "") // Remove surrounding quotes
                                       .replace("\\", "");        // Remove backslashes
            }
            return result;
        }
        return null;
    }



    private String extractJsonValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\"?([^\"\\},]+)\"?";
        java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = r.matcher(json);
        return m.find() ? m.group(1).trim() : null;
    }
    
    private String[] parseOptions(String optionsStr) {
        String[] options = new String[4];
        if (optionsStr != null) {
            String[] optionParts = optionsStr.substring(optionsStr.indexOf("[") + 1, optionsStr.lastIndexOf("]"))
                                             .split("\\s*,\\s*");
            for (int i = 0; i < 4 && i < optionParts.length; i++) {
                // Remove surrounding quotes and unescape slashes
                options[i] = optionParts[i].replaceAll("^\"|\"$", "").replace("\\", "").trim();
            }
        }
        return options;
    }



    private String findPart(String[] parts, String key) {
        for (String part : parts) {
            if (part.contains("\"" + key + "\"")) {
                return part;
            }
        }
        return null;
    }

    private String extractValue(String part, String key) {
        if (part == null) return "";
        int startIndex = part.indexOf("\":") + 2;
        String value = part.substring(startIndex).trim();
        return value.replaceAll("^\"|\"$", "").trim();
    }

    private void saveQuestions() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/View/questions.json"))) {
            writer.write(questionsToJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String questionsToJson() {
        StringBuilder json = new StringBuilder("{\n  \"questions\": [\n");
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            json.append("    {\n")
                .append("      \"questionText\": \"").append(q.getQuestionText().replace("\"", "\\\"")).append("\",\n")
                .append("      \"options\": [\n");
            
            String[] options = q.getOptions();
            for (int j = 0; j < options.length; j++) {
                json.append("        \"").append(options[j].replace("\"", "\\\"")).append("\"")
                    .append(j < options.length - 1 ? ",\n" : "\n");
            }
            
            json.append("      ],\n")
                .append("      \"correctAnswerIndex\": ").append(q.getCorrectAnswerIndex()).append(",\n")
                .append("      \"difficulty\": \"").append(q.getDifficulty().replace("\"", "\\\"")).append("\"\n")
                .append("    }").append(i < questions.size() - 1 ? ",\n" : "\n");
        }
        json.append("  ]\n}");
        return json.toString();
    }

    private void addQuestion() {
        if (!validateFields()) return;
        
        try {
            int index = (int) correctAnswerIndexField.getValue();
            if (index < 0 || index > 3) throw new NumberFormatException();
            
            questions.add(new Question(
                questionTextField.getText(),
                new String[]{option1Field.getText(), option2Field.getText(), 
                            option3Field.getText(), option4Field.getText()},
                index,
                difficultyField.getValue().toString()
            ));
            
            saveQuestions();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Question added successfully.");
            updateComboBoxes();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Correct Answer Index must be a number between 0 and 3.");
        }
    }

    private boolean validateFields() {
        if (questionTextField.getText().isEmpty() || option1Field.getText().isEmpty() || 
            option2Field.getText().isEmpty() || option3Field.getText().isEmpty() || 
            option4Field.getText().isEmpty() || correctAnswerIndexField.getValue()==null || 
            difficultyField.getValue()==null) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled.");
            return false;
        }
        return true;
    }

    private void loadQuestionForEdit() {
        String selected = editComboBox.getValue();
        if (selected == null) return;

        questions.stream()
                .filter(q -> q.getQuestionText().equals(selected))
                .findFirst()
                .ifPresent(q -> {
                    questionTextField.setText(q.getQuestionText());
                    String[] options = q.getOptions();
                    option1Field.setText(options[0]);
                    option2Field.setText(options[1]);
                    option3Field.setText(options[2]);
                    option4Field.setText(options[3]);
                    correctAnswerIndexField.setValue(q.getCorrectAnswerIndex());
                    difficultyField.setValue(q.getDifficulty().toString());
                });
    }

    private void editQuestion() {
        String selected = editComboBox.getValue();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No question selected.");
            return;
        }

        if (!validateFields()) return;

        try {
            int index = (int) correctAnswerIndexField.getValue();
            if (index < 0 || index > 3) throw new NumberFormatException();

            questions.stream()
                    .filter(q -> q.getQuestionText().equals(selected))
                    .findFirst()
                    .ifPresent(q -> {
                        q.setQuestionText(questionTextField.getText());
                        q.setOptions(new String[]{option1Field.getText(), option2Field.getText(),
                                                option3Field.getText(), option4Field.getText()});
                        q.setCorrectAnswerIndex(index);
                        q.setDifficulty(difficultyField.getValue().toString());
                    });

            saveQuestions();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Question edited successfully.");
            updateComboBoxes();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Correct Answer Index must be a number between 0 and 3.");
        }
    }

    private void deleteQuestion() {
        String selected = deleteComboBox.getValue();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No question selected.");
            return;
        }

        questions.removeIf(q -> q.getQuestionText().equals(selected));
        saveQuestions();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Question deleted successfully.");
        updateComboBoxes();
    }

    private void updateComboBoxes() {
        editComboBox.getItems().clear();
        deleteComboBox.getItems().clear();
        questions.forEach(q -> {
            editComboBox.getItems().add(q.getQuestionText());
            deleteComboBox.getItems().add(q.getQuestionText());
        });
    }

    private void clearFields() {
        questionTextField.clear();
        option1Field.clear();
        option2Field.clear();
        option3Field.clear();
        option4Field.clear();
        correctAnswerIndexField.setValue(null);
        difficultyField.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
   
}

