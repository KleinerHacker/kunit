# MathML फ़ॉर्मैटर

`KMathMlUnitFormatter` किसी मान को **प्रेज़ेंटेशन MathML** के रूप में रेंडर करता है, जिसे ब्राउज़र और MathJax मूल रूप
से रेंडर करते हैं। डिफ़ॉल्ट कॉन्फ़िगरेशन में `3 of meters / seconds` को `km/h` में पढ़ने पर `<mi>km</mi>` को
`<mi>h</mi>` पर विभाजित करने वाले `<mfrac>` सहित एक इनलाइन `<math>` बनता है।

यह `org.pcsoft.framework.kunit.formatter` पैकेज में है और एक अपरिवर्तनीय, थ्रेड-सुरक्षित `class` है।

## यह क्या बनाता है

`MFRAC` शैली स्वच्छ एकल-हर रूप को `<mfrac>` में रखती है; हर अन्य रूप (और पूरी `EXPONENT` शैली) गुणन `<mo>` से जुड़ा
सपाट गुणनफल होता है जिसमें चिह्नित `<msup>` घातांक होते हैं। विमारहित मान केवल `<mn>` के रूप में रेंडर होता है।

## कॉन्फ़िगरेशन

`KMathMlFormatConfig` एक मान-प्रकार है; प्रीसेट चुनें या स्वयं बनाएं:

| विकल्प            | मान                                       | डिफ़ॉल्ट        |
|------------------|------------------------------------------|----------------|
| `fractionStyle`  | `MFRAC`, `EXPONENT`                      | `MFRAC`        |
| `unitTag`        | `MI`, `MTEXT`                            | `MI`           |
| `multiplication` | `MIDDLE_DOT` (`·`), `TIMES` (`×`), `INVISIBLE_TIMES` | `INVISIBLE_TIMES` |
| `wrapper`        | `MATH_INLINE`, `MATH_BLOCK`, `FRAGMENT`  | `MATH_INLINE`  |

प्रीसेट: `DEFAULT`, `INLINE` (इनलाइन `<msup>` घातांक), `FRAGMENT` (बिना `<math>` रूट)।

## वास्तविक उदाहरण

दूरी और समय से गति (`v = s / t`):

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KMathMlUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

v.format(kilo.meters / hours, "%.1f", Locale.US, KMathMlUnitFormatter())
// <math display="inline"><mn>90.0</mn><mo>⁢</mo>
//   <mfrac><mrow><mi>km</mi></mrow><mrow><mi>h</mi></mrow></mfrac></math>
```

त्वरण (`a = m/s²`) घातांक को भिन्न के भीतर `<msup>` में रखता है:

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KMathMlUnitFormatter())
// <math display="inline"><mn>9.81</mn><mo>⁢</mo>
//   <mfrac><mrow><mi>m</mi></mrow><mrow><msup><mi>s</mi><mn>2</mn></msup></mrow></mfrac></math>
```

पूरी तरह भिन्न नोटेशन आउटपुट करने के लिए, [कस्टम फ़ॉर्मैटर](custom-formatters.md) देखें।
