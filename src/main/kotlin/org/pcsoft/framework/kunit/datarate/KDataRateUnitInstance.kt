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

package org.pcsoft.framework.kunit.datarate

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KScaledUnit
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTarget
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.storage.KBinaryScaledUnit
import org.pcsoft.framework.kunit.storage.KStorageUnit
import org.pcsoft.framework.kunit.time.KTimeUnit

/**
 * Wraps a [KMixedUnitInstance] representing a data rate, i.e. exactly two terms - one
 * [KStorageUnit.BASE] (byte) at exponent `+1` and one [KTimeUnit.BASE] (second) at exponent `-1`
 * (`B·s⁻¹`). The [value] is always normalized internally to bytes per second ([KDataRateUnit.BASE]),
 * regardless of which [KDataRateUnit], SI/binary prefix, or storage/time combination it was constructed
 * from.
 *
 * Data rate is a *constructed* unit group (the second one, after speed): unlike `KStorageUnitInstance`
 * (a single-term wrapper), it holds a two-term instance and can therefore be read back either as a whole
 * data-rate unit (a single [KDataRateUnit], or an SI-/binary-scaled one, e.g. MB/s, MiB/s - see
 * [valueAs]) or as a storage-per-time pair (two targets, e.g. `MB` + `s` - see the `vararg` [valueAs]).
 *
 * Instances are created via the creator extension properties in `KDataRateUnitExtensions.kt` (e.g.
 * `100.bytesPerSecond`, `50.bitsPerSecond`), the prefix `infix` constructors in
 * `KDataRateUnitPrefix.kt` (e.g. `5 mega bytesPerSecond`, `4 kibi bytesPerSecond`), the cross-group
 * operators in `KDataRateUnitOperators.kt` (e.g. `100.bytes / 10.seconds`), or [toDataRate].
 *
 * Example:
 * ```kotlin
 * val r = 100.bytes / 10.seconds                 // 10.0 (normalized to B/s)
 * r.valueAs(KDataRateUnit.BITS_PER_SECOND)       // 80.0 (read back in bit/s)
 * r.valueAs(KUnitPrefix.KILO with KStorageUnit.BYTE, KTimeUnit.SECOND) // 0.01 (as kB per s)
 * ```
 */
class KDataRateUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KDataRateUnitInstance>, KUnitMeasurable by instance {

    /**
     * Converts [value] into the given whole data-rate unit (a bare [KDataRateUnit], an SI-scaled or a
     * binary-scaled one) - `value / target.baseValue`.
     *
     * For a storage-per-time pair target (e.g. MB + s) use the [valueAs] `vararg` overload instead; a
     * single storage or time [KUnitTarget] is delegated to the internal two-term instance and therefore
     * fails (a data rate needs two targets), which is intentional.
     *
     * Example:
     * ```kotlin
     * val r = 100.bytes / 10.seconds
     * r.valueAs(KDataRateUnit.BITS_PER_SECOND) // 80.0
     * ```
     */
    override fun valueAs(target: KUnitTarget): Double {
        val whole = wholeRateTarget(target)
        return if (whole != null) value / whole.first else instance.valueAs(target)
    }

    /**
     * Converts [value] as a storage-per-time pair, i.e. exactly two targets: one storage-group and one
     * time-group [KUnitTarget] (order independent), delegating to [KMixedUnitInstance.valueAs].
     *
     * @throws IllegalStateException if [targets] does not consist of one matching storage and one
     * matching time target (see [KMixedUnitInstance.valueAs]).
     *
     * Example:
     * ```kotlin
     * val r = 100.bytes / 10.seconds
     * r.valueAs(KUnitPrefix.KILO with KStorageUnit.BYTE, KTimeUnit.SECOND) // 0.01 (kB per s)
     * ```
     */
    fun valueAs(vararg targets: KUnitTarget): Double = instance.valueAs(*targets)

    /**
     * Adds two data rates, automatically converting between different [KDataRateUnit]s since both
     * operands are always normalized to [KDataRateUnit.BASE] (B/s) internally.
     *
     * Example:
     * ```kotlin
     * (1.bytesPerSecond + 8.bitsPerSecond).valueAs(KDataRateUnit.BYTES_PER_SECOND) // 2.0
     * ```
     */
    override operator fun plus(other: KDataRateUnitInstance): KDataRateUnitInstance = KDataRateUnitInstance(instance + other.instance)

    /** Subtracts two data rates. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KDataRateUnitInstance): KDataRateUnitInstance = KDataRateUnitInstance(instance - other.instance)

    /**
     * Multiplies two data rates, producing a new [KMixedUnitInstance] (`B²·s⁻²`, no longer a "pure"
     * data rate). To turn a data rate back into a storage amount, multiply it by a *time* instead
     * (see `KDataRateUnitOperators.kt`).
     */
    operator fun times(other: KDataRateUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two data rates, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KDataRateUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two data rates by their normalized [value] (bytes per second). */
    override operator fun compareTo(other: KDataRateUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized [value] (both operands are always normalized to B/s). */
    override fun equals(other: Any?): Boolean = other is KDataRateUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"10.0 B/s"`. */
    override fun toString(): String = "$value ${KDataRateUnit.BASE.symbol}"

    /**
     * Representation in the given whole data-rate unit (a bare [KDataRateUnit], an SI-scaled or a
     * binary-scaled one); for a storage-per-time pair use the [toString] `vararg` overload. See
     * [valueAs] for the matching rules.
     *
     * Example:
     * ```kotlin
     * (100.bytes / 10.seconds).toString(KDataRateUnit.BITS_PER_SECOND) // "80.0 bit/s"
     * ```
     */
    override fun toString(target: KUnitTarget): String {
        val whole = wholeRateTarget(target)
        return if (whole != null) "${value / whole.first} ${whole.second}" else instance.toString(target)
    }

    /**
     * Resolves a "whole data rate" target - a bare [KDataRateUnit], a [KScaledUnit] wrapping one, or a
     * [KBinaryScaledUnit] wrapping one - to its `(baseValue, symbol)` pair, or `null` if [target] is not
     * a whole data-rate unit (then it is treated as a storage-per-time pair target and delegated to
     * [instance]).
     */
    private fun wholeRateTarget(target: KUnitTarget): Pair<Double, String>? = when {
        target is KDataRateUnit -> target.baseValue to target.symbol
        target is KScaledUnit && target.unit is KDataRateUnit -> target.baseValue to target.symbol
        target is KBinaryScaledUnit && target.unit is KDataRateUnit -> target.baseValue to target.symbol
        else -> null
    }

    /**
     * Representation as a storage-per-time pair (e.g. `MB` + `s`), delegating to
     * [KMixedUnitInstance.toString].
     *
     * Example:
     * ```kotlin
     * (100.bytes / 10.seconds).toString(KUnitPrefix.KILO with KStorageUnit.BYTE, KTimeUnit.SECOND) // "0.01 kB*s^-1"
     * ```
     */
    fun toString(vararg targets: KUnitTarget): String = instance.toString(*targets)
}

/**
 * Converts this mixed unit to a "pure" data-rate value, as long as it consists of exactly two terms: one
 * [KStorageUnit] term at exponent `+1` and one [KTimeUnit] term at exponent `-1` (order independent).
 * The terms are normalized to [KStorageUnit.BASE]/[KTimeUnit.BASE], so the resulting data rate is
 * expressed in bytes per second regardless of which storage/time units the terms were tagged with (e.g.
 * a `[BIT^1, HOUR^-1]` instance is converted to the equivalent B/s).
 *
 * @throws IllegalStateException if this instance is not a storage-per-time data rate (not exactly a
 * storage term at `+1` and a time term at `-1`).
 *
 * Example:
 * ```kotlin
 * val raw = 100.bytes.toUnit() / 10.seconds.toUnit() // [BYTE^1, SECOND^-1]
 * raw.toDataRate().valueAs(KDataRateUnit.BITS_PER_SECOND) // 80.0
 *
 * (200.bytes * 50.bytes).toDataRate() // throws IllegalStateException (B², not a data rate)
 * ```
 */
fun KMixedUnitInstance.toDataRate(): KDataRateUnitInstance {
    val storageTerm = units.singleOrNull { it.unit is KStorageUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -1 }
    check(units.size == 2 && storageTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure data rate (expected one KStorageUnit^1 and one KTimeUnit^-1 term)"
    }
    val bytesPerSecond = value * storageTerm.unit.baseValue / timeTerm.unit.baseValue
    return dataRateUnitInstanceOf(bytesPerSecond)
}

/** Builds a [KDataRateUnitInstance] from a value already expressed in bytes per second ([KDataRateUnit.BASE]). */
internal fun dataRateUnitInstanceOf(bytesPerSecond: Double): KDataRateUnitInstance =
    KDataRateUnitInstance(KMixedUnitInstance(bytesPerSecond, listOf(KUnitTerm(KStorageUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -1))))
