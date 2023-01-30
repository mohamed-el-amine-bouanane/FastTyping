package application.composants;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Choix extends StackPane {

	private final Text value;
	private final Rectangle background;
	private boolean checked = false;
	private Text checkVal;


	public Choix(int y, String value, int height, boolean highlighted) {
		this(y, "", value, height, highlighted);
	}


	public Choix(int y, String checkVal, String value, boolean hightlighed) {
		this(y, checkVal, value, 25, hightlighed);
	}

	public Choix(int y, String checkVal, String value, int height, boolean highlighted) {
		int textX = 240;
		int bgX = 240;
		int bgWidth = 320;

		this.checkVal = new Text(checkVal);
		this.checkVal.setTranslateX(250);
		this.checkVal.setTranslateY(y);
		this.checkVal.setFont(Font.font("Courier new", 14));

		this.value = new Text(value);
		this.value.setTranslateX(textX);
		this.value.setTranslateY(y);
		this.value.setFont(Font.font("Courier new", 14));

		background = new Rectangle(bgWidth, height, Color.WHITE);
		background.setTranslateX(bgX);
		background.setTranslateY(y);

		this.setHighlighted(highlighted);

		setAlignment(this.value, Pos.CENTER);
		setAlignment(this.checkVal, Pos.CENTER_LEFT);
		getChildren().addAll(background, this.value, this.checkVal);
	}

	public void setChecked(boolean checked) {
		this.checkVal.setText(checked ? "[ x ]" : "[   ]");
		this.checked = checked;
	}

	public boolean getChecked() {
		return this.checked;
	}

	public void setHighlighted(boolean highlighted) {
		if(highlighted) {
			this.background.setFill(Color.WHITE);
			this.value.setFill(Color.BLACK);
			this.checkVal.setFill(Color.BLACK);
		} else {
			this.background.setFill(Color.BLACK);
			this.value.setFill(Color.WHITE);
			this.checkVal.setFill(Color.WHITE);
		}
	}
}
