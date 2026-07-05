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

package org.pcsoft.framework.kunit.distance

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance

/**
 * A **length**: the exponent-1 leaf of the distance group, a single [KDistanceUnit.BASE] term at
 * exponent 1. Being its own type (distinct from the general [KDistanceUnitInstance]) is what gives the
 * compile-time guarantees:
 *
 * - `length + length`, `length - length`, and the comparison operators are typed to
 *   [KLengthUnitInstance] and therefore only accept another length - `length + area` does not compile.
 * - `length * length = area`, `length * area = volume`, `area / length = length`, etc. are strongly
 *   typed (see the [times]/[div] overloads), while a length combined with the general
 *   [KDistanceUnitInstance] or a raw [KMixedUnitInstance] falls back to a [KMixedUnitInstance].
 *
 * Instances are created via the creator extension properties in `KLengthUnitExtensions.kt`
 * (e.g. `5.meters`, `5.miles`) or the SI-prefix `infix` constructors in `KDistanceUnitPrefix.kt`
 * (e.g. `3 kilo meters`).
 *
 * Example:
 * ```kotlin
 * val d = 5.miles
 * d.value                       // 8046.72 (normalized to meters)
 * d.valueAs(KDistanceUnit.MILE) // 5.0
 * val area = 200.meters * 50.meters // KAreaUnitInstance, 10000 m²
 * ```
 */
class KLengthUnitInstance internal constructor(instance: KMixedUnitInstance) :
    KDistanceUnitInstance(instance), KUnitInstance<KLengthUnitInstance> {

    /**
     * Adds two lengths, automatically converting between different [KDistanceUnit]s since both operands
     * are always normalized to [KDistanceUnit.BASE] internally. Only another [KLengthUnitInstance] is
     * accepted - `length + area` is a compile error.
     *
     * Example:
     * ```kotlin
     * ((1 kilo meters) + 500.meters).value // 1500.0
     * ```
     */
    override operator fun plus(other: KLengthUnitInstance): KLengthUnitInstance = lengthOf(value + other.value)

    /** Subtracts two lengths. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KLengthUnitInstance): KLengthUnitInstance = lengthOf(value - other.value)

    /** Compares two lengths by their normalized [value] (meters). */
    override operator fun compareTo(other: KLengthUnitInstance): Int = value.compareTo(other.value)

    /** `length * length = area` (m·m = m²). */
    operator fun times(other: KLengthUnitInstance): KAreaUnitInstance = areaOf(value * other.value)

    /** `length * area = volume` (m·m² = m³). */
    operator fun times(other: KAreaUnitInstance): KVolumeUnitInstance = volumeOf(value * other.value)

    /** `length * volume = m⁴`, outside `{1,2,3}`, hence the general [KDistanceUnitInstance]. */
    operator fun times(other: KVolumeUnitInstance): KDistanceUnitInstance = distanceOf(value * other.value, 4)

    /** `length / length` is dimensionless (exponent 0), hence a raw [KMixedUnitInstance]. */
    operator fun div(other: KLengthUnitInstance): KMixedUnitInstance = instance / other.instance

    /** `length / area = m⁻¹`, hence the general [KDistanceUnitInstance]. */
    operator fun div(other: KAreaUnitInstance): KDistanceUnitInstance = distanceOf(value / other.value, -1)

    /** `length / volume = m⁻²`, hence the general [KDistanceUnitInstance]. */
    operator fun div(other: KVolumeUnitInstance): KDistanceUnitInstance = distanceOf(value / other.value, -2)
}
