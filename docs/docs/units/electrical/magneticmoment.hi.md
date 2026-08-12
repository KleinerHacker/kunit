# चुंबकीय द्विध्रुव आघूर्ण

पैकेज: `org.pcsoft.framework.kunit.electric.magneticmoment`
आधार इकाई: **एम्पियर वर्ग मीटर**
(`KMagneticMomentUnit.BASE == KMagneticMomentUnit.AMPERE_SQUARE_METER`)

प्रकार: **संरचित इकाई**

एक धारा-लूप का चुंबकीय द्विध्रुव आघूर्ण `m`, धारा और उसके द्वारा घिरे क्षेत्रफल का गुणनफल है: `m = I · A`।
यह वह राशि है जो निर्धारित करती है कि चुंबकीय क्षेत्र लूप पर कितना बलाघूर्ण लगाता है, और परमाणु व नाभिकीय चुंबकत्व
(बोर मैग्नेटन और नाभिकीय मैग्नेटन) इसी में व्यक्त किए जाते हैं।

इसका विहित आधार-आयाम मानक रूप `current · length²` है।

## नामित इकाइयाँ

| इकाई                | प्रतीक  |                टोकन |   1 इकाई A·m² में |
|---------------------|---------|---------------------:|-------------------:|
| एम्पियर वर्ग मीटर   | `A*m^2` | `ampereSquareMeters` |                1.0 |
| जूल प्रति टेस्ला    | `J/T`   |      `joulesPerTesla` |                1.0 |
| बोर मैग्नेटन        | `μB`    |       `bohrMagnetons` | 9.2740100783e-24   |
| नाभिकीय मैग्नेटन    | `μN`    |    `nuclearMagnetons` | 5.0507837461e-27   |

`joulesPerTesla` उसी इकाई की ऊर्जा-आधारित वर्तनी है — जो ऊर्जा एक द्विध्रुव चुंबकीय फ्लक्स घनत्व की प्रति इकाई प्राप्त करता है।
सभी टोकन हर SI उपसर्ग स्वीकार करते हैं।

## अपघटन

इस समूह का एक अपघटन है, और इसके दोनों रूप एक ही मान-समान टाइप्ड इंस्टेंस बनाते हैं:

| रूप              | व्यंजक                                                       |
|------------------|-------------------------------------------------------------------|
| टाइप्ड ऑपरेटर    | `current * area`                                                 |
| मूल (`toX()`)    | `((2 of amperes).toUnit() * loop.toUnit()).toMagneticMoment()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.magneticmoment.*

val loop = (0.1 of meters) * (0.05 of meters)      // 0.005 m²

val typed = (2 of amperes) * loop
val native = ((2 of amperes).toUnit() * loop.toUnit()).toMagneticMoment()

typed == native                 // true
typed into ampereSquareMeters   // 0.01
```

## समूह के साथ गणना

| व्यंजक                       | परिणाम प्रकार                      | अर्थ          |
|-----------------------------|-----------------------------------|------------------|
| `current * area`            | `KMagneticMomentUnitInstance`    | `m = I · A`      |
| `magneticMoment / area`     | `KElectricCurrentUnitInstance`   | लूप धारा |
| `magneticMoment / current`  | `KAreaUnitInstance`              | लूप का क्षेत्रफल    |

## वास्तविक उदाहरण — एक कुंडली लूप और एक परमाणु

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.magneticmoment.*

val loop = (0.1 of meters) * (0.05 of meters)
val m = (2 of amperes) * loop
m into ampereSquareMeters          // 0.01

// यह कितने बोर मैग्नेटन के बराबर है?
m into bohrMagnetons                // ≈ 1.078e21

// और उल्टा: 1 cm² के लूप को 1 A·m² के लिए कितनी धारा चाहिए?
val small = (0.01 of meters) * (0.01 of meters)
((1 of ampereSquareMeters) / small) into amperes   // 10 000 A
```

## मान शब्दार्थ

`equals`/`hashCode` **सामान्यीकृत A·m² मान** की तुलना करते हैं, इसलिए
`(1 of ampereSquareMeters) == (1 of joulesPerTesla)` सत्य है। `toString()` मान को आधार इकाई में दर्शाता है:
`"0.01 A*m^2"`।

## यह भी देखें

* [चुंबकीय फ्लक्स घनत्व](magneticfluxdensity.hi.md) — वह क्षेत्र जिससे यह आघूर्ण अन्योन्यक्रिया करता है।
* [विद्युत धारा](ec.hi.md) और [दूरी](../kinematics/distance.hi.md) — दो गुणनखंड।
* [इलेक्ट्रिकल इंजीनियरिंग अवलोकन](overview.hi.md)
