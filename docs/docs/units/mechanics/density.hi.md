# घनत्व

पैकेज: `org.pcsoft.framework.kunit.density`
मूल इकाई: **किलोग्राम प्रति घन मीटर** (`KDensityUnit.BASE == KDensityUnit.KILOGRAM_PER_CUBIC_METER`)

प्रकार: **निर्मित इकाई**

घनत्व (द्रव्यमान घनत्व) एक **निर्मित** इकाई है: संघटन `mass · length⁻³` (`kg/m³`)। `KDensityUnitInstance`
दो पदों के एक `KMixedUnitInstance` को लपेटता है — `KMassUnit.BASE` (ग्राम) `+1` पर और
`KDistanceUnit.BASE` (मीटर) `-3` पर। संग्रहित मान कच्चा ग्राम-आधारित घटक मान है; kg/m³ में पठन एक स्थिर
गुणक से विभाजित होते हैं।

## एक घनत्व बनाना

क्षेत्रीय घनत्व की तरह, घनत्व का **कोई नंगा टोकन नहीं** — हर वर्तनी (kg/m³, g/cm³, …) एक अनुपात है। इसे एक
व्यंजक के रूप में या प्रकार-युक्त `mass / volume` संकारक के माध्यम से बनाएँ, और ऐसे व्यंजक के विरुद्ध
`into` से वापस पढ़ें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*

val steel = (7850 of kilo.grams) / (1 of (meters pow 3)) // KDensityUnitInstance, 7850 kg/m³
steel into (kilo.grams / (meters pow 3))   // 7850.0
steel into (kilo.grams / (centi.meters pow 3)) // 0.00785 (= 7.85 g/cm³)

val d = (6 of kilo.grams) / (2 of liters)  // 3 kg/L = 3000 kg/m³
```

## मूल इकाइयों (द्रव्यमान और आयतन) के साथ गणना

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `mass / volume` | `KDensityUnitInstance` | घनत्व = m / V |
| `density * volume` | `KMassUnitInstance` | द्रव्यमान = ρ · V |
| `volume * density` | `KMassUnitInstance` | द्रव्यमान (क्रमविनिमेय) |
| `mass / density` | `KVolumeUnitInstance` | आयतन = m / ρ |
| `density * length` | `KAreaDensityUnitInstance` | क्षेत्रीय घनत्व (क्षेत्रीय घनत्व देखें) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.density.*

val d = (2 of kilo.grams) / (1 of liters)  // 2 kg/L
val m = d * (3 of liters)                  // KMassUnitInstance
m into kilo.grams                          // 6.0
val v = (6 of kilo.grams) / d              // KVolumeUnitInstance
v into liters                              // 3.0
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*

val a = (3 of kilo.grams) / (1 of liters)
val b = (1 of kilo.grams) / (1 of liters)
(a - b) into (kilo.grams / (meters pow 3)) // 2000.0
a > b                                       // true
```

## `toString` स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.density.*

((1 of kilo.grams) / (1 of liters)).toString() // "1000.0 kg/m³" (मूल इकाई)
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे
लिखा जाता है। घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।
जहाँ किसी राशि को भिन्न और ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य
Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `kg/m³` | `kilo.grams / (meters pow 3)` | घनत्व, मूल इकाई (किलोग्राम प्रति घन मीटर) — भिन्न रूप |
| `kg·m⁻³` | `kilo.grams * (meters pow -3)` | वही घनत्व ऋणात्मक घातांक वाले गुणनफल के रूप में |
| `g/cm³` | `grams / (centi.meters pow 3)` | ग्राम प्रति घन सेंटीमीटर |
| `6 kg / 2 L` | `(6 of kilo.grams) / (2 of liters)` | द्रव्यमान ÷ आयतन से निर्माण |
