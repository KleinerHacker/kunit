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

import org.pcsoft.framework.kunit.KUnitDisplay

// Bare, value-1 storage tokens (each = 1 unit, normalized to bytes). Vocabulary for building
// (`10 of bytes`) and reading (`v into bits`); combine with the prefix builders (`kilo.bytes`,
// `kibi.bytes`). Prefixed forms live in KStorageUnitExtensions.kt.

/** 1 byte ([KStorageUnit.BYTE]). */
val bytes: KStorageUnitInstance = storageOf(KStorageUnit.BYTE.baseValue, KUnitDisplay(KStorageUnit.BYTE))

/** 1 bit ([KStorageUnit.BIT] = 0.125 B). */
val bits: KStorageUnitInstance = storageOf(KStorageUnit.BIT.baseValue, KUnitDisplay(KStorageUnit.BIT))
