package application;

import static application.Utilitaires.createText;

import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import application.composants.Language;
import application.composants.Choix;

public class FXScenes {

	public final static String FONT_TITLE = "Grixel Kyrou 7 Wide Bold";
	public final static String FONT_TEXT = "Courier New Bold";
	
	static Text pointsVal = new Text("0");
	static Text conditionVal = new Text();
	static Text CPM = new Text("0");
	static Text accText = new Text("100%");
	static Text timeLeft = new Text("");
	static Text CPMNormal = new Text("0");
	static final StackPane pauseBox = new StackPane();

	public static final TextField input = new TextField();
	public static final Choix[] gamemodes = {
			new Choix(250, "Normal", 40, true),
			new Choix(290, "Jeu", 40, false)
	};
	
	public static Choix[] lngs;

	//le menu de selection:
	public static Pane selectMenu(String type) {
		Pane root = new Pane();
			root.setPrefSize(800, 500);
			root.setStyle("-fx-background-color: rgb(14, 14, 14)");
	
		Text header = createText(type, Color.WHITE, FONT_TITLE, 50);
			header.setTranslateX((800 - header.getLayoutBounds().getWidth())/2);
			header.setTranslateY(130);
		
		root.getChildren().add(header);
		
		switch(type) {
			case "fastClavier":
				root.getChildren().addAll(gamemodes);
				break;
			case "LANGUAGES":
				String[][] loadedLanguages = Language.chargerLanguages();
				lngs = new Choix[Language.how_many_lngs];
				for(int i = 0; i< Language.how_many_lngs; i++) {
					lngs[i] = new Choix(220 + 25*i, loadedLanguages[i][0], loadedLanguages[i][1], i==0);
				}
				root.getChildren().addAll(lngs);
				break;
		}
		return root;
	}
	//le scene de fin
	public static Pane gameOver() {
		
		Pane root = new Pane();
			root.setPrefSize(800, 500);
			root.setStyle("-fx-background: " + "#0E0E0E");
			
		StackPane stack = new StackPane();
			stack.setTranslateY(200);
		Rectangle background = new Rectangle(800, 500);
			background.setTranslateX(0);
			background.setTranslateY(0);
			background.setFill(Color.BLACK);
				
		int pointsLen = String.valueOf(Math.round(VuePrincipale.points)).length();
		Text pointsText = new Text("Your score: ");
			pointsText.setFont(Font.font(FONT_TITLE, 30));
			pointsText.setFill(Color.WHITE);
			pointsText.setTranslateX(400-(300+pointsLen*36)/2);
			
		Text pointsVal = new Text(FXScenes.pointsVal.getText());
			pointsVal.setFont(Font.font(FONT_TITLE, 32));
			pointsVal.setFill(Color.web("#FF554D"));
			pointsVal.setTranslateX(pointsText.getTranslateX() + (305+pointsLen*36)/2);
		
		stack.getChildren().addAll(pointsText, pointsVal);
		root.getChildren().addAll(background, stack);
					
		return root;
	}

	//la scene commune de jeu
	public static Pane jeu(int gamemode) {
		
		Pane root = new Pane();
			root.setPrefSize(800, 500);
				
		Rectangle bottomLine = new Rectangle(800, 5);
			bottomLine.setTranslateX(0); bottomLine.setTranslateY(400);
			bottomLine.setFill(Color.web("#131313"));
		
		Rectangle background = new Rectangle(800, 500);
			background.setTranslateX(0); background.setTranslateY(0);
			background.setFill(Color.BLACK);
			
		int pointsLen = String.valueOf(Math.round(VuePrincipale.points)).length()+5;
                if(gamemode==0){
                	Text pointsText = new Text("Time: ");
					pointsText.setFill(Color.WHITE);
					pointsText.setTranslateX(30);
					pointsText.setFont(Font.font(FONT_TEXT, 17));

					timeLeft.setFill(Color.WHITE);
					timeLeft.setTranslateX(90);
					timeLeft.setFont(Font.font(FONT_TEXT, 17));

					Text conditionText = new Text("Accuraccy: ");
					conditionText.setFill(Color.WHITE);
					conditionText.setTranslateX(230);
					conditionText.setFont(Font.font(FONT_TEXT, 17));

					accText.setFill(Color.GREEN);
					accText.setTranslateX(340);
					accText.setFont(Font.font(FONT_TEXT, 17));

					Text CPMText = new Text("CPM: ");
					CPMText.setFill(Color.WHITE);
					CPMText.setTranslateX(230);
					CPMText.setFont(Font.font(FONT_TEXT, 17));


					CPMNormal.setFill(Color.GOLD);
					CPMNormal.setTranslateX(280);
					CPMNormal.setFont(Font.font(FONT_TEXT, 17));


					StackPane topStack = new StackPane();
					topStack.setTranslateX(0);
					topStack.setTranslateY(425);
					topStack.setMaxWidth(800);
					topStack.setPrefWidth(800);
					topStack.getChildren().addAll(pointsText,timeLeft,conditionText, accText);
				
					StackPane bottomStack = new StackPane();
					bottomStack.setTranslateX(0);
					bottomStack.setTranslateY(455);
					bottomStack.setMaxWidth(800);
					bottomStack.setPrefWidth(800);
					bottomStack.getChildren().addAll(CPMText, CPMNormal);

					bottomStack.setAlignment(Pos.CENTER_LEFT);
					topStack.setAlignment(Pos.CENTER_LEFT);

					root.getChildren().addAll(background, bottomLine, topStack, bottomStack, pauseBox);
                }
                else{
		Text pointsText = new Text("Level: ");
			pointsText.setFill(Color.WHITE);
			pointsText.setTranslateX(30);
			pointsText.setFont(Font.font(FONT_TEXT, 17));
			
			pointsVal.setFill(Color.GREEN);
			pointsVal.setTranslateX(50+10*pointsLen);
			pointsVal.setFont(Font.font(FONT_TEXT, 17));
		
		Text conditionText = new Text("Lives: ");
			conditionText.setFill(Color.WHITE);
			conditionText.setTranslateX(230);
			conditionText.setFont(Font.font(FONT_TEXT, 17));
			
			conditionVal.setFill(Color.RED);
			conditionVal.setTranslateX(conditionText.getTranslateX() + conditionText.getLayoutBounds().getWidth());
			conditionVal.setFont(Font.font(FONT_TEXT, 17));
		
		Text CPMText = new Text("CPM: ");
			CPMText.setFill(Color.WHITE);
			CPMText.setTranslateX(230);
			CPMText.setFont(Font.font(FONT_TEXT, 17));
			
			CPM.setFill(Color.YELLOW);
			CPM.setTranslateX(280);
			CPM.setFont(Font.font(FONT_TEXT, 17));
			
		Text signL = new Text("[");
			signL.setFill(Color.WHITE);
			signL.setTranslateX(27);
			signL.setFont(Font.font(FONT_TEXT, 17));
		
		Text signR = new Text("]");
			signR.setFill(Color.WHITE);
			signR.setTranslateX(27+120+5);
			signR.setFont(Font.font(FONT_TEXT, 17));
			
		Text pause = new Text("PAUSE");
			pause.setFill(Color.RED);
			pause.setFont(Font.font(FONT_TEXT, 25));
			
		Rectangle pauseBg = new Rectangle(100, 40);
			pauseBg.setFill(Color.BLACK);
		
		pauseBox.getChildren().addAll(pauseBg, pause);
			pauseBox.setTranslateX(350);
			pauseBox.setTranslateY(200);
			pauseBox.setVisible(false);
			
		input.setPrefWidth(120);
		input.setPrefHeight(20); 
		input.setMaxWidth(120);
		input.setMaxHeight(20);
		input.setTranslateX(35);
		input.setStyle("-fx-faint-focus-color: transparent;"
				+ "-fx-focus-color: transparent;"
				+ "-fx-text-box-border: transparent;"
				+ "-fx-background-color: #0e0e0e;"
				+ "-fx-text-fill: #FFF;"
				+ "-fx-highlight-fill: #FFF;"
				+ "-fx-highlight-text-fill: #000;"
				+ "-fx-cursor: block;"
				+ "-fx-font-family: 'Courier new', monospace;");
		input.setOnKeyTyped(e -> {
			final int maxCharacters = 30;
	        if(input.getText().length() > maxCharacters) {
	        	e.consume();
	        }
		});
		StackPane topStack = new StackPane();
			topStack.setTranslateX(0);
			topStack.setTranslateY(425);
			topStack.setMaxWidth(800);
			topStack.setPrefWidth(800);
		topStack.getChildren().addAll(pointsText, pointsVal, conditionText, conditionVal);
		StackPane bottomStack = new StackPane();
			bottomStack.setTranslateX(0);
			bottomStack.setTranslateY(455);
			bottomStack.setMaxWidth(800);
			bottomStack.setPrefWidth(800);
		bottomStack.getChildren().addAll(input, signL, signR, CPMText, CPM);
		bottomStack.setAlignment(Pos.CENTER_LEFT);
		topStack.setAlignment(Pos.CENTER_LEFT);
		root.getChildren().addAll(background, bottomLine, topStack, bottomStack, pauseBox);
                }
		return root;
	}
		
	public static StackPane erreur(String err) {
		StackPane root = new StackPane();
			root.setPrefSize(800, 500);
			root.setStyle("-fx-background-color: rgb(14, 14, 14)");
		Rectangle errorBox = new Rectangle(350, 130);
			errorBox.setFill(Color.web("#0e0e0e"));		
			errorBox.setStyle("-fx-stroke: white; fx-stroke-width: 2");
		
		Label error = new Label(" ERROR ");
			error.setTranslateY(-65);
			error.setStyle("-fx-background-color: #0e0e0e;");
			error.setTextFill(Color.WHITE);	
			error.setFont(Font.font(FONT_TEXT, 25));
			
		Text errorMsg = new Text();
			errorMsg.setWrappingWidth(350);
			errorMsg.setFill(Color.WHITE);
			errorMsg.setFont(Font.font(FONT_TEXT, 20));
			errorMsg.setTextAlignment(TextAlignment.CENTER);

		switch(err) {
			case "MISSING_WORDS": 
				errorMsg.setText("Missing folder with words");
			break;
			
			case "TEST":
				errorMsg.setText("LONG TEST TEXT TO SEE HOW IT WRAPS");
			break;
			
		}
		
		root.getChildren().addAll(errorBox, errorMsg, error);
		
		return root;
	}
	public static void fontSet() {
		String[] fontNames = {
				"Grixel Kyrou 7 Wide Bold.ttf",
				"Courier New.ttf",
				"Courier New Bold.ttf",
		};
		
		/* chargement des fonts */
		for(String font : fontNames) {
			try {
				Font.loadFont(FXScenes.class.getResourceAsStream("/application/fonts/" + font), 20);
			} catch (Exception e) {
			}
		}		
	}	

}