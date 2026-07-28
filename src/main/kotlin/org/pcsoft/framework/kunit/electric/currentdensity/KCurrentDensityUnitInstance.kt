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

package org.pcsoft.framework.kunit.electric.currentdensity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **current density**, i.e. exactly two terms in the canonical
 * normal form `current¹ · distance⁻²` (`A·m⁻²`). Since both components are stored in their group base units
 * (ampere, meter), the [value] is directly the reading in amperes per square meter
 * ([KCurrentDensityUnit.BASE]).
 *
 * Current density has no bare tokens and no prefix builders (every spelling is a ratio); it is produced by
 * the typed `current / area` operator in `KCurrentDensityUnitOperators.kt`, built as an expression
 * (`amperes / (milli.meters pow 2)`), or via [toCurrentDensity], and read back with `into` against such an
 * expression.
 *
 * Example:
 * ```kotlin
 * val j = (10 of amperes) / (2 of (meters pow 2)) // KCurrentDensityUnitInstance, 5 A/m²
 * j into (amperes / (meters pow 2))               // 5.0
 * ```
 */
class KCurrentDensityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KCurrentDensityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new current density value with [value] (A/m²) scaled by [factor]. Backs number-times-unit
     * construction (`3 of (amperes / (meters pow 2))`).
     */
    override fun scaledBy(factor: Double): KCurrentDensityUnitInstance = currentDensityInstanceOf(value * factor)

    /**
     * Adds two current densities, automatically converting between the constructing units since both
     * operands are always normalized to [KCurrentDensityUnit.BASE] (A/m²) internally.
     */
    override operator fun plus(other: KCurrentDensityUnitInstance): KCurrentDensityUnitInstance =
        currentDensityInstanceOf(value + other.value)

    /** Subtracts two current densities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KCurrentDensityUnitInstance): KCurrentDensityUnitInstance =
        currentDensityInstanceOf(value - other.value)

    /**
     * Multiplies two current densities, producing a new [KMixedUnitInstance] (no longer a "pure" current
     * density).
     */
    operator fun times(other: KCurrentDensityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two current densities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KCurrentDensityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two current densities by their normalized [value] (A/m²). */
    override operator fun compareTo(other: KCurrentDensityUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two current densities are equal iff they represent the same
     * physical quantity, independent of the units they were constructed with.
     */
    override fun equals(other: Any?): Boolean = other is KCurrentDensityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in A/m², e.g. `"5.0 A/m²"`. */
    override fun toString(): String = "$value ${KCurrentDensityUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KCurrentDensityUnitInstance] from a value already expressed in amperes per square meter
 * ([KCurrentDensityUnit.BASE]).
 *
 * This is the single creation source that every current density decomposition must funnel into: it assembles
 * the canonical normal-form [KMixedUnitInstance] with the two terms `current¹` and `distance⁻²` (each in its
 * group's base unit).
 */
internal fun currentDensityInstanceOf(amperesPerSquareMeter: Double): KCurrentDensityUnitInstance =
    KCurrentDensityUnitInstance(
        KMixedUnitInstance(
            amperesPerSquareMeter,
            listOf(
                KUnitTerm(KElectricCurrentUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, -2),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" current density value, as long as it matches the canonical current
 * density normal form: exactly one [KElectricCurrentUnit] term at exponent `+1` and one [KDistanceUnit] term
 * at exponent `-2` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the result is expressed in A/m² regardless of the
 * concrete units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `current¹·distance⁻²` current density.
 *
 * Example:
 * ```kotlin
 * val raw = 5 of (amperes / (meters pow 2))
 * raw.toCurrentDensity().value // 5.0
 * ```
 */
fun KMixedUnitInstance.toCurrentDensity(): KCurrentDensityUnitInstance {
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -2 }
    check(units.size == 2 && currentTerm != null && distanceTerm != null) {
        "KMixedUnitInstance $this does not represent a pure current density " +
            "(expected KElectricCurrentUnit^1 and KDistanceUnit^-2)"
    }
    val normalized = value * currentTerm.unit.baseValue * Math.pow(distanceTerm.unit.baseValue, -2.0)
    return currentDensityInstanceOf(normalized)
}
