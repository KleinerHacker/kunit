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

package org.pcsoft.framework.kunit.mechanic.lineforce

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * The fixed factor between the group's named base unit (N/m) and the raw component storage (`g·s⁻²`):
 * 1 N/m = 1 kg/s² = 1000 g/s². It exists because the mass component of this library is normalized to grams,
 * not kilograms. The [KLineForceUnitInstance.value] is always the raw component value; readings in N/m
 * divide by this factor.
 */
internal const val NM_IN_BASE: Double = 1000.0

/**
 * Wraps a [KMixedUnitInstance] representing a **force per length** - read either as a **surface tension**
 * or as a **stiffness / spring rate** - i.e. exactly two terms in the canonical normal form:
 * [KMassUnit.BASE] (gram) at exponent `+1` and [KTimeUnit.BASE] (second) at exponent `-2` (`g·s⁻²`). The
 * [value] is the raw component value; readings in N/m ([KLineForceUnit.BASE]) divide by [NM_IN_BASE].
 *
 * The group has two equivalent decompositions, both funnelling into [lineForceInstanceOf]:
 * * `force / length` - the stiffness / line-load reading, and
 * * `energy / area` - the surface-tension reading (`J/m² = N/m`).
 *
 * It is produced by the cross-group operators in `KLineForceUnitOperators.kt`, the bare tokens in
 * `KLineForceUnitBareValues.kt` (e.g. `72 of dynesPerCentimeter`), the prefixed templates in
 * `KLineForceUnitExtensions.kt`, or [toLineForce].
 *
 * Example:
 * ```kotlin
 * val k = (200 of newtons) / (0.05 of meters) // stiffness, 4000 N/m
 * val sigma = 72 of milli.newtonsPerMeter     // surface tension of water at 25 °C
 * ```
 */
class KLineForceUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KLineForceUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new force-per-length value with the component [value] scaled by [factor]. Backs
     * number-times-unit construction (`72 of dynesPerCentimeter`).
     */
    override fun scaledBy(factor: Double): KLineForceUnitInstance = lineForceInstanceOf(value * factor)

    /**
     * Adds two force-per-length values, automatically converting between different [KLineForceUnit]s since
     * both operands are always normalized to the same component base internally.
     */
    override operator fun plus(other: KLineForceUnitInstance): KLineForceUnitInstance =
        lineForceInstanceOf(value + other.value)

    /** Subtracts two force-per-length values. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KLineForceUnitInstance): KLineForceUnitInstance =
        lineForceInstanceOf(value - other.value)

    /** Multiplies two force-per-length values, producing a new [KMixedUnitInstance]. */
    operator fun times(other: KLineForceUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two force-per-length values, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KLineForceUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two force-per-length values by their normalized component [value]. */
    override operator fun compareTo(other: KLineForceUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized component [value] (e.g. `(1 of dynesPerCentimeter) == (1 of milli.newtonsPerMeter)`). */
    override fun equals(other: Any?): Boolean = other is KLineForceUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in N/m, e.g. `"4000.0 N/m"`. */
    override fun toString(): String = "${renderDouble(value / NM_IN_BASE)} ${KLineForceUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KLineForceUnitInstance] from a raw component value (`g·s⁻²`) - the single normalizing creation
 * source both decompositions (`force / length`, `energy / area`) funnel into.
 */
internal fun lineForceInstanceOf(componentValue: Double): KLineForceUnitInstance =
    KLineForceUnitInstance(
        KMixedUnitInstance(
            componentValue,
            listOf(KUnitTerm(KMassUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -2)),
        ),
    )

/** Builds a value-1 [KLineForceUnitInstance] for the given [unit] (its [KLineForceUnit.baseValue] N/m). */
internal fun lineForceOfUnit(unit: KLineForceUnit): KLineForceUnitInstance =
    lineForceInstanceOf(unit.baseValue * NM_IN_BASE)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" force-per-length value, as long as it matches the canonical normal
 * form: exactly one [KMassUnit] term at exponent `+1` and one [KTimeUnit] term at exponent `-2` (order
 * independent).
 *
 * @throws IllegalStateException if this instance is not a mass·time⁻² force per length.
 */
fun KMixedUnitInstance.toLineForce(): KLineForceUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -2 }
    check(units.size == 2 && massTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure force per length (expected one KMassUnit^1 and one KTimeUnit^-2 term)"
    }
    val component = value * massTerm.unit.baseValue / (timeTerm.unit.baseValue * timeTerm.unit.baseValue)
    return lineForceInstanceOf(component)
}
