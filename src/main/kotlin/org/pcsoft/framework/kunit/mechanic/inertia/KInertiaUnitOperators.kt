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

package org.pcsoft.framework.kunit.mechanic.inertia

import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.toArea
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.toMass

// Cross-group operators that let mass and area combine *directly* into a strongly typed moment of
// inertia, and back. They live in the inertia package because it may depend on mass/distance (the reverse
// must never happen). The equivalent native form `mass * length * length` reduces onto the same normal
// form and is converted through [toInertia].

/**
 * Multiplies a mass by an area to obtain a moment of inertia (`mass * area = inertia`).
 *
 * Example:
 * ```kotlin
 * val j = (2 of kilo.grams) * ((3 of meters) * (3 of meters)) // 18 kg·m²
 * ```
 */
operator fun KMassUnitInstance.times(other: KAreaUnitInstance): KInertiaUnitInstance =
    (this.toUnit() * other.toUnit()).toInertia()

/**
 * Multiplies an area by a mass to obtain a moment of inertia (`area * mass = inertia`); the commutative
 * counterpart of [KMassUnitInstance.times].
 */
operator fun KAreaUnitInstance.times(other: KMassUnitInstance): KInertiaUnitInstance =
    (this.toUnit() * other.toUnit()).toInertia()

/** Divides a moment of inertia by its mass to obtain the squared radius of gyration (`inertia / mass = area`). */
operator fun KInertiaUnitInstance.div(other: KMassUnitInstance): KAreaUnitInstance =
    (this.toUnit() / other.toUnit()).toArea()

/** Divides a moment of inertia by the area to obtain the mass (`inertia / area = mass`). */
operator fun KInertiaUnitInstance.div(other: KAreaUnitInstance): KMassUnitInstance =
    (this.toUnit() / other.toUnit()).toMass()
