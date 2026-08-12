# विशिष्ट ध्वनिक प्रतिबाधा

पैकेज: `org.pcsoft.framework.kunit.mechanic.acousticimpedance`
मूल इकाई: **पास्कल सेकंड प्रति मीटर**
(`KAcousticImpedanceUnit.BASE == KAcousticImpedanceUnit.PASCAL_SECOND_PER_METER`)

प्रकार: **संरचित इकाई**

विशिष्ट ध्वनिक प्रतिबाधा `Z` वह ध्वनि दाब है जो एक माध्यम कण वेग की प्रति इकाई उत्पन्न करता है:
`Z = p / v = ρ · c`। यह तय करती है कि किसी सीमा पर कितनी ध्वनि परावर्तित होती है — हवा की प्रतिबाधा
लगभग 413 Pa·s/m है, जबकि पानी की लगभग 1.48 MPa·s/m, अर्थात लगभग 3600 का अनुपात, यही कारण है कि
वायुजनित ध्वनि लगभग कभी पानी में प्रवेश नहीं करती।

इसका विहित आधार-आयाम मानक रूप `mass · length⁻² · time⁻¹` है।

## नामित इकाइयाँ

| इकाई                     | प्रतीक       |                   टोकन | Pa·s/m में 1 इकाई |
|---------------------------|--------------|------------------------:|-----------------:|
| पास्कल सेकंड प्रति मीटर      | `Pa*s/m`     | `pascalSecondsPerMeter` |              1.0 |
| SI रेल                     | `rayl`       |                 `rayls` |              1.0 |
| CGS रेल                    | `rayl (CGS)` |              `cgsRayls` |               10 |

`rayls` मूल इकाई की एक दूसरी वर्तनी है, न कि अपनी अलग इकाई। सभी टोकन हर SI उपसर्ग को स्वीकार
करते हैं (`mega.rayls` ऊतक और पानी के लिए सामान्य रूप से उपयोग होता है)। पड़ोसी बल, दाब और घनत्व
समूहों की तरह, यह इंस्टेंस अपना **कच्चा ग्राम-आधारित घटक मान** संग्रहीत करता है।

## विघटन

इस समूह में **दो** विघटन हैं। दोनों एक ही सामान्यीकरण फैक्ट्री में मिलते हैं:

| रूप                    | अभिव्यक्ति                                                     |
|-------------------------|----------------------------------------------------------------|
| टाइप किया गया ऑपरेटर A  | `pressure / speed`                                             |
| टाइप किया गया ऑपरेटर B  | `density * speed` (`Z = ρ · c`, विशेषता प्रतिबाधा)              |
| नेटिव रूप (`toX()`)     | `(1 of kilo.grams / m² / s).toAcousticImpedance()`              |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.acousticimpedance.*

val air = (1.204 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val c = (343 of meters) / (1 of seconds)

val viaDensity = air * c                                        // B
val viaPressure = (412.972 of pascals) / ((1 of meters) / (1 of seconds))  // A

viaDensity into rayls        // ≈ 412.97
viaPressure into rayls       // ≈ 412.97
```

## समूह के साथ गणना

| अभिव्यक्ति                          | परिणाम प्रकार                          | अर्थ                    |
|----------------------------------------|--------------------------------------------|---------------------------|
| `pressure / speed`                    | `KAcousticImpedanceUnitInstance`         | `Z = p / v`               |
| `density * speed`                     | `KAcousticImpedanceUnitInstance`         | `Z = ρ · c`               |
| `acousticImpedance * speed`           | `KPressureUnitInstance`                  | ध्वनि दाब                   |
| `pressure / acousticImpedance`        | `KSpeedUnitInstance`                     | कण वेग                     |
| `acousticImpedance / speed`           | `KDensityUnitInstance`                   | वापस `ρ` में                |
| `acousticImpedance / density`         | `KSpeedUnitInstance`                     | वापस `c` में                |

## वास्तविक उदाहरण — वायु/जल सीमा

पानी के भीतर तैराक के सिर की ओर चिल्लाना क्यों काम नहीं करता? दोनों विशेषता प्रतिबाधाओं की तुलना
करें:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.acousticimpedance.*

val air = (1.204 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

val zAir = air * ((343 of meters) / (1 of seconds))
val zWater = water * ((1480 of meters) / (1 of seconds))

zAir into rayls              // ≈ 413
zWater into mega.rayls       // ≈ 1.48

(zWater into rayls) / (zAir into rayls)   // ≈ 3584 — लगभग पूर्ण परावर्तन
```

## मान अर्थशास्त्र (Value Semantics)

`equals`/`hashCode` **सामान्यीकृत घटक मान** की तुलना करते हैं, इसलिए `(1 of cgsRayls) == (10 of rayls)`।
`toString()` मूल इकाई में मान प्रदर्शित करता है: `"413.0 Pa*s/m"`।

## यह भी देखें

* [घनत्व](density.hi.md) और [चाल](../kinematics/speed.hi.md) — `Z = ρ · c` के दो कारक।
* [दाब](pressure.hi.md) — ध्वनि दाब वाला पक्ष।
* [यांत्रिकी अवलोकन](overview.hi.md)
