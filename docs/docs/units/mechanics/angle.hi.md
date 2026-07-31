# कोण

पैकेज: `org.pcsoft.framework.kunit.mechanic.angle`
मूल इकाई: **रेडियन** (`KAngleUnit.BASE == KAngleUnit.RADIAN`)

प्रकार: **मूल इकाई**

समतल कोण KUnit की एक **मूल** इकाई है: एक सीधे मापे जाने योग्य आधार राशि जिसकी अपनी इकाई शब्दावली है, न कि कोई संघटन।
`KAngleUnitInstance` एक `KMixedUnitInstance` को लपेटता है जिसमें एकल `KAngleUnit.BASE`
पद घातांक 1 पर होता है, जो हमेशा रेडियन में सामान्यीकृत रहता है।

कोण यांत्रिकी के संपूर्ण घूर्णी भाग की नींव है: [कोणीय वेग](angular-velocity.md),
[कोणीय त्वरण](angular-acceleration.md), [कोणीय संवेग](angular-momentum.md) और
[घन कोण](solid-angle.md) सभी इसी पर आधारित हैं।

## नामित इकाइयाँ

| इकाई       | प्रतीक   |          टोकन |       rad में 1 इकाई |
|-----------|-------|-------------:|------------------:|
| रेडियन      | `rad` |    `radians` |               1.0 |
| डिग्री        | `°`   |    `degrees` | π/180 ≈ 0.0174533 |
| आर्कमिनट     | `'`   | `arcminutes` |           π/10800 |
| आर्कसेकंड     | `"`   | `arcseconds` |          π/648000 |
| ग्रेडियन (गॉन) | `gon` |   `gradians` |             π/200 |
| टर्न (चक्कर)  | `tr`  |      `turns` |       2π ≈ 6.2832 |

सभी इकाइयाँ पूर्ण SI उपसर्ग सीमा स्वीकार करती हैं (`milli.radians`, खगोलमिति के लिए `micro.arcseconds`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.angle.*

val a = 90 of degrees
a into radians      // ≈ 1.5708
a into turns        // 0.25
a into gradians     // 100.0
1 of milli.radians  // 0.001 rad
```

## कोणों के साथ गणना

| व्यंजक                              | परिणाम प्रकार                       | अर्थ                  |
|----------------------------------|--------------------------------|---------------------|
| `angle + angle`, `angle - angle` | `KAngleUnitInstance`           | समान-प्रकार अंकगणित       |
| `angle * angle`                  | `KSolidAngleUnitInstance`      | घन कोण (`rad² = sr`) |
| `angle / time`                   | `KAngularVelocityUnitInstance` | कोणीय वेग `ω = φ / t`  |
| `angle / angularvelocity`        | `KTimeUnitInstance`            | घूर्णन में लगने वाला समय    |
| `angle / angle`                  | `KMixedUnitInstance`           | विमाहीन अनुपात           |

त्रिकोणमितीय फलन सीधे मान पर उपलब्ध हैं, क्योंकि वे रेडियन पठन का उपयोग करते हैं: `angle.sin()`,
`angle.cos()`, `angle.tan()`।

## वास्तविक उदाहरण: गियरबॉक्स आउटपुट कोण

एक मोटर शाफ्ट 3 पूर्ण चक्कर घूमता है। 5:1 अनुपात वाली गियर जोड़ी इसे कम करती है। डिग्री में आउटपुट कोण क्या है, और 600
rpm पर यह गति कितने समय में होती है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val input = 3 of turns
val output = input / 5                 // KAngleUnitInstance, 0.6 turns
output into degrees                    // 216.0

val t = input / (600 of revolutionsPerMinute) // KTimeUnitInstance
t into seconds                                // 0.3
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.*

val sum = (90 of degrees) + (30 of degrees) // 120°
(1 of turns) > (359 of degrees)             // true
(180 of degrees) == (0.5 of turns)          // true (मान-आधारित समानता)
(90 of degrees).sin()                       // 1.0
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.*

(2 of radians).toString()                    // "2.0 rad" (मूल इकाई)
"${(1 of turns) into degrees} °"             // "360.0 °"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखा जाता है।
घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित             | Kotlin                      | अर्थ                  |
|-----------------|-----------------------------|---------------------|
| `rad`           | `radians`                   | समतल कोण, मूल इकाई     |
| `°`             | `degrees`                   | डिग्री                  |
| `mrad`          | `milli.radians`             | उपसर्ग-युक्त कोण (मिलिरेडियन) |
| `1 tr = 2π rad` | `(1 of turns) into radians` | रेडियन में पूर्ण चक्कर       |
| `ω = φ / t`     | `angle / time`              | कोण से कोणीय वेग         |
| `Ω = φ²`        | `angle * angle`             | दो समतल कोणों से घन कोण   |
