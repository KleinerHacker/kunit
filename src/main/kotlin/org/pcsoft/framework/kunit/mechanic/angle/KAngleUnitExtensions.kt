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

package org.pcsoft.framework.kunit.mechanic.angle

import org.pcsoft.framework.kunit.KPrefixBuilder
import org.pcsoft.framework.kunit.KUnitDisplay

// Prefixed, value-1 angle templates: one property per named angle unit on the prefix builder (e.g.
// `milli.radians` = 0.001 rad). An angle accepts *any* magnitude, so the properties hang on the common
// base [KPrefixBuilder]. Use with `of`/`into`, e.g. `5 of milli.radians`, `a into micro.arcseconds`.

private fun prefixedAngle(builder: KPrefixBuilder, unit: KAngleUnit): KAngleUnitInstance =
    angleOf(builder.prefix.factor * unit.baseValue, KUnitDisplay(unit, builder.prefix.symbol))

/** Prefixed radians, e.g. `milli.radians` (mrad), `micro.radians` (µrad). */
val KPrefixBuilder.radians: KAngleUnitInstance get() = prefixedAngle(this, KAngleUnit.RADIAN)

/** Prefixed degrees, e.g. `milli.degrees` (m°). */
val KPrefixBuilder.degrees: KAngleUnitInstance get() = prefixedAngle(this, KAngleUnit.DEGREE)

/** Prefixed arcminutes, e.g. `milli.arcminutes`. */
val KPrefixBuilder.arcminutes: KAngleUnitInstance get() = prefixedAngle(this, KAngleUnit.ARCMINUTE)

/** Prefixed arcseconds, e.g. `milli.arcseconds` (mas), `micro.arcseconds` (µas). */
val KPrefixBuilder.arcseconds: KAngleUnitInstance get() = prefixedAngle(this, KAngleUnit.ARCSECOND)

/** Prefixed gradians, e.g. `milli.gradians`. */
val KPrefixBuilder.gradians: KAngleUnitInstance get() = prefixedAngle(this, KAngleUnit.GRADIAN)

/** Prefixed turns, e.g. `kilo.turns`, `milli.turns`. */
val KPrefixBuilder.turns: KAngleUnitInstance get() = prefixedAngle(this, KAngleUnit.TURN)
