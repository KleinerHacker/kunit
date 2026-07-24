# कंसोल फ़ॉर्मैटर

`KConsoleUnitFormatter` किसी मान को **ANSI-सक्षम टर्मिनल** के लिए रेंडर करता है। यह ठीक वही
संकेतन उत्पन्न करता है जो [डिफ़ॉल्ट फ़ॉर्मैटर](default-formatter.md) करता है (`"10.8 km/h"`, `"m^2"`,
`"m*s^-3*A^-2"`), परंतु प्रत्येक दृश्य भाग — संख्या, इकाई प्रतीक, संकारक और घातांक — को ANSI रंग
अनुक्रमों में लपेट देता है, ताकि कंसोल पर हर भाग उभरकर दिखे।

यह `org.pcsoft.framework.kunit.formatter` पैकेज में है। डिफ़ॉल्ट फ़ॉर्मैटर के विपरीत यह एक सामान्य
(अपरिवर्तनीय, थ्रेड-सुरक्षित) `class` है, क्योंकि यह एक रंग पैलेट धारण करता है।

## यह क्या उत्पन्न करता है

लेआउट [डिफ़ॉल्ट फ़ॉर्मैटर](default-formatter.md) के **समान** है: `"<संख्या> <इकाई>"` रूप, एकल-भिन्न
रूप `a/b` केवल तभी जब ठीक एक अंश पद और ठीक एक हर पद हो, अन्यथा चिह्नित घातांकों वाला सपाट गुणनफल, और
विमारहित मान के लिए केवल संख्या। एकमात्र अंतर यह है कि प्रत्येक भाग ANSI SGR रंग में लपेटा जाता है और
रीसेट अनुक्रम `ESC[0m` से बंद किया जाता है।

### रंगे जाने वाले भाग

चार दृश्य भाग [`KConsoleColorPalette`](#पैलेट) के माध्यम से स्वतंत्र रूप से रंगे जाते हैं:

| भाग        | पैलेट फ़ील्ड     | उदाहरण           |
|------------|------------------|------------------|
| संख्या     | `numberColor`    | `10.8`           |
| इकाई प्रतीक | `symbolColor`    | `km`, `h`, `m`   |
| संकारक     | `operatorColor`  | `*`, `/`         |
| घातांक     | `exponentColor`  | `^2`, `^-3`      |

जिस भाग का रंग **रिक्त स्ट्रिंग** है वह बिना किसी एस्केप अनुक्रम के उत्पन्न होता है (वह भाग बिना रंग के
रहता है) — इसी प्रकार `MONOCHROME` घातांक को बिना रंग के छोड़ देता है।

## पैलेट

रंग एक मान-प्रकार `KConsoleColorPalette` हैं। तीन पैलेट पूर्वपरिभाषित हैं:

| पैलेट        | संख्या                        | प्रतीक                | संकारक          | घातांक                   |
|--------------|-------------------------------|-----------------------|-----------------|--------------------------|
| `CLASSIC`    | सियान `ESC[36m`              | पीला `ESC[33m`        | धूसर `ESC[90m`  | मैजेंटा `ESC[35m`        |
| `VIVID`      | चमकीला हरा मोटा `ESC[92;1m`  | चमकीला नीला `ESC[94m` | सफ़ेद `ESC[97m` | चमकीला मैजेंटा `ESC[95m` |
| `MONOCHROME` | मोटा `ESC[1m`               | मंद `ESC[2m`          | मंद `ESC[2m`    | बिना रंग (रिक्त)         |

- `CLASSIC` गहरे टर्मिनल पर शांत और पठनीय है, और यह **डिफ़ॉल्ट** है।
- `VIVID` उच्च-विरोधाभास और आकर्षक है।
- `MONOCHROME` केवल चमक का उपयोग करता है (बिना रंग), जो कम रंग वाले टर्मिनलों के लिए उपयुक्त है।

## उपयोग

बिना तर्क के इसे बनाने पर डिफ़ॉल्ट `CLASSIC` पैलेट उपयोग होता है, या आप कोई पूर्वपरिभाषित अथवा कस्टम
पैलेट दे सकते हैं। इसके बाद इसे किसी भी अन्य फ़ॉर्मैटर की तरह [`format`](formatting.md) क्रिया (या
पैरामीटर-युक्त `toString`) को सौंपा जाता है:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KConsoleColorPalette
import org.pcsoft.framework.kunit.formatter.KConsoleUnitFormatter
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 3 of meters / seconds

// डिफ़ॉल्ट CLASSIC पैलेट
v.format(kilo.meters / hours, "%.1f", Locale.US, KConsoleUnitFormatter())

// पूर्वपरिभाषित पैलेट
v.format(kilo.meters / hours, "%.1f", Locale.US, KConsoleUnitFormatter(KConsoleColorPalette.VIVID))

// बिना लक्ष्य के आधार इकाई में रेंडरिंग
(5 of meters).toString(pattern = "%.1f", formatter = KConsoleUnitFormatter(KConsoleColorPalette.MONOCHROME))
```

## अपना पैलेट परिभाषित करना

`KConsoleColorPalette` एक सादा डेटा क्लास है, इसलिए आप अपने स्वयं के रंग अनुक्रम दे सकते हैं। प्रत्येक
फ़ील्ड ANSI का **प्रारंभक अनुक्रम** धारण करता है (जैसे लाल के लिए `ESC[31m`, जहाँ `ESC` कोड 27 वाला
एस्केप वर्ण है); साझा `reset` (डिफ़ॉल्ट `ESC[0m`) प्रत्येक रंगे भाग के बाद जोड़ा जाता है:

```kotlin
import org.pcsoft.framework.kunit.formatter.KConsoleColorPalette
import org.pcsoft.framework.kunit.formatter.KConsoleUnitFormatter

val esc = 27.toChar()   // ANSI एस्केप वर्ण (ESC)
val myPalette = KConsoleColorPalette(
    numberColor = "$esc[31m",   // लाल
    symbolColor = "$esc[32m",   // हरा
    operatorColor = "$esc[34m", // नीला
    exponentColor = "$esc[35m", // मैजेंटा
)
val formatter = KConsoleUnitFormatter(myPalette)
```

केवल रंग ही नहीं, बल्कि पूरी तरह भिन्न संकेतन उत्पन्न करने के लिए, इसके बजाय अपना स्वयं का
[कस्टम फ़ॉर्मैटर](custom-formatters.md) कार्यान्वित करें।

## कॉन्फ़िगरेशन (घातांक, चिह्न, फलन प्रतीक)

रंगों से स्वतंत्र, दूसरा तर्क `KConsoleFormatConfig` नोटेशन को नियंत्रित करता है ([डिफ़ॉल्ट फ़ॉर्मैटर](default-formatter.md)
के समान): `exponentStyle` (`CARET` = `m^2` / `SUPERSCRIPT` = `m²`), `multiplication` (`*` / `·` / `×`),
`division` (`/` / `÷`), और `functionSymbols` (`UNICODE` / `ASCII`)। दोनों तर्कों में डिफ़ॉल्ट हैं, इसलिए
`KConsoleUnitFormatter()` पहले जैसा ही है। इसे `KConsoleUnitFormatter(palette, config)` के रूप में पास करें।

वास्तविक भिन्न-रेखा वाले बहु-पंक्ति द्वि-आयामी भिन्न के लिए, [ग्राफ़िकल फ़ॉर्मैटर](graphical-formatter.md) देखें।
