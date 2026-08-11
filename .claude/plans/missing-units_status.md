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
| Agent-Probelauf                                | erledigt     |

Probelauf des Agents `overview-units`, beide Fälle korrekt beantwortet:

* `torque` — keine eigene Zeile in der Unit-Groups-Tabelle; über die im Agent verankerte
  Fallback-Regel als `common.energy` aufgelöst, inkl. Doku-Seite und Torque-Operatoren
* `candela` — nicht vorhanden, korrekt als fehlend gemeldet mit Verweis auf `create-unit` und dem
  Hinweis, dass dafür eine neue Basisdimension eingeführt werden muss

Kein CHANGELOG-Eintrag: reine Tooling-/Regelwerksänderung ohne externe Wirkung.

## Nächster Schritt

Phase 1 — neues Fachgebiet Optik: `optic.luminousintensity` (Candela, native unit, 7. SI-Basiseinheit)
als erste Gruppe anlegen.
