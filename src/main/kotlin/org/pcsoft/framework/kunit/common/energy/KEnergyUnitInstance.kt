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

package org.pcsoft.framework.kunit.common.energy

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit

/**
 * Wraps a [KMixedUnitInstance] representing an **energy** (work, heat), i.e. exactly three terms in the
 * canonical normal form `mass¹ · distance² · time⁻²` (`kg·m²·s⁻²`). The [value] is always normalized
 * internally to joules ([KEnergyUnit.BASE]), regardless of which [KEnergyUnit], SI [KUnitPrefix], or
 * mass/length/time combination it was constructed from.
 *
 * Energy is a *constructed* unit group: unlike a single-term wrapper it holds a three-term instance.
 * Instances are created via the bare tokens in `KEnergyUnitBareValues.kt` (e.g. `5 of joules`), the prefixed
 * templates in `KEnergyUnitExtensions.kt` (e.g. `2 of kilo.joules`), or [toEnergy] on a canonical
 * `mass·length²·time⁻²` expression.
 *
 * Example:
 * ```kotlin
 * val w = (2 of kilo.watts) * (3 of hours)   // 2.16e7 J (= 6 kWh)
 * w.value                                    // 2.16e7 (normalized to joules)
 * w into kilo.joules                         // 21600.0
 * ```
 */
class KEnergyUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KEnergyUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new energy value with [value] (joules) scaled by [factor]. Backs number-times-unit
     * construction (`10 of kilo.joules`).
     */
    override fun scaledBy(factor: Double): KEnergyUnitInstance = energyInstanceOf(value * factor)

    /**
     * Adds two energies, automatically converting between different [KEnergyUnit]s since both operands are
     * always normalized to [KEnergyUnit.BASE] (joules) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of kilo.joules) + (500 of joules)).value // 1500.0
     * ```
     */
    override operator fun plus(other: KEnergyUnitInstance): KEnergyUnitInstance = energyInstanceOf(value + other.value)

    /** Subtracts two energies. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KEnergyUnitInstance): KEnergyUnitInstance = energyInstanceOf(value - other.value)

    /** Multiplies two energies, producing a new [KMixedUnitInstance] (no longer a "pure" energy). */
    operator fun times(other: KEnergyUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two energies, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KEnergyUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two energies by their normalized [value] (joules). */
    override operator fun compareTo(other: KEnergyUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two energies are equal iff they represent the same energy
     * (e.g. `(1 of kilo.joules) == (1000 of joules)`).
     */
    override fun equals(other: Any?): Boolean = other is KEnergyUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"10.0 J"`. */
    override fun toString(): String = "$value ${KEnergyUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// The joule's SI definition uses the *kilogram* as its mass dimension (`1 J = 1 kg·m²·s⁻²`), whereas the
// mass group's base unit is the gram. The canonical normal form is therefore stored with the mass group's
// base term (gram), and this factor bridges a gram-based canonical product to joules. The mass exponent is
// *positive* (+1) here, so the bridge divides (like the ohm).
internal val JOULE_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KEnergyUnitInstance] from a value already expressed in joules ([KEnergyUnit.BASE]).
 *
 * This is the single creation source that every energy decomposition must funnel into: it assembles the
 * canonical normal-form [KMixedUnitInstance] with the three terms `mass¹`, `distance²`, `time⁻²` (each in
 * its group's base unit).
 */
internal fun energyInstanceOf(joules: Double): KEnergyUnitInstance =
    KEnergyUnitInstance(
        KMixedUnitInstance(
            joules,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 2),
                KUnitTerm(KTimeUnit.BASE, -2),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" energy value, as long as it matches the canonical energy normal form:
 * exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent `+2` and one
 * [KTimeUnit] term at exponent `-2` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the resulting energy is expressed in joules regardless
 * of which concrete mass/length/time units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass¹·distance²·time⁻²` energy.
 *
 * Example:
 * ```kotlin
 * val raw = 60 of (kilo.grams * (meters pow 2)) / (seconds pow 2)
 * raw.toEnergy().value // 60.0
 * ```
 */
fun KMixedUnitInstance.toEnergy(): KEnergyUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -2 }
    check(units.size == 3 && massTerm != null && distanceTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure energy (expected KMassUnit^1, KDistanceUnit^2 and KTimeUnit^-2)"
    }
    val gramBaseProduct = value *
        massTerm.unit.baseValue *
        Math.pow(distanceTerm.unit.baseValue, 2.0) *
        Math.pow(timeTerm.unit.baseValue, -2.0)
    return energyInstanceOf(gramBaseProduct / JOULE_MASS_REFERENCE)
}
