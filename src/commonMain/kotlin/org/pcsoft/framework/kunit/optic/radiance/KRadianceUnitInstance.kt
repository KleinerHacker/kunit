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

package org.pcsoft.framework.kunit.optic.radiance

import kotlin.math.pow
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit
import org.pcsoft.framework.kunit.mechanic.solidangle.KSolidAngleUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **radiance** (radiant intensity per emitting area), i.e.
 * exactly three terms in the canonical normal form - [KMassUnit.BASE] (gram) at exponent `+1`,
 * [KTimeUnit.BASE] (second) at exponent `-3` and [KSolidAngleUnit.BASE] (steradian) at exponent `-1`
 * (`kg·s⁻³·sr⁻¹` = `W/(sr·m²)`; the two length exponents of the watt and of the area cancel).
 *
 * Radiance is a *constructed* unit group with one decomposition, funnelling into [radianceInstanceOf]:
 * * `radiantIntensity / area` (typed operator, see `KRadianceUnitOperators.kt`)
 *
 * Instances are additionally created via the bare tokens in `KRadianceUnitBareValues.kt` (e.g.
 * `5 of wattsPerSteradianSquareMeter`), the prefixed templates in `KRadianceUnitExtensions.kt`, or
 * [toRadiance].
 *
 * Example:
 * ```kotlin
 * val l = (10 of wattsPerSteradian) / ((2 of meters) * (1 of meters)) // 5 W/(sr·m²)
 * l into wattsPerSteradianSquareMeter
 * ```
 */
class KRadianceUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KRadianceUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new radiance with [value] (W/(sr·m²)) scaled by [factor]. Backs number-times-unit
     * construction (`5 of wattsPerSteradianSquareMeter`).
     */
    override fun scaledBy(factor: Double): KRadianceUnitInstance = radianceInstanceOf(value * factor)

    /**
     * Adds two radiances, automatically converting between different [KRadianceUnit]s since both operands
     * are always normalized to [KRadianceUnit.BASE] internally.
     */
    override operator fun plus(other: KRadianceUnitInstance): KRadianceUnitInstance =
        radianceInstanceOf(value + other.value)

    /** Subtracts two radiances. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KRadianceUnitInstance): KRadianceUnitInstance =
        radianceInstanceOf(value - other.value)

    /** Multiplies two radiances, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KRadianceUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two radiances, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KRadianceUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two radiances by their normalized [value] (W/(sr·m²)). */
    override operator fun compareTo(other: KRadianceUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two radiances are equal iff they represent the same
     * quantity (e.g. `(1 of wattsPerSteradianSquareMeter) == (1000 of milli.wattsPerSteradianSquareMeter)`).
     */
    override fun equals(other: Any?): Boolean = other is KRadianceUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"5.0 W/(sr*m^2)"`. */
    override fun toString(): String = "${renderDouble(value)} ${KRadianceUnit.BASE.symbol}"
}

// The watt's SI definition uses the *kilogram* as its mass dimension, whereas the mass group's base unit is
// the gram. The canonical normal form is therefore stored with the mass group's base term (gram), and this
// factor bridges a gram-based canonical product to the named unit. The mass exponent is *positive* (+1)
// here, so the bridge divides (like the watt itself).
private val RADIANCE_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KRadianceUnitInstance] from a value already expressed in watts per steradian square meter
 * ([KRadianceUnit.BASE]).
 *
 * This is the single creation source that every radiance decomposition must funnel into: it assembles the
 * canonical normal-form [KMixedUnitInstance] with the three terms `mass¹`, `time⁻³` and `solidAngle⁻¹`
 * (each in its group's base unit).
 */
internal fun radianceInstanceOf(wattsPerSteradianSquareMeter: Double): KRadianceUnitInstance =
    KRadianceUnitInstance(
        KMixedUnitInstance(
            wattsPerSteradianSquareMeter,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KTimeUnit.BASE, -3),
                KUnitTerm(KSolidAngleUnit.BASE, -1),
            ),
        ),
    )

/** Builds a value-1 [KRadianceUnitInstance] for the given [unit] (its [KRadianceUnit.baseValue]). */
internal fun radianceOfUnit(unit: KRadianceUnit): KRadianceUnitInstance = radianceInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" radiance, as long as it matches the canonical radiance normal form:
 * exactly one [KMassUnit] term at exponent `+1`, one [KTimeUnit] term at exponent `-3` and one
 * [KSolidAngleUnit] term at exponent `-1` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s and bridged from the gram-based product to the
 * kilogram-based watt.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass·time⁻³·solidAngle⁻¹` radiance.
 */
fun KMixedUnitInstance.toRadiance(): KRadianceUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -3 }
    val solidAngleTerm = units.singleOrNull { it.unit is KSolidAngleUnit && it.exponent == -1 }
    check(units.size == 3 && massTerm != null && timeTerm != null && solidAngleTerm != null) {
        "KMixedUnitInstance $this does not represent a pure radiance " +
                "(expected one KMassUnit^1, one KTimeUnit^-3 and one KSolidAngleUnit^-1 term)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue *
            timeTerm.unit.baseValue.pow(-3.0) *
            solidAngleTerm.unit.baseValue.pow(-1.0)
    return radianceInstanceOf(gramBaseProduct / RADIANCE_MASS_REFERENCE)
}
