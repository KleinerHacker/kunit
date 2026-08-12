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

// Value-1 acoustic impedance templates for the named units of the group, used with `of`/`into`
// (`413 of rayls`). Prefixed forms live in KAcousticImpedanceUnitExtensions.kt.

/** 1 Pa·s/m ([KAcousticImpedanceUnit.PASCAL_SECOND_PER_METER]), the group's base unit. */
val pascalSecondsPerMeter: KAcousticImpedanceUnitInstance =
    acousticImpedanceOfUnit(KAcousticImpedanceUnit.PASCAL_SECOND_PER_METER)

/** 1 rayl ([KAcousticImpedanceUnit.RAYL]), the named spelling of the pascal second per meter. */
val rayls: KAcousticImpedanceUnitInstance = acousticImpedanceOfUnit(KAcousticImpedanceUnit.RAYL)

/** 1 CGS rayl ([KAcousticImpedanceUnit.CGS_RAYL], 10 Pa·s/m). */
val cgsRayls: KAcousticImpedanceUnitInstance = acousticImpedanceOfUnit(KAcousticImpedanceUnit.CGS_RAYL)
