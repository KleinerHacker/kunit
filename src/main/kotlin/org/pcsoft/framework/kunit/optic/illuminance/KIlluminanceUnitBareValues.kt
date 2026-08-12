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

package org.pcsoft.framework.kunit.optic.illuminance

// Value-1 illuminance templates for the named units of the group, used with `of`/`into`
// (`500 of lux`, `e into footCandles`). Prefixed forms live in KIlluminanceUnitExtensions.kt.

/** 1 lx ([KIlluminanceUnit.LUX]), the group's base unit. */
val lux: KIlluminanceUnitInstance = illuminanceOfUnit(KIlluminanceUnit.LUX)

/** 1 ph ([KIlluminanceUnit.PHOT], 10 000 lx). */
val phots: KIlluminanceUnitInstance = illuminanceOfUnit(KIlluminanceUnit.PHOT)

/** 1 fc ([KIlluminanceUnit.FOOT_CANDLE], ≈ 10.7639 lx). */
val footCandles: KIlluminanceUnitInstance = illuminanceOfUnit(KIlluminanceUnit.FOOT_CANDLE)

/** 1 nx ([KIlluminanceUnit.NOX], 0.001 lx). */
val nox: KIlluminanceUnitInstance = illuminanceOfUnit(KIlluminanceUnit.NOX)
