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

package org.pcsoft.framework.kunit.it.storagedensity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.it.storage.KStorageUnit
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit

/**
 * Wraps a [KMixedUnitInstance] representing a storage density, i.e. exactly two terms - one
 * [KStorageUnit.BASE] (byte) at exponent `+1` and one [KDistanceUnit.BASE] (meter) at exponent `-2`
 * (`B·m⁻²`). The [value] is always normalized internally to bytes per square meter
 * ([KStorageDensityUnit.BASE]), regardless of which SI/binary prefix or storage/area combination it was
 * constructed from.
 *
 * Storage density is a *constructed* unit group (like data rate): it holds a two-term instance and is
 * therefore not built from bare tokens but from expressions such as `100.bytes / (1.meters pow 2)`, the
 * cross-group operators in `KStorageDensityUnitOperators.kt` (e.g. `256.gigaBytes / area`), or
 * [toStorageDensity].
 *
 * Example:
 * ```kotlin
 * val d = (256 of giga.bytes) / (100 of milli.meters pow 2) // areal density of an SSD die
 * d.valueAs(KStorageDensityUnit.BASE)                        // bytes per square meter
 * ```
 */
class KStorageDensityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KStorageDensityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new storage-density value with [value] (B/m²) scaled by [factor]. Backs number-times-unit
     * construction.
     */
    override fun scaledBy(factor: Double): KStorageDensityUnitInstance = storageDensityUnitInstanceOf(value * factor)

    /**
     * Adds two storage densities, automatically converting between different units since both operands
     * are always normalized to [KStorageDensityUnit.BASE] (B/m²) internally.
     */
    override operator fun plus(other: KStorageDensityUnitInstance): KStorageDensityUnitInstance =
        KStorageDensityUnitInstance(instance + other.instance)

    /** Subtracts two storage densities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KStorageDensityUnitInstance): KStorageDensityUnitInstance =
        KStorageDensityUnitInstance(instance - other.instance)

    /**
     * Multiplies two storage densities, producing a new [KMixedUnitInstance] (`B²·m⁻⁴`, no longer a
     * "pure" storage density). To turn a storage density back into a storage amount, multiply it by an
     * *area* instead (see `KStorageDensityUnitOperators.kt`).
     */
    operator fun times(other: KStorageDensityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two storage densities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KStorageDensityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two storage densities by their normalized [value] (bytes per square meter). */
    override operator fun compareTo(other: KStorageDensityUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized [value] (both operands are always normalized to B/m²). */
    override fun equals(other: Any?): Boolean = other is KStorageDensityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"10.0 B/m²"`. */
    override fun toString(): String = "$value ${KStorageDensityUnit.BASE.symbol}"
}

/**
 * Converts this mixed unit to a "pure" storage-density value, as long as it consists of exactly two
 * terms: one [KStorageUnit] term at exponent `+1` and one [KDistanceUnit] term at exponent `-2` (order
 * independent). The terms are normalized to [KStorageUnit.BASE]/[KDistanceUnit.BASE], so the resulting
 * storage density is expressed in bytes per square meter regardless of which storage/distance units the
 * terms were tagged with (e.g. a `[BIT^1, INCH^-2]` instance is converted to the equivalent B/m²).
 *
 * @throws IllegalStateException if this instance is not a storage-per-area density (not exactly a
 * storage term at `+1` and a distance term at `-2`).
 *
 * Example:
 * ```kotlin
 * val raw = 100.bytes.toUnit() / (1.meters pow 2).toUnit() // [BYTE^1, METER^-2]
 * raw.toStorageDensity().valueAs(KStorageDensityUnit.BASE) // 100.0
 * ```
 */
fun KMixedUnitInstance.toStorageDensity(): KStorageDensityUnitInstance {
    val storageTerm = units.singleOrNull { it.unit is KStorageUnit && it.exponent == 1 }
    val areaTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -2 }
    check(units.size == 2 && storageTerm != null && areaTerm != null) {
        "KMixedUnitInstance $this does not represent a pure storage density (expected one KStorageUnit^1 and one KDistanceUnit^-2 term)"
    }
    val bytesPerSquareMeter = value *
            Math.pow(storageTerm.unit.baseValue, storageTerm.exponent.toDouble()) *
            Math.pow(areaTerm.unit.baseValue, areaTerm.exponent.toDouble())
    return storageDensityUnitInstanceOf(bytesPerSquareMeter)
}

/** Builds a [KStorageDensityUnitInstance] from a value already expressed in bytes per square meter ([KStorageDensityUnit.BASE]). */
internal fun storageDensityUnitInstanceOf(bytesPerSquareMeter: Double): KStorageDensityUnitInstance =
    KStorageDensityUnitInstance(
        KMixedUnitInstance(
            bytesPerSquareMeter,
            listOf(KUnitTerm(KStorageUnit.BASE, 1), KUnitTerm(KDistanceUnit.BASE, -2))
        ),
    )
