package application.composants;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import application.VuePrincipale;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import application.FXScenes;
import static application.VuePrincipale.fenetre;
//import app.Window;


public class Menu {
	
	public static final List<File> selected_lng_files = new ArrayList<File>();
	public static final List<String> selected_lng_names = new ArrayList<String>();
	
	public static int[] x = {4, 4, 4, 4};
	private static int row;
	private static int how_many_lngs;
	
	
	public static void selectGamemode() {
		row = 0; setHighlight(FXScenes.gamemodes);
		
		Pane root = FXScenes.selectMenu("fastClavier");
		Scene scene = new Scene(root);		
		fenetre.setScene(scene);
		
		scene.setOnKeyPressed(e -> {
			switch (e.getCode()) {
				case UP:
					row = (row > 0) ? row-1 : 1;
					setHighlight(FXScenes.gamemodes);
					break;
					
				case DOWN:
					row = (row < 1) ? row+1 : 0;
					setHighlight(FXScenes.gamemodes);
					break;
					
				case ENTER:
					VuePrincipale.gameMode = row;
					Menu.selectLanguage();
					break;
					
				default: break;			
			}
		});
		 

	}
	
	/* selecter languages */
	private static void selectLanguage() {
		
		// reset stuff
		selected_lng_files.clear();
		selected_lng_names.clear();
		row=0;
					
		if(Language.chargerLanguages() == null) {
			VuePrincipale.erreur("MISSING_WORDS");
			return;
		}
		
		Pane root = FXScenes.selectMenu("LANGUAGES");
		Scene scene = new Scene(root);
		
		fenetre.setScene(scene);
		how_many_lngs = Language.how_many_lngs;	// redefine variable to avoid `0`
				
		/* menu movement key listener */
		scene.setOnKeyPressed(e -> {
			switch (e.getCode()) {		
				case UP:
					row = (row > 0) ? row-1 : how_many_lngs-1;
					setHighlight(FXScenes.lngs);
					break;
					
				case DOWN:
					row = (row < how_many_lngs-1) ? row+1 : 0;
					setHighlight(FXScenes.lngs);
					break;
					
				case SPACE:
					if(selected_lng_files.contains(Language.listOfFiles[row])) {
						selected_lng_files.remove(Language.listOfFiles[row]);
						selected_lng_names.remove(Language.lngsNames.get(row));
					} else {
						if(!isEmpty(row)) {
							selected_lng_names.add(Language.lngsNames.get(row));
							selected_lng_files.add(Language.listOfFiles[row]);
						}
					}
					setCheck();
					break;
					
				case ENTER:
					if(!selected_lng_files.contains(Language.listOfFiles[row]) && selected_lng_files.size() == 0) {
						if(!isEmpty(row)) {
							selected_lng_names.add(Language.lngsNames.get(row));
							selected_lng_files.add(Language.listOfFiles[row]);
						}
					}
									
					if(!Language.chargerMots(selected_lng_files).isEmpty()) {
							String lngs = "";
							for(String s : selected_lng_names) {
								lngs += "{" + s + "} ";
							}

						VuePrincipale.lancerJeu(selected_lng_files);
					}
					break;
					
				case ESCAPE:
					selectGamemode();
					break;
				default: break;			
			}
			
		});
	}
	
	private static void setCheck() {
		FXScenes.lngs[row].setChecked(!FXScenes.lngs[row].getChecked());
	}
	
	private static void setHighlight(Choix[] tab) {
		int index = 0;
		for (Choix o : tab) {
			o.setHighlighted(index++ == row);
		}
	}
		
	private static boolean isEmpty(int y) {
		try {
			return !(Files.lines(Paths.get(Language.listOfFiles[y].toString())).count() > 1);
		} catch (IOException e) {
			return true;
		}
	}
}