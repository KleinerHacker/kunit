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

package org.pcsoft.framework.kunit.electricfluxdensity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.ec.KElectricCurrentUnit
import org.pcsoft.framework.kunit.time.KTimeUnit

/**
 * Wraps a [KMixedUnitInstance] representing an **electric flux density**, i.e. exactly three terms in the
 * canonical normal form `current¹ · time¹ · distance⁻²` (`A·s·m⁻²` = `C/m²`). The [value] is always
 * normalized internally to coulombs per square meter ([KElectricFluxDensityUnit.BASE]), regardless of which
 * [KElectricFluxDensityUnit], SI [KUnitPrefix], or current/time/distance combination it was constructed from.
 *
 * Electric flux density is a *constructed* unit group: unlike a single-term wrapper it holds a three-term
 * instance. It also carries the dimensionally identical **surface charge density** `σ`. Instances are created
 * via the bare tokens in `KElectricFluxDensityUnitBareValues.kt` (e.g. `5 of coulombsPerSquareMeter`), the
 * prefixed templates in `KElectricFluxDensityUnitExtensions.kt` (e.g. `2 of micro.coulombsPerSquareMeter`),
 * the typed operator (`charge / area`), or [toElectricFluxDensity] on a canonical
 * `current·time·distance⁻²` expression.
 *
 * Example:
 * ```kotlin
 * val d = 5 of micro.coulombsPerSquareMeter
 * d.value                          // 5.0e-6 (normalized to coulombs per square meter)
 * d into coulombsPerSquareMeter    // 5.0e-6
 * ```
 */
class KElectricFluxDensityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KElectricFluxDensityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new electric flux density with [value] (coulombs per square meter) scaled by [factor]. Backs
     * number-times-unit construction (`10 of micro.coulombsPerSquareMeter`).
     */
    override fun scaledBy(factor: Double): KElectricFluxDensityUnitInstance =
        electricFluxDensityInstanceOf(value * factor)

    /**
     * Adds two electric flux densities, automatically converting between different
     * [KElectricFluxDensityUnit]s since both operands are always normalized to
     * [KElectricFluxDensityUnit.BASE] (coulombs per square meter) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of coulombsPerSquareMeter) + (1 of coulombsPerSquareCentimeter)).value // 10001.0
     * ```
     */
    override operator fun plus(other: KElectricFluxDensityUnitInstance): KElectricFluxDensityUnitInstance =
        electricFluxDensityInstanceOf(value + other.value)

    /** Subtracts two electric flux densities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KElectricFluxDensityUnitInstance): KElectricFluxDensityUnitInstance =
        electricFluxDensityInstanceOf(value - other.value)

    /**
     * Multiplies two electric flux densities, producing a new [KMixedUnitInstance] (no longer a "pure"
     * electric flux density).
     */
    operator fun times(other: KElectricFluxDensityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two electric flux densities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KElectricFluxDensityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two electric flux densities by their normalized [value] (coulombs per square meter). */
    override operator fun compareTo(other: KElectricFluxDensityUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two electric flux densities are equal iff they represent the
     * same density (e.g. `(1 of coulombsPerSquareCentimeter) == (10000 of coulombsPerSquareMeter)`).
     */
    override fun equals(other: Any?): Boolean = other is KElectricFluxDensityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"5.0E-6 C/m²"`. */
    override fun toString(): String = "$value ${KElectricFluxDensityUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KElectricFluxDensityUnitInstance] from a value already expressed in coulombs per square meter
 * ([KElectricFluxDensityUnit.BASE]).
 *
 * This is the single creation source that every electric flux density decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the three terms `current¹`, `time¹`,
 * `distance⁻²` (each in its group's base unit). The group has no mass dimension, so no gram/kilogram bridge
 * is required.
 */
internal fun electricFluxDensityInstanceOf(coulombsPerSquareMeter: Double): KElectricFluxDensityUnitInstance =
    KElectricFluxDensityUnitInstance(
        KMixedUnitInstance(
            coulombsPerSquareMeter,
            listOf(
                KUnitTerm(KElectricCurrentUnit.BASE, 1),
                KUnitTerm(KTimeUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, -2),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" electric flux density, as long as it matches the canonical normal
 * form: exactly one [KElectricCurrentUnit] term at exponent `+1`, one [KTimeUnit] term at exponent `+1` and
 * one [KDistanceUnit] term at exponent `-2` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the resulting density is expressed in coulombs per square
 * meter regardless of which concrete current/time/distance units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `current¹·time¹·distance⁻²` electric flux
 * density.
 *
 * Example:
 * ```kotlin
 * val raw = 2 of (amperes * seconds) / (meters pow 2)
 * raw.toElectricFluxDensity().value // 2.0
 * ```
 */
fun KMixedUnitInstance.toElectricFluxDensity(): KElectricFluxDensityUnitInstance {
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -2 }
    check(units.size == 3 && currentTerm != null && timeTerm != null && distanceTerm != null) {
        "KMixedUnitInstance $this does not represent a pure electric flux density (expected KElectricCurrentUnit^1, KTimeUnit^1 and KDistanceUnit^-2)"
    }
    return electricFluxDensityInstanceOf(
        value *
            currentTerm.unit.baseValue *
            timeTerm.unit.baseValue *
            Math.pow(distanceTerm.unit.baseValue, -2.0),
    )
}
