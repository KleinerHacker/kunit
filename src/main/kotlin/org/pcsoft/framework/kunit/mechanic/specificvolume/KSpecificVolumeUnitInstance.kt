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

package org.pcsoft.framework.kunit.mechanic.specificvolume

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * The fixed factor between the group's named base unit (m³/kg) and the raw component storage (`m³·g⁻¹`):
 * 1 m³/kg = 0.001 m³/g. It exists because the mass component of this library is normalized to grams, not
 * kilograms. The [KSpecificVolumeUnitInstance.value] is always the raw component value; readings in m³/kg
 * divide by this factor.
 */
internal const val M3KG_IN_BASE: Double = 1.0e-3

/**
 * Wraps a [KMixedUnitInstance] representing a **specific volume**, i.e. exactly two terms in the canonical
 * normal form - [KDistanceUnit.BASE] (meter) at exponent `+3` and [KMassUnit.BASE] (gram) at exponent `-1`
 * (`m³·g⁻¹`). The [value] is the raw component value; readings in m³/kg ([KSpecificVolumeUnit.BASE]) divide
 * by [M3KG_IN_BASE].
 *
 * The specific volume is the **reciprocal of the density**, which is why `1 / density` yields a typed
 * specific volume (and vice versa) - see `KSpecificVolumeUnitOperators.kt`. It is a *constructed* unit
 * group produced by those operators, the bare tokens in `KSpecificVolumeUnitBareValues.kt` (e.g.
 * `2 of litersPerKilogram`), the prefixed templates in `KSpecificVolumeUnitExtensions.kt`, or
 * [toSpecificVolume].
 *
 * Example:
 * ```kotlin
 * val v = (2 of liters) / (1 of kilo.grams) // KSpecificVolumeUnitInstance
 * v into litersPerKilogram                  // 2.0
 * ```
 */
class KSpecificVolumeUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KSpecificVolumeUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new specific volume with the component [value] scaled by [factor]. Backs number-times-unit
     * construction (`2 of litersPerKilogram`).
     */
    override fun scaledBy(factor: Double): KSpecificVolumeUnitInstance = specificVolumeInstanceOf(value * factor)

    /**
     * Adds two specific volumes, automatically converting between different [KSpecificVolumeUnit]s since
     * both operands are always normalized to the same component base internally.
     */
    override operator fun plus(other: KSpecificVolumeUnitInstance): KSpecificVolumeUnitInstance =
        specificVolumeInstanceOf(value + other.value)

    /** Subtracts two specific volumes. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KSpecificVolumeUnitInstance): KSpecificVolumeUnitInstance =
        specificVolumeInstanceOf(value - other.value)

    /** Multiplies two specific volumes, producing a new [KMixedUnitInstance]. */
    operator fun times(other: KSpecificVolumeUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two specific volumes, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KSpecificVolumeUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two specific volumes by their normalized component [value]. */
    override operator fun compareTo(other: KSpecificVolumeUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized component [value] (e.g. `(1 of litersPerKilogram) == (1 of cubicCentimetersPerGram)`). */
    override fun equals(other: Any?): Boolean = other is KSpecificVolumeUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in m³/kg, e.g. `"0.002 m^3/kg"`. */
    override fun toString(): String = "${value / M3KG_IN_BASE} ${KSpecificVolumeUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KSpecificVolumeUnitInstance] from a raw component value (`m³·g⁻¹`) - the single normalizing
 * creation source every decomposition funnels into.
 */
internal fun specificVolumeInstanceOf(componentValue: Double): KSpecificVolumeUnitInstance =
    KSpecificVolumeUnitInstance(
        KMixedUnitInstance(
            componentValue,
            listOf(KUnitTerm(KDistanceUnit.BASE, 3), KUnitTerm(KMassUnit.BASE, -1)),
        ),
    )

/**
 * Builds a value-1 [KSpecificVolumeUnitInstance] for the given [unit] (its
 * [KSpecificVolumeUnit.baseValue] m³/kg).
 */
internal fun specificVolumeOfUnit(unit: KSpecificVolumeUnit): KSpecificVolumeUnitInstance =
    specificVolumeInstanceOf(unit.baseValue * M3KG_IN_BASE)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" specific volume, as long as it matches the canonical normal form:
 * exactly one [KDistanceUnit] term at exponent `+3` and one [KMassUnit] term at exponent `-1` (order
 * independent).
 *
 * @throws IllegalStateException if this instance is not a volume-per-mass specific volume.
 */
fun KMixedUnitInstance.toSpecificVolume(): KSpecificVolumeUnitInstance {
    val volumeTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 3 }
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == -1 }
    check(units.size == 2 && volumeTerm != null && massTerm != null) {
        "KMixedUnitInstance $this does not represent a pure specific volume (expected one KDistanceUnit^3 and one KMassUnit^-1 term)"
    }
    val base = volumeTerm.unit.baseValue
    return specificVolumeInstanceOf(value * base * base * base / massTerm.unit.baseValue)
}
