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

package org.pcsoft.framework.kunit.mechanic.solidangle

import org.pcsoft.framework.kunit.KUnitDisplay

// Bare, value-1 solid-angle tokens (each = 1 unit, normalized to steradians). Vocabulary for building
// (`2 of steradians`) and reading (`omega into squareDegrees`). Prefixed forms live in
// KSolidAngleUnitExtensions.kt.

private fun bareSolidAngle(unit: KSolidAngleUnit): KSolidAngleUnitInstance =
    solidAngleOf(unit.baseValue, KUnitDisplay(unit))

/** 1 steradian ([KSolidAngleUnit.STERADIAN]), the group's base unit. */
val steradians: KSolidAngleUnitInstance = bareSolidAngle(KSolidAngleUnit.STERADIAN)

/** 1 square degree ([KSolidAngleUnit.SQUARE_DEGREE], ≈ 3.0462e-4 sr). */
val squareDegrees: KSolidAngleUnitInstance = bareSolidAngle(KSolidAngleUnit.SQUARE_DEGREE)

/** 1 spat ([KSolidAngleUnit.SPAT], the full sphere = 4π sr). */
val spats: KSolidAngleUnitInstance = bareSolidAngle(KSolidAngleUnit.SPAT)
