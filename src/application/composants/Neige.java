package application.composants;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Neige extends StackPane {
		
	private final Thread mover;
	
	public Neige(int distance, double x, double y, double alpha, Pane root) {
		this.setPrefSize(distance+2, distance);
		this.setAlignment(Pos.CENTER_LEFT);
		this.setTranslateX(x);
		this.setTranslateY(y);
		
		Rectangle mainBlock = new Rectangle(distance, distance);
		Rectangle trail_1 = new Rectangle(distance, distance);
		Rectangle trail_2 = new Rectangle(distance, distance);
		Rectangle[] rects = {trail_2, trail_1, mainBlock};
		
		for(int i=0; i<3; i++) {
			Rectangle r = rects[i];
				r.setTranslateX(i);
				r.setFill(Color.WHITE);
				r.setOpacity((i+1)*(1.0/3)*alpha);
			this.getChildren().add(r);
		}
		
		mover = new Thread(() -> {
			while(getTranslateX() < 800) {
				Platform.runLater(() -> moveForward());
				try {
					Thread.sleep((long)5*distance);
				} catch (InterruptedException e) {
				}
			}
			stopMoving();
			Platform.runLater(() -> root.getChildren().remove(this));
		});
		mover.start();
	}
	
	public void stopMoving() {
		mover.interrupt();
	}
		
	public void moveForward() {
		this.setTranslateX(this.getTranslateX() + 1);
	}

}
