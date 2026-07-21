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

package org.pcsoft.framework.kunit.force

// Value-1 force templates for the genuinely named units of the group, used with `of`/`into`
// (`10 of newtons`, `f into ponds`). Prefixed magnitudes (kilonewton, kilopond = kgf) are reached via
// the prefix builders in `KForceUnitExtensions.kt` (`kilo.newtons`, `kilo.ponds`) and therefore have no
// own tokens here.

/** 1 newton ([KForceUnit.NEWTON]). */
val newtons: KForceUnitInstance = forceOfUnit(KForceUnit.NEWTON)

/** 1 dyne ([KForceUnit.DYNE], 10⁻⁵ N). */
val dynes: KForceUnitInstance = forceOfUnit(KForceUnit.DYNE)

/** 1 pound-force ([KForceUnit.POUND_FORCE], 4.448 N). */
val poundsForce: KForceUnitInstance = forceOfUnit(KForceUnit.POUND_FORCE)

/** 1 pond / gram-force ([KForceUnit.POND], 9.806 65 mN); the kilopond (kgf) is `kilo.ponds`. */
val ponds: KForceUnitInstance = forceOfUnit(KForceUnit.POND)
