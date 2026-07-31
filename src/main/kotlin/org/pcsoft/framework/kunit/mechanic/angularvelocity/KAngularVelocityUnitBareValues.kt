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

package org.pcsoft.framework.kunit.mechanic.angularvelocity

// An angular velocity is built as an angle-per-time expression, e.g. `2 of radians / seconds` or
// `(1 of turns) / (1 of seconds)`. The plainly composed spellings (rad/s, °/s) therefore have
// deliberately **no** spelled-out tokens - they are `radians / seconds` and `degrees / seconds`.
//
// Only the conventionally named revolution rates survive as value-1 tokens, used with `of`/`into`
// (`3000 of revolutionsPerMinute`, `w into revolutionsPerSecond`). Prefixes are applied to the
// components (`kilo.radians / seconds`), so this group has no own prefix builders.

/** 1 rpm ([KAngularVelocityUnit.REVOLUTIONS_PER_MINUTE], 2π/60 rad/s). */
val revolutionsPerMinute: KAngularVelocityUnitInstance =
    angularVelocityOfUnit(KAngularVelocityUnit.REVOLUTIONS_PER_MINUTE)

/** 1 rps ([KAngularVelocityUnit.REVOLUTIONS_PER_SECOND], 2π rad/s). */
val revolutionsPerSecond: KAngularVelocityUnitInstance =
    angularVelocityOfUnit(KAngularVelocityUnit.REVOLUTIONS_PER_SECOND)
