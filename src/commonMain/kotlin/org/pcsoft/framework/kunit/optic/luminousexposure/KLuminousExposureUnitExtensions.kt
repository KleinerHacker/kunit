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

package org.pcsoft.framework.kunit.optic.luminousexposure

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 luminous exposure templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `2 of kilo.luxHours`.

private fun prefixedLuminousExposure(
    builder: KPrefixBuilder,
    unit: KLuminousExposureUnit
): KLuminousExposureUnitInstance =
    luminousExposureInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed lux seconds, e.g. `kilo.luxSeconds`, `milli.luxSeconds`. */
val KPrefixBuilder.luxSeconds: KLuminousExposureUnitInstance
    get() = prefixedLuminousExposure(this, KLuminousExposureUnit.LUX_SECOND)

/** Prefixed lux hours, e.g. `kilo.luxHours` (the usual unit for annual light-dose budgets). */
val KPrefixBuilder.luxHours: KLuminousExposureUnitInstance
    get() = prefixedLuminousExposure(this, KLuminousExposureUnit.LUX_HOUR)
