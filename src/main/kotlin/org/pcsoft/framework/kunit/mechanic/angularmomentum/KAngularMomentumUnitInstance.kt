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

package org.pcsoft.framework.kunit.mechanic.angularmomentum

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * The fixed factor between the group's named base unit (kg·m²/s) and the raw component storage
 * (`g·m²·s⁻¹`): 1 kg·m²/s = 1000 g·m²/s. It exists because the mass component of this library is
 * normalized to grams, not kilograms. The [KAngularMomentumUnitInstance.value] is always the raw component
 * value; readings in kg·m²/s divide by this factor.
 */
internal const val KGM2S_IN_BASE: Double = 1000.0

/**
 * Wraps a [KMixedUnitInstance] representing an **angular momentum** (equally an **action**), i.e. exactly
 * three terms in the canonical normal form - [KMassUnit.BASE] (gram) at `+1`, [KDistanceUnit.BASE] (meter)
 * at `+2` and [KTimeUnit.BASE] (second) at `-1` (`g·m²·s⁻¹`). The [value] is the raw component value;
 * readings in kg·m²/s ([KAngularMomentumUnit.BASE]) divide by [KGM2S_IN_BASE].
 *
 * The radian does **not** appear in the normal form: it is a dimensionless ratio, so `inertia *
 * angularvelocity` (`kg·m² · rad/s`) and `momentum * length` (`kg·m/s · m`) describe the very same
 * quantity and both funnel into [angularMomentumInstanceOf].
 *
 * Angular momentum is a *constructed* unit group produced by the cross-group operators in
 * `KAngularMomentumUnitOperators.kt`, the bare tokens in `KAngularMomentumUnitBareValues.kt` (e.g.
 * `5 of jouleSeconds`), the prefixed templates in `KAngularMomentumUnitExtensions.kt`, or
 * [toAngularMomentum].
 *
 * Example:
 * ```kotlin
 * val l = (2 of kilogramMetersSquared) * (3 of radiansPerSecond) // 6 kg·m²/s
 * l into kilogramMetersSquaredPerSecond                          // 6.0
 * ```
 */
class KAngularMomentumUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KAngularMomentumUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new angular momentum with the component [value] scaled by [factor]. Backs
     * number-times-unit construction (`5 of jouleSeconds`).
     */
    override fun scaledBy(factor: Double): KAngularMomentumUnitInstance = angularMomentumInstanceOf(value * factor)

    /**
     * Adds two angular momenta, automatically converting between different [KAngularMomentumUnit]s since
     * both operands are always normalized to the same component base internally.
     */
    override operator fun plus(other: KAngularMomentumUnitInstance): KAngularMomentumUnitInstance =
        angularMomentumInstanceOf(value + other.value)

    /** Subtracts two angular momenta. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KAngularMomentumUnitInstance): KAngularMomentumUnitInstance =
        angularMomentumInstanceOf(value - other.value)

    /** Multiplies two angular momenta, producing a new [KMixedUnitInstance]. */
    operator fun times(other: KAngularMomentumUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two angular momenta, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KAngularMomentumUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two angular momenta by their normalized component [value]. */
    override operator fun compareTo(other: KAngularMomentumUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized component [value] (e.g. `(1 of jouleSeconds) == (1 of newtonMeterSeconds)`). */
    override fun equals(other: Any?): Boolean = other is KAngularMomentumUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in kg·m²/s, e.g. `"6.0 kg*m^2/s"`. */
    override fun toString(): String = "${value / KGM2S_IN_BASE} ${KAngularMomentumUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KAngularMomentumUnitInstance] from a raw component value (`g·m²·s⁻¹`) - the single
 * normalizing creation source every decomposition funnels into.
 */
internal fun angularMomentumInstanceOf(componentValue: Double): KAngularMomentumUnitInstance =
    KAngularMomentumUnitInstance(
        KMixedUnitInstance(
            componentValue,
            listOf(KUnitTerm(KMassUnit.BASE, 1), KUnitTerm(KDistanceUnit.BASE, 2), KUnitTerm(KTimeUnit.BASE, -1)),
        ),
    )

/**
 * Builds a value-1 [KAngularMomentumUnitInstance] for the given [unit] (its
 * [KAngularMomentumUnit.baseValue] kg·m²/s).
 */
internal fun angularMomentumOfUnit(unit: KAngularMomentumUnit): KAngularMomentumUnitInstance =
    angularMomentumInstanceOf(unit.baseValue * KGM2S_IN_BASE)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" angular momentum, as long as it matches the canonical normal form:
 * exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent `+2` and one
 * [KTimeUnit] term at exponent `-1` (order independent). This is also the native decomposition
 * `mass * length² / time`.
 *
 * @throws IllegalStateException if this instance is not a mass·length²·time⁻¹ angular momentum.
 */
fun KMixedUnitInstance.toAngularMomentum(): KAngularMomentumUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -1 }
    check(units.size == 3 && massTerm != null && distanceTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure angular momentum (expected one KMassUnit^1, one KDistanceUnit^2 and one KTimeUnit^-1 term)"
    }
    val component = value *
            massTerm.unit.baseValue *
            distanceTerm.unit.baseValue * distanceTerm.unit.baseValue /
            timeTerm.unit.baseValue
    return angularMomentumInstanceOf(component)
}
