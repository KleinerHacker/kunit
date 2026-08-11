# ऊष्मा स्थानांतरण गुणांक

पैकेज: `org.pcsoft.framework.kunit.thermo.heattransfercoefficient`
मूल इकाई: **वाट प्रति वर्ग मीटर-केल्विन**
(`KHeatTransferCoefficientUnit.BASE == KHeatTransferCoefficientUnit.WATT_PER_SQUARE_METER_KELVIN`)

प्रकार: **संघटित इकाई**

ऊष्मा स्थानांतरण गुणांक — भवन-भौतिकी में **U-मान** — वह ऊष्मा प्रवाह घनत्व है जो कोई घटक प्रति केल्विन तापमान अंतर से
गुजरने देता है: `W/(m²·K)`। U-मान जितना कम, इन्सुलेशन उतना बेहतर।

`KHeatTransferCoefficientUnitInstance` विहित सामान्य रूप `mass¹ · time⁻³ · temperature⁻¹`
(`kg·s⁻³·K⁻¹`) में ठीक तीन पदों वाले `KMixedUnitInstance` को लपेटता है, जो हमेशा W/ (m²·K) में सामान्यीकृत रहता
है। [ऊष्मा प्रवाह घनत्व](heat-flux-density.md) की तरह ही क्षेत्रफल वाट के लंबाई आयामों को रद्द कर देता है, इसलिए सामान्य
रूप में कोई दूरी पद नहीं है।

इसका व्युत्क्रम [ऊष्मीय प्रतिरोध](thermal-insulance.md) (R-मान) है; इसे मोटाई से गुणा करने पर
[ऊष्मीय चालकता](thermal-conductivity.md) बनती है।

## नामित इकाइयाँ

| इकाई                | संकेत              |                                       टोकन | 1 इकाई = ? W/(m²·K) |
|--------------------|------------------|------------------------------------------:|-------------------:|
| वाट प्रति वर्ग मीटर-केल्विन   | `W/(m²·K)`       |               `wattsPerSquareMeterKelvin` |                1.0 |
| Btu प्रति घंटा-वर्ग फुट-°F | `Btu/(h·ft²·°F)` |         `btusPerHourSquareFootFahrenheit` |         ≈ 5.678263 |
| कैलोरी प्रति सेकंड-cm²-केल्विन | `cal/(s·cm²·K)`  | `caloriesPerSecondSquareCentimeterKelvin` |            41840.0 |

सभी में पूर्ण SI उपसर्ग सीमा समर्थित है (`milli.wattsPerSquareMeterKelvin`, …)।

## विशिष्ट U-मान

| घटक         |                    U |
|-------------|---------------------:|
| एकल शीशा      |       ≈ 5.8 W/(m²·K) |
| दोहरा शीशा      |       ≈ 2.8 W/(m²·K) |
| त्रिस्तरीय शीशा     | ≈ 0.7 … 1.3 W/(m²·K) |
| पैसिव-हाउस दीवार |      ≈ 0.15 W/(m²·K) |

## वास्तविक उदाहरण: एक खिड़की से ऊष्मा हानि

एक 2.4 m² त्रिस्तरीय शीशे वाली खिड़की का U = 1.3 W/ (m²·K) है। अंदर 21 °C और बाहर 1 °C है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*
import org.pcsoft.framework.kunit.thermo.temperature.celsius

val window = 1.3 of wattsPerSquareMeterKelvin
val drop = (21 of celsius) - (1 of celsius)      // 20 K
val glass = (2 of meters) * (1.2 of meters)      // 2.4 m²

val flux = window * drop                          // KHeatFluxDensityUnitInstance
flux into wattsPerSquareMeter                     // 26.0 W/m²

val loss = flux * glass                           // KPowerUnitInstance
loss into watts                                   // 62.4 W

// एकल शीशा हमें कितना महँगा पड़ेगा?
val single = 5.8 of wattsPerSquareMeterKelvin
((single * drop) * glass) into watts              // 278.4 W - साढ़े चार गुना अधिक
```

## पड़ोसी इकाइयों से गणना

| व्यंजक                                               | परिणाम प्रकार                               | अर्थ               |
|---------------------------------------------------|----------------------------------------|------------------|
| `heatFluxDensity / temperatureDifference`         | `KHeatTransferCoefficientUnitInstance` | माप से U-मान        |
| `thermalConductivity / length`                    | `KHeatTransferCoefficientUnitInstance` | पदार्थ + मोटाई से U-मान |
| `heatTransferCoefficient * temperatureDifference` | `KHeatFluxDensityUnitInstance`         | घटक से गुज़रता प्रवाह   |
| `temperatureDifference * heatTransferCoefficient` | `KHeatFluxDensityUnitInstance`         | वही (क्रमविनिमेय)      |
| `heatFluxDensity / heatTransferCoefficient`       | `KTemperatureDifferenceUnitInstance`   | चालक अंतर          |
| `heatTransferCoefficient * length`                | `KThermalConductivityUnitInstance`     | पदार्थ चालकता         |
| `length * heatTransferCoefficient`                | `KThermalConductivityUnitInstance`     | वही (क्रमविनिमेय)      |
| `thermalConductivity / heatTransferCoefficient`   | `KLengthUnitInstance`                  | आवश्यक मोटाई         |

## अपघटन

तीनों अपघटन समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं।

| अपघटन                                     | रूप                                 | परिणाम                                   |
|-------------------------------------------|------------------------------------|----------------------------------------|
| `heatFluxDensity / temperatureDifference` | टाइप किया गया संकारक                     | `KHeatTransferCoefficientUnitInstance` |
| `thermalConductivity / length`            | टाइप किया गया संकारक                     | `KHeatTransferCoefficientUnitInstance` |
| `mass · time⁻³ · temperature⁻¹`           | मूल + `toHeatTransferCoefficient()` | `KHeatTransferCoefficientUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaFlux         = (1 of wattsPerSquareMeter) / KTemperatureDifference.ofKelvin(1)
val viaConductivity = (1 of wattsPerMeterKelvin) / (1 of meters)
val native = (
    (1000 of grams).toUnit() /
        ((1 of seconds).toUnit() pow 3) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toHeatTransferCoefficient()

viaFlux == viaConductivity // true
viaFlux == native          // true - सभी 1.0 W/(m²·K) हैं
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*

val total = (1 of kilo.wattsPerSquareMeterKelvin) + (500 of wattsPerSquareMeterKelvin)  // 1500
(1 of kilo.wattsPerSquareMeterKelvin) > (500 of wattsPerSquareMeterKelvin)              // true
(1 of kilo.wattsPerSquareMeterKelvin) == (1000 of wattsPerSquareMeterKelvin)            // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*

(1.3 of wattsPerSquareMeterKelvin).toString()                                             // "1.3 W/(m²·K)"
"${(1.3 of wattsPerSquareMeterKelvin) into btusPerHourSquareFootFahrenheit} Btu/(h·ft²·°F)" // "0.229..."
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे।
घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को
भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित              | Kotlin                                            | अर्थ                          |
|------------------|---------------------------------------------------|-----------------------------|
| `W/(m²·K)`       | `wattsPerSquareMeterKelvin`                       | ऊष्मा स्थानांतरण गुणांक (U-मान), मूल इकाई |
| `kg·s⁻³·K⁻¹`     | `grams / (seconds pow 3) / ΔK`                    | वही राशि आधार आयामों में             |
| `U = q̇ / ΔT`     | `(26 of wattsPerSquareMeter) / drop`              | प्रवाह ÷ तापमान अंतर से U-मान       |
| `U = λ / d`      | `(0.04 of wattsPerMeterKelvin) / (0.2 of meters)` | चालकता ÷ मोटाई से U-मान           |
| `q̇ = U · ΔT`     | `window * drop`                                   | U-मान × तापमान अंतर से प्रवाह       |
| `P = U · A · ΔT` | `(window * drop) * glass`                         | कुल ऊष्मा हानि                    |
