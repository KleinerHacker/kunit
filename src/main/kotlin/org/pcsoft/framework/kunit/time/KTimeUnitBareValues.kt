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

package org.pcsoft.framework.kunit.time

// Bare, value-1 time tokens (each = 1 unit, normalized to seconds). Vocabulary for building
// (`10 of seconds`) and reading (`v into hours`); combine with the prefix builders (`milli.seconds`)
// and operators (`meters / seconds`). Prefixed forms live in KTimeUnitExtensions.kt.

/** 1 second ([KTimeUnit.SECOND]). */
val seconds: KTimeUnitInstance = timeUnitInstanceOf(KTimeUnit.SECOND.baseValue)

/** 1 minute ([KTimeUnit.MINUTE]). */
val minutes: KTimeUnitInstance = timeUnitInstanceOf(KTimeUnit.MINUTE.baseValue)

/** 1 hour ([KTimeUnit.HOUR]). */
val hours: KTimeUnitInstance = timeUnitInstanceOf(KTimeUnit.HOUR.baseValue)

/** 1 day ([KTimeUnit.DAY]). */
val days: KTimeUnitInstance = timeUnitInstanceOf(KTimeUnit.DAY.baseValue)
