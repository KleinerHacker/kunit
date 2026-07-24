# Typst फ़ॉर्मैटर

`KTypstUnitFormatter` किसी मान को **Typst** गणित के रूप में रेंडर करता है। डिफ़ॉल्ट कॉन्फ़िगरेशन में
`3 of meters / seconds` को `km/h` में पढ़ने पर `$1.5 upright("km")/upright("h")$` बनता है।

यह `org.pcsoft.framework.kunit.formatter` पैकेज में है और एक अपरिवर्तनीय, थ्रेड-सुरक्षित `class` है।

## यह क्या बनाता है

`FRACTION` शैली स्वच्छ एकल-हर रूप के लिए `a/b` भिन्न रूप का उपयोग करती है (आवश्यकता होने पर अंश या घात वाले हर को
कोष्ठक में समूहित करती है); हर अन्य रूप (और पूरी `EXPONENT` शैली) गुणन चिह्न से जुड़ा सपाट गुणनफल होता है जिसमें
चिह्नित घातांक होते हैं। विमारहित मान केवल संख्या के रूप में रेंडर होता है।

## कॉन्फ़िगरेशन

`KTypstFormatConfig` एक मान-प्रकार है; प्रीसेट चुनें या स्वयं बनाएं:

| विकल्प            | मान                                       | डिफ़ॉल्ट   |
|------------------|------------------------------------------|-----------|
| `fractionStyle`  | `FRACTION`, `EXPONENT`                   | `FRACTION`|
| `unitStyle`      | `UPRIGHT` (`upright("km")`), `TEXT` (`"km"`) | `UPRIGHT` |
| `multiplication` | `SPACE` (स्पेस), `DOT` (`dot`), `TIMES` (`times`) | `SPACE` |
| `delimiter`      | `MATH` (`$…$`), `FRAGMENT`               | `MATH`    |

प्रीसेट: `DEFAULT`, `FRAGMENT` (बिना `$…$` सीमांकक)।

## वास्तविक उदाहरण

दूरी और समय से गति (`v = s / t`):

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KTypstUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

v.format(kilo.meters / hours, "%.1f", Locale.US, KTypstUnitFormatter())
// $90.0 upright("km")/upright("h")$
```

त्वरण (`a = m/s²`) घात वाले हर को कोष्ठक में समूहित करता है:

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KTypstUnitFormatter())
// $9.81 upright("m")/(upright("s")^2)$
```

पूरी तरह भिन्न नोटेशन आउटपुट करने के लिए, [कस्टम फ़ॉर्मैटर](custom-formatters.md) देखें।
