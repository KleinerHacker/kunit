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

package org.pcsoft.framework.kunit.mechanic.lineforce

// Value-1 force-per-length templates for the named units of the group, used with `of`/`into`
// (`72 of dynesPerCentimeter`, `k into newtonsPerMillimeter`). Prefixed forms live in
// KLineForceUnitExtensions.kt (e.g. `milli.newtonsPerMeter` for surface tensions).

/** 1 N/m ([KLineForceUnit.NEWTONS_PER_METER]), the group's base unit. */
val newtonsPerMeter: KLineForceUnitInstance = lineForceOfUnit(KLineForceUnit.NEWTONS_PER_METER)

/** 1 N/mm ([KLineForceUnit.NEWTONS_PER_MILLIMETER], 1000 N/m). */
val newtonsPerMillimeter: KLineForceUnitInstance = lineForceOfUnit(KLineForceUnit.NEWTONS_PER_MILLIMETER)

/** 1 dyn/cm ([KLineForceUnit.DYNES_PER_CENTIMETER], 1 mN/m - the classic surface-tension unit). */
val dynesPerCentimeter: KLineForceUnitInstance = lineForceOfUnit(KLineForceUnit.DYNES_PER_CENTIMETER)

/** 1 lbf/in ([KLineForceUnit.POUNDS_FORCE_PER_INCH], ≈ 175.127 N/m). */
val poundsForcePerInch: KLineForceUnitInstance = lineForceOfUnit(KLineForceUnit.POUNDS_FORCE_PER_INCH)

/** 1 kp/m ([KLineForceUnit.KILOPONDS_PER_METER], 9.80665 N/m). */
val kilopondsPerMeter: KLineForceUnitInstance = lineForceOfUnit(KLineForceUnit.KILOPONDS_PER_METER)
