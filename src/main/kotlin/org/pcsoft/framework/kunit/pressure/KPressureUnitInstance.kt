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

package org.pcsoft.framework.kunit.pressure

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.mass.KMassUnit
import org.pcsoft.framework.kunit.time.KTimeUnit

/**
 * The fixed factor between the group's named base unit (pascal) and the raw component storage
 * (`g·m⁻¹·s⁻²`): 1 Pa = 1 kg/(m·s²) = 1000 g/(m·s²). It exists because the mass component of this
 * library is normalized to grams, not kilograms. The [KPressureUnitInstance.value] is always the raw
 * component value; readings in pascals divide by this factor.
 */
internal const val PA_IN_BASE: Double = 1000.0

/**
 * Wraps a [KMixedUnitInstance] representing a pressure, i.e. exactly three terms - [KMassUnit.BASE]
 * (gram) at exponent `+1`, [KDistanceUnit.BASE] (meter) at exponent `-1` and [KTimeUnit.BASE] (second)
 * at exponent `-2` (`g·m⁻¹·s⁻²`). The [value] is the raw component value; readings in pascals
 * ([KPressureUnit.BASE]) divide by [PA_IN_BASE].
 *
 * Pressure is a *constructed* unit group produced by the cross-group operators in
 * `KPressureUnitOperators.kt` (e.g. `force / area`), the bare tokens in `KPressureUnitBareValues.kt`
 * (e.g. `2 of bars`), the prefixed templates in `KPressureUnitExtensions.kt` (e.g. `kilo.pascals`), or
 * [toPressure].
 *
 * Example:
 * ```kotlin
 * val p = (100 of newtons) / ((2 of meters) * (1 of meters)) // KPressureUnitInstance, 50 Pa
 * p into bars                                                 // 0.0005
 * ```
 */
class KPressureUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KPressureUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new pressure value with the component [value] scaled by [factor]. Backs number-times-unit
     * construction (`2 of bars`).
     */
    override fun scaledBy(factor: Double): KPressureUnitInstance = pressureUnitInstanceOf(value * factor)

    /**
     * Adds two pressures, automatically converting between different [KPressureUnit]s since both operands
     * are always normalized to the same component base internally.
     */
    override operator fun plus(other: KPressureUnitInstance): KPressureUnitInstance = KPressureUnitInstance(instance + other.instance)

    /** Subtracts two pressures. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KPressureUnitInstance): KPressureUnitInstance = KPressureUnitInstance(instance - other.instance)

    /** Multiplies two pressures, producing a new [KMixedUnitInstance] (no longer a "pure" pressure). */
    operator fun times(other: KPressureUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two pressures, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KPressureUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two pressures by their normalized component [value]. */
    override operator fun compareTo(other: KPressureUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized component [value]. */
    override fun equals(other: Any?): Boolean = other is KPressureUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in pascals, e.g. `"50.0 Pa"`. */
    override fun toString(): String = "${value / PA_IN_BASE} ${KPressureUnit.BASE.symbol}"
}

/**
 * Converts this mixed unit to a "pure" pressure value, as long as it consists of exactly three terms:
 * one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent `-1` and one [KTimeUnit]
 * term at exponent `-2` (order independent). The terms are normalized to their component base units.
 *
 * @throws IllegalStateException if this instance is not a mass·length⁻¹·time⁻² pressure.
 */
fun KMixedUnitInstance.toPressure(): KPressureUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val lengthTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -2 }
    check(units.size == 3 && massTerm != null && lengthTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure pressure (expected one KMassUnit^1, one KDistanceUnit^-1 and one KTimeUnit^-2 term)"
    }
    val component = value * massTerm.unit.baseValue * Math.pow(lengthTerm.unit.baseValue, -1.0) * Math.pow(timeTerm.unit.baseValue, -2.0)
    return pressureUnitInstanceOf(component)
}

/**
 * Builds a [KPressureUnitInstance] from a raw component value (`g·m⁻¹·s⁻²`). To build from a pascal
 * reading, use [pressureOfUnit] or the tokens in `KPressureUnitBareValues.kt`.
 */
internal fun pressureUnitInstanceOf(componentValue: Double): KPressureUnitInstance =
    KPressureUnitInstance(
        KMixedUnitInstance(
            componentValue,
            listOf(KUnitTerm(KMassUnit.BASE, 1), KUnitTerm(KDistanceUnit.BASE, -1), KUnitTerm(KTimeUnit.BASE, -2)),
        ),
    )

/** Builds a value-1 [KPressureUnitInstance] for the given [unit] (its [KPressureUnit.baseValue] pascals). */
internal fun pressureOfUnit(unit: KPressureUnit): KPressureUnitInstance = pressureUnitInstanceOf(unit.baseValue * PA_IN_BASE)
