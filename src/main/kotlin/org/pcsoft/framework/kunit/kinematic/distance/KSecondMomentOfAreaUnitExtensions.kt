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

package org.pcsoft.framework.kunit.kinematic.distance

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 second moment of area templates. Use with `of`/`into`,
// e.g. `1940 of milli.quarticMeters`.

private fun prefixedSecondMomentOfArea(
    builder: KPrefixBuilder,
    quarticMeters: Double
): KSecondMomentOfAreaUnitInstance = secondMomentOfAreaOf(builder.prefix.factor * quarticMeters)

/** Prefixed quartic meters, e.g. `milli.quarticMeters`. */
val KPrefixBuilder.quarticMeters: KSecondMomentOfAreaUnitInstance
    get() = prefixedSecondMomentOfArea(this, 1.0)

/** Prefixed quartic centimeters, e.g. `kilo.quarticCentimeters`. */
val KPrefixBuilder.quarticCentimeters: KSecondMomentOfAreaUnitInstance
    get() = prefixedSecondMomentOfArea(this, 1.0e-8)

/** Prefixed quartic millimeters, e.g. `kilo.quarticMillimeters`. */
val KPrefixBuilder.quarticMillimeters: KSecondMomentOfAreaUnitInstance
    get() = prefixedSecondMomentOfArea(this, 1.0e-12)

/** Prefixed quartic inches, e.g. `kilo.quarticInches`. */
val KPrefixBuilder.quarticInches: KSecondMomentOfAreaUnitInstance
    get() = prefixedSecondMomentOfArea(this, 4.162314256e-7)
