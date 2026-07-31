# आवेश घनत्व

पैकेज: `org.pcsoft.framework.kunit.electric.chargedensity`
आधार इकाई: **कूलॉम प्रति घन मीटर** (`KChargeDensityUnit.BASE == KChargeDensityUnit.COULOMB_PER_CUBIC_METER`)

प्रकार: **संरचित इकाई**

(आयतन) आवेश घनत्व एक **संरचित** इकाई है: संयोजन `current¹ · time¹ · length⁻³`
(`A·s·m⁻³` = `C/m³`)। `KChargeDensityUnitInstance` तीन पदों वाले `KMixedUnitInstance` को लपेटता है — घातांक `+1` पर
`KElectricCurrentUnit.BASE` (ऐम्पियर), घातांक `+1` पर `KTimeUnit.BASE` (सेकंड) और घातांक
`-3` पर `KDistanceUnit.BASE` (मीटर)। चूँकि सभी घटक अपने समूह आधार इकाइयों में संग्रहीत रहते हैं, संग्रहीत मान सीधे C/m³
में पठन है।

## आवेश घनत्व बनाना

आवेश घनत्व में **कोई बेयर टोकन नहीं और कोई उपसर्ग बिल्डर नहीं** हैं — हर वर्तनी (C/m³, mC/cm³, …) एक अनुपात है। इसे
व्यंजक के रूप में या टाइप किए गए `charge / volume` ऑपरेटर के माध्यम से बनाएँ, और इसे ऐसे व्यंजक के विरुद्ध `into` से
वापस पढ़ें। उपसर्ग घटक टोकन (`milli.coulombs`, `centi.meters`) से आते हैं:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

val rho = (6 of coulombs) / (2 of liters)  // KChargeDensityUnitInstance, 3 C/L = 3000 C/m³
rho into (coulombs / (meters pow 3))       // 3000.0
rho into (coulombs / (centi.meters pow 3)) // 0.003 (= 3 mC/cm³)
rho into (milli.coulombs / (meters pow 3)) // 3000000.0
```

## अनेक अपघटन

आवेश घनत्व तक **समतुल्य अपघटनों** से पहुँचा जा सकता है, और सभी मान‑समान आवेश घनत्व देते हैं:

| व्यंजक                    | परिणाम प्रकार                     | अर्थ                   |
|------------------------|------------------------------|----------------------|
| `charge / volume`      | `KChargeDensityUnitInstance` | परिभाषा `ρ = Q / V`     |
| `current·time/length³` | `.toChargeDensity()` द्वारा      | मूल विहित `A·s·m⁻³` व्यंजक |

टाइप किया गया ऑपरेटर रूप सीधे आवेश घनत्व लौटाता है। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही रहता है और
`toChargeDensity()` से संकुचित किया जाता है (जो केवल विहित रूप को पहचानता है, अन्यथा
`IllegalStateException` फेंकता है)। दोनों मार्ग मान‑समान हैं।

व्युत्क्रम ऑपरेटर आवेश, आयतन और आवेश घनत्व को जोड़ते हैं:

| व्यंजक                      | परिणाम प्रकार              | अर्थ                   |
|--------------------------|-----------------------|----------------------|
| `chargeDensity * volume` | `KChargeUnitInstance` | `Q = ρ · V`          |
| `volume * chargeDensity` | `KChargeUnitInstance` | `Q = V · ρ` (क्रमविनिमेय) |
| `charge / chargeDensity` | `KVolumeUnitInstance` | `V = Q / ρ`          |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

// वास्तविक उदाहरण - इलेक्ट्रोलाइट में स्पेस चार्ज: 4 लीटर इलेक्ट्रोलाइट में घुला 12 mC शुद्ध आवेश
// 3 C/m³ की आवेश घनत्व देता है।
val rho = (0.012 of coulombs) / (4 of liters)   // KChargeDensityUnitInstance, 3 C/m³

// मूल A·s·m⁻³ व्यंजक के रूप में वही आवेश घनत्व:
val raw = (0.012 of coulombs).toUnit() / (0.004 of (meters pow 3))
raw.toChargeDensity() == rho                    // true

// 4 लीटर में निहित आवेश तक, और 12 mC धारण करने वाले आयतन तक वापस:
val q = rho * (4 of liters)                     // KChargeUnitInstance
q into coulombs                                 // 0.012
val v = (0.012 of coulombs) / rho               // KVolumeUnitInstance
v into liters                                   // 4.0
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

val a = (3 of coulombs) / (1 of liters)     // 3000 C/m³
val b = (1 of coulombs) / (1 of liters)     // 1000 C/m³
(a + b) into (coulombs / (meters pow 3))    // 4000.0
(a - b) into (coulombs / (meters pow 3))    // 2000.0
a > b                                       // true
a * b                                       // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

((1 of coulombs) / (1 of liters)).toString() // "1000.0 C/m³" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे।
घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को
भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित           | Kotlin                                   | अर्थ                                       |
|---------------|------------------------------------------|------------------------------------------|
| `C/m³`        | `coulombs / (meters pow 3)`              | आवेश घनत्व, आधार इकाई (कूलॉम प्रति घन मीटर) — भिन्न रूप |
| `C·m⁻³`       | `coulombs * (meters pow -3)`             | वही आवेश घनत्व ऋणात्मक घातांक वाले गुणनफल के रूप में      |
| `A·s/m³`      | `amperes * seconds / (meters pow 3)`     | मूल विहित रूप (धारा·समय / लंबाई³)                |
| `mC/cm³`      | `milli.coulombs / (centi.meters pow 3)`  | मिलीकूलॉम प्रति घन सेंटीमीटर                        |
| `12 mC / 4 L` | `(12 of milli.coulombs) / (4 of liters)` | आवेश ÷ आयतन से निर्माण                         |
