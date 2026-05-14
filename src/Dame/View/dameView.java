package Dame.View;

import Dame.Controller.IdameController;
import Dame.Model.GameMode;
import Dame.Model.PieceType;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Konkrete Implementierung der View im MVC-Muster, basierend auf Processing.
 *
 * Diese Klasse ist ausschliesslich für die Darstellung zuständig.
 * Sie enthaelt keine Spiellogik. Alle Spielzustands-Informationen werden
 * über den Controller abgefragt.
 *
 * Processing ruft {@link #draw} automatisch 60-mal pro Sekunde auf.
 * Maus- und Tastatureingaben werden an den Controller weitergeleitet.
 * Mausklicks auf dem Modusauswahlbildschirm werden direkt hier abgefangen,
 * da die View die Pixelkoordinaten ihrer eigenen Elemente kennt.
 *
 * @author Dimzz
 * @version 2.0
 * @see IdameView
 * @see IdameController
 */
public class dameView extends PApplet implements IdameView {

    /** Anzahl der Felder pro Reihe und Spalte. */
    private static final int TAILLE           = 8;

    /** Breite des Spielfelds in Pixeln. */
    private static final int PLATEAU_LARGEUR  = 600;

    /** Vertikaler Abstand des Spielfelds vom oberen Fensterrand in Pixeln. */
    private static final int PLATEAU_OFFSET_Y = 100;

    /** Pixelgröße einer Spielfeldkachel. */
    private static final int TAILLE_CASE      = PLATEAU_LARGEUR / TAILLE;

    /** Gesamtbreite des Anwendungsfensters in Pixeln. */
    private static final int FENETRE_LARGEUR  = 1060;

    /** Gesamthöhe des Anwendungsfensters in Pixeln. */
    private static final int FENETRE_HAUTEUR  = 800;

    /** Referenz auf den Controller. Wird nach der Erstellung gesetzt. */
    private IdameController controller;

    /** Bild der Krone, die auf Damen angezeigt wird. */
    private PImage imgCouronne;

    /** Bild für Spieler 1. */
    private PImage imgPlayer1;

    /** Bild für Spieler 2 .*/
    private PImage imgPlayer2;

    /** Bild der Sanduhr für die Timer-Anzeige. */
    private PImage imgTimer;

    /**
     * Erstellt eine neue View mit der angegebenen Fenstergroesse.
     *
     * @param width  Fensterbreite in Pixeln (wird intern durch FENETRE_LARGEUR überschrieben)
     * @param height Fensterhöhe in Pixeln (wird intern durch FENETRE_HAUTEUR überschrieben)
     */
    public dameView(int width, int height) { setSize(width, height); }

    /**
     * Setzt den Controller, über den die View Spielzustands-Informationen abruft.
     * Muss vor dem ersten draw()-Aufruf gesetzt werden.
     *
     * @param controller der Controller dieser View
     */
    public void setController(IdameController controller) { this.controller = controller; }

    /** Legt die Fenstergröße für Processing fest. Wird vor setup() aufgerufen. */
    @Override public void settings() { size(FENETRE_LARGEUR, FENETRE_HAUTEUR); }

    /**
     * Lädt und skaliert alle Bilder einmalig beim Start.
     * Wird von Processing automatisch einmal aufgerufen, bevor draw() startet.
     */
    @Override
    public void setup() {
        imgCouronne    = loadImage("images/dames.png");
        imgPlayer1     = loadImage("images/flex.png");
        imgPlayer2     = loadImage("images/vole.png");
        imgTimer       = loadImage("images/timer.png");

        imgCouronne.resize(TAILLE_CASE / 2, TAILLE_CASE / 2);
        imgPlayer1.resize(100, 90);
        imgPlayer2.resize(100, 90);
        imgTimer.resize(55, 55);
    }

    /** Wird 60-mal pro Sekunde aufgerufen. Delegiert die Zeichenlogik an den Controller. */
    @Override public void draw() { controller.handleDisplay(); }

    /**
     * Verarbeitet Mausklicks.
     * Klicks auf dem Modusauswahlbildschirm werden hier direkt ausgewertet,
     * da die View die genauen Koordinaten ihrer Karten kennt.
     * Alle anderen Klicks werden an den Controller weitergeleitet.
     */
    @Override
    public void mousePressed() {
        if (controller.getGameState() == Dame.Model.Gamestate.MODE_SELECT) {

            float carteW = 340, carteH = 280, gap = 40;
            float startX = (FENETRE_LARGEUR - (carteW * 2 + gap)) / 2f;
            float carteY = FENETRE_HAUTEUR * 0.38f;

            if (mouseX >= startX && mouseX <= startX + carteW
                    && mouseY >= carteY && mouseY <= carteY + carteH) {
                controller.selectionnerMode(Dame.Model.GameMode.PVP);
                return;
            }
            float startXPVE = startX + carteW + gap;
            if (mouseX >= startXPVE && mouseX <= startXPVE + carteW
                    && mouseY >= carteY && mouseY <= carteY + carteH) {
                controller.selectionnerMode(Dame.Model.GameMode.PVE);
                return;
            }
            return;
        }
        controller.handleMouseInput(mouseX, mouseY);
    }

    /** Leitet Tastatureingaben an den Controller weiter. */
    @Override public void keyPressed() { controller.handleKeyInput(key); }

    // Titelbildschirm

    /**
     * Zeichnet den Titelbildschirm mit Hintergrundbild und Anweisungstext.
     */
    @Override
    public void drawStartScreen() {
        // Fond
        background(12, 8, 22);
        drawCheckerBg(5);

        // Purple central glow
        noStroke();
        for (int i = 8; i > 0; i--) {
            fill(100, 40, 200, 10);
            ellipse(FENETRE_LARGEUR / 2f, FENETRE_HAUTEUR / 2f,
                    i * 120, i * 80);
        }

        // Color bar at the top
        fill(110, 50, 210);
        rect(0, 0, FENETRE_LARGEUR, 5);

        // Scattered decorative pawns
        float midX = FENETRE_LARGEUR / 2f;
        float midY = FENETRE_HAUTEUR / 2f;
        drawPionDeco(midX - 220, midY + 20,  36, false);
        drawPionDeco(midX - 140, midY + 55,  28, false);
        drawPionDeco(midX + 110, midY + 2,  32, false);
        drawPionDeco(midX + 170, midY - 45,  24, false);
        drawPionDeco(midX + 210, midY + 15,  32, true);
        drawPionDeco(midX - 172, midY - 40,  24, true);
        drawPionDeco(midX + 140, midY + 60,  24, true);
        drawPionDeco(midX - 105, midY - 10,  32, true);

        // Central queen (larger)
        drawDameDeco(midX, midY + 10, 52);

        // Decorative line
        stroke(110, 50, 210, 100);
        strokeWeight(0.8f);
        line(midX - 220, midY - 95, midX + 220, midY - 95);
        noStroke();
        fill(160, 110, 240);
        ellipse(midX, midY - 95, 6, 6);
        fill(110, 50, 210);
        ellipse(midX - 220, midY - 95, 4, 4);
        ellipse(midX + 220, midY - 95, 4, 4);

        // Title
        textAlign(CENTER, CENTER);
        fill(255);
        textSize(64);
        text("DAMESPIEL", midX, midY - 140);
        fill(160, 110, 240);
        textSize(14);
        text(" CHECKERS ", midX, midY - 85);

        // Space  Button
        noFill();
        stroke(110, 50, 210, 180);
        strokeWeight(1.5f);
        rect(midX - 180, midY + 110, 360, 44, 22);
        noStroke();
        fill(110, 50, 210, 40);
        rect(midX - 178, midY + 112, 356, 40, 21);
        fill(180, 140, 255);
        textSize(13);
        text("PRESS SPACE TO START", midX, midY + 132);

        // Version
        fill(50, 45, 75);
        textSize(11);
        text("v1.0  ·  Dimzz_Bot ", midX, FENETRE_HAUTEUR - 20);

        textAlign(LEFT, BASELINE);
    }

    // Modusauswahlbildschirm

    /**
     * Zeichnet den Modusauswahlbildschirm mit zwei interaktiven Karten.
     * Der dekorative Damier-Hintergrund ist thematisch passend.
     * Die Kartenpositionen definieren gleichzeitig die Klickzonen in mousePressed().
     */
    @Override
    public void drawModeSelectScreen() {
        background(12, 11, 18);

        // spielbrett
        for (int col = 0; col < TAILLE; col++) {
            for (int ligne = 0; ligne < TAILLE; ligne++) {
                float tileSize = FENETRE_LARGEUR / (float) TAILLE;
                if ((col + ligne) % 2 == 0) fill(255, 255, 255, 6);
                else fill(0, 0, 0, 0);
                noStroke();
                rect(col * tileSize, ligne * (FENETRE_HAUTEUR / (float) TAILLE),
                        tileSize, FENETRE_HAUTEUR / (float) TAILLE);
            }
        }

        // Purple bar at the top
        fill(110, 50, 210);
        rect(0, 0, FENETRE_LARGEUR, 4);

        // Title
        textAlign(CENTER, CENTER);
        fill(255);
        textSize(64);
        text("DAMESPIEL", FENETRE_LARGEUR / 2f, FENETRE_HAUTEUR * 0.14f);

        // Decorative line under the title
        stroke(110, 50, 210, 80);
        strokeWeight(0.8f);
        line(FENETRE_LARGEUR / 2f - 200, FENETRE_HAUTEUR * 0.22f,
                FENETRE_LARGEUR / 2f + 200, FENETRE_HAUTEUR * 0.22f);
        noStroke();
        fill(160, 110, 240);
        ellipse(FENETRE_LARGEUR / 2f, FENETRE_HAUTEUR * 0.22f, 5, 5);
        fill(110, 50, 210);
        ellipse(FENETRE_LARGEUR / 2f - 200, FENETRE_HAUTEUR * 0.22f, 3, 3);
        ellipse(FENETRE_LARGEUR / 2f + 200, FENETRE_HAUTEUR * 0.22f, 3, 3);

        // Subtitle
        fill(100, 100, 130);
        textSize(13);
        text("SELECT YOUR MODE TO BEGIN", FENETRE_LARGEUR / 2f, FENETRE_HAUTEUR * 0.27f);

        // Cart - more compact, SVG-style
        float carteW = 340, carteH = 260, gap = 40;
        float startX = (FENETRE_LARGEUR - (carteW * 2 + gap)) / 2f;
        float carteY = FENETRE_HAUTEUR * 0.33f;

        // - Carte PVP -
        drawCarteSvgStyle(startX, carteY, carteW, carteH,
                "1", "PVP", "Player vs Player",
                "Challenge a friend.\nTwo minds, one board.",
                color(124, 58, 237));   // purple

        // PVP tokens on top
        float pvpMidX = startX + carteW / 2f;
        drawPionDeco(pvpMidX - 45, carteY + 160, 20, false); // gray
        fill(110, 50, 210); textSize(16); textAlign(CENTER, CENTER);
        text("VS", pvpMidX, carteY + 160);
        drawPionDeco(pvpMidX + 45, carteY + 160, 20, true);  // white

        // - Carte PVE -
        drawCarteSvgStyle(startX + carteW + gap, carteY, carteW, carteH,
                "2", "PVE", "Player vs AI",
                "Challenge the Dimzz_Bot.\nCan you beat my Bot?",
                color(14, 165, 233));

        // Pawn + Queen on top
        float pveMidX = startX + carteW + gap + carteW / 2f;
        drawPionDeco(pveMidX - 45, carteY + 160, 20, true);  // white
        fill(110, 50, 210); textSize(16); textAlign(CENTER, CENTER);
        text("VS", pveMidX, carteY + 160);
        drawDameDeco(pveMidX + 45, carteY + 160, 20);         // Purple queen
        // Footer
        fill(55, 55, 80);
        textSize(12);
        text("Use keyboard  [ 1 ]  or  [ 2 ]  to select instantly",
                FENETRE_LARGEUR / 2f, FENETRE_HAUTEUR * 0.93f);

        textAlign(LEFT, BASELINE);
    }

    /**
     * Carte style SVG.
     */
    private void drawCarteSvgStyle(float x, float y, float w, float h,
                                   String key, String tag, String titre,
                                   String desc, int accent) {

        int fondTinte = color(14, 14, 32);

        // Drop shadow
        noStroke();
        fill(0, 0, 0, 70);
        rect(x + 5, y + 5, w, h, 14);

        // Bottom of the map
        fill(fondTinte);
        rect(x, y, w, h, 12);

        // Thin colored border
        noFill();
        stroke(accent, 90);
        strokeWeight(1.2f);
        rect(x, y, w, h, 12);
        noStroke();

        // Left vertical bar
        fill(accent);
        rect(x, y + 25, 3, h - 50, 2);

        // Badge tag (PVP / PVE)
        fill(accent, 200);
        rect(x + 18, y + 18, 48, 22, 5);
        fill(255);
        textSize(11);
        textAlign(CENTER, CENTER);
        text(tag, x + 42, y + 29);

        // Title
        fill(240, 240, 250);
        textSize(24);
        textAlign(LEFT, TOP);
        text(titre, x + 18, y + 52);

        // Separator
        stroke(accent, 35);
        strokeWeight(0.8f);
        line(x + 18, y + 88, x + w - 18, y + 88);
        noStroke();

        // Description
        fill(120, 120, 155);
        textSize(13);
        String[] lignes = desc.split("\n");
        for (int i = 0; i < lignes.length; i++) {
            text(lignes[i], x + 18, y + 98 + i * 20);
        }

        // OUTLINE button (not solid - like SVG)
        noFill();
        stroke(accent, 160);
        strokeWeight(1.2f);
        rect(x + 18, y + h - 52, w - 36, 36, 8);
        noStroke();
        // Slight background tint on the button
        fill(accent, 25);
        rect(x + 19, y + h - 51, w - 38, 34, 7);
        // Button text
        fill(accent);
        textSize(12);
        textAlign(CENTER, CENTER);
        text("Press  [ " + key + " ]  to play", x + w / 2f, y + h - 34);

        textAlign(LEFT, BASELINE);
    }

    // Spielende-Bildschirm

    /**
     * Zeichnet den Spielende-Bildschirm mit Gewinnernachricht.
     * Im PVE-Modus wird fuer Spieler 2 "AI WON" statt "PLAYER 2 WON" angezeigt.
     */
    @Override
    public void drawGameOverScreen() {
        background(14, 6, 6);
        // Very dark red checkerboard pattern
        noStroke();
        for (int col = 0; col < TAILLE * 2; col++) {
            for (int ligne = 0; ligne < TAILLE * 2; ligne++) {
                float tW = FENETRE_LARGEUR / (float)(TAILLE * 2);
                float tH = FENETRE_HAUTEUR / (float)(TAILLE * 2);
                if ((col + ligne) % 2 == 0) fill(255, 30, 30, 12);
                else                         fill(0, 0, 0, 0);
                rect(col * tW, ligne * tH, tW, tH);
            }
        }
        // Red glow
        for (int i = 6; i > 0; i--) {
            fill(200, 20, 20, 12);
            ellipse(FENETRE_LARGEUR / 2f, FENETRE_HAUTEUR * 0.45f,
                    i * 150, i * 100);
        }

        // Red bar at the top
        fill(180, 20, 20);
        rect(0, 0, FENETRE_LARGEUR, 5);

        // "Fallen" pawns
        drawPionTombe(150, 330, 28, true,  -0.45f);
        drawPionTombe(100, 260, 22, false,  0.28f);
        drawPionTombe(900, 300, 26, true,  -0.20f);
        drawPionTombe(960, 255, 20, false,  0.38f);

        float midX = FENETRE_LARGEUR / 2f;

        // Title GAME OVER
        textAlign(CENTER, CENTER);
        fill(220, 50, 50);
        textSize(72);
        text("GAME OVER", midX, FENETRE_HAUTEUR * 0.14f);

        // Decorative stars
        fill(180, 30, 30);
        textSize(20);
        text("*", midX - 230, FENETRE_HAUTEUR * 0.27f);
        text("*", midX,       FENETRE_HAUTEUR * 0.26f);
        text("*", midX + 230, FENETRE_HAUTEUR * 0.27f);

        // Results box
        noFill();
        stroke(180, 30, 30, 160);
        strokeWeight(1);
        rect(midX - 200, FENETRE_HAUTEUR * 0.32f, 400, 70, 10);
        noStroke();
        fill(30, 6, 6);
        rect(midX - 199, FENETRE_HAUTEUR * 0.321f, 398, 68, 9);

        PieceType winner = controller.getWinner();
        String msgWinner;
        if (winner == PieceType.PION_J1 || winner == PieceType.DAME_J1) {
            msgWinner = "PLAYER 1 WIN";
        } else if (winner == PieceType.PION_J2 || winner == PieceType.DAME_J2) {
            boolean pve = (controller.getGameMode() == GameMode.PVE);
            msgWinner = pve ? "L'IA WIN" : "PLAYER 2 WIN";
        } else {
            msgWinner = "DRAW";
        }
        fill(230, 90, 90);
        textSize(28);
        text(msgWinner, midX, FENETRE_HAUTEUR * 0.365f);

        // Score
        fill(120, 60, 60);
        textSize(14);
        text("Joueur", midX - 110, FENETRE_HAUTEUR * 0.50f);
        text("IA / J2",  midX + 110, FENETRE_HAUTEUR * 0.50f);
        fill(160, 160, 170);
        textSize(32);
        text(controller.getVictoirePlayer1(), midX - 110, FENETRE_HAUTEUR * 0.56f);
        fill(100, 40, 40);
        textSize(22);
        text("—", midX, FENETRE_HAUTEUR * 0.55f);
        fill(220, 70, 70);
        textSize(32);
        text(controller.getVictoirePlayer2(), midX + 110, FENETRE_HAUTEUR * 0.56f);

        // Bouton
        noFill();
        stroke(180, 30, 30, 160);
        strokeWeight(1.5f);
        rect(midX - 200, FENETRE_HAUTEUR * 0.70f, 400, 44, 22);
        noStroke();
        fill(180, 30, 30, 40);
        rect(midX - 198, FENETRE_HAUTEUR * 0.701f, 396, 40, 21);
        fill(240, 130, 130);
        textSize(13);
        text("SPACE  —  CHOOSE MODE", midX, FENETRE_HAUTEUR * 0.722f);

        fill(50, 20, 20);
        textSize(11);
        text("Dimzz_Bot is relentless....", midX, FENETRE_HAUTEUR - 20f);

        textAlign(LEFT, BASELINE);
    }

    // Spielbildschirm

    /**
     * Zeichnet Hintergrund, Trennlinie und Dekorationsbilder des Spielbildschirms.
     * Zeigt ausserdem den aktuellen Zugnachricht an ("AI is thinking..." oder Spielerzug).
     */
    @Override
    public void drawDecor() {
        fill(45, 52, 70);
        rect(0, 0, width, height);
        fill(18, 24, 38);
        rect(0, 0, PLATEAU_LARGEUR, height);
        fill(255, 255, 255, 40);
        rect(PLATEAU_LARGEUR - 2, 0, 8, height);
        drawPionDeco(295, 55, 22, false);  // Player 1 = gray, top
        drawPionDeco(295, 745, 22, true);  // Player 1 = white, down
        line(PLATEAU_LARGEUR, height, PLATEAU_LARGEUR, 10);
        textSize(20);
        fill(255);
        PieceType player = controller.getActuelPlayer();
        if (player == PieceType.PION_J1 || player == PieceType.DAME_J1) {
            text("It's the grey player's turn.", 715, 160);
        } else {
            boolean pve = (controller.getGameMode() == GameMode.PVE);
            text(pve ? "AI is thinking..." : "It's the white player's turn.", 710, 160);
        }
    }

    /**
     * Zeichnet das Spielbrett (Kacheln) und hebt mögliche Züge der ausgewählten Figur hervor.
     * Schlagzüge werden rot, normale Züge grün markiert.
     * Dame-Züge werden als kleine Punkte dargestellt.
     */
    @Override
    public void drawPlateau() {
        for (int col = 0; col < TAILLE; col++) {
            for (int ligne = 0; ligne < TAILLE; ligne++) {
                fill((col + ligne) % 2 == 0 ? 255 : color(40));
                rect(col * TAILLE_CASE, PLATEAU_OFFSET_Y + ligne * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE);
            }
        }
        int selX = controller.getSelectedX();
        int selY = controller.getSelectedY();
        if (selX == -1) return;

        PieceType piece = controller.getPlateau()[selY][selX];
        boolean isDame  = (piece == PieceType.DAME_J1 || piece == PieceType.DAME_J2);
        if (!isDame) dessinerMouvementsPion(selX, selY);
        else         dessinerMouvementsDame(selX, selY);
    }

    /**
     * Zeichnet die möglichen Zielkacheln für einen Bauern.
     * Schlagzüge werden rot (mit hoher Deckkraft), normale Züge gruen markiert.
     *
     * @param selX Spalte des ausgewählten Bauern
     * @param selY Zeile des ausgewählten Bauern
     */
    private void dessinerMouvementsPion(int selX, int selY) {
        int[][] moves = controller.getPossibleMovesPion(selX, selY);
        boolean prisePossible = controller.isPrisePossible();
        for (int[] move : moves) {
            boolean isCapture = prisePossible && controller.isMoveCapture(move[0], move[1]);
            fill(isCapture ? color(255, 0, 0, 200) : color(0, 255, 0, 100));
            rect(move[0] * TAILLE_CASE, PLATEAU_OFFSET_Y + move[1] * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE);
        }
    }

    /**
     * Zeichnet die möglichen Zielkacheln für eine Dame als kleine grüne Punkte.
     *
     * @param selX Spalte der ausgewählten Dame
     * @param selY Zeile der ausgewählten Dame
     */
    private void dessinerMouvementsDame(int selX, int selY) {
        int[][] moves = controller.getPossibleMovesDame(selX, selY);
        for (int[] move : moves) {
            fill(0, 255, 100);
            ellipse((move[0] + 0.5f) * TAILLE_CASE,
                    PLATEAU_OFFSET_Y + (move[1] + 0.5f) * TAILLE_CASE,
                    TAILLE_CASE / 6f, TAILLE_CASE / 6f);
        }
    }

    /**
     * Zeichnet alle Spielfiguren auf dem Brett.
     * Bauern werden als einfarbige Ellipsen dargestellt.
     * Damen erhalten zusätzlich einen inneren Kreis und das Kronenbild.
     */
    @Override
    public void drawPions() {
        PieceType[][] plateau = controller.getPlateau();
        float offset      = TAILLE_CASE * 0.5f;
        float taillePion  = TAILLE_CASE * 0.8f;
        float tailleInner = TAILLE_CASE * 0.5f;

        for (int ligne = 0; ligne < TAILLE; ligne++) {
            for (int col = 0; col < TAILLE; col++) {
                float cx   = col * TAILLE_CASE + offset;
                float cy   = PLATEAU_OFFSET_Y + ligne * TAILLE_CASE + offset;
                float imgX = col * TAILLE_CASE;
                float imgY = PLATEAU_OFFSET_Y + ligne * TAILLE_CASE;

                switch (plateau[ligne][col]) {
                    case PION_J1 -> { drawPionDeco(cx, cy, TAILLE_CASE * 0.38f, false); }
                    case PION_J2 -> { drawPionDeco(cx, cy, TAILLE_CASE * 0.38f, true); }
                    case DAME_J1 -> {
                        drawPionDeco(cx, cy, TAILLE_CASE * 0.38f, false);
                        image(imgCouronne,
                                imgX + (TAILLE_CASE - imgCouronne.width) / 2f,
                                imgY + (TAILLE_CASE - imgCouronne.height) / 2f);
                    }
                    case DAME_J2 -> {
                        drawPionDeco(cx, cy, TAILLE_CASE * 0.38f, true);
                        image(imgCouronne,
                                imgX + (TAILLE_CASE - imgCouronne.width) / 2f,
                                imgY + (TAILLE_CASE - imgCouronne.height) / 2f);
                    }
                    default -> {}
                }
            }
        }
    }

    /**
     * Zeichnet die Spielstatistiken von Spieler 1 auf der rechten Seitenleiste.
     */
    @Override
    public void drawInfoPlayer1() {
        fill(255, 0, 0); rect(685, 185, 30, 30);
        fill(150); ellipse(700, 200, 30, 30);
        textSize(20); fill(0);
        text("Player 1 :",   665, 260);
        text("Pawns : "      + controller.getNbrPionPlayer1(),  658, 310);
        text("Dames : "      + controller.getNbrDamePlayer1(),  661, 360);
        text("Victories : "  + controller.getVictoirePlayer1(), 658, 410);
    }

    /**
     * Zeichnet die Spielstatistiken von Spieler 2 bzw. der KI auf der rechten Seitenleiste.
     * Im PVE-Modus wird "IA :" statt "Player 2 :" angezeigt.
     */
    @Override
    public void drawInfoPlayer2() {
        fill(255, 0, 0); rect(915, 185, 30, 30);
        fill(255); ellipse(930, 200, 30, 30);
        textSize(20); fill(0);
        boolean pve = (controller.getGameMode() == GameMode.PVE);
        text(pve ? "IA :" : "Player 2 :", 898, 260);
        text("Pawns : "      + controller.getNbrPionPlayer2(),  892, 310);
        text("Dames : "      + controller.getNbrDamePlayer2(),  894, 360);
        text("Victories : "  + controller.getVictoirePlayer2(), 892, 410);
    }

    /**
     * Zeichnet das gemeinsame Gesamtergebnis und die aktuelle Rundennummer.
     */
    @Override
    public void drawInfoCommune() {
        fill(0); textSize(22);
        text("Score : " + controller.getVictoirePlayer1()
                + " - " + controller.getVictoirePlayer2(), 771, 470);
        text("Round : " + controller.getManche(), 780, 510);
    }

    /**
     * Zeichnet die Timer-Anzeige mit dem Sanduhr-Bild und der verbleibenden Spielzeit.
     * Zeiten ueber 60 Sekunden werden im Format "Xmin Ys" angezeigt.
     */
    @Override
    public void drawTimer() {
        image(imgTimer, 790, 25);
        fill(255); textSize(20);
        int totalSec = controller.getRemainingTimer();
        String tempsFormate = totalSec > 60
                ? (totalSec / 60) + "min " + (totalSec % 60) + "s"
                : totalSec + "s";
        text("Time remaining : " + tempsFormate, 710, 115);
    }

    /**
     * Berechnet die X-Position, an der ein Text horizontal zentriert werden muss.
     *
     * @param msg der zu zentrierende Text
     * @return X-Koordinate für den Textanfang
     */
    private float centerText(String msg) {
        return width / 2f - textWidth(msg) / 2f;
    }

    //StartScreen , GameOverscrenn

    /**
     * Zeichnet ein halbtransparentes Damebrett-Muster als Hintergrund,
     * das auf mehreren Bildschirmen wiederverwendet wird.
     *
     * @param alphaLight Transparenz der hellen Felder (0–255)
     */
    private void drawCheckerBg(int alphaLight) {
        noStroke();
        for (int col = 0; col < TAILLE * 2; col++) {
            for (int ligne = 0; ligne < TAILLE * 2; ligne++) {
                float tileW = FENETRE_LARGEUR / (float)(TAILLE * 2);
                float tileH = FENETRE_HAUTEUR / (float)(TAILLE * 2);
                if ((col + ligne) % 2 == 0) fill(255, 255, 255, alphaLight);
                else                         fill(0, 0, 0, 0);
                rect(col * tileW, ligne * tileH, tileW, tileH);
            }
        }
    }

    /**
     * Zeichnet einen dekorativen Spielstein
     * (Splashscreen / Game-Over-Bildschirm) mit Schlagschatten.
     *
     * @param cx       Mittelpunkt X
     * @param cy       Mittelpunkt Y
     * @param r        Radius
     * @param isWhite  true = weisser Spielstein (Spieler 2),
     *                 false = grauer Spielstein (Spieler 1)
     */
    private void drawPionDeco(float cx, float cy, float r, boolean isWhite) {
        // Shadow
        noStroke();
        fill(0, 0, 0, 80);
        ellipse(cx + r * 0.15f, cy + r * 0.2f, r * 2, r * 0.6f);
        // Body
        if (isWhite) fill(220, 225, 235);
        else         fill(65,  75,  90);
        ellipse(cx, cy, r * 2, r * 2);
        // Inner Circle
        if (isWhite) fill(200, 210, 225, 160);
        else         fill(90, 105, 125, 160);
        ellipse(cx, cy - r * 0.08f, r * 1.3f, r * 1.3f);
        // Reflet
        fill(255, 255, 255, isWhite ? 60 : 40);
        ellipse(cx - r * 0.25f, cy - r * 0.3f, r * 0.7f, r * 0.45f);
    }

    /**
     * Zeichnet einen dekorativen gekippten Spielstein
     * (gefallener Effekt fuer den Game-Over-Bildschirm).
     *
     * @param cx       Mittelpunkt X
     * @param cy       Mittelpunkt Y
     * @param r        Radius
     * @param isWhite  Farbe des Spielsteins
     * @param angle    Neigungswinkel in Radiant
     */
    private void drawPionTombe(float cx, float cy, float r, boolean isWhite, float angle) {
        pushMatrix();
        translate(cx, cy);
        rotate(angle);
        noStroke();
        fill(0, 0, 0, 50);
        ellipse(r * 0.1f, r * 0.15f, r * 2, r * 0.5f);
        if (isWhite) fill(220, 225, 235, 180);
        else         fill(65, 75, 90, 180);
        ellipse(0, 0, r * 2, r * 2);
        if (isWhite) fill(200, 210, 225, 100);
        else         fill(90, 105, 125, 100);
        ellipse(0, -r * 0.08f, r * 1.3f, r * 1.3f);
        popMatrix();
    }

    /**
     * Zeichnet eine dekorative Dame (violetter Spielstein mit Krone)
     * fuer den Splashscreen.
     *
     * @param cx Mittelpunkt X
     * @param cy Mittelpunkt Y
     * @param r  Radius des Spielsteins
     */
    private void drawDameDeco(float cx, float cy, float r) {
        noStroke();
        // Shadow
        fill(0, 0, 0, 90);
        ellipse(cx + r * 0.15f, cy + r * 0.2f, r * 2.2f, r * 0.65f);
        // Purple body
        fill(110, 50, 210);
        ellipse(cx, cy, r * 2, r * 2);
        // Inner circle
        fill(155, 100, 240, 180);
        ellipse(cx, cy - r * 0.08f, r * 1.3f, r * 1.3f);
        // Crown
        float crY = cy - r * 0.55f;
        float crW = r * 1.0f;
        stroke(250, 190, 40);
        strokeWeight(2);
        fill(250, 190, 40);
        // Base of the crown
        rect(cx - crW * 0.5f, crY + r * 0.5f, crW, r * 0.22f, 2);
        noStroke();
        // peak
        triangle(cx - crW * 0.5f, crY + r * 0.22f,
                cx - crW * 0.4f + r * 0.12f, crY + r * 0.22f,
                cx - crW * 0.35f, crY);
        triangle(cx - r * 0.12f, crY + r * 0.22f,
                cx + r * 0.16f, crY + r * 0.22f,
                cx, crY - r * 0.18f);
        triangle(cx + crW * 0.5f, crY + r * 0.22f,
                cx + crW * 0.4f - r * 0.12f, crY + r * 0.22f,
                cx + crW * 0.35f, crY);
        // Small circles on the ends
        fill(255, 215, 60);
        ellipse(cx - crW * 0.37f, crY, r * 0.15f, r * 0.15f);
        ellipse(cx, crY - r * 0.18f, r * 0.15f, r * 0.15f);
        ellipse(cx + crW * 0.35f, crY, r * 0.15f, r * 0.15f);
        // Reflet
        fill(255, 255, 255, 35);
        ellipse(cx - r * 0.28f, cy - r * 0.32f, r * 0.65f, r * 0.4f);
    }


}