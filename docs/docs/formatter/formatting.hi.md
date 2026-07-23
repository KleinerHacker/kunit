# आउटपुट स्वरूपण

यह पृष्ठ **फ़ॉर्मैटर** समूह का अवलोकन है। यह `format` क्रिया — सभी स्वरूपण का प्रवेश-बिंदु — समझाता है। दो
समर्पित पृष्ठ अधिक गहराई में जाते हैं:

- [डिफ़ॉल्ट फ़ॉर्मैटर](default-formatter.md) — साथ आने वाला `KDefaultUnitFormatter` इकाई भाग को कैसे रेंडर
  करता है (बॉक्स से बाहर मिलने वाला संकेतन), आउटपुट उदाहरणों के साथ।
- [कस्टम फ़ॉर्मैटर](custom-formatters.md) — अपनी स्वयं की प्रस्तुति (LaTeX, MathML, HTML …) कैसे जोड़ें।

हर मान `toString()` के माध्यम से स्वयं को अपनी **आधार इकाई** में प्रिंट कर सकता है, और उसे
[`into`](../mixed-units.md) से किसी विशिष्ट इकाई में **पढ़ा** जा सकता है — परंतु `into` केवल एक इकाई-चिह्न रहित
शुद्ध `Double` लौटाता है। `format` क्रिया इस अंतर को भरती है: यह `into` का प्रदर्शन-समकक्ष है और मान **तथा**
इकाई-चिह्न को एक `String` के रूप में लौटाती है।

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds

val v = 3 of meters / seconds

v format kilo.meters / hours       // "10.799999999999999 km/h"
```

`into` की तरह ही, `format` पहले मान को लक्ष्य इकाई में पढ़ता है (वही आयाम-जाँच और वही एफ़ाइन रूपांतरण करते हुए),
फिर लक्ष्य का इकाई-चिह्न जोड़ता है। चूँकि लक्ष्य उस इकाई को धारण करता है जैसा वह लिखा गया था, उपसर्ग युक्त और
वैकल्पिक इकाइयाँ समूह के आधार-चिह्न (`m`, `s`) के बजाय अपने **स्वयं के** चिह्न (`km`, `h`, `mi`) में प्रस्तुत
होती हैं।

## संख्या स्वरूपण: पैटर्न और लोकेल

इनफ़िक्स रूप कच्चा `Double` प्रस्तुत करता है। **संख्यात्मक भाग** को गोल करने या स्थानीयकृत करने के लिए
[`java.util.Formatter`](https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html) पैटर्न और वैकल्पिक
`Locale` लेने वाले `format` ओवरलोड का उपयोग करें:

```kotlin
import java.util.Locale

v.format(kilo.meters / hours, "%.1f")                // "10.8 km/h"
v.format(kilo.meters / hours, "%.1f", Locale.GERMAN) // "10,8 km/h"
```

पैटर्न **केवल** संख्या को प्रभावित करता है; इकाई भाग अपरिवर्तित रहता है। अमान्य पैटर्न
`java.util.IllegalFormatException` फेंकता है, और असंगत लक्ष्य आयाम (`into` की तरह) `IllegalStateException`
फेंकता है।

## भिन्न बनाम गुणनफल संकेतन

अंतर्निर्मित फ़ॉर्मैटर इकाई भाग को इस प्रकार प्रस्तुत करता है:

| पद                              | प्रस्तुति        |
|---------------------------------|------------------|
| एकल इकाई, घातांक 1              | `km`             |
| घातांक ≠ 1                     | `m^2`            |
| एक अंश + ठीक एक हर              | `km/h`, `m/s^2`  |
| अन्यथा                          | `m*s^-3*A^-2`, `s^-1` |
| कोई इकाई नहीं (विमारहित)        | केवल संख्या      |

## पैटर्न के साथ `toString`

बिना तर्क वाला `toString()` अपरिवर्तित है (आधार-इकाई प्रस्तुति)। एक अतिरिक्त ओवरलोड वही संख्या पैटर्न/लोकेल
आधार-इकाई आउटपुट पर लागू करता है — यह बिना लक्ष्य वाली `format` क्रिया है:

```kotlin
(3 of meters / seconds).toString("%.2f", Locale.US) // "3.00 m/s"
(1500 of meters).toString("%.1f", Locale.US)        // "1500.0 m"
```

## वास्तविक उदाहरण

दौड़ने की गति परिवर्तित करके साफ़-सुथरे रूप में प्रिंट करें:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*
import java.util.Locale

val distance = 10 of kilo.meters
val time = 50 of minutes
val speed = distance / time                    // KSpeedUnitInstance

println(speed.format(kilo.meters / hours, "%.1f", Locale.US)) // "12.0 km/h"
println(speed.format(meters / seconds, "%.2f", Locale.US))    // "3.33 m/s"
```

## कस्टम प्रस्तुति

इकाई भाग एक प्रतिस्थापन-योग्य [`KUnitFormatter`](custom-formatters.md) द्वारा उत्पन्न होता है; साथ आने वाला
`KDefaultUnitFormatter` ऊपर दिखाया गया सादा पाठ उत्पन्न करता है — इसके सटीक नियम और आउटपुट उदाहरण के लिए [डिफ़ॉल्ट फ़ॉर्मैटर](default-formatter.md) देखें। पूरी तरह भिन्न संकेतन (ग्राफ़िकल सूत्र रेंडरर के
लिए LaTeX या MathML, HTML, ...) उत्पन्न करने के लिए अपना स्वयं का फ़ॉर्मैटर लागू करें और उसे स्पष्ट रूप से पास
करें। देखें [कस्टम फ़ॉर्मैटर](custom-formatters.md)।
