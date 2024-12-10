package application;

import java.net.URL;
import java.util.Random;
import javafx.scene.image.Image ;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import View.Firstlayer;
import Model.Pawns;
import View.SecondLayer;
import Control.GamePlay;
public class Backgammon extends Application {

	
    @Override
    
    public void start(Stage primaryStage) {
        
        //-------------------STATEMENTS-------------------------------------
        //-------------------STATEMENTS-------------------------------------
        
        Pane pane=new Pane();
        
        Firstlayer first = new Firstlayer(primaryStage);
        
        GridPane[] gridCols = new GridPane[30];

        Pawns pawn = new Pawns();
        
        int[] blueUp = new int[12];
        int[] blueDown = new int[12];
        int[] blackUp = new int[12];
        int[] blackDown = new int[12];

        //-------------------STATEMENTS-------------------------------------
        //-------------------STATEMENTS-------------------------------------
        
        pane.getChildren().addAll(first.getBoard());

        for(int i=0; i<12; i++){
        
            SecondLayer up = new SecondLayer(); 

            gridCols[i]= new GridPane();

            gridCols[i]=up.setLayoutUp(i);
        
            pane.getChildren().add(gridCols[i]);

            //gridCols[i].setGridLinesVisible(true);

        }
        
        for(int i=12; i<24; i++){

            SecondLayer down = new SecondLayer(); 

            gridCols[i]= new GridPane();

            gridCols[i]=down.setLayoutDown(i);
     
            pane.getChildren().add(gridCols[i]);

            //gridCols[i].setGridLinesVisible(true);

        }

        GamePlay theGame = new GamePlay(gridCols,primaryStage);

        //************************************
        //************************************
        
        Button dices = new Button();
        
        boolean blue=true;
        
        boolean endOfGame=false;
        
        Label one = new Label("?");
        Label two = new Label("?");

        
        dices.setText("Blue");
        
            dices.setOnAction(new EventHandler<ActionEvent>() {

                boolean player=true;

                @Override
                public void handle(ActionEvent event) {

                    theGame.setTimes(2);   
                    
                    Random rand = new Random();                    
                    
                    int  diceOne = rand.nextInt(6) + 1;
                    int  diceTwo = rand.nextInt(6) + 1;
                    
                    one.setFont(Font.font(null, FontWeight.BOLD, 72));
                    two.setFont(Font.font(null, FontWeight.BOLD, 72));
                    
                    one.setText(String.valueOf(diceOne));
                    one.setLayoutX(528);one.setLayoutY(360);
                    one.setStyle("-fx-text-fill: white;");
                    two.setText(String.valueOf(diceTwo));
                    two.setLayoutX(528);
                    two.setLayoutY(290);
                    two.setStyle("-fx-text-fill: white;");

                    if(player){

                        if(diceOne==diceTwo){ theGame.setTimes(4);}
                        player=false;
                        dices.setText("Black");
                        theGame.reset();
                        theGame.bluePlays(gridCols,diceOne,diceTwo);

                    }else{

                        if(diceOne==diceTwo){ theGame.setTimes(4);}
                        player=true;
                        dices.setText("Blue");
                        theGame.reset();
                        theGame.blackPlays(gridCols,diceOne,diceTwo);
                    }

                }
            });

        //*****************************************************************************
        //*****************************************************************************
        
        pane.getChildren().addAll(dices,one,two);
        
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        
        Scene scene = new Scene(pane, screenBounds.getWidth(), screenBounds.getHeight());

        primaryStage.setTitle("Backgammon");
        
        primaryStage.setScene(scene);
        
        primaryStage.setMaxHeight(800);
        primaryStage.setMaxWidth(1100);
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(1100);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);   
    }




}