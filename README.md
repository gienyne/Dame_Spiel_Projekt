# Dame'Spiel

Dame ist ein strategisches BrettSpiel für zwei Spieler und wird auf einem Schachbrett mit 8 * 8 oder
international 10 * 10 Feldern gespielt..Dabei werden nur die schwarzen Felder des Spielbretts genutzt
, auf denen die typischen scheibenförmigen Spielsteine gezogen werden..Ziel des Spiels ist es ,
die generischen Steine vollständig durch überspringen zu schlagen oder bewegungsunfähig
zu machen und so das Spiel zu gewinnen..Einer der Spieler verliert entweder wenn er keinen Stein mehr hat oder wenn er mit seinen Steinen
keinen Zug mehr machen kann , weil seine Steine durch seinen Gegner blockiert sind.. oder wenn nach der Timer einer der Spieler 
mehr Steine als der andere hat..

## Verwendete Bibliotheken

Das Program verwendet die folgenden Bibliotheken

- [Processing](http://www.processing.org)
- [JUnit](https://junit.org/junit5/)

## Screenschots
Kurze Übersicht über das Spiel..

![StartBildUnit](images/StartBildUnit.png)

StartBild..


![PlateauUnit1](images/PlateauUnit1.png)

Hier haben Sie einen kurzen Übersicht über die Funktionalitäten..
Zu beachten hier ist allerdings der Timer..Beim Ende der Timer wird 
das Spiel sofort beendet..

![PlateauUnit2](images/plateauUnit2.png)

Hier kann festgestellt werden, dass die verschiedenen möglichen bewegungen für
einen Stein entweder rot oder grün oder die rot und grün dargestellt.
wird der mögliche Zug rot dargestellt dann bedeutet das, dass einen Schlag 
in diese Richtung möglich ist..wird der hingegen grün dargestellt dann zeigt es
einen einfachen Zug...

![PlateauUnit3](images/plateauUnit3.png)

verglichen mit den möglichen Bewegungen eines Stein wird die möglichen Züge von der Dame
durch grüne Punkte dargestellt unabhängig davon ob einen Schlag möglich ist oder nicht

![gameOverBildUnit](images/gameOverBildUnit.png)

EndBild wenn der Gewinner der Player1 ist

![gameOverBilldUnit2](images/gameOverBilldUnit2.png)

EndBild wenn der Gewinner der Player2 ist

![gameOverBildUnit0](images/gameOverBildUnit0.png)

EndBild wenn keiner gewinnt


## Startanleitung
zum **Starten des Spiels**  muss die `main()`-Methode
in der *Datei*`Main.java` vorhanden sein.

1. Öffnen der Datei `Main.java`
2. Starten der Funktion `main()`

## JShell Anleitung

1. Starten einer Konsole
2. Den Befehl `jshell --class-path ./out/production/damesGame
   `in der Kommandozeile eingeben.
3. Importieren Sie die Package Dame.Model mit dem Befehl `import Dame.Model.*;
   `, die in dem Modell verwendet werden, mit dem Befehl import. ( Zb für die Klasse: *dameModel* würde das so aussehen: `jshell> import Dame.Model.dameModel;`)
4. Erstellen Sie ein Objekt des Modells, indem Sie den entsprechenden Konstruktor aufrufen. Hier geben Sie Folgendes ein: `jshell> dameModel model = new dameModel();` meistens ist die `jshell> Kommando` schon vorhanden dann brauchen Sie nur das hier `dameModel model = new DameModel();` einzugeben.
5. Rufen Sie dann eine Methode auf dem bereits herstellten *Objekt* auf, um Veränderungen im Model vorzunehmen. (wenn Sie ein neues Spiel starten wollen, können Sie Folgendes eingeben: `jshell> model.newgame();` Sie können auch einen Überblick auf den Aktuelen Zustand des Spieler , indem Sie die toString-Funktion des Modells verwenden und in der Kommendozeile : `jshell> model.toString();`eingeben.)
6. Wenn Sie das SpielBrett zum Beispiel initialisieren wollen können Sie *model.InitPlateaujeu()* eingeben oder wenn sie die Anzahl der SpielSteine der ersten Player abfragen wollen können Sie folgendes eingeben:mode ` model.getNbrPionPlayer1() `