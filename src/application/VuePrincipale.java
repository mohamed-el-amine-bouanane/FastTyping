package application;

import static application.Utilitaires.blinkingNodeTimer;
import static application.Utilitaires.createWord;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import application.composants.FinBloc;
import application.composants.Mot;
import javafx.scene.shape.Rectangle;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import application.composants.Menu;
import application.composants.Language;

public class VuePrincipale extends Application {

	public static Stage fenetre;
	public static long howOften, howFast;
	public static int maxWords, howMany;
	public static int timeLeft;
	public static double multiplierAdd;
	public static List<Integer> xVal, yVal;
	public static double points;
	static final List<Integer> CPMs = new ArrayList<>();     
	static int avgCPM;    
	static double totalSeconds;
	private static AnimationTimer animation_words, animation_background, animation_gameover, animation_curtain, game_timer;
	private static boolean curtain, pause = false;
	private static int typedWords, typedChars, maxWordLen = 0;
	private static double multiplier;
	private static long startTime, pauseTime;

	public static int gameMode;
	public static boolean done = false;

	public static void launcher(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage)  {
		fenetre = primaryStage;
		FXScenes.fontSet();
		Menu.selectGamemode();

		fenetre.setTitle("fastClavier");
		fenetre.setResizable(false);
		fenetre.show();

	}

	public static void erreur(String err) {
		Scene error = new Scene(FXScenes.erreur(err));
		fenetre.setScene(error);

		error.setOnKeyPressed(e -> {
			switch (e.getCode()) {
				case ESCAPE:
					Menu.selectGamemode();
					break;
				default:
					break;
			}
		});
	}

        // cette methode prepare les resultats et le score pour gameOver 
	public static void pre_end(Pane root) {
		if (gameMode == 1 ) game_timer.stop();    

		curtain = true;
		Rectangle cover = new Rectangle(800, 500, Color.BLACK);
		cover.setVisible(false);

		root.getChildren().add(cover);

		List<FinBloc> blocks = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			FinBloc left_block = new FinBloc(i * 50, Math.log(20 - Math.abs(4.5 - i)) * 13, "L");
			blocks.add(left_block);
			FinBloc right_block = new FinBloc(i * 50, Math.log(20 - Math.abs(4.5 - i)) * 13, "R");
			blocks.add(right_block);

		}

		blocks.forEach(block -> block.toFront());

		animation_curtain = new AnimationTimer() {

			private long lastUpdate = 0;
			boolean reverse = false;

			@Override
			public void handle(long now) {

				if (now - lastUpdate >= 35_000_000) {

					if (!reverse) {
						if (blocks.get(0).getWidth() > 450) {
							animation_background.stop();
							if (gameMode == 1 ) animation_words.stop();

							reverse = true;
							cover.setVisible(true);
							cover.toFront();
							blocks.forEach(block -> block.toFront());
						} else {
							blocks.forEach(block -> block.moveToMiddle());
						}
					} else {
						if (blocks.get(8).getWidth() < 1) {
							animation_curtain.stop();
							terminerJeu();
							return;
						} else {
							blocks.forEach(block -> block.moveOutside());
						}
					}
					lastUpdate = now;
				}
			}

		};
		animation_curtain.start();
	}


	public static void terminerJeu() {

		if (CPMs.size() > 0) {
			for (int c : CPMs) {
				avgCPM = c;
			}
			avgCPM /= CPMs.size();
		} else {
			avgCPM = 0;
		}

		Pane root = FXScenes.gameOver();
		root.setPrefSize(800, 500);

		Text retry = new Text("> Press enter to try again <");
		retry.setFill(Color.WHITE);
		retry.setTranslateX(308);
		retry.setTranslateY(370);
		retry.setFont(Font.font("Courier new", 11));

		root.getChildren().add(retry);
		root.setOpacity(0);

		Scene scene = new Scene(root);
		scene.setFill(Color.BLACK);

		fenetre.setScene(scene);
		Utilitaires.fadeIn(root, 300);

		animation_gameover = blinkingNodeTimer(retry);
		animation_gameover.start();

		scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
			if (e.getCode() == KeyCode.ENTER) {
				animation_gameover.stop();
				System.out.println();
				Menu.selectGamemode();
			}
			e.consume();
		});
	}

	public static void lancerJeu(List<File> selected) {
		if (gameMode == 2) {
			Pane root = FXScenes.jeu(0);
			FXScenes.pointsVal.setText(String.valueOf(Math.round(points)));
			pre_end(root);
		} else {
			Pane root = FXScenes.jeu(gameMode);
			Scene scene = new Scene(root);
			if (gameMode == 0) {
				done = false;
				List<String> words = Language.chargerMots(selected);
				VueSecondaire view = new VueSecondaire(words);
				view.setAlignment(Pos.CENTER);
				root.setStyle("-fx-background-color : black;");
				root.getChildren().set(0, view);
				animation_background = Utilitaires.getBackgroundTimer(790, 398, root);
				animation_background.start();
				fenetre.setScene(scene);
				scene.setOnKeyPressed(e -> {
					String text = e.getText();
					if (text.length() != 0) {
						view.handleKeyPress(text);
					}

				});
			} else {
				/* RINISTIALISATION */
				curtain = false;
				typedWords = 0;
				typedChars = 0;
				multiplier = 0.98;
				points = 0;
				CPMs.clear();

				FXScenes.CPM.setText("0");
				FXScenes.pointsVal.setText("0");

				List<String> strings = Language.chargerMots(selected);    // liste de tous les mots
				List<Mot> words = new ArrayList<>();    // liste des mots en-cours
				List<Mot> fresh = new ArrayList<>();    // liste des nouveaux mots

				// trouver le longueur du mots le plus long
				for (String s : strings) {
					if (s.length() > maxWordLen) {
						maxWordLen = s.length();
					}
				}
				maxWordLen *= 9; 
				// liste des x et y
				final List<Integer> xVal_final = new ArrayList<>();
				final List<Integer> yVal_final = new ArrayList<>();
				// vleurs predefinies
				for (int i = -10; i < 10; i += 5) xVal_final.add(i);
				for (int i = 20; i < 400; i += 20) yVal_final.add(i);

				// temporary sublists
				xVal = new ArrayList<>(xVal_final);
				yVal = new ArrayList<>(yVal_final);

				Mot first = new Mot(0, 195, "commencer");        // le premier mot
				words.add(first);

				root.getChildren().add(first);
				fenetre.setScene(scene);    // lancer la scene

				maxWords = 100000;
				multiplierAdd = 0.01;
				howOften = 7_000_000_000L;
				howFast = 1_650_000_000;
				howMany = 3;
				timeLeft = 25;

				FXScenes.conditionVal.setText("3");

				/* calcule du CPM */
				game_timer = new AnimationTimer() {

					private long lastUpdate = 0;

					@Override
					public void handle(long now) {

						if (pause) {
							lastUpdate = now;
						}

						/* a chaque 1s */
						if (now - lastUpdate >= 1_000_000_000) {



							/* calcule du CPM */
							totalSeconds = (now - startTime) / 1_000_000_000l;
							double minutes = totalSeconds / 60;
							int calc = (int) Math.round(typedChars / minutes);

							/* on saute le 1em mot*/
							if (calc > -1 && typedWords > 1) {
								CPMs.add(calc);
								FXScenes.CPM.setText(String.valueOf(calc));
								if (calc > 350) FXScenes.CPM.setStyle("-fx-fill: linear-gradient(#FFA211, #FFD511);");  /* >350 */
								else FXScenes.CPM.setFill(
										(calc > 250) ? Color.GREEN :        /* 250-350 */
												(calc > 200) ? Color.YELLOW :        /* 200-250 */
														(calc > 150) ? Color.ORANGE :        /* 150-200 */
																Color.RED                         /* <150 */
								);
							}
							lastUpdate = now;
						}
					}

				};

				animation_background = Utilitaires.getBackgroundTimer(790, 398, root);
				animation_background.start();

				/* animation des mots */
				animation_words = new AnimationTimer() {

					private long lastUpdate = 0;
					private long lastUpdate2 = 0;

					int strike = 0;

					@Override
					public void handle(long now) {

						if (now - lastUpdate >= howFast) {
							List<Mot> del = new ArrayList<>();        // liste des mots a supprimer
							boolean gameOver = false;

							for (Mot w : words) {

								if (curtain) {
									break;
								}
								w.moveForward();    // avancer les mots
								w.toFront();

								double xPos = w.getTranslateX();
								if (xPos > 805) {    // si le mot depasse la taille de la fenetre
									multiplier = 1;
									root.getChildren().remove(w);    // supprimer tous les mots
									del.add(w);    // ajouter le mots a la liste a supprimer
									FXScenes.conditionVal.setText(String.valueOf(3 - (++strike)));    // mettre a jour missed et incrementer les strikes
									if (strike == 3) {
										gameOver = true;
										break ;
									}
								}
								if (xPos > maxWordLen) {
									fresh.remove(w);
								}
							}

							if (gameOver) {
								root.getChildren().removeAll(words);    // supprimer tous les objets
								pre_end(root);
							} else {
								words.removeAll(del);
							}

							if (words.isEmpty()) { // si il n'y a aucun mot

								if (!curtain) {
									if (typedWords == 0) {
										pre_end(root);
									} else {    // sinon on genere d'autres mots
										for (int i = 0; i < howMany; i++) {
											Mot word = createWord(strings, xVal_final, yVal_final, fresh);
											fresh.add(word);
											words.add(word);
											root.getChildren().add(word);
										}
									}
								}
							}
							lastUpdate = now;
						}

						if (now - lastUpdate2 >= howOften && typedWords > 4) {
							for (int i = 0; i < howMany; i++) {
								if (words.size() < maxWords) {
									Mot word = createWord(strings, xVal_final, yVal_final, fresh);
									fresh.add(word);
									words.add(word);
									root.getChildren().add(word);
								}
							}
							lastUpdate2 = now;
						}
					}

				};
				animation_words.start();

				FXScenes.input.setOnKeyPressed(e -> {

					switch (e.getCode()) {
						case ESCAPE:

							if (!pause) {
								animation_words.stop();
								pauseTime = System.nanoTime();

								FXScenes.input.setEditable(false);
								FXScenes.pauseBox.setVisible(true);
								FXScenes.pauseBox.toFront();
							} else {
								animation_words.start();
								startTime = System.nanoTime() - (pauseTime - startTime);

								FXScenes.input.setEditable(true);
								FXScenes.pauseBox.setVisible(false);
							}

							pause = !pause;
							break;

						case ENTER:


							List<Mot> del = new ArrayList<>();
							List<Mot> add = new ArrayList<>();
							for (Mot w : words) {

								if (w.getValue().equals(FXScenes.input.getText())) {

									timeLeft += (typedWords > 0) ? w.getLength() / 3 * multiplier : 0;
									multiplier += multiplierAdd;
									typedWords++;


									typedChars += w.getValue().length();

									fresh.remove(w);
									del.add(w);
									root.getChildren().remove(w);

									FXScenes.pointsVal.setText(String.valueOf(Math.round(points)));

									switch (typedWords) {
										case 1:
											startTime = System.nanoTime();
											game_timer.start();
											for (int i = 0; i < howMany; i++) {
												Mot word = createWord(strings, xVal_final, yVal_final, fresh);
												fresh.add(word);
												add.add(word);
												root.getChildren().add(word);
											}
											break;

										case 3:
											for (int i = 0; i < 3; i++) {
												Mot word = createWord(strings, xVal_final, yVal_final, fresh);
												fresh.add(word);
												add.add(word);
												root.getChildren().add(word);
											}
											break;

										default:
											if (typedWords % 50 == 0) {
												points += 1;
												howMany += 1;
												if (howMany > 10) {
													howMany = 10;
												}
												howFast = 1_500_000_000 / (long) points;
											}
											if (typedWords % 10 == 0) {
												for (int i = 0; i < howMany; i++) {
													Mot word = createWord(strings, xVal_final, yVal_final, fresh);
													fresh.add(word);
													add.add(word);
													root.getChildren().add(word);
												}
											}
											break;
									}
								}
							}
							words.addAll(add);    // ajouter les mots
							words.removeAll(del);    // supprimer les mots
							FXScenes.input.clear(); // vider les labels
							break;

						default:
							break;
					}
				});
			}


		}
	}
}