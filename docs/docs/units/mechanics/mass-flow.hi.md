# द्रव्यमान प्रवाह दर

पैकेज: `org.pcsoft.framework.kunit.mechanic.massflow`
मूल इकाई: **किलोग्राम प्रति सेकंड** (`KMassFlowUnit.BASE == KMassFlowUnit.KILOGRAMS_PER_SECOND`)

प्रकार: **निर्मित इकाई**

द्रव्यमान प्रवाह दर `ṁ` प्रति इकाई समय में परिवहित द्रव्यमान है — [आयतन प्रवाह](../kinematics/volume-flow.md)
का द्रव्यमान समकक्ष। यह एक **निर्मित** इकाई है — संघटन `mass · time⁻¹` (`kg/s`)।

`KMassFlowUnitInstance` एक `KMixedUnitInstance` को लपेटता है जिसमें कैनोनिकल सामान्य रूप में ठीक दो पद होते हैं:
`KMassUnit.BASE` (ग्राम) घातांक `+1` पर और `KTimeUnit.BASE` (सेकंड) घातांक `-1` पर। चूँकि इस लाइब्रेरी का द्रव्यमान घटक
ग्राम में सामान्यीकृत है, इसलिए संचित मान कच्चा ग्राम-आधारित घटक मान है और kg/s में पठन एक स्थिर गुणांक से विभाजित होते
हैं।

## नामित इकाइयाँ

| इकाई         | प्रतीक    |                  टोकन |       kg/s में 1 इकाई |
|-------------|--------|---------------------:|-------------------:|
| किलोग्राम प्रति सेकंड | `kg/s` | `kilogramsPerSecond` |                1.0 |
| ग्राम प्रति सेकंड   | `g/s`  |     `gramsPerSecond` |               1e-3 |
| किलोग्राम प्रति घंटा  | `kg/h` |   `kilogramsPerHour` |             1/3600 |
| टन प्रति घंटा    | `t/h`  |      `tonnesPerHour` | 1000/3600 ≈ 0.2778 |
| पाउंड प्रति सेकंड  | `lb/s` |    `poundsPerSecond` |         0.45359237 |
| पाउंड प्रति घंटा   | `lb/h` |      `poundsPerHour` |       ≈ 1.25998e-4 |

सभी इकाइयाँ पूरे SI उपसर्ग परिसर को स्वीकार करती हैं (डोज़िंग पंपों के लिए `milli.gramsPerSecond`)।

## अपघटन

द्रव्यमान प्रवाह के दो समतुल्य अपघटन हैं; दोनों एक ही सामान्यीकरण फैक्ट्री में जाते हैं।

| रूप             | Kotlin                                         | परिणाम प्रकार                |
|----------------|------------------------------------------------|-------------------------|
| द्रव्यमान / समय     | `mass / time`                                  | `KMassFlowUnitInstance` |
| घनत्व × आयतन प्रवाह | `density * volumeflow`                         | `KMassFlowUnitInstance` |
| नेटिव व्यंजक        | `(mass.toUnit() / time.toUnit()).toMassFlow()` | `KMassFlowUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.cubicMetersPerSecond
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.massflow.*

val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val viaMassTime = (2000 of kilo.grams) / (1 of seconds)
val viaDensityFlow = water * (2 of cubicMetersPerSecond)

viaMassTime == viaDensityFlow          // true - दोनों 2000 kg/s हैं
viaMassTime into kilogramsPerSecond    // 2000.0
```

## मूल इकाइयों के साथ गणना

| व्यंजक                                            | परिणाम प्रकार                  | अर्थ                     |
|------------------------------------------------|---------------------------|------------------------|
| `mass / time`                                  | `KMassFlowUnitInstance`   | `ṁ = m / t`            |
| `massflow * time`, `time * massflow`           | `KMassUnitInstance`       | परिवहित द्रव्यमान `m = ṁ · t` |
| `mass / massflow`                              | `KTimeUnitInstance`       | आवश्यक समय `t = m / ṁ`   |
| `density * volumeflow`, `volumeflow * density` | `KMassFlowUnitInstance`   | `ṁ = ρ · Q`            |
| `massflow / density`                           | `KVolumeFlowUnitInstance` | `Q = ṁ / ρ`            |
| `massflow / volumeflow`                        | `KDensityUnitInstance`    | `ρ = ṁ / Q`            |

## वास्तविक उदाहरण: पंप का उत्पादन (throughput)

एक पंप 15 m³/h जल (ρ = 998 kg/m³) प्रवाहित करता है। यह t/h में कौन-सा द्रव्यमान प्रवाह है, और 8 घंटों में कितना
द्रव्यमान गुजरता है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.volumeflow.cubicMetersPerHour
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.massflow.*

val water = (998 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val flow = water * (15 of cubicMetersPerHour)
flow into tonnesPerHour                 // ≈ 14.97

val perShift = flow * (8 of hours)      // KMassUnitInstance
perShift into kilo.grams                // ≈ 119760.0
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.massflow.*

val sum = (10 of kilogramsPerSecond) + (4 of kilogramsPerSecond) // 14 kg/s
(1 of kilogramsPerSecond) > (1 of tonnesPerHour)                 // true
(3.6 of tonnesPerHour) == (1 of kilogramsPerSecond)              // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.massflow.*

(2 of kilogramsPerSecond).toString()                     // "2.0 kg/s" (मूल इकाई)
"${(2 of kilogramsPerSecond) into tonnesPerHour} t/h"    // "7.2 t/h"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखा जाता है।
घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित         | Kotlin                          | अर्थ                         |
|-------------|---------------------------------|----------------------------|
| `kg/s`      | `kilogramsPerSecond`            | द्रव्यमान प्रवाह, मूल इकाई (नामित टोकन) |
| `kg·s⁻¹`    | `kilo.grams * (seconds pow -1)` | वही राशि शुद्ध गुणनफल के रूप में      |
| `t/h`       | `tonnesPerHour`                 | औद्योगिक उत्पादन पठन              |
| `ṁ = m / t` | `mass / time`                   | अपघटन A                    |
| `ṁ = ρ · Q` | `density * volumeflow`          | अपघटन B                    |
| `Q = ṁ / ρ` | `massflow / density`            | आयतन प्रवाह के लिए हल किया गया     |
| `mg/s`      | `milli.gramsPerSecond`          | उपसर्ग-युक्त द्रव्यमान प्रवाह           |

</content>
