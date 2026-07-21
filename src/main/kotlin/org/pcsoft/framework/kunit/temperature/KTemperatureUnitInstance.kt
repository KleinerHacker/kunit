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

package org.pcsoft.framework.kunit.temperature

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm

/**
 * A **temperature** value: the "pure" wrapper of the temperature group, a single [KTemperatureUnit.BASE]
 * (kelvin) term at exponent 1, always normalized internally to **absolute kelvin**.
 *
 * Like the storage wrapper it encapsulates a [KMixedUnitInstance] via `by` delegation (so
 * `value`/`toUnit`/`times`/`div` and the group-agnostic `pow` come from the delegate) and implements
 * [KUnitInstance] directly (adding same-type `+`/`-`/comparison operating on absolute kelvin).
 *
 * **Affine exception:** temperature conversions are offset-and-scale, not a single factor. Both the
 * group-agnostic verbs stay robust by overriding their measurable hooks affinely: [scaledBy] backs
 * `of` (`template.scaledBy(n)`) and [readBaseValue] backs `into` - so `25 of celsius` and
 * `t into fahrenheit` work through the normal verbs with no group-specific overloads. To let a value-1
 * template know which unit it stands for, the wrapper additionally carries its construction [unit]
 * (default [KTemperatureUnit.KELVIN] for calculation results). [equals]/[hashCode] stay purely
 * kelvin-value based: the same absolute temperature is equal regardless of the construction unit.
 *
 * Instances are created via the bare tokens (`kelvin`, `celsius`, `fahrenheit`) combined with [of],
 * operator results, or [KMixedUnitInstance.toTemperature]. The constructor is `internal`.
 *
 * Example:
 * ```kotlin
 * val t = 25 of celsius             // 298.15 K internally
 * t into fahrenheit                 // 77.0
 * (t + (5 of celsius)).value        // 303.15 (absolute kelvin)
 * ```
 */
class KTemperatureUnitInstance internal constructor(
    internal val instance: KMixedUnitInstance,
    internal val unit: KTemperatureUnit = KTemperatureUnit.BASE,
) : KUnitMeasurable by instance, KUnitInstance<KTemperatureUnitInstance> {

    /**
     * **Affine construction hook** (the temperature exception): interprets [factor] as a *reading* in
     * this template's [unit] and converts it to absolute kelvin via [KTemperatureUnit.toBase]. This is
     * exactly the primitive the group-agnostic [org.pcsoft.framework.kunit.of] verb calls
     * (`template.scaledBy(n)`), so `25 of celsius` yields `298.15 K` through the normal `of` path -
     * without needing a shadow-prone `of` overload. For the kelvin base (identity transform) it reduces
     * to the plain linear scaling the engine expects.
     */
    override fun scaledBy(factor: Double): KTemperatureUnitInstance = temperatureOf(unit.toBase(factor), unit)

    /**
     * **Affine reading hook** (the temperature exception): reads an absolute-kelvin [baseValue] back into
     * this template's [unit] via [KTemperatureUnit.fromBase]. This is the counterpart to [scaledBy] and
     * is what makes the group-agnostic [org.pcsoft.framework.kunit.into] verb correct for temperatures -
     * no shadow-prone `into` overload needed. For the kelvin base (identity) it reduces to the plain
     * linear default.
     */
    override fun readBaseValue(baseValue: Double): Double = unit.fromBase(baseValue)

    /**
     * Adds two temperatures on their absolute kelvin [value]. Only another [KTemperatureUnitInstance] is
     * accepted; the result is again absolute kelvin.
     */
    override operator fun plus(other: KTemperatureUnitInstance): KTemperatureUnitInstance =
        temperatureOf(value + other.value)

    /** Subtracts two temperatures on their absolute kelvin [value]. See [plus]. */
    override operator fun minus(other: KTemperatureUnitInstance): KTemperatureUnitInstance =
        temperatureOf(value - other.value)

    /** Compares two temperatures by their normalized absolute kelvin [value]. */
    override operator fun compareTo(other: KTemperatureUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized absolute kelvin [value]: two temperatures are equal iff they
     * represent the same absolute temperature (e.g. `(0 of celsius) == (273.15 of kelvin)`), independent
     * of their construction [unit].
     */
    override fun equals(other: Any?): Boolean = other is KTemperatureUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit (kelvin) representation, e.g. `"298.15 K"`. */
    override fun toString(): String = instance.toString()
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KTemperatureUnitInstance] from a value already expressed in absolute kelvin
 * ([KTemperatureUnit.BASE]). [unit] records the intended construction unit (defaults to kelvin).
 */
internal fun temperatureOf(value: Double, unit: KTemperatureUnit = KTemperatureUnit.BASE): KTemperatureUnitInstance =
    KTemperatureUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KTemperatureUnit.BASE, 1))), unit)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" [KTemperatureUnitInstance], as long as it consists of exactly one
 * term of any [KTemperatureUnit] - normalizing it to absolute kelvin if it isn't already.
 *
 * The term's **exponent is irrelevant** to the group check: a term is temperature-typed purely by its
 * [KTemperatureUnit] group. Because the group is affine, terms of a non-kelvin [KTemperatureUnit] are
 * re-expressed through [KTemperatureUnit.toBase] before wrapping.
 *
 * @throws IllegalStateException if this instance does not consist of exactly one term of a
 * [KTemperatureUnit].
 */
fun KMixedUnitInstance.toTemperature(): KTemperatureUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KTemperatureUnit) {
        "KMixedUnitInstance $this does not represent a pure temperature value (expected exactly one term of a KTemperatureUnit)"
    }
    val kelvinValue = unit.toBase(value * Math.pow(unit.baseValue, term.exponent.toDouble()))
    return temperatureOf(kelvinValue)
}
