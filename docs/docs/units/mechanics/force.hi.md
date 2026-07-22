# बल

पैकेज: `org.pcsoft.framework.kunit.force`
मूल इकाई: **न्यूटन** (`KForceUnit.BASE == KForceUnit.NEWTON`)

प्रकार: **निर्मित इकाई**

बल एक **निर्मित** इकाई है: संघटन `mass · length · time⁻²` (`kg·m/s²`)। `KForceUnitInstance` तीन पदों के
एक `KMixedUnitInstance` को लपेटता है — `KMassUnit.BASE` (ग्राम) `+1` पर, `KDistanceUnit.BASE` (मीटर)
`+1` पर, और `KTimeUnit.BASE` (सेकंड) `-2` पर। चूँकि पुस्तकालय का द्रव्यमान घटक **ग्राम** (किलोग्राम नहीं)
में प्रसामान्यीकृत है, न्यूटन कच्चे घटक आधार का 1000× है; संग्रहित मान कच्चा घटक मान है और न्यूटन में
पठन उस स्थिर गुणक से विभाजित होते हैं।

## एक बल बनाना

बल को `mass * acceleration` से, या किसी नामित टोकन से बनाएँ। नामित इकाइयाँ मान-1 टोकन के रूप में बचती हैं
(`of`/`into` के साथ प्रयुक्त):

| बल | प्रतीक | टोकन | 1 इकाई N में |
|---|---|---:|---:|
| न्यूटन | `N` | `newtons` | 1.0 |
| डाइन | `dyn` | `dynes` | 1.0e-5 |
| पाउंड-बल | `lbf` | `poundsForce` | 4.4482216152605 |
| पॉन्ड (ग्राम-बल) | `p` | `ponds` | 9.80665e-3 |

**किलोपॉन्ड / किलोग्राम-बल (kgf) कोई समर्पित टोकन नहीं है** — यह `kilo.ponds` है, ठीक वैसे ही जैसे
किलोन्यूटन `kilo.newtons` है। नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं
(`kilo.newtons`, `mega.newtons`, `kilo.ponds`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.force.*

val f = 10 of newtons
f into newtons               // 10.0
f into poundsForce           // ≈ 2.248
(1 of kilo.ponds) into newtons // 9.80665 (1 kp = 1 kgf)
```

## मूल इकाइयों (द्रव्यमान और त्वरण) के साथ गणना

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `mass * acceleration` | `KForceUnitInstance` | बल = m · a (न्यूटन का द्वितीय नियम) |
| `acceleration * mass` | `KForceUnitInstance` | बल (क्रमविनिमेय) |
| `force / mass` | `KAccelerationUnitInstance` | त्वरण = F / m |
| `force / acceleration` | `KMassUnitInstance` | द्रव्यमान = F / a |
| `force / area` | `KPressureUnitInstance` | दाब (दाब देखें) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.acceleration.*
import org.pcsoft.framework.kunit.force.*

val f = (2 of kilo.grams) * (3 of standardGravities) // KForceUnitInstance
f into newtons               // ≈ 58.84
val a = (10 of newtons) / (2 of kilo.grams)          // KAccelerationUnitInstance, 5 m/s²
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.force.*

val s = (10 of newtons) + (4 of newtons)  // 14 N
(10 of newtons) > (4 of newtons)          // true
(10 of newtons) * (2 of newtons)          // KMixedUnitInstance (समूह से भाग जाता है)
```

## `toString` स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.force.*

(10 of newtons).toString()   // "10.0 N" (मूल इकाई)
"${(1 of kilo.ponds) into newtons} N" // "9.80665 N"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे
लिखा जाता है। घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।
जहाँ किसी राशि को भिन्न और ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य
Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `N` | `newtons` | बल, मूल इकाई (नामित टोकन, न्यूटन) |
| `kg·m/s²` | `kilo.grams * meters / (seconds pow 2)` | बल द्रव्यमान·लंबाई / समय² के रूप में (भिन्न रूप) |
| `kg·m·s⁻²` | `kilo.grams * meters * (seconds pow -2)` | वही बल एक शुद्ध गुणनफल के रूप में |
| `kN` | `kilo.newtons` | उपसर्ग-युक्त बल (किलोन्यूटन) |
