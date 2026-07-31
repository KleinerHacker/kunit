# तापीय चालकता

पैकेज: `org.pcsoft.framework.kunit.thermo.conductivity`
मूल इकाई: **वाट प्रति मीटर-केल्विन** (`KThermalConductivityUnit.BASE == KThermalConductivityUnit.WATT_PER_METER_KELVIN`)

प्रकार: **संघटित इकाई**

तापीय चालकता `λ` (जिसे `k` भी कहा जाता है) फूरियर के नियम में सामग्री-गुणधर्म है: किसी सामग्री के
आर-पार [ऊष्मा फ्लक्स घनत्व](heat-flux-density.md), उसकी चालकता गुणा
[तापमान प्रवणता](temperature-gradient.md) के बराबर होता है। इकाई: `W/(m·K)`।

`KThermalConductivityUnitInstance` एक `KMixedUnitInstance` को लपेटता है जिसमें विहित सामान्य रूप
`mass¹ · distance¹ · time⁻³ · temperature⁻¹` (`kg·m·s⁻³·K⁻¹`) में ठीक चार पद होते हैं, जो हमेशा W/ (m·K) में सामान्यीकृत
रहता है।

!!! note "पैकेज नाम बनाम क्लास नाम"
पैकेज `thermo.conductivity` है, `thermo.thermalconductivity` नहीं — किसी यूनिट पैकेज को अपने फील्ड पैकेज का नाम दोहराना
नहीं चाहिए। **टाइप्स** पूरा तकनीकी शब्द बनाए रखते हैं (`KThermalConductivityUnitInstance`), जो इन्हें
`electric.conductivity` से अलग करता है।

किसी मोटाई से विभाजित करने पर यह [ऊष्मा स्थानांतरण गुणांक](heat-transfer-coefficient.md) बन जाती है; इसके द्वारा मोटाई
को विभाजित करने पर [तापीय प्रतिरोध](thermal-resistance.md) (R-मान) मिलता है।

## नामित इकाइयाँ

| इकाई               | संकेत             |                                 टोकन | W/(m·K) में 1 इकाई |
|-------------------|-----------------|------------------------------------:|----------------:|
| वाट प्रति मीटर-केल्विन     | `W/(m·K)`       |               `wattsPerMeterKelvin` |             1.0 |
| Btu प्रति घंटा-फुट-°F   | `Btu/(h·ft·°F)` |         `btusPerHourFootFahrenheit` |      ≈ 1.730735 |
| कैलोरी प्रति सेकंड-cm-केल्विन | `cal/(s·cm·K)`  | `caloriesPerSecondCentimeterKelvin` |           418.4 |

सभी में पूर्ण SI उपसर्ग सीमा समर्थित है — इन्सुलेशन सामग्री स्वाभाविक रूप से
`40 of milli.wattsPerMeterKelvin` के रूप में लिखी जाती है।

## विशिष्ट मान

| सामग्री    |                            λ |
|--------|-----------------------------:|
| तांबा     |                  401 W/(m·K) |
| स्टील     |                 ≈ 50 W/(m·K) |
| कांच     |                  ≈ 1 W/(m·K) |
| खनिज ऊन | ≈ 0.04 W/(m·K) = 40 mW/(m·K) |

## वास्तविक उदाहरण: इंसुलेटेड दीवार से ऊष्मा हानि

30 cm खनिज-ऊन परत (λ = 0.04 W/ (m·K)) 21 °C कमरे को −5 °C बाहरी हवा से अलग करती है। दीवार 12 m² है। कितनी ऊष्मा खो जाती
है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.conductivity.*
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.temperature.celsius
import org.pcsoft.framework.kunit.thermo.temperaturegradient.kelvinPerMeter

val wool = 40 of milli.wattsPerMeterKelvin      // 0.04 W/(m·K)
val thickness = 30 of centi.meters
val drop = (21 of celsius) - (-5 of celsius)    // 26 K

val gradient = drop / thickness                 // KTemperatureGradientUnitInstance, ≈ 86.7 K/m
gradient into kelvinPerMeter                    // 86.666...

val flux = wool * gradient                      // KHeatFluxDensityUnitInstance (फूरियर का नियम)
flux into wattsPerSquareMeter                   // ≈ 3.47 W/m²

val wall = (4 of meters) * (3 of meters)        // 12 m²
val loss = flux * wall                          // KPowerUnitInstance
loss into watts                                 // ≈ 41.6 W
```

## पड़ोसी इकाइयों के साथ गणना

| व्यंजक                                         | परिणाम प्रकार                           | अर्थ                         |
|---------------------------------------------|------------------------------------|----------------------------|
| `heatFluxDensity / temperatureGradient`     | `KThermalConductivityUnitInstance` | λ के लिए हल किया गया फूरियर का नियम |
| `thermalConductivity * temperatureGradient` | `KHeatFluxDensityUnitInstance`     | फूरियर का नियम                 |
| `temperatureGradient * thermalConductivity` | `KHeatFluxDensityUnitInstance`     | वही (क्रमविनिमेय)                |
| `heatFluxDensity / thermalConductivity`     | `KTemperatureGradientUnitInstance` | निहित प्रवणता                   |

## अपघटन

दोनों अपघटन समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं।

| अपघटन                                      | रूप                             | परिणाम                               |
|--------------------------------------------|--------------------------------|------------------------------------|
| `heatFluxDensity / temperatureGradient`    | टाइप किया गया संकारक                 | `KThermalConductivityUnitInstance` |
| `mass · distance · time⁻³ · temperature⁻¹` | मूल + `toThermalConductivity()` | `KThermalConductivityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.*
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperaturegradient.kelvinPerMeter

val typed = (1 of wattsPerSquareMeter) / (1 of kelvinPerMeter)
val native = (
    (1000 of grams).toUnit() *
        (1 of meters).toUnit() /
        ((1 of seconds).toUnit() pow 3) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toThermalConductivity()

typed == native // true - दोनों 1.0 W/(m·K) हैं
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.conductivity.*

val total = (1 of kilo.wattsPerMeterKelvin) + (500 of wattsPerMeterKelvin)  // 1500 W/(m·K)
(1 of kilo.wattsPerMeterKelvin) > (500 of wattsPerMeterKelvin)              // true
(1 of kilo.wattsPerMeterKelvin) == (1000 of wattsPerMeterKelvin)            // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.conductivity.*

(401 of wattsPerMeterKelvin).toString()                                          // "401.0 W/(m·K)"
"${(401 of wattsPerMeterKelvin) into btusPerHourFootFahrenheit} Btu/(h·ft·°F)"   // "231.7..."
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे।
घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को
भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित            | Kotlin                                   | अर्थ                      |
|----------------|------------------------------------------|-------------------------|
| `W/(m·K)`      | `wattsPerMeterKelvin`                    | तापीय चालकता, मूल इकाई        |
| `kg·m·s⁻³·K⁻¹` | `grams * meters / (seconds pow 3) / ΔK`  | वही राशि आधार आयामों में         |
| `mW/(m·K)`     | `milli.wattsPerMeterKelvin`              | मिलीवाट प्रति मीटर-केल्विन (इन्सुलेशन) |
| `q̇ = λ · ∇T`   | `wool * gradient`                        | फूरियर का नियम              |
| `λ = q̇ / ∇T`   | `(80 of wattsPerSquareMeter) / gradient` | फ्लक्स ÷ प्रवणता से चालकता        |
| `∇T = q̇ / λ`   | `flux / wool`                            | फ्लक्स ÷ चालकता से प्रवणता        |
