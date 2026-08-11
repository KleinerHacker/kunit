# Fehlende Einheiten nachrüsten

## Kontext

KUnit deckt aktuell **69 Unit-Gruppen** in den Fachgebieten `common`, `kinematic`, `mechanic`,
`electric`, `thermo` und `it` ab. Sechs der sieben SI-Basiseinheiten sind vorhanden (m, s, kg, A, K,
mol) — die **Candela fehlt**, und damit das gesamte Fachgebiet Optik/Photometrie. Daneben sind in
Chemie, Mechanik, Thermodynamik und Elektrotechnik einzelne Normalformen unbelegt. Diese Bestands-
aufnahme wurde gegen `src/main/kotlin`, `docs/mkdocs.yml` und `README.md` verifiziert.

Ziel: Die identifizierten Lücken schließen, damit KUnit die SI-Basis vollständig abbildet und die
gängigen abgeleiteten Größen der fünf Fachgebiete typisiert unterstützt.

Randbedingung aus `rules/architecture-unit.md`: pro kanonischer Basisdimensions-Normalform genau
**ein** Typ, damit `toX()` eindeutig bleibt. Dimensionsgleiche Größen werden nur als weitere Lesart
dokumentiert (Muster: `docs/docs/units/thermodynamics/entropy.md`,
`docs/docs/units/mechanics/stiffness.md`).

## Entscheidungen des Users

* Neues Fachgebiet: Code `org.pcsoft.framework.kunit.optic`, MkDocs `docs/docs/units/optics/`
* Flächenträgheitsmoment (m⁴) wird als Exponenten-Spezialisierung in `kinematic.distance` aufgenommen
* Strahlungsgrößen (Bq, Gy, Sv) nur als Doku-Lesarten, plus die echten neuen Gruppen Dosisleistung
  und Ionendosis
* Wärmewiderstand: **harte Umbenennung** ohne Deprecation-Phase (Breaking Change, bewusst gewählt)

## Umsetzungsmuster (gilt für jede neue Gruppe)

Vorlage ist die zuletzt gebaute Gruppe `thermo/molarvolume` (zwei Dekompositionen, `toX()`-Hook).
Pro Gruppe entstehen:

* `KXUnit.kt` — Enum mit `symbol`/`baseValue`, `BASE` im Companion
* `KXUnitInstance.kt` — Wrapper über `KMixedUnitInstance`, `plus`/`minus`/`times`/`div`/`compareTo`,
  wertbasiertes `equals`/`hashCode`/`toString`, die **eine** normalisierende Factory
  `xInstanceOf(...)`, `xOfUnit(...)` und der `KMixedUnitInstance.toX()`-Hook
* `KXUnitBareValues.kt` — Wert-1-Tokens
* `KXUnitExtensions.kt` — Prefix-Builder (`KAugmentingPrefixBuilder`/`KDiminishingPrefixBuilder`)
* `KXUnitOperators.kt` — typisierte Dekompositionen und deren Inverse, inkl. kommutativer Formen

Der Root-Engine (`KMixedUnitInstance`, `KUnit`, `KPrefixBuilder`) wird **nicht** angefasst — neue
Basiseinheiten sind lediglich neue Enums, die `KUnit` implementieren.

Tests je Gruppe unter dem gespiegelten Test-Package (`rules/tests.md`):
`KXUnitTest`, `KXUnitSystemTest`, `KXPrefixTest`, `KXOperatorTest`. Jedes Public-API-Member braucht
eine eigene Assertion — auch jeder einzelne Prefix-Builder und jedes Enum-Entry.

Doku je Gruppe: eine MkDocs-Seite im Field-Ordner mit Typ-Angabe (**native**/**constructed**),
mindestens einem realen Rechenbeispiel und allen Dekompositionen (typisierte Operatorform *und*
native Form über `toX()`). Erst Englisch, dann die fünf Übersetzungen über den Translator-Agent.
Danach `docs/mkdocs.yml` (Nav in allen sechs Sprachblöcken), `README.md` (Gruppen-Tabelle) und
`CHANGELOG.md`.

## Phase 1 — Neues Fachgebiet Optik (`optic` / `optics`)

Schließt die SI-Basis ab. Solid Angle liegt bereits als `mechanic.solidangle` (`angle²`) vor und wird
als Dimensionsträger wiederverwendet.

* `optic.luminousintensity` — Candela (`cd`), **native unit**, 7. SI-Basiseinheit; Tokens `candelas`,
  historisch `hefnerCandles`, `candlepower`
* `optic.luminousflux` — Lumen (`lm`), Normalform `luminousIntensity¹·angle²`;
  Operatoren `luminousIntensity * solidAngle`, Inverse
* `optic.illuminance` — Lux (`lx`), `luminousIntensity·angle²·length⁻²`;
  `luminousFlux / area`, `illuminance * area = luminousFlux`; Tokens `lux`, `footCandles`, `phots`
* `optic.luminance` — `cd/m²` (Nit), `luminousIntensity·length⁻²`; Tokens `nits`, `stilbs`,
  `footLamberts`, `apostilbs`
* `optic.luminousenergy` — Lumen-Sekunde (`lm·s`, Talbot); `luminousFlux * time`
* `optic.luminousexposure` — Lux-Sekunde (`lx·s`); `illuminance * time`
* `optic.efficacy` — `lm/W`; `luminousFlux / power`; Konstante `MAX_LUMINOUS_EFFICACY` (683 lm/W)
* `optic.radiantintensity` — `W/sr`; `power / solidAngle`
* `optic.radiance` — `W/(sr·m²)`; `radiantIntensity / area`
* `common.reciprocallength` — `m⁻¹`, neutraler Gruppenname, weil er **zwei** Fachgebiete bedient:
  Dioptrie (Optik) und Wellenzahl (Mechanik/Spektroskopie). Laut `rules/package.md` gehört er damit
  nach `common`. Zwei Doku-Seiten (`optics/dioptre.md`, `mechanics/wavenumber.md`), die sich
  gegenseitig referenzieren; Operator `1 / length`, `length⁻¹`-Normalform

Neuer Field-Ordner `docs/docs/units/optics/` mit eigener `overview.md` (inkl. Notation-Tabelle
`Mathematics | Kotlin | Meaning` und Rechenbeispiel), in allen sechs Sprachen.

## Phase 2 — Chemie

* `thermo.concentration` — `mol/m³`, Tokens `molesPerCubicMeter`, `molesPerLiter` (Molarität, `M`);
  `amountOfSubstance / volume`, `concentration * volume = amountOfSubstance`
* `thermo.molality` — `mol/kg`; `amountOfSubstance / mass`; Kehrwert-Beziehung zur Molmasse
* `thermo.catalyticactivity` — Katal (`kat` = mol/s), Token `katals`, `enzymeUnits` (U = µmol/min);
  `amountOfSubstance / time`
* `electric.molarconductivity` — `S·m²/mol`; `conductivity / concentration`

## Phase 3 — Thermodynamik (enthält den Breaking Change)

1. **Umbenennung** (per `git mv`): `thermo/resistance` → `thermo/insulance`,
   `KThermalResistanceUnit*` → `KThermalInsulanceUnit*` (Basis bleibt `m²·K/W`). Alle Referenzen
   anpassen — insbesondere `thermo/heattransfercoefficient/KHeatTransferCoefficientUnitOperators.kt`
   und `KChemicalElement.kt`. Doku: `thermal-resistance.md` → `thermal-insulance.md` in allen sechs
   Sprachen, Nav und README nachziehen, CHANGELOG als **Breaking Change** ausweisen.
2. `thermo.resistance` **neu** — absoluter Wärmewiderstand `K/W`
   (`mass⁻¹·length⁻²·time³·temperature`); `temperatureDifference / power`,
   `thermalResistance * power = temperatureDifference`
3. `thermo.conductance` — Wärmeleitwert `W/K`; Kehrwert von (2),
   `power / temperatureDifference`
4. `thermo.volumetricheatcapacity` — `J/(m³·K)`
   (`mass·length⁻¹·time⁻²·temperature⁻¹`); `heatCapacity / volume`,
   `specificHeatCapacity * density`
5. `thermo.doserate` — `Gy/s` (`length²·time⁻³`); `specificEnergy / time`

## Phase 4 — Mechanik & Kinematik

* `kinematic.jerk` — `m/s³`; `acceleration / time`, `jerk * time = acceleration`
* `mechanic.specificweight` — `N/m³` (`mass·length⁻²·time⁻²`); `force / volume`,
  `density * acceleration`
* `mechanic.compressibility` — `1/Pa` (`mass⁻¹·length·time²`); Kehrwert des Elastizitätsmoduls,
  `1 / pressure`
* `mechanic.acousticimpedance` — `Pa·s/m` (`mass·length⁻²·time⁻¹`); `pressure / speed`,
  `density * speed`
* `kinematic.distance`: neue Exponenten-Spezialisierung `KSecondMomentOfAreaUnitInstance` (`m⁴`)
  neben `KAreaUnitInstance`/`KVolumeUnitInstance` — analog `KAreaUnitInstance.kt` aufgebaut, mit
  `KSecondMomentOfAreaUnitBareValues.kt`/`-Extensions.kt`, Operatoren `area * area`,
  `volume * length`, `secondMomentOfArea / area = area`; Doku
  `mechanics/second-moment-of-area.md`

## Phase 5 — Elektrotechnik

* `electric.magneticmoment` — `A·m²` (`current·length²`); `current * area`
* `electric.flux` — elektrischer Fluss `V·m` (`mass·length³·time⁻³·current⁻¹`);
  `electricFieldStrength * area`
* `electric.elastance` — Daraf `1/F`; Kehrwert der Kapazität, `voltage / charge`
* `electric.specificcharge` — `C/kg` (`current·time·mass⁻¹`); `charge / mass`; Konstante
  `ELECTRON_SPECIFIC_CHARGE`. Deckt zugleich die **Ionendosis/Exposition** ab

## Phase 6 — Strahlungs-Doku (keine neuen Typen)

Reine MkDocs-Seiten im Muster von `entropy.md`, je mit Abschnitt „warum kein eigener Typ", Beispiel
und Querverweis — in allen sechs Sprachen, plus Nav-Einträge:

* `kinematics/activity.md` — Becquerel als Lesart von `kinematic.frequency`
* `thermodynamics/absorbed-dose.md` — Gray als Lesart von `thermo.specificenergy`
* `thermodynamics/dose-equivalent.md` — Sievert, ebenfalls `thermo.specificenergy`
* `thermodynamics/exposure.md` — Ionendosis als Lesart von `electric.specificcharge`
* Dosisleistung wird von der in Phase 3 angelegten Gruppe `thermo.doserate` bedient

## Phase 7 — Regelwerk: Übersicht aller existierenden Einheiten

Damit die Frage „Welche Einheiten existieren bereits?" künftig ohne Codebase-Scan beantwortbar ist
(das Scannen von Projektcode ist laut CLAUDE.md ohnehin untersagt), wird die Antwort im Regelwerk
verankert und in einen Skill gekapselt. Beides auf **Englisch**, im Stil der bestehenden Dateien.

* Neue Regel `.claude/rules/unit-overview.md` — benennt `README.md`, Abschnitt
  „What does the framework currently support? → Unit Groups", als die **einzige** maßgebliche
  Übersicht aller existierenden Unit-Gruppen (Gruppe, Sub-Package, Basiseinheit). Hält fest, dass
  die Tabelle bei jeder neuen Gruppe mitzupflegen ist und dass Fragen nach dem Bestand aus ihr
  beantwortet werden, nicht durch Durchsuchen von `src/`.
* Neuer Skill `.claude/skills/unit-overview.md` mit Frontmatter
  (`name: unit-overview`, `description: Look up which units already exist in the framework`) —
  kapselt den Verweis auf die README-Tabelle und beschreibt, wie die Frage beantwortet wird:
  Tabelle lesen, Gruppe/Sub-Package/Basiseinheit zurückgeben, bei Nichtvorhandensein auf
  `create-unit` verweisen.
* Neuer Agent `.claude/agents/unit-overview.md` im Stil von `raw-unit-explore.md` — Frontmatter
  `name: overview-units`, `description: Determine which units already exist in the framework`,
  `model: opus`, `effort: low`, `tools: [Read, Glob, Grep]`, `skills: [unit-overview]`. Rolle: Die
  README-Tabelle lesen und den Bestand als Liste (Gruppe, Sub-Package, Basiseinheit) zurückgeben —
  bei einer konkreten Anfrage gefiltert, sonst vollständig; ausdrücklich **ohne** Scan von `src/`,
  und mit dem Hinweis auf `create-unit`, falls die gesuchte Einheit fehlt.
* `.claude/CLAUDE.md` — die neue Regel unter den bestehenden Verweisen eintragen.
* `README.md` — der Abschnitt „What does the framework currently support?" verweist derzeit auf eine
  `STATUS.md`, die es im Repository nicht gibt; dieser Verweis wird durch den Eigenverweis auf die
  Unit-Groups-Tabelle ersetzt, damit die Regel auf ein tatsächlich existierendes Ziel zeigt.

Diese Phase ist unabhängig von den Phasen 1–6 und kann zuerst umgesetzt werden — danach dient die
README-Tabelle in jeder Folgephase als Pflege-Anker.

## Verifikation

* `./gradlew test` nach jeder Phase — 100 % Coverage; zusätzlich manuell prüfen, dass jeder neue
  Prefix-Builder, jedes Enum-Entry und jedes Bare-Value-Token eine echte Assertion hat (Top-Level-
  `val`s laufen im `<clinit>` und werden sonst fälschlich als „covered" gemeldet)
* Pro Gruppe ein Test, der alle Dekompositionen gegeneinander prüft: gleicher Typ, wertgleiches
  Ergebnis
* Pro Gruppe ein Test, der `toX()` aus einem **äquivalent umgeformten** nativen Ausdruck aufruft
  (nicht nur aus der bereits kanonischen Form)
* Rundlauf `n of token` → `into token` für jede Einheit, inkl. der nicht-SI-Tokens
  (`footCandles`, `enzymeUnits`, …)
* `mkdocs build` im Ordner `docs/` ohne Nav-Warnungen; alle sechs Sprachen vorhanden
* Nach Phase 3 gezielt prüfen, dass `KChemicalElement` und die
  `heattransfercoefficient`-Operatoren nach der Umbenennung weiter kompilieren und ihre Tests grün
  sind
* Phase 7: Skill und Agent werden gegen die Frage „Which units already exist?" gegengelesen — sie
  müssen ohne Codebase-Scan auf die README-Tabelle führen; der Agent wird zusätzlich mit einer
  gefilterten Anfrage (z. B. „does a torque unit exist?") probeweise ausgeführt. Die Tabelle muss
  nach jeder Phase alle neu hinzugekommenen Gruppen enthalten

## Ablauf

Sieben Phasen, jede für sich lauffähig und mit eigenem Commit auf `claude/missing-units-ls7evz`.
Phase 7 (Regelwerk) zuerst, danach 1–6.
Entwicklungsstatus wird nach jeder abgeschlossenen Aufgabe in
`.claude/plans/<name>_status.md` fortgeschrieben.
