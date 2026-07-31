# ऊष्मा क्षमता

पैकेज: `org.pcsoft.framework.kunit.thermo.heatcapacity`
मूल इकाई: **जूल प्रति केल्विन** (`KHeatCapacityUnit.BASE == KHeatCapacityUnit.JOULE_PER_KELVIN`)

प्रकार: **संघटित इकाई**

ऊष्मा क्षमता वह ऊर्जा है जो कोई वस्तु प्रति इकाई तापमान वृद्धि पर अवशोषित करती है: `energy / temperature`
(`J/K`)। `KHeatCapacityUnitInstance` विहित सामान्य रूप `mass¹ · distance² · time⁻² · temperature⁻¹`
(`kg·m²·s⁻²·K⁻¹`) में ठीक चार पदों वाले `KMixedUnitInstance` को लपेटता है, जो हमेशा J/K में सामान्यीकृत रहता है।

!!! note "तापमान *अंतर*, कभी भी परम तापमान नहीं"
तापमान आयाम **अंतर** समूह है (`KTemperatureDifferenceUnit`, संकेत `ΔK`), कभी भी ऐफ़ाइन परम
`KTemperatureUnit` नहीं। ऊष्मा क्षमता ऊर्जा को एक तापमान *अंतराल* से जोड़ती है; एक ऑफ़सेट-युक्त परम पैमाना (°C, °F)
भागफल में भौतिक रूप से गलत होगा।

वही आयाम `J/K` **एन्ट्रॉपी** का भी वर्णन करता है — देखें [एन्ट्रॉपी](entropy.md) कि वह राशि अपना प्रकार न बनाकर इसी को
क्यों साझा करती है। प्रति इकाई द्रव्यमान यह
[विशिष्ट ऊष्मा क्षमता](specific-heat-capacity.md) बन जाती है, प्रति मोल
[मोलर ऊष्मा क्षमता](molar-heat-capacity.md)।

## नामित इकाइयाँ

| इकाई              | संकेत      |                 टोकन | 1 इकाई = ? J/K |
|------------------|----------|--------------------:|--------------:|
| जूल प्रति केल्विन        | `J/K`    |   `joulesPerKelvin` |           1.0 |
| कैलोरी प्रति केल्विन       | `cal/K`  | `caloriesPerKelvin` |         4.184 |
| Btu प्रति डिग्री फ़ारेनहाइट | `Btu/°F` | `btusPerFahrenheit` |   ≈ 1899.1005 |

सभी में पूर्ण SI उपसर्ग सीमा समर्थित है (`kilo.joulesPerKelvin`, `kilo.caloriesPerKelvin`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

val c = 4184 of joulesPerKelvin
c into kilo.joulesPerKelvin  // 4.184
c into caloriesPerKelvin     // 1000.0
```

## वास्तविक उदाहरण: केतली का पानी गर्म करना

एक लीटर पानी (4184 J/K) को 20 °C से 100 °C तक गर्म किया जाता है। इसमें कितनी ऊर्जा लगती है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.*

val kettle = 4184 of joulesPerKelvin          // 1 लीटर पानी
val rise = (100 of celsius) - (20 of celsius) // KTemperatureDifferenceUnitInstance, 80 K

val energy = kettle * rise                    // KEnergyUnitInstance
energy into joules                            // 334_720.0 J
energy into kilo.joules                       // 334.72 kJ

// ... और उल्टी दिशा में: 100 kJ से हम कितनी दूर पहुँच सकते हैं?
val reachable = (100 of kilo.joules) / kettle // KTemperatureDifferenceUnitInstance
reachable into KTemperatureDifference.ofKelvin(1) // ≈ 23.9 K
```

## मूल इकाइयों (ऊर्जा और तापमान अंतर) से गणना

| व्यंजक                                    | परिणाम प्रकार                             | अर्थ          |
|----------------------------------------|--------------------------------------|-------------|
| `energy / temperatureDifference`       | `KHeatCapacityUnitInstance`          | ऊष्मा क्षमता      |
| `heatCapacity * temperatureDifference` | `KEnergyUnitInstance`                | आवश्यक ऊर्जा     |
| `temperatureDifference * heatCapacity` | `KEnergyUnitInstance`                | ऊर्जा (क्रमविनिमेय) |
| `energy / heatCapacity`                | `KTemperatureDifferenceUnitInstance` | प्राप्य तापमान वृद्धि  |

## अपघटन

दोनों अपघटन समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं।

| अपघटन                                       | रूप                          | परिणाम                           |
|---------------------------------------------|-----------------------------|--------------------------------|
| `energy / temperatureDifference`            | टाइप किया गया संकारक              | सीधे `KHeatCapacityUnitInstance` |
| `mass · distance² · time⁻² · temperature⁻¹` | मूल व्यंजक + `toHeatCapacity()` | `KHeatCapacityUnitInstance`    |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

// टाइप किया गया संकारक रूप
val typed = (1 of joules) / KTemperatureDifference.ofKelvin(1)

// मूल आधार-आयाम रूप (kg·m²·s⁻²·K⁻¹), toHeatCapacity() द्वारा पहचाना गया
val native = (
    (1000 of grams).toUnit() *
        ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toHeatCapacity()

typed == native // true - दोनों 1.0 J/K हैं
```

`toHeatCapacity()` **केवल** विहित सामान्य रूप को पहचानता है; कोई भी समतुल्य व्यंजक स्वतः इस पर सिकुड़ जाता है, और एक गलत
रूप `IllegalStateException` फेंकता है।

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

// + / - : समान समूह, इकाइयों और उपसर्गों के बीच स्वतः रूपांतरण
val total = (1 of kilo.joulesPerKelvin) + (500 of joulesPerKelvin)  // 1500 J/K
val rest  = (1 of kilo.joulesPerKelvin) - (250 of joulesPerKelvin)  // 750 J/K

// तुलनाएँ (सामान्यीकृत J/K मान के अनुसार)
(1 of kilo.joulesPerKelvin) > (500 of joulesPerKelvin)   // true
(1 of kilo.joulesPerKelvin) == (1000 of joulesPerKelvin) // true

// दो ऊष्मा क्षमताओं के बीच * / / एक KMixedUnitInstance में बाहर निकल जाते हैं
val squared = (2 of joulesPerKelvin) * (2 of joulesPerKelvin)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

(4184 of joulesPerKelvin).toString()                          // "4184.0 J/K"
"${(4184 of joulesPerKelvin) into caloriesPerKelvin} cal/K"   // "1000.0 cal/K"
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे।
घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को
भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित             | Kotlin                                          | अर्थ                       |
|-----------------|-------------------------------------------------|--------------------------|
| `J/K`           | `joulesPerKelvin`                               | ऊष्मा क्षमता, मूल इकाई — नामित टोकन |
| `kg·m²·s⁻²·K⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / ΔK` | वही राशि आधार आयामों में          |
| `kJ/K`          | `kilo.joulesPerKelvin`                          | किलोजूल प्रति केल्विन              |
| `cal/K`         | `caloriesPerKelvin`                             | कैलोरी प्रति केल्विन               |
| `C = Q / ΔT`    | `(4184 of joules) / rise`                       | ऊर्जा ÷ तापमान वृद्धि से ऊष्मा क्षमता    |
| `Q = C · ΔT`    | `kettle * rise`                                 | ऊष्मा क्षमता × तापमान वृद्धि से ऊर्जा    |
| `ΔT = Q / C`    | `(100 of kilo.joules) / kettle`                 | ऊर्जा ÷ ऊष्मा क्षमता से तापमान वृद्धि    |
