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

package org.pcsoft.framework.kunit.mechanic.viscosity

// Value-1 dynamic-viscosity templates for the named units of the group, used with `of`/`into`
// (`5 of pascalSeconds`, `eta into poises`). The centipoise is not an own token - it is
// `centi.poises` (see KViscosityUnitExtensions.kt), just like the millipascal second is
// `milli.pascalSeconds`.

/** 1 Pa·s ([KViscosityUnit.PASCAL_SECOND]), the group's base unit. */
val pascalSeconds: KViscosityUnitInstance = viscosityOfUnit(KViscosityUnit.PASCAL_SECOND)

/** 1 poise ([KViscosityUnit.POISE], 0.1 Pa·s). */
val poises: KViscosityUnitInstance = viscosityOfUnit(KViscosityUnit.POISE)

/** 1 lbf·s/ft² ([KViscosityUnit.POUND_FORCE_SECOND_PER_SQUARE_FOOT], ≈ 47.8803 Pa·s). */
val poundForceSecondsPerSquareFoot: KViscosityUnitInstance =
    viscosityOfUnit(KViscosityUnit.POUND_FORCE_SECOND_PER_SQUARE_FOOT)

/** 1 reyn ([KViscosityUnit.REYN], lbf·s/in², ≈ 6894.757 Pa·s). */
val reyns: KViscosityUnitInstance = viscosityOfUnit(KViscosityUnit.REYN)
