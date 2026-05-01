package Dame.Controller;

import Dame.Model.GameMode;
import Dame.Model.PieceType;
import Dame.Model.Gamestate;

/**
 * Schnittstelle des Controllers im MVC-Muster.
 *
 * Definiert alle Methoden, die die View vom Controller aufrufen darf.
 * Die View kennt niemals die konkrete Implementierung {@code dameController} direkt,
 * sondern kommuniziert ausschliesslich ueber diese Schnittstelle.
 * Dadurch bleibt die Kopplung zwischen View und Controller minimal.
 *
 * @author Dimzz
 * @version 2.0
 * @see dameController
 */
public interface IdameController {

    /**
     * Hauptanzeigemethode, die in jedem Frame von der View aufgerufen wird.
     * Bestimmt anhand des aktuellen Spielzustands, welcher Bildschirm gezeichnet wird.
     */
    void handleDisplay();


    // Benutzereingaben

    /**
     * Verarbeitet einen Mausklick des Benutzers.
     * Wird nur für Spielzustand PLAYING verwendet; alle anderen Zustände
     * werden direkt von der View abgefangen.
     *
     * @param mouseX horizontale Mausposition in Pixeln
     * @param mouseY vertikale Mausposition in Pixeln
     */
    void handleMouseInput(int mouseX, int mouseY);

    /**
     * Verarbeitet einen Tastendruck des Benutzers.
     *
     * @param key die gedrückte Taste als char
     */
    void handleKeyInput(char key);

    // Spielmodus

    /**
     * Gibt den aktuell gewählten Spielmodus zurück.
     *
     * @return aktueller Spielmodus (PVP oder PVE)
     */
    GameMode getGameMode();

    /**
     * Setzt den Spielmodus.
     *
     * @param mode der neue Spielmodus
     */
    void setGameMode(GameMode mode);

    /**
     * Wird von der View aufgerufen, wenn der Spieler auf dem Modusauswahlbildschirm
     * einen Modus per Mausklick auswählt. Startet die Partie mit dem gewählten Modus.
     *
     * @param mode der vom Spieler gewählte Spielmodus
     */
    void selectionnerMode(Dame.Model.GameMode mode);

    /**
     * Gibt das aktuelle Spielfeld zurück.
     *
     * @return zweidimensionales Array des Spielfelds
     */
    PieceType[][] getPlateau();

    /**
     * Gibt den aktuellen Spielzustand zurück.
     *
     * @return aktueller Spielzustand
     */
    Gamestate getGameState();

    /**
     * Gibt den aktuellen Spieler zurück.
     *
     * @return Figurentyp des aktuellen Spielers
     */
    PieceType getActuelPlayer();

    /**
     * Gibt die X-Koordinate der aktuell ausgewählten Figur zurück.
     *
     * @return X-Koordinate oder -1 wenn keine Figur ausgewählt ist
     */
    int getSelectedX();

    /**
     * Gibt die Y-Koordinate der aktuell ausgewählten Figur zurück.
     *
     * @return Y-Koordinate oder -1 wenn keine Figur ausgewählt ist
     */
    int getSelectedY();

    /**
     * Gibt alle möglichen Züge eines Bauern an der angegebenen Position zurück.
     *
     * @param x Spalte der Figur
     * @param y Zeile der Figur
     * @return Array von Zielkoordinaten {x, y}
     */
    int[][] getPossibleMovesPion(int x, int y);

    /**
     * Gibt alle moeglichen Züge einer Dame an der angegebenen Position zurück.
     *
     * @param x Spalte der Figur
     * @param y Zeile der Figur
     * @return Array von Zielkoordinaten {x, y}
     */
    int[][] getPossibleMovesDame(int x, int y);

    /**
     * Gibt an, ob im aktuellen Zug eine Schlagpflicht besteht.
     *
     * @return true wenn mindestens eine Figur schlagen muss
     */
    boolean isPrisePossible();

    /**
     * Prüft, ob ein Zug zur angegebenen Position ein Schlagzug ist.
     *
     * @param x Zielspalte
     * @param y Zielzeile
     * @return true wenn der Zug ein Schlagzug ist
     */
    boolean isMoveCapture(int x, int y);

    // Spielstatistiken

    /**
     * Gibt die Anzahl der verbleibenden Bauern von Spieler 1 zurück.
     *
     * @return Anzahl Bauern Spieler 1
     */
    int getNbrPionPlayer1();

    /**
     * Gibt die Anzahl der verbleibenden Bauern von Spieler 2 zurück.
     *
     * @return Anzahl Bauern Spieler 2
     */
    int getNbrPionPlayer2();

    /**
     * Gibt die Anzahl der Damen von Spieler 1 zurueck.
     *
     * @return Anzahl Damen Spieler 1
     */
    int getNbrDamePlayer1();

    /**
     * Gibt die Anzahl der Damen von Spieler 2 zurück.
     *
     * @return Anzahl Damen Spieler 2
     */
    int getNbrDamePlayer2();

    /**
     * Gibt die Anzahl der Siege von Spieler 1 zurück.
     *
     * @return Siege Spieler 1
     */
    int getVictoirePlayer1();

    /**
     * Gibt die Anzahl der Siege von Spieler 2 zurück.
     *
     * @return Siege Spieler 2
     */
    int getVictoirePlayer2();

    /**
     * Gibt die aktuelle Rundennummer zurück.
     *
     * @return aktuelle Runde
     */
    int getManche();

    /**
     * Gibt die verbleibende Spielzeit in Sekunden zurück.
     *
     * @return verbleibende Zeit in Sekunden
     */
    int getRemainingTimer();

    /**
     * Gibt den Gewinner der Partie zurück.
     *
     * @return Figurentyp des Gewinners, oder null wenn noch kein Gewinner feststeht
     */
    PieceType getWinner();

    /**
     * Gibt die Pixelgröße einer Spielfeldkachel zurück.
     * Wird von der View für die Umrechnung von Pixeln in Feldkoordinaten benötigt.
     *
     * @return Kantenlänge einer Kachel in Pixeln
     */
    int getTailleCase();
}