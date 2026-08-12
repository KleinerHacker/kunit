# दीप्त ऊर्जा (Luminous Energy)

पैकेज: `org.pcsoft.framework.kunit.optic.luminousenergy`
मूल इकाई: **लुमेन सेकंड** (`KLuminousEnergyUnit.BASE == KLuminousEnergyUnit.LUMEN_SECOND`)

प्रकार: **संघटित इकाई**

दीप्त ऊर्जा `Q` वह दीप्त फ्लक्स है जो **समय के साथ संचित** होता है: `Q = Φ · t`। जहाँ फ्लक्स बताता है कि कोई लैंप
*अभी* कितना उज्ज्वल है, वहीं दीप्त ऊर्जा बताती है कि उसने कुल कितना प्रकाश दिया है — यह वही राशि है जो लैंप जीवन
रेटिंग और फोटोग्राफिक फ्लैश ऊर्जा के पीछे है। लुमेन सेकंड को **टैलबोट (talbot)** भी कहा जाता है।

इसका विहित आधार-आयाम सामान्य रूप है `luminousIntensity¹ · solidAngle¹ · time¹`।

## इकाइयाँ

| इकाई         | Enum मान                          | संकेत  |          टोकन | 1 इकाई = ? lm·s |
|--------------|-------------------------------------|--------|---------------:|----------------:|
| लुमेन सेकंड | `KLuminousEnergyUnit.LUMEN_SECOND`  | `lm*s` | `lumenSeconds` |             1.0 |
| टैलबोट       | `KLuminousEnergyUnit.LUMEN_SECOND`  | `lm*s` |      `talbots` |             1.0 |
| लुमेन घंटा   | `KLuminousEnergyUnit.LUMEN_HOUR`    | `lm*h` |    `lumenHours` |            3600 |

`talbots` मूल इकाई की एक दूसरी वर्तनी है, अपनी अलग इकाई नहीं। सभी टोकन हर SI उपसर्ग को स्वीकार करते हैं
(`kilo.lumenHours`, `milli.lumenSeconds`, …)।

## अपघटन (Decomposition)

इस समूह का एक ही अपघटन है, और इसके दोनों रूप एक ही टाइप किया गया, मान-समान इंस्टेंस उत्पन्न करते हैं:

| रूप               | व्यंजक                                                                  |
|------------------|-----------------------------------------------------------------------------|
| टाइप किया गया संकारक  | `luminousFlux * time`                                                       |
| मूल (`toX()`)     | `((800 of lumens).toUnit() * (5 of seconds).toUnit()).toLuminousEnergy()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.luminousenergy.*

val typed = (800 of lumens) * (5 of seconds)
val native = ((800 of lumens).toUnit() * (5 of seconds).toUnit()).toLuminousEnergy()

typed == native            // true
typed into lumenSeconds    // 4000.0
```

## समूह के साथ गणना

| व्यंजक                            | परिणाम प्रकार                   | अर्थ                       |
|-----------------------------------|--------------------------------|--------------------------------|
| `luminousFlux * time`            | `KLuminousEnergyUnitInstance` | `Q = Φ · t`                   |
| `luminousEnergy / time`          | `KLuminousFluxUnitInstance`   | औसत फ्लक्स |
| `luminousEnergy / luminousFlux`  | `KTimeUnitInstance`           | फ्लक्स कब तक उत्सर्जित हुआ |

## वास्तविक उदाहरण — एक लैंप के जीवनकाल में प्रकाश उत्पादन

एक 800 lm LED बल्ब **25 000 h** के लिए रेट किया गया है। वह अपने पूरे जीवनकाल में कुल कितना प्रकाश देगा:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.luminousenergy.*

val q = (800 of lumens) * (25_000 of hours)
q into lumenHours          // 20_000_000.0
q into mega.lumenHours     // 20.0

// प्रतिदिन 3 h चलाने पर, यह कितने दिनों तक चलेगा?
val perDay = (800 of lumens) * (3 of hours)
q into lumenHours / (perDay into lumenHours)   // ≈ 8333 दिन
```

## मान अर्थविज्ञान (Value Semantics)

`equals`/`hashCode` **सामान्यीकृत lm·s मान** की तुलना करते हैं, इसलिए `(1 of lumenHours) == (3600 of lumenSeconds)`।
`toString()` मान को मूल इकाई में प्रस्तुत करता है: `"3600.0 lm*s"`।

## यह भी देखें

* [दीप्त फ्लक्स](luminous-flux.md) — वह दर जिससे यह राशि संचित होती है।
* [दीप्त एक्सपोज़र](luminous-exposure.md) — फ्लक्स के बजाय प्रदीप्ति (illuminance) के लिए वही विचार।
* [प्रकाशिकी अवलोकन](overview.md)
