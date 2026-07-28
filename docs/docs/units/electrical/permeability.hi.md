# पारगम्यता

पैकेज: `org.pcsoft.framework.kunit.electric.permeability`
आधार इकाई: **हेनरी प्रति मीटर** (`KPermeabilityUnit.BASE == KPermeabilityUnit.HENRY_PER_METER`)

प्रकार: **संरचित इकाई**

पारगम्यता एक **संरचित** इकाई है: संयोजन `mass · length · time⁻² · current⁻²`
(`kg·m·s⁻²·A⁻²` = `H/m`)। `KPermeabilityUnitInstance` चार पदों वाले `KMixedUnitInstance` को लपेटता है —
घातांक `+1` पर `KMassUnit.BASE` (ग्राम), घातांक `+1` पर `KDistanceUnit.BASE` (मीटर), घातांक `-2` पर
`KTimeUnit.BASE` (सेकंड) और घातांक `-2` पर `KElectricCurrentUnit.BASE` (ऐम्पियर)। चूँकि लाइब्रेरी का
द्रव्यमान घटक **ग्राम** में सामान्यीकृत है (किलोग्राम में नहीं), विहित गुणनफल को हेनरी प्रति मीटर तक
पहुँचने के लिए 1000 से विभाजित किया जाता है; संग्रहीत मान हमेशा हेनरी प्रति मीटर में सामान्यीकृत रहता है।

पारगम्यता `μ` किसी पदार्थ का चुंबकीय स्थिरांक है: यह [चुंबकीय फ्लक्स घनत्व](magneticfluxdensity.md) को
[चुंबकीय क्षेत्र तीव्रता](magneticfieldstrength.md) से जोड़ती है (`μ = B / H`) और
[प्रेरकत्व](inductance.md) को कुंडली की ज्यामिति से जोड़ती है। इसका विद्युत समकक्ष
[विद्युतशीलता](permittivity.md) है।

## पारगम्यता बनाना

पारगम्यता को नामित टोकन से या किसी अपघटन (नीचे देखें) से बनाया जाता है। नामित इकाइयाँ मान‑1 टोकन के रूप में
उपलब्ध रहती हैं (`of`/`into` के साथ प्रयोग):

| पारगम्यता | संकेत | टोकन | 1 इकाई H/m में |
|---|---|---:|---:|
| हेनरी प्रति मीटर | `H/m` | `henriesPerMeter` | 1.0 |
| हेनरी प्रति सेंटीमीटर | `H/cm` | `henriesPerCentimeter` | 100.0 |
| निर्वात पारगम्यता `μ₀` | `H/m` | `vacuumPermeability` | 1.25663706127e-6 |

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`micro.henriesPerMeter`,
`milli.henriesPerMeter`, …)। यह स्थिरांक `KPermeabilityUnit.VACUUM_PERMEABILITY` के रूप में भी उपलब्ध है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.permeability.*

val mu = 1 of vacuumPermeability      // μ₀
mu into henriesPerMeter               // 1.25663706127e-6
mu into micro.henriesPerMeter         // 1.25663706127
(1 of henriesPerCentimeter) into henriesPerMeter // 100.0
```

## अनेक अपघटन

पारगम्यता तक कई **समतुल्य अपघटनों** से पहुँचा जा सकता है, और सभी मान‑समान पारगम्यता देते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `inductance / length` | `KPermeabilityUnitInstance` | `μ = L · l / (N² · A)`, ज्यामिति गुणक एक लंबाई है |
| `magneticFluxDensity / magneticFieldStrength` | `KPermeabilityUnitInstance` | `μ = B / H` |
| `mass·length/(time²·current²)` | `.toPermeability()` द्वारा | मूल विहित `kg·m·s⁻²·A⁻²` व्यंजक |

टाइप किए गए ऑपरेटर रूप सीधे पारगम्यता लौटाते हैं। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही रहता
है और `toPermeability()` से संकुचित किया जाता है (जो केवल विहित रूप को पहचानता है, अन्यथा
`IllegalStateException` फेंकता है)। सभी मार्ग मान‑समान हैं।

व्युत्क्रम ऑपरेटर प्रेरकत्व, लंबाई और दोनों चुंबकीय क्षेत्र राशियों को जोड़ते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `permeability * length` | `KInductanceUnitInstance` | `L = μ · N² · A / l` (क्रमविनिमेय) |
| `inductance / permeability` | `KLengthUnitInstance` | ज्यामिति गुणक `N² · A / l = L / μ` |
| `permeability * magneticFieldStrength` | `KMagneticFluxDensityUnitInstance` | `B = μ · H` (क्रमविनिमेय) |
| `magneticFluxDensity / permeability` | `KMagneticFieldStrengthUnitInstance` | `H = B / μ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.inductance.henries
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.amperesPerMeter
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.teslas
import org.pcsoft.framework.kunit.electric.permeability.*

// वास्तविक उदाहरण - निर्वात में 1000 A/m का क्षेत्र 1.257 mT का फ्लक्स घनत्व उत्पन्न करता है।
val b = (1 of vacuumPermeability) * (1000 of amperesPerMeter)  // 1.25663706127e-3 T

// पारगम्यता के लिए हल की गई परिभाषा:
val mu = (6 of teslas) / (3 of amperesPerMeter)                // 2 H/m
val fromInductance = (10 of henries) / (5 of meters)           // 2 H/m

// मूल kg·m·s⁻²·A⁻² व्यंजक के रूप में वही पारगम्यता:
val raw = 2 of (kilo.grams * (meters pow 1)) / ((seconds pow 2) * (amperes pow 2))
raw.toPermeability() == (2 of henriesPerMeter)                 // true
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.permeability.*

val s = (1 of henriesPerMeter) + (1 of henriesPerCentimeter)  // 101 H/m
(1 of henriesPerCentimeter) > (1 of henriesPerMeter)          // true
(2 of henriesPerMeter) * (3 of henriesPerMeter)               // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.permeability.*

(1 of henriesPerCentimeter).toString()   // "100.0 H/m" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `⁻²`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `H/m` | `henriesPerMeter` | पारगम्यता, आधार इकाई (नामित टोकन, हेनरी प्रति मीटर) |
| `μ₀` | `vacuumPermeability` | निर्वात पारगम्यता स्थिरांक, 1.257 µH/m |
| `B / H` | `(6 of teslas) / (3 of amperesPerMeter)` | फ्लक्स घनत्व और क्षेत्र तीव्रता के अनुपात के रूप में पारगम्यता |
| `L · l / (N²·A)` | `(10 of henries) / (5 of meters)` | प्रेरकत्व और कुंडली ज्यामिति से पारगम्यता |
| `kg·m/(s²·A²)` | `(kilo.grams * (meters pow 1)) / ((seconds pow 2) * (amperes pow 2))` | द्रव्यमान·लंबाई / (समय²·धारा²) के रूप में पारगम्यता (भिन्न रूप) |
| `kg·m·s⁻²·A⁻²` | `kilo.grams * (meters pow 1) * (seconds pow -2) * (amperes pow -2)` | वही पारगम्यता शुद्ध गुणनफल के रूप में |
| `µH/m` | `micro.henriesPerMeter` | उपसर्ग सहित पारगम्यता (माइक्रोहेनरी प्रति मीटर) |
