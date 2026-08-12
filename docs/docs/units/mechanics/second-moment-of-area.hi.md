# क्षेत्रफल का द्वितीय आघूर्ण (Second Moment of Area)

पैकेज: `org.pcsoft.framework.kunit.kinematic.distance`
मूल इकाई: **मीटर की चतुर्थ घात** (`m⁴`, distance समूह की घातांक-4 शाखा)

प्रकार: **संरचित इकाई**

क्षेत्रफल का द्वितीय आघूर्ण `I` (area moment of inertia) वह ज्यामितीय गुण है जो यह तय करता है कि
किसी बीम का क्रॉस-सेक्शन झुकाव (bending) में कितना कठोर है — यह झुकाव कठोरता `EI` में `I` है।
स्टील प्रोफ़ाइल तालिकाओं में इसे `cm⁴` में दर्शाया जाता है, जबकि छोटे खंडों के लिए `mm⁴` में।

इस साइट के अन्य समूहों के विपरीत, यह अपना अलग समूह नहीं है: यह distance समूह की **घातांक-4
शाखा** है, अर्थात `KSecondMomentOfAreaUnitInstance`, जो [लंबाई](../kinematics/distance.hi.md)
(घातांक 1), क्षेत्रफल (घातांक 2), और आयतन (घातांक 3) के साथ स्थित है।

!!! warning "यह जड़त्व आघूर्ण नहीं है"
    इसे *द्रव्यमान* आधारित [जड़त्व आघूर्ण](moment-of-inertia.hi.md) (`kg·m²`) के साथ भ्रमित न करें,
    जो कोणीय त्वरण के प्रतिरोध का वर्णन करता है। नाम समान हैं, लेकिन आयाम भिन्न हैं।

## नामित टोकन

| इकाई                    | प्रतीक |                टोकन | m⁴ में 1 इकाई |
|---------------------------|--------|---------------------:|-------------:|
| मीटर की चतुर्थ घात          | `m⁴`   |       `quarticMeters` |          1.0 |
| सेंटीमीटर की चतुर्थ घात     | `cm⁴`  |  `quarticCentimeters` |         1e-8 |
| मिलीमीटर की चतुर्थ घात      | `mm⁴`  |  `quarticMillimeters` |        1e-12 |
| इंच की चतुर्थ घात           | `in⁴`  |       `quarticInches` | ≈ 4.16231e-7 |

सभी टोकन हर SI उपसर्ग को स्वीकार करते हैं।

## शाखा के साथ गणना

घातांक 4 पर पहुँचने वाला हर गुणनफल अब सामान्य `KDistanceUnitInstance` के बजाय टाइप किया गया
शाखा-रूप लौटाता है:

| अभिव्यक्ति                    | परिणाम प्रकार                            | अर्थ                        |
|----------------------------------|------------------------------------------------|--------------------------------|
| `area * area`                   | `KSecondMomentOfAreaUnitInstance`             | m² · m² = m⁴                   |
| `volume * length`               | `KSecondMomentOfAreaUnitInstance`             | m³ · m = m⁴                    |
| `length * volume`               | `KSecondMomentOfAreaUnitInstance`             | m · m³ = m⁴                    |
| `secondMomentOfArea / length`   | `KVolumeUnitInstance`                         | अनुभाग मापांक (section modulus) |
| `secondMomentOfArea / area`     | `KAreaUnitInstance`                           | m⁴/m² = m²                     |
| `secondMomentOfArea / volume`   | `KLengthUnitInstance`                         | m⁴/m³ = m                      |
| `secondMomentOfArea + …`        | `KSecondMomentOfAreaUnitInstance`             | संयुक्त अनुभाग के भाग           |

जोड़ केवल समान आयाम तक सीमित है — `secondMomentOfArea + area`, ठीक `length + area` की तरह,
एक **कंपाइल त्रुटि** है।

नेटिव रूप `toSecondMomentOfArea()` से परिवर्तित होता है:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*

val native = ((1 of centi.meters).toUnit() pow 4).toSecondMomentOfArea()
native into quarticCentimeters      // 1.0
```

## वास्तविक उदाहरण — एक आयताकार बीम

चौड़ाई `b` और ऊँचाई `h` के आयत के लिए, `I = b·h³/12`। 100 mm × 200 mm के लिए:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*

val b = 100 of milli.meters
val h = 200 of milli.meters

val i = (b * (h * h * h)) / 12       // KSecondMomentOfAreaUnitInstance
i into quarticCentimeters             // ≈ 6666.7 cm⁴

// अनुभाग मापांक W = I / (h/2)
val w = i / (h / 2)                   // KVolumeUnitInstance
w.value                                // ≈ 6.667e-4 m³

// संयुक्त अनुभाग: ऐसी दो बीम पास-पास
val doubled = i + i
doubled into quarticCentimeters        // ≈ 13333.3
```

## मान अर्थशास्त्र (Value Semantics)

`equals`/`hashCode` और तुलना सामान्यीकृत `m⁴` मान पर, समान आयाम तक सीमित होकर कार्य करते हैं।
`exponent` `4` लौटाता है।

## यह भी देखें

* [दूरी](../kinematics/distance.hi.md) — वह समूह जिससे यह शाखा संबंधित है।
* [जड़त्व आघूर्ण](moment-of-inertia.hi.md) — समान नाम वाली *द्रव्यमान*-आधारित राशि।
* [यांत्रिकी अवलोकन](overview.hi.md)
