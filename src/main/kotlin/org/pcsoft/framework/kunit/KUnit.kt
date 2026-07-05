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

package org.pcsoft.framework.kunit

/**
 * Marker interface for anything that can be used as a display/conversion target for a unit value.
 *
 * A target is either:
 * - a plain [KUnit] (no scaling, e.g. `KDistanceUnit.METER`),
 * - a [KScaledUnit] (a [KUnit] combined with a [KUnitPrefix], e.g. `KUnitPrefix.KILO with KDistanceUnit.METER`),
 * - a [KDerivedUnit] (a named unit bound to a specific unit group and exponent, e.g. `KDistanceDerivedUnit.HECTARE`), or
 * - a [KScaledDerivedUnit] (a [KDerivedUnit] combined with a [KUnitPrefix], e.g. `KUnitPrefix.MILLI with KDistanceDerivedUnit.LITER`).
 *
 * This lets conversion/formatting functions such as `KMixedUnitInstance.valueAs`, `KMixedUnitInstance.toString`,
 * or `KLengthUnitInstance.valueAs` accept any of these interchangeably at the same call site.
 *
 * Example:
 * ```kotlin
 * val d = 5.miles
 * d.valueAs(KDistanceUnit.MILE)                       // plain KUnit target
 * d.valueAs(KUnitPrefix.KILO with KDistanceUnit.METER) // KScaledUnit target
 * ```
 */
interface KUnitTarget

/**
 * Represents a single physical unit belonging to a unit group (e.g. meter within the length group).
 *
 * Concrete unit groups are modeled as `enum class` implementations of this interface.
 *
 * Two [KUnit] instances are considered to belong to the same group when they share the same runtime
 * type.
 */
interface KUnit : KUnitTarget {
    /**
     * The symbol used to display this unit, e.g. `"m"` for meter or `"mi"` for mile.
     */
    val symbol: String

    /**
     * The conversion factor from this unit to the base unit of its group.
     *
     * The base unit of a group has `baseValue == 1.0` by definition.
     */
    val baseValue: Double
}
