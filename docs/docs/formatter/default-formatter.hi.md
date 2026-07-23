# डिफ़ॉल्ट फ़ॉर्मैटर

`KDefaultUnitFormatter` वह फ़ॉर्मैटर है जिसे kunit बिना किसी अतिरिक्त सेटअप के उपयोग करता है। जब भी आप
[`format`](formatting.md) या पैरामीटर वाले `toString` को अपना फ़ॉर्मैटर **दिए बिना** कॉल करते हैं, तो
यही परिणाम बनाता है — `"10.8 km/h"` जैसा सरल, मानव-पठनीय टेक्स्ट। यह पृष्ठ ठीक-ठीक बताता है कि यह
**क्या** और **कैसे** रेंडर करता है, आउटपुट उदाहरणों के साथ, और इसे स्पष्ट रूप से उपयोग करने का तरीका
दिखाता है।

यह एक स्टेटलेस `object` (थ्रेड-सुरक्षित) है और `org.pcsoft.framework.kunit.formatter` पैकेज में स्थित है।

## यह क्या उत्पन्न करता है

रेंडर की गई स्ट्रिंग के दो भाग होते हैं: **संख्या** और **इकाई** भाग, एक स्पेस से अलग किए गए
(`"<संख्या> <इकाई>"`)। यदि मान विमारहित (कोई इकाई नहीं) है, तो केवल संख्या रेंडर होती है।

### संख्या

- पैटर्न के बिना, कच्चा `Double` `Double.toString()` के माध्यम से मुद्रित होता है।
- `java.util.Formatter` पैटर्न (और वैकल्पिक `Locale`) के साथ, संख्या
  `String.format(locale, pattern, value)` के माध्यम से रेंडर होती है। पैटर्न **केवल** संख्या को
  प्रभावित करता है, इकाई भाग को कभी नहीं।

| कॉल                                               | रेंडर की गई संख्या |
|--------------------------------------------------|-----------------|
| `format(kilo.meters / hours)`                    | `10.799999999999999` |
| `format(kilo.meters / hours, "%.1f")`            | `10.8` |
| `format(kilo.meters / hours, "%.1f", Locale.GERMAN)` | `10,8` |

### इकाई भाग

प्रत्येक इकाई पद अपने **स्वयं के लिखे गए प्रतीक** (उपसर्ग और वैकल्पिक-इकाई प्रदर्शन मेटाडेटा का सम्मान
करते हुए) से रेंडर होता है, इसलिए `km`, `h`, `mi`, `KiB` समूह आधार प्रतीक के बजाय स्वयं के रूप में रेंडर
होते हैं। समग्र आकार पदों पर निर्भर करता है:

| पद                                       | रेंडर परिणाम           |
|------------------------------------------|-----------------------|
| एकल इकाई, घातांक 1                        | `km`                  |
| घातांक ≠ 1                              | `m^2`                 |
| एक अंश + ठीक एक हर                        | `km/h`, `m/s^2`       |
| कुछ और                                   | `m*s^-3*A^-2`, `s^-1` |
| कोई इकाई नहीं (विमारहित)                  | केवल संख्या            |

एकल भिन्न रूप (`a/b`) केवल तभी उपयोग होता है जब **ठीक एक** अंश पद और **ठीक एक** हर पद हो। बाकी सब कुछ
स्पष्ट (संभवतः ऋणात्मक) घातांकों वाले सपाट गुणनफल के रूप में रेंडर होता है।

## आउटपुट उदाहरण

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

(1500 of meters).toString()                          // "1500.0 m"
(3 of meters / seconds).format(kilo.meters / hours)  // "10.799999999999999 km/h"
(3 of meters / seconds).format(meters / seconds, "%.2f") // "3.33 m/s"
(9.81 of meters / (seconds pow 2)).format(meters / (seconds pow 2), "%.2f") // "9.81 m/s^2"
```

## इसे स्पष्ट रूप से उपयोग करना

डिफ़ॉल्ट फ़ॉर्मैटर स्वचालित रूप से लागू होता है, इसलिए आप शायद ही कभी इसका नाम देते हैं। फिर भी आप इसे
स्पष्ट रूप से पास कर सकते हैं — कस्टम फ़ॉर्मैटर के साथ समरूपता के लिए, या कॉल स्थल पर चयन को स्पष्ट करने
के लिए:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KDefaultUnitFormatter
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 3 of meters / seconds

// स्पष्ट फ़ॉर्मैटर, डिफ़ॉल्ट कॉल के समान परिणाम
v.format(kilo.meters / hours, "%.1f", Locale.US, KDefaultUnitFormatter) // "10.8 km/h"

// लक्ष्य के बिना डिफ़ॉल्ट फ़ॉर्मैटर से आधार इकाइयाँ रेंडर करें
(5 of meters).toString(pattern = null, formatter = KDefaultUnitFormatter) // "5.0 m"
```

पूरी तरह से भिन्न संकेतन उत्पन्न करने के लिए, [कस्टम फ़ॉर्मैटर](custom-formatters.md) देखें।
