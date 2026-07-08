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
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
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
     * Returns a new data-rate value with [value] (B/s) scaled by [factor]. Backs number-times-unit
     * construction (`5 of mega.bytes / seconds`, `10 of bytesPerSecond`).
     */
    override fun scaledBy(factor: Double): KDataRateUnitInstance = dataRateUnitInstanceOf(value * factor)

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
