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

package org.pcsoft.framework.kunit.storage

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTarget
import org.pcsoft.framework.kunit.KUnitTerm

/**
 * A **storage** value (data amount): the "pure" wrapper of the storage group, a single
 * [KStorageUnit.BASE] (byte) term at exponent 1, always normalized internally to bytes.
 *
 * This is the *plain, one-dimensional* wrapper shape: it encapsulates a [KMixedUnitInstance] via
 * Kotlin `by` delegation (so `value`/`toUnit`/`times`/`div` and the group-agnostic `pow` come from the
 * delegate) and implements [KUnitInstance] directly (adding same-type `+`/`-`/comparison and the
 * single-target `valueAs`/`toString`). There is no exponent-specialized subtype hierarchy (unlike the
 * distance group) and no `Duration` backing (unlike the time group).
 *
 * Instances are created via the creator extension properties in `KStorageUnitExtensions.kt`
 * (e.g. `5.bytes`, `2.bits`), the SI-prefix `infix` constructors (e.g. `3 kilo bytes`) or the binary
 * `infix` constructors (e.g. `3 kibi bytes`) in `KStorageUnitPrefix.kt`, operator results, or
 * [KMixedUnitInstance.toStorage]. The constructor is `internal`; callers must never build one directly.
 *
 * Example:
 * ```kotlin
 * val size = 4 kibi bytes            // 4096 bytes
 * size.value                         // 4096.0 (normalized to bytes)
 * size.valueAs(bits)                 // 32768.0
 * size.valueAs(KStorageBinaryPrefix.KIBI with bytes) // 4.0 (read back in KiB)
 * ```
 */
class KStorageUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitMeasurable by instance, KUnitInstance<KStorageUnitInstance> {

    /**
     * Converts [value] into the given unit, SI-prefixed unit, or binary-prefixed unit - see
     * [KMixedUnitInstance.valueAs] for the exact matching rules.
     *
     * @throws IllegalStateException if [target] does not belong to the storage group.
     */
    override fun valueAs(target: KUnitTarget): Double = instance.valueAs(target)

    /**
     * Adds two storage values, automatically converting between bit and byte since both operands are
     * always normalized to bytes internally. Only another [KStorageUnitInstance] is accepted.
     *
     * Example:
     * ```kotlin
     * (1.bytes + 8.bits).value // 2.0
     * ```
     */
    override operator fun plus(other: KStorageUnitInstance): KStorageUnitInstance = storageOf(value + other.value)

    /** Subtracts two storage values. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KStorageUnitInstance): KStorageUnitInstance = storageOf(value - other.value)

    /** Compares two storage values by their normalized [value] (bytes). */
    override operator fun compareTo(other: KStorageUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two storage values are equal iff they represent the
     * same amount of data (e.g. `1.bytes == 8.bits`).
     */
    override fun equals(other: Any?): Boolean = other is KStorageUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"1024.0 B"`. */
    override fun toString(): String = instance.toString()

    /**
     * Representation in the given unit, SI-prefixed unit, or binary-prefixed unit - see [valueAs] for
     * the exact matching rules.
     *
     * @throws IllegalStateException under the same conditions as [valueAs].
     */
    override fun toString(target: KUnitTarget): String = instance.toString(target)
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/** Builds a [KStorageUnitInstance] from a value already expressed in bytes ([KStorageUnit.BASE]). */
internal fun storageOf(value: Double): KStorageUnitInstance =
    KStorageUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KStorageUnit.BASE, 1))))

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" [KStorageUnitInstance], as long as it consists of exactly one
 * term of any [KStorageUnit] - normalizing it to [KStorageUnit.BASE] (bytes) if it isn't already.
 *
 * The term's **exponent is irrelevant** to the group check: a term is storage-typed purely by its
 * [KStorageUnit] group, so this just wraps the numeric value normalized to bytes.
 *
 * @throws IllegalStateException if this instance does not consist of exactly one term of a
 * [KStorageUnit].
 */
fun KMixedUnitInstance.toStorage(): KStorageUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KStorageUnit) {
        "KMixedUnitInstance $this does not represent a pure storage value (expected exactly one term of a KStorageUnit)"
    }
    val normalizedValue = value * Math.pow(unit.baseValue, term.exponent.toDouble())
    return storageOf(normalizedValue)
}
