package Dame.View;

/**
 * Methode des view, auf die den Controller zugriff hat
 */
public interface IdameView {
    /**
     * gibt die Größe eines Feldes
     * @return TailleCase
     */
    int getTailleCase();

    /**
     * zeichnet das SpielBrett
     */
    void dessinerTableau();

    /**
     * zeichnet die SpielSteine
     */
    void dessinerPion();

    /**
     * Mauseingabe des Benutzers, die an den Controller weitergeleitet wird.
     */
    void mousePressed();

    /**
     * zeichen das EndBild im BildSchirm
     */
    void drawOverscreen();

    /**
     * zeichen das StartBild im BildSchirm
     */
    void drawStartScreen ();

    /**
     * zeichnet den decor also verschiedene Spieler, Zeile und Farben
     */
    void decor();

    /**
     * draw den Timer im Bildschirm
     */
    void drawTimer();


    /**
     * enthält und zeichnet alle notwendigen Infos über den Player1
     */
    void infoPlayer1();

    /**
     * enthält und zeichnet alle notwendige infos über den Player2
     */
    void infoPlayer2();


    /**
     * enthält und zeichnet alle infos, die sowohl  den Spieler1 und Spieler2 betreffen
     */
    void infoPlayer1Et2();
}
