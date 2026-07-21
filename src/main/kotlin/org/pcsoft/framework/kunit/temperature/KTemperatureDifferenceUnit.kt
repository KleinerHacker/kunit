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

package org.pcsoft.framework.kunit.temperature

import org.pcsoft.framework.kunit.KUnit

/**
 * The units of the **temperature difference** group with base unit [KELVIN] ([baseValue] `1.0`).
 *
 * **Related group:** this is the *linear* counterpart to the *absolute* (affine) [KTemperatureUnit]
 * group. Unlike that group, a temperature *difference* is a **linear** quantity: it represents an
 * interval on the temperature scale, not an absolute point, and therefore carries **no offset** - only
 * the scale factor of kelvin. This is exactly what makes it a normal (non-affine) unit group that runs
 * through the shared multiplicative engine unchanged. A difference is produced by subtracting two
 * [KTemperatureUnitInstance]s and can be added back to one to yield an absolute temperature again.
 *
 * The group deliberately offers only kelvin (by decision): a difference of `20 ΔK` equals a difference of
 * `20 °C` numerically anyway (identical step size), so a single canonical kelvin representation is
 * sufficient. It also offers **no prefixes**.
 *
 * Its symbol is rendered as **`ΔK`** (not `K`) so that a difference is visually distinguishable from an
 * absolute kelvin in mixed units and `toString` output - the two are the same *dimension* but different
 * quantities (affine point vs. linear interval), and printing both as `K` would be misleading.
 *
 * A temperature difference is produced by subtracting two absolute temperatures
 * (`(30 of celsius) - (10 of celsius)`) or constructed explicitly via
 * [KTemperatureDifference.ofKelvin].
 */
enum class KTemperatureDifferenceUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Kelvin ("ΔK"): the base unit of the temperature difference group ([baseValue] `1.0`). */
    KELVIN("ΔK", 1.0);

    companion object {
        /** The base unit of the temperature difference group: [KELVIN]. */
        val BASE: KTemperatureDifferenceUnit = KELVIN
    }
}
