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

package org.pcsoft.framework.kunit.thermo.temperaturegradient

import kotlin.math.pow
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **temperature gradient** (temperature change per unit of
 * length), i.e. exactly two terms in the canonical normal form `temperature¹ · distance⁻¹` (`K·m⁻¹`). The
 * [value] is always normalized internally to kelvin per meter ([KTemperatureGradientUnit.BASE]).
 *
 * The temperature dimension is the **difference** group ([KTemperatureDifferenceUnit]) - a gradient is a
 * change per length, never an absolute reading, so the affine absolute scale would be wrong here.
 *
 * Instances are created via the bare tokens in `KTemperatureGradientUnitBareValues.kt`, the prefixed
 * templates in `KTemperatureGradientUnitExtensions.kt`, the typed `temperatureDifference / length`
 * operator in `KTemperatureGradientUnitOperators.kt`, or [toTemperatureGradient].
 *
 * Example:
 * ```kotlin
 * val g = KTemperatureDifference.ofKelvin(25) / (1 of kilo.meters) // 25 K/km
 * ```
 */
class KTemperatureGradientUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KTemperatureGradientUnitInstance>, KUnitMeasurable by instance {

    /** Returns a new temperature gradient with [value] (K/m) scaled by [factor]. */
    override fun scaledBy(factor: Double): KTemperatureGradientUnitInstance =
        temperatureGradientInstanceOf(value * factor)

    /**
     * Adds two temperature gradients, automatically converting between different
     * [KTemperatureGradientUnit]s since both operands are always normalized to K/m internally.
     */
    override operator fun plus(other: KTemperatureGradientUnitInstance): KTemperatureGradientUnitInstance =
        temperatureGradientInstanceOf(value + other.value)

    /** Subtracts two temperature gradients. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KTemperatureGradientUnitInstance): KTemperatureGradientUnitInstance =
        temperatureGradientInstanceOf(value - other.value)

    /** Multiplies two temperature gradients, producing a new [KMixedUnitInstance]. */
    operator fun times(other: KTemperatureGradientUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two temperature gradients, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KTemperatureGradientUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two temperature gradients by their normalized [value] (K/m). */
    override operator fun compareTo(other: KTemperatureGradientUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized [value] (K/m). */
    override fun equals(other: Any?): Boolean = other is KTemperatureGradientUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"0.025 K/m"`. */
    override fun toString(): String = "${renderDouble(value)} ${KTemperatureGradientUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KTemperatureGradientUnitInstance] from a value already expressed in K/m
 * ([KTemperatureGradientUnit.BASE]).
 *
 * This is the single creation source that every decomposition must funnel into: it assembles the canonical
 * normal-form [KMixedUnitInstance] with the two terms `temperature¹` and `distance⁻¹`.
 */
internal fun temperatureGradientInstanceOf(kelvinPerMeter: Double): KTemperatureGradientUnitInstance =
    KTemperatureGradientUnitInstance(
        KMixedUnitInstance(
            kelvinPerMeter,
            listOf(
                KUnitTerm(KTemperatureDifferenceUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, -1),
            ),
        ),
    )

/** Builds a value-1 [KTemperatureGradientUnitInstance] for the given [unit]. */
internal fun temperatureGradientOfUnit(unit: KTemperatureGradientUnit): KTemperatureGradientUnitInstance =
    temperatureGradientInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" temperature gradient, as long as it matches the canonical normal
 * form: exactly one [KTemperatureDifferenceUnit] term at exponent `+1` and one [KDistanceUnit] term at
 * `-1` (order independent).
 *
 * @throws IllegalStateException if this instance is not a canonical `temperature¹·distance⁻¹` gradient.
 */
fun KMixedUnitInstance.toTemperatureGradient(): KTemperatureGradientUnitInstance {
    val temperatureTerm = units.singleOrNull { it.unit is KTemperatureDifferenceUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -1 }
    check(units.size == 2 && temperatureTerm != null && distanceTerm != null) {
        "KMixedUnitInstance $this does not represent a pure temperature gradient " +
                "(expected KTemperatureDifferenceUnit^1 and KDistanceUnit^-1)"
    }
    val normalized = value *
            temperatureTerm.unit.baseValue *
            distanceTerm.unit.baseValue.pow(-1.0)
    return temperatureGradientInstanceOf(normalized)
}
