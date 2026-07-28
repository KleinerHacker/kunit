# विद्युतशीलता

पैकेज: `org.pcsoft.framework.kunit.permittivity`
आधार इकाई: **फैराड प्रति मीटर** (`KPermittivityUnit.BASE == KPermittivityUnit.FARAD_PER_METER`)

प्रकार: **संरचित इकाई**

विद्युतशीलता एक **संरचित** इकाई है: संयोजन `mass⁻¹ · length⁻³ · time⁴ · current²`
(`kg⁻¹·m⁻³·s⁴·A²` = `F/m`)। `KPermittivityUnitInstance` चार पदों वाले `KMixedUnitInstance` को लपेटता है —
घातांक `-1` पर `KMassUnit.BASE` (ग्राम), घातांक `-3` पर `KDistanceUnit.BASE` (मीटर), घातांक `+4` पर
`KTimeUnit.BASE` (सेकंड) और घातांक `+2` पर `KElectricCurrentUnit.BASE` (ऐम्पियर)। चूँकि लाइब्रेरी का
द्रव्यमान घटक **ग्राम** में सामान्यीकृत है (किलोग्राम में नहीं) और द्रव्यमान घातांक *ऋणात्मक* है, विहित
गुणनफल को फैराड प्रति मीटर तक पहुँचने के लिए 1000 से गुणा किया जाता है; संग्रहीत मान हमेशा F/m में
सामान्यीकृत रहता है।

विद्युतशीलता `ε` किसी पदार्थ का विद्युत स्थिरांक है: यह [विद्युत फ्लक्स घनत्व](electricfluxdensity.md)
को [विद्युत क्षेत्र तीव्रता](electricfieldstrength.md) से जोड़ती है (`ε = D / E`) और
[धारिता](capacitance.md) को प्लेट की ज्यामिति से जोड़ती है। इसका चुंबकीय समकक्ष
[पारगम्यता](permeability.md) है।

## विद्युतशीलता बनाना

विद्युतशीलता को नामित टोकन से या किसी अपघटन (नीचे देखें) से बनाया जाता है। नामित इकाइयाँ मान‑1 टोकन के रूप
में उपलब्ध रहती हैं (`of`/`into` के साथ प्रयोग):

| विद्युतशीलता | संकेत | टोकन | 1 इकाई F/m में |
|---|---|---:|---:|
| फैराड प्रति मीटर | `F/m` | `faradsPerMeter` | 1.0 |
| फैराड प्रति सेंटीमीटर | `F/cm` | `faradsPerCentimeter` | 100.0 |
| निर्वात विद्युतशीलता `ε₀` | `F/m` | `vacuumPermittivity` | 8.8541878188e-12 |

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`pico.faradsPerMeter`,
`nano.faradsPerMeter`, …)। यह स्थिरांक `KPermittivityUnit.VACUUM_PERMITTIVITY` के रूप में भी उपलब्ध है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.permittivity.*

val eps = 1 of vacuumPermittivity     // ε₀
eps into faradsPerMeter               // 8.8541878188e-12
eps into pico.faradsPerMeter          // 8.8541878188
(1 of faradsPerCentimeter) into faradsPerMeter // 100.0
```

## अनेक अपघटन

विद्युतशीलता तक कई **समतुल्य अपघटनों** से पहुँचा जा सकता है, और सभी मान‑समान विद्युतशीलता देते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `capacitance / length` | `KPermittivityUnitInstance` | `ε = C · d / A`, ज्यामिति गुणक `d / A` एक लंबाई है |
| `electricFluxDensity / electricFieldStrength` | `KPermittivityUnitInstance` | `ε = D / E` |
| `(time⁴·current²)/(mass·length³)` | `.toPermittivity()` द्वारा | मूल विहित `kg⁻¹·m⁻³·s⁴·A²` व्यंजक |

टाइप किए गए ऑपरेटर रूप सीधे विद्युतशीलता लौटाते हैं। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही
रहता है और `toPermittivity()` से संकुचित किया जाता है (जो केवल विहित रूप को पहचानता है, अन्यथा
`IllegalStateException` फेंकता है)। सभी मार्ग मान‑समान हैं।

व्युत्क्रम ऑपरेटर धारिता, लंबाई और दोनों क्षेत्र राशियों को जोड़ते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `permittivity * length` | `KCapacitanceUnitInstance` | `C = ε · A / d` (क्रमविनिमेय) |
| `capacitance / permittivity` | `KLengthUnitInstance` | ज्यामिति गुणक `A / d = C / ε` |
| `permittivity * electricFieldStrength` | `KElectricFluxDensityUnitInstance` | `D = ε · E` (क्रमविनिमेय) |
| `electricFluxDensity / permittivity` | `KElectricFieldStrengthUnitInstance` | `E = D / ε` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.capacitance.farads
import org.pcsoft.framework.kunit.electricfieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.electricfluxdensity.coulombsPerSquareMeter
import org.pcsoft.framework.kunit.permittivity.*

// वास्तविक उदाहरण - निर्वात में 1 MV/m का क्षेत्र 8.854 µC/m² का फ्लक्स घनत्व उत्पन्न करता है।
val d = (1 of vacuumPermittivity) * (1_000_000 of voltsPerMeter)  // 8.8541878188e-6 C/m²

// विद्युतशीलता के लिए हल की गई परिभाषा:
val eps = (6 of coulombsPerSquareMeter) / (3 of voltsPerMeter)    // 2 F/m
val fromCapacitance = (10 of farads) / (5 of meters)              // 2 F/m

// मूल kg⁻¹·m⁻³·s⁴·A² व्यंजक के रूप में वही विद्युतशीलता:
val raw = 2 of ((seconds pow 4) * (amperes pow 2)) / (kilo.grams * (meters pow 3))
raw.toPermittivity() == (2 of faradsPerMeter)                     // true
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.permittivity.*

val s = (1 of faradsPerMeter) + (1 of faradsPerCentimeter)  // 101 F/m
(1 of faradsPerCentimeter) > (1 of faradsPerMeter)          // true
(2 of faradsPerMeter) * (3 of faradsPerMeter)               // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.permittivity.*

(1 of faradsPerCentimeter).toString()   // "100.0 F/m" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`⁴`, `⁻³`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `F/m` | `faradsPerMeter` | विद्युतशीलता, आधार इकाई (नामित टोकन, फैराड प्रति मीटर) |
| `ε₀` | `vacuumPermittivity` | निर्वात विद्युतशीलता स्थिरांक, 8.854 pF/m |
| `D / E` | `(6 of coulombsPerSquareMeter) / (3 of voltsPerMeter)` | फ्लक्स घनत्व और क्षेत्र तीव्रता के अनुपात के रूप में विद्युतशीलता |
| `C · (d/A)` | `(10 of farads) / (5 of meters)` | धारिता और ज्यामिति गुणक से विद्युतशीलता |
| `(s⁴·A²)/(kg·m³)` | `((seconds pow 4) * (amperes pow 2)) / (kilo.grams * (meters pow 3))` | (समय⁴·धारा²) / (द्रव्यमान·लंबाई³) के रूप में विद्युतशीलता (भिन्न रूप) |
| `kg⁻¹·m⁻³·s⁴·A²` | `(kilo.grams pow -1) * (meters pow -3) * (seconds pow 4) * (amperes pow 2)` | वही विद्युतशीलता शुद्ध गुणनफल के रूप में |
| `pF/m` | `pico.faradsPerMeter` | उपसर्ग सहित विद्युतशीलता (पिकोफैराड प्रति मीटर) |
