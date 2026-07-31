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

package org.pcsoft.framework.kunit.mechanic.momentum

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * The fixed factor between the group's named base unit (kg·m/s) and the raw component storage
 * (`g·m·s⁻¹`): 1 kg·m/s = 1000 g·m/s. It exists because the mass component of this library is normalized
 * to grams, not kilograms. The [KMomentumUnitInstance.value] is always the raw component value (needed so
 * the generic engine's `*`/`/` stay correct); readings in kg·m/s divide by this factor.
 */
internal const val KGMS_IN_BASE: Double = 1000.0

/**
 * Wraps a [KMixedUnitInstance] representing a **momentum** (and equally an **impulse**), i.e. exactly
 * three terms in the canonical normal form - [KMassUnit.BASE] (gram) at exponent `+1`,
 * [KDistanceUnit.BASE] (meter) at exponent `+1` and [KTimeUnit.BASE] (second) at exponent `-1`
 * (`g·m·s⁻¹`). The [value] is the raw component value; readings in kg·m/s ([KMomentumUnit.BASE]) divide by
 * [KGMS_IN_BASE].
 *
 * Momentum is a *constructed* unit group with two equivalent decompositions - `mass * speed` and
 * `force * time` (the impulse form) - both of which funnel into [momentumInstanceOf]. It is produced by
 * the cross-group operators in `KMomentumUnitOperators.kt`, the bare tokens in
 * `KMomentumUnitBareValues.kt` (e.g. `5 of newtonSeconds`), the prefixed templates in
 * `KMomentumUnitExtensions.kt` (e.g. `kilo.newtonSeconds`), or [toMomentum].
 *
 * Example:
 * ```kotlin
 * val p = (2 of kilo.grams) * (3 of meters / seconds) // KMomentumUnitInstance
 * p into kilogramMetersPerSecond                      // 6.0
 * p into newtonSeconds                                // 6.0 (same dimension)
 * ```
 */
class KMomentumUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KMomentumUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new momentum with the component [value] scaled by [factor]. Backs number-times-unit
     * construction (`5 of newtonSeconds`).
     */
    override fun scaledBy(factor: Double): KMomentumUnitInstance = momentumInstanceOf(value * factor)

    /**
     * Adds two momenta, automatically converting between different [KMomentumUnit]s since both operands
     * are always normalized to the same component base internally.
     */
    override operator fun plus(other: KMomentumUnitInstance): KMomentumUnitInstance =
        momentumInstanceOf(value + other.value)

    /** Subtracts two momenta. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KMomentumUnitInstance): KMomentumUnitInstance =
        momentumInstanceOf(value - other.value)

    /** Multiplies two momenta, producing a new [KMixedUnitInstance] (no longer a "pure" momentum). */
    operator fun times(other: KMomentumUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two momenta, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KMomentumUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two momenta by their normalized component [value]. */
    override operator fun compareTo(other: KMomentumUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized component [value] (e.g. `(1 of newtonSeconds) == (1 of kilogramMetersPerSecond)`). */
    override fun equals(other: Any?): Boolean = other is KMomentumUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in kg·m/s, e.g. `"6.0 kg*m/s"`. */
    override fun toString(): String = "${value / KGMS_IN_BASE} ${KMomentumUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KMomentumUnitInstance] from a raw component value (`g·m·s⁻¹`) - the single normalizing
 * creation source every decomposition (`mass * speed`, `force * time`, the native form) funnels into. To
 * build from a kg·m/s reading, use [momentumOfUnit] or the tokens in `KMomentumUnitBareValues.kt`.
 */
internal fun momentumInstanceOf(componentValue: Double): KMomentumUnitInstance =
    KMomentumUnitInstance(
        KMixedUnitInstance(
            componentValue,
            listOf(KUnitTerm(KMassUnit.BASE, 1), KUnitTerm(KDistanceUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -1)),
        ),
    )

/** Builds a value-1 [KMomentumUnitInstance] for the given [unit] (its [KMomentumUnit.baseValue] kg·m/s). */
internal fun momentumOfUnit(unit: KMomentumUnit): KMomentumUnitInstance =
    momentumInstanceOf(unit.baseValue * KGMS_IN_BASE)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" momentum, as long as it matches the canonical normal form: exactly
 * one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent `+1` and one [KTimeUnit]
 * term at exponent `-1` (order independent). The terms are normalized to their component base units.
 *
 * @throws IllegalStateException if this instance is not a mass·length·time⁻¹ momentum.
 */
fun KMixedUnitInstance.toMomentum(): KMomentumUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val lengthTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -1 }
    check(units.size == 3 && massTerm != null && lengthTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure momentum (expected one KMassUnit^1, one KDistanceUnit^1 and one KTimeUnit^-1 term)"
    }
    val component = value * massTerm.unit.baseValue * lengthTerm.unit.baseValue / timeTerm.unit.baseValue
    return momentumInstanceOf(component)
}
