package application;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class VueSecondaire extends VBox {
    List<HBox> Lines = new ArrayList<>();
    List<String> letters  = new ArrayList<>();
    List<Text> Letters = new ArrayList<>();
    List<Integer> Lengths = new ArrayList<>();
    List<File> files;
    private int typedLetters = 0;
    private int correctLetters = 0;
    private int line_num = 0;
    private int counter = 0 ;
    private int maxLines = 100;
    private int acc;
    private int timeLeft = 61;
    private boolean lastCorrect;
    private  Timer tr;
    public VueSecondaire(List<String> words) {
        Random generator = new Random();
        generator.nextInt(words.size());
        tr = new Timer();
        for(int i=0;i<maxLines;i++){
            HBox Line = new HBox();
            Line.setAlignment(Pos.CENTER_LEFT);
            Line.setTranslateX(25);
            Line.setTranslateY(100);
            if (i>3){
                Line.setVisible(false);
            }
            String build = "";
            while (build.length()<50){
                build += words.get(generator.nextInt(words.size()));
                build += " ";
            }
            build = build.trim();
            Lengths.add(build.length());
            for (char c: build.toCharArray()){
                Text letter = new Text(String.valueOf(c) + "");
                letter.setFont(Font.font("Courier new", 20));
                letter.setFill(Color.DARKGREY);
                Letters.add(letter);
                letters.add(String.valueOf(c));

                Line.getChildren().add(letter);
            }
            Lines.add(Line);
            getChildren().add(Line);
            
        }
        // calcule du CPM
        tr.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                try {
                    System.setOut(new PrintStream(new FileOutputStream("/dev/null")));
                    System.setErr(new PrintStream(new FileOutputStream("/dev/null")));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                timeLeft -= 1;
                updateTimer();
                if (timeLeft == 0 ) {
                    tr.cancel();
                    VuePrincipale.gameMode = 2;
                    VuePrincipale.points = correctLetters*acc/100;
                    VuePrincipale.lancerJeu(files);
                }
            }
        }, 0, 1000);

    }

  public void handleKeyPress(String letter) {


        String c = letters.get(typedLetters);
        if (((int) letter.charAt(0) == 8) && (typedLetters >0)){
            typedLetters-=1;
            counter -= 1;
            Letters.get(typedLetters).setFill(Color.DARKGREY);
            if (lastCorrect){
                correctLetters-=1;
            }

        }else{

        if (letter.equals(c)) {
            /*si les lettre sont egales la couler est jaune*/
            Letters.get(typedLetters).setFill(Color.DARKGOLDENROD);
            correctLetters += 1;
            lastCorrect = true;
            if((Lengths.get(line_num)-1)==counter){
                getChildren().remove(0);
                line_num++;
                if (line_num<maxLines-3){Lines.get(3+line_num).setVisible(true);}
                counter = -1;
            }
        } else {
            /*sinon on va coloer avec le rouge*/
            Letters.get(typedLetters).setFill(Color.RED);
            lastCorrect = false;
            if((Lengths.get(line_num)-1)==counter){
                getChildren().remove(0);
                line_num++;
                if (line_num<maxLines-3){Lines.get(3+line_num).setVisible(true);}
                counter = -1;

            }


        }
        typedLetters++;
        counter +=1;
        }
        // calcule de la precision
        acc = (int) (((float) correctLetters/typedLetters )*100);
        FXScenes.accText.setText(String.format("%d", acc)+"%");

        if (acc > 70){
            FXScenes.accText.setFill(Color.GREEN);
        } else if (acc>30) {
            FXScenes.accText.setFill(Color.YELLOW);

        }else {
            FXScenes.accText.setFill(Color.RED);
        }

        FXScenes.CPMNormal.setText( String.format("%d",(int)((float)(correctLetters*60)/(60-timeLeft))));


    }

    public void updateTimer(){
        // mettre a jour le timer
        FXScenes.timeLeft.setText(String.format("%d", timeLeft));
        if (timeLeft == 9){
            FXScenes.timeLeft.setFill(Color.RED);
        }
        FXScenes.CPMNormal.setText( String.format("%d",(int)((float)(typedLetters*60)/(60-timeLeft))));
    }

}

