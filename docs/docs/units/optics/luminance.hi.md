# ल्यूमिनेंस

पैकेज: `org.pcsoft.framework.kunit.optic.luminance`
मूल इकाई: **कैंडेला प्रति वर्ग मीटर** (`KLuminanceUnit.BASE == KLuminanceUnit.CANDELA_PER_SQUARE_METER`)

प्रकार: **संघटित इकाई**

ल्यूमिनेंस `L` **उत्सर्जक क्षेत्रफल की प्रति इकाई** ल्यूमिनस तीव्रता है: `L = I / A`, अतः `1 cd/m² = 1 nit`। यह वही
राशि है जिसे आँख वास्तव में किसी सतह की "चमक" के रूप में अनुभव करती है, और यह वह संख्या है जिसे हर डिस्प्ले विशिष्टता
उद्धृत करती है — एक सामान्य ऑफिस मॉनिटर 250–350 निट्स होता है, एक HDR टेलीविज़न 1000 निट्स या उससे अधिक।

इसका विहित आधार-आयाम सामान्य रूप `luminousIntensity¹ · distance⁻²` है।

## इकाइयाँ

| इकाई                        | Enum मान                                  | प्रतीक   |                      टोकन | cd/m² में 1 इकाई |
|-------------------------------|---------------------------------------------|----------|-----------------------------:|-------------------:|
| कैंडेला प्रति वर्ग मीटर         | `KLuminanceUnit.CANDELA_PER_SQUARE_METER`    | `cd/m^2` | `candelasPerSquareMeter`     |                 1.0 |
| निट                          | `KLuminanceUnit.CANDELA_PER_SQUARE_METER`    | `cd/m^2` |                       `nits` |                 1.0 |
| स्टिल्ब                       | `KLuminanceUnit.STILB`                       | `sb`     |                     `stilbs` |              10 000 |
| एपोस्टिल्ब                    | `KLuminanceUnit.APOSTILB`                    | `asb`    |                  `apostilbs` |               1 / π |
| लैम्बर्ट                      | `KLuminanceUnit.LAMBERT`                     | `L`      |                   `lamberts` |            10⁴ / π  |
| फुट-लैम्बर्ट                  | `KLuminanceUnit.FOOT_LAMBERT`                | `fL`     |               `footLamberts` |          ≈ 3.426259 |

`nits` मूल इकाई की एक दूसरी वर्तनी है, न कि अपनी स्वयं की इकाई — यह कैंडेला प्रति वर्ग मीटर के लिए डिस्प्ले उद्योग का
नाम है। एपोस्टिल्ब, लैम्बर्ट और फुट-लैम्बर्ट *लैम्बर्टियन* परिवार से संबंधित हैं और वह गुणक `1/π` रखते हैं जो एक आदर्श
विसरित उत्सर्जक की इल्यूमिनेंस को उसकी ल्यूमिनेंस में परिवर्तित करता है। सभी टोकन हर SI उपसर्ग स्वीकार करते हैं।

## अपघटन

समूह के **दो** अपघटन हैं। दोनों एक ही सामान्यीकरण फ़ैक्टरी में जुड़ते हैं, इसलिए वे एक ही टाइप किया गया, मान-समतुल्य
उदाहरण उत्पन्न करते हैं:

| रूप                     | अभिव्यक्ति                                                       |
|---------------------------|--------------------------------------------------------------------|
| टाइप किया गया संकारक A    | `luminousIntensity / area`                                         |
| टाइप किया गया संकारक B    | `illuminance / solidAngle`                                         |
| नेटिव (`toX()`)           | `((250 of candelas).toUnit() / area.toUnit()).toLuminance()`       |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminance.*

val squareMeter = (1 of meters) * (1 of meters)

val viaIntensity  = (250 of candelas) / squareMeter      // A
val viaIlluminance = (500 of lux) / (2 of steradians)    // B
val native = ((250 of candelas).toUnit() / squareMeter.toUnit()).toLuminance()

viaIntensity == viaIlluminance   // true
viaIntensity == native           // true
viaIntensity into nits           // 250.0
```

## समूह के साथ गणना

| अभिव्यक्ति                       | परिणाम प्रकार                    | अर्थ                          |
|-------------------------------------|-------------------------------------|---------------------------------|
| `luminousIntensity / area`          | `KLuminanceUnitInstance`            | `L = I / A`                    |
| `illuminance / solidAngle`          | `KLuminanceUnitInstance`            | `L = E / Ω`                    |
| `luminance * area`                  | `KLuminousIntensityUnitInstance`    | `I = L · A`                    |
| `luminance * solidAngle`            | `KIlluminanceUnitInstance`          | `E = L · Ω`                    |
| `luminousIntensity / luminance`     | `KAreaUnitInstance`                 | उत्सर्जक क्षेत्रफल                |
| `illuminance / luminance`           | `KSolidAngleUnitInstance`           | वह कोण जिस पर यह फैला हुआ है      |

## वास्तविक उदाहरण — एक मॉनिटर की निट रेटिंग

एक 27" मॉनिटर, जिसका पैनल **0.21 m²** है, **300 निट्स** रेटेड है। यह इस कुल अक्षीय तीव्रता से मेल खाता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminance.*

val panel = (0.6 of meters) * (0.35 of meters)   // ≈ 0.21 m²
val l = 300 of nits

val i = l * panel                                 // KLuminousIntensityUnitInstance
i into candelas                                   // 63.0 cd

l into footLamberts                               // ≈ 87.6 (इंपीरियल पठन)
```

## मान अर्थ-विज्ञान

`equals`/`hashCode` **सामान्यीकृत cd/m² मान** की तुलना करते हैं, इसलिए `(1 of stilbs) == (10000 of candelasPerSquareMeter)`।
`toString()` मान को मूल इकाई में प्रस्तुत करता है: `"250.0 cd/m^2"`।

## यह भी देखें

* [ल्यूमिनस तीव्रता](luminous-intensity.md) — ल्यूमिनेंस का अंश (numerator)।
* [इल्यूमिनेंस](illuminance.hi.md) — किसी सतह से निकलने के बजाय उस पर पहुँचने वाला प्रकाश।
* [रेडिएंस](radiance.md) — विकिरणमितीय समकक्ष।
* [प्रकाशिकी अवलोकन](overview.hi.md)
