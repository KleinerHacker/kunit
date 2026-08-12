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

package org.pcsoft.framework.kunit.mechanic.inertia

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * The fixed factor between the group's named base unit (kg·m²) and the raw component storage (`g·m²`):
 * 1 kg·m² = 1000 g·m². It exists because the mass component of this library is normalized to grams, not
 * kilograms. The [KInertiaUnitInstance.value] is always the raw component value (needed so the generic
 * engine's `*`/`/` stay correct); readings in kg·m² divide by this factor.
 */
internal const val KGM2_IN_BASE: Double = 1000.0

/**
 * Wraps a [KMixedUnitInstance] representing a **moment of inertia**, i.e. exactly two terms in the
 * canonical normal form - [KMassUnit.BASE] (gram) at exponent `+1` and [KDistanceUnit.BASE] (meter) at
 * exponent `+2` (`g·m²`). The [value] is the raw component value; readings in kg·m²
 * ([KInertiaUnit.BASE]) divide by [KGM2_IN_BASE].
 *
 * The moment of inertia is a *constructed* unit group produced by the cross-group operators in
 * `KInertiaUnitOperators.kt` (`mass * area`), the bare tokens in `KInertiaUnitBareValues.kt` (e.g.
 * `2 of kilogramMetersSquared`), the prefixed templates in `KInertiaUnitExtensions.kt`, or [toInertia]
 * (which also accepts the native form `mass * length²`).
 *
 * Example:
 * ```kotlin
 * val j = (2 of kilo.grams) * ((3 of meters) * (3 of meters)) // 18 kg·m²
 * j into kilogramMetersSquared                                // 18.0
 * ```
 */
class KInertiaUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KInertiaUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new moment of inertia with the component [value] scaled by [factor]. Backs
     * number-times-unit construction (`2 of kilogramMetersSquared`).
     */
    override fun scaledBy(factor: Double): KInertiaUnitInstance = inertiaInstanceOf(value * factor)

    /**
     * Adds two moments of inertia, automatically converting between different [KInertiaUnit]s since both
     * operands are always normalized to the same component base internally.
     */
    override operator fun plus(other: KInertiaUnitInstance): KInertiaUnitInstance =
        inertiaInstanceOf(value + other.value)

    /** Subtracts two moments of inertia. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KInertiaUnitInstance): KInertiaUnitInstance =
        inertiaInstanceOf(value - other.value)

    /** Multiplies two moments of inertia, producing a new [KMixedUnitInstance]. */
    operator fun times(other: KInertiaUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two moments of inertia, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KInertiaUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two moments of inertia by their normalized component [value]. */
    override operator fun compareTo(other: KInertiaUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized component [value]. */
    override fun equals(other: Any?): Boolean = other is KInertiaUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in kg·m², e.g. `"18.0 kg*m^2"`. */
    override fun toString(): String = "${renderDouble(value / KGM2_IN_BASE)} ${KInertiaUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KInertiaUnitInstance] from a raw component value (`g·m²`) - the single normalizing creation
 * source every decomposition funnels into.
 */
internal fun inertiaInstanceOf(componentValue: Double): KInertiaUnitInstance =
    KInertiaUnitInstance(
        KMixedUnitInstance(
            componentValue,
            listOf(KUnitTerm(KMassUnit.BASE, 1), KUnitTerm(KDistanceUnit.BASE, 2)),
        ),
    )

/** Builds a value-1 [KInertiaUnitInstance] for the given [unit] (its [KInertiaUnit.baseValue] kg·m²). */
internal fun inertiaOfUnit(unit: KInertiaUnit): KInertiaUnitInstance = inertiaInstanceOf(unit.baseValue * KGM2_IN_BASE)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" moment of inertia, as long as it matches the canonical normal form:
 * exactly one [KMassUnit] term at exponent `+1` and one [KDistanceUnit] term at exponent `+2` (order
 * independent). This is also the native decomposition `mass * length²`.
 *
 * @throws IllegalStateException if this instance is not a mass·length² moment of inertia.
 */
fun KMixedUnitInstance.toInertia(): KInertiaUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    check(units.size == 2 && massTerm != null && distanceTerm != null) {
        "KMixedUnitInstance $this does not represent a pure moment of inertia (expected one KMassUnit^1 and one KDistanceUnit^2 term)"
    }
    val component = value * massTerm.unit.baseValue * distanceTerm.unit.baseValue * distanceTerm.unit.baseValue
    return inertiaInstanceOf(component)
}
