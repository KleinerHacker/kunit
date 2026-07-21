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

package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KUnit

/**
 * The units of the **mass** group.
 *
 * The group is one-dimensional (no exponent-specialized subtypes) and its base unit is [GRAM]
 * ([baseValue] `1.0`). Every [baseValue] is the factor to convert into the group's base unit
 * ([BASE], gram): `1 unit = baseValue * gram`.
 *
 * The **kilogram is intentionally not a dedicated entry**: it is reached generically through the SI
 * [org.pcsoft.framework.kunit.KUnitPrefix] table applied to [GRAM] (`1 of kilo.grams` = 1000 g), just
 * like every other decimal magnitude of the gram.
 *
 * Example:
 * ```kotlin
 * KMassUnit.POUND.baseValue // 453.59237 (1 lb = 453.59237 g)
 * ```
 */
enum class KMassUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    // --- Metric / SI ----------------------------------------------------------------------------

    /** Gram ("g"): the base unit of the mass group ([baseValue] `1.0`). */
    GRAM("g", 1.0),

    /** Tonne / metric ton ("t"), 1 t = 1 000 000 g. */
    TONNE("t", 1_000_000.0),

    /** Metric carat ("ct"), 1 ct = 0.2 g. */
    CARAT("ct", 0.2),

    // --- Avoirdupois (angloamerican) ------------------------------------------------------------

    /** Grain ("gr"), 1 gr = 0.064 798 91 g. */
    GRAIN("gr", 0.06479891),

    /** Dram (avoirdupois) ("dr"), 1 dr = 1.771 845 195 312 5 g. */
    DRAM("dr", 1.7718451953125),

    /** Ounce (avoirdupois) ("oz"), 1 oz = 28.349 523 125 g. */
    OUNCE("oz", 28.349523125),

    /** Pound (avoirdupois) ("lb"), 1 lb = 453.592 37 g. */
    POUND("lb", 453.59237),

    /** Stone ("st"), 1 st = 6350.293 18 g (= 14 lb). */
    STONE("st", 6350.29318),

    /** Hundredweight US / short hundredweight ("cwt(US)"), 1 cwt = 45 359.237 g (= 100 lb). */
    HUNDREDWEIGHT_US("cwt(US)", 45359.237),

    /** Hundredweight UK / long hundredweight ("cwt(UK)"), 1 cwt = 50 802.345 44 g (= 112 lb). */
    HUNDREDWEIGHT_UK("cwt(UK)", 50802.34544),

    /** Short ton (US ton) ("ton(US)"), 1 ton = 907 184.74 g (= 2000 lb). */
    SHORT_TON("ton(US)", 907184.74),

    /** Long ton (UK ton) ("ton(UK)"), 1 ton = 1 016 046.908 8 g (= 2240 lb). */
    LONG_TON("ton(UK)", 1016046.9088),

    /** Slug ("slug"), 1 slug = 14 593.902 94 g. */
    SLUG("slug", 14593.90294),

    // --- Troy / apothecary ----------------------------------------------------------------------

    /** Pennyweight ("dwt"), 1 dwt = 1.555 173 84 g. */
    PENNYWEIGHT("dwt", 1.55517384),

    /** Troy ounce ("oz t"), 1 oz t = 31.103 476 8 g. */
    TROY_OUNCE("oz t", 31.1034768),

    /** Troy pound ("lb t"), 1 lb t = 373.241 721 6 g. */
    TROY_POUND("lb t", 373.2417216),

    // --- Historical / regional ------------------------------------------------------------------

    /** German pound ("Pfd"), 1 Pfund = 500 g. */
    GERMAN_POUND("Pfd", 500.0),

    /** Zentner ("Ztr"), 1 Zentner = 50 000 g (= 100 German pounds). */
    ZENTNER("Ztr", 50000.0),

    /** Lot ("Lot"), 1 Lot = 16.666 666 7 g (1/30 German pound). */
    LOT("Lot", 16.6666667),

    /** Jin / catty ("斤"), 1 jin = 500 g. */
    JIN("斤", 500.0),

    /** Liang / tael ("两"), 1 liang = 50 g. */
    LIANG("两", 50.0),

    /** Momme ("匁"), 1 momme = 3.75 g. */
    MOMME("匁", 3.75),

    /** Kan / kanme ("貫"), 1 kan = 3750 g (= 1000 momme). */
    KAN("貫", 3750.0),

    // --- Scientific -----------------------------------------------------------------------------

    /** Dalton / unified atomic mass unit ("Da"), 1 Da = 1.660 539 066 6 × 10⁻²⁴ g. */
    DALTON("Da", 1.6605390666e-24);

    companion object {
        /**
         * The base unit of the mass group: [GRAM]. All internal values of [KMassUnitInstance] are
         * normalized to this unit.
         */
        val BASE: KMassUnit = GRAM
    }
}
