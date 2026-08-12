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

package org.pcsoft.framework.kunit.thermo.catalyticactivity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **catalytic activity** (amount of substance converted per
 * time), i.e. exactly two terms in the canonical normal form - [KAmountOfSubstanceUnit.BASE] (mole) at
 * exponent `+1` and [KTimeUnit.BASE] (second) at exponent `-1` (`mol·s⁻¹` = `kat`). Both components are
 * stored in their group's base unit, so the raw component base *is* the named base unit
 * ([KCatalyticActivityUnit.KATAL]) and no bridging factor is needed.
 *
 * Catalytic activity is a *constructed* unit group with one decomposition, funnelling into
 * [catalyticActivityInstanceOf]:
 * * `amountOfSubstance / time` (typed operator, see `KCatalyticActivityUnitOperators.kt`)
 *
 * Instances are additionally created via the bare tokens in `KCatalyticActivityUnitBareValues.kt` (e.g.
 * `5 of micro.katals`), the prefixed templates in `KCatalyticActivityUnitExtensions.kt`, or
 * [toCatalyticActivity].
 *
 * Example:
 * ```kotlin
 * val a = (0.5 of milli.moles) / (10 of seconds) // 5e-5 kat
 * a into micro.katals
 * ```
 */
class KCatalyticActivityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KCatalyticActivityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new catalytic activity with [value] (kat) scaled by [factor]. Backs number-times-unit
     * construction (`5 of katals`).
     */
    override fun scaledBy(factor: Double): KCatalyticActivityUnitInstance =
        catalyticActivityInstanceOf(value * factor)

    /**
     * Adds two catalytic activities, automatically converting between different [KCatalyticActivityUnit]s
     * since both operands are always normalized to [KCatalyticActivityUnit.BASE] (kat) internally.
     */
    override operator fun plus(other: KCatalyticActivityUnitInstance): KCatalyticActivityUnitInstance =
        catalyticActivityInstanceOf(value + other.value)

    /** Subtracts two catalytic activities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KCatalyticActivityUnitInstance): KCatalyticActivityUnitInstance =
        catalyticActivityInstanceOf(value - other.value)

    /** Multiplies two catalytic activities, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KCatalyticActivityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two catalytic activities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KCatalyticActivityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two catalytic activities by their normalized [value] (kat). */
    override operator fun compareTo(other: KCatalyticActivityUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two catalytic activities are equal iff they represent the
     * same quantity (e.g. `(1 of katals) == (1000 of milli.katals)`).
     */
    override fun equals(other: Any?): Boolean =
        other is KCatalyticActivityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in katals, e.g. `"5.0E-5 kat"`. */
    override fun toString(): String = "${renderDouble(value)} ${KCatalyticActivityUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KCatalyticActivityUnitInstance] from a value already expressed in katals
 * ([KCatalyticActivityUnit.BASE]).
 *
 * This is the single creation source that every catalytic activity decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the two terms `substance¹` and `time⁻¹`
 * (each in its group's base unit).
 */
internal fun catalyticActivityInstanceOf(katals: Double): KCatalyticActivityUnitInstance =
    KCatalyticActivityUnitInstance(
        KMixedUnitInstance(
            katals,
            listOf(
                KUnitTerm(KAmountOfSubstanceUnit.BASE, 1),
                KUnitTerm(KTimeUnit.BASE, -1),
            ),
        ),
    )

/**
 * Builds a value-1 [KCatalyticActivityUnitInstance] for the given [unit] (its
 * [KCatalyticActivityUnit.baseValue] kat).
 */
internal fun catalyticActivityOfUnit(unit: KCatalyticActivityUnit): KCatalyticActivityUnitInstance =
    catalyticActivityInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" catalytic activity, as long as it matches the canonical catalytic
 * activity normal form: exactly one [KAmountOfSubstanceUnit] term at exponent `+1` and one [KTimeUnit] term
 * at exponent `-1` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the result is expressed in katals regardless of which
 * concrete units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `substance·time⁻¹` catalytic activity.
 */
fun KMixedUnitInstance.toCatalyticActivity(): KCatalyticActivityUnitInstance {
    val substanceTerm = units.singleOrNull { it.unit is KAmountOfSubstanceUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -1 }
    check(units.size == 2 && substanceTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure catalytic activity " +
                "(expected one KAmountOfSubstanceUnit^1 and one KTimeUnit^-1 term)"
    }
    return catalyticActivityInstanceOf(value * substanceTerm.unit.baseValue / timeTerm.unit.baseValue)
}
