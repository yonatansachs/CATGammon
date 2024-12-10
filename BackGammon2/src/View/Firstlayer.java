package View;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Firstlayer{

	
    private final Image img  = new Image(getClass().getResourceAsStream("backgammon2.png"));
    private final ImageView board = new ImageView(img);

    public Firstlayer(Stage primaryStage){

        board.fitWidthProperty().bind(primaryStage.widthProperty());
        board.fitHeightProperty().bind(primaryStage.heightProperty());

        board.setPreserveRatio(true);

    }
        
    public ImageView getBoard(){
    
    return board;
    
    }
}