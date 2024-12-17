import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

class Question {
    private String questionText;
    private String[] options;
    private int correctAnswerIndex;
    private String difficulty;

    public Question(String questionText, String[] options, int correctAnswerIndex, String difficulty) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.difficulty = difficulty;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void printQuestion() {
        System.out.println("Question: " + questionText);
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("Correct Answer Index: " + correctAnswerIndex);
        System.out.println("Difficulty: " + difficulty);
    }
}

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