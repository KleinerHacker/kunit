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

package org.pcsoft.framework.kunit.mechanic.lineforce

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 force-per-length templates: one property per named unit on the prefix builder. This is
// how the millinewton per meter (`milli.newtonsPerMeter`), the everyday surface-tension spelling, and the
// kilonewton per meter (`kilo.newtonsPerMeter`) are expressed. Use with `of`/`into`.

private fun prefixedLineForce(builder: KPrefixBuilder, unit: KLineForceUnit): KLineForceUnitInstance =
    lineForceInstanceOf(builder.prefix.factor * unit.baseValue * NM_IN_BASE)

/** Prefixed N/m, e.g. `milli.newtonsPerMeter` (mN/m), `kilo.newtonsPerMeter` (kN/m). */
val KPrefixBuilder.newtonsPerMeter: KLineForceUnitInstance
    get() = prefixedLineForce(
        this,
        KLineForceUnit.NEWTONS_PER_METER
    )

/** Prefixed N/mm, e.g. `kilo.newtonsPerMillimeter`. */
val KPrefixBuilder.newtonsPerMillimeter: KLineForceUnitInstance
    get() = prefixedLineForce(this, KLineForceUnit.NEWTONS_PER_MILLIMETER)

/** Prefixed dyn/cm, e.g. `milli.dynesPerCentimeter`. */
val KPrefixBuilder.dynesPerCentimeter: KLineForceUnitInstance
    get() = prefixedLineForce(this, KLineForceUnit.DYNES_PER_CENTIMETER)

/** Prefixed lbf/in, e.g. `kilo.poundsForcePerInch`. */
val KPrefixBuilder.poundsForcePerInch: KLineForceUnitInstance
    get() = prefixedLineForce(this, KLineForceUnit.POUNDS_FORCE_PER_INCH)

/** Prefixed kp/m, e.g. `kilo.kilopondsPerMeter`. */
val KPrefixBuilder.kilopondsPerMeter: KLineForceUnitInstance
    get() = prefixedLineForce(this, KLineForceUnit.KILOPONDS_PER_METER)
