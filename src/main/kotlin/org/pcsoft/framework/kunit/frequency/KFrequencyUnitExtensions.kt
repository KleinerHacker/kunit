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

package org.pcsoft.framework.kunit.frequency

import org.pcsoft.framework.kunit.KPrefixBuilder
import org.pcsoft.framework.kunit.KUnitDisplay

// Prefixed, value-1 frequency templates: one property per frequency unit on the prefix builder (e.g.
// `kilo.hertz` = 1000 Hz, `mega.hertz` = 1e6 Hz, `milli.hertz` = 0.001 Hz). Frequency accepts *any*
// magnitude, so the properties hang on the common base [KPrefixBuilder]. Use with `of`/`into`, e.g.
// `5 of kilo.hertz`, `v into mega.hertz`.

private fun prefixedFrequency(builder: KPrefixBuilder, unit: KFrequencyUnit): KFrequencyUnitInstance =
    frequencyOf(builder.prefix.factor * unit.baseValue, KUnitDisplay(unit, builder.prefix.symbol))

/** Prefixed hertz, e.g. `kilo.hertz` = 1000 Hz (kHz), `mega.hertz` = 1e6 Hz (MHz). */
val KPrefixBuilder.hertz: KFrequencyUnitInstance get() = prefixedFrequency(this, KFrequencyUnit.HERTZ)

/** Prefixed revolutions per second, e.g. `kilo.rps`. */
val KPrefixBuilder.rps: KFrequencyUnitInstance get() = prefixedFrequency(this, KFrequencyUnit.RPS)

/** Prefixed frames per second, e.g. `kilo.fps`. */
val KPrefixBuilder.fps: KFrequencyUnitInstance get() = prefixedFrequency(this, KFrequencyUnit.FPS)

/** Prefixed revolutions per minute, e.g. `kilo.rpm`. */
val KPrefixBuilder.rpm: KFrequencyUnitInstance get() = prefixedFrequency(this, KFrequencyUnit.RPM)

/** Prefixed beats per minute, e.g. `milli.bpm`. */
val KPrefixBuilder.bpm: KFrequencyUnitInstance get() = prefixedFrequency(this, KFrequencyUnit.BPM)
