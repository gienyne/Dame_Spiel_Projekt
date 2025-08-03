package Dame.Model;

import org.junit.jupiter.api.Test;

import  static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * testet die Zuverlässigkeit der Methoden des Modells
 */

public class dameModelTest {

    dameModel model ;

    public dameModelTest(){
        model = new dameModel();
    }


    @Test
    void SchouldBeInitialise (){

        model.InitPlateaujeu();
        for(int i = 0; i < model.getPlateauJeu().length; i++){
            for(int j = 0; j < model.getPlateauJeu().length; j++){

                if((i+j) % 2 == 0){
                    assertEquals(model.LEER, model.getPlateauJeu()[i][j]);
                }
                else{
                    if(i < 3){
                        assertEquals(model.PLAYER1, model.getPlateauJeu()[i][j]);
                    }
                    else if(i > 4){
                        assertEquals(model.PLAYER2, model.getPlateauJeu()[i][j]);
                    }
                    else {
                        assertEquals(model.EMPTY, model.getPlateauJeu()[i][j]);
                    }
                }
            }
        }
    }

    @Test
    public void testGetters() {
        // Initialisation des valeurs dans votre modèle (ou utiliser setUp pour le faire)

        // Test des méthodes getters
        assertEquals(12, model.getNbrPionPlayer1());
        assertEquals(12, model.getNbrPionPlayer2());
        assertEquals(0, model.getNbrDamePlayer1());
        assertEquals(0, model.getNbrDamePlayer2());
        assertEquals(0, model.getVictoirePlayer1());
        assertEquals(0, model.getVictoirePlayer2());
        assertEquals(1, model.getManche());
        assertEquals(10, model.getRemainingTime());
    }

    @Test
    public void testSetters() {
        // Utilisez les setters pour modifier les valeurs
        model.setNbrPionPlayer1(10);
        model.setNbrPionPlayer2(8);
        model.setNbrDamePlayer1(2);
        model.setNbrDamePlayer2(4);
        model.setVictoirePlayer1(1);
        model.setVictoirePlayer2(2);
        model.setManche(3);
        model.setRemainingTime(5);
        model.setDamePrisePossible(true);

        // Test des méthodes getters après modification
        assertEquals(10, model.getNbrPionPlayer1());
        assertEquals(8, model.getNbrPionPlayer2());
        assertEquals(2, model.getNbrDamePlayer1());
        assertEquals(4, model.getNbrDamePlayer2());
        assertEquals(1, model.getVictoirePlayer1());
        assertEquals(2, model.getVictoirePlayer2());
        assertEquals(3, model.getManche());
        assertEquals(5, model.getRemainingTime());
        assertEquals(true, model.getDamePrisePossible());
    }


    @Test
    public void testSetSelectedPionX() {
        model.setSelectedPionX(2);
        assertEquals(2, model.getSelectedPionX());
    }

    @Test
    public void testSetSelectedPionY() {
        model.setSelectedPionY(3);
        assertEquals(3, model.getSelectedPionY());
    }

    @Test
    public void testSetMoveMade() {
        model.setmoveMade(true);
        assertTrue(model.getmoveMade());

    }

    @Test
    void testIsDame(){
        model.setIsDame(3,4, true);
        assertEquals(true, model.getIsDame()[4][3]);
    }

    @Test
    public void testPossibleMovePiont() {
        // Initialise le plateau avec une configuration spécifique
        model.InitPlateaujeu();

        // Teste avec un pion joueur 1 en position (2, 3)
        model.setPlateauJeu(2, 3, model.PLAYER1);
        int[][] movesPlayer1 = model.PossibleMovePion(2, 3, model.PLAYER1);

        // Vérifie que les déplacements retournés sont corrects
        assertArrayEquals(new int[]{1, 4}, movesPlayer1[0]);
        assertArrayEquals(new int[]{3, 4}, movesPlayer1[1]);

        // Teste avec un pion joueur 2 en position (4, 4)
        model.setPlateauJeu(3, 4, model.PLAYER2);
        int[][] movesPlayer2 = model.PossibleMovePion(3, 4, model.PLAYER2);

        // Vérifier que les déplacements retournés sont corrects
        assertArrayEquals(new int[]{4, 3}, movesPlayer2[0]);
       // model.setPlateauJeu(2, 3, model.EMPTY);
       //bizzare  assertArrayEquals(new int[]{-2, 3}, movesPlayer2[1]);

    }

    @Test
    public void testGetPossibleMovePionAfterCaptures() {
        // Initialiser le plateau avec une configuration spécifique
        model.InitPlateaujeu();

        // Placer un pion joueur 1 en position (2, 3) et un pion adversaire en (3, 4)
        model.setPlateauJeu(2, 3, model.PLAYER1);
        model.setPlateauJeu(3, 4, model.PLAYER2);

        // Appele la méthode pour obtenir les déplacements après capture pour le joueur 1
        int[][] movesAfterCapturePlayer1 = model.getPossibleMovePionAfterCaptures(2, 3, model.PLAYER1);

        model.setSelectedPionX(2);
        model.setSelectedPionY(3);
        model.setPlateauJeu(5, 6, model.EMPTY);
        model.setPlateauJeu(4, 6, model.EMPTY);
        // Vérifie que les déplacements après capture retournés sont corrects
    //    assertArrayEquals(new int[]{5, 6}, movesAfterCapturePlayer1[0]);
       //assertArrayEquals(new int[]{4, 6}, movesAfterCapturePlayer1[0]);
    }

    @Test
    void testChangePlayer(){
        model.setActuelPlayer(model.PLAYER1);
        assertEquals(model.PLAYER1, model.getactuelPlayer());
        model.changePlayer(true);
        assertEquals(model.PLAYER2, model.getactuelPlayer());
        model.setActuelPlayer(model.PLAYER2);
        assertEquals(model.PLAYER2, model.getactuelPlayer());
        model.changePlayer(true);
        assertEquals(model.PLAYER1, model.getactuelPlayer());
    }

    @Test
    public void testIsMoveAndBouffe() {
        // Initialise le plateau avec une configuration spécifique
        model.InitPlateaujeu();

        // Place un pion joueur 1 en position (2, 3) et un pion adversaire en (3, 4)
        model.setPlateauJeu(2, 3, model.PLAYER1);
        model.setPlateauJeu(3, 4, model.PLAYER2);

        // Sélectionne le pion joueur 1
        model.setSelectedPionX(2);
        model.setSelectedPionY(3);

        model.setPlateauJeu(4, 5, model.EMPTY);

        // Teste la méthode avec un déplacement valide
        assertTrue(model.isMoveAndBouffe(4, 5, model.PLAYER1));

        // Teste la méthode avec un déplacement invalide
        assertFalse(model.isMoveAndBouffe(3, 4, model.PLAYER1));

    }

    @Test
    public void testPionToDame() {
        model.InitPlateaujeu();

        // Placer un pion joueur 1 en position (2, 6)
        model.setPlateauJeu(2, 7, model.PLAYER1);

        model.pionToDame(2, 7, model.PLAYER1);

        assertEquals('D', model.getPlateauJeu()[7][2]);

        // Vérifier que le nombre de dames pour le joueur 1 a été incrémenté
        assertEquals(1, model.getNbrDamePlayer1());

        model.setPlateauJeu(2, 0, model.PLAYER2);

        model.pionToDame(2, 0, model.PLAYER2);

        assertEquals('d', model.getPlateauJeu()[0][2]);


    }

    @Test
    public void testPossibleMoveDame() {
        // Initialise le plateau avec une configuration spécifique
        model.InitPlateaujeu();

        // Placer une dame joueur 1 en position (3, 3)
        model.setPlateauJeu(4, 3, model.DAMEPLAYER1);
        model.setPlateauJeu(3, 4, model.EMPTY);
       model.setPlateauJeu(1,6, model.EMPTY);
       model.setPlateauJeu(2, 5, model.PLAYER2);
       model.setPlateauJeu(0, 7, model.EMPTY);
       model.setPlateauJeu(6, 5, model.PLAYER2);
       model.setPlateauJeu(7, 6, model.PLAYER2);
       model.setPlateauJeu(5, 4, model.PLAYER2);
        // Appel la méthode pour obtenir les déplacements possibles pour la dame
        int[][] possibleMoves = model.possibleMoveDame(4, 3, model.DAMEPLAYER1);


       // assertEquals(2, possibleMoves.length); // Vérifie le nombre de déplacements possibles
        assertArrayEquals(new int[]{3, 4}, possibleMoves[0]);
       //assertArrayEquals(new int[]{1, 6}, possibleMoves[1]);


    }

    @Test
    public void testIsPlayerWin() {
        model.InitPlateaujeu();

        // Vérifie que le jeu n'est pas encore gagné pour les joueurs
        assertFalse(model.isPlayerWin(model.PLAYER1));
        assertFalse(model.isPlayerWin(model.PLAYER2));

        // Supprime tous les pions du joueur 2
        model.setNbrPionPlayer2(0);

        // Vérifie que le joueur 1 a gagné car le joueur 2 n'a plus de pions
        assertTrue(model.isPlayerWin(model.PLAYER1));
        assertFalse(model.isPlayerWin(model.PLAYER2));

        // Réinitialise le plateau et définir un nombre de pions différent
        model.InitPlateaujeu();
        model.setNbrPionPlayer1(8);
        model.setNbrPionPlayer2(4);

        // Vérifie que le joueur 1 a gagné car il a plus de pions que le joueur 2
      //  assertTrue(model.isPlayerWin(model.PLAYER1));
        assertFalse(model.isPlayerWin(model.PLAYER2));

    }

    @Test
    public void testIsNoMovePossible() {
        // Initialiser le plateau avec une configuration spécifique
        model.InitPlateaujeu();

        // Vérifier qu'il y a des mouvements possibles pour les joueurs
        assertFalse(model.isNoMovePossible(model.PLAYER1));
        assertFalse(model.isNoMovePossible(model.PLAYER2));


        // Vérifie qu'il n'y a plus de mouvements possibles pour les joueurs
        //assertTrue(model.isNoMovePossible(model.PLAYER1));
       // assertTrue(model.isNoMovePossible(model.PLAYER2));

        // Ajouter d'autres assertions pour tester différents scénarios
    }

    @Test
    public void testGetWinnerBeiGameOver() {
        // Initialise le plateau avec une configuration spécifique
        model.InitPlateaujeu();

        // Vérifie qu'il n'y a pas de gagnant avant la fin de la partie
        assertEquals(' ', model.getWinnerBeiGameOver());

        // Simule une victoire du joueur 1 (PLAYER1)
        model.setNbrPionPlayer2(0);
        model.setNbrPionPlayer1(10);
        assertEquals(model.PLAYER1, model.getWinnerBeiGameOver());
        model.setPreviousWinner(model.PLAYER1);
        assertEquals(model.PLAYER1, model.getPreviousWinner());
        //assertEquals(Gamestate.PLAYING, model.getState());
        model.setState(Gamestate.START);
        assertEquals(Gamestate.START, model.getState());

        model.InitPlateaujeu();

        // Simule une victoire du joueur 2 (PLAYER2)
        model.setNbrPionPlayer1(0);
        model.setNbrPionPlayer2(4);
        assertEquals(model.PLAYER2, model.getWinnerBeiGameOver());
       // assertEquals(Gamestate.GAME_OVER, model.getState());
        model.setState(Gamestate.GAME_OVER);
        assertEquals(Gamestate.GAME_OVER, model.getState());

    }

    @Test
    public void testIsInsideBoard() {
        // Initialise le plateau avec une configuration spécifique
        model.InitPlateaujeu();

        // Vérifie que les coordonnées à l'intérieur du plateau retournent true
        assertTrue(model.isInsideBoard(0, 0));
        assertTrue(model.isInsideBoard(4, 4));
        assertTrue(model.isInsideBoard(7, 7));

        // Vérifier que les coordonnées en dehors du plateau retournent false
        assertFalse(model.isInsideBoard(-1, 0));
        assertFalse(model.isInsideBoard(0, -1));
        assertFalse(model.isInsideBoard(8, 0));
        assertFalse(model.isInsideBoard(0, 8));
        assertFalse(model.isInsideBoard(8, 8));

    }

    @Test
    public void textIncrementVictories(){
        model.setPreviousWinner(model.PLAYER1);
        model.setVictoirePlayer1(1);
        model.setManche(1);
        model.incrementVictories();
        assertEquals(2, model.getVictoirePlayer1());
        assertEquals(2, model.getManche());

        model.setPreviousWinner(model.PLAYER2);
        model.setVictoirePlayer2(2);
        model.setManche(3);
        model.incrementVictories();
        assertEquals(3, model.getVictoirePlayer2());
        assertEquals(4, model.getManche());

        model.setPreviousWinner(' ');
        model.setVictoirePlayer2(2);
        model.setVictoirePlayer1(2);
        model.setManche(4);
        model.incrementVictories();
        assertEquals(2, model.getVictoirePlayer2());
        assertEquals(2, model.getVictoirePlayer1());
        assertEquals(5, model.getManche());
    }

    @Test
    public void testToString() {
        // Initialiser le plateau avec une configuration spécifique
        model.InitPlateaujeu();


       String recep =   String.format("%c %c %c %c %c %c %c %c\n%c %c %c %c %c %c %c %c\n%c %c %c %c %c %c %c %c\n%c %c %c %c %c %c %c %c\n%c %c %c %c %c %c %c %c\n%c %c %c %c %c %c %c %c\n%c %c %c %c %c %c %c %c\n%c %c %c %c %c %c %c %c\n",
                model.getPlateauJeu()[0][0], model.getPlateauJeu()[0][1], model.getPlateauJeu()[0][2], model.getPlateauJeu()[0][3], model.getPlateauJeu()[0][4], model.getPlateauJeu()[0][5], model.getPlateauJeu()[0][6], model.getPlateauJeu()[0][7],
                model.getPlateauJeu()[1][0], model.getPlateauJeu()[1][1], model.getPlateauJeu()[1][2], model.getPlateauJeu()[1][3], model.getPlateauJeu()[1][4], model.getPlateauJeu()[1][5], model.getPlateauJeu()[1][6], model.getPlateauJeu()[1][7],
                model.getPlateauJeu()[2][0], model.getPlateauJeu()[2][1], model.getPlateauJeu()[2][2], model.getPlateauJeu()[2][3], model.getPlateauJeu()[2][4], model.getPlateauJeu()[2][5], model.getPlateauJeu()[2][6], model.getPlateauJeu()[2][7],
                model.getPlateauJeu()[3][0], model.getPlateauJeu()[3][1], model.getPlateauJeu()[3][2], model.getPlateauJeu()[3][3], model.getPlateauJeu()[3][4], model.getPlateauJeu()[3][5], model.getPlateauJeu()[3][6], model.getPlateauJeu()[3][7],
                model.getPlateauJeu()[4][0], model.getPlateauJeu()[4][1], model.getPlateauJeu()[4][2], model.getPlateauJeu()[4][3], model.getPlateauJeu()[4][4], model.getPlateauJeu()[4][5], model.getPlateauJeu()[4][6], model.getPlateauJeu()[4][7],
                model.getPlateauJeu()[5][0], model.getPlateauJeu()[5][1], model.getPlateauJeu()[5][2], model.getPlateauJeu()[5][3], model.getPlateauJeu()[5][4], model.getPlateauJeu()[5][5], model.getPlateauJeu()[5][6], model.getPlateauJeu()[5][7],
                model.getPlateauJeu()[6][0], model.getPlateauJeu()[6][1], model.getPlateauJeu()[6][2], model.getPlateauJeu()[6][3], model.getPlateauJeu()[6][4], model.getPlateauJeu()[6][5], model.getPlateauJeu()[6][6], model.getPlateauJeu()[6][7],
                model.getPlateauJeu()[7][0], model.getPlateauJeu()[7][1], model.getPlateauJeu()[7][2], model.getPlateauJeu()[7][3], model.getPlateauJeu()[7][4], model.getPlateauJeu()[7][5], model.getPlateauJeu()[7][6], model.getPlateauJeu()[7][7]);

        assertEquals(recep, model.toString());

    }

}


