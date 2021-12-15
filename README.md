# Projektvorlage_KAGO4_Gradle
###_Leere Projektvorlage für alle Projekte auf Basis von KAGO4_

##Wichtig: der erste Schritt
Nach dem initialen Klonen bitte zunächst einmal unten in der Leiste oder oben über das Menü "Build" => "Build Project" (STRG+F9) aufrufen, 
damit das Projekt korrekt eingerichtet wird.
Im Anschluss sollte alles funktionieren.

###Falls nicht:
File > Settings > Gradle suchen. Dann dort als SDK eine JDK mit Version zwischen 11 und 16 wählen und ggf. downloaden. 
Der Eintrag "use-Gradle-from" kann auf "wrapper-task in Gradle.build-script" umgestellt werden.
Nun sollte alles Nötige heruntergeladen werden. Eventuell danach noch Build.

##Kurzanleitung: wo ist was?
Alle wichtigen Dinge zum Arbeiten befinden sich im Ordner **src > main > java**. Hier befindet sich der Ordner "KAGO_Framework" das komplette Framework. Es ist les- und editierbar, aber alle Änderungen sind immer auf eigenes Risiko!

###Der Ordner "my_project"
Dies ist der Ordner zum Arbeiten.
* In diesem Ordner findest du die Klasse "MainProgram". Mit dieser Klasse wird das Programm gestartet (Rechtsklick > Run).
* Innerhalb von "my_project" sind außerdem die drei Unterordner "control", "model" und "view". Diese Ordner nennt man "Pakete" oder "packages". 
  Für den Anfang kannst du dir merken, dass in "control" die Klassen gehören, die das Programm steuern und in "model" die Klassen stehen, die "Dinge" im Programm repräsentieren. 
  Du erfährst später mehr dazu.
* Im Paket "control" befindet sich die wichtigste Klasse: "ProgramController", deren Objekt den Kern des Programms steuert.

###Der Ordner "ressources"
In diesem Ordner sind die beiden Unterordner "graphic" und "sound"
* In "graphic" kannst du alle Bilddateien ablegen, die dein Projekt verwenden soll. Am besten sind Bilder im .png-Format mit transparentem Hintergrund und optimal ist es, dass du die Bilder vorher der Verwendung auf eine passende Größe bearbeitest. Ein gutes Programm dazu ist z.B. paint.net.
* In "sound" kannst du Töne und Musik ablegen, z.B. im beliebten .mp3-Format. Dann kannst du diese im Projekt über die Klasse SoundController benutzen.

###_(c) Kneblewski & Ambrosius_
