# तरंग संख्या (Wavenumber)

पैकेज: `org.pcsoft.framework.kunit.common.reciprocallength`
मूल इकाई: **मीटर का व्युत्क्रम** (`KReciprocalLengthUnit.BASE == KReciprocalLengthUnit.RECIPROCAL_METER`)

प्रकार: **संरचित इकाई**

किसी तरंग की तरंग संख्या `ṽ` उसकी तरंगदैर्घ्य का व्युत्क्रम है: `ṽ = 1 / λ` — यानी प्रति इकाई लंबाई
में तरंग चक्रों की संख्या। स्पेक्ट्रोस्कोपी इसे तरंगदैर्घ्य के बजाय उपयोग करती है क्योंकि यह फोटॉन ऊर्जा के
समानुपाती होती है, और यह लगभग हमेशा **प्रति सेंटीमीटर व्युत्क्रम** (`cm⁻¹`, ऐतिहासिक रूप से *kayser*
कहलाता है) में उद्धृत की जाती है: दृश्य प्रकाश लगभग 14,000–25,000 cm⁻¹ के बीच फैला होता है, और
इन्फ्रारेड फिंगरप्रिंट क्षेत्र 400–1500 cm⁻¹ है।

इसका आयाम `distance⁻¹` है — जो किसी लेंस की अपवर्तन शक्ति, [डायोप्टर](../optics/dioptre.hi.md), के
**समान** है। KUnit दोनों पठनों के लिए एक ही तटस्थ समूह `reciprocallength` को मॉडल करता है; तरंग
संख्या उनमें से एक है। यह पृष्ठ उसी पठन का दस्तावेज़ीकरण करता है।

!!! note "एक समूह, दो पठन"
    `KReciprocalLengthUnitInstance` साझा प्रकार है, इसलिए KUnit की दृष्टि से एक तरंग संख्या और
    एक अपवर्तन शक्ति समान इकाई हैं। यह समूह तटस्थ नाम `reciprocallength` रखता है ताकि कोई भी पठन
    दूसरे के नाम पर दावा न करे। अपने मानों को नाम देकर उन्हें अलग करें।

## नामित इकाइयाँ

| इकाई                    | प्रतीक |                   टोकन | m⁻¹ में 1 इकाई |
|---------------------------|--------|------------------------:|--------------:|
| मीटर का व्युत्क्रम           | `1/m`  |      `reciprocalMeters` |           1.0 |
| सेंटीमीटर का व्युत्क्रम       | `1/cm` | `reciprocalCentimeters` |         100.0 |
| कायज़र (Kayser)             | `1/cm` |                `kaysers` |         100.0 |
| डायोप्टर                    | `dpt`  |               `dioptres` |           1.0 |

सभी टोकन हर SI उपसर्ग को स्वीकार करते हैं (`kilo.reciprocalCentimeters` आदि)।

## समूह के साथ गणना

| अभिव्यक्ति                    | परिणाम प्रकार                            | अर्थ                             |
|----------------------------------|------------------------------------------------|--------------------------------------|
| `1 / length`                    | `KReciprocalLengthUnitInstance`               | `ṽ = 1 / λ`                         |
| `1 / reciprocalLength`          | `KLengthUnitInstance`                         | वापस तरंगदैर्घ्य में                  |
| `reciprocalLength * length`     | `Double`                                      | विमाहीन चक्र संख्या                   |
| `reciprocalLength + …`          | `KReciprocalLengthUnitInstance`               | समान-प्रकार जोड़                     |

नेटिव रूप `toReciprocalLength()` से परिवर्तित होता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val native = (100 of ((1 of meters).toUnit() pow -1)).toReciprocalLength()
native into reciprocalCentimeters      // 1.0
```

## वास्तविक उदाहरण — हरी लेज़र रोशनी

500 nm की लेज़र रेखा 20,000 cm⁻¹ की तरंग संख्या में परिवर्तित होती है, और 1 mm पथ में समाने वाले
चक्रों की संख्या सीधे इससे निकलती है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val k = 1 / (500 of nano.meters)       // KReciprocalLengthUnitInstance
k into reciprocalCentimeters            // 20_000.0
k into kaysers                          // 20_000.0 (वही इकाई, शास्त्रीय नाम)

val cycles = k * (1 of milli.meters)    // Double
cycles                                   // 2000.0 — प्रति मिलीमीटर तरंग चक्र

val lambda = 1 / k                       // KLengthUnitInstance
lambda into nano.meters                  // 500.0
```

## मान अर्थशास्त्र (Value Semantics)

`equals`/`hashCode` **सामान्यीकृत m⁻¹ मान** की तुलना करते हैं, इसलिए
`(1 of reciprocalCentimeters) == (100 of reciprocalMeters)`। `toString()` मूल इकाई में मान
प्रदर्शित करता है: `"2000000.0 1/m"`।

## यह भी देखें

* [डायोप्टर](../optics/dioptre.hi.md) — अपवर्तन शक्ति के रूप में पढ़ा गया वही प्रकार।
* [आवृत्ति](../kinematics/frequency.hi.md) — समय का व्युत्क्रम, इस समूह का कालिक समानांतर।
* [यांत्रिकी अवलोकन](overview.hi.md)
