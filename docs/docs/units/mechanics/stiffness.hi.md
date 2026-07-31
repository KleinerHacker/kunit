# दृढ़ता (स्प्रिंग दर)

पैकेज: `org.pcsoft.framework.kunit.mechanic.lineforce`
मूल इकाई: **न्यूटन प्रति मीटर** (`KLineForceUnit.BASE == KLineForceUnit.NEWTONS_PER_METER`)

प्रकार: **निर्मित इकाई**

दृढ़ता (स्प्रिंग दर) `k = F / s` प्रति इकाई विक्षेपण आवश्यक बल है। इसका आयाम `mass · time⁻²` (`N/m`) है —
ठीक [पृष्ठ तनाव](surface-tension.md) का आयाम। KUnit दोनों पठनों के लिए एक तटस्थ समूह, `lineforce`, मॉडल करता है; दृढ़ता
उनमें से एक है। यह पृष्ठ उस पठन का दस्तावेज़ीकरण करता है।

!!! note "एक समूह, दो पठन"
`KLineForceUnitInstance` साझा प्रकार है, इसलिए जहाँ तक KUnit का संबंध है, एक दृढ़ता और एक पृष्ठ तनाव समान इकाई हैं। समूह
का तटस्थ नाम `lineforce` है ताकि कोई भी पठन दूसरे का नाम न ले। उन्हें अपने मानों का नामकरण करके अलग करें।

## नामित इकाइयाँ

| इकाई          | प्रतीक      |                    टोकन | N/m में 1 इकाई |
|--------------|----------|-----------------------:|------------:|
| न्यूटन प्रति मीटर   | `N/m`    |      `newtonsPerMeter` |         1.0 |
| न्यूटन प्रति मिलीमीटर | `N/mm`   | `newtonsPerMillimeter` |      1000.0 |
| किलोपॉन्ड प्रति मीटर  | `kp/m`   |    `kilopondsPerMeter` |     9.80665 |
| पाउंड-बल प्रति इंच | `lbf/in` |   `poundsForcePerInch` |   ≈ 175.127 |
| डाइन प्रति सेंटीमीटर | `dyn/cm` |   `dynesPerCentimeter` |        1e-3 |

स्प्रिंग डेटा शीट N/mm में उल्लेख करती हैं; किलोन्यूटन प्रति मीटर उपसर्ग-युक्त रूप
`kilo.newtonsPerMeter` है और यह संख्यात्मक रूप से N/mm के समान है।

## मूल इकाइयों के साथ गणना

| व्यंजक                                        | परिणाम प्रकार                 | अर्थ                               |
|--------------------------------------------|--------------------------|----------------------------------|
| `force / length`                           | `KLineForceUnitInstance` | `k = F / s`                      |
| `lineforce * length`, `length * lineforce` | `KForceUnitInstance`     | स्प्रिंग बल `F = k · s`                |
| `force / lineforce`                        | `KLengthUnitInstance`    | विक्षेपण `s = F / k`                 |
| `energy / area`                            | `KLineForceUnitInstance` | [पृष्ठ तनाव](surface-tension.md) पठन |

नेटिव रूप `toLineForce()` से रूपांतरित होता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*
import org.pcsoft.framework.kunit.mechanic.mass.grams

val typed = (1 of newtons) / (1 of meters)
val native = ((1000 of grams).toUnit() / ((1 of seconds).toUnit() pow 2)).toLineForce()

typed == native            // true - दोनों 1 N/m हैं
typed into newtonsPerMeter // 1.0
```

## वास्तविक उदाहरण: सस्पेंशन में कॉइल स्प्रिंग

एक कॉइल स्प्रिंग की रेटिंग 40 N/mm है। 2000 N पहिया भार के तहत यह कितना संकुचित होता है, और 15 mm विक्षेपण किस बल को
उत्पन्न करता है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val k = 40 of newtonsPerMillimeter
k into newtonsPerMeter                 // 40000.0

val travel = (2000 of newtons) / k     // KLengthUnitInstance
travel into milli.meters               // 50.0

val force = k * (15 of milli.meters)   // KForceUnitInstance
force into newtons                     // 600.0
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.lineforce.*

// समानांतर स्प्रिंग बस जुड़ जाते हैं
val parallel = (40 of newtonsPerMillimeter) + (20 of newtonsPerMillimeter) // 60 N/mm
(40 of newtonsPerMillimeter) > (30 of kilo.newtonsPerMeter)                // true
(1 of newtonsPerMillimeter) == (1 of kilo.newtonsPerMeter)                 // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineforce.*

(40 of newtonsPerMillimeter).toString()                          // "40000.0 N/m" (मूल इकाई)
"${(40 of newtonsPerMillimeter) into newtonsPerMillimeter} N/mm" // "40.0 N/mm"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखा जाता है।
घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित         | Kotlin                          | अर्थ              |
|-------------|---------------------------------|-----------------|
| `N/m`       | `newtonsPerMeter`               | दृढ़ता, मूल इकाई     |
| `kg·s⁻²`    | `kilo.grams * (seconds pow -2)` | वही राशि आधार आयामों में |
| `N/mm`      | `newtonsPerMillimeter`          | स्प्रिंग-डेटा-शीट पठन    |
| `k = F / s` | `force / length`                | टाइप किया गया अपघटन |
| `F = k · s` | `lineforce * length`            | स्प्रिंग बल           |
| `s = F / k` | `force / lineforce`             | विक्षेपण            |

</content>
