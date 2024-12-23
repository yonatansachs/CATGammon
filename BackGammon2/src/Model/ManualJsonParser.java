package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ManualJsonParser {
    public static void main(String[] args) {
        List<Question> questions = new ArrayList<>();
        try {
            // Read the JSON file into a String
            BufferedReader reader = new BufferedReader(new FileReader("resources/questions.json"));
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            reader.close();

            // Parse the JSON manually
            String content = jsonContent.toString();
            String[] questionBlocks = content.split("\\{");

            for (String block : questionBlocks) {
                if (block.contains("questionText")) {
                    // Extract questionText
                    String questionText = extractValue(block, "questionText");

                    // Extract options
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

                    // Create Question object and add it to the list
                    questions.add(new Question(questionText, options, correctAnswerIndex, difficulty));
                }
            }

            // Print all the questions
            for (Question question : questions) {
                question.printQuestion();
                System.out.println("------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Utility method to extract values for a specific key
    private static String extractValue(String block, String key) {
        String value = block.split("\"" + key + "\":")[1].split(",")[0];
        value = value.replace("\"", "").replace("}", "").trim();
        return value;
    }
}
