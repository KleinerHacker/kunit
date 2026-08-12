# ज्योति तीव्रता (Luminous Intensity)

पैकेज: `org.pcsoft.framework.kunit.optic.luminousintensity`
मूल इकाई: **कैंडेला** (`KLuminousIntensityUnit.BASE == KLuminousIntensityUnit.CANDELA`)

प्रकार: **मूल इकाई (native unit)**

ज्योति तीव्रता `I` वह ज्योति फ्लक्स है जिसे कोई स्रोत किसी दिशा में **प्रति ठोस कोण** उत्सर्जित करता है। इसकी इकाई,
कैंडेला, **सातवीं SI मूल इकाई** है — और मानव अनुभूति के माध्यम से परिभाषित एकमात्र इकाई: 1 cd उस स्रोत की तीव्रता है
जो उस दिशा में 540 THz की एकवर्णी विकिरण को 1/683 W/sr की विकिरण तीव्रता के साथ उत्सर्जित करता है।

यह समूह एक **सरल, एक-आयामी** मूल समूह है (कोई घातांक-विशिष्ट उपप्रकार नहीं):
`KLuminousIntensityUnitInstance` एक ही `KLuminousIntensityUnit.CANDELA` पद को लपेटता है, जो हमेशा कैंडेला में
सामान्यीकृत रहकर संग्रहीत होता है।

## इकाइयाँ

| इकाई          | Enum मान                                   | संकेत    |          टोकन | 1 इकाई = ? कैंडेला |
|---------------|---------------------------------------------|----------|---------------:|-------------------:|
| कैंडेला       | `KLuminousIntensityUnit.CANDELA`             | `cd`     |     `candelas` |                1.0 |
| हेफनर कैंडल   | `KLuminousIntensityUnit.HEFNER_CANDLE`       | `HK`     | `hefnerCandles` |              0.903 |
| कैंडलपावर     | `KLuminousIntensityUnit.CANDLEPOWER`         | `cp`     |  `candlepower` |              0.981 |
| कार्सेल       | `KLuminousIntensityUnit.CARCEL`              | `carcel` |      `carcels` |               9.74 |

तीन गैर-SI प्रविष्टियाँ कैंडेला से पहले प्रचलित ऐतिहासिक राष्ट्रीय मानक हैं — जर्मन हेफनर लैंप, ब्रिटिश अंतरराष्ट्रीय
कैंडल और फ़्रांसीसी कार्सेल तेल लैंप। इन्हें इसलिए रखा गया है ताकि पुराने डेटाशीट सीधे पढ़े जा सकें।

प्रत्येक टोकन एक मान-1 `KLuminousIntensityUnitInstance` है जिसे `of` (निर्माण) और `into` (पठन) के साथ उपयोग किया
जाता है। सभी टोकन प्रत्येक SI उपसर्ग स्वीकार करते हैं (`milli.candelas`, `kilo.candelas`, …)।

## समूह के साथ गणना

| व्यंजक                            | परिणाम प्रकार                     | अर्थ                                |
|------------------------------------|------------------------------------|-------------------------------------|
| `luminousIntensity + …`            | `KLuminousIntensityUnitInstance`   | समान-प्रकार जोड़              |
| `luminousIntensity * solidAngle`   | `KLuminousFluxUnitInstance`        | `Φ = I · Ω`, उत्सर्जित फ्लक्स       |
| `luminousIntensity / area`         | `KLuminanceUnitInstance`           | `L = I / A`, सतह की चमक             |
| `luminousFlux / solidAngle`        | `KLuminousIntensityUnitInstance`   | फ्लक्स से वापस                       |

मूल रूप `toLuminousIntensity()` से परिवर्तित होता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.optic.luminousintensity.*

val raw = (1200 of candelas).toUnit()   // KMixedUnitInstance
raw.toLuminousIntensity() into candelas // 1200.0
```

## वास्तविक उदाहरण — एक कार हेडलाइट

एक लो-बीम हेडलाइट अपनी प्रकाशिक धुरी पर **1200 cd** निर्दिष्ट है। 0.05 sr शंकु पर फैलाने पर, यह सड़क पर वास्तव में
लक्षित ज्योति फ्लक्स है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.*
import org.pcsoft.framework.kunit.optic.luminousflux.*

val i = 1200 of candelas
i into kilo.candelas                     // 1.2

val beam = i * (0.05 of steradians)      // KLuminousFluxUnitInstance
beam into lumens                         // बीम शंकु में 60.0 lm
```

## मान अर्थ-विज्ञान (Value Semantics)

`equals`/`hashCode` **सामान्यीकृत कैंडेला मान** की तुलना करते हैं, इसलिए `(1 of candelas) == (1000 of milli.candelas)`।
`toString()` मान को मूल इकाई में प्रस्तुत करता है: `"1200.0 cd"`।

## संबंधित देखें

* [ज्योति फ्लक्स](luminous-flux.hi.md) — ठोस कोण पर समाकलित तीव्रता।
* [चमकता (Luminance)](luminance.hi.md) — उत्सर्जक क्षेत्रफल प्रति तीव्रता।
* [विकिरण तीव्रता](radiant-intensity.hi.md) — विकिरणमितीय समकक्ष, आँख द्वारा अभारित।
* [प्रकाशिकी अवलोकन](overview.hi.md)
