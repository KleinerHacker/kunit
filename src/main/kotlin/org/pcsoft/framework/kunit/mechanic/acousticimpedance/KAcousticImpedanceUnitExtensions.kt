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

package org.pcsoft.framework.kunit.mechanic.acousticimpedance

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 acoustic impedance templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `1.48 of mega.rayls`.

private fun prefixedAcousticImpedance(
    builder: KPrefixBuilder,
    unit: KAcousticImpedanceUnit
): KAcousticImpedanceUnitInstance =
    acousticImpedanceInstanceOf(builder.prefix.factor * unit.baseValue * RAYL_IN_BASE)

/** Prefixed pascal seconds per meter, e.g. `mega.pascalSecondsPerMeter`. */
val KPrefixBuilder.pascalSecondsPerMeter: KAcousticImpedanceUnitInstance
    get() = prefixedAcousticImpedance(this, KAcousticImpedanceUnit.PASCAL_SECOND_PER_METER)

/** Prefixed rayls, e.g. `mega.rayls` (the usual unit for tissue and water). */
val KPrefixBuilder.rayls: KAcousticImpedanceUnitInstance
    get() = prefixedAcousticImpedance(this, KAcousticImpedanceUnit.RAYL)

/** Prefixed CGS rayls, e.g. `kilo.cgsRayls`. */
val KPrefixBuilder.cgsRayls: KAcousticImpedanceUnitInstance
    get() = prefixedAcousticImpedance(this, KAcousticImpedanceUnit.CGS_RAYL)
