# आवेग

पैकेज: `org.pcsoft.framework.kunit.mechanic.momentum`
मूल इकाई: **किलोग्राम मीटर प्रति सेकंड** (`KMomentumUnit.BASE`), जिसे **न्यूटन सेकंड** के रूप में पढ़ा जाता है
(`KMomentumUnit.NEWTON_SECOND`)

प्रकार: **निर्मित इकाई**

आवेग `J = F · t` वह संवेग है जो एक बल अपने कार्यरत समय में प्रदान करता है। आयामिक रूप से यह *एक*
[संवेग](momentum.md) *है*: `1 N·s = 1 kg·m/s`। इसलिए KUnit इसके लिए **कोई दूसरा** इकाई समूह पेश **नहीं**
करता — आवेग संवेग समूह का एक **पठन** है, जिसे `newtonSeconds` टोकन से व्यक्त किया जाता है। यह पृष्ठ उस पठन का
दस्तावेज़ीकरण करता है; समूह स्वयं [संवेग](momentum.md) पृष्ठ पर वर्णित है।

!!! note "एक समूह, दो पठन"
`(1 of newtonSeconds) == (1 of kilogramMetersPerSecond)` `true` है। टोकन चुनना केवल यह बदलता है कि आप किसी मान को कैसे
पढ़ते हैं, कभी नहीं कि वह क्या है। "बल × समय" के रूप में सोचने पर
`newtonSeconds` का उपयोग करें, और "द्रव्यमान × वेग" के रूप में सोचने पर `kilogramMetersPerSecond`।

## नामित इकाइयाँ

| इकाई             | प्रतीक       |                        टोकन | kg·m/s में 1 इकाई |
|-----------------|-----------|---------------------------:|---------------:|
| न्यूटन सेकंड         | `N*s`     |            `newtonSeconds` |            1.0 |
| किलोग्राम मीटर प्रति सेकंड | `kg*m/s`  |  `kilogramMetersPerSecond` |            1.0 |
| ग्राम सेंटीमीटर प्रति सेकंड | `g*cm/s`  | `gramCentimetersPerSecond` |           1e-5 |
| पाउंड-फुट प्रति सेकंड   | `lb*ft/s` |       `poundFeetPerSecond` |     ≈ 0.138255 |

हर टोकन के लिए उपसर्ग-युक्त रूप मौजूद हैं (`kilo.newtonSeconds` = kN·s, `milli.newtonSeconds` = mN·s)।

## एक आवेग की गणना

| व्यंजक               | परिणाम प्रकार                | अर्थ                    |
|-------------------|-------------------------|-----------------------|
| `force * time`    | `KMomentumUnitInstance` | `J = F · t`           |
| `time * force`    | `KMomentumUnitInstance` | वही, क्रमविनिमेय            |
| `impulse / time`  | `KForceUnitInstance`    | औसत बल `F = J / t`    |
| `impulse / force` | `KTimeUnitInstance`     | कार्यरत समय `t = J / F`  |
| `impulse / mass`  | `KSpeedUnitInstance`    | वेग परिवर्तन `Δv = J / m` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.momentum.*

val j = (10 of newtons) * (3 of seconds)
j into newtonSeconds             // 30.0
j into kilogramMetersPerSecond   // 30.0 (समान आयाम)
```

## वास्तविक उदाहरण: रॉकेट चरण दहन

एक मॉडल रॉकेट मोटर 1.6 s के लिए औसत 12 N प्रणोद प्रदान करता है। यह कुल कितना आवेग उत्पन्न करता है, और 0.8 kg के रॉकेट को
यह कितना वेग परिवर्तन देता है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.momentum.*

val impulse = (12 of newtons) * (1.6 of seconds)
impulse into newtonSeconds              // 19.2

val deltaV = impulse / (0.8 of kilo.grams) // KSpeedUnitInstance
deltaV into (meters / seconds)             // 24.0
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

val total = (19.2 of newtonSeconds) + (5 of newtonSeconds) // 24.2 N·s
(19.2 of newtonSeconds) > (10 of newtonSeconds)            // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

(19.2 of newtonSeconds).toString()                  // "19.2 kg*m/s" (समूह की मूल इकाई)
"${(19.2 of newtonSeconds) into newtonSeconds} N*s" // "19.2 N*s"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखा जाता है।
घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित          | Kotlin                                   | अर्थ                      |
|--------------|------------------------------------------|-------------------------|
| `N·s`        | `newtonSeconds`                          | आवेग (संवेग समूह का नामित टोकन) |
| `kg·m·s⁻¹`   | `kilo.grams * meters * (seconds pow -1)` | वही राशि आधार आयामों में         |
| `J = F · t`  | `force * time`                           | टाइप किया गया अपघटन         |
| `F = J / t`  | `impulse / time`                         | औसत बल के लिए हल किया गया    |
| `Δv = J / m` | `impulse / mass`                         | एक द्रव्यमान का वेग परिवर्तन      |
| `kN·s`       | `kilo.newtonSeconds`                     | उपसर्ग-युक्त आवेग             |
