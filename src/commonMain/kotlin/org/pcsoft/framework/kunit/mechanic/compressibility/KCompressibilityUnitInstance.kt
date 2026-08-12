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

package org.pcsoft.framework.kunit.mechanic.compressibility

import kotlin.math.pow
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * The fixed factor between the group's named base unit (reciprocal pascal) and the raw component storage
 * (`g⁻¹·m·s²`): 1 Pa⁻¹ = 1 (m·s²)/kg = 0.001 (m·s²)/g. It exists because the mass component of this
 * library is normalized to grams, not kilograms. The [KCompressibilityUnitInstance.value] is always the
 * raw component value; readings in reciprocal pascals divide by this factor.
 */
internal const val RECIPROCAL_PA_IN_BASE: Double = 1.0e-3

/**
 * Wraps a [KMixedUnitInstance] representing a **compressibility** (relative volume change per pressure),
 * i.e. exactly three terms - [KMassUnit.BASE] (gram) at exponent `-1`, [KDistanceUnit.BASE] (meter) at
 * exponent `+1` and [KTimeUnit.BASE] (second) at exponent `+2` (`g⁻¹·m·s²`). The [value] is the raw
 * component value; readings in reciprocal pascals ([KCompressibilityUnit.BASE]) divide by
 * [RECIPROCAL_PA_IN_BASE] - the same convention the neighbouring force, pressure and density groups use.
 *
 * Compressibility is the reciprocal of the bulk modulus (an elastic modulus, i.e. a
 * [pressure][org.pcsoft.framework.kunit.mechanic.pressure.KPressureUnitInstance]), which is why
 * `1 / pressure` yields a compressibility and `1 / compressibility` a pressure.
 *
 * Instances are created via the bare tokens in `KCompressibilityUnitBareValues.kt` (e.g.
 * `4.6e-10 of reciprocalPascals`), the prefixed templates in `KCompressibilityUnitExtensions.kt`, the
 * operators in `KCompressibilityUnitOperators.kt`, or [toCompressibility].
 *
 * Example:
 * ```kotlin
 * val kappa = 1 / (2.2 of giga.pascals) // water: ≈ 4.5e-10 Pa⁻¹
 * kappa into reciprocalPascals
 * ```
 */
class KCompressibilityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KCompressibilityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new compressibility with the component [value] scaled by [factor]. Backs number-times-unit
     * construction (`4.6e-10 of reciprocalPascals`).
     */
    override fun scaledBy(factor: Double): KCompressibilityUnitInstance =
        compressibilityInstanceOf(value * factor)

    /**
     * Adds two compressibilities, automatically converting between different [KCompressibilityUnit]s since
     * both operands are always normalized to the same component base internally.
     */
    override operator fun plus(other: KCompressibilityUnitInstance): KCompressibilityUnitInstance =
        compressibilityInstanceOf(value + other.value)

    /** Subtracts two compressibilities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KCompressibilityUnitInstance): KCompressibilityUnitInstance =
        compressibilityInstanceOf(value - other.value)

    /** Multiplies two compressibilities, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KCompressibilityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two compressibilities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KCompressibilityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two compressibilities by their normalized component [value]. */
    override operator fun compareTo(other: KCompressibilityUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized component [value]: two compressibilities are equal iff they
     * represent the same quantity (e.g. `(1 of reciprocalBars) == (1e-5 of reciprocalPascals)`).
     */
    override fun equals(other: Any?): Boolean =
        other is KCompressibilityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in 1/Pa, e.g. `"4.5E-10 1/Pa"`. */
    override fun toString(): String =
        "${renderDouble(value / RECIPROCAL_PA_IN_BASE)} ${KCompressibilityUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KCompressibilityUnitInstance] from a raw component value (`g⁻¹·m·s²`). To build from a
 * reciprocal-pascal reading, use [compressibilityOfUnit] or the tokens in
 * `KCompressibilityUnitBareValues.kt`.
 *
 * This is the single creation source that every compressibility decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the three terms `mass⁻¹`, `distance¹` and
 * `time²` (each in its group's base unit).
 */
internal fun compressibilityInstanceOf(componentValue: Double): KCompressibilityUnitInstance =
    KCompressibilityUnitInstance(
        KMixedUnitInstance(
            componentValue,
            listOf(
                KUnitTerm(KMassUnit.BASE, -1),
                KUnitTerm(KDistanceUnit.BASE, 1),
                KUnitTerm(KTimeUnit.BASE, 2),
            ),
        ),
    )

/**
 * Builds a value-1 [KCompressibilityUnitInstance] for the given [unit] (its
 * [KCompressibilityUnit.baseValue] reciprocal pascals).
 */
internal fun compressibilityOfUnit(unit: KCompressibilityUnit): KCompressibilityUnitInstance =
    compressibilityInstanceOf(unit.baseValue * RECIPROCAL_PA_IN_BASE)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" compressibility, as long as it matches the canonical
 * compressibility normal form: exactly one [KMassUnit] term at exponent `-1`, one [KDistanceUnit] term at
 * exponent `+1` and one [KTimeUnit] term at exponent `+2` (order independent). The terms are normalized
 * over their [org.pcsoft.framework.kunit.KUnit.baseValue]s.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass⁻¹·distance·time²`
 * compressibility.
 */
fun KMixedUnitInstance.toCompressibility(): KCompressibilityUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == -1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 2 }
    check(units.size == 3 && massTerm != null && distanceTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure compressibility " +
                "(expected one KMassUnit^-1, one KDistanceUnit^1 and one KTimeUnit^2 term)"
    }
    val component = value *
            massTerm.unit.baseValue.pow(-1.0) *
            distanceTerm.unit.baseValue *
            timeTerm.unit.baseValue.pow(2.0)
    return compressibilityInstanceOf(component)
}
