# घन कोण

पैकेज: `org.pcsoft.framework.kunit.mechanic.solidangle`
मूल इकाई: **स्टेरेडियन** (`KSolidAngleUnit.BASE == KSolidAngleUnit.STERADIAN`)

प्रकार: **निर्मित इकाई**

घन कोण द्विआयामी कोण है: गोले की सतह का वह अंश जिसे एक शंकु काटता है। यह एक **निर्मित** इकाई है —
`1 sr = 1 rad²` — लेकिन चूँकि स्टेरेडियन एक स्वतंत्र रूप से नामित SI इकाई है जिसकी अपनी शब्दावली (वर्ग डिग्री, स्पैट)
है, इसे अपने ही समूह के रूप में एकल-पद लपेटक के साथ मॉडल किया गया है।

`KSolidAngleUnitInstance` एक `KMixedUnitInstance` को लपेटता है जिसमें एकल `KSolidAngleUnit.BASE` पद घातांक 1 पर होता है,
जो हमेशा स्टेरेडियन में सामान्यीकृत रहता है। [कोण](angle.md) समूह से पुल टाइप-युक्त संकारक `angle * angle` और रूप-पहचान
हुक `toSolidAngle()` है, जो मूल `rad²` रूप को भी स्वीकार करता है।

## नामित इकाइयाँ

| इकाई        | प्रतीक    |             टोकन |            sr में 1 इकाई |
|------------|--------|----------------:|----------------------:|
| स्टेरेडियन      | `sr`   |    `steradians` |                   1.0 |
| वर्ग डिग्री      | `deg²` | `squareDegrees` | (π/180)² ≈ 3.04617e-4 |
| स्पैट (पूर्ण गोला) | `sp`   |         `spats` |          4π ≈ 12.5664 |

सभी इकाइयाँ पूर्ण SI उपसर्ग सीमा स्वीकार करती हैं (`milli.steradians`, `micro.steradians`)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val full = 1 of spats
full into steradians    // ≈ 12.566
full into squareDegrees // ≈ 41252.96 (पूरा आकाश)
```

## अपघटन

एक घन कोण दो समतुल्य तरीकों से प्राप्त किया जा सकता है; दोनों उसी विहित मान पर परिवर्तित हो जाते हैं।

| रूप             | Kotlin                                  | परिणाम प्रकार                  |
|----------------|-----------------------------------------|---------------------------|
| टाइप किया गया संकारक | `angle * angle`                         | `KSolidAngleUnitInstance` |
| मूल व्यंजक         | `(angle.toUnit() pow 2).toSolidAngle()` | `KSolidAngleUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val typed = (90 of degrees) * (90 of degrees)
val native = ((90 of degrees).toUnit() pow 2).toSolidAngle()

typed == native            // true - दोनों (π/2)² sr ≈ 2.4674 sr हैं
typed into steradians      // ≈ 2.4674
```

## समतल कोणों के साथ गणना

| व्यंजक                       | परिणाम प्रकार                  | अर्थ             |
|---------------------------|---------------------------|----------------|
| `angle * angle`           | `KSolidAngleUnitInstance` | घन कोण `Ω = φ²` |
| `solidangle / angle`      | `KAngleUnitInstance`      | शेष समतल कोण     |
| `solidangle + solidangle` | `KSolidAngleUnitInstance` | समान-प्रकार अंकगणित  |

## वास्तविक उदाहरण: LED बीम कोण

एक LED 30° × 30° के वर्गाकार बीम में प्रकाश उत्सर्जित करता है। यह कौन-सा घन कोण प्रकाशित करता है, और यह पूर्ण गोले का
कितना अंश है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val beam = (30 of degrees) * (30 of degrees)
beam into steradians    // ≈ 0.2742
beam into squareDegrees // 900.0
beam into spats         // ≈ 0.0218 (गोले का लगभग 2.2 %)
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val sum = (3 of steradians) + (1 of steradians) // 4 sr
(1 of spats) > (10 of steradians)               // true
(3 of steradians) * (2 of steradians)           // KMixedUnitInstance (समूह से भाग जाता है)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.*

(2 of steradians).toString()               // "2.0 sr" (मूल इकाई)
"${(1 of spats) into squareDegrees} deg²"  // "41252.96... deg²"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखा जाता है।
घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित           | Kotlin                                    | अर्थ                               |
|---------------|-------------------------------------------|----------------------------------|
| `sr`          | `steradians`                              | घन कोण, मूल इकाई                    |
| `deg²`        | `squareDegrees`                           | वर्ग डिग्री                            |
| `rad²`        | `(radians.toUnit() pow 2).toSolidAngle()` | वर्गित समतल कोण के रूप में घन कोण (मूल रूप) |
| `Ω = φ₁ · φ₂` | `angle * angle`                           | टाइप किया गया अपघटन                  |
| `φ = Ω / φ₁`  | `solidangle / angle`                      | समतल कोण के लिए हल किया गया            |
| `msr`         | `milli.steradians`                        | उपसर्ग-युक्त घन कोण                    |
