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

// Bare unit references, usable both as a KUnitTarget (e.g. `s.valueAs(bits)`) and as the `unit`
// argument of the storage-group prefix `infix` functions (e.g. `5 kilo bytes`, `5 kibi bytes`, see
// `KStorageUnitPrefix.kt`).

/** Bare reference to [KStorageUnit.BYTE], for use with `valueAs` or the prefix `infix` functions. */
val bytes: KStorageUnit = KStorageUnit.BYTE

/** Bare reference to [KStorageUnit.BIT]. */
val bits: KStorageUnit = KStorageUnit.BIT
