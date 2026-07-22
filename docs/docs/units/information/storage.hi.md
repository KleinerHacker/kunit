# भंडारण

पैकेज: `org.pcsoft.framework.kunit.storage`
मूल इकाई: **बाइट** (`KStorageUnit.BASE == KStorageUnit.BYTE`)

प्रकार: **नेटिव इकाई**

भंडारण समूह एक डिजिटल डेटा मात्रा को मॉडल करता है। यह एक **सादा, एक-विमीय** समूह है (दूरी समूह की तरह कोई
घातांक-विशेषीकृत उपप्रकार नहीं, और समय समूह की तरह कोई `Duration` समर्थन नहीं): `KStorageUnitInstance`
एक एकल `KStorageUnit.BASE` (बाइट) पद को लपेटता है, सदैव बाइट में प्रसामान्यीकृत।

दो बातें इस समूह को विशेष बनाती हैं:

* **कोई ह्रासमान उपसर्ग नहीं।** एक बिट का अंश कोई सार्थक डेटा मात्रा नहीं है, इसलिए ह्रासमान SI उपसर्ग
  (`deci`, `centi`, `milli`, … — गुणक `< 1`) `bytes`/`bits` के लिए **उपलब्ध नहीं** हैं। `milli.bytes`
  लिखना एक **संकलन त्रुटि** है, रनटाइम विफलता नहीं: `bytes`/`bits` गुण केवल संवर्धक SI बिल्डर
  (`KAugmentingPrefixBuilder`) और द्विआधारी बिल्डर पर लटकते हैं, कभी ह्रासमान बिल्डर पर नहीं।
* **द्विआधारी (IEC) उपसर्ग।** दशमलव SI बिल्डरों (`kilo` = 1000) के अतिरिक्त एक दूसरा, द्विआधारी बिल्डर
  तंत्र है (`kibi` = 1024, `mebi` = 1024², …), इसलिए एक मान दशमलव चरण 1000 को द्विआधारी चरण 1024 से अलग
  कर सकता है।

## इकाइयाँ

| इकाई | Enum मान | प्रतीक | टोकन | 1 इकाई बाइट में |
|---|---|---|---:|---:|
| बाइट | `KStorageUnit.BYTE` | `B` | `bytes` | 1.0 |
| बिट | `KStorageUnit.BIT` | `bit` | `bits` | 0.125 |

एक बाइट आठ बिट है। प्रत्येक `Token` एक मान-1 `KStorageUnitInstance` है जो `of` (निर्माण) और `into` (पठन)
के साथ प्रयुक्त होता है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*

val size = 5 of bytes
size.value          // 5.0 (बाइट में प्रसामान्यीकृत)
size into bits      // 40.0 (बिट में वापस पढ़ा)
(1 of bytes) into bits   // 8.0
(8 of bits) into bytes   // 1.0
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds

// + / - : समान समूह, बिट और बाइट के बीच स्वचालित रूपांतरण
val a = (1 of bytes) + (8 of bits)   // KStorageUnitInstance: 2.0 B
val b = (4 of bytes) - (16 of bits)  // KStorageUnitInstance: 2.0 B

// तुलनाएँ
(1 of bytes) == (8 of bits)          // true (समान प्रसामान्यीकृत मात्रा)
(2 of bytes) > (1 of bytes)          // true

// storage / time एक प्रकार-युक्त डेटा दर है (डेटा दर पृष्ठ देखें)
val rate = (1000 of bytes) / (2 of seconds)  // KDataRateUnitInstance: 500 B/s
```

### तुलनाएँ और समता

`==`, `!=`, `<`, `<=`, `>`, `>=` दो `KStorageUnitInstance` मानों के प्रसामान्यीकृत `value` (बाइट) की
तुलना करते हैं। `equals` प्रसामान्यीकृत मात्रा से होता है, इसलिए `(1 of bytes) == (8 of bits)`।

## `pow` से घात

infix `pow` संकारक से किसी मान को एक पूर्णांक घात तक उठाएँ (Kotlin में कोई अतिभारयोग्य `^` नहीं)। भंडारण
समूह के लिए `pow` एक सामान्य `KMixedUnitInstance` लौटाता है (भंडारण के पास कोई विमायुक्त घात प्रकार नहीं):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.storage.*

val squared = (2 of bytes) pow 2     // KMixedUnitInstance: 4.0 B²
```

## दशमलव SI उपसर्ग

किसी भी भंडारण इकाई को **संवर्धक** (अति-एकत्व) SI उपसर्ग बिल्डरों (`deca`, `hecto`, `kilo`, `mega`,
`giga`, `tera`, `peta`, `exa`, `zetta`, `yotta`, `ronna`, `quetta`) के साथ गुण पहुँच के माध्यम से जोड़ा
जा सकता है। ह्रासमान बिल्डरों (`deci` से नीचे) में **कोई** `bytes`/`bits` गुण नहीं है, इसलिए `milli.bytes`
संकलित नहीं होता।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.*

val fiveKb = 5 of kilo.bytes         // KStorageUnitInstance (== 5000 B)
fiveKb.value                         // 5000.0

(3 of bytes) into kilo.bytes         // 0.003 (kB)

// 5 of milli.bytes                  // संकलित नहीं होता: ह्रासमान बिल्डर पर कोई `bytes` नहीं
```

## द्विआधारी (IEC) उपसर्ग

द्विआधारी उपसर्ग बिल्डर 1024 की घातें हैं और एक मान को 1000 (`kilo`) से 1024 (`kibi`) अलग करने देते हैं:
`kibi`, `mebi`, `gibi`, `tebi`, `pebi`, `exbi`, `zebi`, `yobi`।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.*

(1 of kilo.bytes).value   // 1000.0     (दशमलव)
(1 of kibi.bytes).value   // 1024.0     (द्विआधारी)
(1 of mega.bytes).value   // 1_000_000.0
(1 of mebi.bytes).value   // 1_048_576.0

val file = 4 of mebi.bytes
file into kibi.bytes      // 4096.0 (KiB)
```

| द्विआधारी बिल्डर | प्रतीक | 1 इकाई (बाइट) |
|---|---|---:|
| `kibi` | `Ki` | 1024 |
| `mebi` | `Mi` | 1024² |
| `gibi` | `Gi` | 1024³ |
| `tebi` | `Ti` | 1024⁴ |
| `pebi` | `Pi` | 1024⁵ |
| `exbi` | `Ei` | 1024⁶ |
| `zebi` | `Zi` | 1024⁷ |
| `yobi` | `Yi` | 1024⁸ |

## अन्य इकाइयों के साथ मिश्रण

किसी समय के साथ संयोजित एक भंडारण मान एक डेटा दर (`byte·second⁻¹`) बनाता है, और वापस अपघटित किया जा सकता
है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds

val rate = (1000 of bytes) / (1 of seconds)  // 1000 B/s (प्रकार-युक्त KDataRateUnitInstance)
val amount = rate * (60 of seconds)          // 60000 B (प्रकार-युक्त KStorageUnitInstance)
amount into kibi.bytes                        // ≈ 58.59 (KiB)
```

## `toString` स्वरूपण

केवल मूल-इकाई `toString()` मौजूद है; किसी विशिष्ट इकाई को `into` के माध्यम से स्वरूपित करें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*

(1024 of bytes).toString()               // "1024.0 B" (मूल इकाई निरूपण)
"${(5 of bits) into bits} bit"           // "5.0 bit"
"${(2048 of bytes) into kibi.bytes} KiB" // "2.0 KiB"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे
लिखा जाता है। घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।
जहाँ किसी राशि को भिन्न और ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य
Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `B` | `bytes` | डेटा मात्रा, मूल इकाई (बाइट) |
| `bit` | `bits` | बिट (`1 B = 8 bit`) |
| `kB` | `kilo.bytes` | दशमलव-उपसर्ग बाइट (1000 B) |
| `KiB` | `kibi.bytes` | द्विआधारी-उपसर्ग बाइट (1024 B) |
