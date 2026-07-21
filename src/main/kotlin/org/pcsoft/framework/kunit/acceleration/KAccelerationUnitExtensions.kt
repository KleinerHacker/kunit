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

package org.pcsoft.framework.kunit.acceleration

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 acceleration templates for the genuinely named units of the group. The Gal accepts
// any magnitude, so its property hangs on the common base [KPrefixBuilder] (e.g. `milli.gals` = 1 mGal,
// the everyday gravimetry unit). Use with `of`/`into`, e.g. `5 of milli.gals`.

private fun prefixedAcceleration(builder: KPrefixBuilder, unit: KAccelerationUnit): KAccelerationUnitInstance =
    accelerationUnitInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed gals, e.g. `milli.gals` = 1 mGal (0.000 01 m/s²). */
val KPrefixBuilder.gals: KAccelerationUnitInstance get() = prefixedAcceleration(this, KAccelerationUnit.GAL)

/** Prefixed standard gravities, e.g. `milli.standardGravities`. */
val KPrefixBuilder.standardGravities: KAccelerationUnitInstance get() = prefixedAcceleration(this, KAccelerationUnit.STANDARD_GRAVITY)
