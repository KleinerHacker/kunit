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

package org.pcsoft.framework.kunit.datarate

import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.storage.KStorageBinaryPrefix

// Prefix `infix` constructors for the data-rate group. The right operand is a bare [KDataRateUnit] (e.g.
// the `bytesPerSecond`/`bitsPerSecond` aliases, see `KDataRateUnitBareValues.kt`) and the result is a
// [KDataRateUnitInstance], e.g. `5 kilo bytesPerSecond` == `5000.bytesPerSecond`,
// `5 kibi bytesPerSecond` == `5120.bytesPerSecond`.
//
// The data-rate group mirrors the storage group's prefix policy (the numerator is a storage amount):
//  * The *diminishing* SI prefixes (deci, centi, milli, … - factor < 1) are deliberately **not**
//    declared: a fraction of a bit per second is not a meaningful rate, so `5 milli bytesPerSecond` is a
//    compile error, not a runtime failure. Only the non-diminishing SI prefixes (factor >= 1) exist.
//  * In addition to the decimal SI prefixes there are the binary IEC prefixes (`kibi`, `mebi`, … -
//    powers of 1024), reused from the storage group, so a rate can distinguish 1000 (`kilo`) from
//    1024 (`kibi`).

private fun prefixedDataRate(value: Number, factor: Double, unit: KDataRateUnit): KDataRateUnitInstance =
    dataRateUnitInstanceOf(value.toDouble() * factor * unit.baseValue)

// --- Decimal SI prefixes (non-diminishing only, factor >= 1) -------------------------------------

/** Scales a data-rate [unit] by [KUnitPrefix.QUETTA], e.g. (5 quetta bytesPerSecond). */
infix fun Number.quetta(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KUnitPrefix.QUETTA.factor, unit)

/** Scales a data-rate [unit] by [KUnitPrefix.RONNA], e.g. (5 ronna bytesPerSecond). */
infix fun Number.ronna(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KUnitPrefix.RONNA.factor, unit)

/** Scales a data-rate [unit] by [KUnitPrefix.YOTTA], e.g. (5 yotta bytesPerSecond). */
infix fun Number.yotta(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KUnitPrefix.YOTTA.factor, unit)

/** Scales a data-rate [unit] by [KUnitPrefix.ZETTA], e.g. (5 zetta bytesPerSecond). */
infix fun Number.zetta(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KUnitPrefix.ZETTA.factor, unit)

/** Scales a data-rate [unit] by [KUnitPrefix.EXA], e.g. (5 exa bytesPerSecond). */
infix fun Number.exa(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KUnitPrefix.EXA.factor, unit)

/** Scales a data-rate [unit] by [KUnitPrefix.PETA], e.g. (5 peta bytesPerSecond). */
infix fun Number.peta(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KUnitPrefix.PETA.factor, unit)

/** Scales a data-rate [unit] by [KUnitPrefix.TERA], e.g. (5 tera bytesPerSecond). */
infix fun Number.tera(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KUnitPrefix.TERA.factor, unit)

/** Scales a data-rate [unit] by [KUnitPrefix.GIGA], e.g. (5 giga bytesPerSecond). */
infix fun Number.giga(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KUnitPrefix.GIGA.factor, unit)

/** Scales a data-rate [unit] by [KUnitPrefix.MEGA], e.g. (5 mega bytesPerSecond) - e.g. a typical "MB/s" download rate. */
infix fun Number.mega(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KUnitPrefix.MEGA.factor, unit)

/** Scales a data-rate [unit] by [KUnitPrefix.KILO], e.g. (5 kilo bytesPerSecond) == 5000 B/s. */
infix fun Number.kilo(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KUnitPrefix.KILO.factor, unit)

/** Scales a data-rate [unit] by [KUnitPrefix.HECTO], e.g. (5 hecto bytesPerSecond). */
infix fun Number.hecto(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KUnitPrefix.HECTO.factor, unit)

/** Scales a data-rate [unit] by [KUnitPrefix.DECA], e.g. (5 deca bytesPerSecond). */
infix fun Number.deca(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KUnitPrefix.DECA.factor, unit)

// --- Binary IEC prefixes (powers of 1024, reused from the storage group) -------------------------

/** Scales a data-rate [unit] by [KStorageBinaryPrefix.KIBI], e.g. (5 kibi bytesPerSecond) == 5120 B/s. */
infix fun Number.kibi(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KStorageBinaryPrefix.KIBI.factor, unit)

/** Scales a data-rate [unit] by [KStorageBinaryPrefix.MEBI], e.g. (5 mebi bytesPerSecond). */
infix fun Number.mebi(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KStorageBinaryPrefix.MEBI.factor, unit)

/** Scales a data-rate [unit] by [KStorageBinaryPrefix.GIBI], e.g. (5 gibi bytesPerSecond). */
infix fun Number.gibi(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KStorageBinaryPrefix.GIBI.factor, unit)

/** Scales a data-rate [unit] by [KStorageBinaryPrefix.TEBI], e.g. (5 tebi bytesPerSecond). */
infix fun Number.tebi(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KStorageBinaryPrefix.TEBI.factor, unit)

/** Scales a data-rate [unit] by [KStorageBinaryPrefix.PEBI], e.g. (5 pebi bytesPerSecond). */
infix fun Number.pebi(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KStorageBinaryPrefix.PEBI.factor, unit)

/** Scales a data-rate [unit] by [KStorageBinaryPrefix.EXBI], e.g. (5 exbi bytesPerSecond). */
infix fun Number.exbi(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KStorageBinaryPrefix.EXBI.factor, unit)

/** Scales a data-rate [unit] by [KStorageBinaryPrefix.ZEBI], e.g. (5 zebi bytesPerSecond). */
infix fun Number.zebi(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KStorageBinaryPrefix.ZEBI.factor, unit)

/** Scales a data-rate [unit] by [KStorageBinaryPrefix.YOBI], e.g. (5 yobi bytesPerSecond). */
infix fun Number.yobi(unit: KDataRateUnit): KDataRateUnitInstance = prefixedDataRate(this, KStorageBinaryPrefix.YOBI.factor, unit)
