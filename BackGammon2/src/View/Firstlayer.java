package View;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Firstlayer {

    private final static ImageView board = new ImageView();

    public Firstlayer(Stage primaryStage) {
        // Set default background
        try {
            Image img = new Image(getClass().getResourceAsStream("backgammon2.png"));
            board.setImage(img);
        } catch (Exception e) {
            System.err.println("Error loading default background image: backgammon2.png");
            e.printStackTrace();
        }

        board.fitWidthProperty().bind(primaryStage.widthProperty());
        board.fitHeightProperty().bind(primaryStage.heightProperty());
        board.setPreserveRatio(true);
    }

    public static void changeBackground(String theme) {
        try {
            String imagePath;
            switch (theme) {
                case "Black and White":
                    imagePath = "backgammon_black_and_white.png";
                    break;
                case "Yellow and White":
                    imagePath = "backgammon_yellow_and_white.png";
                    break;
                case "Green and White":
                    imagePath = "backgammon_green_and_white.png";
                    break;
                default:
                    imagePath = "backgammon2.png"; // Default
            }
            Image img = new Image(Firstlayer.class.getResourceAsStream(imagePath));
            board.setImage(img);
        } catch (Exception e) {
            System.err.println("Error loading background image for theme: " + theme);
            e.printStackTrace();
        }
    }

    public ImageView getBoard() {
        return board;
    }
}
