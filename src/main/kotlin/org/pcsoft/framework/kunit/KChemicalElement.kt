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

import org.pcsoft.framework.kunit.common.energy.KEnergyUnitInstance
import org.pcsoft.framework.kunit.common.energy.electronVolts
import org.pcsoft.framework.kunit.electric.resistivity.KResistivityUnitInstance
import org.pcsoft.framework.kunit.electric.resistivity.ohmMeters
import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.distance.pow
import org.pcsoft.framework.kunit.mechanic.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.KThermalConductivityUnitInstance
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.molarmass.KMolarMassUnitInstance
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molarvolume.KMolarVolumeUnitInstance
import org.pcsoft.framework.kunit.thermo.molarvolume.div
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.KSpecificHeatCapacityUnitInstance
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureUnitInstance
import org.pcsoft.framework.kunit.thermo.temperature.kelvin

/**
 * The density literature token `g/cm³`, built from the library's own units - the density group has no bare
 * token of its own because every spelling of it is a ratio. Used as the template the tabulated element
 * densities are read against (`7.874 of gramPerCubicCentimeter`).
 */
private val gramPerCubicCentimeter: KDensityUnitInstance = grams / (centi.meters pow 3).toVolume()

/**
 * The chemical elements of the classic school periodic table: the main and sub groups of periods 1-6
 * **without the f-block** (the lanthanides 57-71 are omitted, hence the gap between [BARIUM] and
 * [HAFNIUM]; actinides and the transactinides are not covered at all).
 *
 * Every entry carries the positional data of the periodic table ([ordinalNumber], [symbol], [fullName],
 * [period], [mainGroup]/[subGroup], [category]) plus its physical constants as **typed unit instances**
 * of this library, so they compose directly with the rest of the framework:
 *
 * ```kotlin
 * val block = KChemicalElement.IRON.density!! * (2 of liters) // mass of 2 l of iron
 * KChemicalElement.IRON.molarMass into gramsPerMole            // 55.845
 * ```
 *
 * Constants that are not meaningfully defined for an element (e.g. the melting point of helium at normal
 * pressure, or the resistivity of a noble gas) are `null`; every such property has a matching
 * `has...` companion property (e.g. [hasDensity]) to test for availability without null handling.
 *
 * @property ordinalNumber the atomic number Z, i.e. the element's index in the periodic table
 * @property symbol the element symbol, e.g. `"Pb"`
 * @property fullName the element's English name, e.g. `"Lead"` (the enum entry itself is `LEAD`)
 * @property period the period (row) of the periodic table, 1-6
 * @property category the chemical family this element belongs to
 */
enum class KChemicalElement(
    val ordinalNumber: Int,
    val symbol: String,
    val fullName: String,
    val period: Int,
    val category: KChemicalElementCategory,
    /**
     * The main group (Hauptgruppe) 1-8 for the s- and p-block elements, `null` for the transition metals
     * (which carry a [subGroup] instead).
     */
    val mainGroup: Int? = null,
    /**
     * The sub group (Nebengruppe) 1-8 for the d-block transition metals in the classic German numbering
     * (Cu = 1, Zn = 2, Sc = 3 ... Fe/Co/Ni = 8), `null` for the s- and p-block elements (which carry a
     * [mainGroup] instead).
     */
    val subGroup: Int? = null,
    /**
     * The molar mass of the element - numerically its relative atomic mass in g/mol. Always present.
     *
     * Example:
     * ```kotlin
     * KChemicalElement.IRON.molarMass into gramsPerMole // 55.845
     * ```
     */
    val molarMass: KMolarMassUnitInstance,
    /** The empirical atomic radius, or `null` if none is tabulated (astatine). */
    val atomicRadius: KLengthUnitInstance? = null,
    /** The single-bond covalent radius, or `null` if none is tabulated. */
    val covalentRadius: KLengthUnitInstance? = null,
    /**
     * The density at standard conditions (solids and liquids in their stable modification, gases at 0 °C
     * and 101.325 kPa), or `null` if none is tabulated (astatine).
     */
    val density: KDensityUnitInstance? = null,
    /** The melting point at normal pressure, or `null` if the element has none (helium). */
    val meltingPoint: KTemperatureUnitInstance? = null,
    /** The boiling point at normal pressure, or `null` if the element sublimes instead (arsenic). */
    val boilingPoint: KTemperatureUnitInstance? = null,
    /** The specific heat capacity at 25 °C, or `null` if none is tabulated. */
    val specificHeatCapacity: KSpecificHeatCapacityUnitInstance? = null,
    /** The thermal conductivity at 25 °C, or `null` if none is tabulated. */
    val thermalConductivity: KThermalConductivityUnitInstance? = null,
    /**
     * The first ionization energy per atom, or `null` if none is tabulated. Stored as a typed energy, so it
     * can be read in any energy unit.
     *
     * Example:
     * ```kotlin
     * KChemicalElement.IRON.ionizationEnergy!! into electronVolts // ≈ 7.902
     * ```
     */
    val ionizationEnergy: KEnergyUnitInstance? = null,
    /**
     * The electrical resistivity at 20 °C, or `null` for elements with no meaningful tabulated value
     * (most gases and several nonmetals).
     */
    val electricalResistivity: KResistivityUnitInstance? = null,
    /**
     * The electronegativity on the Pauling scale - a dimensionless ratio, therefore a plain [Double].
     * `null` for the noble gases helium, neon and argon, which have no defined Pauling value.
     */
    val electronegativity: Double? = null,
) {
    // --- Period 1 -------------------------------------------------------------------------------

    /** Hydrogen (H, Z = 1), the lightest element. */
    HYDROGEN(
        1, "H", "Hydrogen", 1, KChemicalElementCategory.HYDROGEN, mainGroup = 1,
        molarMass = 1.008 of gramsPerMole,
        atomicRadius = 25.0 of pico.meters, covalentRadius = 31.0 of pico.meters,
        density = 8.988e-5 of gramPerCubicCentimeter,
        meltingPoint = 13.99 of kelvin, boilingPoint = 20.271 of kelvin,
        specificHeatCapacity = 14304.0 of joulesPerKilogramKelvin,
        thermalConductivity = 0.1805 of wattsPerMeterKelvin,
        ionizationEnergy = 13.598 of electronVolts,
        electronegativity = 2.20,
    ),

    /** Helium (He, Z = 2), the first noble gas; it has no melting point at normal pressure. */
    HELIUM(
        2, "He", "Helium", 1, KChemicalElementCategory.NOBLE_GAS, mainGroup = 8,
        molarMass = 4.002602 of gramsPerMole,
        atomicRadius = 120.0 of pico.meters, covalentRadius = 28.0 of pico.meters,
        density = 1.785e-4 of gramPerCubicCentimeter,
        boilingPoint = 4.222 of kelvin,
        specificHeatCapacity = 5193.0 of joulesPerKilogramKelvin,
        thermalConductivity = 0.1513 of wattsPerMeterKelvin,
        ionizationEnergy = 24.587 of electronVolts,
    ),

    // --- Period 2 -------------------------------------------------------------------------------

    /** Lithium (Li, Z = 3), the lightest metal. */
    LITHIUM(
        3, "Li", "Lithium", 2, KChemicalElementCategory.ALKALI_METAL, mainGroup = 1,
        molarMass = 6.94 of gramsPerMole,
        atomicRadius = 145.0 of pico.meters, covalentRadius = 128.0 of pico.meters,
        density = 0.534 of gramPerCubicCentimeter,
        meltingPoint = 453.65 of kelvin, boilingPoint = 1603.0 of kelvin,
        specificHeatCapacity = 3582.0 of joulesPerKilogramKelvin,
        thermalConductivity = 84.8 of wattsPerMeterKelvin,
        ionizationEnergy = 5.392 of electronVolts,
        electricalResistivity = 9.28e-8 of ohmMeters,
        electronegativity = 0.98,
    ),

    /** Beryllium (Be, Z = 4). */
    BERYLLIUM(
        4, "Be", "Beryllium", 2, KChemicalElementCategory.ALKALINE_EARTH_METAL, mainGroup = 2,
        molarMass = 9.0121831 of gramsPerMole,
        atomicRadius = 105.0 of pico.meters, covalentRadius = 96.0 of pico.meters,
        density = 1.85 of gramPerCubicCentimeter,
        meltingPoint = 1560.0 of kelvin, boilingPoint = 2742.0 of kelvin,
        specificHeatCapacity = 1825.0 of joulesPerKilogramKelvin,
        thermalConductivity = 200.0 of wattsPerMeterKelvin,
        ionizationEnergy = 9.323 of electronVolts,
        electricalResistivity = 3.6e-8 of ohmMeters,
        electronegativity = 1.57,
    ),

    /** Boron (B, Z = 5). */
    BORON(
        5, "B", "Boron", 2, KChemicalElementCategory.METALLOID, mainGroup = 3,
        molarMass = 10.81 of gramsPerMole,
        atomicRadius = 85.0 of pico.meters, covalentRadius = 84.0 of pico.meters,
        density = 2.34 of gramPerCubicCentimeter,
        meltingPoint = 2349.0 of kelvin, boilingPoint = 4200.0 of kelvin,
        specificHeatCapacity = 1026.0 of joulesPerKilogramKelvin,
        thermalConductivity = 27.4 of wattsPerMeterKelvin,
        ionizationEnergy = 8.298 of electronVolts,
        electricalResistivity = 1.0e4 of ohmMeters,
        electronegativity = 2.04,
    ),

    /** Carbon (C, Z = 6); the values refer to graphite, which sublimes rather than boils. */
    CARBON(
        6, "C", "Carbon", 2, KChemicalElementCategory.NONMETAL, mainGroup = 4,
        molarMass = 12.011 of gramsPerMole,
        atomicRadius = 70.0 of pico.meters, covalentRadius = 76.0 of pico.meters,
        density = 2.267 of gramPerCubicCentimeter,
        meltingPoint = 3823.0 of kelvin, boilingPoint = 4098.0 of kelvin,
        specificHeatCapacity = 709.0 of joulesPerKilogramKelvin,
        thermalConductivity = 140.0 of wattsPerMeterKelvin,
        ionizationEnergy = 11.260 of electronVolts,
        electricalResistivity = 7.837e-6 of ohmMeters,
        electronegativity = 2.55,
    ),

    /** Nitrogen (N, Z = 7). */
    NITROGEN(
        7, "N", "Nitrogen", 2, KChemicalElementCategory.NONMETAL, mainGroup = 5,
        molarMass = 14.007 of gramsPerMole,
        atomicRadius = 65.0 of pico.meters, covalentRadius = 71.0 of pico.meters,
        density = 1.2506e-3 of gramPerCubicCentimeter,
        meltingPoint = 63.15 of kelvin, boilingPoint = 77.355 of kelvin,
        specificHeatCapacity = 1040.0 of joulesPerKilogramKelvin,
        thermalConductivity = 0.02583 of wattsPerMeterKelvin,
        ionizationEnergy = 14.534 of electronVolts,
        electronegativity = 3.04,
    ),

    /** Oxygen (O, Z = 8). */
    OXYGEN(
        8, "O", "Oxygen", 2, KChemicalElementCategory.NONMETAL, mainGroup = 6,
        molarMass = 15.999 of gramsPerMole,
        atomicRadius = 60.0 of pico.meters, covalentRadius = 66.0 of pico.meters,
        density = 1.429e-3 of gramPerCubicCentimeter,
        meltingPoint = 54.36 of kelvin, boilingPoint = 90.188 of kelvin,
        specificHeatCapacity = 918.0 of joulesPerKilogramKelvin,
        thermalConductivity = 0.02658 of wattsPerMeterKelvin,
        ionizationEnergy = 13.618 of electronVolts,
        electronegativity = 3.44,
    ),

    /** Fluorine (F, Z = 9), the most electronegative element. */
    FLUORINE(
        9, "F", "Fluorine", 2, KChemicalElementCategory.HALOGEN, mainGroup = 7,
        molarMass = 18.998403163 of gramsPerMole,
        atomicRadius = 50.0 of pico.meters, covalentRadius = 57.0 of pico.meters,
        density = 1.696e-3 of gramPerCubicCentimeter,
        meltingPoint = 53.48 of kelvin, boilingPoint = 85.03 of kelvin,
        specificHeatCapacity = 824.0 of joulesPerKilogramKelvin,
        thermalConductivity = 0.0277 of wattsPerMeterKelvin,
        ionizationEnergy = 17.423 of electronVolts,
        electronegativity = 3.98,
    ),

    /** Neon (Ne, Z = 10). */
    NEON(
        10, "Ne", "Neon", 2, KChemicalElementCategory.NOBLE_GAS, mainGroup = 8,
        molarMass = 20.1797 of gramsPerMole,
        atomicRadius = 160.0 of pico.meters, covalentRadius = 58.0 of pico.meters,
        density = 8.999e-4 of gramPerCubicCentimeter,
        meltingPoint = 24.56 of kelvin, boilingPoint = 27.104 of kelvin,
        specificHeatCapacity = 1030.0 of joulesPerKilogramKelvin,
        thermalConductivity = 0.0491 of wattsPerMeterKelvin,
        ionizationEnergy = 21.565 of electronVolts,
    ),

    // --- Period 3 -------------------------------------------------------------------------------

    /** Sodium (Na, Z = 11). */
    SODIUM(
        11, "Na", "Sodium", 3, KChemicalElementCategory.ALKALI_METAL, mainGroup = 1,
        molarMass = 22.98976928 of gramsPerMole,
        atomicRadius = 180.0 of pico.meters, covalentRadius = 166.0 of pico.meters,
        density = 0.968 of gramPerCubicCentimeter,
        meltingPoint = 370.944 of kelvin, boilingPoint = 1156.09 of kelvin,
        specificHeatCapacity = 1228.0 of joulesPerKilogramKelvin,
        thermalConductivity = 142.0 of wattsPerMeterKelvin,
        ionizationEnergy = 5.139 of electronVolts,
        electricalResistivity = 4.77e-8 of ohmMeters,
        electronegativity = 0.93,
    ),

    /** Magnesium (Mg, Z = 12). */
    MAGNESIUM(
        12, "Mg", "Magnesium", 3, KChemicalElementCategory.ALKALINE_EARTH_METAL, mainGroup = 2,
        molarMass = 24.305 of gramsPerMole,
        atomicRadius = 150.0 of pico.meters, covalentRadius = 141.0 of pico.meters,
        density = 1.738 of gramPerCubicCentimeter,
        meltingPoint = 923.0 of kelvin, boilingPoint = 1363.0 of kelvin,
        specificHeatCapacity = 1023.0 of joulesPerKilogramKelvin,
        thermalConductivity = 156.0 of wattsPerMeterKelvin,
        ionizationEnergy = 7.646 of electronVolts,
        electricalResistivity = 4.39e-8 of ohmMeters,
        electronegativity = 1.31,
    ),

    /** Aluminium (Al, Z = 13). */
    ALUMINIUM(
        13, "Al", "Aluminium", 3, KChemicalElementCategory.POST_TRANSITION_METAL, mainGroup = 3,
        molarMass = 26.9815385 of gramsPerMole,
        atomicRadius = 125.0 of pico.meters, covalentRadius = 121.0 of pico.meters,
        density = 2.70 of gramPerCubicCentimeter,
        meltingPoint = 933.47 of kelvin, boilingPoint = 2743.0 of kelvin,
        specificHeatCapacity = 897.0 of joulesPerKilogramKelvin,
        thermalConductivity = 237.0 of wattsPerMeterKelvin,
        ionizationEnergy = 5.986 of electronVolts,
        electricalResistivity = 2.65e-8 of ohmMeters,
        electronegativity = 1.61,
    ),

    /** Silicon (Si, Z = 14). */
    SILICON(
        14, "Si", "Silicon", 3, KChemicalElementCategory.METALLOID, mainGroup = 4,
        molarMass = 28.085 of gramsPerMole,
        atomicRadius = 110.0 of pico.meters, covalentRadius = 111.0 of pico.meters,
        density = 2.3290 of gramPerCubicCentimeter,
        meltingPoint = 1687.0 of kelvin, boilingPoint = 3538.0 of kelvin,
        specificHeatCapacity = 705.0 of joulesPerKilogramKelvin,
        thermalConductivity = 149.0 of wattsPerMeterKelvin,
        ionizationEnergy = 8.152 of electronVolts,
        electricalResistivity = 2.3e3 of ohmMeters,
        electronegativity = 1.90,
    ),

    /** Phosphorus (P, Z = 15); the values refer to the white modification. */
    PHOSPHORUS(
        15, "P", "Phosphorus", 3, KChemicalElementCategory.NONMETAL, mainGroup = 5,
        molarMass = 30.973761998 of gramsPerMole,
        atomicRadius = 100.0 of pico.meters, covalentRadius = 107.0 of pico.meters,
        density = 1.823 of gramPerCubicCentimeter,
        meltingPoint = 317.3 of kelvin, boilingPoint = 553.7 of kelvin,
        specificHeatCapacity = 769.0 of joulesPerKilogramKelvin,
        thermalConductivity = 0.236 of wattsPerMeterKelvin,
        ionizationEnergy = 10.487 of electronVolts,
        electricalResistivity = 1.0e7 of ohmMeters,
        electronegativity = 2.19,
    ),

    /** Sulfur (S, Z = 16). */
    SULFUR(
        16, "S", "Sulfur", 3, KChemicalElementCategory.NONMETAL, mainGroup = 6,
        molarMass = 32.06 of gramsPerMole,
        atomicRadius = 100.0 of pico.meters, covalentRadius = 105.0 of pico.meters,
        density = 2.07 of gramPerCubicCentimeter,
        meltingPoint = 388.36 of kelvin, boilingPoint = 717.8 of kelvin,
        specificHeatCapacity = 710.0 of joulesPerKilogramKelvin,
        thermalConductivity = 0.205 of wattsPerMeterKelvin,
        ionizationEnergy = 10.360 of electronVolts,
        electricalResistivity = 1.0e15 of ohmMeters,
        electronegativity = 2.58,
    ),

    /** Chlorine (Cl, Z = 17). */
    CHLORINE(
        17, "Cl", "Chlorine", 3, KChemicalElementCategory.HALOGEN, mainGroup = 7,
        molarMass = 35.45 of gramsPerMole,
        atomicRadius = 100.0 of pico.meters, covalentRadius = 102.0 of pico.meters,
        density = 3.214e-3 of gramPerCubicCentimeter,
        meltingPoint = 171.6 of kelvin, boilingPoint = 239.11 of kelvin,
        specificHeatCapacity = 479.0 of joulesPerKilogramKelvin,
        thermalConductivity = 0.0089 of wattsPerMeterKelvin,
        ionizationEnergy = 12.968 of electronVolts,
        electronegativity = 3.16,
    ),

    /** Argon (Ar, Z = 18). */
    ARGON(
        18, "Ar", "Argon", 3, KChemicalElementCategory.NOBLE_GAS, mainGroup = 8,
        molarMass = 39.948 of gramsPerMole,
        atomicRadius = 71.0 of pico.meters, covalentRadius = 106.0 of pico.meters,
        density = 1.7837e-3 of gramPerCubicCentimeter,
        meltingPoint = 83.81 of kelvin, boilingPoint = 87.302 of kelvin,
        specificHeatCapacity = 520.0 of joulesPerKilogramKelvin,
        thermalConductivity = 0.01772 of wattsPerMeterKelvin,
        ionizationEnergy = 15.760 of electronVolts,
    ),

    // --- Period 4 -------------------------------------------------------------------------------

    /** Potassium (K, Z = 19). */
    POTASSIUM(
        19, "K", "Potassium", 4, KChemicalElementCategory.ALKALI_METAL, mainGroup = 1,
        molarMass = 39.0983 of gramsPerMole,
        atomicRadius = 220.0 of pico.meters, covalentRadius = 203.0 of pico.meters,
        density = 0.89 of gramPerCubicCentimeter,
        meltingPoint = 336.7 of kelvin, boilingPoint = 1032.0 of kelvin,
        specificHeatCapacity = 757.0 of joulesPerKilogramKelvin,
        thermalConductivity = 102.5 of wattsPerMeterKelvin,
        ionizationEnergy = 4.341 of electronVolts,
        electricalResistivity = 7.2e-8 of ohmMeters,
        electronegativity = 0.82,
    ),

    /** Calcium (Ca, Z = 20). */
    CALCIUM(
        20, "Ca", "Calcium", 4, KChemicalElementCategory.ALKALINE_EARTH_METAL, mainGroup = 2,
        molarMass = 40.078 of gramsPerMole,
        atomicRadius = 180.0 of pico.meters, covalentRadius = 176.0 of pico.meters,
        density = 1.55 of gramPerCubicCentimeter,
        meltingPoint = 1115.0 of kelvin, boilingPoint = 1757.0 of kelvin,
        specificHeatCapacity = 647.0 of joulesPerKilogramKelvin,
        thermalConductivity = 201.0 of wattsPerMeterKelvin,
        ionizationEnergy = 6.113 of electronVolts,
        electricalResistivity = 3.36e-8 of ohmMeters,
        electronegativity = 1.00,
    ),

    /** Scandium (Sc, Z = 21). */
    SCANDIUM(
        21, "Sc", "Scandium", 4, KChemicalElementCategory.TRANSITION_METAL, subGroup = 3,
        molarMass = 44.955908 of gramsPerMole,
        atomicRadius = 160.0 of pico.meters, covalentRadius = 170.0 of pico.meters,
        density = 2.985 of gramPerCubicCentimeter,
        meltingPoint = 1814.0 of kelvin, boilingPoint = 3109.0 of kelvin,
        specificHeatCapacity = 568.0 of joulesPerKilogramKelvin,
        thermalConductivity = 15.8 of wattsPerMeterKelvin,
        ionizationEnergy = 6.561 of electronVolts,
        electricalResistivity = 5.5e-7 of ohmMeters,
        electronegativity = 1.36,
    ),

    /** Titanium (Ti, Z = 22). */
    TITANIUM(
        22, "Ti", "Titanium", 4, KChemicalElementCategory.TRANSITION_METAL, subGroup = 4,
        molarMass = 47.867 of gramsPerMole,
        atomicRadius = 140.0 of pico.meters, covalentRadius = 160.0 of pico.meters,
        density = 4.506 of gramPerCubicCentimeter,
        meltingPoint = 1941.0 of kelvin, boilingPoint = 3560.0 of kelvin,
        specificHeatCapacity = 523.0 of joulesPerKilogramKelvin,
        thermalConductivity = 21.9 of wattsPerMeterKelvin,
        ionizationEnergy = 6.828 of electronVolts,
        electricalResistivity = 4.2e-7 of ohmMeters,
        electronegativity = 1.54,
    ),

    /** Vanadium (V, Z = 23). */
    VANADIUM(
        23, "V", "Vanadium", 4, KChemicalElementCategory.TRANSITION_METAL, subGroup = 5,
        molarMass = 50.9415 of gramsPerMole,
        atomicRadius = 135.0 of pico.meters, covalentRadius = 153.0 of pico.meters,
        density = 6.11 of gramPerCubicCentimeter,
        meltingPoint = 2183.0 of kelvin, boilingPoint = 3680.0 of kelvin,
        specificHeatCapacity = 489.0 of joulesPerKilogramKelvin,
        thermalConductivity = 30.7 of wattsPerMeterKelvin,
        ionizationEnergy = 6.746 of electronVolts,
        electricalResistivity = 1.97e-7 of ohmMeters,
        electronegativity = 1.63,
    ),

    /** Chromium (Cr, Z = 24). */
    CHROMIUM(
        24, "Cr", "Chromium", 4, KChemicalElementCategory.TRANSITION_METAL, subGroup = 6,
        molarMass = 51.9961 of gramsPerMole,
        atomicRadius = 140.0 of pico.meters, covalentRadius = 139.0 of pico.meters,
        density = 7.15 of gramPerCubicCentimeter,
        meltingPoint = 2180.0 of kelvin, boilingPoint = 2944.0 of kelvin,
        specificHeatCapacity = 449.0 of joulesPerKilogramKelvin,
        thermalConductivity = 93.9 of wattsPerMeterKelvin,
        ionizationEnergy = 6.767 of electronVolts,
        electricalResistivity = 1.25e-7 of ohmMeters,
        electronegativity = 1.66,
    ),

    /** Manganese (Mn, Z = 25). */
    MANGANESE(
        25, "Mn", "Manganese", 4, KChemicalElementCategory.TRANSITION_METAL, subGroup = 7,
        molarMass = 54.938044 of gramsPerMole,
        atomicRadius = 140.0 of pico.meters, covalentRadius = 139.0 of pico.meters,
        density = 7.21 of gramPerCubicCentimeter,
        meltingPoint = 1519.0 of kelvin, boilingPoint = 2334.0 of kelvin,
        specificHeatCapacity = 479.0 of joulesPerKilogramKelvin,
        thermalConductivity = 7.81 of wattsPerMeterKelvin,
        ionizationEnergy = 7.434 of electronVolts,
        electricalResistivity = 1.44e-6 of ohmMeters,
        electronegativity = 1.55,
    ),

    /** Iron (Fe, Z = 26). */
    IRON(
        26, "Fe", "Iron", 4, KChemicalElementCategory.TRANSITION_METAL, subGroup = 8,
        molarMass = 55.845 of gramsPerMole,
        atomicRadius = 140.0 of pico.meters, covalentRadius = 132.0 of pico.meters,
        density = 7.874 of gramPerCubicCentimeter,
        meltingPoint = 1811.0 of kelvin, boilingPoint = 3134.0 of kelvin,
        specificHeatCapacity = 449.0 of joulesPerKilogramKelvin,
        thermalConductivity = 80.4 of wattsPerMeterKelvin,
        ionizationEnergy = 7.902 of electronVolts,
        electricalResistivity = 9.7e-8 of ohmMeters,
        electronegativity = 1.83,
    ),

    /** Cobalt (Co, Z = 27). */
    COBALT(
        27, "Co", "Cobalt", 4, KChemicalElementCategory.TRANSITION_METAL, subGroup = 8,
        molarMass = 58.933194 of gramsPerMole,
        atomicRadius = 135.0 of pico.meters, covalentRadius = 126.0 of pico.meters,
        density = 8.90 of gramPerCubicCentimeter,
        meltingPoint = 1768.0 of kelvin, boilingPoint = 3200.0 of kelvin,
        specificHeatCapacity = 421.0 of joulesPerKilogramKelvin,
        thermalConductivity = 100.0 of wattsPerMeterKelvin,
        ionizationEnergy = 7.881 of electronVolts,
        electricalResistivity = 6.24e-8 of ohmMeters,
        electronegativity = 1.88,
    ),

    /** Nickel (Ni, Z = 28). */
    NICKEL(
        28, "Ni", "Nickel", 4, KChemicalElementCategory.TRANSITION_METAL, subGroup = 8,
        molarMass = 58.6934 of gramsPerMole,
        atomicRadius = 135.0 of pico.meters, covalentRadius = 124.0 of pico.meters,
        density = 8.908 of gramPerCubicCentimeter,
        meltingPoint = 1728.0 of kelvin, boilingPoint = 3003.0 of kelvin,
        specificHeatCapacity = 445.0 of joulesPerKilogramKelvin,
        thermalConductivity = 90.9 of wattsPerMeterKelvin,
        ionizationEnergy = 7.640 of electronVolts,
        electricalResistivity = 6.99e-8 of ohmMeters,
        electronegativity = 1.91,
    ),

    /** Copper (Cu, Z = 29). */
    COPPER(
        29, "Cu", "Copper", 4, KChemicalElementCategory.TRANSITION_METAL, subGroup = 1,
        molarMass = 63.546 of gramsPerMole,
        atomicRadius = 135.0 of pico.meters, covalentRadius = 132.0 of pico.meters,
        density = 8.96 of gramPerCubicCentimeter,
        meltingPoint = 1357.77 of kelvin, boilingPoint = 2835.0 of kelvin,
        specificHeatCapacity = 385.0 of joulesPerKilogramKelvin,
        thermalConductivity = 401.0 of wattsPerMeterKelvin,
        ionizationEnergy = 7.726 of electronVolts,
        electricalResistivity = 1.68e-8 of ohmMeters,
        electronegativity = 1.90,
    ),

    /** Zinc (Zn, Z = 30). */
    ZINC(
        30, "Zn", "Zinc", 4, KChemicalElementCategory.TRANSITION_METAL, subGroup = 2,
        molarMass = 65.38 of gramsPerMole,
        atomicRadius = 135.0 of pico.meters, covalentRadius = 122.0 of pico.meters,
        density = 7.14 of gramPerCubicCentimeter,
        meltingPoint = 692.68 of kelvin, boilingPoint = 1180.0 of kelvin,
        specificHeatCapacity = 388.0 of joulesPerKilogramKelvin,
        thermalConductivity = 116.0 of wattsPerMeterKelvin,
        ionizationEnergy = 9.394 of electronVolts,
        electricalResistivity = 5.9e-8 of ohmMeters,
        electronegativity = 1.65,
    ),

    /** Gallium (Ga, Z = 31), which melts in the hand at about 30 °C. */
    GALLIUM(
        31, "Ga", "Gallium", 4, KChemicalElementCategory.POST_TRANSITION_METAL, mainGroup = 3,
        molarMass = 69.723 of gramsPerMole,
        atomicRadius = 130.0 of pico.meters, covalentRadius = 122.0 of pico.meters,
        density = 5.91 of gramPerCubicCentimeter,
        meltingPoint = 302.9146 of kelvin, boilingPoint = 2673.0 of kelvin,
        specificHeatCapacity = 371.0 of joulesPerKilogramKelvin,
        thermalConductivity = 40.6 of wattsPerMeterKelvin,
        ionizationEnergy = 5.999 of electronVolts,
        electricalResistivity = 1.4e-7 of ohmMeters,
        electronegativity = 1.81,
    ),

    /** Germanium (Ge, Z = 32). */
    GERMANIUM(
        32, "Ge", "Germanium", 4, KChemicalElementCategory.METALLOID, mainGroup = 4,
        molarMass = 72.630 of gramsPerMole,
        atomicRadius = 125.0 of pico.meters, covalentRadius = 120.0 of pico.meters,
        density = 5.323 of gramPerCubicCentimeter,
        meltingPoint = 1211.4 of kelvin, boilingPoint = 3106.0 of kelvin,
        specificHeatCapacity = 320.0 of joulesPerKilogramKelvin,
        thermalConductivity = 60.2 of wattsPerMeterKelvin,
        ionizationEnergy = 7.900 of electronVolts,
        electricalResistivity = 1.0 of ohmMeters,
        electronegativity = 2.01,
    ),

    /** Arsenic (As, Z = 33), which sublimes instead of boiling at normal pressure. */
    ARSENIC(
        33, "As", "Arsenic", 4, KChemicalElementCategory.METALLOID, mainGroup = 5,
        molarMass = 74.921595 of gramsPerMole,
        atomicRadius = 115.0 of pico.meters, covalentRadius = 119.0 of pico.meters,
        density = 5.727 of gramPerCubicCentimeter,
        meltingPoint = 1090.0 of kelvin,
        specificHeatCapacity = 329.0 of joulesPerKilogramKelvin,
        thermalConductivity = 50.2 of wattsPerMeterKelvin,
        ionizationEnergy = 9.789 of electronVolts,
        electricalResistivity = 3.33e-7 of ohmMeters,
        electronegativity = 2.18,
    ),

    /** Selenium (Se, Z = 34); the values refer to the gray modification. */
    SELENIUM(
        34, "Se", "Selenium", 4, KChemicalElementCategory.NONMETAL, mainGroup = 6,
        molarMass = 78.971 of gramsPerMole,
        atomicRadius = 115.0 of pico.meters, covalentRadius = 120.0 of pico.meters,
        density = 4.81 of gramPerCubicCentimeter,
        meltingPoint = 494.0 of kelvin, boilingPoint = 958.0 of kelvin,
        specificHeatCapacity = 321.0 of joulesPerKilogramKelvin,
        thermalConductivity = 0.519 of wattsPerMeterKelvin,
        ionizationEnergy = 9.752 of electronVolts,
        electronegativity = 2.55,
    ),

    /** Bromine (Br, Z = 35), one of the two elements that are liquid at room temperature. */
    BROMINE(
        35, "Br", "Bromine", 4, KChemicalElementCategory.HALOGEN, mainGroup = 7,
        molarMass = 79.904 of gramsPerMole,
        atomicRadius = 115.0 of pico.meters, covalentRadius = 120.0 of pico.meters,
        density = 3.1028 of gramPerCubicCentimeter,
        meltingPoint = 265.8 of kelvin, boilingPoint = 332.0 of kelvin,
        specificHeatCapacity = 474.0 of joulesPerKilogramKelvin,
        thermalConductivity = 0.122 of wattsPerMeterKelvin,
        ionizationEnergy = 11.814 of electronVolts,
        electricalResistivity = 7.8e10 of ohmMeters,
        electronegativity = 2.96,
    ),

    /** Krypton (Kr, Z = 36). */
    KRYPTON(
        36, "Kr", "Krypton", 4, KChemicalElementCategory.NOBLE_GAS, mainGroup = 8,
        molarMass = 83.798 of gramsPerMole,
        atomicRadius = 88.0 of pico.meters, covalentRadius = 116.0 of pico.meters,
        density = 3.733e-3 of gramPerCubicCentimeter,
        meltingPoint = 115.78 of kelvin, boilingPoint = 119.93 of kelvin,
        specificHeatCapacity = 248.0 of joulesPerKilogramKelvin,
        thermalConductivity = 0.00943 of wattsPerMeterKelvin,
        ionizationEnergy = 13.999 of electronVolts,
        electronegativity = 3.00,
    ),

    // --- Period 5 -------------------------------------------------------------------------------

    /** Rubidium (Rb, Z = 37). */
    RUBIDIUM(
        37, "Rb", "Rubidium", 5, KChemicalElementCategory.ALKALI_METAL, mainGroup = 1,
        molarMass = 85.4678 of gramsPerMole,
        atomicRadius = 235.0 of pico.meters, covalentRadius = 220.0 of pico.meters,
        density = 1.532 of gramPerCubicCentimeter,
        meltingPoint = 312.45 of kelvin, boilingPoint = 961.0 of kelvin,
        specificHeatCapacity = 363.0 of joulesPerKilogramKelvin,
        thermalConductivity = 58.2 of wattsPerMeterKelvin,
        ionizationEnergy = 4.177 of electronVolts,
        electricalResistivity = 1.28e-7 of ohmMeters,
        electronegativity = 0.82,
    ),

    /** Strontium (Sr, Z = 38). */
    STRONTIUM(
        38, "Sr", "Strontium", 5, KChemicalElementCategory.ALKALINE_EARTH_METAL, mainGroup = 2,
        molarMass = 87.62 of gramsPerMole,
        atomicRadius = 200.0 of pico.meters, covalentRadius = 195.0 of pico.meters,
        density = 2.64 of gramPerCubicCentimeter,
        meltingPoint = 1050.0 of kelvin, boilingPoint = 1650.0 of kelvin,
        specificHeatCapacity = 306.0 of joulesPerKilogramKelvin,
        thermalConductivity = 35.4 of wattsPerMeterKelvin,
        ionizationEnergy = 5.695 of electronVolts,
        electricalResistivity = 1.32e-7 of ohmMeters,
        electronegativity = 0.95,
    ),

    /** Yttrium (Y, Z = 39). */
    YTTRIUM(
        39, "Y", "Yttrium", 5, KChemicalElementCategory.TRANSITION_METAL, subGroup = 3,
        molarMass = 88.90584 of gramsPerMole,
        atomicRadius = 180.0 of pico.meters, covalentRadius = 190.0 of pico.meters,
        density = 4.472 of gramPerCubicCentimeter,
        meltingPoint = 1799.0 of kelvin, boilingPoint = 3203.0 of kelvin,
        specificHeatCapacity = 298.0 of joulesPerKilogramKelvin,
        thermalConductivity = 17.2 of wattsPerMeterKelvin,
        ionizationEnergy = 6.217 of electronVolts,
        electricalResistivity = 5.96e-7 of ohmMeters,
        electronegativity = 1.22,
    ),

    /** Zirconium (Zr, Z = 40). */
    ZIRCONIUM(
        40, "Zr", "Zirconium", 5, KChemicalElementCategory.TRANSITION_METAL, subGroup = 4,
        molarMass = 91.224 of gramsPerMole,
        atomicRadius = 155.0 of pico.meters, covalentRadius = 175.0 of pico.meters,
        density = 6.52 of gramPerCubicCentimeter,
        meltingPoint = 2128.0 of kelvin, boilingPoint = 4650.0 of kelvin,
        specificHeatCapacity = 278.0 of joulesPerKilogramKelvin,
        thermalConductivity = 22.6 of wattsPerMeterKelvin,
        ionizationEnergy = 6.634 of electronVolts,
        electricalResistivity = 4.21e-7 of ohmMeters,
        electronegativity = 1.33,
    ),

    /** Niobium (Nb, Z = 41). */
    NIOBIUM(
        41, "Nb", "Niobium", 5, KChemicalElementCategory.TRANSITION_METAL, subGroup = 5,
        molarMass = 92.90637 of gramsPerMole,
        atomicRadius = 145.0 of pico.meters, covalentRadius = 164.0 of pico.meters,
        density = 8.57 of gramPerCubicCentimeter,
        meltingPoint = 2750.0 of kelvin, boilingPoint = 5017.0 of kelvin,
        specificHeatCapacity = 265.0 of joulesPerKilogramKelvin,
        thermalConductivity = 53.7 of wattsPerMeterKelvin,
        ionizationEnergy = 6.759 of electronVolts,
        electricalResistivity = 1.52e-7 of ohmMeters,
        electronegativity = 1.6,
    ),

    /** Molybdenum (Mo, Z = 42). */
    MOLYBDENUM(
        42, "Mo", "Molybdenum", 5, KChemicalElementCategory.TRANSITION_METAL, subGroup = 6,
        molarMass = 95.95 of gramsPerMole,
        atomicRadius = 145.0 of pico.meters, covalentRadius = 154.0 of pico.meters,
        density = 10.28 of gramPerCubicCentimeter,
        meltingPoint = 2896.0 of kelvin, boilingPoint = 4912.0 of kelvin,
        specificHeatCapacity = 251.0 of joulesPerKilogramKelvin,
        thermalConductivity = 138.0 of wattsPerMeterKelvin,
        ionizationEnergy = 7.092 of electronVolts,
        electricalResistivity = 5.34e-8 of ohmMeters,
        electronegativity = 2.16,
    ),

    /** Technetium (Tc, Z = 43), the lightest element without a stable isotope. */
    TECHNETIUM(
        43, "Tc", "Technetium", 5, KChemicalElementCategory.TRANSITION_METAL, subGroup = 7,
        molarMass = 98.0 of gramsPerMole,
        atomicRadius = 135.0 of pico.meters, covalentRadius = 147.0 of pico.meters,
        density = 11.0 of gramPerCubicCentimeter,
        meltingPoint = 2430.0 of kelvin, boilingPoint = 4538.0 of kelvin,
        specificHeatCapacity = 210.0 of joulesPerKilogramKelvin,
        thermalConductivity = 50.6 of wattsPerMeterKelvin,
        ionizationEnergy = 7.28 of electronVolts,
        electricalResistivity = 2.0e-7 of ohmMeters,
        electronegativity = 1.9,
    ),

    /** Ruthenium (Ru, Z = 44). */
    RUTHENIUM(
        44, "Ru", "Ruthenium", 5, KChemicalElementCategory.TRANSITION_METAL, subGroup = 8,
        molarMass = 101.07 of gramsPerMole,
        atomicRadius = 130.0 of pico.meters, covalentRadius = 146.0 of pico.meters,
        density = 12.45 of gramPerCubicCentimeter,
        meltingPoint = 2607.0 of kelvin, boilingPoint = 4423.0 of kelvin,
        specificHeatCapacity = 238.0 of joulesPerKilogramKelvin,
        thermalConductivity = 117.0 of wattsPerMeterKelvin,
        ionizationEnergy = 7.361 of electronVolts,
        electricalResistivity = 7.1e-8 of ohmMeters,
        electronegativity = 2.2,
    ),

    /** Rhodium (Rh, Z = 45). */
    RHODIUM(
        45, "Rh", "Rhodium", 5, KChemicalElementCategory.TRANSITION_METAL, subGroup = 8,
        molarMass = 102.9055 of gramsPerMole,
        atomicRadius = 135.0 of pico.meters, covalentRadius = 142.0 of pico.meters,
        density = 12.41 of gramPerCubicCentimeter,
        meltingPoint = 2237.0 of kelvin, boilingPoint = 3968.0 of kelvin,
        specificHeatCapacity = 243.0 of joulesPerKilogramKelvin,
        thermalConductivity = 150.0 of wattsPerMeterKelvin,
        ionizationEnergy = 7.459 of electronVolts,
        electricalResistivity = 4.3e-8 of ohmMeters,
        electronegativity = 2.28,
    ),

    /** Palladium (Pd, Z = 46). */
    PALLADIUM(
        46, "Pd", "Palladium", 5, KChemicalElementCategory.TRANSITION_METAL, subGroup = 8,
        molarMass = 106.42 of gramsPerMole,
        atomicRadius = 140.0 of pico.meters, covalentRadius = 139.0 of pico.meters,
        density = 12.023 of gramPerCubicCentimeter,
        meltingPoint = 1828.05 of kelvin, boilingPoint = 3236.0 of kelvin,
        specificHeatCapacity = 246.0 of joulesPerKilogramKelvin,
        thermalConductivity = 71.8 of wattsPerMeterKelvin,
        ionizationEnergy = 8.337 of electronVolts,
        electricalResistivity = 1.05e-7 of ohmMeters,
        electronegativity = 2.20,
    ),

    /** Silver (Ag, Z = 47), the best electrical conductor of all elements. */
    SILVER(
        47, "Ag", "Silver", 5, KChemicalElementCategory.TRANSITION_METAL, subGroup = 1,
        molarMass = 107.8682 of gramsPerMole,
        atomicRadius = 160.0 of pico.meters, covalentRadius = 145.0 of pico.meters,
        density = 10.49 of gramPerCubicCentimeter,
        meltingPoint = 1234.93 of kelvin, boilingPoint = 2435.0 of kelvin,
        specificHeatCapacity = 235.0 of joulesPerKilogramKelvin,
        thermalConductivity = 429.0 of wattsPerMeterKelvin,
        ionizationEnergy = 7.576 of electronVolts,
        electricalResistivity = 1.59e-8 of ohmMeters,
        electronegativity = 1.93,
    ),

    /** Cadmium (Cd, Z = 48). */
    CADMIUM(
        48, "Cd", "Cadmium", 5, KChemicalElementCategory.TRANSITION_METAL, subGroup = 2,
        molarMass = 112.414 of gramsPerMole,
        atomicRadius = 155.0 of pico.meters, covalentRadius = 144.0 of pico.meters,
        density = 8.65 of gramPerCubicCentimeter,
        meltingPoint = 594.22 of kelvin, boilingPoint = 1040.0 of kelvin,
        specificHeatCapacity = 232.0 of joulesPerKilogramKelvin,
        thermalConductivity = 96.6 of wattsPerMeterKelvin,
        ionizationEnergy = 8.994 of electronVolts,
        electricalResistivity = 7.27e-8 of ohmMeters,
        electronegativity = 1.69,
    ),

    /** Indium (In, Z = 49). */
    INDIUM(
        49, "In", "Indium", 5, KChemicalElementCategory.POST_TRANSITION_METAL, mainGroup = 3,
        molarMass = 114.818 of gramsPerMole,
        atomicRadius = 155.0 of pico.meters, covalentRadius = 142.0 of pico.meters,
        density = 7.31 of gramPerCubicCentimeter,
        meltingPoint = 429.7485 of kelvin, boilingPoint = 2345.0 of kelvin,
        specificHeatCapacity = 233.0 of joulesPerKilogramKelvin,
        thermalConductivity = 81.8 of wattsPerMeterKelvin,
        ionizationEnergy = 5.786 of electronVolts,
        electricalResistivity = 8.0e-8 of ohmMeters,
        electronegativity = 1.78,
    ),

    /** Tin (Sn, Z = 50). */
    TIN(
        50, "Sn", "Tin", 5, KChemicalElementCategory.POST_TRANSITION_METAL, mainGroup = 4,
        molarMass = 118.710 of gramsPerMole,
        atomicRadius = 145.0 of pico.meters, covalentRadius = 139.0 of pico.meters,
        density = 7.265 of gramPerCubicCentimeter,
        meltingPoint = 505.08 of kelvin, boilingPoint = 2875.0 of kelvin,
        specificHeatCapacity = 228.0 of joulesPerKilogramKelvin,
        thermalConductivity = 66.8 of wattsPerMeterKelvin,
        ionizationEnergy = 7.344 of electronVolts,
        electricalResistivity = 1.09e-7 of ohmMeters,
        electronegativity = 1.96,
    ),

    /** Antimony (Sb, Z = 51). */
    ANTIMONY(
        51, "Sb", "Antimony", 5, KChemicalElementCategory.METALLOID, mainGroup = 5,
        molarMass = 121.760 of gramsPerMole,
        atomicRadius = 145.0 of pico.meters, covalentRadius = 139.0 of pico.meters,
        density = 6.697 of gramPerCubicCentimeter,
        meltingPoint = 903.78 of kelvin, boilingPoint = 1908.0 of kelvin,
        specificHeatCapacity = 207.0 of joulesPerKilogramKelvin,
        thermalConductivity = 24.4 of wattsPerMeterKelvin,
        ionizationEnergy = 8.608 of electronVolts,
        electricalResistivity = 4.0e-7 of ohmMeters,
        electronegativity = 2.05,
    ),

    /** Tellurium (Te, Z = 52). */
    TELLURIUM(
        52, "Te", "Tellurium", 5, KChemicalElementCategory.METALLOID, mainGroup = 6,
        molarMass = 127.60 of gramsPerMole,
        atomicRadius = 140.0 of pico.meters, covalentRadius = 138.0 of pico.meters,
        density = 6.24 of gramPerCubicCentimeter,
        meltingPoint = 722.66 of kelvin, boilingPoint = 1261.0 of kelvin,
        specificHeatCapacity = 202.0 of joulesPerKilogramKelvin,
        thermalConductivity = 2.35 of wattsPerMeterKelvin,
        ionizationEnergy = 9.010 of electronVolts,
        electronegativity = 2.1,
    ),

    /** Iodine (I, Z = 53). */
    IODINE(
        53, "I", "Iodine", 5, KChemicalElementCategory.HALOGEN, mainGroup = 7,
        molarMass = 126.90447 of gramsPerMole,
        atomicRadius = 140.0 of pico.meters, covalentRadius = 139.0 of pico.meters,
        density = 4.933 of gramPerCubicCentimeter,
        meltingPoint = 386.85 of kelvin, boilingPoint = 457.4 of kelvin,
        specificHeatCapacity = 214.0 of joulesPerKilogramKelvin,
        thermalConductivity = 0.449 of wattsPerMeterKelvin,
        ionizationEnergy = 10.451 of electronVolts,
        electricalResistivity = 1.3e7 of ohmMeters,
        electronegativity = 2.66,
    ),

    /** Xenon (Xe, Z = 54). */
    XENON(
        54, "Xe", "Xenon", 5, KChemicalElementCategory.NOBLE_GAS, mainGroup = 8,
        molarMass = 131.293 of gramsPerMole,
        atomicRadius = 108.0 of pico.meters, covalentRadius = 140.0 of pico.meters,
        density = 5.887e-3 of gramPerCubicCentimeter,
        meltingPoint = 161.40 of kelvin, boilingPoint = 165.051 of kelvin,
        specificHeatCapacity = 158.0 of joulesPerKilogramKelvin,
        thermalConductivity = 0.00565 of wattsPerMeterKelvin,
        ionizationEnergy = 12.130 of electronVolts,
        electronegativity = 2.60,
    ),

    // --- Period 6 (without the lanthanides 57-71) ------------------------------------------------

    /** Caesium (Cs, Z = 55), the most electropositive stable element. */
    CAESIUM(
        55, "Cs", "Caesium", 6, KChemicalElementCategory.ALKALI_METAL, mainGroup = 1,
        molarMass = 132.90545196 of gramsPerMole,
        atomicRadius = 260.0 of pico.meters, covalentRadius = 244.0 of pico.meters,
        density = 1.93 of gramPerCubicCentimeter,
        meltingPoint = 301.7 of kelvin, boilingPoint = 944.0 of kelvin,
        specificHeatCapacity = 242.0 of joulesPerKilogramKelvin,
        thermalConductivity = 35.9 of wattsPerMeterKelvin,
        ionizationEnergy = 3.894 of electronVolts,
        electricalResistivity = 2.05e-7 of ohmMeters,
        electronegativity = 0.79,
    ),

    /** Barium (Ba, Z = 56). */
    BARIUM(
        56, "Ba", "Barium", 6, KChemicalElementCategory.ALKALINE_EARTH_METAL, mainGroup = 2,
        molarMass = 137.327 of gramsPerMole,
        atomicRadius = 215.0 of pico.meters, covalentRadius = 215.0 of pico.meters,
        density = 3.51 of gramPerCubicCentimeter,
        meltingPoint = 1000.0 of kelvin, boilingPoint = 2118.0 of kelvin,
        specificHeatCapacity = 204.0 of joulesPerKilogramKelvin,
        thermalConductivity = 18.4 of wattsPerMeterKelvin,
        ionizationEnergy = 5.212 of electronVolts,
        electricalResistivity = 3.5e-7 of ohmMeters,
        electronegativity = 0.89,
    ),

    /** Hafnium (Hf, Z = 72), the first element after the lanthanide gap. */
    HAFNIUM(
        72, "Hf", "Hafnium", 6, KChemicalElementCategory.TRANSITION_METAL, subGroup = 4,
        molarMass = 178.49 of gramsPerMole,
        atomicRadius = 155.0 of pico.meters, covalentRadius = 175.0 of pico.meters,
        density = 13.31 of gramPerCubicCentimeter,
        meltingPoint = 2506.0 of kelvin, boilingPoint = 4876.0 of kelvin,
        specificHeatCapacity = 144.0 of joulesPerKilogramKelvin,
        thermalConductivity = 23.0 of wattsPerMeterKelvin,
        ionizationEnergy = 6.825 of electronVolts,
        electricalResistivity = 3.31e-7 of ohmMeters,
        electronegativity = 1.3,
    ),

    /** Tantalum (Ta, Z = 73). */
    TANTALUM(
        73, "Ta", "Tantalum", 6, KChemicalElementCategory.TRANSITION_METAL, subGroup = 5,
        molarMass = 180.94788 of gramsPerMole,
        atomicRadius = 145.0 of pico.meters, covalentRadius = 170.0 of pico.meters,
        density = 16.69 of gramPerCubicCentimeter,
        meltingPoint = 3290.0 of kelvin, boilingPoint = 5731.0 of kelvin,
        specificHeatCapacity = 140.0 of joulesPerKilogramKelvin,
        thermalConductivity = 57.5 of wattsPerMeterKelvin,
        ionizationEnergy = 7.550 of electronVolts,
        electricalResistivity = 1.31e-7 of ohmMeters,
        electronegativity = 1.5,
    ),

    /** Tungsten (W, Z = 74), the element with the highest melting point. */
    TUNGSTEN(
        74, "W", "Tungsten", 6, KChemicalElementCategory.TRANSITION_METAL, subGroup = 6,
        molarMass = 183.84 of gramsPerMole,
        atomicRadius = 135.0 of pico.meters, covalentRadius = 162.0 of pico.meters,
        density = 19.25 of gramPerCubicCentimeter,
        meltingPoint = 3695.0 of kelvin, boilingPoint = 6203.0 of kelvin,
        specificHeatCapacity = 132.0 of joulesPerKilogramKelvin,
        thermalConductivity = 173.0 of wattsPerMeterKelvin,
        ionizationEnergy = 7.864 of electronVolts,
        electricalResistivity = 5.28e-8 of ohmMeters,
        electronegativity = 2.36,
    ),

    /** Rhenium (Re, Z = 75). */
    RHENIUM(
        75, "Re", "Rhenium", 6, KChemicalElementCategory.TRANSITION_METAL, subGroup = 7,
        molarMass = 186.207 of gramsPerMole,
        atomicRadius = 135.0 of pico.meters, covalentRadius = 151.0 of pico.meters,
        density = 21.02 of gramPerCubicCentimeter,
        meltingPoint = 3459.0 of kelvin, boilingPoint = 5869.0 of kelvin,
        specificHeatCapacity = 137.0 of joulesPerKilogramKelvin,
        thermalConductivity = 48.0 of wattsPerMeterKelvin,
        ionizationEnergy = 7.834 of electronVolts,
        electricalResistivity = 1.8e-7 of ohmMeters,
        electronegativity = 1.9,
    ),

    /** Osmium (Os, Z = 76), the densest naturally occurring element. */
    OSMIUM(
        76, "Os", "Osmium", 6, KChemicalElementCategory.TRANSITION_METAL, subGroup = 8,
        molarMass = 190.23 of gramsPerMole,
        atomicRadius = 130.0 of pico.meters, covalentRadius = 144.0 of pico.meters,
        density = 22.59 of gramPerCubicCentimeter,
        meltingPoint = 3306.0 of kelvin, boilingPoint = 5285.0 of kelvin,
        specificHeatCapacity = 130.0 of joulesPerKilogramKelvin,
        thermalConductivity = 87.6 of wattsPerMeterKelvin,
        ionizationEnergy = 8.438 of electronVolts,
        electricalResistivity = 8.1e-8 of ohmMeters,
        electronegativity = 2.2,
    ),

    /** Iridium (Ir, Z = 77). */
    IRIDIUM(
        77, "Ir", "Iridium", 6, KChemicalElementCategory.TRANSITION_METAL, subGroup = 8,
        molarMass = 192.217 of gramsPerMole,
        atomicRadius = 135.0 of pico.meters, covalentRadius = 141.0 of pico.meters,
        density = 22.56 of gramPerCubicCentimeter,
        meltingPoint = 2719.0 of kelvin, boilingPoint = 4403.0 of kelvin,
        specificHeatCapacity = 131.0 of joulesPerKilogramKelvin,
        thermalConductivity = 147.0 of wattsPerMeterKelvin,
        ionizationEnergy = 8.967 of electronVolts,
        electricalResistivity = 4.7e-8 of ohmMeters,
        electronegativity = 2.20,
    ),

    /** Platinum (Pt, Z = 78). */
    PLATINUM(
        78, "Pt", "Platinum", 6, KChemicalElementCategory.TRANSITION_METAL, subGroup = 8,
        molarMass = 195.084 of gramsPerMole,
        atomicRadius = 135.0 of pico.meters, covalentRadius = 136.0 of pico.meters,
        density = 21.45 of gramPerCubicCentimeter,
        meltingPoint = 2041.4 of kelvin, boilingPoint = 4098.0 of kelvin,
        specificHeatCapacity = 133.0 of joulesPerKilogramKelvin,
        thermalConductivity = 71.6 of wattsPerMeterKelvin,
        ionizationEnergy = 8.959 of electronVolts,
        electricalResistivity = 1.06e-7 of ohmMeters,
        electronegativity = 2.28,
    ),

    /** Gold (Au, Z = 79). */
    GOLD(
        79, "Au", "Gold", 6, KChemicalElementCategory.TRANSITION_METAL, subGroup = 1,
        molarMass = 196.966569 of gramsPerMole,
        atomicRadius = 135.0 of pico.meters, covalentRadius = 136.0 of pico.meters,
        density = 19.30 of gramPerCubicCentimeter,
        meltingPoint = 1337.33 of kelvin, boilingPoint = 3243.0 of kelvin,
        specificHeatCapacity = 129.0 of joulesPerKilogramKelvin,
        thermalConductivity = 318.0 of wattsPerMeterKelvin,
        ionizationEnergy = 9.226 of electronVolts,
        electricalResistivity = 2.44e-8 of ohmMeters,
        electronegativity = 2.54,
    ),

    /** Mercury (Hg, Z = 80), the only metal that is liquid at room temperature. */
    MERCURY(
        80, "Hg", "Mercury", 6, KChemicalElementCategory.TRANSITION_METAL, subGroup = 2,
        molarMass = 200.592 of gramsPerMole,
        atomicRadius = 150.0 of pico.meters, covalentRadius = 132.0 of pico.meters,
        density = 13.534 of gramPerCubicCentimeter,
        meltingPoint = 234.321 of kelvin, boilingPoint = 629.88 of kelvin,
        specificHeatCapacity = 140.0 of joulesPerKilogramKelvin,
        thermalConductivity = 8.30 of wattsPerMeterKelvin,
        ionizationEnergy = 10.438 of electronVolts,
        electricalResistivity = 9.61e-7 of ohmMeters,
        electronegativity = 2.00,
    ),

    /** Thallium (Tl, Z = 81). */
    THALLIUM(
        81, "Tl", "Thallium", 6, KChemicalElementCategory.POST_TRANSITION_METAL, mainGroup = 3,
        molarMass = 204.38 of gramsPerMole,
        atomicRadius = 190.0 of pico.meters, covalentRadius = 145.0 of pico.meters,
        density = 11.85 of gramPerCubicCentimeter,
        meltingPoint = 577.0 of kelvin, boilingPoint = 1746.0 of kelvin,
        specificHeatCapacity = 129.0 of joulesPerKilogramKelvin,
        thermalConductivity = 46.1 of wattsPerMeterKelvin,
        ionizationEnergy = 6.108 of electronVolts,
        electricalResistivity = 1.8e-7 of ohmMeters,
        electronegativity = 1.62,
    ),

    /** Lead (Pb, Z = 82), the heaviest element with a stable isotope in practical terms. */
    LEAD(
        82, "Pb", "Lead", 6, KChemicalElementCategory.POST_TRANSITION_METAL, mainGroup = 4,
        molarMass = 207.2 of gramsPerMole,
        atomicRadius = 180.0 of pico.meters, covalentRadius = 146.0 of pico.meters,
        density = 11.34 of gramPerCubicCentimeter,
        meltingPoint = 600.61 of kelvin, boilingPoint = 2022.0 of kelvin,
        specificHeatCapacity = 127.0 of joulesPerKilogramKelvin,
        thermalConductivity = 35.3 of wattsPerMeterKelvin,
        ionizationEnergy = 7.417 of electronVolts,
        electricalResistivity = 2.08e-7 of ohmMeters,
        electronegativity = 2.33,
    ),

    /** Bismuth (Bi, Z = 83). */
    BISMUTH(
        83, "Bi", "Bismuth", 6, KChemicalElementCategory.POST_TRANSITION_METAL, mainGroup = 5,
        molarMass = 208.98040 of gramsPerMole,
        atomicRadius = 160.0 of pico.meters, covalentRadius = 148.0 of pico.meters,
        density = 9.78 of gramPerCubicCentimeter,
        meltingPoint = 544.7 of kelvin, boilingPoint = 1837.0 of kelvin,
        specificHeatCapacity = 122.0 of joulesPerKilogramKelvin,
        thermalConductivity = 7.97 of wattsPerMeterKelvin,
        ionizationEnergy = 7.286 of electronVolts,
        electricalResistivity = 1.29e-6 of ohmMeters,
        electronegativity = 2.02,
    ),

    /** Polonium (Po, Z = 84), radioactive; no specific heat capacity is commonly tabulated. */
    POLONIUM(
        84, "Po", "Polonium", 6, KChemicalElementCategory.POST_TRANSITION_METAL, mainGroup = 6,
        molarMass = 209.0 of gramsPerMole,
        atomicRadius = 190.0 of pico.meters, covalentRadius = 140.0 of pico.meters,
        density = 9.196 of gramPerCubicCentimeter,
        meltingPoint = 527.0 of kelvin, boilingPoint = 1235.0 of kelvin,
        thermalConductivity = 20.0 of wattsPerMeterKelvin,
        ionizationEnergy = 8.417 of electronVolts,
        electricalResistivity = 4.3e-7 of ohmMeters,
        electronegativity = 2.0,
    ),

    /** Astatine (At, Z = 85), the rarest naturally occurring element; most constants are unmeasured. */
    ASTATINE(
        85, "At", "Astatine", 6, KChemicalElementCategory.HALOGEN, mainGroup = 7,
        molarMass = 210.0 of gramsPerMole,
        covalentRadius = 150.0 of pico.meters,
        meltingPoint = 575.0 of kelvin,
        thermalConductivity = 1.7 of wattsPerMeterKelvin,
        ionizationEnergy = 9.32 of electronVolts,
        electronegativity = 2.2,
    ),

    /** Radon (Rn, Z = 86), the heaviest noble gas of this table; radioactive. */
    RADON(
        86, "Rn", "Radon", 6, KChemicalElementCategory.NOBLE_GAS, mainGroup = 8,
        molarMass = 222.0 of gramsPerMole,
        atomicRadius = 120.0 of pico.meters, covalentRadius = 150.0 of pico.meters,
        density = 9.73e-3 of gramPerCubicCentimeter,
        meltingPoint = 202.0 of kelvin, boilingPoint = 211.5 of kelvin,
        specificHeatCapacity = 93.65 of joulesPerKilogramKelvin,
        thermalConductivity = 0.00361 of wattsPerMeterKelvin,
        ionizationEnergy = 10.749 of electronVolts,
        electronegativity = 2.2,
    ),
    ;

    // --- Derived unit data ----------------------------------------------------------------------

    /**
     * The molar volume, derived from [molarMass] and [density] (`molar mass / density`), or `null` if the
     * element has no tabulated density.
     *
     * Example:
     * ```kotlin
     * KChemicalElement.IRON.molarVolume!! into cubicCentimetersPerMole // ≈ 7.09
     * ```
     */
    val molarVolume: KMolarVolumeUnitInstance? = density?.let { molarMass / it }

    // --- Availability flags ---------------------------------------------------------------------

    /** `true` if [atomicRadius] is available. */
    val hasAtomicRadius: Boolean get() = atomicRadius != null

    /** `true` if [covalentRadius] is available. */
    val hasCovalentRadius: Boolean get() = covalentRadius != null

    /** `true` if [density] is available. */
    val hasDensity: Boolean get() = density != null

    /** `true` if [molarVolume] is available (i.e. if [density] is). */
    val hasMolarVolume: Boolean get() = molarVolume != null

    /** `true` if [meltingPoint] is available. */
    val hasMeltingPoint: Boolean get() = meltingPoint != null

    /** `true` if [boilingPoint] is available. */
    val hasBoilingPoint: Boolean get() = boilingPoint != null

    /** `true` if [specificHeatCapacity] is available. */
    val hasSpecificHeatCapacity: Boolean get() = specificHeatCapacity != null

    /** `true` if [thermalConductivity] is available. */
    val hasThermalConductivity: Boolean get() = thermalConductivity != null

    /** `true` if [ionizationEnergy] is available. */
    val hasIonizationEnergy: Boolean get() = ionizationEnergy != null

    /** `true` if [electricalResistivity] is available. */
    val hasElectricalResistivity: Boolean get() = electricalResistivity != null

    /** `true` if [electronegativity] is available. */
    val hasElectronegativity: Boolean get() = electronegativity != null

    /** Renders the element as `"symbol (fullName)"`, e.g. `"Pb (Lead)"`. */
    override fun toString(): String = "$symbol ($fullName)"

    companion object {
        private val BY_SYMBOL: Map<String, KChemicalElement> = entries.associateBy { it.symbol.lowercase() }
        private val BY_FULL_NAME: Map<String, KChemicalElement> = entries.associateBy { it.fullName.lowercase() }
        private val BY_ORDINAL_NUMBER: Map<Int, KChemicalElement> = entries.associateBy { it.ordinalNumber }

        /**
         * Looks the element up by its [symbol], ignoring case (`"pb"`, `"Pb"`, `"PB"`), or `null` if no
         * element of this table has that symbol.
         *
         * Example:
         * ```kotlin
         * KChemicalElement.ofSymbol("Fe") // KChemicalElement.IRON
         * ```
         */
        fun ofSymbol(symbol: String): KChemicalElement? = BY_SYMBOL[symbol.lowercase()]

        /**
         * Looks the element up by its English [fullName], ignoring case, or `null` if unknown.
         *
         * Example:
         * ```kotlin
         * KChemicalElement.ofFullName("iron") // KChemicalElement.IRON
         * ```
         */
        fun ofFullName(fullName: String): KChemicalElement? = BY_FULL_NAME[fullName.lowercase()]

        /**
         * Looks the element up by its atomic number [ordinalNumber], or `null` if that number is not part
         * of this table (e.g. the omitted lanthanides 57-71).
         */
        fun ofOrdinalNumber(ordinalNumber: Int): KChemicalElement? = BY_ORDINAL_NUMBER[ordinalNumber]

        /**
         * Looks the element up by its position in the main-group block: [mainGroup] 1-8 and [period] 1-6,
         * or `null` if that cell is empty.
         *
         * Example:
         * ```kotlin
         * KChemicalElement.ofMainGroup(4, 6) // KChemicalElement.LEAD
         * ```
         */
        fun ofMainGroup(mainGroup: Int, period: Int): KChemicalElement? =
            entries.firstOrNull { it.mainGroup == mainGroup && it.period == period }

        /**
         * Looks the element up by its position in the sub-group block: [subGroup] 1-8 and [period] 4-6, or
         * `null` if that cell is empty. Sub group 8 holds three elements per period; the first one
         * (Fe, Ru, Os) is returned - use [ofPeriod] to get them all.
         */
        fun ofSubGroup(subGroup: Int, period: Int): KChemicalElement? =
            entries.firstOrNull { it.subGroup == subGroup && it.period == period }

        /** All elements of the given [period] (row) of the table, in ascending atomic number. */
        fun ofPeriod(period: Int): List<KChemicalElement> = entries.filter { it.period == period }

        /** All elements of the given [category], in ascending atomic number. */
        fun ofCategory(category: KChemicalElementCategory): List<KChemicalElement> =
            entries.filter { it.category == category }
    }
}
