# क्षेत्रीय घनत्व

पैकेज: `org.pcsoft.framework.kunit.areadensity`
मूल इकाई: **किलोग्राम प्रति वर्ग मीटर** (`KAreaDensityUnit.BASE == KAreaDensityUnit.KILOGRAM_PER_SQUARE_METER`)

प्रकार: **निर्मित इकाई**

क्षेत्रीय घनत्व (सतही द्रव्यमान / क्षेत्रीय भार, निर्माण स्थैतिकी में सामान्य) एक **निर्मित** इकाई है:
संघटन `mass · length⁻²` (`kg/m²`)। `KAreaDensityUnitInstance` दो पदों के एक `KMixedUnitInstance` को
लपेटता है — `KMassUnit.BASE` (ग्राम) `+1` पर और `KDistanceUnit.BASE` (मीटर) `-2` पर। संग्रहित मान कच्चा
ग्राम-आधारित घटक मान है; kg/m² में पठन एक स्थिर गुणक से विभाजित होते हैं।

## एक क्षेत्रीय घनत्व बनाना

घनत्व की तरह, क्षेत्रीय घनत्व का **कोई नंगा टोकन नहीं** — हर वर्तनी (kg/m², g/mm², …) एक अनुपात है। इसे एक
व्यंजक के रूप में या प्रकार-युक्त `mass / area` संकारक के माध्यम से बनाएँ, और ऐसे व्यंजक के विरुद्ध `into`
से वापस पढ़ें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.areadensity.*

val q = (25 of kilo.grams) / ((5 of meters) * (1 of meters)) // KAreaDensityUnitInstance, 5 kg/m²
q into (kilo.grams / (meters pow 2))       // 5.0
q into (grams / (milli.meters pow 2))      // 0.005 (= प्रति mm² व्यक्त)
```

## मूल इकाइयों (द्रव्यमान, क्षेत्रफल और घनत्व) के साथ गणना

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `mass / area` | `KAreaDensityUnitInstance` | क्षेत्रीय घनत्व = m / A |
| `area density * area` | `KMassUnitInstance` | द्रव्यमान = q · A |
| `area * area density` | `KMassUnitInstance` | द्रव्यमान (क्रमविनिमेय) |
| `mass / area density` | `KAreaUnitInstance` | क्षेत्रफल = m / q |
| `density * length` | `KAreaDensityUnitInstance` | दी गई सामग्री व मोटाई की प्लेट |
| `area density / length` | `KDensityUnitInstance` | वापस आयतनी घनत्व पर |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*
import org.pcsoft.framework.kunit.areadensity.*

// 3 mm इस्पात प्लेट: घनत्व × मोटाई = सतही द्रव्यमान
val density = (2 of kilo.grams) / (1 of liters)      // 2000 kg/m³
val q = density * (3 of meters)                      // KAreaDensityUnitInstance
q into (kilo.grams / (meters pow 2))                 // 6000.0
val back = q / (3 of meters)                         // KDensityUnitInstance, 2000 kg/m³
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.areadensity.*

val area = (5 of meters) * (1 of meters)
val a = (15 of kilo.grams) / area   // 3 kg/m²
val b = (5 of kilo.grams) / area    // 1 kg/m²
(a - b) into (kilo.grams / (meters pow 2)) // 2.0
a > b                                       // true
```

## `toString` स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.areadensity.*

((5 of kilo.grams) / ((5 of meters) * (1 of meters))).toString() // "1.0 kg/m²" (मूल इकाई)
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे
लिखा जाता है। घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।
जहाँ किसी राशि को भिन्न और ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य
Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `kg/m²` | `kilo.grams / (meters pow 2)` | क्षेत्रीय घनत्व, मूल इकाई (किलोग्राम प्रति वर्ग मीटर) — भिन्न रूप |
| `kg·m⁻²` | `kilo.grams * (meters pow -2)` | वही क्षेत्रीय घनत्व ऋणात्मक घातांक वाले गुणनफल के रूप में |
| `g/mm²` | `grams / (milli.meters pow 2)` | ग्राम प्रति वर्ग मिलीमीटर |
| `25 kg / (5 m · 1 m)` | `(25 of kilo.grams) / ((5 of meters) * (1 of meters))` | द्रव्यमान ÷ क्षेत्रफल से निर्माण |
