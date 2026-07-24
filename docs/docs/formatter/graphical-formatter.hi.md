# ग्राफ़िकल फ़ॉर्मैटर

`KGraphicalConsoleUnitFormatter` ANSI-सक्षम टर्मिनल के लिए किसी मान को **कई पंक्तियों में ग्राफ़िकल रूप से** रेंडर करता
है: भिन्न को एक वास्तविक द्वि-आयामी स्टैक (अंश, क्षैतिज रेखा, हर) के रूप में खींचा जाता है, और मान रेखा (मध्य) पंक्ति पर
रखा जाता है। घातांक हमेशा वास्तविक Unicode उर्ध्वांक अंकों के रूप में दिखाए जाते हैं, और प्रत्येक दृश्य भूमिका
`KGraphicalConsoleColorPalette` के माध्यम से रंगी जाती है।

यह `org.pcsoft.framework.kunit.formatter` पैकेज में है और एक अपरिवर्तनीय, थ्रेड-सुरक्षित `class` है।

## यह क्या बनाता है

स्वच्छ एकल-हर रूप तीन पंक्तियों में रखा जाता है; हर अन्य रूप गुणन चिह्न से जुड़ा उर्ध्वांक घातांक वाला एकल-पंक्ति गुणनफल
होता है; विमारहित मान केवल रंगी संख्या होता है। अंश और हर को उनकी **दृश्य** चौड़ाई (ANSI रंग अनुक्रम चौड़ाई में नहीं
गिने जाते) के आधार पर रेखा के ऊपर केंद्रित किया जाता है। त्वरण `9.81 m/s²` (बिना रंग) इस प्रकार दिखता है:

```
     m
9.81 ──
     s²
```

## कॉन्फ़िगरेशन

`KGraphicalConsoleFormatConfig` एक मान-प्रकार है; `DEFAULT` प्रीसेट चुनें या स्वयं बनाएं:

| विकल्प             | मान / प्रकार                                     | डिफ़ॉल्ट    |
|-------------------|-------------------------------------------------|------------|
| `palette`         | `KGraphicalConsoleColorPalette` — `CLASSIC`, `VIVID`, `MONOCHROME` | `CLASSIC` |
| `fractionBar`     | `LINE` (`─`), `HEAVY` (`━`), `ASCII` (`-`)      | `LINE`     |
| `multiplication`  | `ASTERISK` (`*`), `MIDDLE_DOT` (`·`), `CROSS` (`×`) | `MIDDLE_DOT` |
| `functionSymbols` | `KGraphicalFunctionSymbols` — `UNICODE`, `ASCII` | `UNICODE`  |

पैलेट पाँच भूमिकाओं (संख्या, प्रतीक, संकारक, घातांक और रेखा) को रंगता है। जिस भूमिका का रंग खाली स्ट्रिंग है वह बिना रंग
रहती है (इसी तरह `MONOCHROME` घातांक को बिना रंग छोड़ता है)।

## वास्तविक उदाहरण

टर्मिनल में गति और त्वरण:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KGraphicalConsoleUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

// डिफ़ॉल्ट CLASSIC पैलेट (यहाँ रंग छोड़े गए); लेआउट:
//      km
// 90.0 ──
//      h
println(v.format(kilo.meters / hours, "%.1f", Locale.US, KGraphicalConsoleUnitFormatter()))

// मोटी क्षैतिज रेखा
val config = KGraphicalConsoleFormatConfig(fractionBar = KGraphicalFractionBar.HEAVY)
println(v.format(kilo.meters / hours, "%.1f", Locale.US, KGraphicalConsoleUnitFormatter(config)))
```

पूरी तरह भिन्न नोटेशन आउटपुट करने के लिए, [कस्टम फ़ॉर्मैटर](custom-formatters.md) देखें।
