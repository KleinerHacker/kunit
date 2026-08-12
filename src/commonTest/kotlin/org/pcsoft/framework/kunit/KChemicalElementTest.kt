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

import kotlin.test.*
import kotlin.test.Test
import org.pcsoft.framework.kunit.common.energy.electronVolts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molarvolume.cubicCentimetersPerMole
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin
import org.pcsoft.framework.kunit.thermo.temperature.kelvin

/**
 * The periodic table enum: positional data, unit data, availability flags and the companion lookups.
 * Every entry is asserted individually through the parameterized tests below.
 */
class KChemicalElementTest {

    // --- Table shape ----------------------------------------------------------------------------

    /** The classic school table: periods 1-6 without the f-block, i.e. 71 elements. */
    @Test
    fun `table covers periods one to six without the f-block`() {
        assertEquals(71, KChemicalElement.entries.size)
        assertEquals(2, KChemicalElement.ofPeriod(1).size)
        assertEquals(8, KChemicalElement.ofPeriod(2).size)
        assertEquals(8, KChemicalElement.ofPeriod(3).size)
        assertEquals(18, KChemicalElement.ofPeriod(4).size)
        assertEquals(18, KChemicalElement.ofPeriod(5).size)
        assertEquals(17, KChemicalElement.ofPeriod(6).size)
        assertTrue(KChemicalElement.ofPeriod(7).isEmpty())
        // the lanthanides 57-71 are not part of this table
        (57..71).forEach { assertNull(KChemicalElement.ofOrdinalNumber(it)) }
    }

    /** Entries are declared in ascending atomic number and each number occurs exactly once. */
    @Test
    fun `ordinal numbers are unique and ascending`() {
        val numbers = KChemicalElement.entries.map { it.ordinalNumber }
        assertEquals(numbers.sorted(), numbers)
        assertEquals(numbers.size, numbers.toSet().size)
        assertEquals(1, KChemicalElement.entries.first().ordinalNumber)
        assertEquals(86, KChemicalElement.entries.last().ordinalNumber)
    }

    // --- Per-entry invariants (one assertion set per element) ------------------------------------

    /** Positional data of every single element: symbol, name, period, group assignment, category. */
    @Test
    fun `positional data of every element`() = forEachCase(KChemicalElement.entries) { case ->
        `positional data of every element`(case)
    }

    private fun `positional data of every element`(element: KChemicalElement) {
        assertTrue(element.symbol.isNotBlank(), "${element.name} has no symbol")
        assertTrue(element.symbol.length <= 2, "${element.name} has an unexpected symbol")
        assertTrue(element.fullName.isNotBlank(), "${element.name} has no full name")
        assertTrue(element.ordinalNumber in 1..86, "${element.name} has an invalid atomic number")
        assertTrue(element.period in 1..6, "${element.name} has an invalid period")
        assertEquals("${element.symbol} (${element.fullName})", element.toString())

        val main = element.mainGroup
        val sub = element.subGroup
        assertTrue(
            (main == null) != (sub == null),
            "${element.name} must have either a main group or a sub group",
        )
        main?.let { assertTrue(it in 1..8, "${element.name} has an invalid main group") }
        sub?.let { assertTrue(it in 1..8, "${element.name} has an invalid sub group") }
        assertEquals(
            sub != null,
            element.category == KChemicalElementCategory.TRANSITION_METAL,
            "${element.name}: sub group and transition metal category must agree",
        )
    }

    /** Unit data of every single element: the always-present molar mass and every optional constant. */
    @Test
    fun `unit data of every element`() = forEachCase(KChemicalElement.entries) { case ->
        `unit data of every element`(case)
    }

    private fun `unit data of every element`(element: KChemicalElement) {
        assertTrue(element.molarMass into gramsPerMole > 0.0, "${element.name} has no positive molar mass")

        element.atomicRadius?.let { assertTrue(it into meters > 0.0) }
        element.covalentRadius?.let { assertTrue(it into meters > 0.0) }
        element.density?.let { assertTrue(it.value > 0.0) }
        element.meltingPoint?.let { assertTrue(it into kelvin > 0.0) }
        element.boilingPoint?.let { assertTrue(it into kelvin > 0.0) }
        element.specificHeatCapacity?.let { assertTrue(it into joulesPerKilogramKelvin > 0.0) }
        element.thermalConductivity?.let { assertTrue(it into wattsPerMeterKelvin > 0.0) }
        element.ionizationEnergy?.let { assertTrue(it into electronVolts > 0.0) }
        element.electricalResistivity?.let { assertTrue(it.value > 0.0) }
        element.electronegativity?.let { assertTrue(it in 0.5..4.0) }

        // both temperatures present => the element boils above its melting point
        val melting = element.meltingPoint
        val boiling = element.boilingPoint
        if (melting != null && boiling != null) {
            assertTrue(boiling > melting, "${element.name} boils below its melting point")
        }
    }

    /** Every availability flag of every element mirrors the nullability of its property. */
    @Test
    fun `availability flags of every element`() = forEachCase(KChemicalElement.entries) { case ->
        `availability flags of every element`(case)
    }

    private fun `availability flags of every element`(element: KChemicalElement) {
        assertEquals(element.atomicRadius != null, element.hasAtomicRadius)
        assertEquals(element.covalentRadius != null, element.hasCovalentRadius)
        assertEquals(element.density != null, element.hasDensity)
        assertEquals(element.molarVolume != null, element.hasMolarVolume)
        assertEquals(element.meltingPoint != null, element.hasMeltingPoint)
        assertEquals(element.boilingPoint != null, element.hasBoilingPoint)
        assertEquals(element.specificHeatCapacity != null, element.hasSpecificHeatCapacity)
        assertEquals(element.thermalConductivity != null, element.hasThermalConductivity)
        assertEquals(element.ionizationEnergy != null, element.hasIonizationEnergy)
        assertEquals(element.electricalResistivity != null, element.hasElectricalResistivity)
        assertEquals(element.electronegativity != null, element.hasElectronegativity)
    }

    /** The molar volume of every element is the quotient of its molar mass and its density. */
    @Test
    fun `molar volume is derived from molar mass and density`() = forEachCase(KChemicalElement.entries) { case ->
        `molar volume is derived from molar mass and density`(case)
    }

    private fun `molar volume is derived from molar mass and density`(element: KChemicalElement) {
        val density = element.density
        if (density == null) {
            assertNull(element.molarVolume)
            assertFalse(element.hasMolarVolume)
        } else {
            val molarVolume = assertNotNull(element.molarVolume)
            val expected = (element.molarMass into gramsPerMole) / (density.value / 1.0e6)
            assertEquals(expected, molarVolume into cubicCentimetersPerMole, expected * 1e-9)
        }
    }

    /** Every element is reachable through every lookup that applies to it. */
    @Test
    fun `every element is reachable through the lookups`() = forEachCase(KChemicalElement.entries) { case ->
        `every element is reachable through the lookups`(case)
    }

    private fun `every element is reachable through the lookups`(element: KChemicalElement) {
        assertSame(element, KChemicalElement.ofSymbol(element.symbol))
        assertSame(element, KChemicalElement.ofSymbol(element.symbol.uppercase()))
        assertSame(element, KChemicalElement.ofFullName(element.fullName))
        assertSame(element, KChemicalElement.ofFullName(element.fullName.lowercase()))
        assertSame(element, KChemicalElement.ofOrdinalNumber(element.ordinalNumber))
        assertTrue(element in KChemicalElement.ofPeriod(element.period))
        assertTrue(element in KChemicalElement.ofCategory(element.category))
        element.mainGroup?.let { assertNotNull(KChemicalElement.ofMainGroup(it, element.period)) }
        element.subGroup?.let { assertNotNull(KChemicalElement.ofSubGroup(it, element.period)) }
    }

    // --- Concrete values ------------------------------------------------------------------------

    private fun identityArgs(): List<Array<Any>> = listOf(
        arrayOf<Any>(KChemicalElement.HYDROGEN, 1, "H", "Hydrogen"),
        arrayOf<Any>(KChemicalElement.HELIUM, 2, "He", "Helium"),
        arrayOf<Any>(KChemicalElement.LITHIUM, 3, "Li", "Lithium"),
        arrayOf<Any>(KChemicalElement.BERYLLIUM, 4, "Be", "Beryllium"),
        arrayOf<Any>(KChemicalElement.BORON, 5, "B", "Boron"),
        arrayOf<Any>(KChemicalElement.CARBON, 6, "C", "Carbon"),
        arrayOf<Any>(KChemicalElement.NITROGEN, 7, "N", "Nitrogen"),
        arrayOf<Any>(KChemicalElement.OXYGEN, 8, "O", "Oxygen"),
        arrayOf<Any>(KChemicalElement.FLUORINE, 9, "F", "Fluorine"),
        arrayOf<Any>(KChemicalElement.NEON, 10, "Ne", "Neon"),
        arrayOf<Any>(KChemicalElement.SODIUM, 11, "Na", "Sodium"),
        arrayOf<Any>(KChemicalElement.MAGNESIUM, 12, "Mg", "Magnesium"),
        arrayOf<Any>(KChemicalElement.ALUMINIUM, 13, "Al", "Aluminium"),
        arrayOf<Any>(KChemicalElement.SILICON, 14, "Si", "Silicon"),
        arrayOf<Any>(KChemicalElement.PHOSPHORUS, 15, "P", "Phosphorus"),
        arrayOf<Any>(KChemicalElement.SULFUR, 16, "S", "Sulfur"),
        arrayOf<Any>(KChemicalElement.CHLORINE, 17, "Cl", "Chlorine"),
        arrayOf<Any>(KChemicalElement.ARGON, 18, "Ar", "Argon"),
        arrayOf<Any>(KChemicalElement.POTASSIUM, 19, "K", "Potassium"),
        arrayOf<Any>(KChemicalElement.CALCIUM, 20, "Ca", "Calcium"),
        arrayOf<Any>(KChemicalElement.SCANDIUM, 21, "Sc", "Scandium"),
        arrayOf<Any>(KChemicalElement.TITANIUM, 22, "Ti", "Titanium"),
        arrayOf<Any>(KChemicalElement.VANADIUM, 23, "V", "Vanadium"),
        arrayOf<Any>(KChemicalElement.CHROMIUM, 24, "Cr", "Chromium"),
        arrayOf<Any>(KChemicalElement.MANGANESE, 25, "Mn", "Manganese"),
        arrayOf<Any>(KChemicalElement.IRON, 26, "Fe", "Iron"),
        arrayOf<Any>(KChemicalElement.COBALT, 27, "Co", "Cobalt"),
        arrayOf<Any>(KChemicalElement.NICKEL, 28, "Ni", "Nickel"),
        arrayOf<Any>(KChemicalElement.COPPER, 29, "Cu", "Copper"),
        arrayOf<Any>(KChemicalElement.ZINC, 30, "Zn", "Zinc"),
        arrayOf<Any>(KChemicalElement.GALLIUM, 31, "Ga", "Gallium"),
        arrayOf<Any>(KChemicalElement.GERMANIUM, 32, "Ge", "Germanium"),
        arrayOf<Any>(KChemicalElement.ARSENIC, 33, "As", "Arsenic"),
        arrayOf<Any>(KChemicalElement.SELENIUM, 34, "Se", "Selenium"),
        arrayOf<Any>(KChemicalElement.BROMINE, 35, "Br", "Bromine"),
        arrayOf<Any>(KChemicalElement.KRYPTON, 36, "Kr", "Krypton"),
        arrayOf<Any>(KChemicalElement.RUBIDIUM, 37, "Rb", "Rubidium"),
        arrayOf<Any>(KChemicalElement.STRONTIUM, 38, "Sr", "Strontium"),
        arrayOf<Any>(KChemicalElement.YTTRIUM, 39, "Y", "Yttrium"),
        arrayOf<Any>(KChemicalElement.ZIRCONIUM, 40, "Zr", "Zirconium"),
        arrayOf<Any>(KChemicalElement.NIOBIUM, 41, "Nb", "Niobium"),
        arrayOf<Any>(KChemicalElement.MOLYBDENUM, 42, "Mo", "Molybdenum"),
        arrayOf<Any>(KChemicalElement.TECHNETIUM, 43, "Tc", "Technetium"),
        arrayOf<Any>(KChemicalElement.RUTHENIUM, 44, "Ru", "Ruthenium"),
        arrayOf<Any>(KChemicalElement.RHODIUM, 45, "Rh", "Rhodium"),
        arrayOf<Any>(KChemicalElement.PALLADIUM, 46, "Pd", "Palladium"),
        arrayOf<Any>(KChemicalElement.SILVER, 47, "Ag", "Silver"),
        arrayOf<Any>(KChemicalElement.CADMIUM, 48, "Cd", "Cadmium"),
        arrayOf<Any>(KChemicalElement.INDIUM, 49, "In", "Indium"),
        arrayOf<Any>(KChemicalElement.TIN, 50, "Sn", "Tin"),
        arrayOf<Any>(KChemicalElement.ANTIMONY, 51, "Sb", "Antimony"),
        arrayOf<Any>(KChemicalElement.TELLURIUM, 52, "Te", "Tellurium"),
        arrayOf<Any>(KChemicalElement.IODINE, 53, "I", "Iodine"),
        arrayOf<Any>(KChemicalElement.XENON, 54, "Xe", "Xenon"),
        arrayOf<Any>(KChemicalElement.CAESIUM, 55, "Cs", "Caesium"),
        arrayOf<Any>(KChemicalElement.BARIUM, 56, "Ba", "Barium"),
        arrayOf<Any>(KChemicalElement.HAFNIUM, 72, "Hf", "Hafnium"),
        arrayOf<Any>(KChemicalElement.TANTALUM, 73, "Ta", "Tantalum"),
        arrayOf<Any>(KChemicalElement.TUNGSTEN, 74, "W", "Tungsten"),
        arrayOf<Any>(KChemicalElement.RHENIUM, 75, "Re", "Rhenium"),
        arrayOf<Any>(KChemicalElement.OSMIUM, 76, "Os", "Osmium"),
        arrayOf<Any>(KChemicalElement.IRIDIUM, 77, "Ir", "Iridium"),
        arrayOf<Any>(KChemicalElement.PLATINUM, 78, "Pt", "Platinum"),
        arrayOf<Any>(KChemicalElement.GOLD, 79, "Au", "Gold"),
        arrayOf<Any>(KChemicalElement.MERCURY, 80, "Hg", "Mercury"),
        arrayOf<Any>(KChemicalElement.THALLIUM, 81, "Tl", "Thallium"),
        arrayOf<Any>(KChemicalElement.LEAD, 82, "Pb", "Lead"),
        arrayOf<Any>(KChemicalElement.BISMUTH, 83, "Bi", "Bismuth"),
        arrayOf<Any>(KChemicalElement.POLONIUM, 84, "Po", "Polonium"),
        arrayOf<Any>(KChemicalElement.ASTATINE, 85, "At", "Astatine"),
        arrayOf<Any>(KChemicalElement.RADON, 86, "Rn", "Radon"),
    )

    /** Atomic number, symbol and English name of every single element. */
    @Test
    fun `identity of every element`() = forEachCase(identityArgs()) { case ->
        `identity of every element`(case[0] as KChemicalElement, case[1] as Int, case[2] as String, case[3] as String)
    }

    private fun `identity of every element`(
        element: KChemicalElement,
        ordinalNumber: Int,
        symbol: String,
        fullName: String,
    ) {
        assertEquals(ordinalNumber, element.ordinalNumber)
        assertEquals(symbol, element.symbol)
        assertEquals(fullName, element.fullName)
    }

    /** Spot checks of the typed unit data against the literature values. */
    @Test
    fun `unit data of iron`() {
        val iron = KChemicalElement.IRON
        assertEquals(4, iron.period)
        assertEquals(8, iron.subGroup)
        assertNull(iron.mainGroup)
        assertEquals(KChemicalElementCategory.TRANSITION_METAL, iron.category)
        assertEquals(55.845, iron.molarMass into gramsPerMole, 1e-9)
        assertEquals(1.4e-10, assertNotNull(iron.atomicRadius) into meters, 1e-16)
        assertEquals(1.32e-10, assertNotNull(iron.covalentRadius) into meters, 1e-16)
        assertEquals(1811.0, assertNotNull(iron.meltingPoint) into kelvin, 1e-9)
        assertEquals(3134.0, assertNotNull(iron.boilingPoint) into kelvin, 1e-9)
        assertEquals(449.0, assertNotNull(iron.specificHeatCapacity) into joulesPerKilogramKelvin, 1e-9)
        assertEquals(80.4, assertNotNull(iron.thermalConductivity) into wattsPerMeterKelvin, 1e-9)
        assertEquals(7.902, assertNotNull(iron.ionizationEnergy) into electronVolts, 1e-9)
        assertEquals(9.7e-8, assertNotNull(iron.electricalResistivity).value, 1e-16)
        assertEquals(1.83, assertNotNull(iron.electronegativity), 1e-9)
        assertEquals(7.0923, assertNotNull(iron.molarVolume) into cubicCentimetersPerMole, 1e-3)
    }

    /** The density composes with the rest of the framework, e.g. into a mass. */
    @Test
    fun `density composes with other units`() {
        val density = assertNotNull(KChemicalElement.GOLD.density)
        val mass = density * (1 of (centi.meters pow 3))
        assertEquals(19.30, mass into grams, 1e-9)
    }

    /** Elements without a tabulated constant expose `null` and a `false` flag. */
    @Test
    fun `missing constants are null`() {
        assertNull(KChemicalElement.HELIUM.meltingPoint)
        assertFalse(KChemicalElement.HELIUM.hasMeltingPoint)
        assertNull(KChemicalElement.ARSENIC.boilingPoint)
        assertFalse(KChemicalElement.ARSENIC.hasBoilingPoint)
        assertNull(KChemicalElement.ASTATINE.density)
        assertFalse(KChemicalElement.ASTATINE.hasDensity)
        assertNull(KChemicalElement.ASTATINE.molarVolume)
        assertFalse(KChemicalElement.ASTATINE.hasMolarVolume)
        assertNull(KChemicalElement.ASTATINE.atomicRadius)
        assertFalse(KChemicalElement.ASTATINE.hasAtomicRadius)
        assertNull(KChemicalElement.POLONIUM.specificHeatCapacity)
        assertFalse(KChemicalElement.POLONIUM.hasSpecificHeatCapacity)
        assertNull(KChemicalElement.HELIUM.electronegativity)
        assertFalse(KChemicalElement.HELIUM.hasElectronegativity)
        assertNull(KChemicalElement.NEON.electronegativity)
        assertNull(KChemicalElement.ARGON.electronegativity)
        assertNull(KChemicalElement.NITROGEN.electricalResistivity)
        assertFalse(KChemicalElement.NITROGEN.hasElectricalResistivity)
        assertNull(KChemicalElement.ASTATINE.electricalResistivity)
        assertNull(KChemicalElement.ASTATINE.specificHeatCapacity)
        assertNull(KChemicalElement.ASTATINE.boilingPoint)
        assertNotNull(KChemicalElement.ASTATINE.covalentRadius)
        assertTrue(KChemicalElement.ASTATINE.hasCovalentRadius)
    }

    // --- Lookups --------------------------------------------------------------------------------

    @Test
    fun `lookups by symbol and name`() {
        assertSame(KChemicalElement.LEAD, KChemicalElement.ofSymbol("Pb"))
        assertSame(KChemicalElement.LEAD, KChemicalElement.ofSymbol("pb"))
        assertSame(KChemicalElement.LEAD, KChemicalElement.ofFullName("LEAD"))
        assertNull(KChemicalElement.ofSymbol("Uup"))
        assertNull(KChemicalElement.ofFullName("Unobtainium"))
    }

    @Test
    fun `lookup by ordinal number`() {
        assertSame(KChemicalElement.IRON, KChemicalElement.ofOrdinalNumber(26))
        assertSame(KChemicalElement.RADON, KChemicalElement.ofOrdinalNumber(86))
        assertNull(KChemicalElement.ofOrdinalNumber(0))
        assertNull(KChemicalElement.ofOrdinalNumber(57))
        assertNull(KChemicalElement.ofOrdinalNumber(92))
    }

    @Test
    fun `lookup by group and period`() {
        assertSame(KChemicalElement.LEAD, KChemicalElement.ofMainGroup(4, 6))
        assertSame(KChemicalElement.HYDROGEN, KChemicalElement.ofMainGroup(1, 1))
        assertSame(KChemicalElement.IRON, KChemicalElement.ofSubGroup(8, 4))
        assertSame(KChemicalElement.COPPER, KChemicalElement.ofSubGroup(1, 4))
        assertNull(KChemicalElement.ofMainGroup(4, 1))
        assertNull(KChemicalElement.ofSubGroup(3, 6))
        assertNull(KChemicalElement.ofSubGroup(8, 1))
    }

    @Test
    fun `lookup by period and category`() {
        assertEquals(
            listOf(KChemicalElement.HYDROGEN, KChemicalElement.HELIUM),
            KChemicalElement.ofPeriod(1),
        )
        assertEquals(
            listOf(
                KChemicalElement.LITHIUM, KChemicalElement.SODIUM, KChemicalElement.POTASSIUM,
                KChemicalElement.RUBIDIUM, KChemicalElement.CAESIUM,
            ),
            KChemicalElement.ofCategory(KChemicalElementCategory.ALKALI_METAL),
        )
        assertEquals(
            listOf(
                KChemicalElement.HELIUM, KChemicalElement.NEON, KChemicalElement.ARGON,
                KChemicalElement.KRYPTON, KChemicalElement.XENON, KChemicalElement.RADON,
            ),
            KChemicalElement.ofCategory(KChemicalElementCategory.NOBLE_GAS),
        )
        assertEquals(listOf(KChemicalElement.HYDROGEN), KChemicalElement.ofCategory(KChemicalElementCategory.HYDROGEN))
    }
}
