# कस्टम फ़ॉर्मैटर

[`format`](formatting.md) क्रिया और पैरामीटर-युक्त `toString` एक प्रतिस्थापन-योग्य `KUnitFormatter` के माध्यम
से पाठ प्रस्तुत करते हैं। साथ आने वाला `KDefaultUnitFormatter` `"10.8 km/h"` जैसा सादा पाठ उत्पन्न करता है, परंतु
आप पूरी तरह कस्टम प्रस्तुति जोड़ सकते हैं — उदाहरण के लिए ग्राफ़िकल सूत्र रेंडरर के लिए **LaTeX** या **MathML**,
HTML, या कोई भी डोमेन-विशिष्ट संकेतन। इससे kunit को उन तृतीय-पक्ष ढाँचों की ओर विस्तारित करना आसान हो जाता है जो
किसी स्ट्रिंग को टाइपसेट सूत्र में बदलते हैं।

## अनुबंध

फ़ॉर्मैटर को जो भी चाहिए वह एक ही `KUnitFormatContext` में मिलता है, और वह अंतिम स्ट्रिंग लौटाता है:

```kotlin
interface KUnitFormatter {
    fun format(context: KUnitFormatContext): String
}

data class KUnitFormatContext(
    val value: Double,            // संख्या, पहले से लक्ष्य इकाई में परिवर्तित
    val units: List<KUnitTerm>,   // लक्ष्य आयाम के पद (उपसर्ग/घातांक प्रदर्शन मेटाडेटा सहित)
    val pattern: String? = null,  // संख्या के लिए वैकल्पिक java.util.Formatter पैटर्न
    val locale: Locale = Locale.getDefault(),
)
```

सब कुछ **एक** संदर्भ ऑब्जेक्ट में पास होता है ताकि इंटरफ़ेस आपके कार्यान्वयन को तोड़े बिना योगात्मक रूप से बढ़
सके (नए फ़ील्ड डिफ़ॉल्ट मान लेते हैं)। सामान्य निर्माण-खंडों के लिए दो पुनः-प्रयोज्य सहायक हैं:

- `KUnitFormatContext.renderValue()` — संख्या प्रस्तुत करता है: `pattern` के `null` होने पर
  `Double.toString()`, अन्यथा `String.format(locale, pattern, value)`।
- `KUnitTerm.displaySymbol` — पद का लिखा-हुआ चिह्न (`"km"`, `"h"`), प्रदर्शन मेटाडेटा का सम्मान करते हुए; न होने
  पर समूह के आधार-चिह्न पर लौटता है।

पद का `exponent` घात बताता है (धनात्मक = अंश, ऋणात्मक = हर); घातांक को कैसे प्रस्तुत करना है यह फ़ॉर्मैटर तय
करता है।

## चरण-दर-चरण: एक LaTeX फ़ॉर्मैटर

निम्न फ़ॉर्मैटर अंश और हर पदों से `\frac{...}{...}` प्रस्तुत करता है, प्रत्येक इकाई-चिह्न के लिए `\mathrm{...}`
का उपयोग करते हुए:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.*

object LatexFormatter : KUnitFormatter {
    override fun format(context: KUnitFormatContext): String {
        // 1. अंश (घातांक > 0) और हर (घातांक < 0) में विभाजित करें
        val (numerator, denominator) = context.units.partition { it.exponent > 0 }

        // 2. एक पद प्रस्तुत करें, जैसे \mathrm{km} या \mathrm{s}^{2}
        fun render(terms: List<KUnitTerm>) = terms.joinToString(" ") { term ->
            val magnitude = kotlin.math.abs(term.exponent)
            val base = "\\mathrm{${term.displaySymbol}}"      // प्रदर्शन मेटाडेटा का उपयोग
            if (magnitude == 1) base else "$base^{$magnitude}"
        }

        // 3. सहायक के माध्यम से संख्या (पैटर्न + लोकेल का सम्मान)
        val value = context.renderValue()

        // 4. संयोजन
        if (denominator.isEmpty()) return "$value\\,${render(numerator)}".trim()
        return "$value\\,\\frac{${render(numerator)}}{${render(denominator)}}"
    }
}
```

## उपयोग

फ़ॉर्मैटर को स्पष्ट रूप से पास करें — जब तक आप न कहें, डिफ़ॉल्ट व्यवहार कभी नहीं बदलता:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 3 of meters / seconds

// कस्टम फ़ॉर्मैटर के साथ लक्ष्य इकाई में स्वरूपण
v.format(kilo.meters / hours, "%.1f", Locale.US, LatexFormatter)
// "10.8\,\frac{\mathrm{km}}{\mathrm{h}}"

// या बिना लक्ष्य के आधार इकाइयाँ प्रस्तुत करें
(5 of meters).toString(pattern = null, formatter = LatexFormatter)
// "5.0\,\mathrm{m}"
```

## टिप्पणियाँ

- फ़ॉर्मैटर को **स्टेटलेस** और इसलिए थ्रेड-सुरक्षित रखें — साथ आने वाला `KDefaultUnitFormatter` एक सादा
  `object` है, और ऊपर का `LatexFormatter` भी।
- `KUnitFormatContext` को लक्ष्य इकाई में **पहले से परिवर्तित** मान मिलता है, इसलिए फ़ॉर्मैटर स्वयं कोई इकाई
  रूपांतरण नहीं करता — वह केवल प्रस्तुत करता है।
- `units` पद प्रदर्शन मेटाडेटा (`KUnitTerm.display`) धारण करते हैं; चिह्न हमेशा `displaySymbol` से पढ़ें ताकि
  उपसर्ग-युक्त और वैकल्पिक इकाइयाँ (`km`, `mi`, `KiB`) सही ढंग से प्रस्तुत हों।
