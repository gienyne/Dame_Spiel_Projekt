package Dame.Controller;

import Dame.Model.IdameModel;
import Dame.Model.Gamestate;

/**
 * Hintergrund-Thread, der den Spielzeitcountdown verwaltet.
 *
 * Der Thread dekrementiert sekündlich die verbleibende Spielzeit im Model.
 * Erreicht die Zeit null, wird der Spielzustand auf GAME_OVER gesetzt.
 * Der Thread läuft als Daemon-Thread, sodass er automatisch beendet wird,
 * wenn die JVM heruntergefahren wird.
 *
 * @author Dimzz
 * @version 2.0
 */
public class timerThread {

    /** Referenz auf das Model, um die verbleibende Zeit zu lesen und zu setzen. */
    private final IdameModel model;

    /** Referenz auf den Controller, für eventuelle spätere Erweiterungen. */
    private final IdameController controller;

    /** Der interne Java-Thread, der den Countdown ausführt. */
    private Thread thread;

    /**
     * Steuervariable des Threads.
     * Wird auf false gesetzt, um den Thread sauber zu stoppen.
     */
    private boolean running = false;

    /**
     * Erstellt einen neuen Timer.
     *
     * @param model      das Spielmodell, in dem die Zeit gespeichert ist
     * @param controller der Controller, dem dieser Timer gehört
     */
    public timerThread(IdameModel model, IdameController controller) {
        this.model      = model;
        this.controller = controller;
    }

    /**
     * Startet den Countdown.
     * Erstellt und startet einen neuen Thread, der jede Sekunde
     * die verbleibende Zeit um eins dekrementiert.
     * Erreicht die Zeit null, wird der Spielzustand auf GAME_OVER gesetzt.
     */
    public void startTimer() {
        running = true;
        thread = new Thread(() -> {
            while (running && model.getRemainingTime() > 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                if (running) {
                    model.setRemainingTime(model.getRemainingTime() - 1);
                }
            }
            if (running && model.getRemainingTime() <= 0) {
                model.setState(Gamestate.GAME_OVER);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Stoppt den Countdown.
     * Setzt running auf false und unterbricht den Thread, falls er noch läuft.
     * Wird beim Eintreten in GAME_OVER aufgerufen.
     */
    public void stopTimer() {
        running = false;
        if (thread != null) {
            thread.interrupt();
        }
    }
}