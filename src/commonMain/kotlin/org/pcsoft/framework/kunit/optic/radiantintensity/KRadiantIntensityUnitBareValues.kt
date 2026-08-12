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

package org.pcsoft.framework.kunit.optic.radiantintensity

// Value-1 radiant intensity templates for the named units of the group, used with `of`/`into`
// (`5 of wattsPerSteradian`). Prefixed forms live in KRadiantIntensityUnitExtensions.kt.

/** 1 W/sr ([KRadiantIntensityUnit.WATT_PER_STERADIAN]), the group's base unit. */
val wattsPerSteradian: KRadiantIntensityUnitInstance =
    radiantIntensityOfUnit(KRadiantIntensityUnit.WATT_PER_STERADIAN)
