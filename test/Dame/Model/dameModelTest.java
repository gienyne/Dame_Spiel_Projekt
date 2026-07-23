package Dame.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testet die Zuverlaessigkeit der Methoden von {@link dameModel}.
 * @author Dimzz
 */
public class dameModelTest {

    private dameModel model;

    /**
     * Erstellt vor jedem Test ein neues Model.
     */
    @BeforeEach
    void setUp() {
        model = new dameModel();
    }

    // INITIALISIERUNG

    /**
     * Prueft, ob das Spielfeld korrekt initialisiert wurde.
     */
    @Test
    void shouldInitializeBoardCorrectly() {
        PieceType[][] plateau = model.getPlateau();

        for (int ligne = 0; ligne < dameModel.TAILLE; ligne++) {
            for (int col = 0; col < dameModel.TAILLE; col++) {
                if ((ligne + col) % 2 == 0) {
                    assertEquals(PieceType.BLANC, plateau[ligne][col]);
                } else if (ligne < 3) {
                    assertEquals(PieceType.PION_J1, plateau[ligne][col]);
                } else if (ligne > 4) {
                    assertEquals(PieceType.PION_J2, plateau[ligne][col]);
                } else {
                    assertEquals(PieceType.VIDE, plateau[ligne][col]);
                }
            }
        }
    }

    /**
     * Prueft, ob Startwerte fuer Figurenzaehler, Timer
     * und den aktuellen Spieler korrekt gesetzt wurden.
     */
    @Test
    void shouldSetDefaultValuesOnNewGame() {
        assertEquals(12, model.getNbrPionPlayer1());
        assertEquals(12, model.getNbrPionPlayer2());
        assertEquals(0, model.getNbrDamePlayer1());
        assertEquals(0, model.getNbrDamePlayer2());
        assertEquals(600, model.getRemainingTime());
        assertEquals(PieceType.PION_J1, model.getActuelPlayer());
        assertFalse(model.isPrisePossible());
    }

    // AKTUELLER SPIELER

    /**
     * Prueft den Spielerwechsel. changePlayer(false) darf nichts aendern,
     * changePlayer(true) muss zum jeweils anderen Spieler wechseln.
     */
    @Test
    void shouldSwitchCurrentPlayer() {
        assertEquals(PieceType.PION_J1, model.getActuelPlayer());

        model.changePlayer(false);
        assertEquals(PieceType.PION_J1, model.getActuelPlayer());

        model.changePlayer(true);
        assertEquals(PieceType.PION_J2, model.getActuelPlayer());

        model.changePlayer(true);
        assertEquals(PieceType.PION_J1, model.getActuelPlayer());
    }

    // AUSWAHL EINER FIGUR

    /**
     * Prueft das Setzen, Lesen und Zuruecksetzen der ausgewaehlten Figur.
     */
    @Test
    void shouldHandlePieceSelection() {
        assertEquals(-1, model.getSelectedX());
        assertEquals(-1, model.getSelectedY());

        model.setSelected(2, 3);
        assertEquals(2, model.getSelectedX());
        assertEquals(3, model.getSelectedY());

        model.clearSelected();
        assertEquals(-1, model.getSelectedX());
        assertEquals(-1, model.getSelectedY());
    }

    // MOEGLICHE ZUEGE - BAUER

    /**
     * Prueft die moeglichen Zuege eines Bauern von Spieler 1 ohne Schlagpflicht.
     * Ein Bauer auf (1, 2) muss nach (0, 3) und (2, 3) ziehen koennen, da beide
     * Kacheln in Zeile 3 nach der Standardaufstellung leer sind.
     */
    @Test
    void shouldReturnPossiblePawnMovesWithoutCapture() {
        int[][] moves = model.getPossibleMovesPion(1, 2);

        assertEquals(2, moves.length);
        assertArrayEquals(new int[]{0, 3}, moves[0]);
        assertArrayEquals(new int[]{2, 3}, moves[1]);
    }

    // ZUGAUSFUEHRUNG - EINFACHER ZUG

    /**
     * Prueft die Ausfuehrung eines einfachen (nicht schlagenden) Bauernzugs.
     * Nach dem Zug muss die Ausgangskachel leer und die Zielkachel belegt sein.
     */
    @Test
    void shouldExecuteSimplePawnMove() {
        model.setSelected(1, 2);
        boolean moveMade = model.executerDeplacement(2, 3);

        assertTrue(moveMade);
        assertEquals(PieceType.VIDE, model.getPlateau()[2][1]);
        assertEquals(PieceType.PION_J1, model.getPlateau()[3][2]);
    }

    /**
     * Prueft, dass ein ungueltiger Zug (Zielkachel ausserhalb der moeglichen Zuege)
     * abgelehnt wird und das Spielfeld unveraendert bleibt.
     */
    @Test
    void shouldRejectInvalidMove() {
        model.setSelected(1, 2);
        boolean moveMade = model.executerDeplacement(5, 5);

        assertFalse(moveMade);
        assertEquals(PieceType.PION_J1, model.getPlateau()[2][1]);
    }

    // SCHLAGKETTEN

    /**
     * Prueft, ob getChaineCaptures() einen moeglichen Schlagzug korrekt erkennt.
     */
    @Test
    void shouldFindSimpleCaptureChain() {

        model.getPlateau()[3][2] = PieceType.PION_J2;

        int[][] destinations = model.getChaineCaptures(1, 2);

        assertEquals(1, destinations.length);
        assertArrayEquals(new int[]{3, 4}, destinations[0]);
    }

    /**
     * Prueft, ob ein tatsaechlicher Schlagzug ueber executerDeplacement()
     * korrekt ausgefuehrt wird, sobald die Schlagpflicht (ueber einen echten
     * Zugwechsel neu berechnet) aktiv ist. Die geschlagene Figur muss vom
     * Feld entfernt und der Bauernzaehler von Spieler 2 dekrementiert werden.
     */
    @Test
    void shouldExecuteMoveWithCapture() {
        model.getPlateau()[3][2] = PieceType.PION_J2;

        model.changePlayer(true);
        model.changePlayer(true);

        assertTrue(model.isPrisePossible());

        model.setSelected(1, 2);
        boolean moveMade = model.executerDeplacement(3, 4);

        assertTrue(moveMade);
        assertEquals(PieceType.VIDE, model.getPlateau()[2][1]);
        assertEquals(PieceType.VIDE, model.getPlateau()[3][2]);
        assertEquals(PieceType.PION_J1, model.getPlateau()[4][3]);
        assertEquals(11, model.getNbrPionPlayer2());
    }

    // BEFOERDERUNG ZUR DAME

    /**
     * Prueft, ob ein Bauer von Spieler 1 beim Erreichen der letzten Reihe
     * korrekt zur Dame befoerdert wird und die Figurenzaehler aktualisiert werden.
     */
    @Test
    void shouldPromotePawnToQueen() {

        model.getPlateau()[6][1] = PieceType.PION_J1;
        model.getPlateau()[7][0] = PieceType.VIDE;

        model.setSelected(1, 6);
        boolean moveMade = model.executerDeplacement(0, 7);

        assertTrue(moveMade);
        assertEquals(PieceType.DAME_J1, model.getPlateau()[7][0]);
        assertEquals(-1, model.getSelectedX());
    }

    // MOEGLICHE ZUEGE - DAME

    /**
     * Prueft die moeglichen Zuege einer Dame auf einem leeren Feld.
     * Die Dame muss in alle vier Diagonalrichtungen bis zum Spielfeldrand ziehen koennen.
     */
    @Test
    void shouldReturnPossibleQueenMovesOnEmptyBoard() {
        // Spielfeld leeren und eine Dame in die Mitte setzen
        for (PieceType[] row : model.getPlateau()) {
            java.util.Arrays.fill(row, PieceType.VIDE);
        }
        model.getPlateau()[3][3] = PieceType.DAME_J1;

        int[][] moves = model.getPossibleMovesDame(3, 3);

        // 3 Felder in jede Richtung erreichbar (bis zum Rand)
        assertTrue(moves.length > 0);

        assertTrue(containsMove(moves, 4, 4));
        assertTrue(containsMove(moves, 0, 0));
    }

    // ZUSTANDSLOSE METHODEN (KI-SIMULATION)

    /**
     * Prueft, dass getCoupsPossibles() bei vorhandener Schlagmoeglichkeit
     * ausschliesslich Schlagzuege zurueckgibt, auch wenn normale Zuege
     * ebenfalls moeglich waeren (Schlagpflicht hat Vorrang).
     */
    @Test
    void shouldPreferCapturesOverNormalMoves() {
        PieceType[][] plateau = model.copierPlateau(model.getPlateau());

        plateau[2][1] = PieceType.PION_J2;
        plateau[3][2] = PieceType.PION_J1;
        plateau[4][3] = PieceType.VIDE;

        int[][] coups = model.getCoupsPossibles(plateau, PieceType.PION_J2);

        assertTrue(coups.length > 0);
        for (int[] coup : coups) {
            assertTrue(Math.abs(coup[2] - coup[0]) == 2, "Es duerfen nur Schlagzuege zurueckgegeben werden");
        }
    }

    /**
     * Prueft, dass appliquerCoupSurPlateau() einen Schlagzug korrekt auf einer
     * Spielfeldkopie ausfuehrt, ohne das Original zu veraendern.
     */
    @Test
    void shouldApplyMoveOnBoardCopyWithoutAffectingOriginal() {
        PieceType[][] original = model.getPlateau();
        PieceType[][] copie = model.copierPlateau(original);

        copie[3][2] = PieceType.PION_J2;
        int[] coup = {1, 2, 3, 4};
        model.appliquerCoupSurPlateau(copie, coup);

        assertEquals(PieceType.PION_J1, copie[4][3]);
        assertEquals(PieceType.VIDE, copie[3][2]);
        assertEquals(PieceType.VIDE, copie[2][1]);

        // Original darf unveraendert bleiben
        assertEquals(PieceType.PION_J1, original[2][1]);
        assertEquals(PieceType.VIDE, original[3][2]);
    }

    /**
     * Prueft, ob estPartieTermineeSurPlateau() erkennt, wenn einer der beiden
     * Spieler keine Figuren mehr auf einer simulierten Spielfeldkopie besitzt.
     */
    @Test
    void shouldDetectGameOverOnBoard() {
        PieceType[][] plateau = model.copierPlateau(model.getPlateau());

        assertFalse(model.estPartieTermineeSurPlateau(plateau));

        for (int ligne = 0; ligne < dameModel.TAILLE; ligne++) {
            for (int col = 0; col < dameModel.TAILLE; col++) {
                if (plateau[ligne][col] == PieceType.PION_J2) {
                    plateau[ligne][col] = PieceType.VIDE;
                }
            }
        }

        assertTrue(model.estPartieTermineeSurPlateau(plateau));
    }

    // SPIELENDE UND ZUSTAND

    /**
     * Prueft, ob checkGameOver() den Spielzustand auf GAME_OVER setzt,
     * sobald der aktuelle Spieler blockiert ist (kein gueltiger Zug mehr moeglich),
     * und dass der Gegner korrekt als Gewinner ermittelt wird.
     */
    @Test
    void shouldEndGameWhenPlayerIsBlocked() {

        for (PieceType[] row : model.getPlateau()) {
            java.util.Arrays.fill(row, PieceType.VIDE);
        }
        model.getPlateau()[0][1] = PieceType.PION_J1;
        model.getPlateau()[1][0] = PieceType.PION_J2;
        model.getPlateau()[1][2] = PieceType.PION_J2;

        model.setState(Gamestate.PLAYING);
        model.checkGameOver();

        assertEquals(Gamestate.GAME_OVER, model.getState());
        assertEquals(PieceType.PION_J2, model.getWinner());
    }

    /**
     * Prueft, ob getWinner() die Siegzaehler und die Rundennummer nur einmal
     * pro Spielende erhoeht, selbst wenn getWinner() mehrfach aufgerufen wird.
     */
    @Test
    void shouldIncrementVictoriesOnlyOnce() {
        for (PieceType[] row : model.getPlateau()) {
            java.util.Arrays.fill(row, PieceType.VIDE);
        }
        model.getPlateau()[0][1] = PieceType.PION_J1;
        model.getPlateau()[1][0] = PieceType.PION_J2;
        model.getPlateau()[1][2] = PieceType.PION_J2;

        model.setState(Gamestate.PLAYING);
        model.checkGameOver();

        int mancheAvant = model.getManche();
        model.getWinner();
        model.getWinner();

        assertEquals(1, model.getVictoirePlayer2());
        assertEquals(mancheAvant + 1, model.getManche());
    }

    // HILFSMETHODEN UND GRENZWERTE

    /**
     * Prueft die Grenzwertpruefung isInsideBoard() fuer gueltige und ungueltige Koordinaten.
     */
    @Test
    void shouldValidateBoardBoundaries() {
        assertTrue(model.isInsideBoard(0, 0));
        assertTrue(model.isInsideBoard(7, 7));
        assertFalse(model.isInsideBoard(-1, 0));
        assertFalse(model.isInsideBoard(0, -1));
        assertFalse(model.isInsideBoard(8, 0));
        assertFalse(model.isInsideBoard(0, 8));
    }

    /**
     * Prueft, ob isMoveCapture() einen Sprung von zwei Feldern korrekt als
     * Schlagzug erkennt und einen einfachen Zug korrekt ausschliesst.
     */
    @Test
    void shouldDetectCaptureMove() {
        model.setSelected(1, 2);

        assertTrue(model.isMoveCapture(3, 4));
        assertFalse(model.isMoveCapture(2, 3));
    }

    /**
     * prueft, ob ein bestimmtes Ziel in einem Array moeglicher Zuege enthalten ist.
     */
    private boolean containsMove(int[][] moves, int x, int y) {
        for (int[] move : moves) {
            if (move[0] == x && move[1] == y) return true;
        }
        return false;
    }
}