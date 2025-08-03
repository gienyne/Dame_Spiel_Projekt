package Dame.Controller;
import Dame.Model.Gamestate;
import Dame.Model.dameModel;
import Dame.View.IdameView;

/**
 * verwaltet den Timer
 */
public class timerThread extends Thread{

    /**
     * legt die Spieldauer fest
     */
    private timerThread timer;

    /**
     * model
     */
    final private dameModel model;

    /**
     * view
     */
    final  private IdameView view;

    /**
     * stellt die festgelegte Spieldauer dar
     */
    private final int secondes;

    /**
     * Zustand des Threads
     */
    private boolean isRunning = true;

    /**
     * Konstruktor
     * @param secondes secondes
     * @param model model
     * @param view view
     */

    public timerThread(int secondes, dameModel model, IdameView view) {
        this.secondes = secondes;
        this.model = model;
        this.view = view;

    }

    /**
     * Draw den Gewinner auf dem Bildschirm
     */
    public void endSpielByTimer (){
     char winner = model.getWinnerBeiGameOver();
          System.out.println("value: " + winner);

        if(winner == 'X'){

            view.drawOverscreen();
            System.out.println("PLAYER1 WON");
        }
        else if (winner == 'O'){

           view.drawOverscreen();
            System.out.println("PLAYER2 WON");
        }
        else {
            view.drawOverscreen();
            System.out.println("DRAW... NO TEAM WON");
        }

        model.setPreviousWinner(winner);
        System.out.println("Previosu win: " +  model.getPreviousWinner());
        model.setState(Gamestate.GAME_OVER);
        stopTimer();
        isRunning = false;// Signal d'arrêt  du thread
    }

    /**
     * startet den Timer
     */
    public void startTimer() {
        timer = new timerThread(secondes, model, view);
        timer.start();
    }

    /**
     * beendet den Timer
     */
    public void stopTimer(){
        if(timer != null){
            timer.interrupt();
        }
    }

    /**
     *  diese Methode verwaltet den Zeitzähler also den Timer
     */

    public void run(){
        try{
            while(model.getRemainingTime() > 0) {
                Thread.sleep((long) secondes * 1000);
                model.setRemainingTime(model.getRemainingTime() - 1);
            }
            if (isRunning) {
                endSpielByTimer();

            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
