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
 * A **volume**: the exponent-3 leaf of the distance group, a single [KDistanceUnit.BASE] term at
 * exponent 3 (cubic meters). Like the other leaves it is its own type, so `volume + volume` /
 * comparisons only accept another volume, while `*`/`/` are strongly typed (e.g. `volume / area =
 * length`, `volume / length = area`).
 *
 * Instances are created via the volume creator extension properties in `KDistanceUnitExtensions.kt`
 * (e.g. `2.cubicMeters`, `5.liters`), the SI-prefix `infix` constructors in `KDistanceUnitPrefix.kt`
 * (e.g. `5 kilo cubicMeters`), or as the result of `length * length * length` / `area * length`.
 *
 * Example:
 * ```kotlin
 * val v = 2.meters * 2.meters * 2.meters // 8 m³
 * v.valueAs(KDistanceDerivedUnit.LITER)  // 8000.0
 * (v / (2.meters * 2.meters)).valueAs(KDistanceUnit.METER) // 2.0 (volume / area = length)
 * ```
 */
class KVolumeUnitInstance internal constructor(instance: KMixedUnitInstance) :
    KDistanceUnitInstance(instance), KUnitInstance<KVolumeUnitInstance> {

    /** Adds two volumes (both normalized to cubic meters). Only another [KVolumeUnitInstance] is accepted. */
    override operator fun plus(other: KVolumeUnitInstance): KVolumeUnitInstance = volumeOf(value + other.value)

    /** Subtracts two volumes. See [plus]. */
    override operator fun minus(other: KVolumeUnitInstance): KVolumeUnitInstance = volumeOf(value - other.value)

    /** Compares two volumes by their normalized [value] (cubic meters). */
    override operator fun compareTo(other: KVolumeUnitInstance): Int = value.compareTo(other.value)

    /** `volume * length = m⁴`, hence the general [KDistanceUnitInstance]. */
    operator fun times(other: KLengthUnitInstance): KDistanceUnitInstance = distanceOf(value * other.value, 4)

    /** `volume * area = m⁵`, hence the general [KDistanceUnitInstance]. */
    operator fun times(other: KAreaUnitInstance): KDistanceUnitInstance = distanceOf(value * other.value, 5)

    /** `volume * volume = m⁶`, hence the general [KDistanceUnitInstance]. */
    operator fun times(other: KVolumeUnitInstance): KDistanceUnitInstance = distanceOf(value * other.value, 6)

    /** `volume / length = area` (m³/m = m²). */
    operator fun div(other: KLengthUnitInstance): KAreaUnitInstance = areaOf(value / other.value)

    /** `volume / area = length` (m³/m² = m). */
    operator fun div(other: KAreaUnitInstance): KLengthUnitInstance = lengthOf(value / other.value)

    /** `volume / volume` is dimensionless (exponent 0), hence a raw [KMixedUnitInstance]. */
    operator fun div(other: KVolumeUnitInstance): KMixedUnitInstance = instance / other.instance
}
