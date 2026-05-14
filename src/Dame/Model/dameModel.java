package Dame.Model;
import Dame.Model.Gamestate;
import Dame.Model.PieceType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Konkrete Implementierung des Models im MVC-Muster.
 *
 * Enthaelt die vollstaendige Spiellogik des Damespiels. Die Methoden sind
 * in verschiedenen Abschnitten unterteilt:
 *
 * Zustandsbehaftete Methoden:
 *   Arbeiten auf dem echten Spielfeld und veraendern den internen Zustand.
 *   Werden vom Controller fuer menschliche Spielerzuege verwendet.
 *
 * Zustandslose Methoden:
 *   Arbeiten ausschliesslich auf uebergebenen Spielfeldkopien.
 *   Veraendern keinen internen Zustand. Werden ausschliesslich von der KI
 *   fuer Simulationen verwendet.
 *
 * Private gemeinsame Methoden:
 *   Reine Hilfsmethoden ohne Zustandsaenderung, die von Abschnitt 1 und 2
 *   gemeinsam genutzt werden. Bilden die einzige Wahrheitsquelle fuer die Regeln.
 *
 * Spielende, Zustand und Getter:
 *   Verwaltung von Spielzustand, Siegbedingungen und Statistiken.
 *
 * @author Dimzz
 * @version 2.0
 * @see IdameModel
 */
public class dameModel implements IdameModel {

    /** Anzahl der Felder pro Reihe und Spalte des Spielfelds. */
    public static final int TAILLE = 8;

    /** Das aktuelle Spielfeld als zweidimensionales Array. */
    private PieceType[][] plateau;

    /** Der aktuell am Zug befindliche Spieler. */
    private PieceType actuelPlayer;

    /** X-Koordinate (Spalte) der aktuell ausgewaehlten Figur. -1 wenn keine ausgewaehlt. */
    private int selectedX = -1;

    /** Y-Koordinate (Zeile) der aktuell ausgewaehlten Figur. -1 wenn keine ausgewaehlt. */
    private int selectedY = -1;

    /** Gibt an, ob der aktuelle Spieler eine Schlagpflicht hat. */
    private boolean prisePossible = false;

    /** Liste der Figuren, die im aktuellen Zug schlagen koennen. */
    private List<int[]> pionsPouvantCapturer = new ArrayList<>();

    /** Anzahl verbleibender Bauern von Spieler 1. */
    private int nbrPionPlayer1  = 12;

    /** Anzahl verbleibender Bauern von Spieler 2. */
    private int nbrPionPlayer2  = 12;

    /** Anzahl der Damen von Spieler 1. */
    private int nbrDamePlayer1  = 0;

    /** Anzahl der Damen von Spieler 2. */
    private int nbrDamePlayer2  = 0;

    /** Gesamtsiege von Spieler 1 ueber alle Runden. */
    private int victoirePlayer1 = 0;

    /** Gesamtsiege von Spieler 2 ueber alle Runden. */
    private int victoirePlayer2 = 0;

    /** Aktuelle Rundennummer. Beginnt bei 1. */
    private int manche          = 1;

    /** Verbleibende Spielzeit in Sekunden. */
    private int remainingTime   = 0;

    /**
     * Gewinner der vorherigen Runde.
     * Wird bei newgame() verwendet, um die Siegzaehler zu aktualisieren.
     */
    private volatile PieceType previousWinner = null;

    /**
     * erhöht die Anzahl der Siege
     */
    private volatile boolean victoiresIncrementees = false;


    /**
     * Erstellt ein neues Model und initialisiert den Startzustand.
     */
    public dameModel() {
        setState(Gamestate.START);
        newgame();
    }

    /**
     * Setzt das Spiel auf den Anfangszustand zurueck.
     * Aktualisiert vorher die Siegzaehler anhand des Gewinners der letzten Runde.
     */
    @Override
    public void newgame() {

        victoiresIncrementees = false;
        previousWinner        = null;

        plateau              = new PieceType[TAILLE][TAILLE];
        actuelPlayer         = PieceType.PION_J1;
        selectedX            = -1;
        selectedY            = -1;
        prisePossible        = false;
        pionsPouvantCapturer = new ArrayList<>();
        nbrPionPlayer1       = 12;
        nbrPionPlayer2       = 12;
        nbrDamePlayer1       = 0;
        nbrDamePlayer2       = 0;
        remainingTime        = 600;
        initPlateau();
        calculePrisePossible();
    }

    /**
     * Belegt das Spielfeld mit den Startpositionen.
     * Spieler 1 beginnt in den ersten 3 Zeilen, Spieler 2 in den letzten 3 Zeilen.
     * Weisse Kacheln und die mittleren beiden Reihen bleiben leer.
     */
    private void initPlateau() {
        for (int ligne = 0; ligne < TAILLE; ligne++) {
            for (int col = 0; col < TAILLE; col++) {
                if ((ligne + col) % 2 == 0) {
                    plateau[ligne][col] = PieceType.BLANC;
                } else if (ligne < 3) {
                    plateau[ligne][col] = PieceType.PION_J1;
                } else if (ligne > 4) {
                    plateau[ligne][col] = PieceType.PION_J2;
                } else {
                    plateau[ligne][col] = PieceType.VIDE;
                }
            }
        }
    }

    // ZUSTANDSBEHAFTETE METHODEN (menschlicher Spieler, echtes Spiel)

    // Spielfeld

    /** {@inheritDoc} */
    @Override
    public PieceType[][] getPlateau() { return plateau; }

    /**
     * Setzt den Inhalt einer Kachel, falls die Koordinaten gueltig sind.
     *
     * @param col   Spalte der Kachel
     * @param ligne Zeile der Kachel
     * @param piece Figurentyp, der gesetzt werden soll
     */
    private void setPlateau(int col, int ligne, PieceType piece) {
        if (isInsideBoard(col, ligne)) plateau[ligne][col] = piece;
    }

    /**
     * Prueft, ob die angegebenen Koordinaten innerhalb des Spielfelds liegen.
     *
     * @param x Spalte
     * @param y Zeile
     * @return true wenn die Koordinaten gueltig sind
     */
    public boolean isInsideBoard(int x, int y) {
        return x >= 0 && x < TAILLE && y >= 0 && y < TAILLE;
    }

    /** {@inheritDoc} */
    @Override
    public PieceType getActuelPlayer() { return actuelPlayer; }

    /** {@inheritDoc} */
    @Override
    public void changePlayer(boolean moveMade) {
        if (!moveMade) return;
        actuelPlayer = estJ1(actuelPlayer) ? PieceType.PION_J2 : PieceType.PION_J1;
        clearSelected();
        calculePrisePossible();
    }


    @Override public int getSelectedX()              { return selectedX; }
    @Override public int getSelectedY()              { return selectedY; }
    @Override public void setSelected(int x, int y) { selectedX = x; selectedY = y; }
    @Override public void clearSelected()            { selectedX = -1; selectedY = -1; }

    /** {@inheritDoc} */
    @Override
    public boolean peutEtreSelectionne(int x, int y) {
        if (!prisePossible) return true;
        for (int[] pos : pionsPouvantCapturer) {
            if (pos[0] == x && pos[1] == y) return true;
        }
        return false;
    }

    // Berechnung der Schlagpflicht (echtes Spiel (keine KI Simulation) )

    /**
     * Berechnet, welche Figuren des aktuellen Spielers schlagen koennen,
     * und aktualisiert prisePossible sowie pionsPouvantCapturer entsprechend.
     * Wird nach jedem Zugwechsel aufgerufen.
     */

    private void calculePrisePossible() {
        pionsPouvantCapturer = new ArrayList<>();
        for (int ligne = 0; ligne < TAILLE; ligne++) {
            for (int col = 0; col < TAILLE; col++) {
                PieceType piece = plateau[ligne][col];
                if (!estJoueurActuel(piece)) continue;
                boolean isDame = estDame(piece);
                if (isDame ? damePeutCapturer(col, ligne, plateau) : pionPeutCapturer(col, ligne, plateau)) {
                    pionsPouvantCapturer.add(new int[]{col, ligne});
                }
            }
        }
        prisePossible = !pionsPouvantCapturer.isEmpty();
    }

    // Moegliche Zuege - Bauer bzw Spielstein (echtes Spiel)

    @Override
    public int[][] getPossibleMovesPion(int x, int y) {
        List<int[]> moves = new ArrayList<>();
        int direction = estJ1(actuelPlayer) ? 1 : -1;

        if (prisePossible) {
            for (int[] cap : getCapturesPion(plateau, x, y, actuelPlayer)) {
                moves.add(new int[]{cap[2], cap[3]});
            }
        } else {
            addSimpleMove(moves, x, y, x - 1, y + direction);
            addSimpleMove(moves, x, y, x + 1, y + direction);
        }
        return toArray(moves);
    }

    /**
     * Fuegt einen einfachen (nicht schlagenden) Zug zur Liste hinzu,
     * falls die Zielkachel innerhalb des Bretts und leer ist.
     */
    private void addSimpleMove(List<int[]> moves, int fx, int fy, int tx, int ty) {
        if (isInsideBoard(tx, ty) && plateau[ty][tx] == PieceType.VIDE)
            moves.add(new int[]{tx, ty});
    }


    // Moegliche Zuege - Dame (echtes Spiel)

    @Override
    public int[][] getPossibleMovesDame(int x, int y) {
        List<int[]> moves = new ArrayList<>();
        int[][] dirs = {{-1,1},{1,1},{-1,-1},{1,-1}};

        if (prisePossible) {
            for (int[] cap : getCapturesDame(plateau, x, y, actuelPlayer)) {
                moves.add(new int[]{cap[2], cap[3]});
            }
        } else {
            for (int[] dir : dirs) {
                for (int i = 1; i < TAILLE; i++) {
                    int nx = x + i * dir[0], ny = y + i * dir[1];
                    if (!isInsideBoard(nx, ny)) break;
                    if (plateau[ny][nx] != PieceType.VIDE) break;
                    moves.add(new int[]{nx, ny});
                }
            }
        }
        return toArray(moves);
    }

    // Schlagkette

    /**
     * {@inheritDoc}
     *
     * Gibt alle Kacheln zurueck, die ueber eine Schlagkette erreichbar sind.
     * Der Spieler kann sich an jedem Zwischenschritt stoppen.
     * Beispiel: A -> B -> C ist moeglich. Das Ergebnis enthaelt B und C.
     * Klick auf B: eine Figur geschlagen, Zug beendet.
     * Klick auf C: zwei Figuren geschlagen, Zug beendet.
     */
    @Override
    public int[][] getChaineCaptures(int x, int y) {
        Set<String> visited = new HashSet<>();
        List<int[]> destinations = new ArrayList<>();
        PieceType[][] sim = copierPlateau(plateau);
        explorerChaine(x, y, sim, destinations, visited);
        return toArray(destinations);
    }

    /**
     * Rekursive Erkundung der Schlagkette vom Punkt (x, y) aus.
     * Jede erreichbare Position nach einem Schlag wird als Ziel hinzugefuegt.
     * Anschliessend werden weitere Schlaege von dieser Position aus erkundet.
     *
     * @param x            aktuelle Spalte der schlagenden Figur
     * @param y            aktuelle Zeile der schlagenden Figur
     * @param sim          simuliertes Spielfeld fuer diese Rekursionsebene
     * @param destinations bisher gefundene Zielkacheln
     * @param visited      bereits verarbeitete Zug-Schlager-Kombinationen (verhindert Endlosschleifen)
     */
    private void explorerChaine(int x, int y, PieceType[][] sim,
                                List<int[]> destinations, Set<String> visited) {
        PieceType piece  = sim[y][x];
        boolean isDame   = estDame(piece);
        int[] dxs = {-1, 1, -1, 1};
        int[] dys = {-1, -1, 1, 1};

        for (int i = 0; i < 4; i++) {
            if (isDame) {
                boolean adversaireTrouve = false;
                int midX = -1, midY = -1;

                for (int step = 1; step < TAILLE; step++) {
                    int nx = x + step * dxs[i];
                    int ny = y + step * dys[i];
                    if (!isInsideBoard(nx, ny)) break;

                    PieceType cible = sim[ny][nx];

                    if (!adversaireTrouve) {
                        if (cible == PieceType.VIDE) continue; //leeres Feld vor dem Gegner
                        if (estAdversairePour(cible, actuelPlayer)) {
                            adversaireTrouve = true;
                            midX = nx; midY = ny;
                        } else {
                            break;
                        }
                    } else {
                        if (cible != PieceType.VIDE) break;

                        String key = nx + "," + ny + "|" + midX + "," + midY;
                        if (visited.contains(key)) continue;
                        visited.add(key);

                        PieceType[][] nouveauSim = copierPlateau(sim);
                        nouveauSim[ny][nx]     = nouveauSim[y][x];
                        nouveauSim[y][x]       = PieceType.VIDE;
                        nouveauSim[midY][midX] = PieceType.VIDE;

                        destinations.add(new int[]{nx, ny});

                        Set<String> visitedSuite = new HashSet<>(visited);
                        explorerChaine(nx, ny, nouveauSim, destinations, visitedSuite);
                    }
                }
            } else {

                int midX = x + dxs[i], midY = y + dys[i];
                int toX  = x + 2 * dxs[i], toY = y + 2 * dys[i];
                if (!isInsideBoard(midX, midY) || !isInsideBoard(toX, toY)) continue;
                PieceType mid = sim[midY][midX];
                PieceType to  = sim[toY][toX];
                if (!estAdversairePour(mid, actuelPlayer) || to != PieceType.VIDE) continue;
                String key = toX + "," + toY + "|" + midX + "," + midY;
                if (visited.contains(key)) continue;
                visited.add(key);
                PieceType[][] nouveauSim = copierPlateau(sim);
                nouveauSim[toY][toX]   = nouveauSim[y][x];
                nouveauSim[y][x]       = PieceType.VIDE;
                nouveauSim[midY][midX] = PieceType.VIDE;
                destinations.add(new int[]{toX, toY});
                Set<String> visitedSuite = new HashSet<>(visited);
                explorerChaine(toX, toY, nouveauSim, destinations, visitedSuite);
            }
        }
    }

    // Zugausfuehrung (echtes Spiel)

    /**
     * {@inheritDoc}
     *
     * Bei Schlagpflicht muss die Zielposition zu getChaineCaptures gehoeren.
     * Dies stellt sicher, dass mindestens eine Figur geschlagen wird,
     * unabhaengig davon, wie weit die Schlagkette fortgesetzt wird.
     */
    @Override
    public boolean executerDeplacement(int toX, int toY) {
        if (selectedX == -1 || selectedY == -1) return false;

        PieceType piece = plateau[selectedY][selectedX];
        boolean isDame  = estDame(piece);

        int[][] movesPossibles;

        if (prisePossible) {
            movesPossibles = getChaineCaptures(selectedX, selectedY);
        } else {
            movesPossibles = isDame
                    ? getPossibleMovesDame(selectedX, selectedY)
                    : getPossibleMovesPion(selectedX, selectedY);
        }

        boolean estValide = false;
        for (int[] move : movesPossibles) {
            if (move[0] == toX && move[1] == toY) { estValide = true; break; }
        }
        if (!estValide) return false;

        if (isDame) {
            executerDeplacementDame(selectedX, selectedY, toX, toY);
        } else {
            executerDeplacementPion(selectedX, selectedY, toX, toY);
        }
        return true;
    }

    /**
     * Fuehrt den Zug eines Bauern aus.
     * Bei einem Schlagzug wird die geschlagene Figur entfernt.
     * Anschliessend wird die Befoerderung geprueft.
     * Kann die Figur nach dem Schlag weiter schlagen, behaelt der Spieler die Hand.
     * Bei einer Befoerderung endet der Zug sofort (Regelkonform).
     *
     * @param fromX Ausgangsspalte
     * @param fromY Ausgangszeile
     * @param toX   Zielspalte
     * @param toY   Zielzeile
     */
    private void executerDeplacementPion(int fromX, int fromY, int toX, int toY) {
        PieceType piece    = plateau[fromY][fromX];
        boolean estCapture = Math.abs(toX - fromX) == 2;

        if (estCapture) {
            int midX = (fromX + toX) / 2;
            int midY = (fromY + toY) / 2;
            capturerPiece(midX, midY);
        }

        setPlateau(fromX, fromY, PieceType.VIDE);
        setPlateau(toX, toY, piece);

        // Check whether a promotion is taking place BEFORE triggering it
        boolean promotionHasOccured =
                (piece == PieceType.PION_J1 && toY == TAILLE - 1) ||
                        (piece == PieceType.PION_J2 && toY == 0);

        verifierPromotion(toX, toY);

        // If promoted: the turn ends automatically, regardless of any potential captures
        if (promotionHasOccured) {
            prisePossible        = false;
            pionsPouvantCapturer = new ArrayList<>();
            clearSelected();
            return;   // changePlayer() will be called by the controller
        }

        if (estCapture) {
            selectedX = toX;
            selectedY = toY;
            boolean peutEnchaîner = pionPeutCapturer(toX, toY, plateau);

            if (peutEnchaîner) {
                prisePossible        = true;
                pionsPouvantCapturer = new ArrayList<>();
                pionsPouvantCapturer.add(new int[]{toX, toY});
                return; // Player keeps the turn to continue capturing
            }
        }

        prisePossible        = false;
        pionsPouvantCapturer = new ArrayList<>();
        clearSelected();
    }

    /**
     * Fuehrt den Zug einer Dame aus.
     * Die Dame kann sich beliebig weit diagonal bewegen und schlaegt alle
     * gegnerischen Figuren auf ihrem Weg zur Zielkachel.
     * Kann die Dame nach dem Zug weiter schlagen, behaelt der Spieler die Hand.
     *
     * @param fromX Ausgangsspalte
     * @param fromY Ausgangszeile
     * @param toX   Zielspalte
     * @param toY   Zielzeile
     */
    private void executerDeplacementDame(int fromX, int fromY, int toX, int toY) {
        PieceType piece = plateau[fromY][fromX];
        int dx = Integer.signum(toX - fromX);
        int dy = Integer.signum(toY - fromY);
        int x = fromX + dx, y = fromY + dy;

        boolean aCapture = false;
        while (x != toX || y != toY) {
            if (estAdversairePour(plateau[y][x], actuelPlayer)) {
                capturerPiece(x, y);
                aCapture = true;   // mark that a capture has occurred
            }
            x += dx; y += dy;
        }

        setPlateau(fromX, fromY, PieceType.VIDE);
        setPlateau(toX, toY, piece);

        // Additional captures are allowed ONLY if a capture was just made
        if (aCapture && damePeutCapturer(toX, toY, plateau)) {
            selectedX = toX;
            selectedY = toY;
            prisePossible = true;
            pionsPouvantCapturer = new ArrayList<>();
            pionsPouvantCapturer.add(new int[]{toX, toY});
            return;
        }

        prisePossible = false;
        pionsPouvantCapturer = new ArrayList<>();
        clearSelected();
    }

    /**
     * Entfernt die Figur an der angegebenen Position und dekrementiert den entsprechenden Zaehler.
     *
     * @param x Spalte der zu schlagenden Figur
     * @param y Zeile der zu schlagenden Figur
     */
    private void capturerPiece(int x, int y) {
        PieceType piece = plateau[y][x];
        if (piece == PieceType.PION_J1)  nbrPionPlayer1--;
        if (piece == PieceType.PION_J2)  nbrPionPlayer2--;
        if (piece == PieceType.DAME_J1)  nbrDamePlayer1--;
        if (piece == PieceType.DAME_J2)  nbrDamePlayer2--;
        setPlateau(x, y, PieceType.VIDE);
    }

    /**
     * Fuehrt die Befoerderung eines Bauern zur Dame durch, falls die Bedingung erfuellt ist.
     * Aktualisiert ausserdem die Figurenzaehler.
     *
     * @param x Spalte
     * @param y Zeile
     */
    private void verifierPromotion(int x, int y) {
        PieceType piece = plateau[y][x];
        if (piece == PieceType.PION_J1 && y == TAILLE - 1) {
            setPlateau(x, y, PieceType.DAME_J1);
            nbrDamePlayer1++; nbrPionPlayer1--;
        } else if (piece == PieceType.PION_J2 && y == 0) {
            setPlateau(x, y, PieceType.DAME_J2);
            nbrDamePlayer2++; nbrPionPlayer2--;
        }
    }

    // ZUSTANDSLOSE METHODEN (KI-Simulation)

    /** {@inheritDoc} */
    @Override
    public int[][] getCoupsPossibles(PieceType[][] plat, PieceType joueur) {
        List<int[]> captures = new ArrayList<>();
        List<int[]> normaux  = new ArrayList<>();

        for (int ligne = 0; ligne < TAILLE; ligne++) {
            for (int col = 0; col < TAILLE; col++) {
                PieceType piece = plat[ligne][col];
                if (!appartientA(piece, joueur)) continue;

                boolean isDame = estDame(piece);

                List<int[]> caps = isDame
                        ? getCapturesDame(plat, col, ligne, joueur)
                        : getCapturesPion(plat, col, ligne, joueur);
                captures.addAll(caps);

                if (caps.isEmpty()) {
                    List<int[]> mvts = isDame
                            ? getMouvementsNormauxDame(plat, col, ligne)
                            : getMouvementsNormauxPion(plat, col, ligne, joueur);
                    normaux.addAll(mvts);
                }
            }
        }

        return toArray(captures.isEmpty() ? normaux : captures);
    }

    /**
     * {@inheritDoc}
     *
     * Loescht nur gegnerische Figuren auf dem Weg (nicht alle Felder).
     * Fuehrt ausserdem eine Befoerderung durch, wenn die Zielzeile erreicht ist.
     */
    @Override
    public void appliquerCoupSurPlateau(PieceType[][] plat, int[] coup) {
        int fromX = coup[0], fromY = coup[1];
        int toX   = coup[2], toY   = coup[3];
        PieceType piece = plat[fromY][fromX];

        if (Math.abs(toX - fromX) >= 2) {
            int dx = Integer.signum(toX - fromX);
            int dy = Integer.signum(toY - fromY);
            int cx = fromX + dx, cy = fromY + dy;
            while (cx != toX || cy != toY) {
                plat[cy][cx] = PieceType.VIDE;
                cx += dx; cy += dy;
            }
        }

        plat[fromY][fromX] = PieceType.VIDE;
        plat[toY][toX]     = piece;

        if (piece == PieceType.PION_J1 && toY == TAILLE - 1) plat[toY][toX] = PieceType.DAME_J1;
        if (piece == PieceType.PION_J2 && toY == 0)           plat[toY][toX] = PieceType.DAME_J2;
    }

    /** {@inheritDoc} */
    @Override
    public PieceType[][] copierPlateau(PieceType[][] src) {
        PieceType[][] copie = new PieceType[TAILLE][TAILLE];
        for (int i = 0; i < TAILLE; i++) copie[i] = src[i].clone();
        return copie;
    }

    /** {@inheritDoc} */
    @Override
    public boolean estPartieTermineeSurPlateau(PieceType[][] plat) {
        boolean j1 = false, j2 = false;
        for (PieceType[] row : plat)
            for (PieceType p : row) {
                if (p == PieceType.PION_J1 || p == PieceType.DAME_J1) j1 = true;
                if (p == PieceType.PION_J2 || p == PieceType.DAME_J2) j2 = true;
            }
        return !j1 || !j2;
    }

    // PRIVATE GEMEINSAME METHODEN (reine Regellogik)

    /**
     * Gibt alle Schlagmoeglichkeiten eines Bauern an Position (x, y) zurueck.
     * Ein Bauer kann in alle vier Diagonalrichtungen schlagen.
     *
     * @param plat   Spielfeld, auf dem gesucht wird
     * @param x      Spalte des Bauern
     * @param y      Zeile des Bauern
     * @param joueur der schlagende Spieler
     * @return Liste von Zuegen {fromX, fromY, toX, toY}
     */
    private List<int[]> getCapturesPion(PieceType[][] plat, int x, int y, PieceType joueur) {
        List<int[]> coups = new ArrayList<>();
        int[] dxs = {-1, 1, -1, 1};
        int[] dys = {-1, -1, 1, 1};
        for (int i = 0; i < 4; i++) {
            int midX = x + dxs[i], midY = y + dys[i];
            int toX  = x + 2 * dxs[i], toY = y + 2 * dys[i];
            if (!isInsideBoard(midX, midY) || !isInsideBoard(toX, toY)) continue;
            if (estAdversairePour(plat[midY][midX], joueur) && plat[toY][toX] == PieceType.VIDE)
                coups.add(new int[]{x, y, toX, toY});
        }
        return coups;
    }

    /**
     * Gibt alle Schlagmoeglichkeiten einer Dame an Position (x, y) zurueck.
     * Eine Dame kann beliebig weit diagonal springen und nach dem Schlag
     * auf jeder freien Kachel hinter der geschlagenen Figur landen.
     *
     * @param plat   Spielfeld, auf dem gesucht wird
     * @param x      Spalte der Dame
     * @param y      Zeile der Dame
     * @param joueur der schlagende Spieler
     * @return Liste von Zuegen {fromX, fromY, toX, toY}
     */
    private List<int[]> getCapturesDame(PieceType[][] plat, int x, int y, PieceType joueur) {
        List<int[]> coups = new ArrayList<>();
        int[][] dirs = {{-1,1},{1,1},{-1,-1},{1,-1}};
        for (int[] d : dirs) {
            boolean adversaireTrouve = false;
            for (int i = 1; i < TAILLE; i++) {
                int nx = x + i * d[0], ny = y + i * d[1];
                if (!isInsideBoard(nx, ny)) break;
                PieceType p = plat[ny][nx];
                if (p == PieceType.VIDE) {
                    if (adversaireTrouve) coups.add(new int[]{x, y, nx, ny});
                } else if (estAdversairePour(p, joueur)) {
                    if (adversaireTrouve) break;
                    adversaireTrouve = true;
                } else { break; }
            }
        }
        return coups;
    }

    /**
     * Prueft, ob ein Bauer an der angegebenen Position schlagen kann.
     *
     * @param x    Spalte
     * @param y    Zeile
     * @param plat Spielfeld
     * @return true wenn der Bauer schlagen kann
     */
    private boolean pionPeutCapturer(int x, int y, PieceType[][] plat) {
        return !getCapturesPion(plat, x, y, actuelPlayer).isEmpty();
    }

    /**
     * Prueft, ob eine Dame an der angegebenen Position schlagen kann.
     *
     * @param x    Spalte
     * @param y    Zeile
     * @param plat Spielfeld
     * @return true wenn die Dame schlagen kann
     */
    private boolean damePeutCapturer(int x, int y, PieceType[][] plat) {
        return !getCapturesDame(plat, x, y, actuelPlayer).isEmpty();
    }

    /**
     * Gibt alle normalen (nicht schlagenden) Zuege eines Bauern zurueck.
     *
     * @param plat   Spielfeld
     * @param x      Spalte des Bauern
     * @param y      Zeile des Bauern
     * @param joueur der ziehende Spieler
     * @return Liste von Zuegen {fromX, fromY, toX, toY}
     */
    private List<int[]> getMouvementsNormauxPion(PieceType[][] plat, int x, int y, PieceType joueur) {
        List<int[]> coups = new ArrayList<>();
        int dir = estJ1(joueur) ? 1 : -1;
        for (int dx : new int[]{-1, 1}) {
            int nx = x + dx, ny = y + dir;
            if (isInsideBoard(nx, ny) && plat[ny][nx] == PieceType.VIDE)
                coups.add(new int[]{x, y, nx, ny});
        }
        return coups;
    }

    /**
     * Gibt alle normalen (nicht schlagenden) Zuege einer Dame zurueck.
     * Die Dame kann sich beliebig weit diagonal bewegen, solange keine Figur den Weg versperrt.
     *
     * @param plat Spielfeld
     * @param x    Spalte der Dame
     * @param y    Zeile der Dame
     * @return Liste von Zuegen {fromX, fromY, toX, toY}
     */
    private List<int[]> getMouvementsNormauxDame(PieceType[][] plat, int x, int y) {
        List<int[]> coups = new ArrayList<>();
        int[][] dirs = {{-1,1},{1,1},{-1,-1},{1,-1}};
        for (int[] d : dirs) {
            for (int i = 1; i < TAILLE; i++) {
                int nx = x + i * d[0], ny = y + i * d[1];
                if (!isInsideBoard(nx, ny) || plat[ny][nx] != PieceType.VIDE) break;
                coups.add(new int[]{x, y, nx, ny});
            }
        }
        return coups;
    }

    /**
     * Prueft, ob eine Figur zu Spieler 1 gehoert (Bauer oder Dame).
     *
     * @param p der Figurentyp
     * @return true wenn die Figur Spieler 1 gehoert
     */
    private boolean estJ1(PieceType p) {
        return p == PieceType.PION_J1 || p == PieceType.DAME_J1;
    }

    /**
     * Prueft, ob eine Figur eine Dame ist.
     *
     * @param p der Figurentyp
     * @return true wenn es sich um eine Dame handelt
     */
    private boolean estDame(PieceType p) {
        return p == PieceType.DAME_J1 || p == PieceType.DAME_J2;
    }

    /**
     * Prueft, ob eine Figur dem aktuell am Zug befindlichen Spieler gehoert.
     *
     * @param p der Figurentyp
     * @return true wenn die Figur dem aktuellen Spieler gehoert
     */
    private boolean estJoueurActuel(PieceType p) {
        return appartientA(p, actuelPlayer);
    }

    /**
     * Prueft, ob eine Figur zum angegebenen Spieler gehoert.
     *
     * @param piece  der Figurentyp
     * @param joueur der Spieler
     * @return true wenn die Figur zum Spieler gehoert
     */
    private boolean appartientA(PieceType piece, PieceType joueur) {
        return estJ1(joueur) ? estJ1(piece) : (!estJ1(piece) && piece != PieceType.VIDE && piece != PieceType.BLANC);
    }

    /**
     * Prueft, ob eine Figur eine gegnerische Figur des angegebenen Spielers ist.
     * Einzige Wahrheitsquelle fuer die Gegnerpruefung in der gesamten Klasse.
     *
     * @param piece  der Figurentyp
     * @param joueur der Spieler, dessen Sicht eingenommen wird
     * @return true wenn die Figur ein Gegner des Spielers ist
     */
    private boolean estAdversairePour(PieceType piece, PieceType joueur) {
        if (piece == PieceType.VIDE || piece == PieceType.BLANC) return false;
        return estJ1(joueur) ? !estJ1(piece) : estJ1(piece);
    }

    /**
     * Konvertiert eine Liste von int[]-Arrays in ein zweidimensionales Array.
     *
     * @param list die zu konvertierende Liste
     * @return zweidimensionales Array
     */
    private int[][] toArray(List<int[]> list) {
        return list.toArray(new int[0][]);
    }

    // SPIELENDE, ZUSTAND UND GETTER

    /** {@inheritDoc} */
    @Override
    public boolean isPrisePossible() { return prisePossible; }

    /** {@inheritDoc} */
    @Override
    public boolean isMoveCapture(int x, int y) {
        if (selectedX == -1 || selectedY == -1) return false;
        return Math.abs(x - selectedX) >= 2;
    }

    /**
     * {@inheritDoc}
     *
     * Prueft drei Abbruchbedingungen:
     * 1. Spieler 1 hat keine Figuren mehr.
     * 2. Spieler 2 hat keine Figuren mehr.
     * 3. Der aktuelle Spieler hat keinen gueltigen Zug mehr.
     */
    @Override
    public void checkGameOver() {

        if (nbrPionPlayer1 + nbrDamePlayer1 == 0) {
            previousWinner = PieceType.PION_J2;
            setState(Gamestate.GAME_OVER);
        }

        else if (nbrPionPlayer2 + nbrDamePlayer2 == 0) {
            previousWinner = PieceType.PION_J1;
            setState(Gamestate.GAME_OVER);
        }

        else if (isNoMovePossible()) {

            previousWinner =
                    estJ1(actuelPlayer)
                            ? PieceType.PION_J2
                            : PieceType.PION_J1;

            setState(Gamestate.GAME_OVER);
        }
    }

    /**
     * Prueft, ob der aktuelle Spieler keinen einzigen gueltigen Zug mehr hat.
     *
     * @return true wenn der aktuelle Spieler blockiert ist
     */
    private boolean isNoMovePossible() {
        for (int ligne = 0; ligne < TAILLE; ligne++)
            for (int col = 0; col < TAILLE; col++) {
                if (!estJoueurActuel(plateau[ligne][col])) continue;
                boolean isDame = estDame(plateau[ligne][col]);
                int[][] moves = isDame
                        ? getPossibleMovesDame(col, ligne)
                        : getPossibleMovesPion(col, ligne);
                if (moves.length > 0) return false;
            }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public PieceType getWinner() {

        if (state != Gamestate.GAME_OVER)
            return null;

        if (!victoiresIncrementees && previousWinner != null) {

            victoiresIncrementees = true;

            if (previousWinner == PieceType.PION_J1) {
                victoirePlayer1++;
            } else {
                victoirePlayer2++;
            }

            manche++;
        }

        return previousWinner;
    }

    /** Aktueller Spielzustand. */
    private Gamestate state;

    @Override public Gamestate getState()           { return state; }
    @Override public void setState(Gamestate state) { this.state = state; }

    @Override public int getNbrPionPlayer1()  { return nbrPionPlayer1; }
    @Override public int getNbrPionPlayer2()  { return nbrPionPlayer2; }
    @Override public int getNbrDamePlayer1()  { return nbrDamePlayer1; }
    @Override public int getNbrDamePlayer2()  { return nbrDamePlayer2; }
    @Override public int getVictoirePlayer1() { return victoirePlayer1; }
    @Override public int getVictoirePlayer2() { return victoirePlayer2; }
    @Override public int getManche()          { return manche; }
    @Override public int getRemainingTime()   { return remainingTime; }
    @Override public void setRemainingTime(int time) { this.remainingTime = time; }

    /**
     * Gibt eine textuelle Darstellung des Spielfelds zurueck.
     * Nuetzlich fuer Debugging.
     *
     * @return Spielfeld als mehrzeiliger String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int ligne = 0; ligne < TAILLE; ligne++) {
            for (int col = 0; col < TAILLE; col++) {
                switch (plateau[ligne][col]) {
                    case PION_J1 -> sb.append("X ");
                    case PION_J2 -> sb.append("O ");
                    case DAME_J1 -> sb.append("D ");
                    case DAME_J2 -> sb.append("d ");
                    case VIDE    -> sb.append("_ ");
                    case BLANC   -> sb.append(". ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}