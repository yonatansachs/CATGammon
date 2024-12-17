package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class SysData {
    private static SysData instance;
    private List<Question> questions;

    private SysData() {
        questions = new ArrayList<>();
        loadQuestions();
    }

    public static SysData getInstance() {
        if (instance == null) {
            instance = new SysData();
        }
        return instance;
    }

    private void loadQuestions() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/questions.json"))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            String json = jsonContent.toString();
            parseQuestions(json); // Call to parse questions manually
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseQuestions(String json) {
        // Split the JSON string to extract question objects
        String[] questionBlocks = json.split("\\{");

        for (String block : questionBlocks) {
            if (block.contains("questionText")) {
                // Extract questionText
                String questionText = extractValue(block, "questionText");

                // Extract options array
                String optionsBlock = block.split("\"options\": \\[")[1].split("\\]")[0];
                String[] options = optionsBlock.split("\",");
                for (int i = 0; i < options.length; i++) {
                    options[i] = options[i].replace("\"", "").trim();
                }

                // Extract correctAnswerIndex
                String correctAnswerIndexStr = extractValue(block, "correctAnswerIndex");
                int correctAnswerIndex = Integer.parseInt(correctAnswerIndexStr);

                // Extract difficulty
                String difficulty = extractValue(block, "difficulty");

                // Add question to the list
                questions.add(new Question(questionText, options, correctAnswerIndex, difficulty));
            }
        }
    }

    private String extractValue(String block, String key) {
        String value = block.split("\"" + key + "\":")[1].split(",")[0];
        value = value.replace("\"", "").replace("}", "").trim();
        return value;
    }

    public Question getRandomQuestion(String difficulty) {
        List<Question> filteredQuestions = new ArrayList<>();
        for (Question question : questions) {
            if (question.getDifficulty().equalsIgnoreCase(difficulty)) {
                filteredQuestions.add(question);
            }
        }
        if (filteredQuestions.isEmpty()) return null;

        int randomIndex = (int) (Math.random() * filteredQuestions.size());
        return filteredQuestions.get(randomIndex);
    }
}