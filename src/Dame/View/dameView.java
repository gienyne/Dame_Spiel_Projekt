package Dame.View;
import Dame.Controller.IdameController;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Zeichnet auf dem Bildchirm
 */

public class dameView extends PApplet implements IdameView{

    /**
     *variable Controller vom typ IdameController
     */
    private IdameController controller;

    /**
     * die Variable Taille stellt die Länge des Arrays dar (also unser SpielBrett)
     */
    private static final int TAILLE = 8;
    /**
     * TailleCase stellt die Grösse jedes Feldes dar
     */
   private int TailleCase;

    /**
     * steht für das Bild der Dame
     */

   PImage dames;
    /**
     * steht für den ersten Player
     */

   PImage flex;

    /**
     * steht für den zweiten Player
     */
   PImage vole;

    /**
     * steht für das StartBild
     */
   PImage startScreen;
    /**
     * steht für das Bild am Ende beim GameOver
     */
   PImage gameOverImage;

    /**
     * steht für das Bild der Timer
     */
   PImage bildTimer;
    /**
     * view
     * @param width width vom Spiel
     * @param height height vom Spiel
     */
   public dameView(int width, int height){
       setSize(width, height);
   }

    /**
     * setController initialisiert die vAariable controller
     * @param controller controller
     */
   public void setController(IdameController controller){
       this.controller = controller;
   }

    /**
     * die Methode legt die größe des Bilschirm fest
     */
    public void settings(){
        size(1060, 800);

    }

    /**
     * dient zur Initialisierung der Variable im view
     */
    public void setup(){
   TailleCase = (600) / TAILLE;
   dames = loadImage("images/dames.png");
   flex = loadImage("images/flex.png");
   vole = loadImage("images/vole.png");
   startScreen = loadImage("images/startScreen.png");
   gameOverImage = loadImage("images/gameOver.png");
   bildTimer = loadImage("images/timer.png");


    }

    /**
     * draw the Game
     */
    public void draw(){

    controller.handleDisplay();

    }

    /**
     * zeichnet das SpielBrett sowie die möglichen Bewegungen der SpielSteine und damen
     */
    public void dessinerTableau() {
        int start = 100;

        boolean bouffePossible = controller.getPrisePossible();

        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                if ((i + j) % 2 == 0) {
                    fill(255);
                } else {
                    fill(40, 40, 40);
                }

                rect(i * TailleCase, start + j * TailleCase, TailleCase, TailleCase);
            }
        }

        if (controller.getSelectedX() != -1 && controller.getSelectedY() != -1) {
            if (!controller.getIsDame()[controller.getSelectedX()][controller.getSelectedY()]) {
                int[][] deplacements = controller.getPossibleMovePion(controller.getSelectedX(), controller.getSelectedY(), controller.getactuelPlayer());

            for (int[] deplacement : deplacements) {
                int x = deplacement[0];
                int y = deplacement[1];

                boolean capturePossibleAtPosition = bouffePossible && controller.MoveandBouffeCtrl(x, y, controller.getactuelPlayer());

                fill(capturePossibleAtPosition ? 255 : 0, capturePossibleAtPosition ? 0 : 255, 0, capturePossibleAtPosition ? 255 : 100);
                rect(x * TailleCase, start + y * TailleCase, TailleCase, TailleCase);
                if (capturePossibleAtPosition) {
                    int[][] prisesMultiples = controller.getPossibleMovePionAfterCapture(x, y, controller.getactuelPlayer());

                    for (int[] prise : prisesMultiples) {
                        int x2 = prise[0];
                        int y2 = prise[1];

                        fill(255, 0, 0);
                        rect(x2 * TailleCase, start + y2 * TailleCase, TailleCase, TailleCase);

                        int[][] autresprisesMultiples = controller.getPossibleMovePionAfterCapture(x2, y2, controller.getactuelPlayer());

                        for (int[] autresprises : autresprisesMultiples) {
                            int x3 = autresprises[0];
                            int y3 = autresprises[1];

                            fill(255, 0, 0);
                            rect(x3 * TailleCase, start + y3 * TailleCase, TailleCase, TailleCase);
                        }

                    }


                }
            }
        }

            if ( controller.getIsDame()[controller.getSelectedX()][controller.getSelectedY()]){
               // System.out.println("j'Aime le porc" + controller.getIsDame()[controller.getSelectedX()][controller.getSelectedY()]);
                int[][] deplacementsDame = controller.possibleMoveDame(controller.getSelectedX(), controller.getSelectedY(), controller.getactuelPlayer());
            for (int[] deplacement : deplacementsDame) {
                int xd = deplacement[0];
                int yd = deplacement[1];
                fill(0 , 255, 100);
                ellipse((float)((xd + 0.5) * TailleCase), (float)((yd + 0.5) * TailleCase + start), (float)(TailleCase / 6), (float)(TailleCase / 6));

            }
        }
        }
    }

    /**
     * zeichnet die SpielSteine auf dem SpielBrett
     */
   public void dessinerPion(){
    int start = 100;

       float xOffset = (float)((TailleCase - dames.height) / 2);
       float yOffset = (float)((TailleCase - dames.height) / 2);

        for(int i = 0; i < controller.getTableau().length; i++){
            for(int j = 0; j < controller.getTableau().length; j++){
                if(controller.getTableau()[i][j] == 'X'){

                        fill(100, 100, 100);
                        ellipse((float) (  j * TailleCase + TailleCase / 2), (float) (start + i * TailleCase + TailleCase / 2), (float) (TailleCase * 0.8), (float) (TailleCase * 0.8));

                }
                else if(controller.getTableau()[i][j] == 'O'){

                        fill(255);
                        ellipse((float) (  j * TailleCase + TailleCase / 2), (float) (start +  i * TailleCase + TailleCase / 2), (float) (TailleCase * 0.8), (float) (TailleCase * 0.8));
                }
                else if (controller.getTableau()[i][j] == 'd' ) {

                    fill(255);
                    ellipse((float)(j * TailleCase + TailleCase / 2), (float)(start + i * TailleCase + TailleCase / 2), (float)(TailleCase * 0.8), (float)(TailleCase * 0.8));
                    ellipse((float) (  j * TailleCase + TailleCase / 2 + 0.4), (float) (start +  i * TailleCase + TailleCase / 2 - 0.4 ), (float) (TailleCase * 0.6), (float) (TailleCase * 0.6));

                    image(dames, j * TailleCase + xOffset , start + i * TailleCase + yOffset, dames.height, dames.height );
                }
                else if (controller.getTableau()[i][j] == 'D' ) {

                    fill(100, 100, 100);
                    ellipse((float)(j * TailleCase + TailleCase / 2), (float)(start + i * TailleCase + TailleCase / 2), (float)(TailleCase * 0.8), (float)(TailleCase * 0.8));
                    ellipse((float) (  j * TailleCase + TailleCase / 2 + 0.4), (float) (start +  i * TailleCase + TailleCase / 2 - 0.4 ), (float) (TailleCase * 0.6), (float) (TailleCase * 0.6));

                    image(dames, j * TailleCase + xOffset , start + i * TailleCase + yOffset, dames.height, dames.height );

                }
            }

        }
    }

    /**
     * zeichnet das Startbild im Bildschirm
     */
    public void drawStartScreen (){
       fill(255);
       textSize(48);

       imageMode(CORNER);
       image(startScreen, 0, 0, width, height);
        String messageTop = "Let's DameSpiel";
        float xTop = width/2 - textWidth(messageTop)/2;
        float yTop = 60;
        text(messageTop, xTop, yTop);
        String messageBottom = "Press SPACE to start the game";
        float xBottom = width/2 - textWidth(messageBottom)/2;
        float yBottom =  height - 100;

        text(messageBottom, xBottom, yBottom);
    }

    /**
     * zeichen das EndBild im BildSchirm
     */
    public void drawOverscreen(){

        fill(46, 204, 113);
        textSize(80);
       imageMode(CORNER);
       image(gameOverImage, 0, 0, width, height);
        String messageBottomOver = "Game Over";
        float xBottomOver = width/2 - textWidth(messageBottomOver)/2;
        float yBottomOver =  60;

        text(messageBottomOver, xBottomOver, yBottomOver);

        textSize(48);
        String messageBottom = "Press SPACE to start a new game";
        float xBottom = width/2 - textWidth(messageBottom)/2;
        float yBottom =  height - 70;
        text(messageBottom, xBottom, yBottom);

        if(controller.getWinnerBeiGameOver() == 'X'){
            System.out.println("getWinner: " + controller.getWinnerBeiGameOver());
            text("********** PLAYER1 WON **********", (width / 2) - textWidth("********** PLAYER1 WON **********")/2, height - 500);
        }
        else if (controller.getWinnerBeiGameOver() == 'O'){
            System.out.println("getWinner: " + controller.getWinnerBeiGameOver());
            text("********** PLAYER2 WON **********", (width / 2) - textWidth("********** PLAYER2 WON ********** ")/2, height - 500);
        }

        else {
            text("********** DRAW.. NO TEAM WON ..*********", (width / 2) - textWidth("********** DRAW... NO TEAM WON ********** ")/2, height - 500);
        }

    }

    /**
     * zeichnet den decor also verschiedene Spieler, Zeile und Farben
     */
    public void decor(){
       fill(100, 100, 100);
        rect(0, 0, width, height);
        fill(0);
        rect(0, 0, 600, height);
        vole.resize(100, 90);
        flex.resize(100, 90);
        image(flex, 250, 9);
        image(vole, 250, 705);
        dames.resize(28, 28);
        line(600, height, 600, 10);

        if (controller.getactuelPlayer() == 'X' || controller.getactuelPlayer() == 'D'){
            textSize(20);
            fill(0);
            text("it's the grey player's turn to play.", 680, 160);

        }
        else if(controller.getactuelPlayer() == 'O' || controller.getactuelPlayer() == 'd'){
            textSize(20);
            fill(0);
            text("it's the white player's turn to play.", 680, 160);

        }

    }

    /**
     * enthält und zeichnet alle notwendigen Infos über den Player1
     */
    public void infoPlayer1(){
        textSize(20);
        fill(255, 0, 0);
        rect(685, 185, 30, 30);
        fill(150);
        ellipse(700, 200, 30, 30);

        text("Player 1 : " , 665, 260);
        text("Nombre de pion: " + controller.getNbrPionPlayer1(), 620, 310);
        text("Anzahl der Dame: " + controller.getNbrDamePlayer1(), 620, 360);
        text("Runde gewonnen: " + controller.getVictoirePlayer1(), 620, 410);
    }

    /**
     * enthält und zeichnet alle notwendige infos über den Player2
     */
    public void infoPlayer2(){
        textSize(20);
        fill(255, 0, 0);
        rect(915, 185, 30, 30);
        fill(255);
        ellipse(930, 200, 30, 30);

        text("Player 2 : " , 898, 260);
        text("Nombre de pion: " + controller.getNbrPionPlayer2(), 860, 310);
        text("Anzahl der Dame: " + controller.getNbrDamePlayer2(), 860, 360);
        text("Runde gewonnen: " + controller.getVictoirePlayer2() , 860, 410);

    }

    /**
     * enthält und zeichnet alle infos, die sowohl  den Spieler1 und Spieler2 betreffen
     */
    public void infoPlayer1Et2(){
       fill(0);
        textSize(22);
       text("Gesamtpunktzahl : " + controller.getVictoirePlayer1() + " - " + controller.getVictoirePlayer2(), 725, 470);
       text("Runde : " + controller.getManche(), 777, 510);
    }

    /**
     * draw den Timer im Bildschirm
     */
     public void drawTimer(){
       int minute = 0;
       int seconde = 0;

      fill(255);

       bildTimer.resize(55, 55);
       image(bildTimer, 790, 25);

         if(controller.getRemainingTimer() > 60){
             minute = controller.getRemainingTimer() / 60;
             seconde = controller.getRemainingTimer() % 60;
             text("verbleibende Zeit: " + minute + "min " + seconde + "s", 710, 115);
         }
         else if(controller.getRemainingTimer() <= 60){
             text("verbleibende Zeit: " + controller.getRemainingTimer() + "s", 724, 115);
         }
     }


    /**
     * gibt die Größe eines Feldes zurück
     * @return TailleCase
     */
    public int getTailleCase(){
       return TailleCase;
    }

    /**
     * Mauseingabe des Benutzers, die an den Controller weitergeleitet wird.
     */

    public void mousePressed(){
        if(mouseX >= 0 && mouseX <= 600 && mouseY >=  100 && mouseY <= height - 100) {
            controller.userInput(mouseX, mouseY);
        }
    }


    /**
     * Tastatureingabe des Benutzers, die an den Controller weitergeleitet wird.
     */
    public  void keyPressed(){
        controller.handleUserInput(key);

    }

}
