package View;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PawnsView {

	public static double radius=24.0f;
	public static boolean color;
	    
	public static Node aPawn(boolean white) {
	    
	        Circle w = new Circle();
	        w.setCenterX(100.0f);
	        w.setCenterY(100.0f);
	        w.setRadius(radius);
	        w.setFill(Color.BLUE);
	        
	        Circle b = new Circle();
	        b.setCenterX(100.0f);
	        b.setCenterY(100.0f);
	        b.setRadius(radius);
	        b.setFill(Color.BLACK);

	        if(white)
	            return w;

	        return b;
	    }
}
