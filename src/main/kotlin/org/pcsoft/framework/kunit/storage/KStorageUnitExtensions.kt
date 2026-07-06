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

// Storage creator extension properties.

private fun storageFrom(value: Number, unit: KStorageUnit): KStorageUnitInstance = storageOf(value.toDouble() * unit.baseValue)

/**
 * Creates a pure storage value in bytes from any [Number] type. The value is normalized to bytes
 * ([KStorageUnit.BASE]).
 *
 * Example:
 * ```kotlin
 * 5.bytes.value   // 5.0
 * 5L.bytes.value  // 5.0
 * ```
 */
val Number.bytes: KStorageUnitInstance get() = storageFrom(this, KStorageUnit.BYTE)

/**
 * Creates a pure storage value in bits from any [Number] type. The value is normalized to bytes
 * (1 bit = 0.125 bytes).
 *
 * Example: `8.bits.value // 1.0`.
 */
val Number.bits: KStorageUnitInstance get() = storageFrom(this, KStorageUnit.BIT)
