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
 * Represents a single physical unit belonging to a unit group (e.g. meter within the length group).
 *
 * Concrete unit groups are modeled as `enum class` implementations of this interface. A [KUnit] only
 * carries the group's raw scale metadata ([symbol], [baseValue]); it is **not** a construction or
 * reading target itself. Both building (`10 of meters`) and reading (`v into kilo.meters`) work purely
 * with **value-1 unit instances** (see [KUnitMeasurable.of]/[KUnitMeasurable.into] and the prefix
 * builders), so there is no separate "unit target" abstraction any more.
 *
 * Two [KUnit] instances are considered to belong to the same group when they share the same runtime
 * type.
 */
interface KUnit {
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
