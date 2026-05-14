package Dame.Model;

/**
 * Schnittstelle des Models im MVC-Muster.
 *
 * Definiert den vollständigen Vertrag der Spiellogik.
 * Die Methoden sind in zwei Kategorien unterteilt:
 *
 * 1. Zustandsbehaftete Methoden (Abschnitt "Mit Zustand"):
 *    Arbeiten auf dem echten Spielfeld und veraendern den internen Zustand.
 *    Werden vom Controller für menschliche Spielerzüge aufgerufen.
 *
 * 2. Zustandslose Methoden (Abschnitt "Stateless"):
 *    Arbeiten ausschliesslich auf uebergebenen Spielfeldkopien.
 *    Verändern keinerlei internen Zustand. Werden von der KI für
 *    Simulationen verwendet, damit die KI-Berechnung das echte Spiel
 *    nicht beeinflusst.
 *
 * @author Dimzz
 * @version 2.0
 * @see dameModel
 */
public interface IdameModel {

    // Spielzustand

    /**
     * Gibt den aktuellen Spielzustand zurück.
     *
     * @return aktueller Spielzustand
     */
   Gamestate getState();

    /**
     * Setzt den Spielzustand.
     *
     * @param state neuer Spielzustand
     */
    void setState(Gamestate state);

    /**
     * Initialisiert eine neue Partie. Setzt Spielfeld, Figuren, Zähler
     * und Timer auf die Ausgangswerte zurück.
     */
    void newgame();

    // Spielfeld

    /**
     * Gibt das aktuelle Spielfeld zurück.
     * Das zurückgegebene Array ist die interne Referenz; Änderungen
     * daran wirken sich direkt auf das Spiel aus.
     *
     * @return zweidimensionales Array mit dem Spielfeldzustand
     */
    PieceType[][] getPlateau();

    // -------------------------------------------------------------------------
    // Spieler
    // -------------------------------------------------------------------------

    /**
     * Gibt den aktuellen Spieler zurück.
     *
     * @return Figurentyp des aktuellen Spielers (PION_J1 oder PION_J2)
     */
    PieceType getActuelPlayer();

    /**
     * Wechselt den aktiven Spieler und berechnet die Schlagpflicht neu.
     *
     * @param moveMade true wenn ein Zug ausgeführt wurde; bei false passiert nichts
     */
    void changePlayer(boolean moveMade);

    // Figurenauswahl

    /**
     * Gibt die X-Koordinate (Spalte) der ausgewaehlten Figur zurück.
     *
     * @return Spalte der ausgewählten Figur, oder -1 wenn keine ausgewählt ist
     */
    int getSelectedX();

    /**
     * Gibt die Y-Koordinate (Zeile) der ausgewählten Figur zurück.
     *
     * @return Zeile der ausgewählten Figur, oder -1 wenn keine ausgewählt ist
     */
    int getSelectedY();

    /**
     * Setzt die ausgewählte Figur auf die angegebene Position.
     *
     * @param x Spalte
     * @param y Zeile
     */
    void setSelected(int x, int y);

    /**
     * Hebt die aktuelle Auswahl auf. Setzt selectedX und selectedY auf -1.
     */
    void clearSelected();

    /**
     * Prüft, ob die Figur an der angegebenen Position ausgewaehlt werden darf.
     * Bei bestehender Schlagpflicht darf nur eine Figur ausgewählt werden,
     * die tatsächlich schlagen kann.
     *
     * @param x Spalte
     * @param y Zeile
     * @return true wenn die Figur ausgewählt werden darf
     */
    boolean peutEtreSelectionne(int x, int y);

    // -------------------------------------------------------------------------
    // Zuege mit Zustand (menschlicher Spieler, echtes Spiel)
    // -------------------------------------------------------------------------

    /**
     * Gibt alle legalen Zielkacheln für den Bauern bzw Spielstein an der angegebenen Position zurück.
     * Bei Schlagpflicht werden nur Schlagzüge zurückgegeben.
     *
     * @param x Spalte des Bauern
     * @param y Zeile des Bauern
     * @return Array von Zielkoordinaten {x, y}
     */
    int[][] getPossibleMovesPion(int x, int y);

    /**
     * Gibt alle legalen Zielkacheln für die Dame an der angegebenen Position zurück.
     * Bei Schlagpflicht werden nur Schlagzüge zurückgegeben.
     *
     * @param x Spalte der Dame
     * @param y Zeile der Dame
     * @return Array von Zielkoordinaten {x, y}
     */
    int[][] getPossibleMovesDame(int x, int y);

    /**
     * Validiert und führt den Zug der ausgewählten Figur zur Zielposition aus.
     * Bei Schlagpflicht muss die Zielposition zu einer gültigen Schlagkette gehören.
     *
     * @param x Zielspalte
     * @param y Zielzeile
     * @return true wenn der Zug erfolgreich ausgeführt wurde
     */
    boolean executerDeplacement(int x, int y);

    /**
     * Gibt alle Zielkacheln zurück, die über eine Schlagkette von der
     * ausgewählten Figur erreichbar sind. Der Spieler kann sich an jedem
     * Zwischenschritt stoppen.
     *
     * @param x Ausgangsspalte
     * @param y Ausgangszeile
     * @return Array aller erreichbaren Zielkoordinaten
     */
    int[][] getChaineCaptures(int x, int y);

    // Zustandslose Methoden für die KI-Simulation

    /**
     * Gibt alle legalen Züge für einen Spieler auf einem simulierten Spielfeld zurück.
     * Verändert den internen Zustand des Models nicht.
     * Bei bestehender Schlagpflicht werden ausschliesslich Schlagzüge zurückgegeben.
     *
     * @param plateau Spielfeldkopie, auf der simuliert wird
     * @param joueur  der Spieler, dessen Züge gesucht werden
     * @return Array von Zügen, jeweils {fromX, fromY, toX, toY}
     */
    int[][] getCoupsPossibles(PieceType[][] plateau, PieceType joueur);

    /**
     * Führt einen Zug auf einer Spielfeldkopie aus, ohne den internen Zustand zu verändern.
     * Verwaltet Bewegung, Schlagen (einfach und in einer Linie für Damen) sowie Beförderung.
     *
     * @param plateau Spielfeldkopie, die verändert wird (muss eine Kopie sein)
     * @param coup    Zug als Array {fromX, fromY, toX, toY}
     */
    void appliquerCoupSurPlateau(PieceType[][] plateau, int[] coup);

    /**
     * Erstellt eine unabhängige Kopie des angegebenen Spielfelds.
     * Die Kopie wird für KI-Simulationen verwendet.
     *
     * @param source das zu kopierende Spielfeld
     * @return tiefe Kopie des Spielfelds
     */
    PieceType[][] copierPlateau(PieceType[][] source);

    /**
     * Prüft auf einem simulierten Spielfeld, ob die Partie beendet ist.
     * Die Partie ist beendet, wenn eine Seite keine Figuren mehr hat.
     *
     * @param plateau das zu prüfende Spielfeld
     * @return true wenn keine Figuren einer Seite mehr vorhanden sind
     */
    boolean estPartieTermineeSurPlateau(PieceType[][] plateau);

    // Schlagpflicht

    /**
     * Gibt an, ob der aktuelle Spieler schlagen muss.
     *
     * @return true wenn Schlagpflicht besteht
     */
    boolean isPrisePossible();

    /**
     * Prüft, ob ein Zug zur angegebenen Position ein Schlagzug ist.
     * Basiert auf dem Abstand zwischen ausgewählter Figur und Zielposition.
     *
     * @param x Zielspalte
     * @param y Zielzeile
     * @return true wenn es sich um einen Schlagzug handelt
     */
    boolean isMoveCapture(int x, int y);

    // Spielstatistiken

    /** @return Anzahl verbleibender Bauern von Spieler 1 */
    int getNbrPionPlayer1();

    /** @return Anzahl verbleibender Bauern von Spieler 2 */
    int getNbrPionPlayer2();

    /** @return Anzahl der Damen von Spieler 1 */
    int getNbrDamePlayer1();

    /** @return Anzahl der Damen von Spieler 2 */
    int getNbrDamePlayer2();

    /** @return Anzahl der Siege von Spieler 1 */
    int getVictoirePlayer1();

    /** @return Anzahl der Siege von Spieler 2 */
    int getVictoirePlayer2();

    /** @return aktuelle Rundennummer */
    int getManche();

    /** @return verbleibende Spielzeit in Sekunden */
    int getRemainingTime();

    /**
     * Setzt die verbleibende Spielzeit.
     *
     * @param time neue verbleibende Zeit in Sekunden
     */
    void setRemainingTime(int time);

    // Spielende

    /**
     * Gibt den Gewinner der beendeten Partie zurück.
     *
     * @return Figurentyp des Gewinners, oder null wenn das Spiel noch läuft
     */
    PieceType getWinner();

    /**
     * Prüft, ob das Spiel beendet ist, und setzt den Zustand auf GAME_OVER
     * falls eine Abbruchbedingung erfüllt ist:
     * - Spieler 1 hat keine Figuren mehr
     * - Spieler 2 hat keine Figuren mehr
     * - Der aktuelle Spieler hat keinen gültigen Zug mehr
     */
    void checkGameOver();
}