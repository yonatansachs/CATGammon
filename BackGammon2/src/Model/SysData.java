package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SysData {

    private static SysData instance; // Singleton instance
    private final List<Question> questions; // List to store questions
    //private final List<GameRecord> history; // List to store game history
    private final String QUESTIONS_FILE = "src/questions.json";
    //private final String HISTORY_FILE = "src/game_history.json";

    private SysData() {
        questions = new ArrayList<>();
        //history = new ArrayList<>();
        loadQuestions();
        //loadHistory();
    }

    public static SysData getInstance() {
        if (instance == null) {
            instance = new SysData();
        }
        return instance;
    }

    // Load Questions
    private void loadQuestions() {
        try (BufferedReader br = new BufferedReader(new FileReader(QUESTIONS_FILE))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                jsonContent.append(line.trim());
            }

            parseQuestions(jsonContent.toString());
        } catch (IOException e) {
            System.err.println("Failed to load questions: " + e.getMessage());
        }
    }

    private void parseQuestions(String json) {
        json = json.substring(1, json.length() - 1);
        String[] questionObjects = json.split("\\},\\{");

        for (String obj : questionObjects) {
            obj = obj.replace("{", "").replace("}", "").replace("\"", "");
            String[] fields = obj.split(",");

            String questionText = "";
            String[] options = new String[4];
            int correctAnswerIndex = -1;
            String difficulty = "";

            for (String field : fields) {
                String[] keyValue = field.split(":", 2);
                if (keyValue.length < 2) continue;

                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                switch (key) {
                    case "questionText":
                        questionText = value;
                        break;
                    case "options":
                        value = value.substring(1, value.length() - 1);
                        options = value.split(",");
                        break;
                    case "correctAnswerIndex":
                        correctAnswerIndex = Integer.parseInt(value);
                        break;
                    case "difficulty":
                        difficulty = value;
                        break;
                }
            }

            if (!questionText.isEmpty() && options.length > 0 && correctAnswerIndex >= 0 && !difficulty.isEmpty()) {
                questions.add(new Question(questionText, options, correctAnswerIndex, difficulty));
            }
        }
    }

    /*// Load Game History
    private void loadHistory() {
        try (BufferedReader br = new BufferedReader(new FileReader(HISTORY_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    history.add(new GameRecord(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load game history: " + e.getMessage());
        }
    }*/

    /*public void saveHistory() {
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
    }*/

    /*// Add Game Record
    public void addGameRecord(GameRecord record) {
        history.add(record);
        saveHistory();
    }*/

    /*public List<GameRecord> getHistory() {
        return new ArrayList<>(history);
    }*/

    public Question getRandomQuestion(String difficulty) {
        List<Question> filteredQuestions = new ArrayList<>();
        for (Question q : questions) {
            if (q.getDifficulty().equalsIgnoreCase(difficulty)) {
                filteredQuestions.add(q);
            }
        }

        if (filteredQuestions.isEmpty()) return null;

        Random random = new Random();
        return filteredQuestions.get(random.nextInt(filteredQuestions.size()));
    }
}