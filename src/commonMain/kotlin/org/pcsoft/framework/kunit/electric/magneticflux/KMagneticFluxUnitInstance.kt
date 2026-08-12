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

package org.pcsoft.framework.kunit.electric.magneticflux

import kotlin.math.pow
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnit
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **magnetic flux**, i.e. exactly four terms in the canonical
 * normal form `mass¹ · distance² · time⁻² · current⁻¹` (`kg·m²·s⁻²·A⁻¹`). The [value] is always normalized
 * internally to webers ([KMagneticFluxUnit.BASE]), regardless of which [KMagneticFluxUnit], SI
 * [KUnitPrefix], or mass/length/time/current combination it was constructed from.
 *
 * Magnetic flux is a *constructed* unit group: unlike a single-term wrapper it holds a four-term instance.
 * Instances are created via the bare tokens in `KMagneticFluxUnitBareValues.kt` (e.g. `5 of webers`), the
 * prefixed templates in `KMagneticFluxUnitExtensions.kt` (e.g. `2 of milli.webers`), or [toMagneticFlux] on
 * a canonical `mass·length²·time⁻²·current⁻¹` expression.
 *
 * Example:
 * ```kotlin
 * val phi = 2 of milli.webers   // 0.002 Wb
 * phi.value                     // 0.002 (normalized to webers)
 * phi into webers               // 0.002 (read back in webers)
 * ```
 */
class KMagneticFluxUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KMagneticFluxUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new magnetic flux value with [value] (webers) scaled by [factor]. Backs number-times-unit
     * construction (`10 of milli.webers`).
     */
    override fun scaledBy(factor: Double): KMagneticFluxUnitInstance = magneticFluxInstanceOf(value * factor)

    /**
     * Adds two magnetic fluxes, automatically converting between different [KMagneticFluxUnit]s since both
     * operands are always normalized to [KMagneticFluxUnit.BASE] (webers) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of webers) + (500 of milli.webers)).value // 1.5
     * ```
     */
    override operator fun plus(other: KMagneticFluxUnitInstance): KMagneticFluxUnitInstance =
        magneticFluxInstanceOf(value + other.value)

    /** Subtracts two magnetic fluxes. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KMagneticFluxUnitInstance): KMagneticFluxUnitInstance =
        magneticFluxInstanceOf(value - other.value)

    /** Multiplies two magnetic fluxes, producing a new [KMixedUnitInstance] (no longer a "pure" flux). */
    operator fun times(other: KMagneticFluxUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two magnetic fluxes, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KMagneticFluxUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two magnetic fluxes by their normalized [value] (webers). */
    override operator fun compareTo(other: KMagneticFluxUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two magnetic fluxes are equal iff they represent the same
     * flux (e.g. `(1 of webers) == (1e8 of maxwells)`).
     */
    override fun equals(other: Any?): Boolean = other is KMagneticFluxUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"10.0 Wb"`. */
    override fun toString(): String = "${renderDouble(value)} ${KMagneticFluxUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// The weber's SI definition uses the *kilogram* as its mass dimension (`1 Wb = 1 kg·m²·s⁻²·A⁻¹`), whereas
// the mass group's base unit is the gram. The canonical normal form is therefore stored with the mass
// group's base term (gram), and this factor bridges a gram-based canonical product to webers. The mass
// exponent is *positive* (+1) here, so the bridge divides (like the ohm).
private val WEBER_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KMagneticFluxUnitInstance] from a value already expressed in webers
 * ([KMagneticFluxUnit.BASE]).
 *
 * This is the single creation source that every magnetic flux decomposition must funnel into: it assembles
 * the canonical normal-form [KMixedUnitInstance] with the four terms `mass¹`, `distance²`, `time⁻²`,
 * `current⁻¹` (each in its group's base unit).
 */
internal fun magneticFluxInstanceOf(webers: Double): KMagneticFluxUnitInstance =
    KMagneticFluxUnitInstance(
        KMixedUnitInstance(
            webers,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 2),
                KUnitTerm(KTimeUnit.BASE, -2),
                KUnitTerm(KElectricCurrentUnit.BASE, -1),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" magnetic flux value, as long as it matches the canonical magnetic
 * flux normal form: exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent
 * `+2`, one [KTimeUnit] term at exponent `-2` and one [KElectricCurrentUnit] term at exponent `-1` (order
 * independent). The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the
 * resulting flux is expressed in webers regardless of which concrete mass/length/time/current units the
 * terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass¹·distance²·time⁻²·current⁻¹`
 * magnetic flux.
 *
 * Example:
 * ```kotlin
 * val raw = 3 of (kilo.grams * (meters pow 2)) / ((seconds pow 2) * amperes)
 * raw.toMagneticFlux().value // 3.0
 * ```
 */
fun KMixedUnitInstance.toMagneticFlux(): KMagneticFluxUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -2 }
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == -1 }
    check(units.size == 4 && massTerm != null && distanceTerm != null && timeTerm != null && currentTerm != null) {
        "KMixedUnitInstance $this does not represent a pure magnetic flux (expected KMassUnit^1, KDistanceUnit^2, KTimeUnit^-2 and KElectricCurrentUnit^-1)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue *
            distanceTerm.unit.baseValue.pow(2.0) *
            timeTerm.unit.baseValue.pow(-2.0) *
            currentTerm.unit.baseValue.pow(-1.0)
    return magneticFluxInstanceOf(gramBaseProduct / WEBER_MASS_REFERENCE)
}
