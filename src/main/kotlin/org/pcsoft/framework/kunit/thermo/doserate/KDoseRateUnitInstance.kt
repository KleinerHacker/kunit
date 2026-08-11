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

package org.pcsoft.framework.kunit.thermo.doserate

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **dose rate** (absorbed radiation dose per time), i.e.
 * exactly two terms in the canonical normal form - [KDistanceUnit.BASE] (meter) at exponent `+2` and
 * [KTimeUnit.BASE] (second) at exponent `-3` (`m²·s⁻³` = `Gy/s`). Both components are stored in their
 * group's base unit, so the raw component base *is* the named base unit ([KDoseRateUnit.GRAY_PER_SECOND])
 * and no bridging factor is needed - the kilogram of the gray's `J/kg` cancels against the joule's.
 *
 * Dose rate is a *constructed* unit group with one decomposition, funnelling into [doseRateInstanceOf]:
 * * `specificEnergy / time` (typed operator, see `KDoseRateUnitOperators.kt`)
 *
 * Instances are additionally created via the bare tokens in `KDoseRateUnitBareValues.kt` (e.g.
 * `0.1 of micro.sievertsPerHour`), the prefixed templates in `KDoseRateUnitExtensions.kt`, or
 * [toDoseRate].
 *
 * Example:
 * ```kotlin
 * val rate = (2 of milli.joulesPerKilogram) / (1 of hours)
 * rate into milli.graysPerHour
 * ```
 */
class KDoseRateUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KDoseRateUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new dose rate with [value] (Gy/s) scaled by [factor]. Backs number-times-unit construction
     * (`0.1 of micro.sievertsPerHour`).
     */
    override fun scaledBy(factor: Double): KDoseRateUnitInstance = doseRateInstanceOf(value * factor)

    /**
     * Adds two dose rates, automatically converting between different [KDoseRateUnit]s since both operands
     * are always normalized to [KDoseRateUnit.BASE] (Gy/s) internally.
     */
    override operator fun plus(other: KDoseRateUnitInstance): KDoseRateUnitInstance =
        doseRateInstanceOf(value + other.value)

    /** Subtracts two dose rates. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KDoseRateUnitInstance): KDoseRateUnitInstance =
        doseRateInstanceOf(value - other.value)

    /** Multiplies two dose rates, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KDoseRateUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two dose rates, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KDoseRateUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two dose rates by their normalized [value] (Gy/s). */
    override operator fun compareTo(other: KDoseRateUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two dose rates are equal iff they represent the same
     * quantity (e.g. `(1 of graysPerHour) == (1 of sievertsPerHour)`).
     */
    override fun equals(other: Any?): Boolean = other is KDoseRateUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in Gy/s, e.g. `"2.7777777777777776E-7 Gy/s"`. */
    override fun toString(): String = "$value ${KDoseRateUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KDoseRateUnitInstance] from a value already expressed in grays per second
 * ([KDoseRateUnit.BASE]).
 *
 * This is the single creation source that every dose rate decomposition must funnel into: it assembles the
 * canonical normal-form [KMixedUnitInstance] with the two terms `distance²` and `time⁻³` (each in its
 * group's base unit).
 */
internal fun doseRateInstanceOf(graysPerSecond: Double): KDoseRateUnitInstance =
    KDoseRateUnitInstance(
        KMixedUnitInstance(
            graysPerSecond,
            listOf(
                KUnitTerm(KDistanceUnit.BASE, 2),
                KUnitTerm(KTimeUnit.BASE, -3),
            ),
        ),
    )

/** Builds a value-1 [KDoseRateUnitInstance] for the given [unit] (its [KDoseRateUnit.baseValue] Gy/s). */
internal fun doseRateOfUnit(unit: KDoseRateUnit): KDoseRateUnitInstance = doseRateInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" dose rate, as long as it matches the canonical dose rate normal
 * form: exactly one [KDistanceUnit] term at exponent `+2` and one [KTimeUnit] term at exponent `-3` (order
 * independent). The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the
 * result is expressed in Gy/s regardless of which concrete units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `distance²·time⁻³` dose rate.
 */
fun KMixedUnitInstance.toDoseRate(): KDoseRateUnitInstance {
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -3 }
    check(units.size == 2 && distanceTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure dose rate " +
                "(expected one KDistanceUnit^2 and one KTimeUnit^-3 term)"
    }
    val distanceBase = distanceTerm.unit.baseValue
    return doseRateInstanceOf(
        value * distanceBase * distanceBase / Math.pow(timeTerm.unit.baseValue, 3.0),
    )
}
