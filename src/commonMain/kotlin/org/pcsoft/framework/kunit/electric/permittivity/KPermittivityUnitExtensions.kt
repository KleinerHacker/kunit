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

package org.pcsoft.framework.kunit.electric.permittivity

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 permittivity templates: one property per unit on the prefix builder (e.g.
// `pico.faradsPerMeter` = 1e-12 F/m). Permittivity accepts *any* magnitude, so the properties hang on the
// common base [KPrefixBuilder]. Use with `of`/`into`, e.g. `8.854 of pico.faradsPerMeter`,
// `eps into nano.faradsPerMeter`.

private fun prefixedPermittivity(builder: KPrefixBuilder, unit: KPermittivityUnit): KPermittivityUnitInstance =
    permittivityInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed farads per meter, e.g. `pico.faradsPerMeter` = 1e-12 F/m. */
val KPrefixBuilder.faradsPerMeter: KPermittivityUnitInstance
    get() = prefixedPermittivity(this, KPermittivityUnit.FARAD_PER_METER)

/** Prefixed farads per centimeter, e.g. `pico.faradsPerCentimeter` = 1e-10 F/m. */
val KPrefixBuilder.faradsPerCentimeter: KPermittivityUnitInstance
    get() = prefixedPermittivity(this, KPermittivityUnit.FARAD_PER_CENTIMETER)
