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

package org.pcsoft.framework.kunit.mechanic.compressibility

// Value-1 compressibility templates for the named units of the group, used with `of`/`into`
// (`4.6e-10 of reciprocalPascals`). Prefixed forms live in KCompressibilityUnitExtensions.kt.

/** 1 1/Pa ([KCompressibilityUnit.RECIPROCAL_PASCAL]), the group's base unit. */
val reciprocalPascals: KCompressibilityUnitInstance =
    compressibilityOfUnit(KCompressibilityUnit.RECIPROCAL_PASCAL)

/** 1 1/bar ([KCompressibilityUnit.RECIPROCAL_BAR], 1e-5 1/Pa). */
val reciprocalBars: KCompressibilityUnitInstance =
    compressibilityOfUnit(KCompressibilityUnit.RECIPROCAL_BAR)

/** 1 1/atm ([KCompressibilityUnit.RECIPROCAL_ATMOSPHERE], 1/101325 1/Pa). */
val reciprocalAtmospheres: KCompressibilityUnitInstance =
    compressibilityOfUnit(KCompressibilityUnit.RECIPROCAL_ATMOSPHERE)
