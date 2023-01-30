package application.composants;

import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.paint.Color;

public class FinBloc extends Rectangle {
	private final double speed;
	public FinBloc(int y, double speed, String LR) {
		super(0, 50, Color.RED);
		
		this.speed = speed;
		if(LR.equals("R")) {
			getTransforms().add(new Rotate(180));
			setTranslateY(y+50);
			setTranslateX(800);
		} else {
			setTranslateX(0);
			setTranslateY(y);
		}
			
	}
	
	public void moveToMiddle() {
		this.setWidth(this.getWidth()+this.speed*0.7);
	}
	
	public void moveOutside() {
		this.setWidth(this.getWidth()-this.speed);
	}
	
}
