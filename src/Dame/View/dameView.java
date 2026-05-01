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

    /** Hintergrundbild des Titelbildschirms. */
    private PImage imgStartScreen;

    /** Hintergrundbild des Spielende-Bildschirms. */
    private PImage imgGameOver;

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
        imgStartScreen = loadImage("images/startScreen.png");
        imgGameOver    = loadImage("images/gameOver.png");
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
            // Koordinaten muessen exakt mit drawModeSelectScreen() uebereinstimmen
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
        imageMode(CORNER);
        image(imgStartScreen, 0, 0, width, height);
        fill(255);
        textSize(48);
        String titre = "Let's DameSpiel";
        text(titre, centerText(titre), 60);
        String instruction = "Press SPACE to continue";
        text(instruction, centerText(instruction), height - 100);
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

        // Thematischer Damier-Hintergrund (sehr schwach sichtbar)
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

        textAlign(CENTER, CENTER);
        fill(255);
        textSize(72);
        text("DAMESPIEL", FENETRE_LARGEUR / 2f, FENETRE_HAUTEUR * 0.18f);

        fill(120, 120, 150);
        textSize(16);
        text("SELECT YOUR MODE TO BEGIN", FENETRE_LARGEUR / 2f, FENETRE_HAUTEUR * 0.28f);

        // Dynamisch berechnete Kartenpositionen (auch in mousePressed verwendet)
        float carteW = 340, carteH = 280, gap = 40;
        float startX = (FENETRE_LARGEUR - (carteW * 2 + gap)) / 2f;
        float carteY = FENETRE_HAUTEUR * 0.38f;

        drawCarteMode(startX, carteY, carteW, carteH,
                "1", "PVP", "Player vs Player",
                "Challenge a friend.\nTwo minds, one board.",
                color(34, 197, 94), color(16, 90, 42), color(20, 30, 22));

        drawCarteMode(startX + carteW + gap, carteY, carteW, carteH,
                "2", "PVE", "Player vs AI",
                "Challenge the Dimzz_Bot.\nCan you beat my Bot?",
                color(139, 92, 246), color(76, 29, 149), color(20, 18, 32));

        fill(60, 60, 80);
        textSize(13);
        text("Use keyboard  [ 1 ]  or  [ 2 ]  to select instantly",
                FENETRE_LARGEUR / 2f, FENETRE_HAUTEUR * 0.92f);

        textAlign(LEFT, BASELINE);
    }

    /**
     * Zeichnet eine einzelne Moduswahl-Karte.
     *
     * @param x          linke obere X-Koordinate der Karte
     * @param y          linke obere Y-Koordinate der Karte
     * @param w          Breite der Karte
     * @param h          Höhe der Karte
     * @param key        Taste zum Auswählen (wird als Beschriftung angezeigt)
     * @param tag        Kurzbezeichnung des Modus (z.B. "PVP")
     * @param titre      Haupttitel der Karte
     * @param desc       Beschreibungstext, Zeilenumbruch mit "\n"
     * @param accent     Akzentfarbe (Rand, Beschriftungen, CTA-Button)
     * @param accentDark dunklere Variante der Akzentfarbe für Badge-Hintergrund
     * @param fondTinte  Hintergrundfarbe der Karte
     */
    private void drawCarteMode(float x, float y, float w, float h,
                               String key, String tag, String titre,
                               String desc, int accent, int accentDark, int fondTinte) {
        noStroke();
        fill(0, 0, 0, 80);
        rect(x + 6, y + 6, w, h, 20);

        fill(fondTinte);
        rect(x, y, w, h, 18);

        stroke(accent, 60);
        strokeWeight(1);
        rect(x, y, w, h, 18);
        noStroke();

        fill(accent);
        rect(x, y + 30, 4, h - 60, 2);

        fill(accentDark);
        rect(x + 20, y + 22, 52, 24, 6);
        fill(accent);
        textSize(12);
        textAlign(CENTER, CENTER);
        text(tag, x + 46, y + 34);

        fill(235, 235, 245);
        textSize(28);
        textAlign(LEFT, TOP);
        text(titre, x + 20, y + 62);

        stroke(accent, 40);
        strokeWeight(1);
        line(x + 20, y + 104, x + w - 20, y + 104);
        noStroke();

        fill(130, 130, 160);
        textSize(14);
        String[] lignes = desc.split("\n");
        for (int i = 0; i < lignes.length; i++) {
            text(lignes[i], x + 20, y + 118 + i * 22);
        }

        fill(accent, 30);
        rect(x + 20, y + h - 56, w - 40, 36, 10);
        stroke(accent, 80);
        strokeWeight(1);
        rect(x + 20, y + h - 56, w - 40, 36, 10);
        noStroke();
        fill(accent);
        textSize(15);
        textAlign(CENTER, CENTER);
        text("Press  [ " + key + " ]  to play", x + w / 2f, y + h - 38);

        textAlign(LEFT, BASELINE);
    }

    // Spielende-Bildschirm

    /**
     * Zeichnet den Spielende-Bildschirm mit Gewinnernachricht.
     * Im PVE-Modus wird fuer Spieler 2 "AI WON" statt "PLAYER 2 WON" angezeigt.
     */
    @Override
    public void drawGameOverScreen() {
        imageMode(CORNER);
        image(imgGameOver, 0, 0, width, height);

        fill(46, 204, 113);
        textSize(80);
        String gameOver = "Game Over";
        text(gameOver, centerText(gameOver), 60);

        textSize(48);
        PieceType winner = controller.getWinner();
        String msgWinner;
        if (winner == PieceType.PION_J1 || winner == PieceType.DAME_J1) {
            msgWinner = "*** PLAYER 1 WON ***";
        } else if (winner == PieceType.PION_J2 || winner == PieceType.DAME_J2) {
            boolean pve = (controller.getGameMode() == GameMode.PVE);
            msgWinner = pve ? "*** AI WON ***" : "*** PLAYER 2 WON ***";
        } else {
            msgWinner = "*** DRAW - NO WINNER ***";
        }
        fill(255);
        text(msgWinner, centerText(msgWinner), height / 2);

        String restart = "Press SPACE to choose mode";
        textSize(32);
        text(restart, centerText(restart), height - 70);
    }

    // Spielbildschirm

    /**
     * Zeichnet Hintergrund, Trennlinie und Dekorationsbilder des Spielbildschirms.
     * Zeigt ausserdem den aktuellen Zugnachricht an ("AI is thinking..." oder Spielerzug).
     */
    @Override
    public void drawDecor() {
        fill(100);
        rect(0, 0, width, height);
        fill(0);
        rect(0, 0, PLATEAU_LARGEUR, height);
        image(imgPlayer1, 250, 9);
        image(imgPlayer2, 250, 705);
        line(PLATEAU_LARGEUR, height, PLATEAU_LARGEUR, 10);
        textSize(20);
        fill(0);
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
                    case PION_J1 -> { fill(100); noStroke(); ellipse(cx, cy, taillePion, taillePion); }
                    case PION_J2 -> { fill(255); noStroke(); ellipse(cx, cy, taillePion, taillePion); }
                    case DAME_J1 -> {
                        fill(100); noStroke(); ellipse(cx, cy, taillePion, taillePion);
                        fill(200); ellipse(cx, cy, tailleInner, tailleInner);
                        image(imgCouronne,
                                imgX + (TAILLE_CASE - imgCouronne.width) / 2f,
                                imgY + (TAILLE_CASE - imgCouronne.height) / 2f);
                    }
                    case DAME_J2 -> {
                        fill(255); noStroke(); ellipse(cx, cy, taillePion, taillePion);
                        fill(180); ellipse(cx, cy, tailleInner, tailleInner);
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
}