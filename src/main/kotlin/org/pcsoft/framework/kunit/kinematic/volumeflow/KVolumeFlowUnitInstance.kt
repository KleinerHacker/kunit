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

package org.pcsoft.framework.kunit.kinematic.volumeflow

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **volumetric flow rate**, i.e. exactly two terms in the
 * canonical normal form `distance³ · time⁻¹` (`m³·s⁻¹`). The [value] is always normalized internally to
 * cubic meters per second ([KVolumeFlowUnit.BASE]), regardless of which [KVolumeFlowUnit], SI prefix, or
 * volume/time combination it was constructed from.
 *
 * Volumetric flow is a *constructed* unit group. Instances are created via the bare tokens in
 * `KVolumeFlowUnitBareValues.kt` (e.g. `5 of litersPerSecond`), the prefixed templates in
 * `KVolumeFlowUnitExtensions.kt` (e.g. `2 of milli.cubicMetersPerSecond`), the typed `volume / time`
 * operator in `KVolumeFlowUnitOperators.kt`, or [toVolumeFlow] on a canonical `distance³·time⁻¹`
 * expression.
 *
 * Example:
 * ```kotlin
 * val q = (600 of liters) / (2 of minutes) // KVolumeFlowUnitInstance, 0.005 m³/s
 * q into litersPerMinute                    // 300.0
 * ```
 */
class KVolumeFlowUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KVolumeFlowUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new volumetric flow value with [value] (m³/s) scaled by [factor]. Backs number-times-unit
     * construction (`10 of litersPerSecond`).
     */
    override fun scaledBy(factor: Double): KVolumeFlowUnitInstance = volumeFlowInstanceOf(value * factor)

    /**
     * Adds two volumetric flows, automatically converting between different [KVolumeFlowUnit]s since both
     * operands are always normalized to [KVolumeFlowUnit.BASE] (m³/s) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of litersPerSecond) + (500 of litersPerMinute)) into litersPerSecond // ≈ 9.333
     * ```
     */
    override operator fun plus(other: KVolumeFlowUnitInstance): KVolumeFlowUnitInstance =
        volumeFlowInstanceOf(value + other.value)

    /** Subtracts two volumetric flows. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KVolumeFlowUnitInstance): KVolumeFlowUnitInstance =
        volumeFlowInstanceOf(value - other.value)

    /** Multiplies two volumetric flows, producing a new [KMixedUnitInstance] (no longer a "pure" flow). */
    operator fun times(other: KVolumeFlowUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two volumetric flows, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KVolumeFlowUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two volumetric flows by their normalized [value] (m³/s). */
    override operator fun compareTo(other: KVolumeFlowUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two flows are equal iff they represent the same
     * volumetric flow (e.g. `(1 of litersPerSecond) == (60 of litersPerMinute)`).
     */
    override fun equals(other: Any?): Boolean = other is KVolumeFlowUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"0.005 m³/s"`. */
    override fun toString(): String = "$value ${KVolumeFlowUnit.BASE.symbol}"
}

// --- Factory helpers (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KVolumeFlowUnitInstance] from a value already expressed in cubic meters per second
 * ([KVolumeFlowUnit.BASE]).
 *
 * This is the single creation source that every volumetric flow decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the two terms `distance³` and `time⁻¹`
 * (each in its group's base unit).
 */
internal fun volumeFlowInstanceOf(cubicMetersPerSecond: Double): KVolumeFlowUnitInstance =
    KVolumeFlowUnitInstance(
        KMixedUnitInstance(
            cubicMetersPerSecond,
            listOf(
                KUnitTerm(KDistanceUnit.BASE, 3),
                KUnitTerm(KTimeUnit.BASE, -1),
            ),
        ),
    )

/** Builds a value-1 [KVolumeFlowUnitInstance] for the given [unit] (its [KVolumeFlowUnit.baseValue] m³/s). */
internal fun volumeFlowOfUnit(unit: KVolumeFlowUnit): KVolumeFlowUnitInstance = volumeFlowInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" volumetric flow value, as long as it matches the canonical
 * volumetric flow normal form: exactly one [KDistanceUnit] term at exponent `+3` and one [KTimeUnit] term
 * at exponent `-1` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the result is expressed in cubic meters per second
 * regardless of which concrete distance/time units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `distance³·time⁻¹` volumetric flow.
 *
 * Example:
 * ```kotlin
 * val raw = ((2 of meters) pow 3).toUnit() / (4 of seconds).toUnit()
 * raw.toVolumeFlow() into cubicMetersPerSecond // 2.0
 * ```
 */
fun KMixedUnitInstance.toVolumeFlow(): KVolumeFlowUnitInstance {
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 3 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -1 }
    check(units.size == 2 && distanceTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure volumetric flow (expected KDistanceUnit^3 and KTimeUnit^-1)"
    }
    val normalized = value *
        Math.pow(distanceTerm.unit.baseValue, 3.0) *
        Math.pow(timeTerm.unit.baseValue, -1.0)
    return volumeFlowInstanceOf(normalized)
}
