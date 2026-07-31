# गतिक श्यानता (Kinematic Viscosity)

पैकेज: `org.pcsoft.framework.kunit.common.diffusivity`
मूल इकाई: **वर्ग मीटर प्रति सेकंड**
(`KDiffusivityUnit.BASE == KDiffusivityUnit.SQUARE_METER_PER_SECOND`)

प्रकार: **निर्मित इकाई**

गतिक श्यानता (kinematic viscosity) `ν = η / ρ` [श्यानता](viscosity.md) (dynamic viscosity) को
[घनत्व](density.md) से विभाजित करने पर मिलती है — वह राशि जो यह नियंत्रित करती है कि किसी तरल में संवेग कैसे विसरित होता
है। इसका आयाम `length² ·
time⁻¹` (`m²/s`) है।

यह ठीक उसी आयाम और राशि की है जो **diffusivity** समूह की है,
जो [ऊष्मीय विसरणशीलता](../thermodynamics/thermal-diffusivity.md)
(ऊष्मागतिकी) के साथ साझा की जाती है। इसलिए KUnit इसके लिए **कोई दूसरा** समूह पेश **नहीं** करता: गतिक श्यानता
`KDiffusivityUnitInstance` का एक **पठन** है, यही कारण है कि यह समूह `common` में रहता है। यह पृष्ठ यांत्रिक पठन का
दस्तावेज़ीकरण करता है।

!!! note "एक समूह, दो विषय क्षेत्र"
`KDiffusivityUnit` दोनों शब्दावलियाँ रखता है: दोनों क्षेत्रों द्वारा साझा मीट्रिक पठन (m²/s, mm²/s), और गतिक-श्यानता के
पारंपरिक वर्तनी वाले स्टोक्स और सेंटीस्टोक्स।

## नामित इकाइयाँ

| इकाई             | प्रतीक     |                          टोकन | m²/s में 1 इकाई |
|-----------------|---------|-----------------------------:|-------------:|
| वर्ग मीटर प्रति सेकंड   | `m²/s`  |      `squareMetersPerSecond` |          1.0 |
| वर्ग मिलीमीटर प्रति सेकंड | `mm²/s` | `squareMillimetersPerSecond` |         1e-6 |
| स्टोक्स              | `St`    |                     `stokes` |         1e-4 |
| सेंटीस्टोक्स            | `cSt`   |                `centistokes` |         1e-6 |
| वर्ग फुट प्रति घंटा     | `ft²/h` |          `squareFeetPerHour` | ≈ 2.58064e-5 |

`1 cSt = 1 mm²/s` ठीक-ठीक है — 20 °C पर पानी ≈ 1 cSt है। सभी इकाइयाँ पूरे SI उपसर्ग परिसर को स्वीकार करती हैं, इसलिए
`centi.stokes` सेंटीस्टोक्स की एक और वर्तनी है।

## अपघटन

| रूप                  | Kotlin                                                      | परिणाम प्रकार                   |
|---------------------|-------------------------------------------------------------|----------------------------|
| श्यानता (dynamic) / घनत्व | `viscosity / density`                                       | `KDiffusivityUnitInstance` |
| नेटिव व्यंजक             | `((length.toUnit() pow 2) / time.toUnit()).toDiffusivity()` | `KDiffusivityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.viscosity.pascalSeconds

val water = (1000 of kilo.grams) / (1 of (meters pow 3))
val typed = (1 of milli.pascalSeconds) / water
val native = (((1 of milli.meters).toUnit() pow 2) / (1 of seconds).toUnit()).toDiffusivity()

typed == native          // true - दोनों 1e-6 m²/s हैं
typed into centistokes   // 1.0
```

## मूल इकाइयों के साथ गणना

| व्यंजक                                              | परिणाम प्रकार                   | अर्थ          |
|--------------------------------------------------|----------------------------|-------------|
| `viscosity / density`                            | `KDiffusivityUnitInstance` | `ν = η / ρ` |
| `diffusivity * density`, `density * diffusivity` | `KViscosityUnitInstance`   | `η = ν · ρ` |
| `viscosity / diffusivity`                        | `KDensityUnitInstance`     | `ρ = η / ν` |

## वास्तविक उदाहरण: हाइड्रोलिक तेल चयन

एक हाइड्रोलिक तेल ISO VG 46 के रूप में निर्दिष्ट है, अर्थात 40 °C पर 46 cSt, जिसका घनत्व 870 kg/m³ है। यह किस गतिक
श्यानता के अनुरूप है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.viscosity.pascalSeconds

val nu = 46 of centistokes
nu into squareMillimetersPerSecond // 46.0

val rho = (870 of kilo.grams) / (1 of (meters pow 3))
val eta = nu * rho                 // KViscosityUnitInstance
eta into pascalSeconds             // ≈ 0.04002
eta into centi.poises              // ≈ 40.02
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.diffusivity.*

val sum = (10 of centistokes) + (4 of centistokes) // 14 cSt
(1 of stokes) > (10 of centistokes)                // true
(1 of centistokes) == (1 of squareMillimetersPerSecond) // true (समान मान)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.diffusivity.*

(46 of centistokes).toString()                  // "4.6E-5 m²/s" (मूल इकाई)
"${(46 of centistokes) into centistokes} cSt"   // "46.0 cSt"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखा जाता है।
घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित         | Kotlin                     | अर्थ                              |
|-------------|----------------------------|---------------------------------|
| `m²/s`      | `squareMetersPerSecond`    | गतिक श्यानता, मूल इकाई                 |
| `m²·s⁻¹`    | `(meters pow 2) / seconds` | वही राशि आधार आयामों में                 |
| `cSt`       | `centistokes`              | सेंटीस्टोक्स (= 1 mm²/s)                |
| `ν = η / ρ` | `viscosity / density`      | टाइप किया गया अपघटन                 |
| `η = ν · ρ` | `diffusivity * density`    | गतिक श्यानता (dynamic) के लिए हल किया गया |
| `ρ = η / ν` | `viscosity / diffusivity`  | घनत्व के लिए हल किया गया               |

</content>
