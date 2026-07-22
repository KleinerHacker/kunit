# परम तापमान

> **तापमान** विषय का भाग — [अवलोकन](temperature-overview.md) और रैखिक समकक्ष
> [तापमान अंतर](temperature-difference.md) देखें।

पैकेज: `org.pcsoft.framework.kunit.temperature`
मूल इकाई: **केल्विन** (`KTemperatureUnit.BASE == KTemperatureUnit.KELVIN`)

प्रकार: **नेटिव इकाई**

तापमान समूह एक ऊष्मागतिक तापमान को मॉडल करता है। यह फ़्रेमवर्क का **पहला (और, डिज़ाइन द्वारा, स्थायी)
ऐफ़ाइन अपवाद** है: हर अन्य समूह के विपरीत, तापमान इकाइयों के बीच रूपांतरण कोई एकल गुणक नहीं बल्कि एक
**ऑफ़सेट-और-मापन** (ऐफ़ाइन) परिवर्तन है — `25 °C`, `25 × 1 °C` *नहीं* है। मान **परम केल्विन** में
प्रसामान्यीकृत संग्रहित होते हैं, इसलिए `*`/`/`/`pow` सामान्य इंजन से अपरिवर्तित चलते रहते हैं।

दो बातें इस समूह को विशेष बनाती हैं:

* **हुक के माध्यम से ऐफ़ाइन रूपांतरण, अतिभार के माध्यम से नहीं।** साझा इंजन शुद्ध रूप से गुणात्मक रहता
  है। ऐफ़ाइन परिवर्तन दो मापन-योग्य हुक `scaledBy` (निर्माण, `of` के पीछे) और `readBaseValue` (पठन, `into`
  के पीछे) के माध्यम से इंजेक्ट होता है, इसलिए `25 of celsius` और `t into fahrenheit` सामान्य क्रियाओं के
  माध्यम से काम करते हैं — `of`/`into` के लिए कोई समूह-विशिष्ट अतिभार नहीं (जिसे एक स्पष्ट रूप से आयातित
  सामान्य क्रिया ढँक देती)।
* **कोई उपसर्ग नहीं।** तापमान समूह जानबूझकर **कोई** उपसर्ग बिल्डर प्रदान नहीं करता (`milli.celsius` मॉडल
  नहीं किया गया)। कोई `KTemperatureUnitExtensions.kt` नहीं है।

## इकाइयाँ

| इकाई | Enum मान | प्रतीक | टोकन | केल्विन से/तक |
|---|---|---|---:|---|
| केल्विन | `KTemperatureUnit.KELVIN` | `K` | `kelvin` | तत्समक |
| डिग्री सेल्सियस | `KTemperatureUnit.CELSIUS` | `°C` | `celsius` | `K = °C + 273.15` |
| डिग्री फ़ारेनहाइट | `KTemperatureUnit.FAHRENHEIT` | `°F` | `fahrenheit` | `K = (°F − 32)·5/9 + 273.15` |
| डिग्री रैंकिन | `KTemperatureUnit.RANKINE` | `°R` | `rankine` | `K = °R·5/9` |

प्रत्येक `Token` एक मान-1 `KTemperatureUnitInstance` है जो `of` (निर्माण) और `into` (पठन) के साथ प्रयुक्त
होता है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.temperature.*

val t = 25 of celsius
t.value             // 298.15 (परम केल्विन में प्रसामान्यीकृत)
t into fahrenheit   // 77.0
t into kelvin       // 298.15

(0 of celsius) into kelvin       // 273.15
(100 of celsius) into fahrenheit // 212.0
(32 of fahrenheit) into celsius  // 0.0
(-40 of celsius) into fahrenheit // -40.0 (C/F प्रतिच्छेदन)
```

## संकारक

एक परम तापमान एक ऐफ़ाइन **बिंदु** है, सदिश नहीं। इसलिए इसका अंकगणित जानबूझकर असममित है — भौतिक रूप से सही
व्यवहार ([तापमान अंतर](temperature-difference.md) भी देखें):

* `परम − परम` ← एक **`KTemperatureDifferenceUnitInstance`** (उनके बीच केल्विन *अंतराल*, जैसे
  `30 °C − 10 °C = 20 ΔK`, **न कि** `20 °C`)।
* `परम ± अंतर` ← फिर एक परम तापमान।
* `परम + परम` ← **संकलन त्रुटि** (दो परम तापमानों को जोड़ना भौतिक रूप से निरर्थक है)।
* `परम * संख्या` / `परम / संख्या` ← **संकलन त्रुटि**: किसी ऐफ़ाइन बिंदु को सादे संख्या से मापना निरर्थक है
  (इसका केल्विन मान −273.15 ऑफ़सेट वहन करता है)। इसके बजाय एक [तापमान अंतर](temperature-difference.md)
  मापें, जो रैखिक है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

// परम − परम = तापमान अंतर (केल्विन में)
val d = (30 of celsius) - (10 of celsius)          // KTemperatureDifferenceUnitInstance: 20 ΔK
d.value                                             // 20.0

// परम ± अंतर = परम तापमान
val a = (25 of celsius) + KTemperatureDifference.ofKelvin(5)   // KTemperatureUnitInstance: 303.15 K
val b = (25 of celsius) - KTemperatureDifference.ofKelvin(5)   // KTemperatureUnitInstance: 293.15 K

// (30 of celsius) + (10 of celsius)               // संकलित नहीं होता

// तुलनाएँ (परम केल्विन से)
(0 of celsius) == (273.15 of kelvin)      // true (समान परम तापमान)
(100 of celsius) > (100 of fahrenheit)    // true
```

### तुलनाएँ और समता

`==`, `!=`, `<`, `<=`, `>`, `>=` प्रसामान्यीकृत परम केल्विन `value` की तुलना करते हैं। `equals` परम
तापमान से होता है, निर्माण इकाई से स्वतंत्र, इसलिए `(0 of celsius) == (273.15 of kelvin)`।

## `pow` से घात

infix `pow` संकारक से किसी मान को एक पूर्णांक घात तक उठाएँ। तापमान समूह के लिए `pow` एक सामान्य
`KMixedUnitInstance` लौटाता है (तापमान के पास कोई विमायुक्त घात प्रकार नहीं), जो परम केल्विन पद पर रैखिक
रूप से कार्य करता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.temperature.*

val squared = (2 of kelvin) pow 2   // KMixedUnitInstance: 4.0 K²
```

## अन्य इकाइयों के साथ मिश्रण

किसी तापमान को किसी अन्य समूह से गुणा या भाग करने पर एक सामान्य `KMixedUnitInstance` मिलता है (कोई मानक
तापमान संयोजन नहीं), जो परम केल्विन मान पर गणना किया जाता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*
import org.pcsoft.framework.kunit.time.seconds

val rate = (2 of kelvin) / (1 of seconds)   // KMixedUnitInstance: 2.0 K·s⁻¹
```

## `toString` स्वरूपण

केवल मूल-इकाई `toString()` मौजूद है; किसी विशिष्ट इकाई को `into` के माध्यम से स्वरूपित करें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.temperature.*

(25 of celsius).toString()               // "298.15 K" (मूल इकाई निरूपण)
"${(25 of celsius) into fahrenheit} °F"  // "77.0 °F"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे
लिखा जाता है। घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।
तापमान ऐफ़ाइन है, इसलिए कोई `·`/घातांक गुणनफल रूप नहीं है — केवल नामित इकाइयाँ और ऑफ़सेट परिवर्तन।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `K` | `kelvin` | परम तापमान, मूल इकाई (केल्विन) |
| `°C` | `celsius` | डिग्री सेल्सियस (`K = °C + 273.15`) |
| `°F` | `fahrenheit` | डिग्री फ़ारेनहाइट |
| `25 °C` | `25 of celsius` | एक परम तापमान बनाना |
