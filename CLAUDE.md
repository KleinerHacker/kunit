# Übersicht

* Verrechnung von Einheiten
* Basis bildet immer eine "Mischeinheit"
  * Dies ist eine Einheit bestehend aus mehreren Einheiten
* Ermöglicht das Rechnen in physikalischen Umgebungen mit echten Einheiten in Double

# Architektur

* `KUnitInstance` - Bildet eine Mischeinheit ab.
  * Besteht aus einem Double (Basis Wert)
  * Besteht aus ein oder mehreren `KUnit`s, jeweils als Paar mit seinem Exponenten
    * Der Exponent ist ein Integer, der positiv (für den Nenner) oder negativ (für den Zähler) ist
    * Die `KUnits` werden untereinander multipliziert
* `KUnit` - Bildet eine Einheit ab.
  * Ist ein **Interface** (nicht Klasse), da konkrete Einheiten pro Gruppe als `enum class ... : KUnit` abgebildet werden
    (Enums können in Kotlin keine Klassen erweitern, aber Interfaces implementieren)
  * Besteht aus einem String (Symbol)
  * Besteht aus einem Double (Basis Wert), dem Umrechnungsfaktor zur Basiseinheit der Gruppe
  * Diese gehört zu einer Gruppe von Einheiten, z. B. Length (dazu gehören dann z. B. Metric, Miles, Yards, ...)
    * Für jede "reine" Einheit wird eine eigene `enum class` (z. B. `LengthUnit`) verwendet
    * Eine Gruppe deklariert ihre Basiseinheit explizit (z. B. `LengthUnit.BASE`)
* Eine `KUnitInstance` wird für jede Gruppe von Einheiten für "reine Einheiten" gewrappt
  * Die Wrapperklassen (z. B. `LengthUnitInstance`) kapseln eine `KUnitInstance` per Delegation
    (kein Vererbungsverhältnis) und speichern ihren Wert **immer normalisiert auf die Basiseinheit der Gruppe**
* SI-Vorsilben (kilo, hecto, deca, deci, centi, milli) sind kein Bestandteil von `KUnit`/dem (KUnit, Exponent)-Paar,
  da sie nur beim Ein-/Auslesen von Werten relevant sind. Sie werden über ein generisches `KUnitPrefix`-Enum
  (Root-Paket) abgebildet; jedes Einheiten-Sub-Paket stellt dafür eigene `infix`-Funktionen bereit
  (z. B. `5 kilo meters` in `length`), die den Rohwert vor der Normalisierung auf die Basiseinheit skalieren
* Für bestimmte Kombinationen aus Einheiten-Gruppe und Exponent existieren **Spezialeinheiten** mit eigenem
  Namen/Symbol und eigenem Umrechnungsfaktor (z. B. Hektar für Fläche = Länge², Liter für Volumen = Länge³).
  Diese ersetzen **nicht** den normalen Mechanismus (z. B. bleibt die Basiseinheit mit Exponent 2 weiterhin die
  "rohe" Darstellung einer Fläche) - sie sind rein zusätzliche, gruppen- und exponentengebundene Konvertierungs-Ziele
  für Ein-/Ausgabe, generisch über den referenzierten Einheitstyp (Compile-Zeit-Gruppensicherheit), analog zu den
  Vorsilben kombinierbar
* Erzeugung (nur Konstruktor und Erzeuger-Erweiterungsfunktionen) von `KUnitInstance`/den Wrapperklassen ist von
  jedem `Number`-Typ aus möglich (`Int`, `Long`, `Float`, `Double`, ...), nicht nur `Double`; intern wird stets zu
  `Double` normalisiert. Alle Ausgaben (Wert, Umrechnungen, Textdarstellung) sind ausnahmslos `Double`.
  Operatoren und Vergleichsoperatoren arbeiten dagegen **nie** direkt mit nackten `Number`-Werten - nur zwischen
  zwei Unit-Typen

## Package Strukturen

* Das Root Paket heißt `org.pcsoft.framework.kunit`
* Für jede "reine" Einheit wird ein Sub-Paket erstellt
* Die Basis Klassen `KUnit` und `KUnitInstance` befinden sich im Root Paket

# Implementierung

## Dokumentation

* Jedes öffentliche Member muss auf Englisch dokumentiert werden
* Die Dokumentation sollte in Markdown formatiert sein
* Die Dokumentation soll umfangreich sein und ggf. Beispiele enthalten
  * vor allem für Operatoren

## Operatoren

* Alle Standard Operatoren '+', '-', '*', '/' müssen unterstützt werden für:
  * "reine" Einheiten
  * Mischeinheiten
  * Mixin von "reinen" Einheiten und Mischeinheiten
* Alle Standard Vergleichsoperatoren '==', '!=', '<', '<=', '>', '>=' müssen unterstützt werden für:
  * "reine" Einheiten
  * Zusätzlich neben dem klassischen Equals muss es eine Methode zur Prüfung der Einheit (`KUnit` + Exponent) geben 
    bei Mischenheiten
* Sowohl `KUnitInstance` als auch die "reinen" Wrapperklassen bieten neben dem normalisierten Rohwert eine
  Möglichkeit, einen umgerechneten Wert für eine gewünschte Zieleinheit auszulesen sowie eine `toString`-Überladung,
  die diese Zieleinheit(en) in der Textausgabe berücksichtigt. Zieleinheiten können dabei eine reine Einheit oder
  eine per Vorsilbe/Spezialeinheit skalierte Einheit sein

### Fehlerbehandlung

* Bei Vergleichen:
  * Bei Unterschieden in den `KUnit` bzw deren Exponenten muss ein Fehler geworfen werden: IllegalStateException

## Konvertierung

* Jede "reine" Einheit bietet über eine Erweiterungsmethode an, eine `KUnitInstance` zu dieser umzuwandeln
* Bei Verrechnung einer gleichen "reinen" Einheit wird diese wieder zurückgegeben
* Bei Verrechnung verschiedener "reiner" Einheiten wird eine neue `KUnitInstance` zurückgegeben
* Bei Verrechnung einer "reinen" Einheit mit einer Mischeinheit oder Mischeinheiten untereinander werden neue `KUnitInstance`n zurückgegeben

### Fehlerbehandlung

* Jede Konvertierung zu einer "reinen" Einheit muss prüfen, ob diese auch in einer Mischeinheit vorliegt
  * Wenn nicht: IllegalStateException
* Verrechnungen mit '*' sind immer erlaubt
  * Für jede Einheit, die bereits vorhanden ist, werden beide Exponenten addiert
  * Für jede Einheit, die noch nicht vorhanden ist, wird eine neue in `KUnitInstance` mit dem Exponenten 1 erstellt
* Verrechnungen mit '/' sind immer erlaubt
  * Für jede Einheit, die bereits vorhanden ist, werden beide Exponenten subtrahiert
  * Für jede Einheit, die noch nicht vorhanden ist, wird eine neue in `KUnitInstance` mit dem Exponenten -1 erstellt
* Verrechnungen mit '+' oder '-' sind nur erlaubt, wenn
  * Zwei "reine" Einheiten (Wrapperklassen wie `LengthUnitInstance`) verrechnet werden, die derselben Einheiten-Gruppe
    angehören (z. B. Meter + Meile ist erlaubt, automatische Umrechnung über die Normalisierung) **und** denselben
    Exponenten besitzen (z. B. Fläche darf nicht mit Volumen verrechnet werden)
  * Zwei Mischeinheiten (`KUnitInstance`) untereinander verrechnet werden mit exakt den gleichen `KUnit`s und deren
    Exponenten (keine automatische Umrechnung)
    * Auch bei gleichen `KUnit`s mit unterschiedlichen Exponenten schlägt der Vorgang fehl
    * Ergebnis: IllegalStateException

## Tests

* Jede "reine" Einheit wird separat getestet
  * Volle Tests für eine möglichst vollständige Testabdeckung
  * Vollständige Tests für alle Operationen
  * Jede Operator-Funktion ('+', '-', '*', '/') und jede Vergleichsoperation ('==', '!=', '<', '<=', '>', '>=')
    wird pro Typ mindestens einmal mit einem Erfolgsfall und, wo ein Fehler vorgesehen ist, mindestens einmal mit
    dem entsprechenden Fehlerfall (IllegalStateException) getestet - es reicht nicht, nur einen Operator
    stellvertretend für alle zu testen
  * Für jede in einer Gruppe definierte Einheit und jede Spezialeinheit existiert ein eigener Test je Vorsilbe,
    der die Kombination aus Vorsilbe und Einheit (Konstruktion + Rückrechnung) verifiziert - eine vollständige
    Vorsilbe-×-Einheit-Matrix, keine Stichproben. Zusätzlich wird für jede einzelne Vorsilbe mindestens ein
    eigenständiger, von der jeweiligen Einheit unabhängiger Test ergänzt
* Die Mischeinheiten werden getestet
  * Zusammengesetzt mit jeweils mindestens einer anderen Einheit
  * Jede "reine" Einheit wird zusammen mit einer Mischeinheit getestet
  * Bei einer "reinen" Einheit, welche aus einer Mischeinheit besteht (z. B. Newton) besteht ein Test, 
    der zu dieser Einheit rechnet, oder von der Einheit zu einer anderen "reinen" Einheit.

Grundsätzlich testen alle Tests die Richtigkeit der Werte und Rechnungen.

## Umsetzung

Der Status der Umsetzung wird in STATUS.md dokumentiert.