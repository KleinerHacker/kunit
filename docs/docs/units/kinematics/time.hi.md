# समय

पैकेज: `org.pcsoft.framework.kunit.time`
मूल इकाई: **सेकंड** (`KTimeUnit.BASE == KTimeUnit.SECOND`)

प्रकार: **नेटिव इकाई**

`KTimeUnitInstance` [`java.time.Duration`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/Duration.html)
के चारों ओर 100 % रैपर है: `Duration` सत्य का एकमात्र स्रोत है (नैनोसेकंड-सटीक), और पूरा `Duration` API
अग्रेषित होता है। इसके ऊपर यह हर अन्य «शुद्ध» इकाई रैपर के समान सतह प्रदान करता है
(`value`/`+`/`-`/`*`/`/`/`toString`/`toUnit` तथा `of`/`into` क्रियाएँ), इसलिए एक समय मान सामान्य
मिश्रित-इकाई इंजन में जुड़ता है (जैसे `length / time` = चाल)। मान सदैव सेकंड में प्रसामान्यीकृत संग्रहित
होता है।

चूँकि एक `Duration` केवल एक सादी अवधि दर्शाता है, एक समय मान सदैव घातांक 1 होता है — कोई समय² या 1/समय
रैपर नहीं (गुणन/भाग एक कच्चे `KMixedUnitInstance` में «भाग जाता» है, बिलकुल लंबाई की तरह)। परिणामस्वरूप
`KMixedUnitInstance.toTime()` केवल **घातांक 1 पर** एक `KTimeUnit` पद स्वीकारता है।

## इकाइयाँ

| इकाई | Enum मान | प्रतीक | टोकन | 1 इकाई सेकंड में |
|---|---|---|---:|---:|
| सेकंड | `KTimeUnit.SECOND` | `s` | `seconds` | 1.0 |
| मिनट | `KTimeUnit.MINUTE` | `min` | `minutes` | 60.0 |
| घंटा | `KTimeUnit.HOUR` | `h` | `hours` | 3600.0 |
| दिन | `KTimeUnit.DAY` | `d` | `days` | 86 400.0 |

केवल भौतिक समय-मापों को मॉडल किया गया है; कैलेंडर-आधारित इकाइयाँ (सप्ताह, वर्ष) जानबूझकर छोड़ी गई हैं,
क्योंकि वे किसी स्थिर भौतिक राशि के बजाय कैलेंडरों द्वारा परिभाषित होती हैं। प्रत्येक `Token` एक मान-1
`KTimeUnitInstance` है जो `of` (निर्माण) और `into` (पठन) के साथ प्रयुक्त होता है।

उप-सेकंड माप (मिलीसेकंड, माइक्रोसेकंड, नैनोसेकंड, ...) समर्पित इकाइयाँ **नहीं** हैं — उन तक `seconds` पर
SI उपसर्ग बिल्डरों के माध्यम से सामान्य रूप से पहुँचा जाता है (नीचे [SI उपसर्ग](#si) देखें)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.time.*

val t = 2 of hours
t.value          // 7200.0 (सेकंड में प्रसामान्यीकृत)
t into hours     // 2.0 (घंटों में वापस पढ़ा)
t into minutes   // 120.0
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.*

// + / - : समान समूह, भिन्न समय इकाइयों के बीच स्वचालित रूपांतरण (सटीक Duration अंकगणित)
val a = (1 of hours) + (30 of minutes)   // KTimeUnitInstance, सेकंड में प्रसामान्यीकृत (5400.0)
val b = (2 of hours) - (30 of minutes)

// तुलनाएँ
(2 of hours) > (90 of minutes)           // true
(1 of hours) == (60 of minutes)          // true (समान प्रसामान्यीकृत मान)

// * / / : सदैव अनुमत, एक नए घातांक के साथ KMixedUnitInstance उत्पन्न करता है
val secondsSquared = (3 of seconds) * (4 of seconds)   // KMixedUnitInstance: value=12.0, units=[SECOND^2]
val ratio = (10 of seconds) / (2 of seconds)           // KMixedUnitInstance: value=5.0, विमाहीन
```

## तुलनाएँ और समता

`==`, `!=`, `<`, `<=`, `>`, `>=` दो `KTimeUnitInstance` की उनके अंतर्निहित `Duration` (नैनोसेकंड-सटीक) से
तुलना करते हैं। चूँकि एक समय मान सदैव घातांक 1 होता है, वहाँ घातांक-बेमेल त्रुटि नहीं होती जैसी लंबाई
क्षेत्रफल/आयतन के लिए होती है।

## `java.time.Duration` रैपर

`KTimeUnitInstance`, `Duration` के ऊपर एक ड्रॉप-इन मुखौटा है: लिपटा हुआ `Duration` प्राप्त करें, किसी
मौजूदा को लपेटें, और अग्रेषित `Duration` विधियों का सीधे उपयोग करें (जो `Duration` लौटाती हैं वे
`KTimeUnitInstance` लौटाती हैं; क्वेरी विधियाँ पास-थ्रू होती हैं)।

```kotlin
import java.time.Duration
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.time.*

val t = 90 of minutes
t.toDuration()                  // PT1H30M
Duration.ofMinutes(90).toTime() into hours // 1.5

// अग्रेषित परिवर्तक KTimeUnitInstance लौटाते हैं
t.plusHours(1) into hours       // 2.5
t.negated().isNegative()        // true

// अग्रेषित क्वेरी विधियाँ पास-थ्रू होती हैं
t.toHours()             // 1
t.toMinutesPart()       // 30
t.dividedBy(30 of minutes) // 3
```

## SI उपसर्ग

किसी भी समय इकाई को 24 SI उपसर्ग **बिल्डरों** (`kilo`, `milli`, `micro`, …; रूट पैकेज) में से किसी के
साथ गुण पहुँच के माध्यम से जोड़ा जा सकता है, जो `of`/`into` के लिए एक मान-1 टेम्पलेट उत्पन्न करता है। इसी
प्रकार उप-सेकंड माप व्यक्त होते हैं। ध्यान दें कि `Duration` समर्थन प्रस्तुत करने योग्य परास को सीमित करता
है (नीचे नोट देखें), इसलिए बहु-सेकंड आधार पर चरम उपसर्ग प्रस्तुत करने योग्य नहीं हैं:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.time.*

// निर्माण: "5 of milli.seconds" -> KTimeUnitInstance
val fiveMillis = 5 of milli.seconds
fiveMillis.value // 0.005 (सेकंड)

// किसी उपसर्ग-युक्त इकाई में मान वापस पढ़ना
val t = 2 of hours
t into milli.seconds  // 7 200 000.0 (ms)
```

!!! note "Duration परास"
    चूँकि मान `java.time.Duration` (पूर्ण सेकंड `Long` के रूप में संग्रहित, नैनोसेकंड विभेदन) द्वारा
    समर्थित है, एक `KTimeUnitInstance` केवल लगभग `[1 ns, Long.MAX seconds]` (≈ 292 अरब वर्ष) के भीतर के
    परिमाणों को विश्वसनीय रूप से प्रस्तुत कर सकता है। दिनों पर लागू `quetta` जैसे चरम उपसर्ग इस परास से
    अधिक होते हैं, और उप-नैनोसेकंड मान शून्य पर पूर्णांकित होते हैं। सामान्य `KMixedUnitInstance`/उपसर्ग
    परत स्वयं `Double`-आधारित और अप्रभावित है — केवल Duration-समर्थित रैपर में रूपांतरण परास-सीमित है।

## `toString` स्वरूपण

केवल मूल-इकाई `toString()` मौजूद है; किसी विशिष्ट इकाई को `into` के माध्यम से स्वरूपित करें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.time.*

(2 of hours).toString()          // "7200.0 s" (मूल इकाई निरूपण)
"${(2 of hours) into hours} h"   // "2.0 h"
"${(2 of hours) into minutes} min" // "120.0 min"
```

## अन्य इकाइयों के साथ मिश्रण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

val speed = (10 of meters) / (1 of seconds)  // KSpeedUnitInstance
speed into (kilo.meters / hours)             // 36.0 (km/h)

// चाल को पुनः किसी समय से गुणा करने पर एक शुद्ध लंबाई पुनः प्राप्त होती है
val distance = speed * (2 of seconds)
distance into meters // 20.0
```

किसी समर्पित पार-समूह संकारक **बिना** दो समूहों की दो शुद्ध इकाइयाँ (जैसे `(2 of hours) * (5 of bytes)`)
सीधे एक `KMixedUnitInstance` में संयोजित होती हैं, बिना किसी `.toUnit()` की आवश्यकता के।

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे
लिखा जाता है। घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।
जहाँ किसी राशि को भिन्न और ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य
Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `s` | `seconds` | समय, मूल इकाई (सेकंड) |
| `min` | `minutes` | मिनट |
| `h` | `hours` | घंटा |
| `ms` | `milli.seconds` | उपसर्ग-युक्त समय (मिलीसेकंड) |
| `s⁻¹` | `seconds pow -1` | व्युत्क्रम समय (मिश्रित इकाई में भाग जाता है) |
