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

import org.pcsoft.framework.kunit.mechanic.angle.KAngleUnitInstance
import org.pcsoft.framework.kunit.mechanic.angle.angleOf

// Cross-group operators that let plane angles combine *directly* into a strongly typed solid angle, and
// back. They live in the solid-angle package because it may depend on the angle group (the reverse must
// never happen). Both sides are already normalized to their group base (radian / steradian), so the typed
// operators compute on the normalized values and funnel into the group's single factory.

/**
 * Multiplies two plane angles into a solid angle (`angle * angle = solidangle`, since `1 sr = 1 rad²`).
 *
 * This is the typed decomposition; the equivalent native form `(a.toUnit() pow 2)` reduces onto the same
 * normal form via [toSolidAngle].
 *
 * Example:
 * ```kotlin
 * val omega = (90 of degrees) * (90 of degrees) // KSolidAngleUnitInstance, ≈ 2.4674 sr
 * ```
 */
operator fun KAngleUnitInstance.times(other: KAngleUnitInstance): KSolidAngleUnitInstance =
    solidAngleOf(value * other.value)

/**
 * Divides a solid angle by a plane angle to obtain the remaining plane angle
 * (`solidangle / angle = angle`).
 */
operator fun KSolidAngleUnitInstance.div(other: KAngleUnitInstance): KAngleUnitInstance =
    angleOf(value / other.value)
