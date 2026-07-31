# कोणीय संवेग

पैकेज: `org.pcsoft.framework.kunit.mechanic.angularmomentum`
मूल इकाई: **किलोग्राम मीटर वर्ग प्रति सेकंड**
(`KAngularMomentumUnit.BASE == KAngularMomentumUnit.KILOGRAM_METERS_SQUARED_PER_SECOND`)

प्रकार: **निर्मित इकाई**

कोणीय संवेग `L` [संवेग](momentum.md) का घूर्णी समकक्ष और घूर्णी तंत्रों की संरक्षित राशि है। यह एक **निर्मित** इकाई है —
संघटन `mass · length² · time⁻¹` (`kg·m²/s`)।

`KAngularMomentumUnitInstance` विहित सामान्य रूप में ठीक तीन पदों के एक `KMixedUnitInstance` को लपेटता है:
`KMassUnit.BASE` (ग्राम) `+1` पर, `KDistanceUnit.BASE` (मीटर) `+2` पर और `KTimeUnit.BASE` (सेकंड)
`-1` पर। रेडियन सामान्य रूप में **प्रकट नहीं** होता — यह एक विमाहीन अनुपात है।

!!! note "क्रिया (action) वही राशि है"
**क्रिया** (ऊर्जा × समय) का ठीक यही आयाम है, यही कारण है कि जूल सेकंड (`jouleSeconds`, प्लांक स्थिरांक की इकाई) *इसी*
समूह का एक टोकन है: `1 J·s = 1 kg·m²/s`।

## नामित इकाइयाँ

| इकाई                | प्रतीक        |                               टोकन | kg·m²/s में 1 इकाई |
|--------------------|------------|----------------------------------:|----------------:|
| किलोग्राम मीटर वर्ग प्रति सेकंड | `kg*m^2/s` |  `kilogramMetersSquaredPerSecond` |             1.0 |
| न्यूटन मीटर सेकंड        | `N*m*s`    |              `newtonMeterSeconds` |             1.0 |
| जूल सेकंड             | `J*s`      |                    `jouleSeconds` |             1.0 |
| ग्राम सेंटीमीटर वर्ग प्रति सेकंड | `g*cm^2/s` | `gramCentimetersSquaredPerSecond` |            1e-7 |

सभी इकाइयाँ पूर्ण SI उपसर्ग सीमा स्वीकार करती हैं (`femto.jouleSeconds`, `milli.jouleSeconds`)।

## अपघटन

कोणीय संवेग के दो समतुल्य अपघटन हैं; दोनों उसी सामान्यीकरण फैक्ट्री में जाते हैं।

| रूप               | Kotlin                                                                          | परिणाम प्रकार                       |
|------------------|---------------------------------------------------------------------------------|--------------------------------|
| जड़त्व आघूर्ण × कोणीय वेग | `inertia * angularvelocity`                                                     | `KAngularMomentumUnitInstance` |
| संवेग × लीवर भुजा     | `momentum * length`                                                             | `KAngularMomentumUnitInstance` |
| मूल व्यंजक           | `(mass.toUnit() * (length.toUnit() pow 2) / time.toUnit()).toAngularMomentum()` | `KAngularMomentumUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.radians
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.div
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.momentum.kilogramMetersPerSecond

val omega = (3 of radians) / (1 of seconds)
val viaInertia = (2 of kilogramMetersSquared) * omega
val viaMomentum = (3 of kilogramMetersPerSecond) * (2 of meters)
val viaNative =
    ((2000 of grams).toUnit() * ((3 of meters).toUnit() pow 2) / (1 of seconds).toUnit()).toAngularMomentum()

viaInertia == viaMomentum                       // true - दोनों 6 kg·m²/s हैं
viaInertia into kilogramMetersSquaredPerSecond  // 6.0
viaNative into kilogramMetersSquaredPerSecond   // 18.0
```

## मूल इकाइयों के साथ गणना

| व्यंजक                                      | परिणाम प्रकार                       | अर्थ          |
|------------------------------------------|--------------------------------|-------------|
| `inertia * angularvelocity`              | `KAngularMomentumUnitInstance` | `L = J · ω` |
| `angularvelocity * inertia`              | `KAngularMomentumUnitInstance` | वही, क्रमविनिमेय  |
| `momentum * length`, `length * momentum` | `KAngularMomentumUnitInstance` | `L = p · r` |
| `angularmomentum / inertia`              | `KAngularVelocityUnitInstance` | `ω = L / J` |
| `angularmomentum / angularvelocity`      | `KInertiaUnitInstance`         | `J = L / ω` |
| `angularmomentum / length`               | `KMomentumUnitInstance`        | `p = L / r` |
| `angularmomentum / momentum`             | `KLengthUnitInstance`          | `r = L / p` |

## वास्तविक उदाहरण: फिगर स्केटर बाहों को समेटना

एक स्केटर 4 kg·m² के जड़त्व आघूर्ण के साथ 2 rev/s पर घूमता है। बाहों को समेटने से यह घटकर 1.6 kg·m² हो जाता है। चूँकि
कोणीय संवेग संरक्षित है, नई दर `ω = L / J` से निकलती है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerSecond
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared

val l = (4 of kilogramMetersSquared) * (2 of revolutionsPerSecond)
l into kilogramMetersSquaredPerSecond // ≈ 50.27

val faster = l / (1.6 of kilogramMetersSquared) // KAngularVelocityUnitInstance
faster into revolutionsPerSecond                 // 5.0
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*

val sum = (10 of jouleSeconds) + (4 of jouleSeconds) // 14 J·s
(10 of jouleSeconds) > (4 of newtonMeterSeconds)     // true
(1 of jouleSeconds) == (1 of newtonMeterSeconds)     // true (समान आयाम)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*

(6 of kilogramMetersSquaredPerSecond).toString()             // "6.0 kg*m^2/s" (मूल इकाई)
"${(6 of kilogramMetersSquaredPerSecond) into jouleSeconds} J*s" // "6.0 J*s"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखा जाता है।
घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित         | Kotlin                                           | अर्थ                        |
|-------------|--------------------------------------------------|---------------------------|
| `kg·m²/s`   | `kilogramMetersSquaredPerSecond`                 | कोणीय संवेग, मूल इकाई (नामित टोकन) |
| `kg·m²·s⁻¹` | `kilo.grams * (meters pow 2) * (seconds pow -1)` | वही राशि एक शुद्ध गुणनफल के रूप में  |
| `J·s`       | `jouleSeconds`                                   | समान आयाम की क्रिया वर्तनी          |
| `L = J · ω` | `inertia * angularvelocity`                      | अपघटन A                   |
| `L = p · r` | `momentum * length`                              | अपघटन B                   |
| `ω = L / J` | `angularmomentum / inertia`                      | कोणीय वेग के लिए हल किया गया      |
| `r = L / p` | `angularmomentum / momentum`                     | लीवर भुजा के लिए हल किया गया      |
