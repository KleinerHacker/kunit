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

package org.pcsoft.framework.kunit.kinematic.jerk

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 jerk templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `500 of milli.metersPerSecondCubed`.

private fun prefixedJerk(builder: KPrefixBuilder, unit: KJerkUnit): KJerkUnitInstance =
    jerkInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed meters per second cubed, e.g. `milli.metersPerSecondCubed`. */
val KPrefixBuilder.metersPerSecondCubed: KJerkUnitInstance
    get() = prefixedJerk(this, KJerkUnit.METER_PER_SECOND_CUBED)

/** Prefixed standard gravities per second, e.g. `milli.standardGravitiesPerSecond`. */
val KPrefixBuilder.standardGravitiesPerSecond: KJerkUnitInstance
    get() = prefixedJerk(this, KJerkUnit.STANDARD_GRAVITY_PER_SECOND)

/** Prefixed feet per second cubed, e.g. `milli.feetPerSecondCubed`. */
val KPrefixBuilder.feetPerSecondCubed: KJerkUnitInstance
    get() = prefixedJerk(this, KJerkUnit.FOOT_PER_SECOND_CUBED)
