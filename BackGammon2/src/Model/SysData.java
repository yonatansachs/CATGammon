package Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Callback;

public class SysData {
    private static SysData instance;
    private List<Question> questions;
    private final List<GameRecord> history; // List to store game history
    private final String QUESTIONS_FILE = "src/questions.json";
    private final String HISTORY_FILE = "src/game_history.json";

    private SysData() {
        questions = new ArrayList<>();
        loadQuestions();
        history = new ArrayList<>();
        loadHistory();

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

    private void loadHistory() {
        try (BufferedReader br = new BufferedReader(new FileReader(HISTORY_FILE))) {
            history.clear(); // Clear the current history list
            StringBuilder jsonContent = new StringBuilder();
            String line;

            // Read the entire JSON file into a single string
            while ((line = br.readLine()) != null) {
                jsonContent.append(line.trim());
            }

            // Remove square brackets and split individual records
            String content = jsonContent.toString().replace("[", "").replace("]", "");
            String[] records = content.split("\\},\\{");

            for (String record : records) {
                // Clean up braces and quotes
                record = record.replace("{", "").replace("}", "").replace("\"", "");
                String[] fields = record.split(",");

                // Temporary variables to hold field values
                String player1 = "", player2 = "", winner = "", difficulty = "", duration = "";

                for (String field : fields) {
                    String[] keyValue = field.split(":", 2);
                    if (keyValue.length < 2) continue;

                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();

                    // Assign values based on keys
                    switch (key) {
                        case "player1":
                            player1 = value;
                            break;
                        case "player2":
                            player2 = value;
                            break;
                        case "winner":
                            winner = value;
                            break;
                        case "difficulty":
                            difficulty = value;
                            break;
                        case "duration":
                            duration = value;
                            break;
                    }
                }

                // Add parsed record to history
                history.add(new GameRecord(player1, player2, winner, difficulty, duration));
            }

        } catch (IOException e) {
            System.err.println("Failed to load game history: " + e.getMessage());
        }
    }
    
    public void saveHistory() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(HISTORY_FILE))) {
            for (GameRecord record : history) {
                bw.write(String.format("%s,%s,%s,%s,%s%n",
                        record.getPlayer1(), record.getPlayer2(),
                        record.getWinner(), record.getDifficulty(),
                        record.getDuration()));
            }
        } catch (IOException e) {
            System.err.println("Failed to save game history: " + e.getMessage());
        }
    }
    public void addGameRecord(GameRecord record) {
        history.add(record);
        saveHistory();
    }

    public List<GameRecord> getHistory() {
        return new ArrayList<>(history);
    }
    
    public List<GameRecord> getGameHistory() {
        return new ArrayList<>(history); // Return the full history list directly
    }
}