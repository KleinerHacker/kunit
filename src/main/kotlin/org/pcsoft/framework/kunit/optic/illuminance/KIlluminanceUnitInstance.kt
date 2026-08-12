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

package org.pcsoft.framework.kunit.optic.illuminance

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.mechanic.solidangle.KSolidAngleUnit
import org.pcsoft.framework.kunit.optic.luminousintensity.KLuminousIntensityUnit

/**
 * Wraps a [KMixedUnitInstance] representing an **illuminance** (luminous flux per illuminated area), i.e.
 * exactly three terms in the canonical normal form - [KLuminousIntensityUnit.BASE] (candela) at exponent
 * `+1`, [KSolidAngleUnit.BASE] (steradian) at exponent `+1` and [KDistanceUnit.BASE] (meter) at exponent
 * `-2` (`cd·sr·m⁻²` = `lm/m²`). All components are stored in their group's base unit, so the raw component
 * base *is* the named base unit ([KIlluminanceUnit.LUX]) and no bridging factor is needed.
 *
 * Illuminance is a *constructed* unit group with one decomposition, funnelling into
 * [illuminanceInstanceOf]:
 * * `luminousFlux / area` (typed operator, see `KIlluminanceUnitOperators.kt`)
 *
 * Instances are additionally created via the bare tokens in `KIlluminanceUnitBareValues.kt` (e.g.
 * `500 of lux`), the prefixed templates in `KIlluminanceUnitExtensions.kt`, or [toIlluminance].
 *
 * Example:
 * ```kotlin
 * val e = (1000 of lumens) / ((2 of meters) * (1 of meters)) // office desk: 500 lx
 * e into lux
 * ```
 */
class KIlluminanceUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KIlluminanceUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new illuminance with [value] (lux) scaled by [factor]. Backs number-times-unit
     * construction (`500 of lux`).
     */
    override fun scaledBy(factor: Double): KIlluminanceUnitInstance = illuminanceInstanceOf(value * factor)

    /**
     * Adds two illuminances, automatically converting between different [KIlluminanceUnit]s since both
     * operands are always normalized to [KIlluminanceUnit.BASE] (lux) internally.
     */
    override operator fun plus(other: KIlluminanceUnitInstance): KIlluminanceUnitInstance =
        illuminanceInstanceOf(value + other.value)

    /** Subtracts two illuminances. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KIlluminanceUnitInstance): KIlluminanceUnitInstance =
        illuminanceInstanceOf(value - other.value)

    /** Multiplies two illuminances, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KIlluminanceUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two illuminances, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KIlluminanceUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two illuminances by their normalized [value] (lux). */
    override operator fun compareTo(other: KIlluminanceUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two illuminances are equal iff they represent the same
     * quantity (e.g. `(1 of phots) == (10000 of lux)`).
     */
    override fun equals(other: Any?): Boolean = other is KIlluminanceUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in lux, e.g. `"500.0 lx"`. */
    override fun toString(): String = "$value ${KIlluminanceUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KIlluminanceUnitInstance] from a value already expressed in lux ([KIlluminanceUnit.BASE]).
 *
 * This is the single creation source that every illuminance decomposition must funnel into: it assembles
 * the canonical normal-form [KMixedUnitInstance] with the three terms `luminousIntensity¹`, `solidAngle¹`
 * and `distance⁻²` (each in its group's base unit).
 */
internal fun illuminanceInstanceOf(lux: Double): KIlluminanceUnitInstance =
    KIlluminanceUnitInstance(
        KMixedUnitInstance(
            lux,
            listOf(
                KUnitTerm(KLuminousIntensityUnit.BASE, 1),
                KUnitTerm(KSolidAngleUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, -2),
            ),
        ),
    )

/** Builds a value-1 [KIlluminanceUnitInstance] for the given [unit] (its [KIlluminanceUnit.baseValue] lx). */
internal fun illuminanceOfUnit(unit: KIlluminanceUnit): KIlluminanceUnitInstance =
    illuminanceInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" illuminance, as long as it matches the canonical illuminance normal
 * form: exactly one [KLuminousIntensityUnit] term at exponent `+1`, one [KSolidAngleUnit] term at exponent
 * `+1` and one [KDistanceUnit] term at exponent `-2` (order independent). The terms are normalized over
 * their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the result is expressed in lux regardless of
 * which concrete units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical
 * `luminousIntensity·solidAngle·distance⁻²` illuminance.
 */
fun KMixedUnitInstance.toIlluminance(): KIlluminanceUnitInstance {
    val intensityTerm = units.singleOrNull { it.unit is KLuminousIntensityUnit && it.exponent == 1 }
    val solidAngleTerm = units.singleOrNull { it.unit is KSolidAngleUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -2 }
    check(units.size == 3 && intensityTerm != null && solidAngleTerm != null && distanceTerm != null) {
        "KMixedUnitInstance $this does not represent a pure illuminance " +
                "(expected one KLuminousIntensityUnit^1, one KSolidAngleUnit^1 and one KDistanceUnit^-2 term)"
    }
    val distanceBase = distanceTerm.unit.baseValue
    return illuminanceInstanceOf(
        value * intensityTerm.unit.baseValue * solidAngleTerm.unit.baseValue / (distanceBase * distanceBase),
    )
}
