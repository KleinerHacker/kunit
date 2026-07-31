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

package org.pcsoft.framework.kunit.mechanic.angularacceleration

// Value-1 angular-acceleration templates, used with `of`/`into` (`5 of radiansPerSecondSquared`,
// `alpha into revolutionsPerMinutePerSecond`). An angular acceleration is normally *computed*
// (`angularvelocity / time`, `angle / (seconds pow 2)`); these tokens name the conventional readings.
// Prefixes are applied to the components (`kilo.radians / (seconds pow 2)`), so this group has no own
// prefix builders.

/** 1 rad/s² ([KAngularAccelerationUnit.RADIANS_PER_SECOND_SQUARED]), the group's base unit. */
val radiansPerSecondSquared: KAngularAccelerationUnitInstance =
    angularAccelerationOfUnit(KAngularAccelerationUnit.RADIANS_PER_SECOND_SQUARED)

/** 1 °/s² ([KAngularAccelerationUnit.DEGREES_PER_SECOND_SQUARED], π/180 rad/s²). */
val degreesPerSecondSquared: KAngularAccelerationUnitInstance =
    angularAccelerationOfUnit(KAngularAccelerationUnit.DEGREES_PER_SECOND_SQUARED)

/** 1 rps² ([KAngularAccelerationUnit.REVOLUTIONS_PER_SECOND_SQUARED], 2π rad/s²). */
val revolutionsPerSecondSquared: KAngularAccelerationUnitInstance =
    angularAccelerationOfUnit(KAngularAccelerationUnit.REVOLUTIONS_PER_SECOND_SQUARED)

/** 1 rpm/s ([KAngularAccelerationUnit.REVOLUTIONS_PER_MINUTE_PER_SECOND], 2π/60 rad/s²). */
val revolutionsPerMinutePerSecond: KAngularAccelerationUnitInstance =
    angularAccelerationOfUnit(KAngularAccelerationUnit.REVOLUTIONS_PER_MINUTE_PER_SECOND)
