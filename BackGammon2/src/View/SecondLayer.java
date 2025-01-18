package View;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class SecondLayer{

    private final GridPane gridUp = new GridPane();
    
    private final int rows = 6;
    private final int columns = 1;
    
    private int heightC=51;
    private int heightR=50;
    
    public static final double[] cols = {
        48,
        128,
        212,
        289,
        375,
        455,
        593,
        675,
        757,
        838,
        922,
        1006,   
        1006,
        922,
        838,
        757,
        675,
        593,
        455,
        375,
        289,
        212,
        128,
        48,
    };
    
    public SecondLayer(){

            for(int i = 0; i < columns; i++) {
            
            ColumnConstraints column = new ColumnConstraints(heightC);
            gridUp.getColumnConstraints().add(column);

            }

            for(int i = 0; i < rows; i++) {
            
            RowConstraints row = new RowConstraints(heightR);
            gridUp.getRowConstraints().add(row);

            }

            
            
    }
    
    public GridPane setLayoutUp(int a){

            gridUp.setLayoutX(cols[a]);
            gridUp.setLayoutY(26);
            
        return gridUp;
    }
    
    public GridPane setLayoutDown(int a){

            gridUp.setLayoutX(cols[a]);
            gridUp.setLayoutY(428);

        return gridUp;
    }
    
    public void setHeightR(int value){ heightR=value;}
    public int getHeightR(){ return heightR;}

}


