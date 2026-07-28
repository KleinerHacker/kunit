# विद्युत फ्लक्स घनत्व

पैकेज: `org.pcsoft.framework.kunit.electric.electricfluxdensity`
आधार इकाई: **कूलम्ब प्रति वर्ग मीटर**
(`KElectricFluxDensityUnit.BASE == KElectricFluxDensityUnit.COULOMB_PER_SQUARE_METER`)

प्रकार: **संरचित इकाई**

विद्युत फ्लक्स घनत्व एक **संरचित** इकाई है: संयोजन `current · time · length⁻²`
(`A·s·m⁻²` = `C/m²`)। `KElectricFluxDensityUnitInstance` तीन पदों वाले `KMixedUnitInstance` को लपेटता है —
घातांक `+1` पर `KElectricCurrentUnit.BASE` (ऐम्पियर), घातांक `+1` पर `KTimeUnit.BASE` (सेकंड) और घातांक
`-2` पर `KDistanceUnit.BASE` (मीटर)। समूह में कोई द्रव्यमान आयाम नहीं है, इसलिए ग्राम/किलोग्राम सेतु की
आवश्यकता नहीं है; संग्रहीत मान हमेशा कूलम्ब प्रति वर्ग मीटर में सामान्यीकृत रहता है।

फ्लक्स घनत्व `D` (जिसे विद्युत विस्थापन भी कहा जाता है) प्रति इकाई क्षेत्रफल आवेश है। **सतही आवेश घनत्व**
`σ` आयामीय रूप से वही राशि है और इसलिए इसे एक अलग समूह के बजाय इसी समूह से दर्शाया जाता है। `D`,
[विद्युत क्षेत्र तीव्रता](electricfieldstrength.md) से [विद्युतशीलता](permittivity.md) के माध्यम से जुड़ा
है (`D = ε · E`)। इसका एक-आयामी समकक्ष [रैखिक आवेश घनत्व](linearchargedensity.md) है, त्रि-आयामी समकक्ष
[आवेश घनत्व](chargedensity.md) है।

## विद्युत फ्लक्स घनत्व बनाना

फ्लक्स घनत्व को नामित टोकन से या किसी अपघटन (नीचे देखें) से बनाया जाता है। नामित इकाइयाँ मान‑1 टोकन के रूप
में उपलब्ध रहती हैं (`of`/`into` के साथ प्रयोग):

| फ्लक्स घनत्व | संकेत | टोकन | 1 इकाई C/m² में |
|---|---|---:|---:|
| कूलम्ब प्रति वर्ग मीटर | `C/m²` | `coulombsPerSquareMeter` | 1.0 |
| कूलम्ब प्रति वर्ग सेंटीमीटर | `C/cm²` | `coulombsPerSquareCentimeter` | 1.0e4 |

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`micro.coulombsPerSquareMeter`,
`milli.coulombsPerSquareMeter`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.fluxdensity.*

val d = 5 of micro.coulombsPerSquareMeter   // एक आवेशित संधारित्र प्लेट
d into micro.coulombsPerSquareMeter         // 5.0
d into coulombsPerSquareMeter               // 5.0e-6
(1 of coulombsPerSquareCentimeter) into coulombsPerSquareMeter // 10000.0
```

## अनेक अपघटन

विद्युत फ्लक्स घनत्व तक कई **समतुल्य अपघटनों** से पहुँचा जा सकता है, और सभी मान‑समान घनत्व देते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `charge / area` | `KElectricFluxDensityUnitInstance` | `D = Q / A`, क्षेत्रफल पर फैला आवेश |
| `permittivity * electricFieldStrength` | `KElectricFluxDensityUnitInstance` | `D = ε · E` (क्रमविनिमेय, देखें [विद्युतशीलता](permittivity.md)) |
| `current·time/length²` | `.toElectricFluxDensity()` द्वारा | मूल विहित `A·s·m⁻²` व्यंजक |

टाइप किए गए ऑपरेटर रूप सीधे फ्लक्स घनत्व लौटाते हैं। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही
रहता है और `toElectricFluxDensity()` से संकुचित किया जाता है (जो केवल विहित रूप को पहचानता है, अन्यथा
`IllegalStateException` फेंकता है)। सभी मार्ग मान‑समान हैं।

व्युत्क्रम ऑपरेटर आवेश, क्षेत्रफल और फ्लक्स घनत्व को जोड़ते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `electricFluxDensity * area` | `KChargeUnitInstance` | `Q = D · A` (क्रमविनिमेय) |
| `charge / electricFluxDensity` | `KAreaUnitInstance` | `A = Q / D` |
| `electricFluxDensity / electricFieldStrength` | `KPermittivityUnitInstance` | `ε = D / E` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.kinematic.distance.ares
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.electric.fluxdensity.*

// वास्तविक उदाहरण - 4 m² के संधारित्र प्लेट पर फैले 20 µC से 5 µC/m² मिलता है।
val plate: KAreaUnitInstance = 0.04 of ares            // 4 m²
val d = (20 of micro.coulombs) / plate                 // 5e-6 C/m²

// मूल A·s·m⁻² व्यंजक के रूप में वही फ्लक्स घनत्व:
val raw = 5e-6 of ((amperes pow 1) * (seconds pow 1)) / (meters pow 2)
raw.toElectricFluxDensity() == d                       // true
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.fluxdensity.*

val s = (1 of coulombsPerSquareMeter) + (1 of coulombsPerSquareCentimeter)  // 10001 C/m²
(1 of coulombsPerSquareCentimeter) > (1 of coulombsPerSquareMeter)          // true
(2 of coulombsPerSquareMeter) * (3 of coulombsPerSquareMeter)               // KMixedUnitInstance
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.fluxdensity.*

(1 of coulombsPerSquareCentimeter).toString()   // "10000.0 C/m²" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `⁻²`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `C/m²` | `coulombsPerSquareMeter` | विद्युत फ्लक्स घनत्व, आधार इकाई (नामित टोकन) |
| `Q / A` | `(20 of micro.coulombs) / plate` | क्षेत्रफल पर आवेश से फ्लक्स घनत्व |
| `ε · E` | `(1 of vacuumPermittivity) * (1 of voltsPerMeter)` | विद्युतशीलता और क्षेत्र तीव्रता से फ्लक्स घनत्व |
| `A·s/m²` | `((amperes pow 1) * (seconds pow 1)) / (meters pow 2)` | धारा·समय / लंबाई² के रूप में फ्लक्स घनत्व (भिन्न रूप) |
| `A·s·m⁻²` | `(amperes pow 1) * (seconds pow 1) * (meters pow -2)` | वही फ्लक्स घनत्व शुद्ध गुणनफल के रूप में |
| `µC/m²` | `micro.coulombsPerSquareMeter` | उपसर्ग सहित फ्लक्स घनत्व (माइक्रोकूलम्ब प्रति वर्ग मीटर) |
