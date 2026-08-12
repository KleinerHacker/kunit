# आउटपुट स्वरूपण

यह पृष्ठ **फ़ॉर्मैटर** समूह का अवलोकन है। यह `format` क्रिया — सभी स्वरूपण का प्रवेश-बिंदु — समझाता है। दो समर्पित पृष्ठ
अधिक गहराई में जाते हैं:

- [डिफ़ॉल्ट फ़ॉर्मैटर](default-formatter.md) — साथ आने वाला `KDefaultUnitFormatter` इकाई भाग को कैसे रेंडर करता है
  (बॉक्स से बाहर मिलने वाला संकेतन), आउटपुट उदाहरणों के साथ।
- [कस्टम फ़ॉर्मैटर](custom-formatters.md) — अपनी स्वयं की प्रस्तुति (LaTeX, MathML, HTML …) कैसे जोड़ें।

हर मान `toString()` के माध्यम से स्वयं को अपनी **आधार इकाई** में प्रिंट कर सकता है, और उसे
[`into`](../mixed-units.md) से किसी विशिष्ट इकाई में **पढ़ा** जा सकता है — परंतु `into` केवल एक इकाई-चिह्न रहित शुद्ध
`Double` लौटाता है। `format` क्रिया इस अंतर को भरती है: यह `into` का प्रदर्शन-समकक्ष है और मान **तथा**
इकाई-चिह्न को एक `String` के रूप में लौटाती है।

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds

val v = 3 of meters / seconds

v format kilo.meters / hours       // "10.799999999999999 km/h"
```

`into` की तरह ही, `format` पहले मान को लक्ष्य इकाई में पढ़ता है (वही आयाम-जाँच और वही एफ़ाइन रूपांतरण करते हुए), फिर
लक्ष्य का इकाई-चिह्न जोड़ता है। चूँकि लक्ष्य उस इकाई को धारण करता है जैसा वह लिखा गया था, उपसर्ग युक्त और वैकल्पिक
इकाइयाँ समूह के आधार-चिह्न (`m`, `s`) के बजाय अपने **स्वयं के** चिह्न (`km`, `h`, `mi`) में प्रस्तुत होती हैं।

## संख्या स्वरूपण: पैटर्न और लोकेल

इनफ़िक्स रूप कच्चा `Double` प्रस्तुत करता है। **संख्यात्मक भाग** को गोल करने या स्थानीयकृत करने के लिए संख्या पैटर्न
और वैकल्पिक `KLocale` लेने वाले `format` ओवरलोड का उपयोग करें:

```kotlin
import org.pcsoft.framework.kunit.formatter.KLocale

v.format(kilo.meters / hours, "%.1f")                 // "10.8 km/h"
v.format(kilo.meters / hours, "%.1f", KLocale.DE_DE)  // "10,8 km/h"
```

पैटर्न **केवल** संख्या को प्रभावित करता है; इकाई भाग अपरिवर्तित रहता है। अमान्य पैटर्न
`IllegalArgumentException` फेंकता है, और असंगत लक्ष्य आयाम (`into` की तरह) `IllegalStateException`
फेंकता है।

### `KLocale`

Kotlin के पास कोई समान लोकेल API नहीं है — `java.util.Locale` केवल JVM पर मौजूद है — इसलिए kunit यह अपना स्वयं का
न्यूनतम विवरण रखता है कि किसी संख्या को कैसे लिखा जाए: दशमलव विभाजक, समूहन विभाजक और समूहन आकार। चूँकि ये परंपराएँ
मान के साथ ही आगे बढ़ती हैं, **एक ही पैटर्न किसी भी लक्ष्य पर वही स्ट्रिंग प्रस्तुत करता है**।

`KLocale.ROOT` (बिंदु दशमलव, अल्पविराम समूहन) डिफ़ॉल्ट है। पूर्वनिर्धारित स्थिरांक सामान्य मामलों को कवर करते हैं:
`EN_US`, `EN_GB`, `DE_DE`, `FR_FR`, `ES_ES`, `IT_IT`, `PT_BR`, `NL_NL`, `RU_RU`, `JA_JP`, `ZH_CN`, `KO_KR`,
`AR_SA` और `HI_IN` (जो भारतीय 3-then-2 समूहन को दर्शाता है)। कोई भी अन्य परंपरा सीधे `KLocale` का निर्माण करके
व्यक्त की जा सकती है।

JVM पर `java.util.Locale` अभी भी काम करता है: इसे लेने वाले ओवरलोड JVM सोर्स सेट में उपलब्ध हैं और `toKLocale()`
के माध्यम से रूपांतरित होते हैं।

```kotlin
import java.util.Locale

v.format(kilo.meters / hours, "%.1f", Locale.GERMANY) // "10,8 km/h" (केवल JVM)
```

### समर्थित पैटर्न

पैटर्न एकल संख्यात्मक मान पर लागू होने वाला printf का एक उपसमुच्चय है:

```
%[flags][width][.precision]conversion
```

| भाग        | अर्थ                                                                            |
|------------|-----------------------------------------------------------------------------------|
| flags      | `-` बाएँ-संरेखण · `+` हमेशा चिह्न · धनात्मक के लिए स्थान · `0` शून्य-भरण · `,` समूहन |
| width      | कुल वर्णों की न्यूनतम संख्या                                                      |
| precision  | दशमलव अंकों की संख्या (रूपांतरण `f`, `e`, `E`)                                    |
| conversion | `f` स्थिर-बिंदु · `e`/`E` वैज्ञानिक · `d` पूर्णांक · `s` सादी प्रस्तुति            |

`%%` एक शाब्दिक प्रतिशत चिह्न उत्सर्जित करता है, और रूपांतरण के आसपास का शाब्दिक पाठ ज्यों-का-त्यों कॉपी किया जाता है।

```kotlin
(1500 of meters).toString("%,.2f", KLocale.EN_US) // "1,500.00 m"
(1500 of meters).toString("%,.2f", KLocale.DE_DE) // "1.500,00 m"
(1500 of meters).toString("%.2e", KLocale.EN_US)  // "1.50e+03 m"
```

## भिन्न बनाम गुणनफल संकेतन

अंतर्निर्मित फ़ॉर्मैटर इकाई भाग को इस प्रकार प्रस्तुत करता है:

| पद                | प्रस्तुति                   |
|-------------------|-----------------------|
| एकल इकाई, घातांक 1    | `km`                  |
| घातांक ≠ 1           | `m^2`                 |
| एक अंश + ठीक एक हर  | `km/h`, `m/s^2`       |
| अन्यथा               | `m*s^-3*A^-2`, `s^-1` |
| कोई इकाई नहीं (विमारहित) | केवल संख्या                |

## पैटर्न के साथ `toString`

बिना तर्क वाला `toString()` अपरिवर्तित है (आधार-इकाई प्रस्तुति)। एक अतिरिक्त ओवरलोड वही संख्या पैटर्न/लोकेल आधार-इकाई
आउटपुट पर लागू करता है — यह बिना लक्ष्य वाली `format` क्रिया है:

```kotlin
(3 of meters / seconds).toString("%.2f", KLocale.EN_US) // "3.00 m/s"
(1500 of meters).toString("%.1f", KLocale.EN_US)        // "1500.0 m"
```

## वास्तविक उदाहरण

दौड़ने की गति परिवर्तित करके साफ़-सुथरे रूप में प्रिंट करें:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*
import org.pcsoft.framework.kunit.kinematic.time.*
import org.pcsoft.framework.kunit.formatter.KLocale

val distance = 10 of kilo.meters
val time = 50 of minutes
val speed = distance / time                    // KSpeedUnitInstance

println(speed.format(kilo.meters / hours, "%.1f", KLocale.EN_US)) // "12.0 km/h"
println(speed.format(meters / seconds, "%.2f", KLocale.EN_US))    // "3.33 m/s"
```

## कस्टम प्रस्तुति

इकाई भाग एक प्रतिस्थापन-योग्य [`KUnitFormatter`](custom-formatters.md) द्वारा उत्पन्न होता है; साथ आने वाला
`KDefaultUnitFormatter` ऊपर दिखाया गया सादा पाठ उत्पन्न करता है — इसके सटीक नियम और आउटपुट उदाहरण के
लिए [डिफ़ॉल्ट फ़ॉर्मैटर](default-formatter.md) देखें। पूरी तरह भिन्न संकेतन (ग्राफ़िकल सूत्र रेंडरर के लिए LaTeX या
MathML, HTML, ...) उत्पन्न करने के लिए अपना स्वयं का फ़ॉर्मैटर लागू करें और उसे स्पष्ट रूप से पास करें।
देखें [कस्टम फ़ॉर्मैटर](custom-formatters.md)।
