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

package org.pcsoft.framework.kunit.distance

// Bare, value-1 volume tokens for the named special volume units (liter/gallon/…), each normalized to
// cubic meters. A plain metric volume is built from a length via `pow`, e.g. `2 of (meters pow 3)`.
// Used with `of`/`into` (`5 of liters`, `v into liters`). Prefixed forms live in KVolumeUnitExtensions.kt.

/** 1 liter (0.001 m³ = 1 dm³). */
val liters: KVolumeUnitInstance = volumeOf(0.001)

/** 1 US liquid gallon (0.003785411784 m³). */
val usGallons: KVolumeUnitInstance = volumeOf(0.003785411784)

/** 1 imperial gallon (0.00454609 m³). */
val imperialGallons: KVolumeUnitInstance = volumeOf(0.00454609)

/** 1 US fluid ounce (2.95735295625e-5 m³). */
val usFluidOunces: KVolumeUnitInstance = volumeOf(2.95735295625e-5)

/** 1 oil barrel (0.158987294928 m³). */
val oilBarrels: KVolumeUnitInstance = volumeOf(0.158987294928)

/** 1 imperial bushel (0.03636872 m³; historical British dry/liquid capacity unit). */
val imperialBushels: KVolumeUnitInstance = volumeOf(0.03636872)

/** 1 imperial hogshead (0.32731785 m³; historical British cask capacity unit). */
val hogsheads: KVolumeUnitInstance = volumeOf(0.32731785)

/** 1 imperial pint (0.00056826125 m³; historical British capacity unit). */
val imperialPints: KVolumeUnitInstance = volumeOf(0.00056826125)

/** 1 imperial quart (0.0011365225 m³; historical British capacity unit; = 2 imperial pints). */
val imperialQuarts: KVolumeUnitInstance = volumeOf(0.0011365225)
