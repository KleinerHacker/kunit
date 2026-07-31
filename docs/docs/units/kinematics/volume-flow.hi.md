# आयतन प्रवाह

पैकेज: `org.pcsoft.framework.kunit.kinematic.volumeflow`
मूल इकाई: **घन मीटर प्रति सेकंड** (`KVolumeFlowUnit.BASE == KVolumeFlowUnit.CUBIC_METER_PER_SECOND`)

प्रकार: **संघटित इकाई**

आयतन प्रवाह (आयतन प्रवाह दर) यह बताता है कि प्रति इकाई समय में एक अनुप्रस्थ काट से कितना आयतन गुजरता है:
`distance³ · time⁻¹` (`m³/s`)। `KVolumeFlowUnitInstance` ठीक दो पदों वाले `KMixedUnitInstance` को लपेटता है — एक
`KDistanceUnit.BASE` (मीटर) घातांक `+3` पर और एक `KTimeUnit.BASE` (सेकंड) घातांक `-1` पर। मान हमेशा घन मीटर प्रति सेकंड
में सामान्यीकृत रहता है, चाहे इसे किसी भी इकाई या आयतन/समय संयोजन से बनाया गया हो।

ऊर्जा या शक्ति के विपरीत, आयतन प्रवाह में **कोई** द्रव्यमान आयाम नहीं है, इसलिए इसका संग्रहीत मान ही `m³/s`
में पठन *है* — कोई ग्राम/किलोग्राम सेतु शामिल नहीं है।

## नामित इकाइयाँ

| इकाई           | संकेत     |                    टोकन |      1 इकाई = ? m³/s |
|---------------|---------|-----------------------:|--------------------:|
| घन मीटर प्रति सेकंड | `m³/s`  | `cubicMetersPerSecond` |                 1.0 |
| घन मीटर प्रति घंटा  | `m³/h`  |   `cubicMetersPerHour` |   1/3600 ≈ 2.778e-4 |
| लीटर प्रति सेकंड    | `l/s`   |      `litersPerSecond` |               0.001 |
| लीटर प्रति मिनट    | `l/min` |      `litersPerMinute` | 0.001/60 ≈ 1.667e-5 |
| US गैलन प्रति मिनट | `gpm`   |   `usGallonsPerMinute` |          ≈ 6.309e-5 |

इन सभी में पूर्ण SI उपसर्ग सीमा भी समर्थित है (`milli.litersPerSecond`, `kilo.cubicMetersPerHour`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

val q = 5 of litersPerSecond
q.value                       // 0.005 (m³/s में सामान्यीकृत)
q into litersPerMinute        // 300.0
q into cubicMetersPerHour     // 18.0
q into usGallonsPerMinute     // ≈ 79.25
(250 of milli.litersPerSecond) into litersPerSecond // 0.25
```

## वास्तविक उदाहरण: वर्षाजल टैंक भरना

एक बाग़ का पंप **300 l/min** की दर से **5 m³** के टैंक में पानी भरता है। टैंक को भरने में कितना समय लगेगा, और पंप
डेटाशीट में प्रयुक्त इकाइयों में प्रवाह दर क्या है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

val pump = 300 of litersPerMinute
val tank = 5000 of liters

val fillTime = tank / pump          // KTimeUnitInstance
fillTime into minutes               // ≈ 16.67 मिनट

pump into cubicMetersPerHour        // 18.0 m³/h (डेटाशीट इकाई)
pump into usGallonsPerMinute        // ≈ 79.25 gpm

// उल्टी दिशा: एक चौथाई घंटे में कितना पानी?
val volume = pump * (15 of minutes) // KVolumeUnitInstance
volume into liters                  // 4500.0
```

## मूल इकाइयों (आयतन और समय) से गणना

| व्यंजक                   | परिणाम प्रकार                  | अर्थ                  |
|-----------------------|---------------------------|---------------------|
| `volume / time`       | `KVolumeFlowUnitInstance` | प्रवाह दर = आयतन / अवधि |
| `volumeFlow * time`   | `KVolumeUnitInstance`     | आयतन = प्रवाह दर × अवधि |
| `time * volumeFlow`   | `KVolumeUnitInstance`     | आयतन (क्रमविनिमेय)       |
| `volume / volumeFlow` | `KTimeUnitInstance`       | अवधि = आयतन / प्रवाह दर |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

val q = (600 of liters) / (2 of minutes)  // KVolumeFlowUnitInstance
q into cubicMetersPerSecond               // 0.005

val v = q * (60 of seconds)               // KVolumeUnitInstance
v into liters                             // 300.0

val t = (600 of liters) / q               // KTimeUnitInstance
t into minutes                            // 2.0
```

## अपघटन

आयतन प्रवाह तक दो तरीकों से पहुँचा जा सकता है; दोनों ही समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं।

| अपघटन                | रूप                        | परिणाम                         |
|----------------------|---------------------------|------------------------------|
| `volume / time`      | टाइप किया गया संकारक            | सीधे `KVolumeFlowUnitInstance` |
| `distance³ · time⁻¹` | मूल व्यंजक + `toVolumeFlow()` | `KVolumeFlowUnitInstance`    |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

// टाइप किया गया संकारक रूप
val typed = (8000 of liters) / (4 of seconds)

// मूल आधार-आयाम रूप (m³ · s⁻¹), toVolumeFlow() द्वारा पहचाना गया
val native = (((2 of meters).toUnit() pow 3) / (4 of seconds).toUnit()).toVolumeFlow()

typed == native // true - दोनों 2.0 m³/s हैं
```

`toVolumeFlow()` **केवल** विहित सामान्य रूप को पहचानता है (घातांक `+3` पर एक `KDistanceUnit` पद और घातांक `-1` पर एक
`KTimeUnit` पद); कोई भी समतुल्य व्यंजक स्वतः इस पर सिकुड़ जाता है। एक गलत रूप चुपचाप गलत मान लौटाने के बजाय
`IllegalStateException` फेंकता है।

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

// + / - : समान समूह, विभिन्न प्रवाह इकाइयों के बीच स्वतः रूपांतरण
val a = (1 of litersPerSecond) + (60 of litersPerMinute)   // 2 l/s
val b = (1 of litersPerSecond) - (30 of litersPerMinute)   // 0.5 l/s

// तुलनाएँ (सामान्यीकृत m³/s मान के अनुसार)
(1 of litersPerSecond) > (30 of litersPerMinute)   // true
(1 of litersPerSecond) == (60 of litersPerMinute)  // true

// दो प्रवाहों के बीच * / / एक KMixedUnitInstance में बाहर निकल जाते हैं
val squared = (1 of litersPerSecond) * (1 of litersPerSecond) // KMixedUnitInstance, [m^6, s^-2]
```

## toString स्वरूपण

`toString()` मान को मूल इकाई में प्रस्तुत करता है; किसी अन्य इकाई के लिए `into` का प्रयोग करें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

(5 of litersPerSecond).toString()                       // "0.005 m³/s"
"${(5 of litersPerSecond) into litersPerMinute} l/min"  // "300.0 l/min"
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे।
घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को
भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित         | Kotlin                             | अर्थ                         |
|-------------|------------------------------------|----------------------------|
| `m³/s`      | `cubicMetersPerSecond`             | आयतन प्रवाह, मूल इकाई — नामित टोकन |
| `m³·s⁻¹`    | `(meters pow 3) / seconds`         | वही प्रवाह आधार-आयाम व्यंजक के रूप में  |
| `l/s`       | `litersPerSecond`                  | लीटर प्रति सेकंड                 |
| `l/min`     | `litersPerMinute`                  | लीटर प्रति मिनट                 |
| `m³/h`      | `cubicMetersPerHour`               | घन मीटर प्रति घंटा               |
| `V / t`     | `(600 of liters) / (2 of minutes)` | आयतन ÷ समय से निर्माण           |
| `V = q̇ · t` | `q * (60 of seconds)`              | प्रवाह दर × अवधि से आयतन        |
| `t = V / q̇` | `(600 of liters) / q`              | आयतन ÷ प्रवाह दर से अवधि        |
