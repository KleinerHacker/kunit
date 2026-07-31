# कोणीय त्वरण

पैकेज: `org.pcsoft.framework.kunit.mechanic.angularacceleration`
मूल इकाई: **रेडियन प्रति सेकंड वर्ग**
(`KAngularAccelerationUnit.BASE == KAngularAccelerationUnit.RADIANS_PER_SECOND_SQUARED`)

प्रकार: **निर्मित इकाई**

कोणीय त्वरण `α` [त्वरण](../kinematics/acceleration.md) का घूर्णी समकक्ष है: प्रति इकाई समय में
[कोणीय वेग](angular-velocity.md) का परिवर्तन। यह एक **निर्मित** इकाई है — संघटन `angle · time⁻²`
(`rad/s²`)।

`KAngularAccelerationUnitInstance` विहित सामान्य रूप में ठीक दो पदों के एक `KMixedUnitInstance` को लपेटता है:
`KAngleUnit.BASE` (रेडियन) `+1` पर और `KTimeUnit.BASE` (सेकंड) `-2` पर। मान हमेशा rad/s² में सामान्यीकृत रहता है।

## नामित इकाइयाँ

| इकाई               | प्रतीक       |                             टोकन | rad/s² में 1 इकाई |
|-------------------|-----------|--------------------------------:|---------------:|
| रेडियन प्रति सेकंड वर्ग    | `rad/s^2` |       `radiansPerSecondSquared` |            1.0 |
| डिग्री प्रति सेकंड वर्ग      | `°/s^2`   |       `degreesPerSecondSquared` |          π/180 |
| चक्कर प्रति सेकंड वर्ग     | `rps^2`   |   `revolutionsPerSecondSquared` |             2π |
| चक्कर प्रति मिनट प्रति सेकंड | `rpm/s`   | `revolutionsPerMinutePerSecond` |          2π/60 |

उपसर्ग घटकों पर लागू होते हैं (`kilo.radians / (seconds pow 2)`), इसलिए इस समूह के अपने प्रीफिक्स-बिल्डर नहीं हैं।

## अपघटन

कोणीय त्वरण के दो समतुल्य अपघटन हैं; दोनों उसी विहित मान पर परिवर्तित हो जाते हैं।

| रूप             | Kotlin                                                             | परिणाम प्रकार                           |
|----------------|--------------------------------------------------------------------|------------------------------------|
| टाइप किया गया संकारक | `angularvelocity / time`                                           | `KAngularAccelerationUnitInstance` |
| मूल व्यंजक         | `(angle.toUnit() / (time.toUnit() pow 2)).toAngularAcceleration()` | `KAngularAccelerationUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val typed = (6 of radians / seconds) / (3 of seconds)
val native = ((2 of radians).toUnit() / ((1 of seconds).toUnit() pow 2)).toAngularAcceleration()

typed == native                        // true - दोनों 2 rad/s² हैं
typed into radiansPerSecondSquared     // 2.0
```

## मूल इकाइयों के साथ गणना

| व्यंजक                                     | परिणाम प्रकार                           | अर्थ                                 |
|-----------------------------------------|------------------------------------|------------------------------------|
| `angularvelocity / time`                | `KAngularAccelerationUnitInstance` | `α = ω / t`                        |
| `angularacceleration * time`            | `KAngularVelocityUnitInstance`     | प्राप्त गति `ω = α · t`                  |
| `time * angularacceleration`            | `KAngularVelocityUnitInstance`     | वही, क्रमविनिमेय                         |
| `angularvelocity / angularacceleration` | `KTimeUnitInstance`                | रन-अप समय `t = ω / α`              |
| `inertia * angularacceleration`         | `KEnergyUnitInstance`              | टॉर्क `M = J · α`, देखें [टॉर्क](torque.md) |

## वास्तविक उदाहरण: मोटर रन-अप

एक सर्वो मोटर 0.4 s में 3000 rpm तक पहुँचती है। इसका कोणीय त्वरण क्या है, और खड़े होने से शुरू होकर 0.2 s के त्वरण के
बाद यह कितना घूम चुकी है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val alpha = (3000 of revolutionsPerMinute) / (0.4 of seconds)
alpha into radiansPerSecondSquared      // ≈ 785.4
alpha into revolutionsPerMinutePerSecond // 7500.0

val afterHalf = alpha * (0.2 of seconds) // KAngularVelocityUnitInstance
afterHalf into revolutionsPerMinute      // 1500.0
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*

val sum = (10 of radiansPerSecondSquared) + (4 of radiansPerSecondSquared) // 14 rad/s²
(1 of revolutionsPerSecondSquared) > (300 of degreesPerSecondSquared)      // true
(60 of revolutionsPerMinutePerSecond) == (1 of revolutionsPerSecondSquared) // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*

(2 of radiansPerSecondSquared).toString()                          // "2.0 rad/s^2"
"${(1 of revolutionsPerSecondSquared) into radiansPerSecondSquared} rad/s^2" // "6.283... rad/s^2"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखा जाता है।
घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित         | Kotlin                                                                  | अर्थ                        |
|-------------|-------------------------------------------------------------------------|---------------------------|
| `rad/s²`    | `radiansPerSecondSquared`                                               | कोणीय त्वरण, मूल इकाई (नामित टोकन) |
| `rad·s⁻²`   | `radians * (seconds pow -2)`                                            | वही राशि एक शुद्ध गुणनफल के रूप में  |
| `rad/s²`    | `(radians.toUnit() / (seconds.toUnit() pow 2)).toAngularAcceleration()` | मूल अपघटन                  |
| `α = ω / t` | `angularvelocity / time`                                                | टाइप किया गया अपघटन           |
| `ω = α · t` | `angularacceleration * time`                                            | कोणीय वेग के लिए हल किया गया      |
| `rpm/s`     | `revolutionsPerMinutePerSecond`                                         | मशीन रन-अप दर              |
