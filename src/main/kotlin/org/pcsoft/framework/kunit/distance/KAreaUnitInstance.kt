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
 * An **area**: the exponent-2 leaf of the distance group, a single [KDistanceUnit.BASE] term at
 * exponent 2 (square meters). Like [KLengthUnitInstance] it is its own type, so `area + area` /
 * comparisons only accept another area (`area + length` does not compile), while `*`/`/` are strongly
 * typed (e.g. `area / length = length`, `area * length = volume`).
 *
 * Instances are created via the area creator extension properties in `KAreaUnitExtensions.kt`
 * (e.g. `200.squareMeters`, `5.hectares`), the SI-prefix `infix` constructors in
 * `KDistanceUnitPrefix.kt` (e.g. `5 kilo squareMeters`), or as the result of `length * length`.
 *
 * Example:
 * ```kotlin
 * val a = 200.meters * 50.meters // 10000 m²
 * a.valueAs(KDistanceDerivedUnit.HECTARE) // 1.0
 * (a / 100.meters).valueAs(KDistanceUnit.METER) // 100.0 (an area divided by a length is a length)
 * ```
 */
class KAreaUnitInstance internal constructor(instance: KMixedUnitInstance) :
    KDistanceUnitInstance(instance), KUnitInstance<KAreaUnitInstance> {

    /** Adds two areas (both normalized to square meters). Only another [KAreaUnitInstance] is accepted. */
    override operator fun plus(other: KAreaUnitInstance): KAreaUnitInstance = areaOf(value + other.value)

    /** Subtracts two areas. See [plus]. */
    override operator fun minus(other: KAreaUnitInstance): KAreaUnitInstance = areaOf(value - other.value)

    /** Compares two areas by their normalized [value] (square meters). */
    override operator fun compareTo(other: KAreaUnitInstance): Int = value.compareTo(other.value)

    /** `area * length = volume` (m²·m = m³). */
    operator fun times(other: KLengthUnitInstance): KVolumeUnitInstance = volumeOf(value * other.value)

    /** `area * area = m⁴`, hence the general [KDistanceUnitInstance]. */
    operator fun times(other: KAreaUnitInstance): KDistanceUnitInstance = distanceOf(value * other.value, 4)

    /** `area * volume = m⁵`, hence the general [KDistanceUnitInstance]. */
    operator fun times(other: KVolumeUnitInstance): KDistanceUnitInstance = distanceOf(value * other.value, 5)

    /** `area / length = length` (m²/m = m). */
    operator fun div(other: KLengthUnitInstance): KLengthUnitInstance = lengthOf(value / other.value)

    /** `area / area` is dimensionless (exponent 0), hence a raw [KMixedUnitInstance]. */
    operator fun div(other: KAreaUnitInstance): KMixedUnitInstance = instance / other.instance

    /** `area / volume = m⁻¹`, hence the general [KDistanceUnitInstance]. */
    operator fun div(other: KVolumeUnitInstance): KDistanceUnitInstance = distanceOf(value / other.value, -1)
}
