# दाब

पैकेज: `org.pcsoft.framework.kunit.pressure`
मूल इकाई: **पास्कल** (`KPressureUnit.BASE == KPressureUnit.PASCAL`)

प्रकार: **निर्मित इकाई**

दाब एक **निर्मित** इकाई है: संघटन `mass · length⁻¹ · time⁻²` (`kg/(m·s²)` = `N/m²`)।
`KPressureUnitInstance` तीन पदों के एक `KMixedUnitInstance` को लपेटता है — `KMassUnit.BASE` (ग्राम)
`+1` पर, `KDistanceUnit.BASE` (मीटर) `-1` पर, और `KTimeUnit.BASE` (सेकंड) `-2` पर। बल की तरह, संग्रहित
मान कच्चा ग्राम-आधारित घटक मान है, और पास्कल में पठन एक स्थिर गुणक से विभाजित होते हैं।

## एक दाब बनाना

दाब को `force / area` से, या किसी नामित टोकन से बनाएँ। नामित इकाइयाँ मान-1 टोकन के रूप में बचती हैं
(`of`/`into` के साथ प्रयुक्त):

| दाब | प्रतीक | टोकन | 1 इकाई Pa में |
|---|---|---:|---:|
| पास्कल | `Pa` | `pascals` | 1.0 |
| बार | `bar` | `bars` | 100000.0 |
| वायुमंडल | `atm` | `atmospheres` | 101325.0 |
| पाउंड प्रति वर्ग इंच | `psi` | `psis` | 6894.757 |
| टॉर (mmHg) | `Torr` | `torrs` | 133.322 |

उपसर्ग-व्युत्पन्न वर्तनियाँ समर्पित टोकन **नहीं** हैं: **hPa** = `hecto.pascals`, **kPa** =
`kilo.pascals`, और स्थैतिकी इकाई **N/mm² = MPa** = `mega.pascals` (या व्यंजक
`newtons / (milli.meters pow 2)`)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.pressure.*

val p = 2 of bars
p into pascals               // 200000.0
p into atmospheres           // ≈ 1.974
(1 of mega.pascals) into pascals // 1000000.0 (= 1 N/mm²)
```

## मूल इकाइयों (बल और क्षेत्रफल) के साथ गणना

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `force / area` | `KPressureUnitInstance` | दाब = F / A |
| `pressure * area` | `KForceUnitInstance` | बल = p · A |
| `area * pressure` | `KForceUnitInstance` | बल (क्रमविनिमेय) |
| `force / pressure` | `KAreaUnitInstance` | क्षेत्रफल = F / p |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.force.newtons
import org.pcsoft.framework.kunit.pressure.*

val area = (2 of meters) * (1 of meters)   // KAreaUnitInstance, 2 m²
val p = (100 of newtons) / area            // KPressureUnitInstance, 50 Pa
val f = p * area                           // KForceUnitInstance, 100 N
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pressure.*

val s = (10 of pascals) + (4 of pascals)  // 14 Pa
(2 of bars) > (1 of atmospheres)          // true
(10 of pascals) * (2 of pascals)          // KMixedUnitInstance (समूह से भाग जाता है)
```

## `toString` स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pressure.*

(50 of pascals).toString()   // "50.0 Pa" (मूल इकाई)
"${(1 of bars) into pascals} Pa" // "100000.0 Pa"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे
लिखा जाता है। घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।
जहाँ किसी राशि को भिन्न और ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य
Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `Pa` | `pascals` | दाब, मूल इकाई (नामित टोकन, पास्कल) |
| `N/m²` | `newtons / (meters pow 2)` | दाब बल / क्षेत्रफल के रूप में (भिन्न रूप) |
| `kg·m⁻¹·s⁻²` | `kilo.grams * (meters pow -1) * (seconds pow -2)` | वही दाब एक शुद्ध गुणनफल के रूप में |
| `kPa` | `kilo.pascals` | उपसर्ग-युक्त दाब (किलोपास्कल) |
