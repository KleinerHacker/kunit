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

package org.pcsoft.framework.kunit

/**
 * A SI-style magnitude prefix (e.g. kilo, milli). It is the scale factor behind the prefix **builders**
 * ([KPrefixBuilder] and its `KDiminishing`/`KAugmenting` subtypes): a builder such as `kilo` or `milli`
 * wraps one [KUnitPrefix] and exposes value-1 unit templates (`kilo.meters`, `milli.seconds`) for use
 * with the [of]/[into] verbs.
 *
 * Prefixes are **not** stored as part of a [KUnitTerm]/[KUnit] - they only scale a raw value at the
 * input/output boundary; once a value has been constructed, the prefix used to construct it is no
 * longer recoverable (only the resulting, normalized base-unit value is kept).
 *
 * Example:
 * ```kotlin
 * val d = 5 of kilo.meters // KLengthUnitInstance
 * d.value                  // 5000.0 (normalized to meters)
 * d into kilo.meters       // 5.0 (read back in kilometers)
 * ```
 */
enum class KUnitPrefix(val symbol: String, val factor: Double) {
    /** Quetta: factor 10^30, the largest SI prefix (2022 standard). */
    QUETTA("Q", 1e30),

    /** Ronna: factor 10^27. */
    RONNA("R", 1e27),

    /** Yotta: factor 10^24. */
    YOTTA("Y", 1e24),

    /** Zetta: factor 10^21. */
    ZETTA("Z", 1e21),

    /** Exa: factor 10^18. */
    EXA("E", 1e18),

    /** Peta: factor 10^15. */
    PETA("P", 1e15),

    /** Tera: factor 10^12. */
    TERA("T", 1e12),

    /** Giga: factor 10^9 (10^9), e.g. gigameter = 1 000 000 000 meters. */
    GIGA("G", 1e9),

    /** Mega: factor 1 000 000 (10^6), e.g. megameter = 1 000 000 meters. */
    MEGA("M", 1e6),

    /** Kilo: factor 1 000 (10^3), e.g. kilometer = 1000 meters. */
    KILO("k", 1_000.0),

    /** Hecto: factor 100 (10^2), e.g. hectometer = 100 meters. */
    HECTO("h", 100.0),

    /** Deca: factor 10 (10^1), e.g. decameter = 10 meters. */
    DECA("da", 10.0),

    /** Deci: factor 0.1 (10^-1), e.g. decimeter = 0.1 meters. */
    DECI("d", 0.1),

    /** Centi: factor 0.01 (10^-2), e.g. centimeter = 0.01 meters. */
    CENTI("c", 0.01),

    /** Milli: factor 0.001 (10^-3), e.g. millimeter = 0.001 meters. */
    MILLI("m", 0.001),

    /** Micro: factor 10^-6, e.g. micrometer = 0.000001 meters. */
    MICRO("µ", 1e-6),

    /** Nano: factor 10^-9, e.g. nanometer = 0.000000001 meters. */
    NANO("n", 1e-9),

    /** Pico: factor 10^-12. */
    PICO("p", 1e-12),

    /** Femto: factor 10^-15. */
    FEMTO("f", 1e-15),

    /** Atto: factor 10^-18. */
    ATTO("a", 1e-18),

    /** Zepto: factor 10^-21. */
    ZEPTO("z", 1e-21),

    /** Yocto: factor 10^-24. */
    YOCTO("y", 1e-24),

    /** Ronto: factor 10^-27. */
    RONTO("r", 1e-27),

    /** Quecto: factor 10^-30, the smallest SI prefix (2022 standard). */
    QUECTO("q", 1e-30)
}
