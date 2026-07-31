# तापमान प्रवणता

पैकेज: `org.pcsoft.framework.kunit.thermo.temperaturegradient`
मूल इकाई: **केल्विन प्रति मीटर** (`KTemperatureGradientUnit.BASE == KTemperatureGradientUnit.KELVIN_PER_METER`)

प्रकार: **संघटित इकाई**

तापमान प्रवणता प्रति इकाई लंबाई तापमान परिवर्तन है: `temperatureDifference / length`
(`K/m`)। यह चालन (conduction) की प्रेरक राशि है — किसी [तापीय चालकता](thermal-conductivity.md) से गुणा करने पर
यह [ऊष्मा फ्लक्स घनत्व](heat-flux-density.md) देती है।

`KTemperatureGradientUnitInstance` एक `KMixedUnitInstance` को लपेटता है जिसमें विहित सामान्य रूप
`temperature¹ · distance⁻¹` (`K·m⁻¹`) में ठीक दो पद होते हैं, जो हमेशा K/m में सामान्यीकृत रहता है।

!!! note "प्रवणता प्रति लंबाई एक *परिवर्तन* है"
तापमान आयाम **अंतर** समूह (`KTemperatureDifferenceUnit`) है। ऑफसेट वाले निरपेक्ष स्केल (°C, °F)
का किसी प्रवणता में कोई अर्थ नहीं है — केवल अंतराल का अर्थ है। यही कारण है कि `°F/ft` फारेनहाइट *अंतराल* गुणांक 5/9 से
परिवर्तित होता है, −32 ऑफसेट से नहीं।

## नामित इकाइयाँ

| इकाई             | संकेत     |                  टोकन | K/m में 1 इकाई |
|-----------------|---------|---------------------:|------------:|
| केल्विन प्रति मीटर      | `K/m`   |     `kelvinPerMeter` |         1.0 |
| केल्विन प्रति किलोमीटर    | `K/km`  | `kelvinPerKilometer` |       0.001 |
| डिग्री फारेनहाइट प्रति फुट | `°F/ft` |  `fahrenheitPerFoot` |  ≈ 1.822689 |

सभी में पूर्ण SI उपसर्ग सीमा समर्थित है (`milli.kelvinPerMeter`, …)।

## वास्तविक उदाहरण: भूतापीय प्रवणता

पृथ्वी की परत गहराई के प्रति किलोमीटर लगभग 25 K गर्म होती है। एक बोरहोल 3.5 km तक पहुँचता है। तली में चट्टान कितनी अधिक
गर्म है, और 100 K वृद्धि के लिए कितनी गहराई ड्रिल करनी होगी?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val geothermal = 25 of kelvinPerKilometer
val borehole = 3.5 of kilo.meters

val rise = geothermal * borehole            // KTemperatureDifferenceUnitInstance
rise into KTemperatureDifference.ofKelvin(1) // तली में 87.5 K अधिक गर्म

val depthFor100K = KTemperatureDifference.ofKelvin(100) / geothermal // KLengthUnitInstance
depthFor100K into kilo.meters               // 4.0 km
depthFor100K into meters                    // 4000.0 m
```

## मूल इकाइयों (तापमान अंतर व लंबाई) से गणना

| व्यंजक                                           | परिणाम प्रकार                             | अर्थ          |
|-----------------------------------------------|--------------------------------------|-------------|
| `temperatureDifference / length`              | `KTemperatureGradientUnitInstance`   | प्रवणता        |
| `temperatureGradient * length`                | `KTemperatureDifferenceUnitInstance` | लंबाई पर वृद्धि   |
| `length * temperatureGradient`                | `KTemperatureDifferenceUnitInstance` | वृद्धि (क्रमविनिमेय) |
| `temperatureDifference / temperatureGradient` | `KLengthUnitInstance`                | फैली हुई लंबाई   |

## अपघटन

दोनों अपघटन समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं।

| अपघटन                            | रूप                                 | परिणाम                               |
|----------------------------------|------------------------------------|------------------------------------|
| `temperatureDifference / length` | टाइप किया गया संकारक                     | `KTemperatureGradientUnitInstance` |
| `temperature · distance⁻¹`       | मूल व्यंजक + `toTemperatureGradient()` | `KTemperatureGradientUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val typed  = KTemperatureDifference.ofKelvin(1) / (1 of meters)
val native = (KTemperatureDifference.ofKelvin(1).toUnit() / (1 of meters).toUnit()).toTemperatureGradient()

typed == native // true - दोनों 1.0 K/m हैं
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*

val total = (1 of kelvinPerMeter) + (500 of kelvinPerKilometer)  // 1.5 K/m
(1 of kelvinPerMeter) > (500 of kelvinPerKilometer)              // true
(1 of kelvinPerMeter) == (1000 of kelvinPerKilometer)            // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*

(25 of kelvinPerKilometer).toString()                        // "0.025 K/m"
"${(25 of kelvinPerKilometer) into kelvinPerKilometer} K/km" // "25.0 K/km"
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे।
घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को
भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित           | Kotlin                                                     | अर्थ                       |
|---------------|------------------------------------------------------------|--------------------------|
| `K/m`         | `kelvinPerMeter`                                           | तापमान प्रवणता, मूल इकाई        |
| `K·m⁻¹`       | `ΔK / meters`                                              | वही राशि आधार आयामों में          |
| `K/km`        | `kelvinPerKilometer`                                       | केल्विन प्रति किलोमीटर (भूतापीय प्रवणता) |
| `°F/ft`       | `fahrenheitPerFoot`                                        | डिग्री फारेनहाइट प्रति फुट          |
| `∇T = ΔT / L` | `KTemperatureDifference.ofKelvin(25) / (1 of kilo.meters)` | वृद्धि ÷ लंबाई से प्रवणता          |
| `ΔT = ∇T · L` | `geothermal * borehole`                                    | प्रवणता × लंबाई से वृद्धि          |
| `L = ΔT / ∇T` | `KTemperatureDifference.ofKelvin(100) / geothermal`        | वृद्धि ÷ प्रवणता से लंबाई          |
