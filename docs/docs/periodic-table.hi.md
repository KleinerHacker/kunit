# आवर्त सारणी

पैकेज: `org.pcsoft.framework.kunit`
प्रकार: `KChemicalElement`, `KChemicalElementCategory`

`KChemicalElement` रासायनिक तत्वों के लिए केंद्रीय स्थान है। यह एक सामान्य Kotlin enum है, इसलिए हर तत्व एक कंपाइल-टाइम
स्थिरांक है — और इसका हर भौतिक स्थिरांक इस लाइब्रेरी का एक **टाइप किया गया इकाई इंस्टेंस** है, जो बाकी सब कुछ के साथ
संघटित होने के लिए तैयार है।

## दायरा

यह enum क्लासिक स्कूली आवर्त सारणी को कवर करता है: **आवर्त 1-6 बिना f-ब्लॉक के** के मुख्य और उप समूह। इसलिए लैंथेनाइड्स
(57-71) गायब हैं — परमाणु क्रमांक बेरियम (56) से हाफ़नियम (72) तक छलांग लगाते हैं — और न तो एक्टिनाइड्स और न ही
ट्रांसएक्टिनाइड्स शामिल हैं। इस तरह कुल 71 प्रविष्टियाँ बनती हैं।

## स्थितीय डेटा

| गुण              | प्रकार                        | अर्थ                                              |
|-----------------|----------------------------|-------------------------------------------------|
| `ordinalNumber` | `Int`                      | परमाणु क्रमांक Z, आवर्त सारणी में सूचकांक                      |
| `symbol`        | `String`                   | तत्व संकेत, जैसे `"Pb"`                               |
| `fullName`      | `String`                   | अंग्रेज़ी नाम, जैसे `"Lead"` (enum प्रविष्टि `LEAD` है)         |
| `period`        | `Int`                      | आवर्त (पंक्ति), 1-6                                   |
| `mainGroup`     | `Int?`                     | s/p-ब्लॉक तत्वों के लिए मुख्य समूह 1-8, संक्रमण धातुओं के लिए `null` |
| `subGroup`      | `Int?`                     | d-ब्लॉक तत्वों के लिए उप समूह 1-8, अन्यथा `null`             |
| `category`      | `KChemicalElementCategory` | रासायनिक परिवार                                      |

`mainGroup` और `subGroup` में से ठीक एक सेट होता है। उप समूह क्लासिक क्रमांकन का उपयोग करते हैं (Cu = 1, Zn = 2, Sc =
3 … Fe/Co/Ni = 8)।

`KChemicalElementCategory` में प्रविष्टियाँ `HYDROGEN`, `ALKALI_METAL`, `ALKALINE_EARTH_METAL`,
`TRANSITION_METAL`, `POST_TRANSITION_METAL`, `METALLOID`, `NONMETAL`, `HALOGEN` और `NOBLE_GAS` हैं।

## इकाई डेटा

| गुण                      | प्रकार                                  | उपलब्धता ध्वज                   |
|-------------------------|--------------------------------------|----------------------------|
| `molarMass`             | `KMolarMassUnitInstance`             | हमेशा मौजूद                    |
| `molarVolume`           | `KMolarVolumeUnitInstance?`          | `hasMolarVolume`           |
| `atomicRadius`          | `KLengthUnitInstance?`               | `hasAtomicRadius`          |
| `covalentRadius`        | `KLengthUnitInstance?`               | `hasCovalentRadius`        |
| `density`               | `KDensityUnitInstance?`              | `hasDensity`               |
| `meltingPoint`          | `KTemperatureUnitInstance?`          | `hasMeltingPoint`          |
| `boilingPoint`          | `KTemperatureUnitInstance?`          | `hasBoilingPoint`          |
| `specificHeatCapacity`  | `KSpecificHeatCapacityUnitInstance?` | `hasSpecificHeatCapacity`  |
| `thermalConductivity`   | `KThermalConductivityUnitInstance?`  | `hasThermalConductivity`   |
| `ionizationEnergy`      | `KEnergyUnitInstance?`               | `hasIonizationEnergy`      |
| `electricalResistivity` | `KResistivityUnitInstance?`          | `hasElectricalResistivity` |
| `electronegativity`     | `Double?` (पॉलिंग, आयामरहित)              | `hasElectronegativity`     |

जो स्थिरांक किसी तत्व के लिए सार्थक रूप से परिभाषित नहीं हैं वे `null` हैं — हीलियम का सामान्य दबाव पर कोई गलनांक नहीं
है, आर्सेनिक उबलने के बजाय ऊर्ध्वपातित होता है, एस्टैटिन इतना दुर्लभ है कि उसका कोई मापा गया घनत्व नहीं है। संबंधित
`has...` गुण null हैंडलिंग के बिना उसी प्रश्न का उत्तर देता है।

`molarVolume`, `molarMass / density` से व्युत्पन्न है, अर्थात यह [मोलर आयतन](units/thermodynamics/molar-volume.md)
समूह के दूसरे अपघटन का उपयोग करता है।

## वास्तविक उदाहरण: सोने की एक छड़ कितनी भारी है?

सोने की एक मानक छड़ 7 cm × 4 cm × 2 cm मापती है। इसका वज़न कितना है, और यह सोने के कितने मोल के बराबर है?

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.mechanic.density.times
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole

val gold = KChemicalElement.GOLD

val volume = (7 of centi.meters) * (4 of centi.meters) * (2 of centi.meters) // 56 cm³
val mass = gold.density!! * volume                                          // KMassUnitInstance
mass into kilo.grams                                                        // ≈ 1.081 kg

val amount = mass / gold.molarMass                                          // KAmountOfSubstanceUnitInstance
amount into moles                                                           // ≈ 5.49 mol

gold.molarMass into gramsPerMole                                            // 196.966569
```

## वास्तविक उदाहरण: तांबे का बर्तन गर्म करना

1.2 kg के तांबे के बर्तन को 20 °C से 200 °C तक गर्म करने के लिए कितनी ऊर्जा लगती है?

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin

val copper = KChemicalElement.COPPER
val c = copper.specificHeatCapacity!! into joulesPerKilogramKelvin // 385.0
val mass = 1.2 of kilo.grams

val energy = (mass into kilo.grams) * c * 180.0 // ΔT = 180 K
energy                                          // ≈ 83 160 J
```

## लुकअप

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.KChemicalElementCategory

KChemicalElement.ofSymbol("Fe")        // IRON (केस-असंवेदनशील)
KChemicalElement.ofFullName("iron")    // IRON (केस-असंवेदनशील)
KChemicalElement.ofOrdinalNumber(26)   // IRON
KChemicalElement.ofOrdinalNumber(57)   // null - लैंथेनाइड्स इस सारणी का हिस्सा नहीं हैं
KChemicalElement.ofMainGroup(4, 6)     // LEAD (मुख्य समूह 4, आवर्त 6)
KChemicalElement.ofSubGroup(8, 4)      // IRON (उप समूह 8, आवर्त 4 - Fe/Co/Ni में पहला)
KChemicalElement.ofPeriod(1)           // [HYDROGEN, HELIUM]
KChemicalElement.ofCategory(KChemicalElementCategory.NOBLE_GAS)
// [HELIUM, NEON, ARGON, KRYPTON, XENON, RADON]
```

उप समूह 8 में प्रति आवर्त तीन तत्व होते हैं; `ofSubGroup` पहला वाला (Fe, Ru, Os) लौटाता है — सभी पाने के लिए `ofPeriod`
का उपयोग करें और फ़िल्टर करें।

## संकेतन

| गणित           | Kotlin                                         | अर्थ                         |
|---------------|------------------------------------------------|----------------------------|
| `Z`           | `element.ordinalNumber`                        | परमाणु क्रमांक                   |
| `M`           | `element.molarMass`                            | मोलर द्रव्यमान, `g/mol`          |
| `V_m = M / ρ` | `element.molarVolume`                          | मोलर आयतन, `m³/mol`         |
| `ρ`           | `element.density`                              | घनत्व                        |
| `T_m`, `T_b`  | `element.meltingPoint`, `element.boilingPoint` | गलनांक / क्वथनांक K में            |
| `m = ρ · V`   | `gold.density!! * volume`                      | घनत्व × आयतन से द्रव्यमान          |
| `n = m / M`   | `mass / gold.molarMass`                        | द्रव्यमान ÷ मोलर द्रव्यमान से पदार्थ की मात्रा |
