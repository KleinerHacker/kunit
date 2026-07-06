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

package org.pcsoft.framework.kunit.storage

import org.pcsoft.framework.kunit.KUnitPrefix

// Prefix `infix` constructors for the storage group. The right operand is a bare [KStorageUnit] (e.g.
// the `bytes`/`bits` aliases, see `KStorageUnitBareValues.kt`) and the result is a
// [KStorageUnitInstance], e.g. `5 kilo bytes` == `5000.bytes`, `5 kibi bytes` == `5120.bytes`.
//
// Two special traits of the storage group:
//  * The *diminishing* SI prefixes (deci, centi, milli, … - factor < 1) are deliberately **not**
//    declared: a fraction of a bit is not a meaningful data amount, so `5 milli bytes` is a compile
//    error, not a runtime failure. Only the non-diminishing SI prefixes (factor >= 1, deca upward) exist.
//  * In addition to the decimal SI prefixes there are the binary IEC prefixes (`kibi`, `mebi`, … -
//    powers of 1024), so a value can distinguish 1000 (`kilo`) from 1024 (`kibi`).

private fun prefixedStorage(value: Number, factor: Double, unit: KStorageUnit): KStorageUnitInstance =
    storageOf(value.toDouble() * factor * unit.baseValue)

// --- Decimal SI prefixes (non-diminishing only, factor >= 1) -------------------------------------

/** Scales a storage [unit] by [KUnitPrefix.QUETTA], e.g. (5 quetta bytes). */
infix fun Number.quetta(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KUnitPrefix.QUETTA.factor, unit)

/** Scales a storage [unit] by [KUnitPrefix.RONNA], e.g. (5 ronna bytes). */
infix fun Number.ronna(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KUnitPrefix.RONNA.factor, unit)

/** Scales a storage [unit] by [KUnitPrefix.YOTTA], e.g. (5 yotta bytes). */
infix fun Number.yotta(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KUnitPrefix.YOTTA.factor, unit)

/** Scales a storage [unit] by [KUnitPrefix.ZETTA], e.g. (5 zetta bytes). */
infix fun Number.zetta(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KUnitPrefix.ZETTA.factor, unit)

/** Scales a storage [unit] by [KUnitPrefix.EXA], e.g. (5 exa bytes). */
infix fun Number.exa(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KUnitPrefix.EXA.factor, unit)

/** Scales a storage [unit] by [KUnitPrefix.PETA], e.g. (5 peta bytes). */
infix fun Number.peta(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KUnitPrefix.PETA.factor, unit)

/** Scales a storage [unit] by [KUnitPrefix.TERA], e.g. (5 tera bytes). */
infix fun Number.tera(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KUnitPrefix.TERA.factor, unit)

/** Scales a storage [unit] by [KUnitPrefix.GIGA], e.g. (5 giga bytes). */
infix fun Number.giga(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KUnitPrefix.GIGA.factor, unit)

/** Scales a storage [unit] by [KUnitPrefix.MEGA], e.g. (5 mega bytes). */
infix fun Number.mega(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KUnitPrefix.MEGA.factor, unit)

/** Scales a storage [unit] by [KUnitPrefix.KILO], e.g. (5 kilo bytes) == 5000 bytes. */
infix fun Number.kilo(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KUnitPrefix.KILO.factor, unit)

/** Scales a storage [unit] by [KUnitPrefix.HECTO], e.g. (5 hecto bytes). */
infix fun Number.hecto(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KUnitPrefix.HECTO.factor, unit)

/** Scales a storage [unit] by [KUnitPrefix.DECA], e.g. (5 deca bytes). */
infix fun Number.deca(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KUnitPrefix.DECA.factor, unit)

// --- Binary IEC prefixes (powers of 1024) --------------------------------------------------------

/** Scales a storage [unit] by [KStorageBinaryPrefix.KIBI], e.g. (5 kibi bytes) == 5120 bytes. */
infix fun Number.kibi(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KStorageBinaryPrefix.KIBI.factor, unit)

/** Scales a storage [unit] by [KStorageBinaryPrefix.MEBI], e.g. (5 mebi bytes). */
infix fun Number.mebi(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KStorageBinaryPrefix.MEBI.factor, unit)

/** Scales a storage [unit] by [KStorageBinaryPrefix.GIBI], e.g. (5 gibi bytes). */
infix fun Number.gibi(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KStorageBinaryPrefix.GIBI.factor, unit)

/** Scales a storage [unit] by [KStorageBinaryPrefix.TEBI], e.g. (5 tebi bytes). */
infix fun Number.tebi(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KStorageBinaryPrefix.TEBI.factor, unit)

/** Scales a storage [unit] by [KStorageBinaryPrefix.PEBI], e.g. (5 pebi bytes). */
infix fun Number.pebi(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KStorageBinaryPrefix.PEBI.factor, unit)

/** Scales a storage [unit] by [KStorageBinaryPrefix.EXBI], e.g. (5 exbi bytes). */
infix fun Number.exbi(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KStorageBinaryPrefix.EXBI.factor, unit)

/** Scales a storage [unit] by [KStorageBinaryPrefix.ZEBI], e.g. (5 zebi bytes). */
infix fun Number.zebi(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KStorageBinaryPrefix.ZEBI.factor, unit)

/** Scales a storage [unit] by [KStorageBinaryPrefix.YOBI], e.g. (5 yobi bytes). */
infix fun Number.yobi(unit: KStorageUnit): KStorageUnitInstance = prefixedStorage(this, KStorageBinaryPrefix.YOBI.factor, unit)
