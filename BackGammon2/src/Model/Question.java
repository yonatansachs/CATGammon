package Model;

public class Question {
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