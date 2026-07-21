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

package org.pcsoft.framework.kunit.force

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.mass.KMassUnit
import org.pcsoft.framework.kunit.time.KTimeUnit

/**
 * The fixed factor between the group's named base unit (newton) and the raw component storage
 * (`g·m·s⁻²`): 1 N = 1 kg·m/s² = 1000 g·m/s². It exists because the mass component of this library is
 * normalized to grams, not kilograms. The [KForceUnitInstance.value] is always the raw component value
 * (`g·m·s⁻²`, needed so the generic engine's `*`/`/` stay correct); readings in newtons divide by this
 * factor.
 */
internal const val N_IN_BASE: Double = 1000.0

/**
 * Wraps a [KMixedUnitInstance] representing a force, i.e. exactly three terms - [KMassUnit.BASE] (gram)
 * at exponent `+1`, [KDistanceUnit.BASE] (meter) at exponent `+1` and [KTimeUnit.BASE] (second) at
 * exponent `-2` (`g·m·s⁻²`). The [value] is the raw component value; readings in newtons
 * ([KForceUnit.BASE]) divide by [N_IN_BASE].
 *
 * Force is a *constructed* unit group produced by the cross-group operators in `KForceUnitOperators.kt`
 * (e.g. `mass * acceleration`), the bare tokens in `KForceUnitBareValues.kt` (e.g. `10 of newtons`), the
 * prefixed templates in `KForceUnitExtensions.kt` (e.g. `kilo.newtons`, `kilo.ponds`), or [toForce].
 *
 * Example:
 * ```kotlin
 * val f = (2 of kilo.grams) * (3 of standardGravities) // KForceUnitInstance
 * f into newtons                                        // ≈ 58.84
 * ```
 */
class KForceUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KForceUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new force value with the component [value] scaled by [factor]. Backs number-times-unit
     * construction (`10 of newtons`).
     */
    override fun scaledBy(factor: Double): KForceUnitInstance = forceUnitInstanceOf(value * factor)

    /**
     * Adds two forces, automatically converting between different [KForceUnit]s since both operands are
     * always normalized to the same component base internally.
     */
    override operator fun plus(other: KForceUnitInstance): KForceUnitInstance = KForceUnitInstance(instance + other.instance)

    /** Subtracts two forces. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KForceUnitInstance): KForceUnitInstance = KForceUnitInstance(instance - other.instance)

    /** Multiplies two forces, producing a new [KMixedUnitInstance] (no longer a "pure" force). */
    operator fun times(other: KForceUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two forces, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KForceUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two forces by their normalized component [value]. */
    override operator fun compareTo(other: KForceUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized component [value]. */
    override fun equals(other: Any?): Boolean = other is KForceUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in newtons, e.g. `"58.84 N"`. */
    override fun toString(): String = "${value / N_IN_BASE} ${KForceUnit.BASE.symbol}"
}

/**
 * Converts this mixed unit to a "pure" force value, as long as it consists of exactly three terms: one
 * [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent `+1` and one [KTimeUnit] term
 * at exponent `-2` (order independent). The terms are normalized to their component base units.
 *
 * @throws IllegalStateException if this instance is not a mass·length·time⁻² force.
 */
fun KMixedUnitInstance.toForce(): KForceUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val lengthTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -2 }
    check(units.size == 3 && massTerm != null && lengthTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure force (expected one KMassUnit^1, one KDistanceUnit^1 and one KTimeUnit^-2 term)"
    }
    val component = value * massTerm.unit.baseValue * lengthTerm.unit.baseValue * Math.pow(timeTerm.unit.baseValue, -2.0)
    return forceUnitInstanceOf(component)
}

/**
 * Builds a [KForceUnitInstance] from a raw component value (`g·m·s⁻²`). To build from a newton reading,
 * use [forceOfUnit] or the tokens in `KForceUnitBareValues.kt`.
 */
internal fun forceUnitInstanceOf(componentValue: Double): KForceUnitInstance =
    KForceUnitInstance(
        KMixedUnitInstance(
            componentValue,
            listOf(KUnitTerm(KMassUnit.BASE, 1), KUnitTerm(KDistanceUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -2)),
        ),
    )

/** Builds a value-1 [KForceUnitInstance] for the given [unit] (its [KForceUnit.baseValue] newtons). */
internal fun forceOfUnit(unit: KForceUnit): KForceUnitInstance = forceUnitInstanceOf(unit.baseValue * N_IN_BASE)
