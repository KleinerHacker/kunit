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

package org.pcsoft.framework.kunit.electric.chargedensity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **(volume) charge density**, i.e. exactly three terms in the
 * canonical normal form `current¹ · time¹ · distance⁻³` (`A·s·m⁻³`). Since all three components are stored
 * in their group base units (ampere, second, meter), the [value] is directly the reading in coulombs per
 * cubic meter ([KChargeDensityUnit.BASE]).
 *
 * Charge density has no bare tokens and no prefix builders (every spelling is a ratio); it is produced by
 * the typed `charge / volume` operator in `KChargeDensityUnitOperators.kt`, built as an expression
 * (`coulombs / (meters pow 3)`), or via [toChargeDensity], and read back with `into` against such an
 * expression.
 *
 * Example:
 * ```kotlin
 * val rho = (6 of coulombs) / (2 of (meters pow 3)) // KChargeDensityUnitInstance, 3 C/m³
 * rho into (coulombs / (meters pow 3))              // 3.0
 * ```
 */
class KChargeDensityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KChargeDensityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new charge density value with [value] (C/m³) scaled by [factor]. Backs number-times-unit
     * construction (`3 of (coulombs / (meters pow 3))`).
     */
    override fun scaledBy(factor: Double): KChargeDensityUnitInstance = chargeDensityInstanceOf(value * factor)

    /**
     * Adds two charge densities, automatically converting between the constructing units since both operands
     * are always normalized to [KChargeDensityUnit.BASE] (C/m³) internally.
     */
    override operator fun plus(other: KChargeDensityUnitInstance): KChargeDensityUnitInstance =
        chargeDensityInstanceOf(value + other.value)

    /** Subtracts two charge densities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KChargeDensityUnitInstance): KChargeDensityUnitInstance =
        chargeDensityInstanceOf(value - other.value)

    /** Multiplies two charge densities, producing a new [KMixedUnitInstance] (no longer a "pure" charge density). */
    operator fun times(other: KChargeDensityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two charge densities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KChargeDensityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two charge densities by their normalized [value] (C/m³). */
    override operator fun compareTo(other: KChargeDensityUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two charge densities are equal iff they represent the same
     * physical quantity, independent of the units they were constructed with.
     */
    override fun equals(other: Any?): Boolean = other is KChargeDensityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in C/m³, e.g. `"3.0 C/m³"`. */
    override fun toString(): String = "$value ${KChargeDensityUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KChargeDensityUnitInstance] from a value already expressed in coulombs per cubic meter
 * ([KChargeDensityUnit.BASE]).
 *
 * This is the single creation source that every charge density decomposition must funnel into: it assembles
 * the canonical normal-form [KMixedUnitInstance] with the three terms `current¹`, `time¹` and `distance⁻³`
 * (each in its group's base unit).
 */
internal fun chargeDensityInstanceOf(coulombsPerCubicMeter: Double): KChargeDensityUnitInstance =
    KChargeDensityUnitInstance(
        KMixedUnitInstance(
            coulombsPerCubicMeter,
            listOf(
                KUnitTerm(KElectricCurrentUnit.BASE, 1),
                KUnitTerm(KTimeUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, -3),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" charge density value, as long as it matches the canonical charge
 * density normal form: exactly one [KElectricCurrentUnit] term at exponent `+1`, one [KTimeUnit] term at
 * exponent `+1` and one [KDistanceUnit] term at exponent `-3` (order independent). The terms are normalized
 * over their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the result is expressed in C/m³ regardless of
 * the concrete units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `current¹·time¹·distance⁻³` charge density.
 *
 * Example:
 * ```kotlin
 * val raw = 3 of (amperes * seconds / (meters pow 3))
 * raw.toChargeDensity().value // 3.0
 * ```
 */
fun KMixedUnitInstance.toChargeDensity(): KChargeDensityUnitInstance {
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -3 }
    check(units.size == 3 && currentTerm != null && timeTerm != null && distanceTerm != null) {
        "KMixedUnitInstance $this does not represent a pure charge density " +
            "(expected KElectricCurrentUnit^1, KTimeUnit^1 and KDistanceUnit^-3)"
    }
    val normalized = value * currentTerm.unit.baseValue * timeTerm.unit.baseValue *
        Math.pow(distanceTerm.unit.baseValue, -3.0)
    return chargeDensityInstanceOf(normalized)
}
