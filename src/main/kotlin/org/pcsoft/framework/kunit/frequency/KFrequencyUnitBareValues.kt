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

import org.pcsoft.framework.kunit.KUnitDisplay

// Bare, value-1 frequency tokens (each = 1 unit, normalized to hertz). Vocabulary for building
// (`10 of hertz`) and reading (`v into rpm`); combine with the prefix builders (`kilo.hertz`).
// Prefixed forms live in KFrequencyUnitExtensions.kt.

/** 1 hertz ([KFrequencyUnit.HERTZ]). */
val hertz: KFrequencyUnitInstance = frequencyOf(KFrequencyUnit.HERTZ.baseValue, KUnitDisplay(KFrequencyUnit.HERTZ))

/** 1 revolution per second ([KFrequencyUnit.RPS]). */
val rps: KFrequencyUnitInstance = frequencyOf(KFrequencyUnit.RPS.baseValue, KUnitDisplay(KFrequencyUnit.RPS))

/** 1 frame per second ([KFrequencyUnit.FPS]). */
val fps: KFrequencyUnitInstance = frequencyOf(KFrequencyUnit.FPS.baseValue, KUnitDisplay(KFrequencyUnit.FPS))

/** 1 revolution per minute ([KFrequencyUnit.RPM]). */
val rpm: KFrequencyUnitInstance = frequencyOf(KFrequencyUnit.RPM.baseValue, KUnitDisplay(KFrequencyUnit.RPM))

/** 1 beat per minute ([KFrequencyUnit.BPM]). */
val bpm: KFrequencyUnitInstance = frequencyOf(KFrequencyUnit.BPM.baseValue, KUnitDisplay(KFrequencyUnit.BPM))
