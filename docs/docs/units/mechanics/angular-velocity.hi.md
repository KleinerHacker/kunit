# कोणीय वेग

पैकेज: `org.pcsoft.framework.kunit.mechanic.angularvelocity`
मूल इकाई: **रेडियन प्रति सेकंड** (`KAngularVelocityUnit.BASE == KAngularVelocityUnit.RADIANS_PER_SECOND`)

प्रकार: **निर्मित इकाई**

कोणीय वेग `ω` [चाल](../kinematics/speed.md) का घूर्णी समकक्ष है: प्रति इकाई समय में बहा हुआ कोण। यह एक **निर्मित** इकाई
है — संघटन `angle · time⁻¹` (`rad/s`)।

`KAngularVelocityUnitInstance` विहित सामान्य रूप में ठीक दो पदों के एक `KMixedUnitInstance` को लपेटता है:
`KAngleUnit.BASE` (रेडियन) `+1` पर और `KTimeUnit.BASE` (सेकंड) `-1` पर। मान हमेशा rad/s में सामान्यीकृत रहता है।

## एक कोणीय वेग बनाना

इसे `angle / time` से, या किसी परंपरागत चक्कर-दर टोकन से बनाएँ। सादे संघटित वर्तनियों के जानबूझकर **कोई** अपने टोकन नहीं
हैं: `rad/s` है `radians / seconds` और `°/s` है `degrees / seconds`। उपसर्ग घटकों पर लागू होते हैं
(`kilo.radians / seconds`), इसलिए इस समूह के अपने प्रीफिक्स-बिल्डर नहीं हैं।

| इकाई         | प्रतीक     |                    टोकन |   rad/s में 1 इकाई |
|-------------|---------|-----------------------:|----------------:|
| रेडियन प्रति सेकंड | `rad/s` |    `radians / seconds` |             1.0 |
| डिग्री प्रति सेकंड   | `°/s`   |    `degrees / seconds` |           π/180 |
| चक्कर प्रति मिनट  | `rpm`   | `revolutionsPerMinute` | 2π/60 ≈ 0.10472 |
| चक्कर प्रति सेकंड  | `rps`   | `revolutionsPerSecond` |     2π ≈ 6.2832 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val w = (1 of turns) / (1 of seconds)
w into revolutionsPerMinute  // 60.0
w into (radians / seconds)   // ≈ 6.2832
```

## मूल इकाइयों (कोण और समय) के साथ गणना

| व्यंजक                         | परिणाम प्रकार                           | अर्थ                                 |
|-----------------------------|------------------------------------|------------------------------------|
| `angle / time`              | `KAngularVelocityUnitInstance`     | `ω = φ / t`                        |
| `angularvelocity * time`    | `KAngleUnitInstance`               | बहा हुआ कोण `φ = ω · t`               |
| `time * angularvelocity`    | `KAngleUnitInstance`               | वही, क्रमविनिमेय                         |
| `angle / angularvelocity`   | `KTimeUnitInstance`                | आवश्यक समय `t = φ / ω`               |
| `angularvelocity / time`    | `KAngularAccelerationUnitInstance` | [कोणीय त्वरण](angular-acceleration.md) |
| `inertia * angularvelocity` | `KAngularMomentumUnitInstance`     | [कोणीय संवेग](angular-momentum.md)     |
| `torque * angularvelocity`  | `KPowerUnitInstance`               | घूर्णी शक्ति, देखें [टॉर्क](torque.md)          |

मूल रूप भी उपलब्ध है: सामान्य इंजन के माध्यम से बना कोई भी `angle / time` व्यंजक `toAngularVelocity()`
से रूपांतरित होता है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val typed = (2 of radians) / (4 of seconds)
val native = ((2 of radians).toUnit() / (4 of seconds).toUnit()).toAngularVelocity()

typed == native // true - दोनों 0.5 rad/s हैं
```

## वास्तविक उदाहरण: स्पिंडल गति

एक मिलिंग स्पिंडल 12 000 rpm पर चलता है। टूल परिधि पर एक बिंदु प्रति सेकंड कोण के संदर्भ में कितनी दूर जाता है, और एक
चक्कर में कितना समय लगता है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val spindle = 12_000 of revolutionsPerMinute
val perSecond = spindle * (1 of seconds)   // KAngleUnitInstance
perSecond into turns                        // 200.0

val perTurn = (1 of turns) / spindle        // KTimeUnitInstance
perTurn into seconds                        // 0.005
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val sum = (1000 of revolutionsPerMinute) + (500 of revolutionsPerMinute) // 1500 rpm
(1 of revolutionsPerSecond) > (59 of revolutionsPerMinute)               // true
(60 of revolutionsPerMinute) == (1 of revolutionsPerSecond)              // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

(1 of revolutionsPerSecond).toString()                        // "6.283185307179586 rad/s"
"${(1 of revolutionsPerSecond) into revolutionsPerMinute} rpm" // "60.0 rpm"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखा जाता है।
घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित         | Kotlin                       | अर्थ                       |
|-------------|------------------------------|--------------------------|
| `rad/s`     | `radians / seconds`          | कोणीय वेग, मूल इकाई (भिन्न रूप)   |
| `rad·s⁻¹`   | `radians * (seconds pow -1)` | वही राशि एक शुद्ध गुणनफल के रूप में |
| `rpm`       | `revolutionsPerMinute`       | चक्कर प्रति मिनट (नामित टोकन)     |
| `ω = φ / t` | `angle / time`               | टाइप किया गया अपघटन          |
| `φ = ω · t` | `angularvelocity * time`     | कोण के लिए हल किया गया         |
| `t = φ / ω` | `angle / angularvelocity`    | समय के लिए हल किया गया        |
