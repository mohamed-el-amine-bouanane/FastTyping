package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import application.composants.Neige;
import application.composants.Mot;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class Utilitaires {

	public static Mot createWord(List<String> strings, List<Integer> xVal_final, List<Integer> yVal_final, List<Mot> fresh) {
		List<Integer> xVal = VuePrincipale.xVal, yVal = VuePrincipale.yVal;
		
		if(xVal.size() < 1) {
			xVal = new ArrayList<>(xVal_final);
		}
		if(yVal.size() < 1) {
			yVal = new ArrayList<>(yVal_final);
		}
		
		Random random = new Random();

		int rndmx = random.nextInt(xVal.size());
		int rndmy = random.nextInt(yVal.size());
	
		String value = strings.get(random.nextInt(strings.size()));		// get random text from all words
		int y = yVal.get(rndmy);
			yVal.remove(rndmy);
		int x = xVal.get(rndmx);
			xVal.remove(rndmx);
		
		for(Mot w : fresh) {
			if(w.getTranslateY() == y) {
				while(w.getTranslateX() <= (value.length()*9)+x+20) {
					x -= 5;
				}
			}
		}	
		
		return new Mot(x, y, value);
		
	}
	
	/* fades in given node */
	public static void fadeIn(Node node, int duration) {
		FadeTransition ft = new FadeTransition(Duration.millis(duration), node);
			ft.setFromValue(node.getOpacity());
		    ft.setToValue(1);
		    ft.play();
	}
		
	public static AnimationTimer blinkingNodeTimer(Node node) {
		return blinkingNodeTimer(node, 500_000_000);
	}
	
	public static AnimationTimer blinkingNodeTimer(Node node, long speed) {
		 return new AnimationTimer() {
			
			private long lastUpdate = 0;
						
			@Override
			public void handle(long now) {		
				
				if(now - lastUpdate >= speed) {
					node.setVisible(!node.isVisible());
					lastUpdate = now;
				}
			}
		};		
	}
	

	public static Text createText(String value, Color fill, String fontName, int fontSize) {
		Text t = new Text(value);
		t.setFont(Font.font(fontName, fontSize));
		t.setFill(fill);
		
		return t;
	}
	
	public static AnimationTimer getBackgroundTimer(int xRange, int yRange, Pane root) {
		return getBackgroundTimer(xRange, yRange, root, 30, 5_000_000);
	}
	
	public static AnimationTimer getBackgroundTimer(int xRange, int yRange, Pane root, int initialAmount, long creationDelay, Node... toFront) {
		Random random = new Random();
		
		int[] particleY = new int[yRange];
		for(int i=0; i<yRange; i++) {
			particleY[i] = i+2;	// des valeurs predefinies de Y
		}
		
		/* generer la neige*/
		for(int i=0; i<initialAmount; i++) {
			final int x = random.nextInt(xRange)+10;
			final int y = particleY[random.nextInt(yRange)];
			final double alpha = 0.1 + 0.70 * random.nextDouble();
			final int distance = getRandomDistance(alpha);
			
			Neige p = new Neige(distance, x, y, alpha, root);
				root.getChildren().add(p);
		}
				
		/* animation neige */
		return new AnimationTimer() {

			private long particle_create = 0;
			
			@Override
			public void handle(long now) { 				
				if(now - particle_create >= creationDelay) {
					final int y = particleY[random.nextInt(yRange)];
					final double alpha = 0.1 + 0.70 * random.nextDouble();
					final int distance = getRandomDistance(alpha);
					
					Neige p = new Neige(distance, -2, y, alpha, root);
						root.getChildren().add(p);
						
					for(Node n : toFront) {
						n.toFront();
					}
					
					particle_create = now;	
				}
			}
			
		};		
		
	}
	
	private static int getRandomDistance(double alpha) {
		if(alpha <= 0.45) return 1;
		if(alpha <= 0.70) return 2;
		if(alpha <= 1.00) return 3;
		
		return 0;
	}
	
}
