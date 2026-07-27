# चालकता

पैकेज: `org.pcsoft.framework.kunit.conductivity`
आधार इकाई: **सीमेंस प्रति मीटर** (`KConductivityUnit.BASE == KConductivityUnit.SIEMENS_PER_METER`)

प्रकार: **संरचित इकाई**

विद्युत चालकता एक **संरचित** इकाई है: संयोजन `mass⁻¹ · length⁻³ · time³ · current²`
(`kg⁻¹·m⁻³·s³·A²`)। `KConductivityUnitInstance` चार पदों वाले `KMixedUnitInstance` को लपेटता है — घातांक
`-1` पर `KMassUnit.BASE` (ग्राम), घातांक `-3` पर `KDistanceUnit.BASE` (मीटर), घातांक `+3` पर
`KTimeUnit.BASE` (सेकंड) और घातांक `+2` पर `KElectricCurrentUnit.BASE` (ऐम्पियर)। चूँकि लाइब्रेरी का
द्रव्यमान घटक **ग्राम** में सामान्यीकृत है (किलोग्राम में नहीं) और द्रव्यमान घातांक *ऋणात्मक* है, विहित
गुणनफल को सीमेंस प्रति मीटर तक पहुँचने के लिए 1000 से गुणा किया जाता है; संग्रहीत मान हमेशा S/m में
सामान्यीकृत रहता है।

चालकता चालन के पीछे की पदार्थ गुण है और [प्रतिरोधकता](resistivity.md) की व्युत्क्रम (`σ = 1 / ρ`) है।

## चालकता बनाना

चालकता को नामित टोकन से या किसी अपघटन (नीचे देखें) से बनाया जाता है। नामित इकाइयाँ मान‑1 टोकन के रूप में
उपलब्ध रहती हैं (`of`/`into` के साथ प्रयोग):

| चालकता | संकेत | टोकन | 1 इकाई = ? S/m |
|---|---|---:|---:|
| सीमेंस प्रति मीटर | `S/m` | `siemensPerMeter` | 1.0 |
| सीमेंस प्रति सेंटीमीटर | `S/cm` | `siemensPerCentimeter` | 100.0 |
| माइक्रोसीमेंस प्रति सेंटीमीटर | `µS/cm` | `microsiemensPerCentimeter` | 1.0e-4 |
| मेगासीमेंस प्रति मीटर | `MS/m` | `megasiemensPerMeter` | 1.0e6 |

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`mega.siemensPerMeter`,
`milli.siemensPerMeter`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.conductivity.*

val sigma = 58 of mega.siemensPerMeter        // तांबा
sigma into mega.siemensPerMeter               // 58.0
sigma into siemensPerMeter                    // 5.8e7
(1 of siemensPerCentimeter) into siemensPerMeter // 100.0
```

## अनेक अपघटन

चालकता तक कई **समतुल्य अपघटनों** से पहुँचा जा सकता है, और सभी मान‑समान चालकता देते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `1 / resistivity` | `KConductivityUnitInstance` | व्युत्क्रम `σ = 1 / ρ` |
| `conductance / length` | `KConductivityUnitInstance` | `σ = G · l / A`; ज्यामिति गुणक `l / A` एक व्युत्क्रम लंबाई है, इसलिए भाग |
| `current²·time³/(mass·length³)` | `.toConductivity()` द्वारा | मूल विहित `kg⁻¹·m⁻³·s³·A²` व्यंजक |

टाइप किए गए ऑपरेटर रूप सीधे चालकता लौटाते हैं। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही रहता है
और `toConductivity()` से संकुचित किया जाता है (जो केवल विहित रूप को पहचानता है, अन्यथा
`IllegalStateException` फेंकता है)। सभी मार्ग मान‑समान हैं।

व्युत्क्रम ऑपरेटर चालन, लंबाई और चालकता को जोड़ते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `conductivity * length` | `KConductanceUnitInstance` | `G = σ · A / l` (क्रमविनिमेय) |
| `conductance / conductivity` | `KLengthUnitInstance` | ज्यामिति गुणक `A / l = G / σ` |
| `1 / conductivity` | `KResistivityUnitInstance` | वापस प्रतिरोधकता में |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.conductance.siemens
import org.pcsoft.framework.kunit.resistivity.ohmMeters
import org.pcsoft.framework.kunit.conductivity.*

// वास्तविक उदाहरण - तांबा: 17 nΩ·m की प्रतिरोधकता लगभग 58.8 MS/m की चालकता है।
val sigma = 1 / (17 of nano.ohmMeters)
sigma into mega.siemensPerMeter               // 58.82352941176471

// चालक ज्यामिति पर चालन:
val fromConductance = (10 of siemens) / (5 of meters)  // KConductivityUnitInstance, 2 S/m

// मूल kg⁻¹·m⁻³·s³·A² व्यंजक के रूप में वही चालकता:
val raw = 2 of ((amperes pow 2) * (seconds pow 3)) / (kilo.grams * (meters pow 3))
raw.toConductivity() == (2 of siemensPerMeter) // true

// व्युत्क्रम जोड़ा सममित है:
1 / (2 of siemensPerMeter) into ohmMeters      // 0.5
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.conductivity.*

val s = (100 of siemensPerMeter) + (40 of siemensPerMeter)  // 140 S/m
(100 of siemensPerMeter) > (40 of siemensPerMeter)          // true
(100 of siemensPerMeter) * (40 of siemensPerMeter)          // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.conductivity.*

(1 of siemensPerCentimeter).toString()   // "100.0 S/m" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`³`, `⁻¹`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `S/m` | `siemensPerMeter` | चालकता, आधार इकाई (नामित टोकन, सीमेंस प्रति मीटर) |
| `1 / ρ` | `1 / (17 of nano.ohmMeters)` | व्युत्क्रम प्रतिरोधकता के रूप में चालकता |
| `A²·s³/(kg·m³)` | `((amperes pow 2) * (seconds pow 3)) / (kilo.grams * (meters pow 3))` | धारा²·समय³ / (द्रव्यमान·लंबाई³) के रूप में चालकता (भिन्न रूप) |
| `kg⁻¹·m⁻³·s³·A²` | `(kilo.grams pow -1) * (meters pow -3) * (seconds pow 3) * (amperes pow 2)` | वही चालकता शुद्ध गुणनफल के रूप में |
| `MS/m` | `mega.siemensPerMeter` | उपसर्ग सहित चालकता (मेगासीमेंस प्रति मीटर) |
