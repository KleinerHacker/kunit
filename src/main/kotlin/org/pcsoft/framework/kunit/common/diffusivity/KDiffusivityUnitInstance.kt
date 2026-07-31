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

package org.pcsoft.framework.kunit.common.diffusivity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **thermal diffusivity** `α`, i.e. exactly two terms in the
 * canonical normal form `distance² · time⁻¹` (`m²·s⁻¹`). The [value] is always normalized internally to
 * m²/s ([KDiffusivityUnit.BASE]).
 *
 * Thermal diffusivity says how fast a temperature change propagates through a material - as opposed to the
 * thermal conductivity, which says how much heat flows in the steady state. It is defined as
 * `α = λ / (ρ · c_p)`; see `KDiffusivityUnitOperators.kt`.
 *
 * The dimension `m²/s` is shared with kinematic viscosity and mass diffusivity; this group models the
 * thermal quantity.
 *
 * Example:
 * ```kotlin
 * val copper = 111 of squareMillimetersPerSecond // ≈ 1.11e-4 m²/s
 * ```
 */
class KDiffusivityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KDiffusivityUnitInstance>, KUnitMeasurable by instance {

    /** Returns a new thermal diffusivity with [value] (m²/s) scaled by [factor]. */
    override fun scaledBy(factor: Double): KDiffusivityUnitInstance =
        diffusivityInstanceOf(value * factor)

    /**
     * Adds two thermal diffusivities, automatically converting between different
     * [KDiffusivityUnit]s since both operands are always normalized to m²/s internally.
     */
    override operator fun plus(other: KDiffusivityUnitInstance): KDiffusivityUnitInstance =
        diffusivityInstanceOf(value + other.value)

    /** Subtracts two thermal diffusivities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KDiffusivityUnitInstance): KDiffusivityUnitInstance =
        diffusivityInstanceOf(value - other.value)

    /** Multiplies two thermal diffusivities, producing a new [KMixedUnitInstance]. */
    operator fun times(other: KDiffusivityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two thermal diffusivities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KDiffusivityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two thermal diffusivities by their normalized [value] (m²/s). */
    override operator fun compareTo(other: KDiffusivityUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized [value] (m²/s). */
    override fun equals(other: Any?): Boolean = other is KDiffusivityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"1.11E-4 m²/s"`. */
    override fun toString(): String = "$value ${KDiffusivityUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KDiffusivityUnitInstance] from a value already expressed in m²/s
 * ([KDiffusivityUnit.BASE]).
 *
 * This is the single creation source that every decomposition must funnel into: it assembles the canonical
 * normal-form [KMixedUnitInstance] with the two terms `distance²` and `time⁻¹`.
 */
internal fun diffusivityInstanceOf(squareMetersPerSecond: Double): KDiffusivityUnitInstance =
    KDiffusivityUnitInstance(
        KMixedUnitInstance(
            squareMetersPerSecond,
            listOf(
                KUnitTerm(KDistanceUnit.BASE, 2),
                KUnitTerm(KTimeUnit.BASE, -1),
            ),
        ),
    )

/** Builds a value-1 [KDiffusivityUnitInstance] for the given [unit]. */
internal fun diffusivityOfUnit(unit: KDiffusivityUnit): KDiffusivityUnitInstance =
    diffusivityInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" thermal diffusivity, as long as it matches the canonical normal
 * form: exactly one [KDistanceUnit] term at exponent `+2` and one [KTimeUnit] term at `-1` (order
 * independent).
 *
 * @throws IllegalStateException if this instance is not a canonical `distance²·time⁻¹` thermal
 * diffusivity.
 */
fun KMixedUnitInstance.toDiffusivity(): KDiffusivityUnitInstance {
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -1 }
    check(units.size == 2 && distanceTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure thermal diffusivity " +
                "(expected KDistanceUnit^2 and KTimeUnit^-1)"
    }
    val normalized = value *
            Math.pow(distanceTerm.unit.baseValue, 2.0) *
            Math.pow(timeTerm.unit.baseValue, -1.0)
    return diffusivityInstanceOf(normalized)
}
