# त्वरण

पैकेज: `org.pcsoft.framework.kunit.acceleration`
मूल इकाई: **मीटर प्रति सेकंड वर्ग** (`KAccelerationUnit.BASE == KAccelerationUnit.METERS_PER_SECOND_SQUARED`)

प्रकार: **निर्मित इकाई**

त्वरण एक **निर्मित** इकाई है: संघटन `length · time⁻²` (`m/s²`)। `KAccelerationUnitInstance` ठीक दो पदों
के एक `KMixedUnitInstance` को लपेटता है — घातांक `+1` पर एक `KDistanceUnit.BASE` (मीटर) और घातांक `-2`
पर एक `KTimeUnit.BASE` (सेकंड)। मान सदैव m/s² में प्रसामान्यीकृत संग्रहित होता है। चूँकि मूल इकाई घटक मूल
इकाइयों (मीटर, सेकंड) से मेल खाती है, कोई अतिरिक्त मापन गुणक नहीं है।

## एक त्वरण बनाना

त्वरण सामान्यतः `speed / time` से, या किसी नामित टोकन से बनाया जाता है। जानबूझकर कोई
`metersPerSecondSquared` टोकन **नहीं** है (वह ठीक `meters / (seconds pow 2)` है)। केवल वास्तव में नामित
इकाइयाँ मान-1 टोकन के रूप में बचती हैं (`of`/`into` के साथ प्रयुक्त):

| त्वरण | प्रतीक | टोकन | 1 इकाई m/s² में |
|---|---|---:|---:|
| गैल (गैलीलियो) | `Gal` | `gals` | 0.01 (1 cm/s²) |
| मानक गुरुत्व | `g₀` | `standardGravities` | 9.80665 |

दोनों टोकन पूर्ण SI उपसर्ग तालिका का समर्थन करते हैं (जैसे `milli.gals` = 1 mGal, रोज़मर्रा की गुरुत्वमापन
इकाई)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.acceleration.*

val a = 5 of gals               // KAccelerationUnitInstance
a.value                         // 0.05 (m/s² में प्रसामान्यीकृत)
a into standardGravities        // ≈ 0.0051
(1 of milli.gals).value         // 0.00001 (1 mGal)
```

## मूल इकाइयों (चाल और समय) के साथ गणना

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `speed / time` | `KAccelerationUnitInstance` | त्वरण = Δचाल / अवधि |
| `acceleration * time` | `KSpeedUnitInstance` | चाल = त्वरण × अवधि |
| `time * acceleration` | `KSpeedUnitInstance` | चाल (क्रमविनिमेय) |
| `speed / acceleration` | `KTimeUnitInstance` | अवधि = चाल / त्वरण |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*
import org.pcsoft.framework.kunit.acceleration.*

val a = ((100 of meters) / (10 of seconds)) / (5 of seconds) // KAccelerationUnitInstance, 2 m/s²
val v = a * (3 of seconds)      // KSpeedUnitInstance, 6 m/s
val t = ((100 of meters) / (10 of seconds)) / a             // KTimeUnitInstance
t into seconds                  // 5.0
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.acceleration.*

// + / - : समान समूह, भिन्न त्वरण व्यंजकों के बीच स्वचालित रूपांतरण
val s = (10 of gals) + (4 of gals)   // 0.14 m/s²
(10 of gals) > (4 of gals)           // true
// दो त्वरणों के बीच * / / एक KMixedUnitInstance में भाग जाते हैं
(10 of gals) * (2 of gals)           // KMixedUnitInstance
```

## `toString` स्वरूपण

केवल मूल-इकाई `toString()` मौजूद है; किसी विशिष्ट इकाई को `into` के माध्यम से स्वरूपित करें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.acceleration.*

(1 of gals).toString()               // "0.01 m/s²" (मूल इकाई)
"${(1 of standardGravities) into gals} Gal" // "980.665 Gal"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे
लिखा जाता है। घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।
जहाँ किसी राशि को भिन्न और ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य
Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `m/s²` | `meters / (seconds pow 2)` | त्वरण, मूल इकाई (मीटर प्रति सेकंड वर्ग) — भिन्न रूप |
| `m·s⁻²` | `meters * (seconds pow -2)` | वही त्वरण ऋणात्मक घातांक वाले गुणनफल के रूप में |
| `Gal` | `gals` | नामित इकाई (1 cm/s²) |
| `v / t` | `speed / time` | चाल ÷ समय से निर्माण |
