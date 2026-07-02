# Umsetzungsstatus

## Fertig

* Root-Paket `org.pcsoft.framework.kunit`:
  * `KUnit`, `KUnitTarget` (Basis-Interfaces)
  * `KUnitInstance`, `KUnitTerm` (Mischeinheiten-Engine inkl. `+`, `-`, `*`, `/`, `hasSameUnits`, `valueAs`, `toString`-Überladung)
  * `KUnitPrefix` (SI-Vorsilben) sowie `KScaledUnit`, `KDerivedUnit`, `KScaledDerivedUnit` und die `with`-Infix-Funktionen
* Sub-Paket `org.pcsoft.framework.kunit.length` (Prototyp für die physikalische Größe Länge):
  * `LengthUnit` (Meter, Meile, Seemeile, Yard, Foot, Inch, Fathom, Chain, Furlong, Astronomische Einheit, Lichtjahr, Parsec)
  * `LengthUnitInstance` (inkl. `+`, `-`, `*`, `/`, Vergleichsoperatoren, `valueIn`, `toString`-Überladung, `toKUnitInstance`)
  * `LengthUnitExtensions` (Erzeuger-Funktionen je Einheit + sechs Prefix-`infix`-Funktionen, `toLengthUnit`)
  * `LengthDerivedUnit` (Fläche: Are, Hektar, Acre; Volumen: Liter, US-/Imperial-Gallone, US Fluid Ounce, Ölfass)
* Vollständige Testsuite für alle oben genannten Klassen (Root-Paket + `length`-Paket)
* `CLAUDE.md` um die im Rahmen dieses Prototyps getroffenen Architekturentscheidungen ergänzt

## Offen

* Weitere Einheiten-Gruppen (z. B. Masse, Zeit, Temperatur, ...) nach dem hier etablierten `length`-Muster
* Zusammengesetzte "reine" Einheiten, die selbst aus einer Mischeinheit bestehen (z. B. Newton), inkl. Rück- und Hinkonvertierung (siehe CLAUDE.md-Abschnitt "Tests")
