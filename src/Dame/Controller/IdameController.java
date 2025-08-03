package Dame.Controller;

/**
 * Methode des Controllers, auf die den View zugriff hat
 */
public interface IdameController {
 /**
  * gibt das Spielbrett zurück
  * @return gamestates
  */
    char[][] getTableau();


 /**
  *Wenn die Funktion aufgerufen wird, während sich der Benutzer im Startbildschirm befindet,
  *  wird das Spiel gestartet.
  * @param x x-koordinaten von dem ausgewählten SpielStein
  * @param y y-koordinaten von dem ausgewählten SpielStein
  */
 void userInput(int x , int y);

 /**
  * gibt den x-koordinaten des ausgewählten SpielSteins
  * @return model.getSelectedPionX()
  */
    int getSelectedX();

 /**
  * gibt den y-koordinaten des ausgewählten SpielSteins
  * @return model.getSelectedPionY()
  */
    int getSelectedY();


 /**
  * Unsere methode PossibleMovePion bestimmt für einfachen SpielStein von seinen Koordinaten aus seine möglichen bewegungen mit oder ohne Aufnahme
  *
  * @param x      x-koordinaten des ausgewählten SpielSteins
  * @param y      Y-koordinaten des ausgewählten SpielSteins
  * @param player Aktueller Spieler
  * @return model.getPossibleMovePion
  */
    int[][] getPossibleMovePion(int x, int y, char player);

 /**
  * gibt an, ob einen Zug oder Schlag für einen einfachen Spielstein möglich ist oder nicht
  *
  * @return model.getPrisePossible
  */
    boolean getPrisePossible();


 /**
  * gib an, ob eine Aufnahme beim Bewegen möglich ist
  *
  * @param x      x-koordinaten der ausgewälten SpielStein
  * @param y      Y-koordinaten der ausgewälten SpielStein
  * @param player Aktuel Spieler
  * @return true oder false
  */
    boolean MoveandBouffeCtrl(int x , int y, char player);

 /**
  * gibt den aktuelen Player aus dem model
  * @return model.getactuelPlayer()
  */
 char getactuelPlayer();

 /**
  * Unsere methode PossibleMoveDame bestimmt für eine von seinen Koordinaten aus seine möglichen bewegungen
  *
  * @param x      x-koordinaten der ausgewälten Dame
  * @param y      Y-koordinaten der ausgewälten Dame
  * @param player Aktuel Spieler
  * @return eine Liste von allen möglichen Bewegungen für den gewählten SpielStein
  */
 int[][] possibleMoveDame(int x, int y, char player);

 /**
  * Unsere methode PossibleMovePionAfterCapture bestimmt für einfachen SpielStein nach einer ersten Bewegung mit aufnahme von seinen neuen Koordinaten aus seine neuen möglichen bewegungen mit oder ohne Aufnahme
  *
  * @param x      x-koordinaten des ausgewählten SpielSteins
  * @param y      Y-koordinaten des ausgewählten SpielSteins
  * @param player Aktueller Spieler
  * @return model.getPossibleMovePionAfterCaptures();
  */
 int [][] getPossibleMovePionAfterCapture(int x, int y, char player);

 /**
  * gibt an, ob ein Feld eine dame enthält oder nicht
  * @return model.getIsDame()
  */
 boolean[][] getIsDame();

 /**
  * Ruft die Zeichenmethoden der view auf, abhängig vom aktuellen Spielzustand.
  */
 void handleDisplay ();

 /**
  * verwaltet die Benutzereingaben
  * @param key key
  */
 void handleUserInput (char key);

 /**
  * gibt die Anzall der Spielsteine der ersten Player zurück
  *
  * @return model.getNbrPionPlayer1
  */
 int getNbrPionPlayer1();

 /**
  * gibt die Anzall der Spielsteine der zweiten Player zurück
  *
  * @return model.getNbrPionPlayer2()
  */
 int getNbrPionPlayer2();

 /**
  * gibt die Anzahl der Dame des zweiten Spielers
  *
  * @return model.getNbrDamePlayer2()
  */
 int getNbrDamePlayer2();

 /**
  * gibt die Anzahl der Dame des zweiten Spielers
  *
  * @return model.getNbrDamePlayer1()
  */
 int getNbrDamePlayer1();

 /**
  * gibt den Gewinner des Spiels zurück
  *
  * @return model.getWinnerbeiGameOver()
  */
 char getWinnerBeiGameOver();

 /**
  * gibt die verbleibende Zeit zum Spielen zurück
  *
  * @return model.getRemainingTime()
  */
 int getRemainingTimer();

 /**
  * gibt die Anzahl der Siege des ersten Spielers zurück
  *
  * @return model.getVictoirePlayer1()
  */
 int getVictoirePlayer1();

 /**
  * gibt die Anzahl der Siege des zweiten Spielers zurück
  *
  * @return model.getVictoirePlayer2()
  */
 int getVictoirePlayer2();

 /**
  * gibt die Anzahl der gespielten Runde zurück
  *
  * @return model.getManche()
  */
 int getManche();

}
