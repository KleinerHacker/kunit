# डायोप्टर (अपवर्तक शक्ति)

पैकेज: `org.pcsoft.framework.kunit.common.reciprocallength`
मूल इकाई: **व्युत्क्रम मीटर** (`KReciprocalLengthUnit.BASE == KReciprocalLengthUnit.RECIPROCAL_METER`)

प्रकार: **संघटित इकाई**

किसी लेंस की अपवर्तक शक्ति `D` उसकी फोकल लंबाई का व्युत्क्रम है: `D = 1 / f`। इसकी इकाई **डायोप्टर** है, जो ठीक व्युत्क्रम
मीटर के बराबर है — 1 m पर फोकस करने वाले लेंस की शक्ति 1 dpt है, 0.5 m पर फोकस करने वाले की 2 dpt।

इसका आयाम `distance⁻¹` है — जो स्पेक्ट्रोस्कोपी की [तरंग संख्या](../mechanics/wavenumber.md) के **समान** है। KUnit दोनों
पठनों के लिए एक ही तटस्थ समूह `reciprocallength` मॉडल करता है; अपवर्तक शक्ति उनमें से एक है। यह पृष्ठ उसी पठन का
दस्तावेज़ीकरण करता है।

!!! note "एक समूह, दो पठन"
    `KReciprocalLengthUnitInstance` साझा प्रकार है, इसलिए जहाँ तक KUnit का संबंध है, अपवर्तक शक्ति और तरंग संख्या एक ही
    इकाई हैं। समूह का तटस्थ नाम `reciprocallength` है ताकि कोई भी पठन दूसरे का नाम न ले ले। इन्हें अपने मानों को नाम
    देकर अलग करें।

## नामित इकाइयाँ

| इकाई                    | प्रतीक |                    टोकन | m⁻¹ में 1 इकाई |
|--------------------------|--------|------------------------:|---------------:|
| व्युत्क्रम मीटर            | `1/m`  |      `reciprocalMeters` |            1.0 |
| डायोप्टर                  | `dpt`  |              `dioptres` |            1.0 |
| व्युत्क्रम सेंटीमीटर        | `1/cm` | `reciprocalCentimeters` |          100.0 |
| कायसर                    | `1/cm` |                `kaysers` |          100.0 |

`dioptres` और `kaysers` क्रमशः व्युत्क्रम मीटर और व्युत्क्रम सेंटीमीटर के वैकल्पिक वर्तनी हैं, न कि अपनी स्वयं की
इकाइयाँ। सभी टोकन हर SI उपसर्ग स्वीकार करते हैं (`milli.dioptres`, …)।

## समूह के साथ गणना

| अभिव्यक्ति                     | परिणाम प्रकार                   | अर्थ                              |
|----------------------------------|----------------------------------|-----------------------------------|
| `1 / length`                     | `KReciprocalLengthUnitInstance`  | `D = 1 / f`                      |
| `1 / reciprocalLength`           | `KLengthUnitInstance`            | वापस फोकल लंबाई तक                |
| `reciprocalLength + …`           | `KReciprocalLengthUnitInstance`  | संपर्क में पतले लेंस शक्ति जोड़ते हैं |
| `reciprocalLength * length`      | `Double`                         | विमाहीन गणना (`m⁻¹ · m`)          |

नेटिव रूप `toReciprocalLength()` से रूपांतरित होता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val native = (2.5 of ((1 of meters).toUnit() pow -1)).toReciprocalLength()
native into dioptres      // 2.5
```

## वास्तविक उदाहरण — रीडिंग चश्मा

**40 cm** फोकल लंबाई वाला एक लेंस `D = 1 / 0.4 m = 2.5 dpt` देता है। उसके संपर्क में एक दूसरा, कमज़ोर लेंस रखने से शक्तियाँ
बस जुड़ जाती हैं — जो ठीक वही है जो समान-प्रकार `+` करता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val d = 1 / (40 of centi.meters)     // KReciprocalLengthUnitInstance
d into dioptres                       // 2.5

val combined = d + (1.5 of dioptres)  // संपर्क में लेंस
combined into dioptres                // 4.0

val f = 1 / combined                  // KLengthUnitInstance
f into centi.meters                   // 25.0 — संयुक्त फोकल लंबाई
```

## मान अर्थ-विज्ञान

`equals`/`hashCode` **सामान्यीकृत m⁻¹ मान** की तुलना करते हैं, इसलिए
`(1 of reciprocalCentimeters) == (100 of reciprocalMeters)`। `toString()` मान को मूल इकाई में प्रस्तुत करता है:
`"2.5 1/m"`।

## यह भी देखें

* [तरंग संख्या](../mechanics/wavenumber.md) — वही प्रकार, स्पेक्ट्रोस्कोपिक राशि के रूप में पढ़ा गया।
* [दूरी](../kinematics/distance.md) — वह समूह जिसका यह व्युत्क्रम है।
* [प्रकाशिकी अवलोकन](overview.hi.md)
