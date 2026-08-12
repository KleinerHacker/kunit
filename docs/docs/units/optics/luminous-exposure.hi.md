# दीप्त एक्सपोज़र (Luminous Exposure)

पैकेज: `org.pcsoft.framework.kunit.optic.luminousexposure`
मूल इकाई: **लक्स सेकंड** (`KLuminousExposureUnit.BASE == KLuminousExposureUnit.LUX_SECOND`)

प्रकार: **संघटित इकाई**

दीप्त एक्सपोज़र `H` वह प्रदीप्ति (illuminance) है जो **समय के साथ संचित** होती है: `H = E · t`। यह किसी सतह द्वारा
प्राप्त *प्रकाश खुराक (light dose)* है — वह राशि जिसे संग्रहालय संरक्षक रंगों के फीके पड़ने को सीमित करने के लिए
प्रतिवर्ष लक्स-घंटों में बजट करते हैं, और वही राशि जो कैमरे के एक्सपोज़र मान के पीछे है।

इसका विहित आधार-आयाम सामान्य रूप है `luminousIntensity¹ · solidAngle¹ · distance⁻² · time¹`।

## इकाइयाँ

| इकाई       | Enum मान                          | संकेत  |        टोकन | 1 इकाई = ? lx·s |
|------------|-------------------------------------|--------|-------------:|----------------:|
| लक्स सेकंड | `KLuminousExposureUnit.LUX_SECOND`  | `lx*s` | `luxSeconds` |             1.0 |
| लक्स घंटा  | `KLuminousExposureUnit.LUX_HOUR`    | `lx*h` |   `luxHours` |            3600 |

सभी टोकन हर SI उपसर्ग को स्वीकार करते हैं (`kilo.luxHours` वार्षिक प्रकाश-खुराक बजट के लिए सामान्य इकाई है)।

## अपघटन (Decomposition)

इस समूह का एक ही अपघटन है, और इसके दोनों रूप एक ही टाइप किया गया, मान-समान इंस्टेंस उत्पन्न करते हैं:

| रूप               | व्यंजक                                                                   |
|------------------|------------------------------------------------------------------------------|
| टाइप किया गया संकारक  | `illuminance * time`                                                         |
| मूल (`toX()`)     | `((50 of lux).toUnit() * (10 of seconds).toUnit()).toLuminousExposure()`     |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousexposure.*

val typed = (50 of lux) * (10 of seconds)
val native = ((50 of lux).toUnit() * (10 of seconds).toUnit()).toLuminousExposure()

typed == native          // true
typed into luxSeconds    // 500.0
```

## समूह के साथ गणना

| व्यंजक                            | परिणाम प्रकार                   | अर्थ                    |
|------------------------------------|----------------------------------|-----------------------------|
| `illuminance * time`              | `KLuminousExposureUnitInstance` | `H = E · t`                |
| `luminousExposure / time`         | `KIlluminanceUnitInstance`      | औसत प्रदीप्ति    |
| `luminousExposure / illuminance`  | `KTimeUnitInstance`             | एक्सपोज़र का समय          |

## वास्तविक उदाहरण — एक संग्रहालय प्रकाश बजट

संवेदनशील जल-रंगों को प्रतिवर्ष लगभग **50 000 lx·h** तक सीमित किया जाता है। 50 lx की प्रदर्शन प्रदीप्ति और प्रतिदिन 8
खुलने के घंटों पर, यह वस्तु कितने दिनों तक प्रदर्शित की जा सकती है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousexposure.*

val perDay = (50 of lux) * (8 of hours)     // KLuminousExposureUnitInstance
perDay into luxHours                         // 400.0

val budget = 50_000 of luxHours
(budget into luxHours) / (perDay into luxHours)   // प्रतिवर्ष 125 खुले दिन

// दूसरी दिशा में: 200 lx पर यह कितनी देर रह सकती है?
val t = budget / (200 of lux)                // KTimeUnitInstance
t into hours                                  // 250.0 h
```

## मान अर्थविज्ञान (Value Semantics)

`equals`/`hashCode` **सामान्यीकृत lx·s मान** की तुलना करते हैं, इसलिए `(1 of luxHours) == (3600 of luxSeconds)`।
`toString()` मान को मूल इकाई में प्रस्तुत करता है: `"3600.0 lx*s"`।

## यह भी देखें

* [प्रदीप्ति (Illuminance)](illuminance.md) — वह दर जिससे यह राशि संचित होती है।
* [दीप्त ऊर्जा](luminous-energy.md) — प्रदीप्ति के बजाय फ्लक्स के लिए वही विचार।
* [प्रकाशिकी अवलोकन](overview.md)
