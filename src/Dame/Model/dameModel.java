package Dame.Model;

import java.util.ArrayList;


/**
 * enthält die Logik des Spiels
 */

public class dameModel {

    /**
     * EMPTY stellt die leeren Felder dar, die sich in der Mitte des SpielBretts zu Begin des Spiels befinden
     */
    public final char EMPTY = '_'; // case du milieu au debut du jeu
    /**
     * PLAYER1 stellt der erste Spieler dar
     */
    public final char PLAYER1 = 'X'; //joueur gris en haut
    /**
     * PLAYER2 stellt der zweite Spieler dar
     */
    public final char PLAYER2 = 'O'; // joueur blanc en bas
    /**
     * LEER stellt die weißen Felder dar, wo Bewegungen unerlaubt sind
     */
    public final char LEER = '.'; //case blanches ou les deplacements ne sont pas autorisee
    /**
     * DAMEPLAYER1 stellt die Dame des ersten Spielers dar
     */
    public final char DAMEPLAYER1 = 'D';
    /**
     * DAMEPLAYER2 stellt die Dame des zweitens Spielers dar
     */
    public final char DAMEPLAYER2 = 'd';
    /**
     * actuelPlayer steht für den aktuellen Spieler
     */
    private char actuelPlayer = PLAYER1;
    /**
     * isDame bestimmt, ob ein Feld eine Dame enthält oder nicht
     */
    private boolean[][] isDame;
    /**
     * nbrPioPlayer1 steht für die Anzahl der Spielsteine des ersten Players
     */
    private int nbrPionPlayer1 = 12;
    /**
     * nbrPioPlayer2 steht für die Anzahl der Spielsteine des zweiten Players
     */
    private int nbrPionPlayer2 = 12;
    /**
     * nbrDamePlayer1 steht für die Anzahl der Dame des ersten Players
     */
    private int nbrDamePlayer1 = 0;
    /**
     * nbrDamePlayer2 steht für die Anzahl der Dame des zweiten Players
     */
    private int nbrDamePlayer2 = 0;
    /**
     * remainingTime steht für die maximale Spieldauer
     */
    private int remainingTime;
    /**
     * victoirePlayer1 steht für die Anzahl der Siege des ersten Spielers.
     */
    private int victoirePlayer1 = 0;
    /**
     * victoirePlayer2 steht für die Anzahl der Siege des ersten Spielers.
     */
    private int victoirePlayer2 = 0;

    /**
     * manche stellt die Anzahl der Spielrunden dar.
     */
    private int manche = 1;
    /**
     * previousWinner steht für den Gewinner des vorherigen Spiels.
     */
    private char previousWinner;
    /**
     * Unser Array deplacement enthällt alle möglichen bewegungen eines Spielsteins von seinen Koordinaten aus
     */
    private ArrayList<int[]> deplacement;
    /**
     * possibleMoves AfterCapture enthällt die neuen möglichen bewegungen eines Spielsteins nach einer Bewegung
     */
    private ArrayList<int[]> possibleMovesAfterCapture;
    /**
     * gamestates stellt für das SpielBrett dar
     */
    private char[][] gamestates;
    /**
     * @adversaire steht für den Gegner des aktuelen Spieler
     */
    private char adversaire;
    /**
     * @param prisePossible bestimmt, ob ein Schlagen für einen einfachen Spielstein möglich ist oder nicht
     */
    private boolean prisePossible = false;
    /**
     * @param DamePrisePossible bestimmt, ob ein Schlagen für eine Dame möglich ist oder nicht
     */
    private boolean DamePrisePossible = false;
    /**
     * moveMade bestimmt, ob eine Bewegung sei es von einem einfachen SpielSteine oder eine Dame gemacht wurde oder nicht
     */
    private boolean moveMade = false;
    /**
     * @param state stellt den Zustand des Spiels dar
     */
    Gamestate state;

    /**
     * @param selectedPionsX stellt den Auswahlstatus eines SpielSteins nach ihrer x-Koordinate dar.. die Vlue -1 bedeutet, dass der Speilstein nicht ausgewählt wurde.
     */
    private int selectedPionX = -1; //pr savoir si un pion a ete selectionne ou pas..je declare ca ici prcq je veux a la fois l'utiliser ds le view et le controller
    /**
     * @param selectedPionsY stellt den Auswahlstatus eines SpielSteins nach ihrer y-Koordinate dar.. die Vlue -1 bedeutet, dass der Speilstein nicht ausgewählt wurde.
     */
    private int selectedPionY = -1;

    /**
     * Construktor dameModel der Klasse dameModel initialisiert den SpielZustand und startet ein neues Spiel
     */
    public dameModel() {
        setState(Gamestate.START);
        newgame();
    }

    /**
     * newgame initialisiert alle notwendigen Methoden zum Starten eines neuen Spiels
     */
    public void newgame() {

        remainingTime = 10;
        isDame = new boolean[8][8];
        gamestates = new char[8][8];
        InitPlateaujeu();
        setNbrPionPlayer1(12);
        setNbrPionPlayer2(12);
        setNbrDamePlayer1(0);
        setNbrDamePlayer2(0);
        incrementVictories();


    }

    /**
     * InitPlateau initialisiert das SpielBrett zu begin des Spiels
     */
    public void InitPlateaujeu() {
        for (int i = 0; i < gamestates.length; i++) {
            for (int j = 0; j < gamestates.length; j++) {
                isDame[i][j] = false;

                if ((i + j) % 2 == 0) {
                    gamestates[i][j] = LEER;
                } else {
                    if (i < 3) {
                        gamestates[i][j] = PLAYER1;
                    } else if (i > 4) {
                        gamestates[i][j] = PLAYER2;
                    } else {
                        gamestates[i][j] = EMPTY;
                    }
                }
            }
        }
    }

    /**
     * getPlateau gibt das SpielBrett zurückt
     *
     * @return gamestates, die hier das SpielBrett darstellt
     */
    public char[][] getPlateauJeu() {

        return gamestates;

    }

    /**
     * gibt die Anzall der Spielsteine der ersten Player zurück
     *
     * @return nbrPionPlayer1 (stellt die anzahl der Spielsteine der ersten Player dar)
     */
    public int getNbrPionPlayer1() {
        return nbrPionPlayer1;
    }

    /**
     * gibt die Anzall der Spielsteine der zweiten Player zurück
     *
     * @return nbrPionPlayer2 (stellt die anzahl der Spielsteine der zweiten Player dar)
     */
    public int getNbrPionPlayer2() {
        return nbrPionPlayer2;
    }

    /**
     * gibt den Gewinner des vorherigen Spiels zurück
     *
     * @return previousWinner
     */
    public char getPreviousWinner() {
        return previousWinner;
    }

    /**
     * dient zur Reinitialisierung der value von previousWinner
     *
     * @param previousWinner
     */
    public void setPreviousWinner(char previousWinner) {
        this.previousWinner = previousWinner;
    }

    /**
     * gibt die Anzahl der Dame des zweiten Spielers
     *
     * @return nbrDamePlayer2
     */
    public int getNbrDamePlayer2() {
        return nbrDamePlayer2;
    }

    /**
     * gibt die Anzahl der Dame des zweiten Spielers
     *
     * @return nbrDamePlayer1
     */
    public int getNbrDamePlayer1() {
        return nbrDamePlayer1;
    }

    /**
     * gibt die Anzahl der Siege des ersten Spielers zurück
     *
     * @return victoirePlayer1;
     */
    public int getVictoirePlayer1() {
        return victoirePlayer1;
    }

    /**
     * gibt die Anzahl der Siege des zweiten Spielers zurück
     *
     * @return victoirePlayer2
     */
    public int getVictoirePlayer2() {
        return victoirePlayer2;
    }

    /**
     * gibt die Anzahl der gespielten Runde zurück
     *
     * @return manche
     */
    public int getManche() {
        return manche;
    }

    /**
     * dient zur Reinitialisierung der value von nbrPionPlayer1
     *
     * @param nbrPionPlayer1 nbrPionPlayer1
     */
    public void setNbrPionPlayer1(int nbrPionPlayer1) {
        this.nbrPionPlayer1 = nbrPionPlayer1;
    }

    /**
     * dient zur Reinitialisierung der value von nbrPionPlayer2
     *
     * @param nbrPionPlayer2 nbrPionPlayer2
     */
    public void setNbrPionPlayer2(int nbrPionPlayer2) {
        this.nbrPionPlayer2 = nbrPionPlayer2;
    }

    /**
     * dient zur Reinitialisierung der value von nbrDamePlayer1
     *
     * @param nbrDamePlayer1 nbrDamePlayer1
     */
    public void setNbrDamePlayer1(int nbrDamePlayer1) {
        this.nbrDamePlayer1 = nbrDamePlayer1;
    }

    /**
     * dient zur Reinitialisierung der value von nbrDamePlayer2
     *
     * @param nbrDamePlayer2 nbrDamePlayer2
     */
    public void setNbrDamePlayer2(int nbrDamePlayer2) {
        this.nbrDamePlayer2 = nbrDamePlayer2;
    }

    /**
     * gibt die verbleibende Zeit zum Spielen zurück
     *
     * @return remainingTime
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    /**
     * dient zur Reinitialisierung der value von remainingTime
     *
     * @param remainingTime remainingTime
     */
    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    /**
     * dient zur Reinitialisierung der value von victoirePlayer1
     *
     * @param victoirePlayer1 victoirePlayer1
     */
    public void setVictoirePlayer1(int victoirePlayer1) {
        this.victoirePlayer1 = victoirePlayer1;
    }

    /**
     * dient zur Reinitialisierung der value von victoirePlayer2
     *
     * @param victoirePlayer2 victoirePlayer2
     */
    public void setVictoirePlayer2(int victoirePlayer2) {
        this.victoirePlayer2 = victoirePlayer2;
    }

    /**
     * dient zur Reinitialisierung der value von manche
     *
     * @param manche manche
     */
    public void setManche(int manche) {
        this.manche = manche;
    }

    /**
     * dient zur Reinitialisierung der value von gamestates
     *
     * @param colonne colonne stellt die Spalte des Spielbretts dar
     * @param ligne   ligne stellt die Spalte des Spielbretts dar
     * @param joueur  joueur  stellt den actuelen Spielr dar
     */
    public void setPlateauJeu(int colonne, int ligne, char joueur) {
        if (colonne >= 0 && colonne < 8 && ligne >= 0 && ligne < 8) {
            gamestates[ligne][colonne] = joueur;
        }
    }

    /**
     * dient zur Reinitialisierung der value von selectedPionX
     *
     * @param selectedPionX selestedPionX
     */
    public void setSelectedPionX(int selectedPionX) {
        this.selectedPionX = selectedPionX;
    }

    /**
     * gibt der x-Koordinate des ausgewählten SpielStein
     *
     * @return selectedPionX
     */
    public int getSelectedPionX() {
        return selectedPionX;
    }

    /**
     * dient zur Reinitialisierung der value von selectedPionX
     */
    public void setSelectedPionY(int selectedPionY) {
        this.selectedPionY = selectedPionY;
    }

    /**
     * gibt der y-Koordinate des ausgewählten SpielStein
     *
     * @return selectedPionY
     */
    public int getSelectedPionY() {
        return selectedPionY;
    }

    /**
     * gibt an, ob eine Bewegung gemacht wurde oder nicht
     *
     * @return moveMade
     */
    public boolean getmoveMade() {
        return moveMade;
    }

    /**
     * dient zur Reinitialisierung der value von moveMade
     *
     * @param moveMade moveMade
     */
    public void setmoveMade(boolean moveMade) {
        this.moveMade = moveMade;
    }

    /**
     * dient zur Reinitialisierung der value von prisePossible
     *
     * @param prisePossible prisePossible
     */
    public void setPrisePossible(boolean prisePossible) {
        this.prisePossible = prisePossible;
    }

    /**
     * gibt an, ob einen Zug oder Schlag für einen einfachen Spielstein möglich ist oder nicht
     *
     * @return prisePossible
     */
    public boolean getPrisePossible() {
        return prisePossible;
    }

    /**
     * dient zur Reinitialisierung der value von DamePrisePossible
     *
     * @param Dameprise DamePrise
     */
    public void setDamePrisePossible(boolean Dameprise) {
        this.DamePrisePossible = Dameprise;
    }

    /**
     * gibt an, ob eine Aufnahme für eine Dame möglich ist oder nicht
     *
     * @return DamePrisePossible
     */
    public boolean getDamePrisePossible() {
        return DamePrisePossible;
    }

    /**
     * gibt an, ob ein Feld eine Dame enthält oder nicht
     *
     * @return isDame
     */
    public boolean[][] getIsDame() {
        return isDame;
    }

    /**
     * dient zur Reinitialisierung der value von isDame
     *
     * @param x     x-kordinaten eines Feldes
     * @param y     y-koordinaten eines Feldes
     * @param value value (true: bedeutet das Feld enthält eine Dame und false: das gegenteil
     */
    public void setIsDame(int x, int y, boolean value) {
        isDame[y][x] = value;

    }

    /**
     * gibt den Zustand des Spiels zurück
     *
     * @return state
     */
    public Gamestate getState() {
        return state;
    }

    /**
     * dient zur Reinitialisierung der value von state
     *
     * @param state state
     */
    public void setState(Gamestate state) {
        this.state = state;
    }

    /**
     * Unsere methode PossibleMovePion bestimmt für einfachen SpielStein von seinen Koordinaten aus seine möglichen bewegungen mit oder ohne Aufnahme
     *
     * @param x      x-koordinaten des ausgewählten SpielSteins
     * @param y      Y-koordinaten des ausgewählten SpielSteins
     * @param joueur Aktueller Spieler
     * @return eine Liste von allen möglichen Bewegungen für den gewählten SpielStein
     */
    public int[][] PossibleMovePion(int x, int y, char joueur) {
        deplacement = new ArrayList<>();

        // ist Spieler jr1 wenn ja sein Gegner ist jr2 sonst ist es jr1

        if (joueur == PLAYER1) {
            adversaire = PLAYER2;
        } else if (joueur == PLAYER2) {
            adversaire = PLAYER1;
        }


        // Überprüfung der möglichen diagonalen Züge für den aktuellen Spieler.
        // Wenn der Spieler unten auf dem Brett steht, werden die Züge für ihn umgekehrt.

        int direction = (joueur == PLAYER1) ? 1 : -1;


        //Überprüfung einfacher diagonaler Züge (ohne Fressen / aufnahme)
        if (y + direction >= 0 && y + direction < 8) {// wir bleiben innerhalb der Grenzen unseres Spiels, d.h. unseres Heights, um Überschreitungen zu vermeiden.

            if (x > 0 && gamestates[y + direction][x - 1] == EMPTY) { // überprüft, ob das diagonal obere linke Feld leer ist (enthält '_'), was bedeutet, dass ein Zug möglich ist.
                deplacement.add(new int[]{x - 1, y + direction});
            }
            if (x < 7 && gamestates[y + direction][x + 1] == EMPTY) {
                deplacement.add(new int[]{x + 1, y + direction});
            }
        }

        //Überprüfung mit Aufnahme der SpielSteine
        if (y + 2 * direction >= 0 && y + 2 * direction < 8) {// le fois 2 parceque avec bouffe ou prise de pions adverses le deplacement s'effectura chaque fois de 2 cases
            if (x > 1 && (gamestates[y + direction][x - 1] == adversaire || gamestates[y + direction][x - 1] == (getactuelPlayer() == PLAYER1?DAMEPLAYER2:DAMEPLAYER1) ) && gamestates[y + 2 * direction][x - 2] == EMPTY) { // x > 1 le jr adverse doit etre au moins a la 2e ligne
                deplacement.add(new int[]{x - 2, y + 2 * direction});
                setPrisePossible(true);

            }

            if (x < 6 && (gamestates[y + direction][x + 1] == adversaire || gamestates[y + direction][x + 1] == (getactuelPlayer() == PLAYER1?DAMEPLAYER2:DAMEPLAYER1)) && gamestates[y + 2 * direction][x + 2] == EMPTY) {
                deplacement.add(new int[]{x + 2, y + 2 * direction});
                setPrisePossible(true);

            }
        }


        int[][] deplacementPion = new int[deplacement.size()][2];
        for (int i = 0; i < deplacement.size(); i++) {
            deplacementPion[i] = deplacement.get(i);
        }

        return deplacementPion;

    }

    /**
     * Unsere methode PossibleMovePionAfterCapture bestimmt für einfachen SpielStein nach einer ersten Bewegung mit aufnahme von seinen neuen Koordinaten aus seine neuen möglichen bewegungen mit oder ohne Aufnahme
     *
     * @param x      x-koordinaten der ausgewälten SpielStein
     * @param y      Y-koordinaten der ausgewälten SpielStein
     * @param joueur Aktue Spieler
     * @return eine Liste von allen möglichen Bewegungen für den gewählten SpielStein
     */
    public int[][] getPossibleMovePionAfterCaptures(int x, int y, char joueur) {
        possibleMovesAfterCapture = new ArrayList<>();

        int direction = (joueur == PLAYER1) ? 1 : -1;

        //Überprüfung mit Aufnahme der SpielSteine
        if (y + 2 * direction >= 0 && y + 2 * direction < 8) {// le fois 2 parceque avec bouffe ou prise de pions adverses le deplacement s'effectura chaque fois de 2 cases
            if (x > 1 && (gamestates[y + direction][x - 1] == adversaire || gamestates[y + direction][x - 1] == (getactuelPlayer() == PLAYER1?DAMEPLAYER2:DAMEPLAYER1)) && gamestates[y + 2 * direction][x - 2] == EMPTY) { // x > 1 le jr adverse doit etre au moins a la 2e ligne
                possibleMovesAfterCapture.add(new int[]{x - 2, y + 2 * direction});
                setPrisePossible(true);
            }

            if (x < 6 && (gamestates[y + direction][x + 1] == adversaire || gamestates[y + direction][x + 1] == (getactuelPlayer() == PLAYER1?DAMEPLAYER2:DAMEPLAYER1))&& gamestates[y + 2 * direction][x + 2] == EMPTY) {
                possibleMovesAfterCapture.add(new int[]{x + 2, y + 2 * direction});
                setPrisePossible(true);
            }
        }
        // umwandlung der Liste der möglichen Bewegungen in eine 2D-Tabelle.
        int[][] movesArray = new int[possibleMovesAfterCapture.size()][2];
        for (int i = 0; i < possibleMovesAfterCapture.size(); i++) {
            movesArray[i] = possibleMovesAfterCapture.get(i);
        }

        return movesArray;


    }

    /**
     * dient zur Reinitialisierung der aktuelen Spieler
     *
     * @param actuelPlayer actuelPlayer
     */
    public void setActuelPlayer(char actuelPlayer) {
        this.actuelPlayer = actuelPlayer;
    }

    /**
     * gibt den aktuelen Spieler zurück
     *
     * @return actuelPlayer
     */
    public char getactuelPlayer() {
        return actuelPlayer;
    }

    /**
     * Diese Methode behandelt Spielerwechsel.
     *
     * @param moveTrue moveTrue
     */
    public void changePlayer(boolean moveTrue) {
        if (moveTrue) {
            if (getactuelPlayer() == PLAYER1 || getactuelPlayer() == DAMEPLAYER1) {
                setActuelPlayer(PLAYER2);
            } else if (getactuelPlayer() == PLAYER2 || getactuelPlayer() == DAMEPLAYER2) {
                setActuelPlayer(PLAYER1);
            }
        }

    }

    /**
     * gib an, ob eine Aufnahme beim Bewegen möglich ist
     *
     * @param x      x-koordinaten der ausgewälten SpielStein
     * @param y      Y-koordinaten der ausgewälten SpielStein
     * @param player Aktuel Spieler
     * @return true oder false
     */
    public boolean isMoveAndBouffe(int x, int y, char player) {
        int selectedX = getSelectedPionX();
        int selectedY = getSelectedPionY();

        int[][] possibleMoves = PossibleMovePion(getSelectedPionX(), getSelectedPionY(), getactuelPlayer());

        for (int[] move : possibleMoves) {
            if (move[0] == x && move[1] == y) {
                int dx = (x - selectedX) / 2; // Calcul des déplacements horizontaux
                int dy = (y - selectedY) / 2; // Calcul des déplacements verticaux


                // Vérification si la case médiane est occupée par un pion adverse
                if (gamestates[selectedY + dy][selectedX + dx] != EMPTY &&
                        gamestates[selectedY + dy][selectedX + dx] != player) {
                    return true; // Un pion adverse est présent pour être capturé
                }
            }
        }

        return false; // Aucune capture possible pour ce déplacement
    }

    /**
     * dient zur Umwandlund eines SpielSteins zu einer Dame
     *
     * @param x      x-koordinaten der ausgewälten SpielStein
     * @param y      Y-koordinaten der ausgewälten SpielStein
     * @param player Aktuel Spieler
     */
    public void pionToDame(int x, int y, char player) {

        if ((player == PLAYER1 && y == 7 && gamestates[y][x] == 'X') ||
                (player == PLAYER2 && y == 0 && gamestates[y][x] == 'O')) {

            // Aktualisiert den neuen Zustand der alte SpielStein zu einer Dame
            gamestates[y][x] = (player == PLAYER1) ? 'D' : 'd';

        }
        if (gamestates[y][x] == 'D') {
            nbrDamePlayer1++;
        } else if (gamestates[y][x] == 'd') {
            nbrDamePlayer2++;
        }

        //steht nur für die debuggage
        System.out.println("x: " + x + " y: " + y);
      /*  for (int i = 0; i < isDame.length; i++) {
            for (int j = 0; j < isDame.length; j++) {
                System.out.println(" case : x = " + j + " j = " + i + " " + isDame[i][j]);
            }
        }*/

    }


    /**
     * Unsere methode PossibleMoveDame bestimmt für eine von seinen Koordinaten aus seine möglichen bewegungen
     *
     * @param x      x-koordinaten der ausgewälten Dame
     * @param y      Y-koordinaten der ausgewälten Dame
     * @param player Aktuel Spieler
     * @return eine Liste von allen möglichen Bewegungen für den gewählten SpielStein
     */
    public int[][] possibleMoveDame(int x, int y, char player) {
        ArrayList<int[]> deplacements = new ArrayList<>();

        // Déplacements dans toutes les directions (avec et sans prise)
        int[][] directions = {{-1, 1}, {1, 1}, {-1, -1}, {1, -1}};

        for (int[] direction : directions) {
            int dx = direction[0];
            int dy = direction[1];

            for (int i = 1; i < 8; i++) {
                int newX = x + i * dx;
                int newY = y + i * dy;

                if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                    if (gamestates[newY][newX] == EMPTY) {
                        deplacements.add(new int[]{newX, newY});
                    } else if (gamestates[newY][newX] != (player == 'D' ? PLAYER1 : PLAYER2)) {
                        // Case occupée par un pion adverse, vérifie si la case suivante est vide
                        int nextX = x + (i + 1) * dx;
                        int nextY = y + (i + 1) * dy;

                        if (nextX >= 0 && nextX < 8 && nextY >= 0 && nextY < 8 &&
                                gamestates[nextY][nextX] == EMPTY) {
                            deplacements.add(new int[]{nextX, nextY});
                            setDamePrisePossible(true);
                        } else {
                            break;  // Arrête la boucle si la prise n'est pas possible
                        }
                    } else {
                        break;  // Arrête la boucle si la case est occupée par le joueur actuel
                    }
                } else {
                    break;  // Arrête la boucle si on dépasse les limites du plateau
                }
            }
        }

        int[][] deplacementDlaDame = new int[deplacements.size()][2];
        for (int i = 0; i < deplacements.size(); i++) {
            deplacementDlaDame[i] = deplacements.get(i);
        }

        return deplacementDlaDame;
    }

    /**
     * gibt an, ob ein Player gewonnen hat
     *
     * @param player
     * @return true oder false
     */
    public boolean isPlayerWin(char player) {
        if (player == PLAYER1 && getNbrPionPlayer2() == 0) {
            return true;
        } else if (player == PLAYER2 && getNbrPionPlayer1() == 0) {
            return true;
        } else if (player == PLAYER1 && getNbrPionPlayer1() > getNbrPionPlayer2()) {
            return true;
        } else if (player == PLAYER2 && getNbrPionPlayer2() > getNbrPionPlayer1()) {
            return true;
        }
        return false;

    }

    /**
     * gibt an, ob eine Bewegung für einen SpielStein möglich ist oder nicht
     *
     * @param player player
     * @return player
     */
    public boolean isNoMovePossible(char player) {

        for (int i = 0; i < gamestates.length; i++) {
            for (int j = 0; j < gamestates.length; j++) {
                if (gamestates[i][j] == player) {
                    int[][] possibleMove = PossibleMovePion(i, j, player);

                    if (possibleMove.length > 0) {
                        return false;
                    } // besizt mindestens eine erlaubte Bewegung
                }
            }
        }
        return true; //keine Bewegung möglich
    }

    /**
     * gibt den Gewinner des Spiels zurück
     *
     * @return gewinner
     */
    public char getWinnerBeiGameOver() {
        // Vérifie si le joueur actuel a mangé tous les pions adverses
        if (isPlayerWin(PLAYER1)) {
            //setVictoirePlayer1(getVictoirePlayer1() + 1);
            return PLAYER1;
        } else if (isPlayerWin(PLAYER2)) {
            // setVictoirePlayer2(getVictoirePlayer2() + 1);
            return PLAYER2;
        }

        System.out.println("previous winner: " + previousWinner);
        System.out.println("Player2 pions: " + getNbrPionPlayer2());
        // Vérifiez si le joueur actuel n'a plus de mouvements possibles
        if (isNoMovePossible(getactuelPlayer())) {
            setState(Gamestate.GAME_OVER);
            return getactuelPlayer() == PLAYER1 ? PLAYER2 : PLAYER1;
        }
        return ' '; // Aucun gagnant, le jeu continue


    }

    /**
     * dient zur Aktualisierung der Anzahl der Siege sei es vom Player1 oder Player1
     * und aktualisiert zugleich die Anzahl der gespielten Runde..
     */
    public void incrementVictories() {

        if (previousWinner == PLAYER1) {
            setVictoirePlayer1(getVictoirePlayer1() + 1);
            setManche(getManche() + 1);
        } else if (previousWinner == PLAYER2) {
            setVictoirePlayer2(getVictoirePlayer2() + 1);
            setManche(getManche() + 1);
        } else if (previousWinner == ' ') {
            setManche(getManche() + 1);
        }
    }

    /**
     * gibt an, ob sich einen ausgewählten SpielStein innerhalb der Grenzen unseres Spiels befindet
     *
     * @param x x-koordinaten des ausgewählten SpielStein
     * @param y y-koordinaten des ausgewählten SpielStein
     * @return true oder false
     */
    public boolean isInsideBoard(int x, int y) {
        return x >= 0 && x < gamestates[0].length && y >= 0 && y < gamestates.length;
    }

    /**
     * Zeichnet das SpielBrett in der Konsole
     *
     * @return das SpielBrett
     */
    @Override
    public String toString() {
        return String.format("%c %c %c %c %c %c %c %c\n%c %c %c %c %c %c %c %c\n%c %c %c %c %c %c %c %c\n%c %c %c %c %c %c %c %c\n%c %c %c %c %c %c %c %c\n%c %c %c %c %c %c %c %c\n%c %c %c %c %c %c %c %c\n%c %c %c %c %c %c %c %c\n",
                gamestates[0][0], gamestates[0][1], gamestates[0][2], gamestates[0][3], gamestates[0][4], gamestates[0][5], gamestates[0][6], gamestates[0][7],
                gamestates[1][0], gamestates[1][1], gamestates[1][2], gamestates[1][3], gamestates[1][4], gamestates[1][5], gamestates[1][6], gamestates[1][7],
                gamestates[2][0], gamestates[2][1], gamestates[2][2], gamestates[2][3], gamestates[2][4], gamestates[2][5], gamestates[2][6], gamestates[2][7],
                gamestates[3][0], gamestates[3][1], gamestates[3][2], gamestates[3][3], gamestates[3][4], gamestates[3][5], gamestates[3][6], gamestates[3][7],
                gamestates[4][0], gamestates[4][1], gamestates[4][2], gamestates[4][3], gamestates[4][4], gamestates[4][5], gamestates[4][6], gamestates[4][7],
                gamestates[5][0], gamestates[5][1], gamestates[5][2], gamestates[5][3], gamestates[5][4], gamestates[5][5], gamestates[5][6], gamestates[5][7],
                gamestates[6][0], gamestates[6][1], gamestates[6][2], gamestates[6][3], gamestates[6][4], gamestates[6][5], gamestates[6][6], gamestates[6][7],
                gamestates[7][0], gamestates[7][1], gamestates[7][2], gamestates[7][3], gamestates[7][4], gamestates[7][5], gamestates[7][6], gamestates[7][7]);
    }

}
