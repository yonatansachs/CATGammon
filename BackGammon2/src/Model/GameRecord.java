package Model;

public class GameRecord {
    private String player1;
    private String player2;
    private String winner;
    private String difficulty;
    private String duration;

    public GameRecord(String player1, String player2, String winner, String difficulty, String duration) {
        this.player1 = player1;
        this.player2 = player2;
        this.winner = winner;
        this.difficulty = difficulty;
        this.duration = duration;
    }

    // Getters
    public String getPlayer1() {
        return player1.trim();
    }

    public String getPlayer2() {
        return player2.trim();
    }

    public String getWinner() {
        return winner.trim();
    }

    public String getDifficulty() {
        return difficulty.trim();
    }

    public String getDuration() {
        return duration.trim();
    }
}
