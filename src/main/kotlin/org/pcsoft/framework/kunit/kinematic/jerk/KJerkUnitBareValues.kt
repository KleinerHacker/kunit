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

// Value-1 jerk templates for the named units of the group, used with `of`/`into`
// (`0.5 of metersPerSecondCubed`). Prefixed forms live in KJerkUnitExtensions.kt.

/** 1 m/s³ ([KJerkUnit.METER_PER_SECOND_CUBED]), the group's base unit. */
val metersPerSecondCubed: KJerkUnitInstance = jerkOfUnit(KJerkUnit.METER_PER_SECOND_CUBED)

/** 1 g/s ([KJerkUnit.STANDARD_GRAVITY_PER_SECOND], 9.80665 m/s³). */
val standardGravitiesPerSecond: KJerkUnitInstance = jerkOfUnit(KJerkUnit.STANDARD_GRAVITY_PER_SECOND)

/** 1 ft/s³ ([KJerkUnit.FOOT_PER_SECOND_CUBED], 0.3048 m/s³). */
val feetPerSecondCubed: KJerkUnitInstance = jerkOfUnit(KJerkUnit.FOOT_PER_SECOND_CUBED)
