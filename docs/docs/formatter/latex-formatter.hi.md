# LaTeX फ़ॉर्मैटर

`KLatexUnitFormatter` किसी मान को **LaTeX** गणित के रूप में रेंडर करता है, जो MathJax, KaTeX या LaTeX दस्तावेज़ के
लिए तैयार होता है। डिफ़ॉल्ट कॉन्फ़िगरेशन में `3 of meters / seconds` को `km/h` में पढ़ने पर
`1.5\,\frac{\mathrm{km}}{\mathrm{h}}` बनता है।

यह `org.pcsoft.framework.kunit.formatter` पैकेज में है और एक अपरिवर्तनीय, थ्रेड-सुरक्षित `class` है।

## यह क्या बनाता है

लेआउट साझा नियमों का पालन करता है: `FRACTION` शैली में एक अंश और ठीक एक हर वाला स्वच्छ रूप `\frac{…}{…}` के रूप में
रखा जाता है; हर अन्य रूप — और पूरी `INLINE` शैली — गुणन चिह्न से जुड़ा एक सपाट गुणनफल होता है जिसमें चिह्नित घातांक
होते हैं। विमारहित मान केवल संख्या के रूप में रेंडर होता है।

## कॉन्फ़िगरेशन

`KLatexFormatConfig` एक मान-प्रकार है; प्रीसेट चुनें या स्वयं बनाएं:

| विकल्प            | मान                                       | डिफ़ॉल्ट   |
|------------------|------------------------------------------|-----------|
| `fractionStyle`  | `FRACTION`, `INLINE`                     | `FRACTION`|
| `unitWrapper`    | `MATHRM`, `TEXT`, `NONE`                 | `MATHRM`  |
| `multiplication` | `CDOT` (`\cdot`), `TIMES` (`\times`), `THIN_SPACE` (`\,`) | `CDOT` |
| `delimiter`      | `DOLLAR` (`$…$`), `PARENTHESES` (`\(…\)`), `NONE` | `NONE` |
| `spacing`        | `THIN` (`\,`), `NORMAL` (स्पेस)           | `THIN`    |

प्रीसेट: `DEFAULT`, `INLINE` (इनलाइन गुणनफल), `PLAIN` (बिना रैपर, सामान्य स्पेस)।

## वास्तविक उदाहरण

दूरी और समय से गति (`v = s / t`):

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KLatexUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

v.format(kilo.meters / hours, "%.1f", Locale.US, KLatexUnitFormatter())
// 90.0\,\frac{\mathrm{km}}{\mathrm{h}}
```

त्वरण (`a = m/s²`) भिन्न के भीतर घात वाले हर को दिखाता है:

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KLatexUnitFormatter())
// 9.81\,\frac{\mathrm{m}}{\mathrm{s}^{2}}
```

पूरी तरह भिन्न नोटेशन आउटपुट करने के लिए, [कस्टम फ़ॉर्मैटर](custom-formatters.md) देखें।
