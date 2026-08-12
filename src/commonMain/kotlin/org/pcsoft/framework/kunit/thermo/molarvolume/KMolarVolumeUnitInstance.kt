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

package org.pcsoft.framework.kunit.thermo.molarvolume

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **molar volume** (volume per amount of substance), i.e.
 * exactly two terms in the canonical normal form - [KDistanceUnit.BASE] (meter) at exponent `+3` and
 * [KAmountOfSubstanceUnit.BASE] (mole) at exponent `-1` (`m³·mol⁻¹`). Both components are stored in their
 * group's base unit, so the raw component base *is* the named base unit
 * ([KMolarVolumeUnit.CUBIC_METERS_PER_MOLE]) and no bridging factor is needed.
 *
 * Molar volume is a *constructed* unit group with two decompositions, both funnelling into
 * [molarVolumeInstanceOf]:
 * * `volume / amountOfSubstance` (typed operator, see `KMolarVolumeUnitOperators.kt`)
 * * `molarMass / density` (typed operator, same file)
 *
 * Instances are additionally created via the bare tokens in `KMolarVolumeUnitBareValues.kt` (e.g.
 * `22.4 of litersPerMole`), the prefixed templates in `KMolarVolumeUnitExtensions.kt`, or [toMolarVolume].
 *
 * Example:
 * ```kotlin
 * val v = (18.015 of gramsPerMole) / ((1 of kilo.grams) / (1 of liters)) // water: ≈ 18.015 cm³/mol
 * v into cubicCentimetersPerMole                                          // ≈ 18.015
 * ```
 */
class KMolarVolumeUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KMolarVolumeUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new molar volume with [value] (m³/mol) scaled by [factor]. Backs number-times-unit
     * construction (`22.4 of litersPerMole`).
     */
    override fun scaledBy(factor: Double): KMolarVolumeUnitInstance = molarVolumeInstanceOf(value * factor)

    /**
     * Adds two molar volumes, automatically converting between different [KMolarVolumeUnit]s since both
     * operands are always normalized to [KMolarVolumeUnit.BASE] (m³/mol) internally.
     */
    override operator fun plus(other: KMolarVolumeUnitInstance): KMolarVolumeUnitInstance =
        molarVolumeInstanceOf(value + other.value)

    /** Subtracts two molar volumes. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KMolarVolumeUnitInstance): KMolarVolumeUnitInstance =
        molarVolumeInstanceOf(value - other.value)

    /** Multiplies two molar volumes, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KMolarVolumeUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two molar volumes, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KMolarVolumeUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two molar volumes by their normalized [value] (m³/mol). */
    override operator fun compareTo(other: KMolarVolumeUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two molar volumes are equal iff they represent the same
     * quantity (e.g. `(1 of litersPerMole) == (1000 of cubicCentimetersPerMole)`).
     */
    override fun equals(other: Any?): Boolean = other is KMolarVolumeUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in m³/mol, e.g. `"0.0224 m^3/mol"`. */
    override fun toString(): String = "${renderDouble(value)} ${KMolarVolumeUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KMolarVolumeUnitInstance] from a value already expressed in cubic meters per mole
 * ([KMolarVolumeUnit.BASE]).
 *
 * This is the single creation source that every molar volume decomposition must funnel into: it assembles
 * the canonical normal-form [KMixedUnitInstance] with the two terms `distance³` and `substance⁻¹` (each in
 * its group's base unit).
 */
internal fun molarVolumeInstanceOf(cubicMetersPerMole: Double): KMolarVolumeUnitInstance =
    KMolarVolumeUnitInstance(
        KMixedUnitInstance(
            cubicMetersPerMole,
            listOf(
                KUnitTerm(KDistanceUnit.BASE, 3),
                KUnitTerm(KAmountOfSubstanceUnit.BASE, -1),
            ),
        ),
    )

/**
 * Builds a value-1 [KMolarVolumeUnitInstance] for the given [unit] (its [KMolarVolumeUnit.baseValue]
 * m³/mol).
 */
internal fun molarVolumeOfUnit(unit: KMolarVolumeUnit): KMolarVolumeUnitInstance =
    molarVolumeInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" molar volume, as long as it matches the canonical molar volume
 * normal form: exactly one [KDistanceUnit] term at exponent `+3` and one [KAmountOfSubstanceUnit] term at
 * exponent `-1` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the result is expressed in m³/mol regardless of which
 * concrete units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `distance³·substance⁻¹` molar volume.
 */
fun KMixedUnitInstance.toMolarVolume(): KMolarVolumeUnitInstance {
    val volumeTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 3 }
    val substanceTerm = units.singleOrNull { it.unit is KAmountOfSubstanceUnit && it.exponent == -1 }
    check(units.size == 2 && volumeTerm != null && substanceTerm != null) {
        "KMixedUnitInstance $this does not represent a pure molar volume " +
                "(expected one KDistanceUnit^3 and one KAmountOfSubstanceUnit^-1 term)"
    }
    val base = volumeTerm.unit.baseValue
    return molarVolumeInstanceOf(value * base * base * base / substanceTerm.unit.baseValue)
}
