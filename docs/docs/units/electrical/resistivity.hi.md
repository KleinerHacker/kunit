# प्रतिरोधकता

पैकेज: `org.pcsoft.framework.kunit.electric.resistivity`
आधार इकाई: **ओम मीटर** (`KResistivityUnit.BASE == KResistivityUnit.OHM_METER`)

प्रकार: **संरचित इकाई**

विद्युत प्रतिरोधकता एक **संरचित** इकाई है: संयोजन `mass · length³ · time⁻³ · current⁻²`
(`kg·m³·s⁻³·A⁻²`)। `KResistivityUnitInstance` चार पदों वाले `KMixedUnitInstance` को लपेटता है — घातांक `+1`
पर `KMassUnit.BASE` (ग्राम), घातांक `+3` पर `KDistanceUnit.BASE` (मीटर), घातांक `-3` पर `KTimeUnit.BASE`
(सेकंड) और घातांक `-2` पर `KElectricCurrentUnit.BASE` (ऐम्पियर)। चूँकि लाइब्रेरी का द्रव्यमान घटक
**ग्राम** में सामान्यीकृत है (किलोग्राम में नहीं), ओम मीटर तक पहुँचने के लिए विहित गुणनफल को 1000 से
विभाजित किया जाता है; संग्रहीत मान हमेशा ओम मीटर में सामान्यीकृत रहता है।

प्रतिरोधकता प्रतिरोध के पीछे की पदार्थ गुण है और [चालकता](conductivity.md) की व्युत्क्रम (`ρ = 1 / σ`) है।

## प्रतिरोधकता बनाना

प्रतिरोधकता को नामित टोकन से या किसी अपघटन (नीचे देखें) से बनाया जाता है। नामित इकाइयाँ मान‑1 टोकन के रूप
में उपलब्ध रहती हैं (`of`/`into` के साथ प्रयोग):

| प्रतिरोधकता | संकेत | टोकन | 1 इकाई = ? Ω·m |
|---|---|---:|---:|
| ओम मीटर | `Ω·m` | `ohmMeters` | 1.0 |
| ओम सेंटीमीटर | `Ω·cm` | `ohmCentimeters` | 0.01 |
| स्टैटओम सेंटीमीटर (CGS-ESU) | `statΩ·cm` | `statohmCentimeters` | 8.98755179e9 |

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`nano.ohmMeters`,
`micro.ohmMeters`, `milli.ohmCentimeters`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.electric.resistivity.*

val rho = 17 of nano.ohmMeters     // तांबा
rho into nano.ohmMeters            // 17.0
rho into ohmMeters                 // 1.7e-8
(1 of ohmMeters) into ohmCentimeters // 100.0
```

## अनेक अपघटन

प्रतिरोधकता तक कई **समतुल्य अपघटनों** से पहुँचा जा सकता है, और सभी मान‑समान प्रतिरोधकता देते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `resistance * length` | `KResistivityUnitInstance` | `ρ = R · A / l`, ज्यामिति गुणक `A / l` एक लंबाई है (क्रमविनिमेय) |
| `1 / conductivity` | `KResistivityUnitInstance` | व्युत्क्रम `ρ = 1 / σ` |
| `mass·length³/(time³·current²)` | `.toResistivity()` द्वारा | मूल विहित `kg·m³·s⁻³·A⁻²` व्यंजक |

टाइप किए गए ऑपरेटर रूप सीधे प्रतिरोधकता लौटाते हैं। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही रहता
है और `toResistivity()` से संकुचित किया जाता है (जो केवल विहित रूप को पहचानता है, अन्यथा
`IllegalStateException` फेंकता है)। सभी मार्ग मान‑समान हैं।

व्युत्क्रम ऑपरेटर प्रतिरोध, लंबाई और प्रतिरोधकता को जोड़ते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `resistivity / length` | `KResistanceUnitInstance` | `R = ρ · l / A` |
| `resistivity / resistance` | `KLengthUnitInstance` | ज्यामिति गुणक `A / l = ρ / R` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.resistance.ohms
import org.pcsoft.framework.kunit.electric.resistivity.*

// वास्तविक उदाहरण - तांबे की वायरिंग: 1 mm के ज्यामिति गुणक पर 17 nΩ·m से 17 µΩ मिलता है।
val r = (17 of nano.ohmMeters) / (1 of milli.meters)  // KResistanceUnitInstance, 1.7e-5 Ω

// प्रतिरोधकता के लिए हल की गई परिभाषा:
val rho = (5 of ohms) * (0.4 of meters)               // KResistivityUnitInstance, 2 Ω·m

// मूल kg·m³·s⁻³·A⁻² व्यंजक के रूप में वही प्रतिरोधकता:
val raw = 2 of (kilo.grams * (meters pow 3)) / ((seconds pow 3) * (amperes pow 2))
raw.toResistivity() == (2 of ohmMeters)               // true
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.resistivity.*

val s = (100 of ohmMeters) + (40 of ohmMeters)  // 140 Ω·m
(100 of ohmMeters) > (40 of ohmMeters)          // true
(100 of ohmMeters) * (40 of ohmMeters)          // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.resistivity.*

(1 of ohmCentimeters).toString()   // "0.01 Ω·m" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`³`, `⁻²`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `Ω·m` | `ohmMeters` | प्रतिरोधकता, आधार इकाई (नामित टोकन, ओम मीटर) |
| `R · (A/l)` | `(5 of ohms) * (0.4 of meters)` | प्रतिरोध और ज्यामिति गुणक से प्रतिरोधकता |
| `kg·m³/(s³·A²)` | `(kilo.grams * (meters pow 3)) / ((seconds pow 3) * (amperes pow 2))` | द्रव्यमान·लंबाई³ / (समय³·धारा²) के रूप में प्रतिरोधकता (भिन्न रूप) |
| `kg·m³·s⁻³·A⁻²` | `kilo.grams * (meters pow 3) * (seconds pow -3) * (amperes pow -2)` | वही प्रतिरोधकता शुद्ध गुणनफल के रूप में |
| `nΩ·m` | `nano.ohmMeters` | उपसर्ग सहित प्रतिरोधकता (नैनोओम मीटर) |
