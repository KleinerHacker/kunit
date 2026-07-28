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

package org.pcsoft.framework.kunit.electricdipolemoment

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 electric dipole moment templates: one property per unit on the prefix builder (e.g.
// `pico.coulombMeters` = 1e-12 C·m). Electric dipole moment accepts *any* magnitude, so the properties hang
// on the common base [KPrefixBuilder]. Use with `of`/`into`, e.g. `2 of pico.coulombMeters`.

private fun prefixedElectricDipoleMoment(
    builder: KPrefixBuilder,
    unit: KElectricDipoleMomentUnit,
): KElectricDipoleMomentUnitInstance = electricDipoleMomentInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed coulomb meters, e.g. `pico.coulombMeters` = 1e-12 C·m. */
val KPrefixBuilder.coulombMeters: KElectricDipoleMomentUnitInstance
    get() = prefixedElectricDipoleMoment(this, KElectricDipoleMomentUnit.COULOMB_METER)

/** Prefixed debyes, e.g. `milli.debyes` = 3.335 640 952e-33 C·m. */
val KPrefixBuilder.debyes: KElectricDipoleMomentUnitInstance
    get() = prefixedElectricDipoleMoment(this, KElectricDipoleMomentUnit.DEBYE)
