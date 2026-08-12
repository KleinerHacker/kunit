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

package org.pcsoft.framework.kunit.electric.elastance

// Value-1 elastance templates for the named units of the group, used with `of`/`into`
// (`1000 of reciprocalFarads`). Prefixed forms live in KElastanceUnitExtensions.kt.

/** 1 F⁻¹ ([KElastanceUnit.RECIPROCAL_FARAD]), the group's base unit. */
val reciprocalFarads: KElastanceUnitInstance = elastanceOfUnit(KElastanceUnit.RECIPROCAL_FARAD)

/** 1 daraf ([KElastanceUnit.DARAF]), the classical name for the reciprocal farad. */
val darafs: KElastanceUnitInstance = elastanceOfUnit(KElastanceUnit.DARAF)
