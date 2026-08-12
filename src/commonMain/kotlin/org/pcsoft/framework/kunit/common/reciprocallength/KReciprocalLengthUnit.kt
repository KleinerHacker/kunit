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

package org.pcsoft.framework.kunit.common.reciprocallength

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **reciprocal length** (a *constructed* quantity: length⁻¹). [baseValue] is
 * the factor to convert into the group's base unit ([BASE], reciprocal meter):
 * `1 unit = baseValue * m⁻¹`.
 *
 * The group carries the neutral name *reciprocal length* because it serves two readings in two subject
 * areas: the **dioptre** of optics (refractive power `D = 1/f`) and the **wavenumber** of spectroscopy
 * (`ṽ = 1/λ`). Both are the same unit as far as KUnit is concerned; distinguish them by naming your values.
 *
 * Example:
 * ```kotlin
 * KReciprocalLengthUnit.RECIPROCAL_CENTIMETER.baseValue // 100.0 (1 cm⁻¹ = 100 m⁻¹)
 * ```
 */
enum class KReciprocalLengthUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Reciprocal meter ("1/m"), the coherent SI unit of reciprocal length; [baseValue] = 1.0. */
    RECIPROCAL_METER("1/m", 1.0),

    /**
     * Dioptre ("dpt"), the optometric spelling of the reciprocal meter: the refractive power of a lens
     * with a focal length of 1 m. Numerically identical to [RECIPROCAL_METER].
     */
    DIOPTRE("dpt", 1.0),

    /**
     * Reciprocal centimeter ("1/cm"), the spectroscopic wavenumber unit (also called *kayser*);
     * 1 cm⁻¹ = 100 m⁻¹.
     */
    RECIPROCAL_CENTIMETER("1/cm", 100.0);

    companion object {
        /**
         * The base unit of the reciprocal-length group: [RECIPROCAL_METER]. All internal values of
         * [KReciprocalLengthUnitInstance] are normalized to reciprocal meters.
         */
        val BASE: KReciprocalLengthUnit = RECIPROCAL_METER
    }
}
