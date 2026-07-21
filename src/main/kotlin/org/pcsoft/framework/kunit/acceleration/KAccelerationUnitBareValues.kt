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

// An acceleration is normally built as a length-per-time² expression, e.g. `10 of meters / (seconds pow 2)`
// or as the result of `speed / time`. There are therefore deliberately **no** spelled-out composite tokens
// (`metersPerSecondSquared` = plain `meters / (seconds pow 2)` and thus redundant).
//
// Only accelerations with a genuinely single, conventional name and their own factor survive as value-1
// tokens, used with `of`/`into` (`50 of gals`, `a into standardGravities`).

/** 1 Gal ([KAccelerationUnit.GAL], 1 cm/s²). */
val gals: KAccelerationUnitInstance = accelerationUnitInstanceOf(KAccelerationUnit.GAL.baseValue)

/** 1 standard gravity ([KAccelerationUnit.STANDARD_GRAVITY], 9.806 65 m/s²). */
val standardGravities: KAccelerationUnitInstance = accelerationUnitInstanceOf(KAccelerationUnit.STANDARD_GRAVITY.baseValue)
