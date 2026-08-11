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

package org.pcsoft.framework.kunit.optic.luminousexposure

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.solidangle.KSolidAngleUnit
import org.pcsoft.framework.kunit.optic.luminousintensity.KLuminousIntensityUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **luminous exposure** (illuminance accumulated over time),
 * i.e. exactly four terms in the canonical normal form - [KLuminousIntensityUnit.BASE] (candela) at
 * exponent `+1`, [KSolidAngleUnit.BASE] (steradian) at exponent `+1`, [KDistanceUnit.BASE] (meter) at
 * exponent `-2` and [KTimeUnit.BASE] (second) at exponent `+1` (`cd·sr·m⁻²·s` = `lx·s`). All components
 * are stored in their group's base unit, so the raw component base *is* the named base unit
 * ([KLuminousExposureUnit.LUX_SECOND]) and no bridging factor is needed.
 *
 * Luminous exposure is a *constructed* unit group with one decomposition, funnelling into
 * [luminousExposureInstanceOf]:
 * * `illuminance * time` (typed operator, see `KLuminousExposureUnitOperators.kt`)
 *
 * Instances are additionally created via the bare tokens in `KLuminousExposureUnitBareValues.kt` (e.g.
 * `50 of luxSeconds`), the prefixed templates in `KLuminousExposureUnitExtensions.kt`, or
 * [toLuminousExposure].
 *
 * Example:
 * ```kotlin
 * val h = (50 of lux) * (8 of hours) // museum light dose: 400 lx·h
 * h into luxHours
 * ```
 */
class KLuminousExposureUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KLuminousExposureUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new luminous exposure with [value] (lx·s) scaled by [factor]. Backs number-times-unit
     * construction (`50 of luxSeconds`).
     */
    override fun scaledBy(factor: Double): KLuminousExposureUnitInstance =
        luminousExposureInstanceOf(value * factor)

    /**
     * Adds two luminous exposures, automatically converting between different [KLuminousExposureUnit]s
     * since both operands are always normalized to [KLuminousExposureUnit.BASE] (lx·s) internally.
     */
    override operator fun plus(other: KLuminousExposureUnitInstance): KLuminousExposureUnitInstance =
        luminousExposureInstanceOf(value + other.value)

    /** Subtracts two luminous exposures. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KLuminousExposureUnitInstance): KLuminousExposureUnitInstance =
        luminousExposureInstanceOf(value - other.value)

    /** Multiplies two luminous exposures, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KLuminousExposureUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two luminous exposures, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KLuminousExposureUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two luminous exposures by their normalized [value] (lx·s). */
    override operator fun compareTo(other: KLuminousExposureUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two luminous exposures are equal iff they represent the
     * same quantity (e.g. `(1 of luxHours) == (3600 of luxSeconds)`).
     */
    override fun equals(other: Any?): Boolean =
        other is KLuminousExposureUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in lux seconds, e.g. `"3600.0 lx*s"`. */
    override fun toString(): String = "$value ${KLuminousExposureUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KLuminousExposureUnitInstance] from a value already expressed in lux seconds
 * ([KLuminousExposureUnit.BASE]).
 *
 * This is the single creation source that every luminous exposure decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the four terms `luminousIntensity¹`,
 * `solidAngle¹`, `distance⁻²` and `time¹` (each in its group's base unit).
 */
internal fun luminousExposureInstanceOf(luxSeconds: Double): KLuminousExposureUnitInstance =
    KLuminousExposureUnitInstance(
        KMixedUnitInstance(
            luxSeconds,
            listOf(
                KUnitTerm(KLuminousIntensityUnit.BASE, 1),
                KUnitTerm(KSolidAngleUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, -2),
                KUnitTerm(KTimeUnit.BASE, 1),
            ),
        ),
    )

/**
 * Builds a value-1 [KLuminousExposureUnitInstance] for the given [unit] (its
 * [KLuminousExposureUnit.baseValue] lx·s).
 */
internal fun luminousExposureOfUnit(unit: KLuminousExposureUnit): KLuminousExposureUnitInstance =
    luminousExposureInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" luminous exposure, as long as it matches the canonical luminous
 * exposure normal form: exactly one [KLuminousIntensityUnit] term at exponent `+1`, one [KSolidAngleUnit]
 * term at exponent `+1`, one [KDistanceUnit] term at exponent `-2` and one [KTimeUnit] term at exponent
 * `+1` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the result is expressed in lx·s regardless of which
 * concrete units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical
 * `luminousIntensity·solidAngle·distance⁻²·time` luminous exposure.
 */
fun KMixedUnitInstance.toLuminousExposure(): KLuminousExposureUnitInstance {
    val intensityTerm = units.singleOrNull { it.unit is KLuminousIntensityUnit && it.exponent == 1 }
    val solidAngleTerm = units.singleOrNull { it.unit is KSolidAngleUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 1 }
    check(
        units.size == 4 && intensityTerm != null && solidAngleTerm != null &&
                distanceTerm != null && timeTerm != null
    ) {
        "KMixedUnitInstance $this does not represent a pure luminous exposure (expected one " +
                "KLuminousIntensityUnit^1, one KSolidAngleUnit^1, one KDistanceUnit^-2 and one KTimeUnit^1 term)"
    }
    val distanceBase = distanceTerm.unit.baseValue
    return luminousExposureInstanceOf(
        value * intensityTerm.unit.baseValue * solidAngleTerm.unit.baseValue * timeTerm.unit.baseValue /
                (distanceBase * distanceBase),
    )
}
