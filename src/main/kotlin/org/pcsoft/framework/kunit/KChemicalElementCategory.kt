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
 * The chemical family a [KChemicalElement] belongs to - the usual colour coding of a periodic table.
 *
 * The f-block families (lanthanides, actinides) are intentionally absent: [KChemicalElement] currently
 * covers the classic school periodic table, i.e. the main and sub groups of periods 1-6 without the
 * f-block.
 *
 * Example:
 * ```kotlin
 * KChemicalElement.IRON.category // KChemicalElementCategory.TRANSITION_METAL
 * ```
 */
enum class KChemicalElementCategory(val displayName: String) {
    /** Hydrogen - the single element that fits none of the other families. */
    HYDROGEN("Hydrogen"),

    /** Alkali metals: the soft, highly reactive metals of main group 1 (Li, Na, K, Rb, Cs). */
    ALKALI_METAL("Alkali metal"),

    /** Alkaline earth metals: main group 2 (Be, Mg, Ca, Sr, Ba). */
    ALKALINE_EARTH_METAL("Alkaline earth metal"),

    /** Transition metals: the d-block elements forming the sub groups (Sc ... Zn, Y ... Cd, Hf ... Hg). */
    TRANSITION_METAL("Transition metal"),

    /** Post-transition metals: the softer p-block metals (Al, Ga, In, Sn, Tl, Pb, Bi, Po). */
    POST_TRANSITION_METAL("Post-transition metal"),

    /** Metalloids: elements with properties between metal and nonmetal (B, Si, Ge, As, Sb, Te). */
    METALLOID("Metalloid"),

    /** Nonmetals other than halogens and noble gases (C, N, O, P, S, Se). */
    NONMETAL("Nonmetal"),

    /** Halogens: main group 7 (F, Cl, Br, I, At). */
    HALOGEN("Halogen"),

    /** Noble gases: main group 8 (He, Ne, Ar, Kr, Xe, Rn). */
    NOBLE_GAS("Noble gas"),
    ;

    /** The category's human readable name, e.g. `"Transition metal"`. */
    override fun toString(): String = displayName
}
