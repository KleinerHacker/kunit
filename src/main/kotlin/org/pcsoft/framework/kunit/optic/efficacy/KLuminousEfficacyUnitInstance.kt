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

package org.pcsoft.framework.kunit.optic.efficacy

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit
import org.pcsoft.framework.kunit.mechanic.solidangle.KSolidAngleUnit
import org.pcsoft.framework.kunit.optic.luminousintensity.KLuminousIntensityUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **luminous efficacy** (luminous flux per electrical power),
 * i.e. exactly five terms in the canonical normal form - [KLuminousIntensityUnit.BASE] (candela) at
 * exponent `+1`, [KSolidAngleUnit.BASE] (steradian) at exponent `+1`, [KMassUnit.BASE] (gram) at exponent
 * `-1`, [KDistanceUnit.BASE] (meter) at exponent `-2` and [KTimeUnit.BASE] (second) at exponent `+3`
 * (`cd·sr·kg⁻¹·m⁻²·s³` = `lm/W`).
 *
 * Luminous efficacy is a *constructed* unit group with one decomposition, funnelling into
 * [luminousEfficacyInstanceOf]:
 * * `luminousFlux / power` (typed operator, see `KLuminousEfficacyUnitOperators.kt`)
 *
 * Instances are additionally created via the bare tokens in `KLuminousEfficacyUnitBareValues.kt` (e.g.
 * `120 of lumensPerWatt`), the prefixed templates in `KLuminousEfficacyUnitExtensions.kt`, or
 * [toLuminousEfficacy].
 *
 * Example:
 * ```kotlin
 * val eta = (800 of lumens) / (7 of watts) // LED bulb: ≈ 114 lm/W
 * eta into lumensPerWatt
 * ```
 */
class KLuminousEfficacyUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KLuminousEfficacyUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new luminous efficacy with [value] (lm/W) scaled by [factor]. Backs number-times-unit
     * construction (`120 of lumensPerWatt`).
     */
    override fun scaledBy(factor: Double): KLuminousEfficacyUnitInstance =
        luminousEfficacyInstanceOf(value * factor)

    /**
     * Adds two luminous efficacies, automatically converting between different [KLuminousEfficacyUnit]s
     * since both operands are always normalized to [KLuminousEfficacyUnit.BASE] (lm/W) internally.
     */
    override operator fun plus(other: KLuminousEfficacyUnitInstance): KLuminousEfficacyUnitInstance =
        luminousEfficacyInstanceOf(value + other.value)

    /** Subtracts two luminous efficacies. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KLuminousEfficacyUnitInstance): KLuminousEfficacyUnitInstance =
        luminousEfficacyInstanceOf(value - other.value)

    /** Multiplies two luminous efficacies, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KLuminousEfficacyUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two luminous efficacies, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KLuminousEfficacyUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two luminous efficacies by their normalized [value] (lm/W). */
    override operator fun compareTo(other: KLuminousEfficacyUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two luminous efficacies are equal iff they represent the
     * same quantity (e.g. `(1 of lumensPerWatt) == (1000 of milli.lumensPerWatt)`).
     */
    override fun equals(other: Any?): Boolean =
        other is KLuminousEfficacyUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in lumens per watt, e.g. `"120.0 lm/W"`. */
    override fun toString(): String = "$value ${KLuminousEfficacyUnit.BASE.symbol}"
}

// The watt's SI definition uses the *kilogram* as its mass dimension (`1 W = 1 kg·m²·s⁻³`), whereas the
// mass group's base unit is the gram. The canonical normal form is therefore stored with the mass group's
// base term (gram), and this factor bridges a gram-based canonical product to lumens per watt. The mass
// exponent is *negative* (-1) here, so the bridge is applied as `pow(reference, -1.0)` (like the siemens).
private val LUMEN_PER_WATT_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KLuminousEfficacyUnitInstance] from a value already expressed in lumens per watt
 * ([KLuminousEfficacyUnit.BASE]).
 *
 * This is the single creation source that every luminous efficacy decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the five terms `luminousIntensity¹`,
 * `solidAngle¹`, `mass⁻¹`, `distance⁻²` and `time³` (each in its group's base unit).
 */
internal fun luminousEfficacyInstanceOf(lumensPerWatt: Double): KLuminousEfficacyUnitInstance =
    KLuminousEfficacyUnitInstance(
        KMixedUnitInstance(
            lumensPerWatt,
            listOf(
                KUnitTerm(KLuminousIntensityUnit.BASE, 1),
                KUnitTerm(KSolidAngleUnit.BASE, 1),
                KUnitTerm(KMassUnit.BASE, -1),
                KUnitTerm(KDistanceUnit.BASE, -2),
                KUnitTerm(KTimeUnit.BASE, 3),
            ),
        ),
    )

/**
 * Builds a value-1 [KLuminousEfficacyUnitInstance] for the given [unit] (its
 * [KLuminousEfficacyUnit.baseValue] lm/W).
 */
internal fun luminousEfficacyOfUnit(unit: KLuminousEfficacyUnit): KLuminousEfficacyUnitInstance =
    luminousEfficacyInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" luminous efficacy, as long as it matches the canonical luminous
 * efficacy normal form: exactly one [KLuminousIntensityUnit] term at exponent `+1`, one [KSolidAngleUnit]
 * term at exponent `+1`, one [KMassUnit] term at exponent `-1`, one [KDistanceUnit] term at exponent `-2`
 * and one [KTimeUnit] term at exponent `+3` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s and bridged from the gram-based product to the
 * kilogram-based watt, so the result is expressed in lm/W regardless of which concrete units the terms
 * were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical
 * `luminousIntensity·solidAngle·mass⁻¹·distance⁻²·time³` luminous efficacy.
 */
fun KMixedUnitInstance.toLuminousEfficacy(): KLuminousEfficacyUnitInstance {
    val intensityTerm = units.singleOrNull { it.unit is KLuminousIntensityUnit && it.exponent == 1 }
    val solidAngleTerm = units.singleOrNull { it.unit is KSolidAngleUnit && it.exponent == 1 }
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == -1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 3 }
    check(
        units.size == 5 && intensityTerm != null && solidAngleTerm != null &&
                massTerm != null && distanceTerm != null && timeTerm != null
    ) {
        "KMixedUnitInstance $this does not represent a pure luminous efficacy (expected one " +
                "KLuminousIntensityUnit^1, one KSolidAngleUnit^1, one KMassUnit^-1, one KDistanceUnit^-2 " +
                "and one KTimeUnit^3 term)"
    }
    val gramBaseProduct = value *
            intensityTerm.unit.baseValue *
            solidAngleTerm.unit.baseValue *
            Math.pow(massTerm.unit.baseValue, -1.0) *
            Math.pow(distanceTerm.unit.baseValue, -2.0) *
            Math.pow(timeTerm.unit.baseValue, 3.0)
    return luminousEfficacyInstanceOf(gramBaseProduct / Math.pow(LUMEN_PER_WATT_MASS_REFERENCE, -1.0))
}
