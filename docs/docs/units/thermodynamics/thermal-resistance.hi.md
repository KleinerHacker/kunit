# तापीय प्रतिरोध (R-मान)

पैकेज: `org.pcsoft.framework.kunit.thermo.resistance`
मूल इकाई: **वर्ग मीटर-केल्विन प्रति वाट**
(`KThermalResistanceUnit.BASE == KThermalResistanceUnit.SQUARE_METER_KELVIN_PER_WATT`)

प्रकार: **संघटित इकाई**

तापीय प्रतिरोध — **R-मान** — यह दर्शाता है कि कोई परत ऊष्मा प्रवाह का कितना प्रतिरोध करती है: `m²·K/W`।
यह [ऊष्मा स्थानांतरण गुणांक](heat-transfer-coefficient.md) (U-मान) का ठीक व्युत्क्रम है, और वह रूप है जिसमें इन्सुलेशन
उत्पाद वास्तव में बेचे जाते हैं, क्योंकि श्रेणी में लगी परतों के R-मान बस **जुड़ जाते हैं**।

`KThermalResistanceUnitInstance` एक `KMixedUnitInstance` को लपेटता है जिसमें विहित सामान्य रूप
`mass⁻¹ · time³ · temperature¹` (`kg⁻¹·s³·K`) में ठीक तीन पद होते हैं, जो हमेशा m²·K/W में सामान्यीकृत रहता है।

!!! note "पैकेज नाम बनाम क्लास नाम"
पैकेज `thermo.resistance` है, `thermo.thermalresistance` नहीं — किसी यूनिट पैकेज को अपने फील्ड पैकेज का नाम दोहराना नहीं
चाहिए। **टाइप्स** पूरा तकनीकी शब्द बनाए रखते हैं (`KThermalResistanceUnitInstance`), जो इन्हें `electric.resistance` से
अलग करता है।

## नामित इकाइयाँ

| इकाई                    | संकेत            |                              टोकन | m²·K/W में 1 इकाई |
|------------------------|----------------|---------------------------------:|---------------:|
| वर्ग मीटर-केल्विन प्रति वाट (RSI) | `m²·K/W`       |       `squareMeterKelvinPerWatt` |            1.0 |
| इंपीरियल R-मान             | `h·ft²·°F/Btu` | `hourSquareFootFahrenheitPerBtu` |     ≈ 0.176110 |
| क्लो (Clo)                | `clo`          |                            `clo` |          0.155 |
| टॉग (Tog)               | `tog`          |                            `tog` |            0.1 |

एक US "R-30" बैट `30 of hourSquareFootFahrenheitPerBtu` ≈ 5.28 m²·K/W के बराबर है। एक बिज़नेस सूट लगभग 1 क्लो का होता
है; रज़ाई (duvets) को टॉग में दर्जा दिया जाता है (1 क्लो = 1.55 टॉग)। सभी इकाइयाँ पूर्ण SI उपसर्ग सीमा को स्वीकार करती
हैं।

## वास्तविक उदाहरण: एक इंसुलेटेड दीवार, परत दर परत

एक दीवार 20 cm खनिज ऊन (λ = 0.04 W/ (m·K)) और 12 cm ईंट (λ = 0.8 W/ (m·K)) से बनी है। कुल R-मान, परिणामी U-मान, और ΔT =
25 K पर ऊष्मा हानि क्या है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.wattsPerSquareMeterKelvin
import org.pcsoft.framework.kunit.thermo.resistance.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val wool  = (20 of centi.meters) / (0.04 of wattsPerMeterKelvin)  // 5.0 m²·K/W
val brick = (12 of centi.meters) / (0.8 of wattsPerMeterKelvin)   // 0.15 m²·K/W

val total = wool + brick                    // श्रेणी में लगी परतें जुड़ जाती हैं
total into squareMeterKelvinPerWatt         // 5.15 m²·K/W
total into hourSquareFootFahrenheitPerBtu   // ≈ 29.2 (एक "R-29" दीवार)

val u = 1 / total                           // KHeatTransferCoefficientUnitInstance
u into wattsPerSquareMeterKelvin            // ≈ 0.194 W/(m²·K)

val drop = KTemperatureDifference.ofKelvin(25)
val flux = drop / total                     // KHeatFluxDensityUnitInstance
flux into wattsPerSquareMeter               // ≈ 4.85 W/m²

val wall = (10 of meters) * (2.5 of meters) // 25 m²
(flux * wall) into watts                    // ≈ 121 W
```

## पड़ोसी इकाइयों के साथ गणना

| व्यंजक                                         | परिणाम प्रकार                               | अर्थ            |
|---------------------------------------------|----------------------------------------|---------------|
| `temperatureDifference / heatFluxDensity`   | `KThermalResistanceUnitInstance`       | माप से R        |
| `length / thermalConductivity`              | `KThermalResistanceUnitInstance`       | सामग्री + मोटाई से R |
| `thermalResistance * heatFluxDensity`       | `KTemperatureDifferenceUnitInstance`   | स्थायी अंतर        |
| `heatFluxDensity * thermalResistance`       | `KTemperatureDifferenceUnitInstance`   | वही (क्रमविनिमेय)   |
| `temperatureDifference / thermalResistance` | `KHeatFluxDensityUnitInstance`         | परिणामी फ्लक्स       |
| `thermalResistance * thermalConductivity`   | `KLengthUnitInstance`                  | आवश्यक मोटाई      |
| `thermalConductivity * thermalResistance`   | `KLengthUnitInstance`                  | वही (क्रमविनिमेय)   |
| `length / thermalResistance`                | `KThermalConductivityUnitInstance`     | निहित चालकता      |
| `1 / heatTransferCoefficient`               | `KThermalResistanceUnitInstance`       | U से R         |
| `1 / thermalResistance`                     | `KHeatTransferCoefficientUnitInstance` | R से U         |

दोनों व्युत्क्रम संकारक संकीर्ण रूप से घोषित किए गए हैं, ताकि `1 / u` और `1 / r` एक **टाइप किया गया**
मान लौटाएँ, न कि वह सामान्य मिश्रित इकाई जो समूह-अज्ञेय `Number.div` उत्पन्न करता।

## अपघटन

तीनों अपघटन समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं।

| अपघटन                                     | रूप                           | परिणाम                             |
|-------------------------------------------|------------------------------|----------------------------------|
| `temperatureDifference / heatFluxDensity` | टाइप किया गया संकारक               | `KThermalResistanceUnitInstance` |
| `length / thermalConductivity`            | टाइप किया गया संकारक               | `KThermalResistanceUnitInstance` |
| `mass⁻¹ · time³ · temperature¹`           | मूल + `toThermalResistance()` | `KThermalResistanceUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.resistance.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaFlux      = KTemperatureDifference.ofKelvin(1) / (1 of wattsPerSquareMeter)
val viaThickness = (1 of meters) / (1 of wattsPerMeterKelvin)
val native = (
    ((1 of seconds).toUnit() pow 3) *
        KTemperatureDifference.ofKelvin(1).toUnit() /
        (1000 of grams).toUnit()
    ).toThermalResistance()

viaFlux == viaThickness // true
viaFlux == native       // true - सभी 1.0 m²·K/W हैं
```

## ऑपरेटर

`+` और `-` यहाँ ठीक वही भौतिक रूप से सार्थक संक्रिया हैं: श्रेणी में लगी परतें अपने R-मान जोड़ती हैं।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.resistance.*

val series = (5 of squareMeterKelvinPerWatt) + (0.15 of squareMeterKelvinPerWatt) // 5.15
(1 of squareMeterKelvinPerWatt) > (5 of tog)      // true (5 tog = 0.5 m²·K/W)
(1 of squareMeterKelvinPerWatt) == (10 of tog)    // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.resistance.*

(5 of squareMeterKelvinPerWatt).toString()                                        // "5.0 m²·K/W"
"R-${(5 of squareMeterKelvinPerWatt) into hourSquareFootFahrenheitPerBtu}"        // "R-28.39..."
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे।
घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को
भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित                 | Kotlin                                                 | अर्थ                      |
|---------------------|--------------------------------------------------------|-------------------------|
| `m²·K/W`            | `squareMeterKelvinPerWatt`                             | तापीय प्रतिरोध (R-मान), मूल इकाई |
| `kg⁻¹·s³·K`         | `(seconds pow 3) * ΔK / grams`                         | वही राशि आधार आयामों में         |
| `h·ft²·°F/Btu`      | `hourSquareFootFahrenheitPerBtu`                       | इंपीरियल R-मान              |
| `R = d / λ`         | `(20 of centi.meters) / (0.04 of wattsPerMeterKelvin)` | मोटाई ÷ चालकता से R          |
| `R = ΔT / q̇`        | `drop / (4 of wattsPerSquareMeter)`                    | अंतर ÷ फ्लक्स से R            |
| `R_total = R₁ + R₂` | `wool + brick`                                         | श्रेणी में लगी परतें             |
| `U = 1 / R`         | `1 / total`                                            | R-मान से U-मान             |
| `q̇ = ΔT / R`        | `drop / total`                                         | अंतर ÷ R से फ्लक्स            |
