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

package org.pcsoft.framework.kunit.mechanic.massflow

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * The fixed factor between the group's named base unit (kg/s) and the raw component storage (`g·s⁻¹`):
 * 1 kg/s = 1000 g/s. It exists because the mass component of this library is normalized to grams, not
 * kilograms. The [KMassFlowUnitInstance.value] is always the raw component value; readings in kg/s divide
 * by this factor.
 */
internal const val KGS_IN_BASE: Double = 1000.0

/**
 * Wraps a [KMixedUnitInstance] representing a **mass flow rate**, i.e. exactly two terms in the canonical
 * normal form - [KMassUnit.BASE] (gram) at exponent `+1` and [KTimeUnit.BASE] (second) at exponent `-1`
 * (`g·s⁻¹`). The [value] is the raw component value; readings in kg/s ([KMassFlowUnit.BASE]) divide by
 * [KGS_IN_BASE].
 *
 * Mass flow is a *constructed* unit group with two equivalent decompositions - `mass / time` and
 * `density * volumeflow` - both of which funnel into [massFlowInstanceOf]. It is produced by the
 * cross-group operators in `KMassFlowUnitOperators.kt`, the bare tokens in `KMassFlowUnitBareValues.kt`
 * (e.g. `5 of tonnesPerHour`), the prefixed templates in `KMassFlowUnitExtensions.kt`, or [toMassFlow].
 *
 * Example:
 * ```kotlin
 * val m = (2 of kilo.grams) / (1 of seconds) // KMassFlowUnitInstance, 2 kg/s
 * m into tonnesPerHour                       // 7.2
 * ```
 */
class KMassFlowUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KMassFlowUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new mass flow with the component [value] scaled by [factor]. Backs number-times-unit
     * construction (`5 of tonnesPerHour`).
     */
    override fun scaledBy(factor: Double): KMassFlowUnitInstance = massFlowInstanceOf(value * factor)

    /**
     * Adds two mass flows, automatically converting between different [KMassFlowUnit]s since both operands
     * are always normalized to the same component base internally.
     */
    override operator fun plus(other: KMassFlowUnitInstance): KMassFlowUnitInstance =
        massFlowInstanceOf(value + other.value)

    /** Subtracts two mass flows. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KMassFlowUnitInstance): KMassFlowUnitInstance =
        massFlowInstanceOf(value - other.value)

    /** Multiplies two mass flows, producing a new [KMixedUnitInstance]. */
    operator fun times(other: KMassFlowUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two mass flows, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KMassFlowUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two mass flows by their normalized component [value]. */
    override operator fun compareTo(other: KMassFlowUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized component [value] (e.g. `(3.6 of tonnesPerHour) == (1 of kilogramsPerSecond)`). */
    override fun equals(other: Any?): Boolean = other is KMassFlowUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in kg/s, e.g. `"2.0 kg/s"`. */
    override fun toString(): String = "${value / KGS_IN_BASE} ${KMassFlowUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KMassFlowUnitInstance] from a raw component value (`g·s⁻¹`) - the single normalizing creation
 * source every decomposition funnels into.
 */
internal fun massFlowInstanceOf(componentValue: Double): KMassFlowUnitInstance =
    KMassFlowUnitInstance(
        KMixedUnitInstance(
            componentValue,
            listOf(KUnitTerm(KMassUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -1)),
        ),
    )

/** Builds a value-1 [KMassFlowUnitInstance] for the given [unit] (its [KMassFlowUnit.baseValue] kg/s). */
internal fun massFlowOfUnit(unit: KMassFlowUnit): KMassFlowUnitInstance =
    massFlowInstanceOf(unit.baseValue * KGS_IN_BASE)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" mass flow, as long as it matches the canonical normal form: exactly
 * one [KMassUnit] term at exponent `+1` and one [KTimeUnit] term at exponent `-1` (order independent).
 *
 * @throws IllegalStateException if this instance is not a mass-per-time flow.
 */
fun KMixedUnitInstance.toMassFlow(): KMassFlowUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -1 }
    check(units.size == 2 && massTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure mass flow (expected one KMassUnit^1 and one KTimeUnit^-1 term)"
    }
    return massFlowInstanceOf(value * massTerm.unit.baseValue / timeTerm.unit.baseValue)
}
