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

package org.pcsoft.framework.kunit.optic.luminousflux

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.mechanic.solidangle.KSolidAngleUnit
import org.pcsoft.framework.kunit.optic.luminousintensity.KLuminousIntensityUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **luminous flux** (luminous intensity per solid angle
 * cone), i.e. exactly two terms in the canonical normal form - [KLuminousIntensityUnit.BASE] (candela) at
 * exponent `+1` and [KSolidAngleUnit.BASE] (steradian) at exponent `+1` (`cd·sr`). Both components are
 * stored in their group's base unit, so the raw component base *is* the named base unit
 * ([KLuminousFluxUnit.LUMEN]) and no bridging factor is needed.
 *
 * Luminous flux is a *constructed* unit group with one decomposition, funnelling into
 * [luminousFluxInstanceOf]:
 * * `luminousIntensity * solidAngle` (typed operator, see `KLuminousFluxUnitOperators.kt`)
 *
 * Instances are additionally created via the bare tokens in `KLuminousFluxUnitBareValues.kt` (e.g.
 * `800 of lumens`), the prefixed templates in `KLuminousFluxUnitExtensions.kt`, or [toLuminousFlux].
 *
 * Example:
 * ```kotlin
 * val phi = (100 of candelas) * (4 * Math.PI of steradians) // isotropic lamp: ≈ 1256.6 lm
 * phi into lumens
 * ```
 */
class KLuminousFluxUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KLuminousFluxUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new luminous flux with [value] (lumens) scaled by [factor]. Backs number-times-unit
     * construction (`800 of lumens`).
     */
    override fun scaledBy(factor: Double): KLuminousFluxUnitInstance = luminousFluxInstanceOf(value * factor)

    /**
     * Adds two luminous fluxes, automatically converting between different [KLuminousFluxUnit]s since both
     * operands are always normalized to [KLuminousFluxUnit.BASE] (lumens) internally.
     */
    override operator fun plus(other: KLuminousFluxUnitInstance): KLuminousFluxUnitInstance =
        luminousFluxInstanceOf(value + other.value)

    /** Subtracts two luminous fluxes. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KLuminousFluxUnitInstance): KLuminousFluxUnitInstance =
        luminousFluxInstanceOf(value - other.value)

    /** Multiplies two luminous fluxes, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KLuminousFluxUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two luminous fluxes, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KLuminousFluxUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two luminous fluxes by their normalized [value] (lumens). */
    override operator fun compareTo(other: KLuminousFluxUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two luminous fluxes are equal iff they represent the same
     * quantity (e.g. `(1 of lumens) == (1000 of milli.lumens)`).
     */
    override fun equals(other: Any?): Boolean = other is KLuminousFluxUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in lumens, e.g. `"800.0 lm"`. */
    override fun toString(): String = "$value ${KLuminousFluxUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KLuminousFluxUnitInstance] from a value already expressed in lumens
 * ([KLuminousFluxUnit.BASE]).
 *
 * This is the single creation source that every luminous flux decomposition must funnel into: it assembles
 * the canonical normal-form [KMixedUnitInstance] with the two terms `luminousIntensity¹` and `solidAngle¹`
 * (each in its group's base unit).
 */
internal fun luminousFluxInstanceOf(lumens: Double): KLuminousFluxUnitInstance =
    KLuminousFluxUnitInstance(
        KMixedUnitInstance(
            lumens,
            listOf(
                KUnitTerm(KLuminousIntensityUnit.BASE, 1),
                KUnitTerm(KSolidAngleUnit.BASE, 1),
            ),
        ),
    )

/** Builds a value-1 [KLuminousFluxUnitInstance] for the given [unit] (its [KLuminousFluxUnit.baseValue] lm). */
internal fun luminousFluxOfUnit(unit: KLuminousFluxUnit): KLuminousFluxUnitInstance =
    luminousFluxInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" luminous flux, as long as it matches the canonical luminous flux
 * normal form: exactly one [KLuminousIntensityUnit] term at exponent `+1` and one [KSolidAngleUnit] term at
 * exponent `+1` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the result is expressed in lumens regardless of which
 * concrete units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `luminousIntensity·solidAngle`
 * luminous flux.
 */
fun KMixedUnitInstance.toLuminousFlux(): KLuminousFluxUnitInstance {
    val intensityTerm = units.singleOrNull { it.unit is KLuminousIntensityUnit && it.exponent == 1 }
    val solidAngleTerm = units.singleOrNull { it.unit is KSolidAngleUnit && it.exponent == 1 }
    check(units.size == 2 && intensityTerm != null && solidAngleTerm != null) {
        "KMixedUnitInstance $this does not represent a pure luminous flux " +
                "(expected one KLuminousIntensityUnit^1 and one KSolidAngleUnit^1 term)"
    }
    return luminousFluxInstanceOf(value * intensityTerm.unit.baseValue * solidAngleTerm.unit.baseValue)
}
