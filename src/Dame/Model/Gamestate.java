package Dame.Model;

/**
 * verwaltet die verschiedenen Zustände des Spiels
 */
public enum Gamestate {

    /** Anfangszustand */
    START,

    /**
     * Neuer Zustand: Der Bildschirm, auf dem der Spieler zwischen PVP und PVE wählt.
     */
    MODE_SELECT,

    /** Zustand während des Spiels */
    PLAYING,

    /** EndZustand */
    GAME_OVER
}