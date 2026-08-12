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

package org.pcsoft.framework.kunit.mechanic.lineardensity

import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.toLength
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.toMass

// Cross-group operators that let mass and length combine *directly* into a strongly typed linear density,
// and back. They live in the linear-density package because it may depend on mass/distance (the reverse
// must never happen), and each delegates to the generic KMixedUnitInstance `*`/`/` engine.

/**
 * Divides a mass by a length to obtain the linear density (`mass / length = lineardensity`).
 *
 * Example:
 * ```kotlin
 * val rho = (2 of kilo.grams) / (4 of meters) // 0.5 kg/m
 * ```
 */
operator fun KMassUnitInstance.div(other: KLengthUnitInstance): KLinearDensityUnitInstance =
    (this.toUnit() / other.toUnit()).toLinearDensity()

/** Multiplies a linear density by a length to obtain the mass (`lineardensity * length = mass`). */
operator fun KLinearDensityUnitInstance.times(other: KLengthUnitInstance): KMassUnitInstance =
    (this.toUnit() * other.toUnit()).toMass()

/**
 * Multiplies a length by a linear density to obtain the mass (`length * lineardensity = mass`); the
 * commutative counterpart of [KLinearDensityUnitInstance.times].
 */
operator fun KLengthUnitInstance.times(other: KLinearDensityUnitInstance): KMassUnitInstance =
    (this.toUnit() * other.toUnit()).toMass()

/** Divides a mass by a linear density to obtain the length (`mass / lineardensity = length`). */
operator fun KMassUnitInstance.div(other: KLinearDensityUnitInstance): KLengthUnitInstance =
    (this.toUnit() / other.toUnit()).toLength()
