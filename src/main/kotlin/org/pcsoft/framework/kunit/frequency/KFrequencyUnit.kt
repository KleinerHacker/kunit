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

import org.pcsoft.framework.kunit.KUnit

/**
 * The units of the **frequency** group.
 *
 * Frequency is a native base unit and the **inverse of time** (`1 Hz = 1/s`): where time counts a
 * duration, a frequency counts how many events occur per unit of time. The group is one-dimensional (no
 * exponent-specialized subtypes) and its base unit is [HERTZ] ([baseValue] `1.0`). Every [baseValue] is
 * the factor to convert into the group's base unit ([BASE], hertz): `1 unit = baseValue * hertz`.
 *
 * Sub- and supra-hertz scales (kilohertz, megahertz, gigahertz, millihertz, ...) are intentionally
 * **not** modeled as dedicated enum entries - they are reached generically through the SI
 * [org.pcsoft.framework.kunit.KUnitPrefix] table applied to [HERTZ] (e.g. `5 of kilo.hertz`).
 *
 * Example:
 * ```kotlin
 * KFrequencyUnit.RPM.baseValue // 0.016666… (1 rpm = 1/60 Hz)
 * ```
 */
enum class KFrequencyUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Hertz ("Hz"): the SI base unit of frequency ([baseValue] `1.0`), one cycle per second. */
    HERTZ("Hz", 1.0),

    /** Revolutions per second ("rps"), 1 rps = 1 Hz. */
    RPS("rps", 1.0),

    /** Frames per second ("fps"), 1 fps = 1 Hz. */
    FPS("fps", 1.0),

    /** Revolutions per minute ("rpm", min⁻¹), 1 rpm = 1/60 Hz. */
    RPM("rpm", 1.0 / 60.0),

    /** Beats per minute ("bpm"), 1 bpm = 1/60 Hz. */
    BPM("bpm", 1.0 / 60.0);

    companion object {
        /**
         * The base unit of the frequency group: [HERTZ]. All internal values of
         * [KFrequencyUnitInstance] are normalized to this unit.
         */
        val BASE: KFrequencyUnit = HERTZ
    }
}
