# विकिरण तीव्रता (Radiant Intensity)

पैकेज: `org.pcsoft.framework.kunit.optic.radiantintensity`
मूल इकाई: **वाट प्रति स्टेरेडियन** (`KRadiantIntensityUnit.BASE == KRadiantIntensityUnit.WATT_PER_STERADIAN`)

प्रकार: **संघटित इकाई (constructed unit)**

विकिरण तीव्रता `Iₑ` वह विकिरण फ्लक्स (शक्ति) है जिसे कोई स्रोत **प्रति ठोस कोण** उत्सर्जित करता है: `Iₑ = P / Ω`। यह
[ज्योति तीव्रता](luminous-intensity.hi.md) का **विकिरणमितीय (radiometric)** समकक्ष है — वही ज्यामिति, लेकिन लुमेन
के बजाय वाट में मापी गई, इसलिए यह अवरक्त और पराबैंगनी सहित सारी विकिरण गिनती है जिसे आँख नहीं देख सकती।

इसका विहित आधार-आयाम सामान्य रूप `mass¹ · distance² · time⁻³ · solidAngle⁻¹` है।

## इकाइयाँ

| इकाई                 | Enum मान                                      | संकेत  |               टोकन | 1 इकाई = ? W/sr |
|-----------------------|--------------------------------------------------|--------|--------------------:|---------------:|
| वाट प्रति स्टेरेडियन  | `KRadiantIntensityUnit.WATT_PER_STERADIAN`       | `W/sr` | `wattsPerSteradian` |            1.0 |

यह टोकन प्रत्येक SI उपसर्ग स्वीकार करता है (`milli.wattsPerSteradian`, `kilo.wattsPerSteradian`, …)।

## अपघटन

समूह का एक अपघटन है, और इसके दोनों रूप समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं। मूल रूप **यूनिट टेम्पलेट्स** से
जोड़ा गया है क्योंकि समूह में एक द्रव्यमान (mass) पद है (देखें
[ज्योति दक्षता](luminous-efficacy.hi.md) में यही टिप्पणी)।

| रूप                | व्यंजक                                                              |
|---------------------|-------------------------------------------------------------------------|
| टाइप किया गया संकारक | `power / solidAngle`                                                    |
| मूल (`toX()`)        | `(5 of kilo.grams · m² / s³ / sr).toRadiantIntensity()`                 |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.radiantintensity.*

val typed = (20 of watts) / (4 of steradians)
val native = (
    5 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / steradians.toUnit()
).toRadiantIntensity()

typed == native                 // true
typed into wattsPerSteradian    // 5.0
```

## समूह के साथ गणना

| व्यंजक                          | परिणाम प्रकार                       | अर्थ                          |
|------------------------------------|---------------------------------------|-------------------------------|
| `power / solidAngle`               | `KRadiantIntensityUnitInstance`       | `Iₑ = P / Ω`                  |
| `radiantIntensity * solidAngle`    | `KPowerUnitInstance`                  | `P = Iₑ · Ω`                  |
| `power / radiantIntensity`         | `KSolidAngleUnitInstance`             | जिस शंकु में यह फैला है       |
| `radiantIntensity / area`          | `KRadianceUnitInstance`               | `Lₑ = Iₑ / A`                 |

## वास्तविक उदाहरण — एक अवरक्त (इन्फ्रारेड) LED

एक IR उत्सर्जक 0.2 sr शंकु में **20 mW** विकिरण करता है। इसकी विकिरण तीव्रता, और 0.05 sr डिटेक्टर एपर्चर द्वारा
पकड़ी गई शक्ति:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.radiantintensity.*

val i = (20 of milli.watts) / (0.2 of steradians)
i into milli.wattsPerSteradian       // 100.0

val caught = i * (0.05 of steradians)  // KPowerUnitInstance
caught into milli.watts                // डिटेक्टर तक 5.0 mW पहुँचती है
```

## मान अर्थ-विज्ञान (Value Semantics)

`equals`/`hashCode` **सामान्यीकृत W/sr मान** की तुलना करते हैं, इसलिए
`(1 of wattsPerSteradian) == (1000 of milli.wattsPerSteradian)`। `toString()` मान को मूल इकाई में प्रस्तुत करता है:
`"5.0 W/sr"`।

## संबंधित देखें

* [ज्योति तीव्रता](luminous-intensity.hi.md) — प्रकाशमितीय समकक्ष।
* [रेडियंस](radiance.hi.md) — उत्सर्जक क्षेत्रफल प्रति विकिरण तीव्रता।
* [ज्योति दक्षता](luminous-efficacy.hi.md) — वाट और लुमेन के बीच सेतु।
* [प्रकाशिकी अवलोकन](overview.hi.md)
