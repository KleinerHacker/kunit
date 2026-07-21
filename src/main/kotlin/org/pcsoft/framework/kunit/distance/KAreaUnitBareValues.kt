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

// Bare, value-1 area tokens for the named special area units (are/hectare/acre), each normalized to
// square meters. A plain metric area is instead built from a length via `pow`, e.g. `10 of (meters pow 2)`
// or `10 of kilo.meters pow 2`. These special units carry their own factor and are used with `of`/`into`
// (`2 of hectares`, `area into hectares`). Prefixed forms live in KAreaUnitExtensions.kt.

/** 1 are (100 m²). */
val ares: KAreaUnitInstance = areaOf(100.0)

/** 1 hectare (10 000 m² = 100 a). */
val hectares: KAreaUnitInstance = areaOf(10_000.0)

/** 1 international acre (4046.8564224 m²). */
val acres: KAreaUnitInstance = areaOf(4046.8564224)

/** 1 rood (1011.7141056 m² = 1/4 acre; historical English land area unit). */
val roods: KAreaUnitInstance = areaOf(1011.7141056)

/** 1 square perch, a.k.a. square rod (25.29285264 m²; historical surveyor's area unit). */
val squarePerches: KAreaUnitInstance = areaOf(25.29285264)

/** 1 Morgen (Prussian) (2553.22 m²; historical German land area unit). */
val morgens: KAreaUnitInstance = areaOf(2553.22)

/** 1 Joch (Austrian) (5754.642 m²; historical Austrian land area unit). */
val jochs: KAreaUnitInstance = areaOf(5754.642)

/** 1 Tagwerk (Bavarian) (3407.27 m²; historical Bavarian land area unit). */
val tagwerks: KAreaUnitInstance = areaOf(3407.27)
