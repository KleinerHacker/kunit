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

package org.pcsoft.framework.kunit.optic.luminance

// Value-1 luminance templates for the named units of the group, used with `of`/`into`
// (`250 of candelasPerSquareMeter`, `l into footLamberts`). Prefixed forms live in
// KLuminanceUnitExtensions.kt.

/** 1 cd/m² ([KLuminanceUnit.CANDELA_PER_SQUARE_METER]), the group's base unit; also called *nit*. */
val candelasPerSquareMeter: KLuminanceUnitInstance = luminanceOfUnit(KLuminanceUnit.CANDELA_PER_SQUARE_METER)

/** 1 nit - the display-industry name for [KLuminanceUnit.CANDELA_PER_SQUARE_METER]. */
val nits: KLuminanceUnitInstance = luminanceOfUnit(KLuminanceUnit.CANDELA_PER_SQUARE_METER)

/** 1 sb ([KLuminanceUnit.STILB], 10 000 cd/m²). */
val stilbs: KLuminanceUnitInstance = luminanceOfUnit(KLuminanceUnit.STILB)

/** 1 asb ([KLuminanceUnit.APOSTILB], 1/π cd/m²). */
val apostilbs: KLuminanceUnitInstance = luminanceOfUnit(KLuminanceUnit.APOSTILB)

/** 1 L ([KLuminanceUnit.LAMBERT], 10⁴/π cd/m²). */
val lamberts: KLuminanceUnitInstance = luminanceOfUnit(KLuminanceUnit.LAMBERT)

/** 1 fL ([KLuminanceUnit.FOOT_LAMBERT], ≈ 3.4263 cd/m²). */
val footLamberts: KLuminanceUnitInstance = luminanceOfUnit(KLuminanceUnit.FOOT_LAMBERT)
