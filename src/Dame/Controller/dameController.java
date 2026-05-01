package Dame.Controller;

import Dame.Model.GameMode;
import Dame.Model.Gamestate;
import Dame.Model.IdameModel;
import Dame.Model.PieceType;
import Dame.IA.dameIA;

/**
 * Konkrete Implementierung des Controllers im MVC-Muster.
 *
 * Der Controller vermittelt zwischen Model und View. Er verarbeitet
 * Benutzereingaben, aktualisiert den Spielzustand im Model und
 * entscheidet, welche Bildschirme die View anzeigt.
 *
 * Wesentliche Verantwortlichkeiten:
 * - Verwaltung des Spielmodus (PVP oder PVE)
 * - Steuerung des KI-Zugs in einem separaten Thread
 * - Weiterleitung von Maus- und Tastatureingaben an das Model
 *
 * @author Dimzz
 * @version 2.0
 * @see IdameController
 * @see dameIA
 */
public class dameController implements IdameController {

    /** Breite des Spielfelds in Pixeln. */
    private static final int PLATEAU_LARGEUR  = 600;

    /** Vertikaler Abstand des Spielfelds vom oberen Fensterrand in Pixeln. */
    private static final int PLATEAU_OFFSET_Y = 100;

    /** Anzahl der Felder pro Reihe und Spalte. */
    private static final int TAILLE           = 8;

    /** Fensterbreite in Pixeln. Muss mit dem Wert in dameView übereinstimmen. */
    private static final int FENETRE_LARGEUR  = 1060;

    /** Fensterhöhe in Pixeln. Muss mit dem Wert in dameView übereinstimmen. */
    private static final int FENETRE_HAUTEUR  = 800;

    /** Referenz auf das Model, das die gesamte Spiellogik enthält. */
    private final IdameModel model;

    /** Referenz auf die View, die für die Darstellung zuständig ist. */
    private final Dame.View.IdameView view;

    /** Timer-Thread, der den Countdown verwaltet. */
    private final timerThread timer;

    /** Aktuell gewählter Spielmodus. Standardmässig PVP. */
    private GameMode gameMode = GameMode.PVP;

    /** Instanz der KI-Engine. Wird einmalig erstellt und wiederverwendet. */
    private final dameIA ia;

    /**
     * Sperrvariable für den KI-Zug.
     * true bedeutet, die KI berechnet gerade einen Zug.
     * Verhindert gleichzeitige Klicks des Spielers und doppelte Thread-Starts.
     * volatile stellt sicher, dass der Wert threadübergreifend sichtbar ist.
     */
    private volatile boolean iaEnCours = false;

    /**
     * Erstellt einen neuen Controller und verbindet Model, View und KI.
     *
     * @param model das Spielmodell
     * @param view  die Spielansicht
     */
    public dameController(IdameModel model, Dame.View.IdameView view) {
        this.model = model;
        this.view  = view;
        this.timer = new timerThread(model, this);
        this.ia    = new dameIA(model);
    }

    /**
     * Wird von Processing 60-mal pro Sekunde aufgerufen.
     * Bestimmt anhand des aktuellen Spielzustands, welcher Bildschirm gezeichnet wird.
     * Im Zustand PLAYING wird ausserdem der KI-Zug ausgeloest, falls noetig.
     *
     * @throws IllegalStateException wenn ein unbekannter Spielzustand vorliegt
     */
    @Override
    public void handleDisplay() {
        switch (model.getState()) {
            case START       -> view.drawStartScreen();
            case MODE_SELECT -> view.drawModeSelectScreen();
            case PLAYING     -> {
                view.drawDecor();
                view.drawTimer();
                view.drawInfoPlayer1();
                view.drawInfoPlayer2();
                view.drawInfoCommune();
                view.drawPlateau();
                view.drawPions();

                if (gameMode == GameMode.PVE && isIATurn() && !iaEnCours) {
                    jouerTourIA();
                }
            }
            case GAME_OVER -> {
                timer.stopTimer();
                view.drawGameOverScreen();
            }
            default -> throw new IllegalStateException("Unbekannter Spielzustand: " + model.getState());
        }
    }

    /**
     * Prüft, ob die KI (Spieler 2) am Zug ist.
     *
     * @return true wenn der aktuelle Spieler Spieler 2 ist
     */
    private boolean isIATurn() {
        PieceType player = model.getActuelPlayer();
        return player == PieceType.PION_J2 || player == PieceType.DAME_J2;
    }

    /**
     * Startet den KI-Zug in einem separaten Thread.
     *
     * Processing ruft draw() 60-mal pro Sekunde im Hauptthread auf.
     * Wuerde die Minimax-Berechnung direkt in draw() ausgeführt, würde
     * das Spiel für die Dauer der Berechnung einfrieren.
     * Der separate Thread erlaubt Processing, die Anzeige weiterzufuehren,
     * während die KI rechnet. iaEnCours = true blockiert Klicks und
     * verhindert, dass ein zweiter KI-Thread gestartet wird.
     */
    private void jouerTourIA() {
        iaEnCours = true;

        Thread threadIA = new Thread(() -> {
            try { Thread.sleep(400); } catch (InterruptedException ignored) {}

            int[] coup = ia.choisirMeilleurCoup();

            if (coup != null) {
                model.setSelected(coup[0], coup[1]);
                boolean moveMade = model.executerDeplacement(coup[2], coup[3]);

                if (moveMade) {
                    model.checkGameOver();
                    if (!model.isPrisePossible()) {
                        model.changePlayer(true);
                    }
                }
            } else {
                // Kein Zug möglich: KI verliert
                model.checkGameOver();
            }

            iaEnCours = false;
        });

        threadIA.setDaemon(true);
        threadIA.start();
    }

    // Tastatureingaben

    /**
     * Verarbeitet Tastatureingaben je nach aktuellem Spielzustand.
     * START     : Leertaste wechselt zur Modusauswahl.
     * MODE_SELECT: Taste 1 startet PVP, Taste 2 startet PVE.
     * GAME_OVER : Leertaste kehrt zur Modusauswahl zurück.
     *
     * @param key die gedrückte Taste
     */
    @Override
    public void handleKeyInput(char key) {
        switch (model.getState()) {
            case START -> {
                if (key == ' ') model.setState(Gamestate.MODE_SELECT);
            }
            case MODE_SELECT -> {
                if (key == '1') lancerPartie(GameMode.PVP);
                if (key == '2') lancerPartie(GameMode.PVE);
            }
            case GAME_OVER -> {
                if (key == ' ') model.setState(Gamestate.MODE_SELECT);
            }
            case PLAYING -> { /* erweiterbar */ }
        }
    }

    // Mauseingaben

    /**
     * Verarbeitet Mausklicks während des Spiels (Zustand PLAYING).
     * Mausklicks auf dem Modusauswahlbildschirm werden direkt von der View
     * abgefangen und über {@link #selectionnerMode} weitergeleitet.
     *
     * Ablauf bei PLAYING:
     * 1. Klicks waehrend des KI-Zugs werden ignoriert.
     * 2. Klicks ausserhalb des Spielfelds werden ignoriert.
     * 3. Klick auf eigene Figur: Figur auswählen oder Auswahl wechseln.
     * 4. Klick auf leere Zielkachel: Zug ausführen.
     *
     * @param mouseX horizontale Mausposition in Pixeln
     * @param mouseY vertikale Mausposition in Pixeln
     */
    @Override
    public void handleMouseInput(int mouseX, int mouseY) {
        if (model.getState() != Gamestate.PLAYING) return;
        if (gameMode == GameMode.PVE && (isIATurn() || iaEnCours)) return;
        if (mouseX < 0 || mouseX >= PLATEAU_LARGEUR) return;
        if (mouseY < PLATEAU_OFFSET_Y || mouseY >= PLATEAU_OFFSET_Y + PLATEAU_LARGEUR) return;

        int col   = mouseX / getTailleCase();
        int ligne = (mouseY - PLATEAU_OFFSET_Y) / getTailleCase();

        PieceType[][] plateau = model.getPlateau();

        if (model.getSelectedX() == -1) {
            PieceType piece = plateau[ligne][col];
            if (isCurrentPlayerPiece(piece)) {
                model.setSelected(col, ligne);
            }
            return;
        }

        PieceType pieceCliquee = plateau[ligne][col];
        if (isCurrentPlayerPiece(pieceCliquee)) {
            model.setSelected(col, ligne);
            return;
        }

        boolean moveMade = model.executerDeplacement(col, ligne);
        if (moveMade) {
            model.checkGameOver();
            if (!model.isPrisePossible()) {
                model.changePlayer(true);
            }
        }
    }

    // Spielstart

    /**
     * Konfiguriert den Spielmodus, startet eine neue Partie und den Timer.
     * Setzt ausserdem iaEnCours zurück, um Sperren aus der vorherigen Partie
     * zu beseitigen.
     *
     * @param mode der gewählte Spielmodus
     */
    private void lancerPartie(GameMode mode) {
        this.gameMode  = mode;
        this.iaEnCours = false;
        model.setState(Gamestate.PLAYING);
        model.newgame();
        timer.startTimer();
    }

    /**
     * Prüft, ob die angegebene Figur dem aktuellen Spieler gehört.
     *
     * @param piece zu prüfender Figurentyp
     * @return true wenn die Figur dem aktuellen Spieler gehört
     */
    private boolean isCurrentPlayerPiece(PieceType piece) {
        PieceType player = model.getActuelPlayer();
        if (player == PieceType.PION_J1 || player == PieceType.DAME_J1)
            return piece == PieceType.PION_J1 || piece == PieceType.DAME_J1;
        return piece == PieceType.PION_J2 || piece == PieceType.DAME_J2;
    }

    /**
     * Wird von der View aufgerufen, wenn der Spieler per Mausklick einen Modus
     * auf dem Auswahlbildschirm auswählt. Delegiert an lancerPartie.
     *
     * @param mode der gewählte Spielmodus
     */
    @Override
    public void selectionnerMode(GameMode mode) {
        lancerPartie(mode);
    }


    @Override public GameMode getGameMode()           { return gameMode; }
    @Override public void setGameMode(GameMode mode)  { this.gameMode = mode; }


    @Override public PieceType[][] getPlateau()                      { return model.getPlateau(); }
    @Override public Gamestate getGameState()                        { return model.getState(); }
    @Override public PieceType getActuelPlayer()                     { return model.getActuelPlayer(); }
    @Override public int getSelectedX()                              { return model.getSelectedX(); }
    @Override public int getSelectedY()                              { return model.getSelectedY(); }
    @Override public int[][] getPossibleMovesPion(int x, int y)      { return model.getPossibleMovesPion(x, y); }
    @Override public int[][] getPossibleMovesDame(int x, int y)      { return model.getPossibleMovesDame(x, y); }
    @Override public boolean isPrisePossible()                       { return model.isPrisePossible(); }
    @Override public boolean isMoveCapture(int x, int y)            { return model.isMoveCapture(x, y); }
    @Override public int getNbrPionPlayer1()                         { return model.getNbrPionPlayer1(); }
    @Override public int getNbrPionPlayer2()                         { return model.getNbrPionPlayer2(); }
    @Override public int getNbrDamePlayer1()                         { return model.getNbrDamePlayer1(); }
    @Override public int getNbrDamePlayer2()                         { return model.getNbrDamePlayer2(); }
    @Override public int getVictoirePlayer1()                        { return model.getVictoirePlayer1(); }
    @Override public int getVictoirePlayer2()                        { return model.getVictoirePlayer2(); }
    @Override public int getManche()                                 { return model.getManche(); }
    @Override public int getRemainingTimer()                         { return model.getRemainingTime(); }
    @Override public PieceType getWinner()                           { return model.getWinner(); }
    @Override public int getTailleCase()                             { return PLATEAU_LARGEUR / TAILLE; }
}