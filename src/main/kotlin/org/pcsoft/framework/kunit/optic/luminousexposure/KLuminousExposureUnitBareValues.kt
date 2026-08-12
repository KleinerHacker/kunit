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

package org.pcsoft.framework.kunit.optic.luminousexposure

// Value-1 luminous exposure templates for the named units of the group, used with `of`/`into`
// (`400 of luxHours`, `h into luxSeconds`). Prefixed forms live in KLuminousExposureUnitExtensions.kt.

/** 1 lx·s ([KLuminousExposureUnit.LUX_SECOND]), the group's base unit. */
val luxSeconds: KLuminousExposureUnitInstance = luminousExposureOfUnit(KLuminousExposureUnit.LUX_SECOND)

/** 1 lx·h ([KLuminousExposureUnit.LUX_HOUR], 3600 lx·s). */
val luxHours: KLuminousExposureUnitInstance = luminousExposureOfUnit(KLuminousExposureUnit.LUX_HOUR)
