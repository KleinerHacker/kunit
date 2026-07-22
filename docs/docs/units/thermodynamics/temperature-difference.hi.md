# तापमान अंतर

पैकेज: `org.pcsoft.framework.kunit.temperature`
मूल इकाई: **केल्विन** (`KTemperatureDifferenceUnit.BASE == KTemperatureDifferenceUnit.KELVIN`)

प्रकार: **नेटिव इकाई**

एक तापमान *अंतर* दो तापमानों के बीच का अंतराल है — एक **रैखिक** राशि, ऐफ़ाइन, परम
[तापमान](temperature.md) समूह के विपरीत। यह **कोई ऑफ़सेट नहीं** वहन करता (केवल केल्विन का मापन), इसलिए यह
एक साधारण इकाई समूह की तरह व्यवहार करता है और सामान्य इंजन से अपरिवर्तित चलता है।

भौतिक रूप से यही कारण है कि दो परम तापमानों को घटाने पर केल्विन मिलता है, तापमान नहीं:
`30 °C − 10 °C = 20 ΔK`, `20 °C` नहीं। `20 ΔK` का अंतर वैसे भी संख्यात्मक रूप से `20 °C` के अंतर के
बराबर है (समान चरण-आकार), इसलिए समूह जानबूझकर **केवल केल्विन** और **कोई उपसर्ग नहीं** प्रदान करता है।

## इकाइयाँ

| इकाई | Enum मान | प्रतीक | केल्विन से/तक |
|---|---|---|---|
| केल्विन | `KTemperatureDifferenceUnit.KELVIN` | `ΔK` | तत्समक |

!!! note "प्रतीक `ΔK`, `K` नहीं"
    एक तापमान अंतर प्रतीक **`ΔK`** (जैसे `"20.0 ΔK"`) के साथ छापा जाता है, जानबूझकर एक परम केल्विन (`K`)
    से भिन्न। दोनों समान *विमा* (केल्विन) हैं पर भिन्न राशियाँ — एक ऐफ़ाइन बिंदु बनाम एक रैखिक अंतराल। किसी
    [मिश्रित इकाई](../../mixed-units.md) में `m·K` (परम) और `m·ΔK` (अंतर) इसलिए समान इकाई **नहीं** हैं और
    न बराबर हैं न योग्य; विशिष्ट प्रतीक इसे एक नज़र में दृश्यमान बनाता है।

## निर्माण

एक अंतर सामान्य `of` क्रिया से नहीं बनाया जाता (जो परम राशियों के लिए आरक्षित है)। यह या तो **दो परम
तापमानों को घटाकर** या **स्पष्ट रूप से** `KTemperatureDifference.ofKelvin(…)` फ़ैक्टरी के माध्यम से
उत्पन्न होता है — "यह एक अंतराल है" आशय को स्पष्ट करते हुए:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

val d1 = (30 of celsius) - (10 of celsius)   // KTemperatureDifferenceUnitInstance: 20 ΔK
val d2 = KTemperatureDifference.ofKelvin(20) // स्पष्ट, d1 के बराबर
d1.value                                      // 20.0 (केल्विन)
```

## संकारक

`+`/`-`/तुलना साधारण रैखिक समान-प्रकार संकारक हैं (एक अंतर जमा एक अंतर एक अंतर है):

```kotlin
import org.pcsoft.framework.kunit.temperature.*

val sum  = KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10) // 30 ΔK
val diff = KTemperatureDifference.ofKelvin(20) - KTemperatureDifference.ofKelvin(10) // 10 ΔK

KTemperatureDifference.ofKelvin(20) > KTemperatureDifference.ofKelvin(10) // true
```

रैखिक होने के कारण, एक अंतर को **सादे संख्या से भी मापा** जा सकता है (परम तापमान के विपरीत), अपना प्रकार
बनाए रखते हुए:

```kotlin
import org.pcsoft.framework.kunit.times

val doubled = KTemperatureDifference.ofKelvin(5) * 2 // KTemperatureDifferenceUnitInstance: 10 ΔK
```

एक अंतर को एक परम तापमान में जोड़ा या उससे घटाया जा सकता है ताकि फिर एक परम तापमान मिले (देखें
[तापमान](temperature.md)):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

(25 of celsius) + KTemperatureDifference.ofKelvin(5) // KTemperatureUnitInstance: 303.15 K
```

## अन्य इकाइयों के साथ मिश्रण

किसी अंतर को किसी अन्य समूह से गुणा या भाग करने पर एक सामान्य `KMixedUnitInstance` मिलता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.times
import org.pcsoft.framework.kunit.temperature.*

KTemperatureDifference.ofKelvin(2) * (3 of bytes) // KMixedUnitInstance
```

## `toString` स्वरूपण

केवल मूल-इकाई `toString()` मौजूद है (केल्विन):

```kotlin
import org.pcsoft.framework.kunit.temperature.*

KTemperatureDifference.ofKelvin(20).toString() // "20.0 ΔK"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे
लिखा जाता है। घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।
एक अंतर केवल केल्विन मापन वहन करता है (कोई ऑफ़सेट नहीं) और स्पष्ट रूप से बनाया जाता है, कभी सामान्य `of` से
नहीं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `ΔK` | `KTemperatureDifference.ofKelvin(20)` | तापमान अंतराल, मूल इकाई (केल्विन) |
| `30 °C − 10 °C` | `(30 of celsius) - (10 of celsius)` | दो परम तापमानों से अंतर |
| `20 ΔK + 10 ΔK` | `KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10)` | दो अंतरों का योग |
