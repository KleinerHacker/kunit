# चुंबकीय क्षेत्र तीव्रता

पैकेज: `org.pcsoft.framework.kunit.electric.magneticfieldstrength`
आधार इकाई: **ऐम्पियर प्रति मीटर** (`KMagneticFieldStrengthUnit.BASE == KMagneticFieldStrengthUnit.AMPERE_PER_METER`)

प्रकार: **रचित इकाई**

चुंबकीय क्षेत्र तीव्रता (चुंबकनकारी क्षेत्र `H`) एक **रचित** इकाई है: संयोजन `current · length⁻¹` (`A/m`)।
`KMagneticFieldStrengthUnitInstance` दो पदों वाले `KMixedUnitInstance` को लपेटता है — घातांक `+1` पर
`KElectricCurrentUnit.BASE` (ऐम्पियर) और घातांक `-1` पर `KDistanceUnit.BASE` (मीटर)। संग्रहीत मान सदैव
ऐम्पियर प्रति मीटर में सामान्यीकृत रहता है।

संबंधित पृष्ठ: [विद्युत धारा](ec.md) और [दूरी](../kinematics/distance.md) इस इकाई के दो घटक समूह हैं।

## चुंबकीय क्षेत्र तीव्रता बनाना

क्षेत्र तीव्रता किसी नामित टोकन से, या किसी अपघटन से (नीचे देखें) बनाई जाती है। नामित इकाइयाँ मान‑1 टोकन के
रूप में उपलब्ध रहती हैं (`of`/`into` के साथ प्रयुक्त):

| चुंबकीय क्षेत्र तीव्रता | प्रतीक | टोकन | 1 इकाई A/m में |
|---|---|---:|---:|
| ऐम्पियर प्रति मीटर | `A/m` | `amperesPerMeter` | 1.0 |
| ओर्स्टेड (CGS-EMU) | `Oe` | `oersteds` | 79.57747154594767 |
| गिल्बर्ट प्रति सेंटीमीटर | `Gb/cm` | `gilbertsPerCentimeter` | 79.57747154594767 |
| ऐम्पियर‑टर्न प्रति इंच | `At/in` | `ampereTurnsPerInch` | 39.37007874015748 |

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`kilo.amperesPerMeter`,
`milli.oersteds` आदि)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.*

val h = 470 of amperesPerMeter
h into amperesPerMeter                  // 470.0
h into kilo.amperesPerMeter             // 0.47
(1 of kilo.amperesPerMeter) into amperesPerMeter // 1000.0
```

## अनेक अपघटन

चुंबकीय क्षेत्र तीव्रता कई **समतुल्य अपघटनों** से प्राप्त की जा सकती है, जो सभी मान‑समान परिणाम देते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `current / length` | `KMagneticFieldStrengthUnitInstance` | परिभाषक संबंध `H = I / l` |
| `current·length⁻¹` | `.toMagneticFieldStrength()` द्वारा | मूल विहित `A·m⁻¹` व्यंजक |

टाइप‑युक्त संकारक रूप सीधे क्षेत्र तीव्रता लौटाता है। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही रहता
है और `toMagneticFieldStrength()` से संकुचित किया जाता है (जो केवल विहित सामान्य रूप पहचानता है, अन्यथा
`IllegalStateException` फेंकता है)। दोनों मार्ग मान‑समान हैं।

व्युत्क्रम संकारक धारा, लंबाई और क्षेत्र तीव्रता को जोड़ते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `fieldStrength * length` | `KElectricCurrentUnitInstance` | `I = H · l` |
| `length * fieldStrength` | `KElectricCurrentUnitInstance` | क्रमविनिमेय रूप |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.*

// वास्तविक उदाहरण - 500 फेरों वाली कुंडली में 2 A धारा, लंबाई 0.25 m:
// H = N · I / l = 500 · 2 A / 0.25 m = 4000 A/m
val h = (1000 of amperes) / (0.25 of meters)  // KMagneticFieldStrengthUnitInstance, 4000 A/m

// वही क्षेत्र तीव्रता मूल A·m⁻¹ व्यंजक के रूप में:
val raw = 4000 of (amperes pow 1) / (meters pow 1)
raw.toMagneticFieldStrength() == (4000 of amperesPerMeter)  // true
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.*

val s = (100 of amperesPerMeter) + (40 of amperesPerMeter)  // 140 A/m
(100 of amperesPerMeter) > (40 of amperesPerMeter)          // true
(100 of amperesPerMeter) * (40 of amperesPerMeter)          // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.*

(470 of amperesPerMeter).toString()     // "470.0 A/m" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि यह इकाई और इसके घटक गणितीय रूप में कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `A/m` | `amperesPerMeter` | चुंबकीय क्षेत्र तीव्रता, आधार इकाई (नामित टोकन) |
| `A/m` | `(amperes pow 1) / (meters pow 1)` | धारा / लंबाई के रूप में क्षेत्र तीव्रता (भिन्न रूप) |
| `A·m⁻¹` | `(amperes pow 1) * (meters pow -1)` | वही क्षेत्र तीव्रता शुद्ध गुणनफल के रूप में |
| `kA/m` | `kilo.amperesPerMeter` | उपसर्ग सहित क्षेत्र तीव्रता (किलोऐम्पियर प्रति मीटर) |
