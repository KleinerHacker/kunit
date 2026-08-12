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

package org.pcsoft.framework.kunit.optic.luminousflux

// Value-1 luminous flux templates for the named units of the group, used with `of`/`into`
// (`800 of lumens`, `phi into lumens`). Prefixed forms live in KLuminousFluxUnitExtensions.kt.

/** 1 lm ([KLuminousFluxUnit.LUMEN]), the group's base unit. */
val lumens: KLuminousFluxUnitInstance = luminousFluxOfUnit(KLuminousFluxUnit.LUMEN)

/** 1 cd·sr ([KLuminousFluxUnit.CANDELA_STERADIAN]), the written-out lumen definition. */
val candelaSteradians: KLuminousFluxUnitInstance = luminousFluxOfUnit(KLuminousFluxUnit.CANDELA_STERADIAN)
