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

import kotlin.test.Test
import kotlin.test.assertEquals

/** Every [KChemicalElementCategory] entry carries its display name; each one gets its own assertion. */
class KChemicalElementCategoryTest {

    @Test
    fun `display names of all categories`() {
        assertEquals("Hydrogen", KChemicalElementCategory.HYDROGEN.displayName)
        assertEquals("Alkali metal", KChemicalElementCategory.ALKALI_METAL.displayName)
        assertEquals("Alkaline earth metal", KChemicalElementCategory.ALKALINE_EARTH_METAL.displayName)
        assertEquals("Transition metal", KChemicalElementCategory.TRANSITION_METAL.displayName)
        assertEquals("Post-transition metal", KChemicalElementCategory.POST_TRANSITION_METAL.displayName)
        assertEquals("Metalloid", KChemicalElementCategory.METALLOID.displayName)
        assertEquals("Nonmetal", KChemicalElementCategory.NONMETAL.displayName)
        assertEquals("Halogen", KChemicalElementCategory.HALOGEN.displayName)
        assertEquals("Noble gas", KChemicalElementCategory.NOBLE_GAS.displayName)
    }

    @Test
    fun `toString renders the display name`() {
        KChemicalElementCategory.entries.forEach { assertEquals(it.displayName, it.toString()) }
        assertEquals("Transition metal", KChemicalElementCategory.TRANSITION_METAL.toString())
    }

    @Test
    fun `the f-block families are intentionally absent`() {
        assertEquals(9, KChemicalElementCategory.entries.size)
    }
}
