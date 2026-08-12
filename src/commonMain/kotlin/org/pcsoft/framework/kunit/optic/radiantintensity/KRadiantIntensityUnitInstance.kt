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

package org.pcsoft.framework.kunit.optic.radiantintensity

import kotlin.math.pow
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit
import org.pcsoft.framework.kunit.mechanic.solidangle.KSolidAngleUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **radiant intensity** (radiant flux per solid angle), i.e.
 * exactly four terms in the canonical normal form - [KMassUnit.BASE] (gram) at exponent `+1`,
 * [KDistanceUnit.BASE] (meter) at exponent `+2`, [KTimeUnit.BASE] (second) at exponent `-3` and
 * [KSolidAngleUnit.BASE] (steradian) at exponent `-1` (`kg·m²·s⁻³·sr⁻¹` = `W/sr`).
 *
 * Radiant intensity is a *constructed* unit group with one decomposition, funnelling into
 * [radiantIntensityInstanceOf]:
 * * `power / solidAngle` (typed operator, see `KRadiantIntensityUnitOperators.kt`)
 *
 * Instances are additionally created via the bare tokens in `KRadiantIntensityUnitBareValues.kt` (e.g.
 * `5 of wattsPerSteradian`), the prefixed templates in `KRadiantIntensityUnitExtensions.kt`, or
 * [toRadiantIntensity].
 *
 * Example:
 * ```kotlin
 * val i = (20 of watts) / (4 of steradians) // 5 W/sr
 * i into wattsPerSteradian
 * ```
 */
class KRadiantIntensityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KRadiantIntensityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new radiant intensity with [value] (W/sr) scaled by [factor]. Backs number-times-unit
     * construction (`5 of wattsPerSteradian`).
     */
    override fun scaledBy(factor: Double): KRadiantIntensityUnitInstance =
        radiantIntensityInstanceOf(value * factor)

    /**
     * Adds two radiant intensities, automatically converting between different [KRadiantIntensityUnit]s
     * since both operands are always normalized to [KRadiantIntensityUnit.BASE] (W/sr) internally.
     */
    override operator fun plus(other: KRadiantIntensityUnitInstance): KRadiantIntensityUnitInstance =
        radiantIntensityInstanceOf(value + other.value)

    /** Subtracts two radiant intensities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KRadiantIntensityUnitInstance): KRadiantIntensityUnitInstance =
        radiantIntensityInstanceOf(value - other.value)

    /** Multiplies two radiant intensities, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KRadiantIntensityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two radiant intensities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KRadiantIntensityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two radiant intensities by their normalized [value] (W/sr). */
    override operator fun compareTo(other: KRadiantIntensityUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two radiant intensities are equal iff they represent the
     * same quantity (e.g. `(1 of wattsPerSteradian) == (1000 of milli.wattsPerSteradian)`).
     */
    override fun equals(other: Any?): Boolean =
        other is KRadiantIntensityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in watts per steradian, e.g. `"5.0 W/sr"`. */
    override fun toString(): String = "${renderDouble(value)} ${KRadiantIntensityUnit.BASE.symbol}"
}

// The watt's SI definition uses the *kilogram* as its mass dimension (`1 W = 1 kg·m²·s⁻³`), whereas the
// mass group's base unit is the gram. The canonical normal form is therefore stored with the mass group's
// base term (gram), and this factor bridges a gram-based canonical product to watts per steradian. The
// mass exponent is *positive* (+1) here, so the bridge divides (like the watt itself).
private val WATT_PER_STERADIAN_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KRadiantIntensityUnitInstance] from a value already expressed in watts per steradian
 * ([KRadiantIntensityUnit.BASE]).
 *
 * This is the single creation source that every radiant intensity decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the four terms `mass¹`, `distance²`,
 * `time⁻³` and `solidAngle⁻¹` (each in its group's base unit).
 */
internal fun radiantIntensityInstanceOf(wattsPerSteradian: Double): KRadiantIntensityUnitInstance =
    KRadiantIntensityUnitInstance(
        KMixedUnitInstance(
            wattsPerSteradian,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 2),
                KUnitTerm(KTimeUnit.BASE, -3),
                KUnitTerm(KSolidAngleUnit.BASE, -1),
            ),
        ),
    )

/**
 * Builds a value-1 [KRadiantIntensityUnitInstance] for the given [unit] (its
 * [KRadiantIntensityUnit.baseValue] W/sr).
 */
internal fun radiantIntensityOfUnit(unit: KRadiantIntensityUnit): KRadiantIntensityUnitInstance =
    radiantIntensityInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" radiant intensity, as long as it matches the canonical radiant
 * intensity normal form: exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at
 * exponent `+2`, one [KTimeUnit] term at exponent `-3` and one [KSolidAngleUnit] term at exponent `-1`
 * (order independent). The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s
 * and bridged from the gram-based product to the kilogram-based watt.
 *
 * @throws IllegalStateException if this instance is not a canonical
 * `mass·distance²·time⁻³·solidAngle⁻¹` radiant intensity.
 */
fun KMixedUnitInstance.toRadiantIntensity(): KRadiantIntensityUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -3 }
    val solidAngleTerm = units.singleOrNull { it.unit is KSolidAngleUnit && it.exponent == -1 }
    check(
        units.size == 4 && massTerm != null && distanceTerm != null &&
                timeTerm != null && solidAngleTerm != null
    ) {
        "KMixedUnitInstance $this does not represent a pure radiant intensity (expected one KMassUnit^1, " +
                "one KDistanceUnit^2, one KTimeUnit^-3 and one KSolidAngleUnit^-1 term)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue *
            distanceTerm.unit.baseValue.pow(2.0) *
            timeTerm.unit.baseValue.pow(-3.0) *
            solidAngleTerm.unit.baseValue.pow(-1.0)
    return radiantIntensityInstanceOf(gramBaseProduct / WATT_PER_STERADIAN_MASS_REFERENCE)
}
