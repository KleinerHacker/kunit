# Entwicklungsstatus: Fehlende Einheiten nachrüsten

Plan: `.claude/plans/missing-units.md`
Branch: `claude/missing-units-ls7evz`

## Übersicht

| Phase | Inhalt                                      |      Status |
|-------|---------------------------------------------|------------:|
| 7     | Regelwerk: Übersicht aller Einheiten        | **erledigt** |
| 1     | Neues Fachgebiet Optik (`optic` / `optics`) |       offen |
| 2     | Chemie                                      |       offen |
| 3     | Thermodynamik (inkl. Breaking Change)       |       offen |
| 4     | Mechanik & Kinematik                        |       offen |
| 5     | Elektrotechnik                              |       offen |
| 6     | Strahlungs-Doku                             |       offen |

## Phase 7 — Regelwerk (erledigt)

| Aufgabe                                       |       Status |
|-----------------------------------------------|-------------:|
| `.claude/rules/unit-overview.md` anlegen       | erledigt     |
| `.claude/skills/unit-overview.md` anlegen      | erledigt     |
| `.claude/agents/unit-overview.md` anlegen      | erledigt     |
| `.claude/CLAUDE.md` um Regelverweis ergänzen   | erledigt     |
| `README.md`: STATUS.md-Verweis ersetzt         | erledigt     |
| Agent-Probelauf                                | manuell geprüft |

Anmerkung zum Probelauf: Der Agent `overview-units` konnte in der laufenden Session nicht
aufgerufen werden, weil Agent-Definitionen beim Session-Start geladen werden. Die beiden Probefälle
wurden stattdessen manuell gegen die Quellen geprüft:

* `torque` — keine eigene Zeile in der Unit-Groups-Tabelle; wird über die im Agent verankerte
  Fallback-Regel (mkdocs-Nav prüfen) als `common.energy` aufgelöst
* `candela` — in `README.md` und `docs/mkdocs.yml` nicht vorhanden, wird korrekt als fehlend
  gemeldet (Verweis auf `create-unit`)

Kein CHANGELOG-Eintrag: reine Tooling-/Regelwerksänderung ohne externe Wirkung.

## Nächster Schritt

Phase 1 — neues Fachgebiet Optik: `optic.luminousintensity` (Candela, native unit, 7. SI-Basiseinheit)
als erste Gruppe anlegen.
