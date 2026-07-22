# विद्युत धारा

पैकेज: `org.pcsoft.framework.kunit.ec`
मूल इकाई: **ऐम्पियर** (`KElectricCurrentUnit.BASE == KElectricCurrentUnit.AMPERE`)

प्रकार: **नेटिव इकाई**

विद्युत धारा समूह एक विद्युत धारा को मॉडल करता है। यह एक **सादा, एक-विमीय** नेटिव समूह है (कोई
घातांक-विशेषीकृत उपप्रकार नहीं, कोई पार-इकाई प्रकार-युक्त परिणाम नहीं): `KElectricCurrentUnitInstance`
एक एकल `KElectricCurrentUnit.AMPERE` पद को लपेटता है, सदैव ऐम्पियर में प्रसामान्यीकृत।

SI ऐम्पियर के अतिरिक्त, समूह दो शास्त्रीय CGS धारा इकाइयाँ प्रदान करता है: विद्युतचुंबकीय प्रणाली का
**बायो** (ऐब-ऐम्पियर) (`1 Bi = 10 A`) और विद्युतस्थैतिक प्रणाली का **स्टैट-ऐम्पियर**
(`1 statA ≈ 3.335 641 × 10⁻¹⁰ A`)।

## इकाइयाँ

| समूह | इकाई | Enum मान | प्रतीक | टोकन | 1 इकाई ऐम्पियर में |
|---|---|---|---|---:|---:|
| SI | ऐम्पियर | `KElectricCurrentUnit.AMPERE` | `A` | `amperes` | 1.0 |
| CGS | बायो / ऐब-ऐम्पियर | `KElectricCurrentUnit.BIOT` | `Bi` (`abA`) | `biot` / `abamperes` | 10 |
| CGS | स्टैट-ऐम्पियर | `KElectricCurrentUnit.STATAMPERE` | `statA` | `statamperes` | 3.335641e-10 |

प्रत्येक `Token` एक मान-1 `KElectricCurrentUnitInstance` है जो `of` (निर्माण) और `into` (पठन) के साथ
प्रयुक्त होता है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

val i = 2 of milli.amperes    // 0.002 A
i.value                       // 0.002 (ऐम्पियर में प्रसामान्यीकृत)
i into amperes                // 0.002 (ऐम्पियर में वापस पढ़ा)
(1 of biot) into amperes      // 10.0
```

## वास्तविक उदाहरण

ओम का नियम: `U = 5 V` पर `R = 220 Ω` का एक प्रतिरोधक एक धारा `I = U / R` वहन करता है। धारा इकाई के
माध्यम से व्यक्त:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

val voltage = 5.0    // V
val resistance = 220.0 // Ω
val current = (voltage / resistance) of amperes   // ≈ 0.0227 A
current into milli.amperes                         // ≈ 22.7 mA
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.ec.*

// + / - : समान समूह, इकाइयों के बीच स्वचालित रूपांतरण
val a = (1 of amperes) + (1 of biot)   // KElectricCurrentUnitInstance: 11.0 A
val b = (1 of biot) - (1 of amperes)   // KElectricCurrentUnitInstance: 9.0 A

// तुलनाएँ
(1 of biot) == (10 of amperes)         // true (समान प्रसामान्यीकृत मात्रा)
(1 of biot) > (1 of amperes)           // true
```

### तुलनाएँ और समता

`==`, `!=`, `<`, `<=`, `>`, `>=` दो `KElectricCurrentUnitInstance` मानों के प्रसामान्यीकृत `value`
(ऐम्पियर) की तुलना करते हैं। `equals` प्रसामान्यीकृत मात्रा से होता है, इसलिए
`(1 of biot) == (10 of amperes)`।

## `pow` से घात

infix `pow` संकारक से किसी मान को एक पूर्णांक घात तक उठाएँ (Kotlin में कोई अतिभारयोग्य `^` नहीं)। विद्युत
धारा समूह के लिए `pow` एक सामान्य `KMixedUnitInstance` लौटाता है (धारा के पास कोई विमायुक्त घात प्रकार
नहीं):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.ec.*

val squared = (2 of amperes) pow 2     // KMixedUnitInstance: 4.0 A²
```

## SI उपसर्ग

विद्युत धारा **कोई भी** परिमाण स्वीकारती है, इसलिए हर SI उपसर्ग बिल्डर (`quetta` … `quecto`) को हर धारा
इकाई के साथ गुण पहुँच के माध्यम से जोड़ा जा सकता है। मिलीऐम्पियर `milli.amperes` है, किलोऐम्पियर
`kilo.amperes` है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

(1 of milli.amperes).value   // 0.001      (मिलीऐम्पियर)
(1 of kilo.amperes).value    // 1000.0     (किलोऐम्पियर)

(2500 of amperes) into kilo.amperes  // 2.5
```

## `toString` स्वरूपण

केवल मूल-इकाई `toString()` मौजूद है; किसी विशिष्ट इकाई को `into` के माध्यम से स्वरूपित करें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

(1 of biot).toString()                       // "10.0 A" (मूल इकाई निरूपण)
"${(0.002 of amperes) into milli.amperes} mA" // "2.0 mA"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे
लिखा जाता है। घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `A` | `amperes` | विद्युत धारा, मूल इकाई (ऐम्पियर) |
| `mA` | `milli.amperes` | मिलीऐम्पियर (ऐम्पियर पर लागू उपसर्ग) |
| `kA` | `kilo.amperes` | किलोऐम्पियर |
| `Bi` | `biot` | बायो / ऐब-ऐम्पियर (10 A) |
| `A²` | `amperes pow 2` | ऐम्पियर वर्ग (सामान्य मिश्रित इकाई) |
