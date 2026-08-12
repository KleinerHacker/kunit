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

package org.pcsoft.framework.kunit.kinematic.distance

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitDisplay
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm

/**
 * A **second moment of area** (area moment of inertia, `I`): the exponent-4 leaf of the distance group, a
 * single [KDistanceUnit.BASE] term at exponent 4 (`m⁴`).
 *
 * It is the geometric property that decides how stiff a beam cross-section is in bending - the `I` in
 * `EI` - and is quoted in `cm⁴` for steel profiles and in `mm⁴` for small sections. Do not confuse it with
 * the *mass* [moment of inertia][org.pcsoft.framework.kunit.mechanic.inertia.KInertiaUnitInstance]
 * (`kg·m²`), which is a different quantity with a different dimension.
 *
 * Like the other leaf types it is additive only within its own dimension (`secondMomentOfArea + area` does
 * not compile), while `*`/`/` are strongly typed (e.g. `secondMomentOfArea / area = area`).
 *
 * Instances are created via `area * area`, `volume * length`, `length * volume`, the power operation on a
 * length (`(1 of meters).toUnit() pow 4` narrowed with [toSecondMomentOfArea]), or the bare tokens in
 * `KSecondMomentOfAreaUnitBareValues.kt`.
 *
 * Example:
 * ```kotlin
 * val i = (1 of centi.meters) * (1 of centi.meters) * (1 of centi.meters) * (1 of centi.meters)
 * i into quarticCentimeters // 1.0
 * ```
 */
class KSecondMomentOfAreaUnitInstance internal constructor(instance: KMixedUnitInstance) :
    KDistanceUnitInstance(instance), KUnitInstance<KSecondMomentOfAreaUnitInstance> {

    /**
     * Adds two second moments of area (both normalized to `m⁴`). Only another
     * [KSecondMomentOfAreaUnitInstance] is accepted - this is how the contributions of the parts of a
     * built-up cross-section combine.
     */
    override operator fun plus(other: KSecondMomentOfAreaUnitInstance): KSecondMomentOfAreaUnitInstance =
        secondMomentOfAreaOf(value + other.value)

    /** Subtracts two second moments of area - e.g. a hollow section minus its bore. See [plus]. */
    override operator fun minus(other: KSecondMomentOfAreaUnitInstance): KSecondMomentOfAreaUnitInstance =
        secondMomentOfAreaOf(value - other.value)

    /** Compares two second moments of area by their normalized [value] (`m⁴`). */
    override operator fun compareTo(other: KSecondMomentOfAreaUnitInstance): Int =
        value.compareTo(other.value)

    /** `secondMomentOfArea / length = volume` (m⁴/m = m³) - the section modulus. */
    operator fun div(other: KLengthUnitInstance): KVolumeUnitInstance = volumeOf(value / other.value)

    /** `secondMomentOfArea / area = area` (m⁴/m² = m²). */
    operator fun div(other: KAreaUnitInstance): KAreaUnitInstance = areaOf(value / other.value)

    /** `secondMomentOfArea / volume = length` (m⁴/m³ = m). */
    operator fun div(other: KVolumeUnitInstance): KLengthUnitInstance = lengthOf(value / other.value)

    /**
     * `secondMomentOfArea / secondMomentOfArea` is dimensionless (exponent 0), hence a raw
     * [KMixedUnitInstance].
     */
    operator fun div(other: KSecondMomentOfAreaUnitInstance): KMixedUnitInstance =
        instance / other.instance

    /** `secondMomentOfArea * length = m⁵`, hence the general [KDistanceUnitInstance]. */
    operator fun times(other: KLengthUnitInstance): KDistanceUnitInstance =
        distanceOf(value * other.value, 5)

    /** `secondMomentOfArea * area = m⁶`, hence the general [KDistanceUnitInstance]. */
    operator fun times(other: KAreaUnitInstance): KDistanceUnitInstance =
        distanceOf(value * other.value, 6)
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KSecondMomentOfAreaUnitInstance] (exponent 4) from a value already expressed in `m⁴`.
 */
internal fun secondMomentOfAreaOf(
    value: Double,
    display: KUnitDisplay? = null
): KSecondMomentOfAreaUnitInstance =
    KSecondMomentOfAreaUnitInstance(
        KMixedUnitInstance(value, listOf(KUnitTerm(KDistanceUnit.BASE, 4, display))),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a [KSecondMomentOfAreaUnitInstance], requiring it to be a single
 * [KDistanceUnit] term at **exponent 4**.
 *
 * @throws IllegalStateException if it is not a pure distance value, or its exponent is not 4.
 */
fun KMixedUnitInstance.toSecondMomentOfArea(): KSecondMomentOfAreaUnitInstance {
    val d = toDistance()
    check(d.exponent == 4) {
        "KMixedUnitInstance $this is not a second moment of area (expected exponent 4, was ${d.exponent})"
    }
    return d as KSecondMomentOfAreaUnitInstance
}
