package Dame.View;

/**
 * Schnittstelle der View im MVC-Muster.
 *
 * Definiert alle Zeichenmethoden, die der Controller aufrufen darf.
 * Der Controller kennt nie die konkrete Implementierung {@code dameView} direkt,
 * sondern kommuniziert ausschliesslich ueber diese Schnittstelle.
 * Jede Methode ist für das Zeichnen eines bestimmten Bildschirmbereichs
 * oder eines bestimmten Spielelements zuständig.
 *
 * @author Dimzz
 * @version 2.0
 * @see dameView
 */
public interface IdameView {

    /**
     * Zeichnet den Titelbildschirm.
     * Wird aufgerufen wenn der Spielzustand START ist.
     */
    void drawStartScreen();

    /**
     * Zeichnet den Modusauswahlbildschirm mit zwei anklickbaren Karten (PVP und PVE).
     * Wird aufgerufen wenn der Spielzustand MODE_SELECT ist.
     * Die Klickzonen entsprechen den gezeichneten Kartenpositionen.
     */
    void drawModeSelectScreen();

    /**
     * Zeichnet den Spielende-Bildschirm mit dem Namen des Gewinners.
     * Wird aufgerufen wenn der Spielzustand GAME_OVER ist.
     */
    void drawGameOverScreen();

    /**
     * Zeichnet den allgemeinen Dekorationshintergrund des Spielbildschirms,
     * einschliesslich Hintergrundfarben und Spielerfiguren-Bilder.
     */
    void drawDecor();

    /**
     * Zeichnet den Timer mit der verbleibenden Spielzeit.
     */
    void drawTimer();

    /**
     * Zeichnet das Spielbrett mit den Kacheln und hebt mögliche Züge
     * der ausgewählten Figur hervor.
     */
    void drawPlateau();

    /**
     * Zeichnet alle Spielfiguren (Bauern und Damen beider Spieler)
     * an ihrer aktuellen Position auf dem Brett.
     */
    void drawPions();

    /**
     * Zeichnet die Spielinformationen von Spieler 1:
     * Anzahl Bauern, Damen und Siege.
     */
    void drawInfoPlayer1();

    /**
     * Zeichnet die Spielinformationen von Spieler 2 bzw. der KI:
     * Anzahl Bauern, Damen und Siege.
     */
    void drawInfoPlayer2();

    /**
     * Zeichnet die gemeinsamen Spielinformationen:
     * aktuelles Gesamtergebnis und Rundennummer.
     */
    void drawInfoCommune();
}
