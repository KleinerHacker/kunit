/*
 * Copyright (c) KleinerHacker alias Pfeiffer C Soft 2026.
 * This work is licensed under the Apache License, Version 2.0.
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, this software is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations.
 */

package org.pcsoft.framework.kunit.conductance

// Bare, value-1 conductance tokens (each = 1 unit, normalized to siemens). Vocabulary for building
// (`10 of siemens`) and reading (`g into mhos`); combine with the prefix builders (`milli.siemens`).
// Prefixed forms live in KConductanceUnitExtensions.kt.

/**
 * 1 siemens ([KConductanceUnit.SIEMENS]), the base unit of the conductance group. The name is both
 * singular and plural, hence no trailing `s`.
 *
 * Not to be confused with `org.pcsoft.framework.kunit.resistance.siemensUnits`: that token lives in the
 * *resistance* package and denotes the historical **Siemens mercury unit** (a resistance of 0.9534 Ω),
 * whereas this token is the SI unit of **conductance**.
 */
val siemens: KConductanceUnitInstance = conductanceInstanceOf(KConductanceUnit.SIEMENS.baseValue)

/** 1 mho ([KConductanceUnit.MHO], the traditional name of the siemens, 1 S). */
val mhos: KConductanceUnitInstance = conductanceInstanceOf(KConductanceUnit.MHO.baseValue)

/** 1 abmho ([KConductanceUnit.ABMHO], CGS-EMU, 10⁹ S). */
val abmhos: KConductanceUnitInstance = conductanceInstanceOf(KConductanceUnit.ABMHO.baseValue)

/** 1 statmho ([KConductanceUnit.STATMHO], CGS-ESU, 1.112650e-12 S). */
val statmhos: KConductanceUnitInstance = conductanceInstanceOf(KConductanceUnit.STATMHO.baseValue)
