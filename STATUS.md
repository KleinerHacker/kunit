# Umsetzungsstatus

## Fertig

* Root-Paket `org.pcsoft.framework.kunit`:
  * `KUnit`, `KUnitTarget` (Basis-Interfaces)
  * `KUnitInstance`, `KUnitTerm` (Mischeinheiten-Engine inkl. `+`, `-`, `*`, `/`, `hasSameUnits`, `valueAs`, `toString`-Überladung)
  * `KUnitPrefix` (vollständige SI-Vorsilben-Tabelle, Quetta/Q bis Quecto/q, 24 Werte) sowie `KScaledUnit`,
    `KDerivedUnit`, `KScaledDerivedUnit` und die `with`-Infix-Funktionen (Output-Konvertierung)
  * `KPrefixBuilder` + 24 generische, gruppen-unabhängige Prefix-`infix`-Funktionen zur Konstruktion
    (z. B. `5 kilo meters`), gefolgt von `toKUnitInstance()` und der gruppen-spezifischen `toXxxUnit()`-Konvertierung
* Sub-Paket `org.pcsoft.framework.kunit.length` (Prototyp für die physikalische Größe Länge):
  * `KLengthUnit` (Meter, Meile, Seemeile, Yard, Foot, Inch, Fathom, Chain, Furlong, Astronomische Einheit, Lichtjahr, Parsec)
  * `KLengthUnitInstance` (inkl. `+`, `-`, `*`, `/`, Vergleichsoperatoren, `valueIn`, `toString`-Überladung,
    `toKUnitInstance`) - kapselt beliebige Exponenten von `KLengthUnit.BASE` (Länge, Fläche, Volumen, ...)
  * `KLengthUnitExtensions` (Erzeuger-Funktionen je Länge-, Flächen- und Volumeneinheit, `toKLengthUnit`)
  * `KLengthDerivedUnit` (Fläche: Are, Hektar, Acre; Volumen: Liter, US-/Imperial-Gallone, US Fluid Ounce, Ölfass)
* Alle Length-Typen tragen konsistent das `K`-Präfix-Namensschema (`KLengthUnit`, `KLengthUnitInstance`, `KLengthDerivedUnit`)
* Vollständige Testsuite für alle oben genannten Klassen (Root-Paket + `length`-Paket)
* `CLAUDE.md` um die im Rahmen dieses Prototyps getroffenen Architekturentscheidungen ergänzt

## Offen

* Weitere Einheiten-Gruppen (z. B. Masse, Zeit, Temperatur, ...) nach dem hier etablierten `length`-Muster
* Zusammengesetzte "reine" Einheiten, die selbst aus einer Mischeinheit bestehen (z. B. Newton), inkl. Rück- und Hinkonvertierung (siehe CLAUDE.md-Abschnitt "Tests")
