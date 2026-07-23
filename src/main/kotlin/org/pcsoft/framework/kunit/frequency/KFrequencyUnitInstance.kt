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

package org.pcsoft.framework.kunit.frequency

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitDisplay
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.time.KTimeUnit

/**
 * A **frequency** value: the "pure" wrapper of the frequency group, a single [KFrequencyUnit.BASE]
 * (hertz) term at exponent 1, always normalized internally to hertz.
 *
 * Frequency is the **inverse of time** (`1 Hz = 1/s`). Structurally it is the *plain, one-dimensional*
 * wrapper shape (like mass): it encapsulates a [KMixedUnitInstance] via Kotlin `by` delegation (so
 * `value`/`toUnit`/`times`/`div` and the group-agnostic `pow` come from the delegate) and implements
 * [KUnitInstance] directly (adding same-type `+`/`-`/comparison).
 *
 * Its cross-group behaviour is defined to be exactly inverse to time (see `KFrequencyUnitOperators.kt`):
 * multiplying by a frequency behaves like dividing by a time, and dividing by a frequency like
 * multiplying by a time (e.g. `distance * frequency = speed`, `speed / frequency = distance`).
 *
 * Instances are created via the bare tokens in `KFrequencyUnitBareValues.kt` (e.g. `5 of hertz`), the
 * prefixed templates in `KFrequencyUnitExtensions.kt` (e.g. `2 of kilo.hertz`), operator results
 * (`1 / (2 of seconds)`), or [KMixedUnitInstance.toFrequency]. The constructor is `internal`.
 *
 * Example:
 * ```kotlin
 * val f = 2 of kilo.hertz   // 2000 Hz
 * f.value                   // 2000.0 (normalized to hertz)
 * f into rpm                // 120000.0 (read back in rpm)
 * ```
 */
class KFrequencyUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitMeasurable by instance, KUnitInstance<KFrequencyUnitInstance> {

    /**
     * Returns a new frequency value with [value] (hertz) scaled by [factor]. Backs number-times-unit
     * construction (`10 of kilo.hertz`).
     */
    override fun scaledBy(factor: Double): KFrequencyUnitInstance = frequencyOf(value * factor)

    /**
     * Adds two frequency values, automatically converting between different [KFrequencyUnit]s since both
     * operands are always normalized to hertz internally. Only another [KFrequencyUnitInstance] is accepted.
     *
     * Example:
     * ```kotlin
     * ((1 of kilo.hertz) + (500 of hertz)).value // 1500.0
     * ```
     */
    override operator fun plus(other: KFrequencyUnitInstance): KFrequencyUnitInstance = frequencyOf(value + other.value)

    /** Subtracts two frequency values. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KFrequencyUnitInstance): KFrequencyUnitInstance = frequencyOf(value - other.value)

    /**
     * Multiplies two frequency values, producing a new [KMixedUnitInstance] (`Hz²`, no longer a "pure"
     * frequency).
     */
    operator fun times(other: KFrequencyUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two frequency values, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KFrequencyUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two frequency values by their normalized [value] (hertz). */
    override operator fun compareTo(other: KFrequencyUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two frequency values are equal iff they represent the
     * same physical frequency (e.g. `(1 of kilo.hertz) == (1000 of hertz)`).
     */
    override fun equals(other: Any?): Boolean = other is KFrequencyUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"2000.0 Hz"`. */
    override fun toString(): String = instance.toString()

    /**
     * The inverse-time view of this frequency as a [KMixedUnitInstance] tagged `[SECOND^-1]`, numerically
     * equal to the hertz [value]. This is the bridge that makes frequency behave exactly inverse to time
     * in the cross-group operators (`KFrequencyUnitOperators.kt`): multiplying by a frequency reduces a
     * time exponent, mirroring a division by a time.
     */
    internal fun toInverseTime(): KMixedUnitInstance =
        KMixedUnitInstance(value, listOf(KUnitTerm(KTimeUnit.SECOND, -1)))
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/** Builds a [KFrequencyUnitInstance] from a value already expressed in hertz ([KFrequencyUnit.BASE]). */
internal fun frequencyOf(value: Double, display: KUnitDisplay? = null): KFrequencyUnitInstance =
    KFrequencyUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KFrequencyUnit.BASE, 1, display))))

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" [KFrequencyUnitInstance], as long as it consists of exactly one
 * term of any [KFrequencyUnit] - normalizing it to [KFrequencyUnit.BASE] (hertz) if it isn't already.
 *
 * The term's **exponent is irrelevant** to the group check: a term is frequency-typed purely by its
 * [KFrequencyUnit] group, so this just wraps the numeric value normalized to hertz.
 *
 * @throws IllegalStateException if this instance does not consist of exactly one term of a [KFrequencyUnit].
 */
fun KMixedUnitInstance.toFrequency(): KFrequencyUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KFrequencyUnit) {
        "KMixedUnitInstance $this does not represent a pure frequency value (expected exactly one term of a KFrequencyUnit)"
    }
    val normalizedValue = value * Math.pow(unit.baseValue, term.exponent.toDouble())
    return frequencyOf(normalizedValue)
}
