package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SysData {

    private static SysData instance; // Singleton instance
    private final List<Question> questions; // List to store questions
    private final List<GameRecord> history; // List to store game history
    private final String QUESTIONS_FILE = "src/questions.json";
    private final String HISTORY_FILE = "src/game_history.json";

    private SysData() {
        questions = new ArrayList<>();
        history = new ArrayList<>();
        loadQuestions();
        loadHistory();
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
            System.out.println(line);

            parseQuestions(jsonContent.toString());
        } catch (IOException e) {
            System.err.println("Failed to load questions: " + e.getMessage());
        }
    }
    
    
    /*private void loadQuestions() {
        questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/questions.json")))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            // Parse JSON
            JSONObject jsonObject = new JSONObject(jsonContent.toString());
            JSONArray questionsArray = jsonObject.getJSONArray("questions");

            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject questionObj = questionsArray.getJSONObject(i);

                String questionText = questionObj.getString("questionText");

                // קריאת התשובות מה-JSON
                JSONArray optionsArray = questionObj.getJSONArray("options");
                String[] options = new String[optionsArray.length()];
                for (int j = 0; j < optionsArray.length(); j++) {
                    options[j] = optionsArray.getString(j);
                }

                int correctAnswerIndex = questionObj.getInt("correctAnswerIndex");
                String difficulty = questionObj.getString("difficulty");

                // יצירת אובייקט Question והוספה לרשימה
                questions.add(new Question(questionText, options, correctAnswerIndex, difficulty));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/


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
                    	 value = value.substring(1, value.length() - 1); // Remove brackets
                    	 options = value.split("\",\""); // Split options correctly
                    	 for (int j = 0; j < options.length; j++) {
                    		 options[j] = options[j].replace("\"", "").trim(); // Remove extra quotes
                    	 }
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

    // Add Game Record
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


    public Question getRandomQuestion(String difficulty) {
    	List<Question> filteredQuestions = new ArrayList<>();
        for (Question q : questions) {
            if (q.getDifficulty().equalsIgnoreCase(difficulty)) {
                filteredQuestions.add(q);
            }
        }
        Random random = new Random();
        return questions.isEmpty() ? null : questions.get(random.nextInt(questions.size()));
    }
    
 /*   public Question getRandomQuestion(String difficulty) {
        List<Question> filteredQuestions = new ArrayList<>();
        for (Question q : questions) {
            if (q.getDifficulty().equalsIgnoreCase(difficulty)) {
                filteredQuestions.add(q);
            }
        }

        if (filteredQuestions.isEmpty()) return null;

        Random random = new Random();
        return filteredQuestions.get(random.nextInt(filteredQuestions.size()));
    }*/
}