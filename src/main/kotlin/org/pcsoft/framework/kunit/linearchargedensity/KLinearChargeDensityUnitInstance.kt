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

package org.pcsoft.framework.kunit.linearchargedensity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.ec.KElectricCurrentUnit
import org.pcsoft.framework.kunit.time.KTimeUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **linear charge density**, i.e. exactly three terms in the
 * canonical normal form `current¹ · time¹ · distance⁻¹` (`A·s·m⁻¹` = `C/m`). The [value] is always normalized
 * internally to coulombs per meter ([KLinearChargeDensityUnit.BASE]), regardless of which charge/length
 * combination it was constructed from.
 *
 * Linear charge density is a *constructed* unit group without named units of its own: instances are created
 * from an expression (`coulombs / meters`), through the typed operator (`charge / length`), or via
 * [toLinearChargeDensity] on a canonical `current·time·distance⁻¹` expression.
 *
 * Example:
 * ```kotlin
 * val lambda = (5 of micro.coulombs) / (2 of meters)
 * lambda.value // 2.5e-6 (normalized to coulombs per meter)
 * ```
 */
class KLinearChargeDensityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KLinearChargeDensityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new linear charge density with [value] (coulombs per meter) scaled by [factor]. Backs
     * number-times-unit construction.
     */
    override fun scaledBy(factor: Double): KLinearChargeDensityUnitInstance =
        linearChargeDensityInstanceOf(value * factor)

    /**
     * Adds two linear charge densities; both operands are always normalized to
     * [KLinearChargeDensityUnit.BASE] (coulombs per meter) internally.
     */
    override operator fun plus(other: KLinearChargeDensityUnitInstance): KLinearChargeDensityUnitInstance =
        linearChargeDensityInstanceOf(value + other.value)

    /** Subtracts two linear charge densities. See [plus] for the automatic normalization. */
    override operator fun minus(other: KLinearChargeDensityUnitInstance): KLinearChargeDensityUnitInstance =
        linearChargeDensityInstanceOf(value - other.value)

    /**
     * Multiplies two linear charge densities, producing a new [KMixedUnitInstance] (no longer a "pure" linear
     * charge density).
     */
    operator fun times(other: KLinearChargeDensityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two linear charge densities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KLinearChargeDensityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two linear charge densities by their normalized [value] (coulombs per meter). */
    override operator fun compareTo(other: KLinearChargeDensityUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two linear charge densities are equal iff they represent the
     * same physical density, independent of the charge and length units used to build them.
     */
    override fun equals(other: Any?): Boolean =
        other is KLinearChargeDensityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"2.5E-6 C/m"`. */
    override fun toString(): String = "$value ${KLinearChargeDensityUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KLinearChargeDensityUnitInstance] from a value already expressed in coulombs per meter
 * ([KLinearChargeDensityUnit.BASE]).
 *
 * This is the single creation source that every linear charge density decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the three terms `current¹`, `time¹`,
 * `distance⁻¹` (each in its group's base unit). The group has no mass dimension, so no gram/kilogram bridge
 * is required.
 */
internal fun linearChargeDensityInstanceOf(coulombsPerMeter: Double): KLinearChargeDensityUnitInstance =
    KLinearChargeDensityUnitInstance(
        KMixedUnitInstance(
            coulombsPerMeter,
            listOf(
                KUnitTerm(KElectricCurrentUnit.BASE, 1),
                KUnitTerm(KTimeUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, -1),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" linear charge density, as long as it matches the canonical normal
 * form: exactly one [KElectricCurrentUnit] term at exponent `+1`, one [KTimeUnit] term at exponent `+1` and
 * one [KDistanceUnit] term at exponent `-1` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the resulting density is expressed in coulombs per meter
 * regardless of which concrete current/time/distance units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `current¹·time¹·distance⁻¹` linear charge
 * density.
 *
 * Example:
 * ```kotlin
 * val raw = 2 of (amperes * seconds) / meters
 * raw.toLinearChargeDensity().value // 2.0
 * ```
 */
fun KMixedUnitInstance.toLinearChargeDensity(): KLinearChargeDensityUnitInstance {
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -1 }
    check(units.size == 3 && currentTerm != null && timeTerm != null && distanceTerm != null) {
        "KMixedUnitInstance $this does not represent a pure linear charge density (expected KElectricCurrentUnit^1, KTimeUnit^1 and KDistanceUnit^-1)"
    }
    return linearChargeDensityInstanceOf(
        value *
            currentTerm.unit.baseValue *
            timeTerm.unit.baseValue *
            Math.pow(distanceTerm.unit.baseValue, -1.0),
    )
}
