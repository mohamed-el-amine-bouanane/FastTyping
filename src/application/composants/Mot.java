package application.composants;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Mot extends Text {

	public Mot(int x, int y, String value) {
		this(x, y, value, "");
	}

	public Mot(int x, int y, String value, String toSuper) {
		super(value);

		setTranslateX(x);
		setTranslateY(y);
		
		setFill(Color.WHITE);
		setFont(Font.font("Courier New Bold", 14.49));
	}
	
	public int getLength() {
		return this.getText().length();
	}
	
	public String getValue() {
		return this.getText();
	}
	
	public void moveForward() {
		this.setTranslateX(this.getTranslateX() + 15);
	}
}
