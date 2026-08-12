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
 * A prefix **builder**: the receiver that turns a bare unit token into a **prefixed, value-1 unit
 * template** through a plain property access, e.g. `kilo.meters` (1000 m) or `milli.seconds` (0.001 s).
 * The resulting template is then used with the [of]/[into] verbs (`10 of kilo.meters`,
 * `v into milli.seconds`).
 *
 * Because a property access (`kilo.meters`) is a tight postfix expression, it composes cleanly with the
 * `* / pow` operators and the weaker-binding `of`/`into` infixes (`10 of kilo.meters / milli.seconds`)
 * without parentheses - unlike a hypothetical `5 milli seconds` infix, which the `/` operator would tear
 * apart.
 *
 * The unit properties themselves are **not** declared here: each unit group contributes them as
 * extension properties on the appropriate builder type (e.g. `val KPrefixBuilder.meters`,
 * `val KAugmentingPrefixBuilder.bytes`), keeping this root file free of group dependencies. This class
 * only carries the underlying [prefix] scale factor.
 *
 * The hierarchy encodes, at compile time, which units accept which prefixes:
 * - [KPrefixBuilder] (this base) declares nothing itself; groups hang the units that accept **any**
 *   magnitude on it (length, time, ...).
 * - [KDiminishingPrefixBuilder] carries the sub-unity prefixes (factor < 1: `deci` … `quecto`); groups
 *   hang units that are only sensible when made **smaller** on it.
 * - [KAugmentingPrefixBuilder] carries the supra-unity prefixes (factor > 1: `deca` … `quetta`); groups
 *   hang units that are only sensible when made **larger** on it - e.g. storage's `bytes`/`bits`, so
 *   `kilo.bytes` compiles while `milli.bytes` is a **compile error** (there is no `bytes` property on
 *   the diminishing builder).
 */
sealed class KPrefixBuilder(internal val prefix: KUnitPrefix)

/**
 * The builder for a **sub-unity** SI prefix (factor < 1, i.e. `deci` down to `quecto`). Groups add unit
 * properties here that are only meaningful when scaled **down** (in addition to everything on
 * [KPrefixBuilder]).
 */
class KDiminishingPrefixBuilder internal constructor(prefix: KUnitPrefix) : KPrefixBuilder(prefix)

/**
 * The builder for a **supra-unity** SI prefix (factor > 1, i.e. `deca` up to `quetta`). Groups add unit
 * properties here that are only meaningful when scaled **up** (in addition to everything on
 * [KPrefixBuilder]) - e.g. storage's `bytes`/`bits`, which makes `milli.bytes` a compile error.
 */
class KAugmentingPrefixBuilder internal constructor(prefix: KUnitPrefix) : KPrefixBuilder(prefix)

// --- Supra-unity builders (factor > 1) -----------------------------------------------------------

/** Quetta builder (10^30). Use as `quetta.<unit>`, e.g. `quetta.meters`. */
val quetta: KAugmentingPrefixBuilder = KAugmentingPrefixBuilder(KUnitPrefix.QUETTA)

/** Ronna builder (10^27), e.g. `ronna.meters`. */
val ronna: KAugmentingPrefixBuilder = KAugmentingPrefixBuilder(KUnitPrefix.RONNA)

/** Yotta builder (10^24), e.g. `yotta.meters`. */
val yotta: KAugmentingPrefixBuilder = KAugmentingPrefixBuilder(KUnitPrefix.YOTTA)

/** Zetta builder (10^21), e.g. `zetta.meters`. */
val zetta: KAugmentingPrefixBuilder = KAugmentingPrefixBuilder(KUnitPrefix.ZETTA)

/** Exa builder (10^18), e.g. `exa.meters`. */
val exa: KAugmentingPrefixBuilder = KAugmentingPrefixBuilder(KUnitPrefix.EXA)

/** Peta builder (10^15), e.g. `peta.meters`. */
val peta: KAugmentingPrefixBuilder = KAugmentingPrefixBuilder(KUnitPrefix.PETA)

/** Tera builder (10^12), e.g. `tera.bytes`. */
val tera: KAugmentingPrefixBuilder = KAugmentingPrefixBuilder(KUnitPrefix.TERA)

/** Giga builder (10^9), e.g. `giga.bytes`. */
val giga: KAugmentingPrefixBuilder = KAugmentingPrefixBuilder(KUnitPrefix.GIGA)

/** Mega builder (10^6), e.g. `mega.bytes`. */
val mega: KAugmentingPrefixBuilder = KAugmentingPrefixBuilder(KUnitPrefix.MEGA)

/** Kilo builder (10^3), e.g. `kilo.meters`, `kilo.bytes`. */
val kilo: KAugmentingPrefixBuilder = KAugmentingPrefixBuilder(KUnitPrefix.KILO)

/** Hecto builder (10^2), e.g. `hecto.meters`. */
val hecto: KAugmentingPrefixBuilder = KAugmentingPrefixBuilder(KUnitPrefix.HECTO)

/** Deca builder (10^1), e.g. `deca.meters`. */
val deca: KAugmentingPrefixBuilder = KAugmentingPrefixBuilder(KUnitPrefix.DECA)

// --- Sub-unity builders (factor < 1) -------------------------------------------------------------

/** Deci builder (10^-1), e.g. `deci.meters`. */
val deci: KDiminishingPrefixBuilder = KDiminishingPrefixBuilder(KUnitPrefix.DECI)

/** Centi builder (10^-2), e.g. `centi.meters`. */
val centi: KDiminishingPrefixBuilder = KDiminishingPrefixBuilder(KUnitPrefix.CENTI)

/** Milli builder (10^-3), e.g. `milli.meters`, `milli.seconds`. */
val milli: KDiminishingPrefixBuilder = KDiminishingPrefixBuilder(KUnitPrefix.MILLI)

/** Micro builder (10^-6), e.g. `micro.seconds`. */
val micro: KDiminishingPrefixBuilder = KDiminishingPrefixBuilder(KUnitPrefix.MICRO)

/** Nano builder (10^-9), e.g. `nano.seconds`. */
val nano: KDiminishingPrefixBuilder = KDiminishingPrefixBuilder(KUnitPrefix.NANO)

/** Pico builder (10^-12), e.g. `pico.seconds`. */
val pico: KDiminishingPrefixBuilder = KDiminishingPrefixBuilder(KUnitPrefix.PICO)

/** Femto builder (10^-15), e.g. `femto.seconds`. */
val femto: KDiminishingPrefixBuilder = KDiminishingPrefixBuilder(KUnitPrefix.FEMTO)

/** Atto builder (10^-18), e.g. `atto.seconds`. */
val atto: KDiminishingPrefixBuilder = KDiminishingPrefixBuilder(KUnitPrefix.ATTO)

/** Zepto builder (10^-21), e.g. `zepto.seconds`. */
val zepto: KDiminishingPrefixBuilder = KDiminishingPrefixBuilder(KUnitPrefix.ZEPTO)

/** Yocto builder (10^-24), e.g. `yocto.seconds`. */
val yocto: KDiminishingPrefixBuilder = KDiminishingPrefixBuilder(KUnitPrefix.YOCTO)

/** Ronto builder (10^-27), e.g. `ronto.seconds`. */
val ronto: KDiminishingPrefixBuilder = KDiminishingPrefixBuilder(KUnitPrefix.RONTO)

/** Quecto builder (10^-30), e.g. `quecto.seconds`. */
val quecto: KDiminishingPrefixBuilder = KDiminishingPrefixBuilder(KUnitPrefix.QUECTO)
