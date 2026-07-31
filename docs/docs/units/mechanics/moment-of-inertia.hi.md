# जड़त्व आघूर्ण

पैकेज: `org.pcsoft.framework.kunit.mechanic.inertia`
मूल इकाई: **किलोग्राम मीटर वर्ग** (`KInertiaUnit.BASE == KInertiaUnit.KILOGRAM_METERS_SQUARED`)

प्रकार: **निर्मित इकाई**

जड़त्व आघूर्ण `J` [द्रव्यमान](mass.md) का घूर्णी समकक्ष है: यह बताता है कि कोई पिंड अपने घूर्णन में परिवर्तन का कितनी
दृढ़ता से प्रतिरोध करता है। यह एक **निर्मित** इकाई है — संघटन `mass · length²`
(`kg·m²`)।

`KInertiaUnitInstance` विहित सामान्य रूप में ठीक दो पदों के एक `KMixedUnitInstance` को लपेटता है:
`KMassUnit.BASE` (ग्राम) `+1` पर और `KDistanceUnit.BASE` (मीटर) `+2` पर। चूँकि पुस्तकालय का द्रव्यमान घटक ग्राम में
सामान्यीकृत है, संग्रहित मान कच्चा ग्राम-आधारित घटक मान है और kg·m² में पठन एक स्थिर गुणक से विभाजित होते हैं।

## नामित इकाइयाँ

| इकाई         | प्रतीक       |                      टोकन | kg·m² में 1 इकाई |
|-------------|-----------|-------------------------:|--------------:|
| किलोग्राम मीटर वर्ग | `kg*m^2`  |  `kilogramMetersSquared` |           1.0 |
| ग्राम सेंटीमीटर वर्ग | `g*cm^2`  | `gramCentimetersSquared` |          1e-7 |
| पाउंड-फुट वर्ग   | `lb*ft^2` |       `poundFeetSquared` |   ≈ 0.0421401 |

सभी इकाइयाँ पूर्ण SI उपसर्ग सीमा स्वीकार करती हैं (छोटे सर्वो रोटरों के लिए
`milli.kilogramMetersSquared`)।

## अपघटन

| रूप          | Kotlin                                                  | परिणाम प्रकार               |
|-------------|---------------------------------------------------------|------------------------|
| द्रव्यमान × क्षेत्रफल | `mass * area`                                           | `KInertiaUnitInstance` |
| मूल व्यंजक      | `(mass.toUnit() * (length.toUnit() pow 2)).toInertia()` | `KInertiaUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.inertia.*
import org.pcsoft.framework.kunit.mechanic.mass.grams

val typed = (2 of kilo.grams) * ((3 of meters) * (3 of meters))
val native = ((2000 of grams).toUnit() * ((3 of meters).toUnit() pow 2)).toInertia()

typed == native                     // true
typed into kilogramMetersSquared    // 18.0
```

## मूल इकाइयों के साथ गणना

| व्यंजक                             | परिणाम प्रकार                       | अर्थ                                         |
|---------------------------------|--------------------------------|--------------------------------------------|
| `mass * area`, `area * mass`    | `KInertiaUnitInstance`         | `J = m · r²`                               |
| `inertia / mass`                | `KAreaUnitInstance`            | घूर्णन त्रिज्या का वर्ग `r² = J / m`                   |
| `inertia / area`                | `KMassUnitInstance`            | `m = J / r²`                               |
| `inertia * angularvelocity`     | `KAngularMomentumUnitInstance` | [कोणीय संवेग](angular-momentum.md) `L = J · ω` |
| `inertia * angularacceleration` | `KEnergyUnitInstance`          | [टॉर्क](torque.md) `M = J · α`                |

## वास्तविक उदाहरण: प्रेस का फ्लाईव्हील

एक ठोस फ्लाईव्हील डिस्क (`J = ½ · m · r²`) का द्रव्यमान 40 kg और त्रिज्या 0.3 m है। इसका जड़त्व आघूर्ण क्या है, और 1500
rpm पर यह कौन-सा कोणीय संवेग रखता है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerMinute
import org.pcsoft.framework.kunit.mechanic.inertia.*

val r = 0.3 of meters
val j = ((40 of kilo.grams) * (r * r)) / 2  // ½ · m · r²
j into kilogramMetersSquared                // 1.8

val l = j * (1500 of revolutionsPerMinute)  // KAngularMomentumUnitInstance
l into kilogramMetersSquaredPerSecond       // ≈ 282.74
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.inertia.*

val total = (10 of kilogramMetersSquared) + (4 of kilogramMetersSquared) // 14 kg·m²
(10 of kilogramMetersSquared) > (4 of kilogramMetersSquared)            // true
(10 of kilogramMetersSquared) * (2 of kilogramMetersSquared)            // KMixedUnitInstance
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.inertia.*

(18 of kilogramMetersSquared).toString()                       // "18.0 kg*m^2" (मूल इकाई)
"${(18 of kilogramMetersSquared) into poundFeetSquared} lb*ft^2" // "427.1... lb*ft^2"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखा जाता है।
घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित          | Kotlin                          | अर्थ                        |
|--------------|---------------------------------|---------------------------|
| `kg·m²`      | `kilogramMetersSquared`         | जड़त्व आघूर्ण, मूल इकाई (नामित टोकन) |
| `kg·m^2`     | `kilo.grams * (meters pow 2)`   | वही राशि एक शुद्ध गुणनफल के रूप में  |
| `J = m · r²` | `mass * area`                   | टाइप किया गया अपघटन           |
| `r² = J / m` | `inertia / mass`                | घूर्णन त्रिज्या का वर्ग               |
| `L = J · ω`  | `inertia * angularvelocity`     | कोणीय संवेग                   |
| `M = J · α`  | `inertia * angularacceleration` | टॉर्क                        |
