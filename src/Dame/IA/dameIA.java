package Dame.IA;

import Dame.Model.IdameModel;
import Dame.Model.PieceType;

/**
 * Kuenstliche Intelligenz für das Damespiel, basierend auf dem Minimax-Algorithmus
 * mit Alpha-Beta-Pruning.
 *
 * Die KI spielt stets als Spieler 2 (weisse Figuren).
 * Sie simuliert mögliche Züge auf Kopien des Spielfelds, ohne den echten
 * Spielzustand zu verändern. Die Suchtiefe ist auf {@value PROFONDEUR} Halbzüge
 * festgelegt, was einem guten Gleichgewicht zwischen Staerke und Rechenzeit entspricht.
 *
 * Bewertungsfunktion:
 * Bauer Spieler 2  : +10 Punkte
 * Dame Spieler 2   : +30 Punkte
 * Bauer Spieler 1  : -10 Punkte
 * Dame Spieler 1   : -30 Punkte
 *
 * @author Dimzz
 * @version 2.0
 * @see Dame.Model.IdameModel#getCoupsPossibles
 * @see Dame.Model.IdameModel#appliquerCoupSurPlateau
 */
public class dameIA {

    /**
     * Suchtiefe des Minimax-Algorithmus in Halbzügen.
     * Eine höhere Tiefe ergibt eine staerkere KI, erfordert aber mehr Rechenzeit.
     */
    private static final int PROFONDEUR = 4;

    /** Referenz auf das Model für den Zugriff auf Spielfeldkopien und Zuglogik. */
    private final IdameModel model;


    /**
     * Erstellt eine neue KI-Instanz.
     *
     * @param model das Spielmodell, das Spielfeldkopien und Zuglogik bereitstellt
     */
    public dameIA(IdameModel model) {
        this.model = model;
    }


    /**
     * Wählt den besten Zug für die KI (Spieler 2) auf dem aktuellen Spielfeld.
     *
     * Ablauf:
     * 1. Alle legalen Züge für Spieler 2 werden ermittelt.
     * 2. Für jeden Zug wird ein simuliertes Spielfeld erzeugt.
     * 3. Der Minimax-Algorithmus bewertet jeden Zug.
     * 4. Der Zug mit dem höchsten Score wird zurückgegeben.
     *
     * @return bester Zug als Array {fromX, fromY, toX, toY},
     *         oder null wenn kein Zug möglich ist
     */
    public int[] choisirMeilleurCoup() {
        PieceType[][] plateau = model.copierPlateau(model.getPlateau());

        int[][] coupsArray = model.getCoupsPossibles(plateau, PieceType.PION_J2);
        if (coupsArray.length == 0) return null;

        int meilleurScore = Integer.MIN_VALUE;
        int[] meilleurCoup = null;

        for (int[] coup : coupsArray) {
            PieceType[][] sim = model.copierPlateau(plateau);
            model.appliquerCoupSurPlateau(sim, coup);

            int score = minimax(sim, PROFONDEUR - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurCoup  = coup;
            }
        }

        return meilleurCoup;
    }

    /**
     * Rekursiver Minimax-Algorithmus mit Alpha-Beta-Pruning.
     *
     * Maximisierender Spieler: KI (Spieler 2), versucht den Score zu maximieren.
     * Minimisierender Spieler: Mensch (Spieler 1), versucht den Score zu minimieren.
     * Alpha-Beta-Pruning: Schneidet branch ab, die den Score nicht mehr verbessern können,
     * um die Rechenzeit deutlich zu reduzieren.
     *
     * @param plateau    das simulierte Spielfeld für diesen Knoten
     * @param profondeur verbleibende Suchtiefe; bei 0 wird die Bewertungsfunktion aufgerufen
     * @param alpha      bisher bester Wert für den Maximierer (wird nach oben aktualisiert)
     * @param beta       bisher bester Wert für den Minimierer (wird nach unten aktualisiert)
     * @param max        true wenn der maximisierende Spieler (KI) am Zug ist
     * @return bewerteter Score für diesen Spielzustand
     */
    private int minimax(PieceType[][] plateau, int profondeur,
                        int alpha, int beta, boolean max) {

        if (profondeur == 0 || model.estPartieTermineeSurPlateau(plateau)) {
            return evaluer(plateau);
        }

        PieceType joueur = max ? PieceType.PION_J2 : PieceType.PION_J1;
        int[][] coups = model.getCoupsPossibles(plateau, joueur);

        if (coups.length == 0) return evaluer(plateau);

        if (max) {
            int best = Integer.MIN_VALUE;
            for (int[] coup : coups) {
                PieceType[][] sim = model.copierPlateau(plateau);
                model.appliquerCoupSurPlateau(sim, coup);

                int score = minimax(sim, profondeur - 1, alpha, beta, false);

                best  = Math.max(best, score);
                alpha = Math.max(alpha, score);

                // Beta-Pruning: dieser Ast kann den Minimierer nicht mehr verschlechtern
                if (beta <= alpha) break;
            }
            return best;

        } else {
            int best = Integer.MAX_VALUE;
            for (int[] coup : coups) {
                PieceType[][] sim = model.copierPlateau(plateau);
                model.appliquerCoupSurPlateau(sim, coup);

                int score = minimax(sim, profondeur - 1, alpha, beta, true);

                best = Math.min(best, score);
                beta = Math.min(beta, score);

                // Alpha-Pruning: dieser Ast kann den Maximierer nicht mehr verbessern
                if (beta <= alpha) break;
            }
            return best;
        }
    }


    /**
     * Bewertet einen Spielfeldzustand aus der Sicht der KI (Spieler 2).
     *
     * Positive Werte bevorteilen die KI, negative Werte bevorteilen den Menschen.
     * Damen werden dreimal so hoch bewertet wie Bauern, da sie beweglicher sind
     * und eine größere strategische Bedeutung haben.
     *
     * @param plateau das zu bewertende Spielfeld
     * @return numerischer Score des Spielfeldzustands
     */
    private int evaluer(PieceType[][] plateau) {
        int score = 0;

        for (PieceType[] row : plateau) {
            for (PieceType p : row) {
                switch (p) {
                    case PION_J2 -> score += 10;
                    case DAME_J2 -> score += 30;
                    case PION_J1 -> score -= 10;
                    case DAME_J1 -> score -= 30;
                }
            }
        }

        return score;
    }
}