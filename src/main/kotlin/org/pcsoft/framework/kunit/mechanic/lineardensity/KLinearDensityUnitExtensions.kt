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

package org.pcsoft.framework.kunit.mechanic.lineardensity

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 linear-density templates: one property per named unit on the prefix builder. Linear
// density accepts *any* magnitude, so the properties hang on the common base [KPrefixBuilder]. Use with
// `of`/`into`, e.g. `5 of milli.gramsPerMeter`, `rho into deci.tex` (dtex).

private fun prefixedLinearDensity(builder: KPrefixBuilder, unit: KLinearDensityUnit): KLinearDensityUnitInstance =
    linearDensityInstanceOf(builder.prefix.factor * unit.baseValue * KGM_IN_BASE)

/** Prefixed kg/m, e.g. `milli.kilogramsPerMeter`. */
val KPrefixBuilder.kilogramsPerMeter: KLinearDensityUnitInstance
    get() = prefixedLinearDensity(this, KLinearDensityUnit.KILOGRAMS_PER_METER)

/** Prefixed g/m, e.g. `milli.gramsPerMeter` (mg/m). */
val KPrefixBuilder.gramsPerMeter: KLinearDensityUnitInstance
    get() = prefixedLinearDensity(this, KLinearDensityUnit.GRAMS_PER_METER)

/** Prefixed g/cm, e.g. `milli.gramsPerCentimeter`. */
val KPrefixBuilder.gramsPerCentimeter: KLinearDensityUnitInstance
    get() = prefixedLinearDensity(this, KLinearDensityUnit.GRAMS_PER_CENTIMETER)

/** Prefixed tex, e.g. `deci.tex` (dtex), `kilo.tex` (ktex). */
val KPrefixBuilder.tex: KLinearDensityUnitInstance
    get() = prefixedLinearDensity(this, KLinearDensityUnit.TEX)

/** Prefixed denier, e.g. `deci.denier`. */
val KPrefixBuilder.denier: KLinearDensityUnitInstance
    get() = prefixedLinearDensity(this, KLinearDensityUnit.DENIER)

/** Prefixed lb/ft, e.g. `kilo.poundsPerFoot`. */
val KPrefixBuilder.poundsPerFoot: KLinearDensityUnitInstance
    get() = prefixedLinearDensity(this, KLinearDensityUnit.POUNDS_PER_FOOT)
