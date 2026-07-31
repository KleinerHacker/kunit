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

package org.pcsoft.framework.kunit.electric.magneticfluxdensity

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **magnetic flux density**, i.e. exactly three terms in the
 * canonical normal form `mass¹ · time⁻² · current⁻¹` (`kg·s⁻²·A⁻¹`). The [value] is always normalized
 * internally to teslas ([KMagneticFluxDensityUnit.BASE]), regardless of which
 * [KMagneticFluxDensityUnit], SI [org.pcsoft.framework.kunit.KUnitPrefix], or mass/time/current
 * combination it was constructed from.
 *
 * Magnetic flux density is a *constructed* unit group: unlike a single-term wrapper it holds a three-term
 * instance. Instances are created via the bare tokens in `KMagneticFluxDensityUnitBareValues.kt` (e.g.
 * `5 of teslas`), the prefixed templates in `KMagneticFluxDensityUnitExtensions.kt` (e.g.
 * `50 of micro.teslas`), the typed `flux / area` operator, or [toMagneticFluxDensity] on a canonical
 * mass·time⁻²·current⁻¹ expression.
 *
 * Example:
 * ```kotlin
 * val b = 50 of micro.teslas   // 5.0e-5 T (Earth's magnetic field)
 * b.value                      // 5.0e-5 (normalized to teslas)
 * b into gauss                 // 0.5 (read back in gauss)
 * ```
 */
class KMagneticFluxDensityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KMagneticFluxDensityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new magnetic flux density value with [value] (teslas) scaled by [factor]. Backs
     * number-times-unit construction (`10 of milli.teslas`).
     */
    override fun scaledBy(factor: Double): KMagneticFluxDensityUnitInstance =
        magneticFluxDensityInstanceOf(value * factor)

    /**
     * Adds two magnetic flux densities, automatically converting between different
     * [KMagneticFluxDensityUnit]s since both operands are always normalized to
     * [KMagneticFluxDensityUnit.BASE] (teslas) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of teslas) + (10000 of gauss)).value // 2.0
     * ```
     */
    override operator fun plus(other: KMagneticFluxDensityUnitInstance): KMagneticFluxDensityUnitInstance =
        magneticFluxDensityInstanceOf(value + other.value)

    /** Subtracts two magnetic flux densities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KMagneticFluxDensityUnitInstance): KMagneticFluxDensityUnitInstance =
        magneticFluxDensityInstanceOf(value - other.value)

    /**
     * Multiplies two magnetic flux densities, producing a new [KMixedUnitInstance] (no longer a "pure"
     * magnetic flux density).
     */
    operator fun times(other: KMagneticFluxDensityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two magnetic flux densities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KMagneticFluxDensityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two magnetic flux densities by their normalized [value] (teslas). */
    override operator fun compareTo(other: KMagneticFluxDensityUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two magnetic flux densities are equal iff they represent
     * the same flux density (e.g. `(1 of teslas) == (10000 of gauss)`).
     */
    override fun equals(other: Any?): Boolean = other is KMagneticFluxDensityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"3.0 T"`. */
    override fun toString(): String = "$value ${KMagneticFluxDensityUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// The tesla's SI definition uses the *kilogram* as its mass dimension (`1 T = 1 kg·s⁻²·A⁻¹`), whereas the
// mass group's base unit is the gram. The canonical normal form is therefore stored with the mass group's
// base term (gram), and this factor bridges a gram-based canonical product to teslas.
private val TESLA_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KMagneticFluxDensityUnitInstance] from a value already expressed in teslas
 * ([KMagneticFluxDensityUnit.BASE]).
 *
 * This is the single creation source that every magnetic flux density decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the three terms `mass¹`, `time⁻²`,
 * `current⁻¹` (each in its group's base unit).
 */
internal fun magneticFluxDensityInstanceOf(teslas: Double): KMagneticFluxDensityUnitInstance =
    KMagneticFluxDensityUnitInstance(
        KMixedUnitInstance(
            teslas,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KTimeUnit.BASE, -2),
                KUnitTerm(KElectricCurrentUnit.BASE, -1),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" magnetic flux density value, as long as it matches the canonical
 * normal form: exactly one [KMassUnit] term at exponent `+1`, one [KTimeUnit] term at exponent `-2` and
 * one [KElectricCurrentUnit] term at exponent `-1` (order independent). The terms are normalized over
 * their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the resulting flux density is expressed in
 * teslas regardless of which concrete mass/time/current units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass¹·time⁻²·current⁻¹` magnetic
 * flux density.
 *
 * Example:
 * ```kotlin
 * val raw = (3 of kilo.grams) / ((seconds pow 2) * amperes)
 * raw.toMagneticFluxDensity().value // 3.0
 * ```
 */
fun KMixedUnitInstance.toMagneticFluxDensity(): KMagneticFluxDensityUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -2 }
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == -1 }
    check(units.size == 3 && massTerm != null && timeTerm != null && currentTerm != null) {
        "KMixedUnitInstance $this does not represent a pure magnetic flux density (expected KMassUnit^1, KTimeUnit^-2 and KElectricCurrentUnit^-1)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue *
            Math.pow(timeTerm.unit.baseValue, -2.0) *
            Math.pow(currentTerm.unit.baseValue, -1.0)
    return magneticFluxDensityInstanceOf(gramBaseProduct / TESLA_MASS_REFERENCE)
}
