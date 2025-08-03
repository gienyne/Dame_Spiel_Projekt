import processing.core.PApplet;
import Dame.Model.dameModel;
import Dame.Controller.dameController;
import Dame.View.dameView;

public class Main extends PApplet {

    /**
     * Main-Methode..Hier wird eine Verbindung zwischen model und controller herstellt sowie Controller und view
     * @param args args
     */
    public static void main(String[] args) {

        int GameSize = 600;
        var model = new dameModel();
        var view = new dameView(GameSize, GameSize);
        var controller = new dameController(model, view);

        controller.setModel(model);
        controller.setView(view);
        view.setController(controller);


       PApplet.runSketch(new String[]{"dameView"}, view);
       System.out.println(model);


    }



}