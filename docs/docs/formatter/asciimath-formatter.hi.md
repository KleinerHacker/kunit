# AsciiMath फ़ॉर्मैटर

`KAsciiMathUnitFormatter` किसी मान को **AsciiMath** के रूप में रेंडर करता है, जो MathJax का संक्षिप्त इनपुट सिंटैक्स
है। डिफ़ॉल्ट कॉन्फ़िगरेशन में `3 of meters / seconds` को `km/h` में पढ़ने पर `10.8 "km"/"h"` बनता है।

यह `org.pcsoft.framework.kunit.formatter` पैकेज में है और एक अपरिवर्तनीय, थ्रेड-सुरक्षित `class` है।

## यह क्या बनाता है

`FRACTION` शैली स्वच्छ एकल-हर रूप के लिए `a/b` भिन्न रूप का उपयोग करती है (आवश्यकता होने पर अंश या घात वाले हर को
कोष्ठक में समूहित करती है); हर अन्य रूप (और पूरी `EXPONENT` शैली) गुणन चिह्न से जुड़ा सपाट गुणनफल होता है जिसमें
चिह्नित घातांक होते हैं। विमारहित मान केवल संख्या के रूप में रेंडर होता है।

## कॉन्फ़िगरेशन

`KAsciiMathFormatConfig` एक मान-प्रकार है; प्रीसेट चुनें या स्वयं बनाएं:

| विकल्प            | मान                                       | डिफ़ॉल्ट   |
|------------------|------------------------------------------|-----------|
| `fractionStyle`  | `FRACTION`, `EXPONENT`                   | `FRACTION`|
| `quoting`        | `QUOTED` (`"km"`), `BARE` (`km`)         | `QUOTED`  |
| `multiplication` | `ASTERISK` (`*`), `TIMES` (`xx`), `SPACE` (स्पेस) | `SPACE` |

प्रीसेट: `DEFAULT`, `PLAIN` (`*` से जुड़े बिना-उद्धरण प्रतीक)।

## वास्तविक उदाहरण

दूरी और समय से गति (`v = s / t`):

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KAsciiMathUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

v.format(kilo.meters / hours, "%.1f", Locale.US, KAsciiMathUnitFormatter())
// 90.0 "km"/"h"
```

त्वरण (`a = m/s²`) घात वाले हर को कोष्ठक में समूहित करता है:

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KAsciiMathUnitFormatter())
// 9.81 "m"/("s"^2)
```

पूरी तरह भिन्न नोटेशन आउटपुट करने के लिए, [कस्टम फ़ॉर्मैटर](custom-formatters.md) देखें।
