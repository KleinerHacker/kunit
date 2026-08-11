# Entwicklungsstatus: Fehlende Einheiten nachrüsten

Plan: `.claude/plans/missing-units.md`
Branch: `claude/missing-units-ls7evz`

## Übersicht

| Phase | Inhalt                                      |       Status |
|-------|---------------------------------------------|-------------:|
| 7     | Regelwerk: Übersicht aller Einheiten        | **erledigt** |
| 1     | Neues Fachgebiet Optik (`optic` / `optics`) | **erledigt** |
| 2     | Chemie                                      | **erledigt** |
| 3     | Thermodynamik (inkl. Breaking Change)       | **erledigt** |
| 4     | Mechanik & Kinematik                        |    in Arbeit |
| 5     | Elektrotechnik                              |        offen |
| 6     | Strahlungs-Doku                             |        offen |

## Phase 7 — Regelwerk (erledigt)

Regel `.claude/rules/unit-overview.md`, Skill `.claude/skills/unit-overview.md`, Agent
`.claude/agents/unit-overview.md` (`overview-units`), CLAUDE.md-Verweis, README-Anker statt
STATUS.md-Link. Agent-Probelauf grün (torque → `common.energy`, candela → fehlt).

## Phase 1 — Optik (erledigt)

Zehn neue Gruppen, alle mit Code, vier Testklassen und englischer MkDocs-Seite:

| Gruppe                       | Package                        | Basiseinheit  |
|------------------------------|--------------------------------|---------------|
| Luminous Intensity (native)  | `optic.luminousintensity`      | `cd`          |
| Luminous Flux                | `optic.luminousflux`           | `lm`          |
| Illuminance                  | `optic.illuminance`            | `lx`          |
| Luminance                    | `optic.luminance`              | `cd/m²`       |
| Luminous Energy              | `optic.luminousenergy`         | `lm·s`        |
| Luminous Exposure            | `optic.luminousexposure`       | `lx·s`        |
| Luminous Efficacy            | `optic.efficacy`               | `lm/W`        |
| Radiant Intensity            | `optic.radiantintensity`       | `W/sr`        |
| Radiance                     | `optic.radiance`               | `W/(sr·m²)`   |
| Reciprocal Length            | `common.reciprocallength`      | `1/m`         |

Damit deckt KUnit alle sieben SI-Basiseinheiten ab. Doku: `docs/docs/units/optics/` (11 Seiten inkl.
`overview.md`) plus `docs/docs/units/mechanics/wavenumber.md`; Nav, README-Tabelle und CHANGELOG
nachgezogen.

Volle Testsuite grün, keine Regression.

## Phase 2 — Chemie (erledigt)

| Gruppe             | Package                        | Basiseinheit |
|--------------------|--------------------------------|--------------|
| Concentration      | `thermo.concentration`         | `mol/m³`     |
| Molality           | `thermo.molality`              | `mol/kg`     |
| Catalytic Activity | `thermo.catalyticactivity`     | `kat`        |
| Molar Conductivity | `electric.molarconductivity`   | `S·m²/mol`   |

## Phase 3 — Thermodynamik (erledigt)

Breaking Change: `thermo.resistance` → `thermo.insulance` (`KThermalResistanceUnit*` →
`KThermalInsulanceUnit*`), weil die Gruppe mit `m²·K/W` den R-Wert abbildet. Der Name
`thermo.resistance` gehört jetzt dem absoluten Wärmewiderstand. Werte und Tokens unverändert.

| Gruppe                   | Package                          | Basiseinheit |
|--------------------------|----------------------------------|--------------|
| Thermal Resistance (neu) | `thermo.resistance`              | `K/W`        |
| Thermal Conductance      | `thermo.conductance`             | `W/K`        |
| Volumetric Heat Capacity | `thermo.volumetricheatcapacity`  | `J/(m³·K)`   |
| Dose Rate                | `thermo.doserate`                | `Gy/s`       |

## Hinweis zur lokalen Verifikation

Das Projekt fordert `jvmToolchain(25)`, der Container hat nur JDK 21 und der Toolchain-Download ist
durch den Proxy blockiert. Tests laufen daher lokal über ein Init-Script, das das Toolchain auf 21
setzt (`-I <scratchpad>/jdk21.init.gradle`). Das Repository bleibt unverändert.

## Nächster Schritt

Phase 4 — Mechanik & Kinematik: `kinematic.jerk` (m/s³) als erste Gruppe, danach
`mechanic.specificweight`, `mechanic.compressibility`, `mechanic.acousticimpedance` und die
Exponenten-Spezialisierung `KSecondMomentOfAreaUnitInstance` (m⁴) in `kinematic.distance`.
