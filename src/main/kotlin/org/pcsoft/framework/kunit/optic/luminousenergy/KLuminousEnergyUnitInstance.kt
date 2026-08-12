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

package org.pcsoft.framework.kunit.optic.luminousenergy

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.solidangle.KSolidAngleUnit
import org.pcsoft.framework.kunit.optic.luminousintensity.KLuminousIntensityUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **luminous energy** (luminous flux accumulated over time),
 * i.e. exactly three terms in the canonical normal form - [KLuminousIntensityUnit.BASE] (candela) at
 * exponent `+1`, [KSolidAngleUnit.BASE] (steradian) at exponent `+1` and [KTimeUnit.BASE] (second) at
 * exponent `+1` (`cd·sr·s` = `lm·s`). All components are stored in their group's base unit, so the raw
 * component base *is* the named base unit ([KLuminousEnergyUnit.LUMEN_SECOND]) and no bridging factor is
 * needed.
 *
 * Luminous energy is a *constructed* unit group with one decomposition, funnelling into
 * [luminousEnergyInstanceOf]:
 * * `luminousFlux * time` (typed operator, see `KLuminousEnergyUnitOperators.kt`)
 *
 * Instances are additionally created via the bare tokens in `KLuminousEnergyUnitBareValues.kt` (e.g.
 * `5 of lumenSeconds`), the prefixed templates in `KLuminousEnergyUnitExtensions.kt`, or
 * [toLuminousEnergy].
 *
 * Example:
 * ```kotlin
 * val q = (800 of lumens) * (2 of hours) // 1600 lm·h
 * q into lumenHours
 * ```
 */
class KLuminousEnergyUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KLuminousEnergyUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new luminous energy with [value] (lm·s) scaled by [factor]. Backs number-times-unit
     * construction (`5 of lumenSeconds`).
     */
    override fun scaledBy(factor: Double): KLuminousEnergyUnitInstance =
        luminousEnergyInstanceOf(value * factor)

    /**
     * Adds two luminous energies, automatically converting between different [KLuminousEnergyUnit]s since
     * both operands are always normalized to [KLuminousEnergyUnit.BASE] (lm·s) internally.
     */
    override operator fun plus(other: KLuminousEnergyUnitInstance): KLuminousEnergyUnitInstance =
        luminousEnergyInstanceOf(value + other.value)

    /** Subtracts two luminous energies. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KLuminousEnergyUnitInstance): KLuminousEnergyUnitInstance =
        luminousEnergyInstanceOf(value - other.value)

    /** Multiplies two luminous energies, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KLuminousEnergyUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two luminous energies, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KLuminousEnergyUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two luminous energies by their normalized [value] (lm·s). */
    override operator fun compareTo(other: KLuminousEnergyUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two luminous energies are equal iff they represent the
     * same quantity (e.g. `(1 of lumenHours) == (3600 of lumenSeconds)`).
     */
    override fun equals(other: Any?): Boolean = other is KLuminousEnergyUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in lumen seconds, e.g. `"3600.0 lm*s"`. */
    override fun toString(): String = "$value ${KLuminousEnergyUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KLuminousEnergyUnitInstance] from a value already expressed in lumen seconds
 * ([KLuminousEnergyUnit.BASE]).
 *
 * This is the single creation source that every luminous energy decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the three terms `luminousIntensity¹`,
 * `solidAngle¹` and `time¹` (each in its group's base unit).
 */
internal fun luminousEnergyInstanceOf(lumenSeconds: Double): KLuminousEnergyUnitInstance =
    KLuminousEnergyUnitInstance(
        KMixedUnitInstance(
            lumenSeconds,
            listOf(
                KUnitTerm(KLuminousIntensityUnit.BASE, 1),
                KUnitTerm(KSolidAngleUnit.BASE, 1),
                KUnitTerm(KTimeUnit.BASE, 1),
            ),
        ),
    )

/**
 * Builds a value-1 [KLuminousEnergyUnitInstance] for the given [unit] (its
 * [KLuminousEnergyUnit.baseValue] lm·s).
 */
internal fun luminousEnergyOfUnit(unit: KLuminousEnergyUnit): KLuminousEnergyUnitInstance =
    luminousEnergyInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" luminous energy, as long as it matches the canonical luminous
 * energy normal form: exactly one [KLuminousIntensityUnit] term at exponent `+1`, one [KSolidAngleUnit]
 * term at exponent `+1` and one [KTimeUnit] term at exponent `+1` (order independent). The terms are
 * normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the result is expressed in lm·s
 * regardless of which concrete units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `luminousIntensity·solidAngle·time`
 * luminous energy.
 */
fun KMixedUnitInstance.toLuminousEnergy(): KLuminousEnergyUnitInstance {
    val intensityTerm = units.singleOrNull { it.unit is KLuminousIntensityUnit && it.exponent == 1 }
    val solidAngleTerm = units.singleOrNull { it.unit is KSolidAngleUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 1 }
    check(units.size == 3 && intensityTerm != null && solidAngleTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure luminous energy " +
                "(expected one KLuminousIntensityUnit^1, one KSolidAngleUnit^1 and one KTimeUnit^1 term)"
    }
    return luminousEnergyInstanceOf(
        value * intensityTerm.unit.baseValue * solidAngleTerm.unit.baseValue * timeTerm.unit.baseValue,
    )
}
