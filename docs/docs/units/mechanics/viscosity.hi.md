# श्यानता (Dynamic Viscosity)

पैकेज: `org.pcsoft.framework.kunit.mechanic.viscosity`
मूल इकाई: **पास्कल सेकंड** (`KViscosityUnit.BASE == KViscosityUnit.PASCAL_SECOND`)

प्रकार: **निर्मित इकाई**

श्यानता `η` किसी तरल के कतरनी (shear) प्रतिरोध का वर्णन करती है। यह एक **निर्मित** इकाई है — संघटन
`pressure · time`, अर्थात `mass · length⁻¹ · time⁻¹` (`Pa·s`)।

`KViscosityUnitInstance` एक `KMixedUnitInstance` को लपेटता है जिसमें कैनोनिकल सामान्य रूप में ठीक तीन पद होते हैं:
`KMassUnit.BASE` (ग्राम) घातांक `+1` पर, `KDistanceUnit.BASE` (मीटर) घातांक `-1` पर और
`KTimeUnit.BASE` (सेकंड) घातांक `-1` पर। चूँकि इस लाइब्रेरी का द्रव्यमान घटक ग्राम में सामान्यीकृत है, इसलिए संचित मान
कच्चा ग्राम-आधारित घटक मान है और Pa·s में पठन एक स्थिर गुणांक से विभाजित होते हैं।

!!! note "श्यानता (dynamic) बनाम गतिक श्यानता (kinematic)"
**गतिक श्यानता** `ν = η / ρ` (`m²/s`) एक भिन्न राशि है और diffusivity समूह में रहती है — देखें
[गतिक श्यानता](kinematic-viscosity.md)।

## नामित इकाइयाँ

| इकाई                 | प्रतीक          |                              टोकन | Pa·s में 1 इकाई |
|---------------------|--------------|---------------------------------:|-------------:|
| पास्कल सेकंड             | `Pa*s`       |                  `pascalSeconds` |          1.0 |
| पॉइज़                 | `P`          |                         `poises` |          0.1 |
| पाउंड-बल सेकंड प्रति वर्ग फुट | `lbf*s/ft^2` | `poundForceSecondsPerSquareFoot` |    ≈ 47.8803 |
| रेयन (lbf·s/in²)     | `reyn`       |                          `reyns` |   ≈ 6894.757 |

पानी जैसे तरल पदार्थों के लिए दो रोज़मर्रा की वर्तनी उपसर्ग-युक्त रूप हैं, अपने स्वयं के टोकन नहीं:
**मिलीपास्कल सेकंड** `milli.pascalSeconds` है और **सेंटीपॉइज़** `centi.poises` है — और वे बराबर हैं (`1 mPa·s = 1 cP`,
20 °C पर पानी)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.viscosity.*

val water = 1 of milli.pascalSeconds
water into centi.poises  // 1.0
water into pascalSeconds // 0.001
(1 of poises) into pascalSeconds // 0.1
```

## मूल इकाइयों (दाब और समय) के साथ गणना

| व्यंजक                                  | परिणाम प्रकार                   | अर्थ                  |
|--------------------------------------|----------------------------|---------------------|
| `pressure * time`, `time * pressure` | `KViscosityUnitInstance`   | `η = p · t`         |
| `viscosity / pressure`               | `KTimeUnitInstance`        | `t = η / p`         |
| `viscosity / time`                   | `KPressureUnitInstance`    | `p = η / t`         |
| `viscosity / density`                | `KDiffusivityUnitInstance` | गतिक श्यानता `ν = η / ρ` |
| `viscosity / diffusivity`            | `KDensityUnitInstance`     | `ρ = η / ν`         |

नेटिव रूप `toViscosity()` से रूपांतरित होता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.viscosity.*

val typed = (2 of pascals) * (3 of seconds)
val native = ((2 of pascals).toUnit() * (3 of seconds).toUnit()).toViscosity()

typed == native            // true - दोनों 6 Pa·s हैं
typed into pascalSeconds   // 6.0
```

## वास्तविक उदाहरण: परिचालन तापमान पर इंजन तेल

एक SAE 30 तेल 100 °C पर 9.3 cP मापा जाता है, जिसका घनत्व 850 kg/m³ है। यह Pa·s में क्या है, और यह किस गतिक श्यानता के
अनुरूप है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.diffusivity.centistokes
import org.pcsoft.framework.kunit.common.diffusivity.div
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.viscosity.*
import org.pcsoft.framework.kunit.pow

val oil = 9.3 of centi.poises
oil into pascalSeconds        // 0.0093

val rho = (850 of kilo.grams) / (1 of (meters pow 3))
val nu = oil / rho            // KDiffusivityUnitInstance
nu into centistokes           // ≈ 10.94
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.viscosity.*

val sum = (10 of pascalSeconds) + (4 of pascalSeconds) // 14 Pa·s
(1 of poises) > (1 of milli.pascalSeconds)             // true
(1 of poises) == (100 of milli.pascalSeconds)          // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.mechanic.viscosity.*

(2 of pascalSeconds).toString()                    // "2.0 Pa*s" (मूल इकाई)
"${(2 of pascalSeconds) into centi.poises} cP"     // "2000.0 cP"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखा जाता है।
घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित          | Kotlin                                            | अर्थ                    |
|--------------|---------------------------------------------------|-----------------------|
| `Pa·s`       | `pascalSeconds`                                   | श्यानता, मूल इकाई (नामित टोकन) |
| `kg·m⁻¹·s⁻¹` | `kilo.grams * (meters pow -1) * (seconds pow -1)` | वही राशि शुद्ध गुणनफल के रूप में |
| `cP`         | `centi.poises`                                    | सेंटीपॉइज़ (= 1 mPa·s)     |
| `η = p · t`  | `pressure * time`                                 | टाइप किया गया अपघटन       |
| `ν = η / ρ`  | `viscosity / density`                             | गतिक श्यानता               |
| `mPa·s`      | `milli.pascalSeconds`                             | उपसर्ग-युक्त श्यानता           |

</content>
