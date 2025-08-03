package Dame.Controller;
import Dame.Model.Gamestate;
import Dame.Model.dameModel;
import Dame.View.IdameView;

/**
 * entscheidet anhand vom BenutzerEingaben was gezeichnet werden soll oder nicht
 */

public class dameController implements IdameController {

    /**
     * variable model vom Typ dameModel
     */
    private dameModel model;
    /**
     * variable view vom Typ IdameView
     */
    private IdameView view;
    /**
     * variable timer vom Typ timerThread
     */
    private final timerThread timer;

    /**
     * Methode setModel dient zur Initialisiserung des models in der main-Methode
     * @param model model
     */

    public void setModel(dameModel model){
        this.model = model;

    }

    /**
     * Methode setView dient zur Initialisierung des view in der main-methode
     * @param view view
     */
    public void setView(IdameView view){
        this.view = view;
    }

    /**
     * nimmt als Parameter das model und den view und initialisiert ihre Werte
     * @param model model
     * @param view view
     */
    public dameController(dameModel model, IdameView view) {
        this.model = model;
        this.view = view;
        this.timer = new timerThread(10, model, view);
    }

    /**
     * Methode getTableau gibt das Spielbrett aus dem model
     * @return model.getPlateauJeu()
     */
    public char[][] getTableau(){
        return model.getPlateauJeu();
    }

    /**
     * gibt den aktuelen Player aus dem model
     * @return model.getactuelPlayer()
     */
    public char getactuelPlayer(){
        return model.getactuelPlayer();
    }


    /**
     *Wenn die Funktion aufgerufen wird, während sich der Benutzer im Startbildschirm befindet,
     *  wird das Spiel gestartet.
     * @param x x-koordinaten von dem ausgewählten SpielStein
     * @param y y-koordinaten von dem ausgewählten SpielStein
     */
    public void userInput(int x, int y){
        y = y - 100;
        int colonne = x / view.getTailleCase();
        int ligne = y / view.getTailleCase();

        if(model.getSelectedPionX() == -1 && model.getSelectedPionY() == -1){

          //  Wenn kein SpielStein ausgewählt ist, überprüfe, ob der Klick mit einer Spielfigur des aktuellen Spielers übereinstimmt.

            if(getTableau()[ligne][colonne] == getactuelPlayer() ||  getTableau()[ligne][colonne] == 'D' || getTableau()[ligne][colonne] == 'd'){


                if(getTableau()[ligne][colonne] == 'D' || getTableau()[ligne][colonne] == 'd'){
                    model.setActuelPlayer(getTableau()[ligne][colonne] == 'D'? 'D': 'd');
                    model.setIsDame(ligne, colonne, true);
                }

                model.setSelectedPionX(colonne);
                model.setSelectedPionY(ligne);


            }




        }

        else {

            // Vérifie si le clic correspond à un déplacement valide pour le pion sélectionné
            model.setmoveMade(false);

            if (!getIsDame()[getSelectedX()][getSelectedY()]){
                deplacementPionSimple( colonne , ligne);
                model.changePlayer(model.getmoveMade());
                model.setIsDame(ligne, colonne, false);
                System.out.println("actuel player PLAYER: " + getactuelPlayer());
            }

            else if (getIsDame()[getSelectedX()][getSelectedY()]){
                // System.out.println("caseValue: "+ getIsDame()[getSelectedX()][getSelectedY()]);
                if(colonne >= 0 && colonne < 8 && ligne >= 0 && ligne < 8) {
                    deplacementDame(colonne, ligne);

                    model.changePlayer(model.getmoveMade());
                    model.setIsDame(ligne, colonne, false);
                }
                //      model.setIsDame(ligne, colonne, false); // La case de départ n'est plus une dame

            }


            if (!model.getmoveMade()) {
                // Si aucun déplacement n'a été fait, vérifie si le clic correspond à un autre pion du joueur actuel
                if (getTableau()[ligne][colonne] == getactuelPlayer() || getTableau()[ligne][colonne] == 'D' || getTableau()[ligne][colonne] == 'd') {
                    model.setSelectedPionX(colonne);
                    model.setSelectedPionY(ligne);
                    model.setmoveMade(false);

                }

            }

        }

    }

    /**
     * Unsere methode PossibleMovePionAfterCapture bestimmt für einfachen SpielStein nach einer ersten Bewegung mit aufnahme von seinen neuen Koordinaten aus seine neuen möglichen bewegungen mit oder ohne Aufnahme
     *
     * @param x      x-koordinaten des ausgewählten SpielSteins
     * @param y      Y-koordinaten des ausgewählten SpielSteins
     * @param player Aktueller Spieler
     * @return model.getPossibleMovePionAfterCaptures();
     */
    public int [][] getPossibleMovePionAfterCapture(int x, int y, char player){
        return model.getPossibleMovePionAfterCaptures(x, y, player);
    }

    /**
     * gib an, ob eine Aufnahme beim Bewegen möglich ist
     *
     * @param x      x-koordinaten der ausgewälten SpielStein
     * @param y      Y-koordinaten der ausgewälten SpielStein
     * @param player Aktuel Spieler
     * @return true oder false
     */
    public boolean MoveandBouffeCtrl(int x , int y, char player){
        boolean MaBc = model.isMoveAndBouffe(x, y, player);
        return MaBc;
    }

    /**
     * gibt den x-koordinaten des ausgewählten SpielSteins
     * @return model.getSelectedPionX()
     */
    public int getSelectedX(){
        return model.getSelectedPionX();
    }

    /**
     * gibt den y-koordinaten des ausgewählten SpielSteins
     * @return model.getSelectedPionY()
     */
    public int getSelectedY(){
        return model.getSelectedPionY();
    }

    /**
     * Unsere methode PossibleMovePion bestimmt für einfachen SpielStein von seinen Koordinaten aus seine möglichen bewegungen mit oder ohne Aufnahme
     *
     * @param x      x-koordinaten des ausgewählten SpielSteins
     * @param y      Y-koordinaten des ausgewählten SpielSteins
     * @param player Aktueller Spieler
     * @return model.getPossibleMovePion
     */
    public int[][] getPossibleMovePion(int x, int y, char player){
        return model.PossibleMovePion(x, y, player);
    }

    /**
     * gibt an, ob einen Zug oder Schlag für einen einfachen Spielstein möglich ist oder nicht
     *
     * @return model.getPrisePossible
     */
    public boolean getPrisePossible(){
        return model.getPrisePossible();
    }

    /**
     * Unsere methode PossibleMoveDame bestimmt für eine von seinen Koordinaten aus seine möglichen bewegungen
     *
     * @param x      x-koordinaten der ausgewälten Dame
     * @param y      Y-koordinaten der ausgewälten Dame
     * @param player Aktuel Spieler
     * @return eine Liste von allen möglichen Bewegungen für den gewählten SpielStein
     */
    public int[][] possibleMoveDame(int x, int y, char player){
        return model.possibleMoveDame(x, y, player);
    }

    /**
     * gibt an, ob ein Feld eine dame enthält oder nicht
     * @return model.getIsDame()
     */
    public boolean[][] getIsDame(){
        return model.getIsDame();
    }

    /**
     * diese Methode ruft die methode PossibleMovePion aus dem model, die die bewegungen von einfachen SpielSteine verwaltet,
     * und speichert dann für einen ausgewählten SpielStein seine Bewegung in einem anderen zwei-dimenssionales Array ..auf diese weise
     * verfügt er über die möglichen Bewegungen für einen SpielStein..und beim nächster Click überprüft er, ob die das gewählte Feld einer
     * erlaubte bewegung entspricht. falls ja wird die Bewegungen zugelassen und im Fall einer Aufnahme wird den aufgenommenen SpielStein aus
     * dem SpielBrett entfernt durch die methode model.setPlateau(x-koordinaten (von dem SpielStein, der aufgenommen werden wird), y-koordinaten, model.Empty)
     * @param colonne colonne entspricht den y-koordinaten des ausgewählten SpielSteins
     * @param ligne ligne entspricht den y-koordinaten des ausgewählten SpielSteins
     */
    public void deplacementPionSimple(int colonne , int ligne) {
        // Si un pion est déjà sélectionné, vérifie les déplacements possibles


        int[][] deplacements = model.PossibleMovePion(model.getSelectedPionX(), model.getSelectedPionY(), model.getactuelPlayer());


        for (int[] deplacement : deplacements) {
            int x1 = deplacement[0];
            int y1 = deplacement[1];

            if (x1 == colonne && y1 == ligne) {

                if (Math.abs(x1 - model.getSelectedPionX()) == 2) {


                    int medianeX = (x1 + model.getSelectedPionX()) / 2;
                    int medianeY = (y1 + model.getSelectedPionY()) / 2;

                    // modifie le plateau de jeu pour définir la case où se trouve le pion adverse
                    // comme étant vide (model.EMPTY), simulant ainsi la capture du pion adverse.

                    if(getTableau()[medianeY][medianeX] == 'X'){model.setNbrPionPlayer1(model.getNbrPionPlayer1() - 1);}
                    else if(getTableau()[medianeY][medianeX] == 'O') {model.setNbrPionPlayer2(model.getNbrPionPlayer2() -1);}
                    model.setPlateauJeu(medianeX, medianeY, model.EMPTY);
                }

                model.setPlateauJeu(getSelectedX(), getSelectedY(), model.EMPTY);
                model.setPlateauJeu(colonne, ligne, getactuelPlayer());
                System.out.println(model.toString());
                model.pionToDame(x1, y1, model.getactuelPlayer());

                // Réinitialisation de la sélection
                model.setSelectedPionX(-1);
                model.setSelectedPionY(-1);

                model.setmoveMade(true);

                return;
            }

            int[][] nextState = model.getPossibleMovePionAfterCaptures(x1, y1, getactuelPlayer());
            for (int[] nextmove : nextState) {
                int x2 = nextmove[0];
                int y2 = nextmove[1];

                if (x2 == colonne && y2 == ligne) {
                    if (Math.abs(x2 - model.getSelectedPionX()) == 4 || Math.abs(x2 - model.getSelectedPionX()) == 0) {
                        int medianeX = (x1 + model.getSelectedPionX()) / 2;
                        int medianeY = (y1 + model.getSelectedPionY()) / 2;

                        int medianeX2 = (x2 + x1) / 2;
                        int medianeY2 = (y2 + y1) / 2;

                        if(getTableau()[medianeY][medianeX] == 'X'){model.setNbrPionPlayer1(model.getNbrPionPlayer1() - 1);}
                        else if(getTableau()[medianeY][medianeX] == 'O') {model.setNbrPionPlayer2(model.getNbrPionPlayer2() -1);}
                        model.setPlateauJeu(medianeX, medianeY, model.EMPTY);

                        if(getTableau()[medianeY2][medianeX2] == 'X'){model.setNbrPionPlayer1(model.getNbrPionPlayer1() - 1);}
                        else if(getTableau()[medianeY2][medianeX2] == 'O') {model.setNbrPionPlayer2(model.getNbrPionPlayer2() -1);}
                        model.setPlateauJeu(medianeX2, medianeY2, model.EMPTY);


                    }

                    model.setPlateauJeu(getSelectedX(), getSelectedY(), model.EMPTY);
                    model.setPlateauJeu(colonne, ligne, getactuelPlayer());
                    System.out.println(model.toString());
                    model.pionToDame(x2, y2, model.getactuelPlayer());


                    // Auswahl zurücksetzen
                    model.setSelectedPionX(-1);
                    model.setSelectedPionY(-1);

                    model.setmoveMade(true);
                }

                //entrspicht einer mehrfachaufnahme

                int[][] autrenextState = model.getPossibleMovePionAfterCaptures(x2, y2, getactuelPlayer());
                for (int[] autrenextmove : autrenextState) {
                    int x3 = autrenextmove[0];
                    int y3 = autrenextmove[1];
                    System.out.println(" x3: " + x3+ " y3: "+  y3);

                    if (x3 == colonne && y3 == ligne) {
                        if (Math.abs(x3 - model.getSelectedPionX()) == 6 || Math.abs(x1 - model.getSelectedPionX()) == 2) {

                            int medianeX = (x1 + model.getSelectedPionX()) / 2;
                            int medianeY = (y1 + model.getSelectedPionY()) / 2;

                            int medianeX2 = (x2 + x1) / 2;
                            int medianeY2 = (y2 + y1) / 2;

                            int medianeX3 = (x3 + x2) / 2;
                            int medianeY3 = (y3 + y2) / 2;


                            if(getTableau()[medianeY][medianeX] == 'X'){model.setNbrPionPlayer1(model.getNbrPionPlayer1() - 1);}
                            else if(getTableau()[medianeY][medianeX] == 'O') {model.setNbrPionPlayer2(model.getNbrPionPlayer2() -1);}
                            model.setPlateauJeu(medianeX, medianeY, model.EMPTY);

                            if(getTableau()[medianeY2][medianeX2] == 'X'){model.setNbrPionPlayer1(model.getNbrPionPlayer1() - 1);}
                            else if(getTableau()[medianeY2][medianeX2] == 'O') {model.setNbrPionPlayer2(model.getNbrPionPlayer2() -1);}
                            model.setPlateauJeu(medianeX2, medianeY2, model.EMPTY);

                            if(getTableau()[medianeY3][medianeX3] == 'X'){model.setNbrPionPlayer1(model.getNbrPionPlayer1() - 1);}
                            else if(getTableau()[medianeY3][medianeX3] == 'O') {model.setNbrPionPlayer2(model.getNbrPionPlayer2() -1);}
                            model.setPlateauJeu(medianeX3, medianeY3, model.EMPTY);



                        }

                        model.setPlateauJeu(getSelectedX(), getSelectedY(), model.EMPTY);
                        model.setPlateauJeu(colonne, ligne, getactuelPlayer());
                        System.out.println(model.toString());
                        model.pionToDame(x3, y3, model.getactuelPlayer());


                       // Auswahl zurücksetzen
                        model.setSelectedPionX(-1);
                        model.setSelectedPionY(-1);

                        model.setmoveMade(true);
                        model.setIsDame(colonne, ligne, false);

                    }

                }
            }
        }
    }

    /**
     * diese Methode ruft die methode PossibleMoveDame aus dem model, die die bewegungen von der dame verwaltet,
     * und speichert dann für die ausgewählte Dame ihre Bewegung in ein anderes zwei-dimenssionales Array ..auf diese weise
     * verfügt er über die möglichen Bewegungen für diese Dame..und beim nächster Click überprüft er ob die das gewählte Felder einer
     * erlaubte bewegung entspricht. falls ja wird die Bewegungen zugelassen und im Fall einer Aufnahme wird den aufgenommenen SpielStein oder dame aus
     * dem SpielBrett entfernt durch die methode model.setPlateau(x-koordinaten (von dem SpielStein, der aufgenommen werden wird), y-koordinaten, model.Empty)
     * @param colonne colonne entspricht den y-koordinaten des ausgewählten SpielSteins
     * @param ligne ligne entspricht den y-koordinaten des ausgewählten SpielSteins
     */
    public void deplacementDame(int colonne, int ligne) {


        int[][] deplacementsDame = model.possibleMoveDame(getSelectedX(), getSelectedY(), getactuelPlayer());

        for (int[] deplacement : deplacementsDame) {
            int xd = deplacement[0];
            int yd = deplacement[1];

            if (xd == colonne && yd == ligne) {

                if (Math.abs(xd - getSelectedX()) == 2) {

                    int medianeXd = (xd + model.getSelectedPionX()) / 2;
                    int medianeYd = (yd + model.getSelectedPionY()) / 2;

                    model.setPlateauJeu(medianeXd, medianeYd, model.EMPTY);

                    if( getTableau()[medianeYd][medianeXd] == 'D'){model.setNbrDamePlayer1(model.getNbrDamePlayer1() - 1);}
                    else if(getactuelPlayer() == 'D' && getTableau()[medianeYd][medianeXd] == 'O'){model.setNbrPionPlayer2(getNbrPionPlayer2() - 1);}
                    else if( getTableau()[medianeYd][medianeXd] == 'd') {model.setNbrDamePlayer2(model.getNbrDamePlayer2() -1);}
                    else if (getactuelPlayer() == 'd' && getTableau()[medianeYd][medianeXd] == 'X'){model.setNbrPionPlayer1(getNbrPionPlayer1() - 1);}

                } if (Math.abs(xd - getSelectedX()) == 4) {
                    int diffXd = xd - getSelectedX();
                    int diffYd = yd - getSelectedY();

                    //int possibleX = diffXd * 2;
                    // int possibleY = diffYd * 2;
                    int possibleXd1 = getSelectedX() + (diffXd / 2);
                    int possibleYd1 = getSelectedY() + (diffYd / 2);

                    int possibleXd2 = getSelectedX() + (diffXd / 2) * 3;
                    int possibleYd2 = getSelectedY() + (diffYd / 2) * 3;

                    int possibleXd3 = getSelectedX() + (diffXd / 4) * 3;
                    int possibleYd3 = getSelectedY() + (diffYd / 4) * 3;

                    //  model.setPlateauJeu(possibleX, possibleY, model.EMPTY);
                    // Vérifier si les positions intermédiaires sont valides avant de les modifier
                    if (model.isInsideBoard(possibleXd1, possibleYd1)) {
                        //  model.setPlateauJeu(possibleXd1, possibleYd1, model.EMPTY);
                        if (getTableau()[possibleYd1][possibleXd1] == 'D') {
                            model.setNbrDamePlayer1(model.getNbrDamePlayer1() - 1);
                        }  else if(getactuelPlayer() == 'D' && getTableau()[possibleYd1][possibleXd1] == 'O'){model.setNbrPionPlayer2(getNbrPionPlayer2() - 1);}
                        else if (getTableau()[possibleYd1][possibleXd1] == 'd') {
                            model.setNbrPionPlayer2(model.getNbrPionPlayer2() - 1);
                        }
                        else if (getactuelPlayer() == 'd' && getTableau()[possibleYd1][possibleXd1] == 'X'){model.setNbrPionPlayer1(getNbrPionPlayer1() - 1);}
                    }

                    if (model.isInsideBoard(possibleXd2, possibleYd2)) {
                        model.setPlateauJeu(possibleXd2, possibleYd2, model.EMPTY);
                        if (getTableau()[possibleYd2][possibleXd2] == 'D') {
                            model.setNbrDamePlayer1(model.getNbrDamePlayer1() - 1);
                        }  else if(getactuelPlayer() == 'D' && getTableau()[possibleYd2][possibleXd2] == 'O'){model.setNbrPionPlayer2(getNbrPionPlayer2() - 1);}
                        else if (getTableau()[possibleYd2][possibleXd2] == 'd') {
                            model.setNbrDamePlayer2(model.getNbrDamePlayer2() - 1);
                        }
                        else if(getactuelPlayer() == 'd' && getTableau()[possibleYd2][possibleXd2] == 'X'){model.setNbrPionPlayer1(getNbrPionPlayer1() - 1);}
                    }

                    if (model.isInsideBoard(possibleXd3, possibleYd3)) {
                        model.setPlateauJeu(possibleXd3, possibleYd3, model.EMPTY);
                        if (getTableau()[possibleYd3][possibleXd3] == 'D') {
                            model.setNbrDamePlayer1(model.getNbrDamePlayer1() - 1);
                        }  else if(getactuelPlayer() == 'D' && getTableau()[possibleYd3][possibleXd3] == 'O'){model.setNbrPionPlayer2(getNbrPionPlayer2() - 1);}
                        else if (getTableau()[possibleYd3][possibleXd3] == 'd') {
                            model.setNbrDamePlayer2(model.getNbrDamePlayer2() - 1);
                        }
                        else if(getactuelPlayer() == 'd' && getTableau()[possibleYd3][possibleXd3] == 'X'){model.setNbrPionPlayer1(getNbrPionPlayer1() - 1);}
                    }
                } else if (Math.abs(xd - getSelectedX()) == 3 ||
                        Math.abs(xd - getSelectedX()) == 5 ||
                        Math.abs(xd - getSelectedX()) == 6) {
                    int diffX = xd - getSelectedX();
                    int diffY = yd - getSelectedY();

                    int possibleXd1 = getSelectedX() + (diffX / Math.abs(diffX)) * (Math.abs(diffX) / 3);
                    int possibleYd1 = getSelectedY() + (diffY / Math.abs(diffY)) * (Math.abs(diffY) / 3);

                    int possibleXd2 = getSelectedX() + (diffX / Math.abs(diffX)) * (Math.abs(diffX) / 3) * 2;
                    int possibleYd2 = getSelectedY() + (diffY / Math.abs(diffY)) * (Math.abs(diffY) / 3) * 2;

                    int possibleXd3 = getSelectedX() + (diffX / Math.abs(diffX)) * (Math.abs(diffX) / 3) * 4;
                    int possibleYd3 = getSelectedY() + (diffY / Math.abs(diffY)) * (Math.abs(diffY) / 3) * 4;

                    // Vider les positions intermédiaires
                    model.setPlateauJeu(possibleXd1, possibleYd1, model.EMPTY);
                    if (model.isInsideBoard(possibleXd1, possibleYd1) && getTableau()[possibleYd1][possibleXd1] == 'D') {
                        model.setNbrDamePlayer1(model.getNbrDamePlayer1() - 1);
                    }  else if(getactuelPlayer() == 'D' && getTableau()[possibleYd1][possibleXd1] == 'O'){model.setNbrPionPlayer2(getNbrPionPlayer2() - 1);}
                    else if (model.isInsideBoard(possibleXd1, possibleYd1) && getTableau()[possibleYd2][possibleXd2] == 'd') {
                        model.setNbrDamePlayer2(model.getNbrDamePlayer2() - 1);
                    }
                    else if(model.isInsideBoard(possibleXd1, possibleYd1) && getactuelPlayer() == 'd' && getTableau()[possibleYd1][possibleXd1] == 'X'){model.setNbrPionPlayer1(getNbrPionPlayer1() - 1);}

                    model.setPlateauJeu(possibleXd2, possibleYd2, model.EMPTY);
                    if (model.isInsideBoard(possibleXd2, possibleYd2) && getTableau()[possibleYd2][possibleXd2] == 'D') {
                        model.setNbrDamePlayer1(model.getNbrDamePlayer1() - 1);
                    }  else if(getactuelPlayer() == 'D' && getTableau()[possibleYd2][possibleXd2] == 'O'){model.setNbrPionPlayer2(getNbrPionPlayer2() - 1);}
                    else if (model.isInsideBoard(possibleXd2, possibleYd2) && getTableau()[possibleYd2][possibleXd2] == 'd') {
                        model.setNbrDamePlayer2(model.getNbrDamePlayer2() - 1);
                    }
                    else if(model.isInsideBoard(possibleXd2, possibleYd2) && getactuelPlayer() == 'd' && getTableau()[possibleYd1][possibleXd1] == 'X'){model.setNbrPionPlayer1(getNbrPionPlayer1() - 1);}

                    model.setPlateauJeu(possibleXd3, possibleYd3, model.EMPTY);
                    if (model.isInsideBoard(possibleXd3, possibleYd3) && getTableau()[possibleYd3][possibleXd3] == 'D') {
                        model.setNbrDamePlayer1(model.getNbrDamePlayer1() - 1);
                    }  else if(model.isInsideBoard(possibleXd3, possibleYd3) && getactuelPlayer() == 'D' && getTableau()[possibleYd3][possibleXd3] == 'O'){model.setNbrPionPlayer2(getNbrPionPlayer2() - 1);}
                    else if (model.isInsideBoard(possibleXd3, possibleYd3) && getTableau()[possibleYd3][possibleXd3] == 'd') {
                        model.setNbrDamePlayer2(model.getNbrDamePlayer2() - 1);
                    }
                    else if(model.isInsideBoard(possibleXd3, possibleYd3) && getactuelPlayer() == 'd' && getTableau()[possibleYd1][possibleXd1] == 'X'){model.setNbrPionPlayer1(getNbrPionPlayer1() - 1);}

                }


                model.setPlateauJeu(getSelectedX(), getSelectedY(), model.EMPTY);
                model.setPlateauJeu(colonne, ligne, getactuelPlayer());
                System.out.println(model.toString());

                model.setSelectedPionX(-1);
                model.setSelectedPionY(-1);
                model.setmoveMade(true);
                model.setIsDame(colonne, ligne, false);
            }
        }
    }


    /**
     * verwaltet die Benutzereingaben
     * @param key key
     */
    public void handleUserInput (char key) {

        switch (model.getState()){

            case START, GAME_OVER -> {
                if(key == ' ') {
                    model.setState(Gamestate.PLAYING);
                    model.newgame();
                    timer.startTimer();
                    System.out.println("Début du jeu");
                }
            }
            case PLAYING -> {
                view.mousePressed();
            }
            default -> throw new IllegalStateException("Unexpected value: " + model.getState());
        }
    }

    /**
     * Ruft die Zeichenmethoden der view auf, abhängig vom aktuellen Spielzustand.
     */
    public void handleDisplay (){
        switch (model.getState()){
            case START -> {
                view.drawStartScreen();
            }
            case PLAYING -> {

                view.decor();
                view.drawTimer();
                view.infoPlayer1();
                view.infoPlayer2();
                view.infoPlayer1Et2();
                view.dessinerTableau();
                view.dessinerPion();


            }

            case GAME_OVER -> {
                timer.endSpielByTimer();
                 timer.stopTimer();
                System.out.println("Fin du jeu");

            }
            default -> throw new IllegalStateException("Unexcepted value: " + model.getState());


        }
    }


    /**
     * gibt den Gewinner des Spiels zurück
     *
     * @return model.getWinnerbeiGameOver()
     */
    public char getWinnerBeiGameOver(){
        return model.getWinnerBeiGameOver();
    }


    /**
     * gibt die verbleibende Zeit zum Spielen zurück
     *
     * @return model.getRemainingTime()
     */
    public int getRemainingTimer(){
        return model.getRemainingTime();
    }

    /**
     * gibt die Anzall der Spielsteine der ersten Player zurück
     *
     * @return model.getNbrPionPlayer1
     */
    public int getNbrPionPlayer1(){return model.getNbrPionPlayer1();}

    /**
     * gibt die Anzall der Spielsteine der zweiten Player zurück
     *
     * @return model.getNbrPionPlayer2()
     */
    public int getNbrPionPlayer2(){return model.getNbrPionPlayer2();}

    /**
     * gibt die Anzahl der Dame des zweiten Spielers
     *
     * @return model.getNbrDamePlayer2()
     */
    public int getNbrDamePlayer2(){return model.getNbrDamePlayer2();}

    /**
     * gibt die Anzahl der Dame des zweiten Spielers
     *
     * @return model.getNbrDamePlayer1()
     */
    public int getNbrDamePlayer1(){return model.getNbrDamePlayer1();}

    /**
     * gibt die Anzahl der Siege des ersten Spielers zurück
     *
     * @return model.getVictoirePlayer1()
     */
    public int getVictoirePlayer1(){return model.getVictoirePlayer1();}

    /**
     * gibt die Anzahl der Siege des zweiten Spielers zurück
     *
     * @return model.getVictoirePlayer2()
     */
    public int getVictoirePlayer2(){return model.getVictoirePlayer2();}

    /**
     * gibt die Anzahl der gespielten Runde zurück
     *
     * @return model.getManche()
     */
    public int getManche(){return model.getManche();}

}


