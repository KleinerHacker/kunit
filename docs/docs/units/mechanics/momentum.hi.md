# संवेग

पैकेज: `org.pcsoft.framework.kunit.mechanic.momentum`
मूल इकाई: **किलोग्राम मीटर प्रति सेकंड**
(`KMomentumUnit.BASE == KMomentumUnit.KILOGRAM_METERS_PER_SECOND`)

प्रकार: **निर्मित इकाई**

संवेग `p = m · v` किसी पिंड की "गति की मात्रा" है। यह एक **निर्मित** इकाई है — संघटन
`mass · length · time⁻¹` (`kg·m/s`)।

`KMomentumUnitInstance` विहित सामान्य रूप में ठीक तीन पदों के एक `KMixedUnitInstance` को लपेटता है:
`KMassUnit.BASE` (ग्राम) `+1` पर, `KDistanceUnit.BASE` (मीटर) `+1` पर, और `KTimeUnit.BASE` (सेकंड)
`-1` पर। चूँकि पुस्तकालय का द्रव्यमान घटक ग्राम में सामान्यीकृत है, संग्रहित मान कच्चा ग्राम-आधारित घटक मान है और kg·m/s
में पठन एक स्थिर गुणक से विभाजित होते हैं।

!!! note "आवेग वही राशि है"
**आवेग** `F · t` का ठीक यही आयाम है (`1 N·s = 1 kg·m/s`), इसलिए यह अपने खुद के समूह के बजाय *इसी* समूह में है —
देखें [आवेग](impulse.md) पृष्ठ।

## नामित इकाइयाँ

| इकाई             | प्रतीक       |                        टोकन | kg·m/s में 1 इकाई |
|-----------------|-----------|---------------------------:|---------------:|
| किलोग्राम मीटर प्रति सेकंड | `kg*m/s`  |  `kilogramMetersPerSecond` |            1.0 |
| न्यूटन सेकंड         | `N*s`     |            `newtonSeconds` |            1.0 |
| ग्राम सेंटीमीटर प्रति सेकंड | `g*cm/s`  | `gramCentimetersPerSecond` |           1e-5 |
| पाउंड-फुट प्रति सेकंड   | `lb*ft/s` |       `poundFeetPerSecond` |     ≈ 0.138255 |

सभी इकाइयाँ पूर्ण SI उपसर्ग सीमा स्वीकार करती हैं (`kilo.newtonSeconds`, `milli.kilogramMetersPerSecond`)।

## अपघटन

संवेग के दो समतुल्य अपघटन हैं; ये सभी उसी सामान्यीकरण फैक्ट्री में जाते हैं और इसलिए वही टाइप किया गया, मान-समान परिणाम
उत्पन्न करते हैं।

| रूप             | Kotlin                                                           | परिणाम प्रकार                |
|----------------|------------------------------------------------------------------|-------------------------|
| द्रव्यमान × चाल      | `mass * speed`                                                   | `KMomentumUnitInstance` |
| बल × समय (आवेग) | `force * time`                                                   | `KMomentumUnitInstance` |
| मूल व्यंजक         | `(mass.toUnit() * length.toUnit() / time.toUnit()).toMomentum()` | `KMomentumUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.momentum.*

val speed = (3 of meters) / (1 of seconds)
val viaMassSpeed = (2 of kilo.grams) * speed
val viaForceTime = (6 of newtons) * (1 of seconds)
val viaNative =
    ((2000 of grams).toUnit() * (3 of meters).toUnit() / (1 of seconds).toUnit()).toMomentum()

viaMassSpeed == viaForceTime            // true
viaMassSpeed == viaNative               // true
viaMassSpeed into kilogramMetersPerSecond // 6.0
```

## मूल इकाइयों के साथ गणना

| व्यंजक                            | परिणाम प्रकार                       | अर्थ                             |
|--------------------------------|--------------------------------|--------------------------------|
| `mass * speed`, `speed * mass` | `KMomentumUnitInstance`        | `p = m · v`                    |
| `force * time`, `time * force` | `KMomentumUnitInstance`        | आवेग `p = F · t`                |
| `momentum / mass`              | `KSpeedUnitInstance`           | `v = p / m`                    |
| `momentum / speed`             | `KMassUnitInstance`            | `m = p / v`                    |
| `momentum / time`              | `KForceUnitInstance`           | औसत बल `F = p / t`             |
| `momentum / force`             | `KTimeUnitInstance`            | कार्यरत समय `t = p / F`           |
| `momentum * length`            | `KAngularMomentumUnitInstance` | [कोणीय संवेग](angular-momentum.md) |

## वास्तविक उदाहरण: कार को ब्रेक करना

1200 kg की एक कार 20 m/s की चाल से चलती है। इसका संवेग क्या है, और कौन-सा स्थिर बल इसे 5 s में रोकता है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.momentum.*

val v = (20 of meters) / (1 of seconds)
val p = (1200 of kilo.grams) * v
p into kilogramMetersPerSecond      // 24000.0

val brakingForce = p / (5 of seconds)
brakingForce into newtons           // 4800.0
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

val sum = (10 of newtonSeconds) + (4 of newtonSeconds) // 14 N·s
(10 of kilogramMetersPerSecond) > (4 of newtonSeconds) // true
(1 of newtonSeconds) == (1 of kilogramMetersPerSecond) // true (समान आयाम)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

(6 of kilogramMetersPerSecond).toString()          // "6.0 kg*m/s" (मूल इकाई)
"${(6 of kilogramMetersPerSecond) into newtonSeconds} N*s" // "6.0 N*s"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखा जाता है।
घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित         | Kotlin                                   | अर्थ                       |
|-------------|------------------------------------------|--------------------------|
| `kg·m/s`    | `kilogramMetersPerSecond`                | संवेग, मूल इकाई (नामित टोकन)    |
| `kg·m·s⁻¹`  | `kilo.grams * meters * (seconds pow -1)` | वही राशि एक शुद्ध गुणनफल के रूप में |
| `N·s`       | `newtonSeconds`                          | समान आयाम की आवेग वर्तनी        |
| `p = m · v` | `mass * speed`                           | अपघटन A                  |
| `p = F · t` | `force * time`                           | अपघटन B (आवेग)            |
| `v = p / m` | `momentum / mass`                        | चाल के लिए हल किया गया         |
| `F = p / t` | `momentum / time`                        | औसत बल के लिए हल किया गया     |
