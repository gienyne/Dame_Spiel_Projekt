import processing.core.PApplet;
import Dame.Model.dameModel;
import Dame.Controller.dameController;
import Dame.View.dameView;

/**
 * Main-Methode..Hier wird eine Verbindung zwischen model und controller herstellt sowie Controller und view
 * @param args args
 */
public class Main {

    public static void main(String[] args) {

        dameModel model = new dameModel();
        dameView view = new dameView(600, 800);
        dameController controller = new dameController(model, view);
        view.setController(controller);

        PApplet.runSketch(new String[]{"DameSpiel"}, view);

    }
}