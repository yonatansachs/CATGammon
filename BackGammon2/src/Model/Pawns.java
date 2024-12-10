package Model;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import View.PawnsView;
public class Pawns{



public Pawns(){}

public Node createPawn(boolean color){

    PawnsView.color=color;
    return PawnsView.aPawn(color);

}

public void setRadius(double value){

    PawnsView.radius=value;

}
public double getRadius(){return PawnsView.radius;}

public Node getPawn(){
    
return PawnsView.aPawn(PawnsView.color);
}

}
