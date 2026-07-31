# पृष्ठ तनाव

पैकेज: `org.pcsoft.framework.kunit.mechanic.lineforce`
मूल इकाई: **न्यूटन प्रति मीटर** (`KLineForceUnit.BASE == KLineForceUnit.NEWTONS_PER_METER`)

प्रकार: **निर्मित इकाई**

पृष्ठ तनाव `σ` नई सतह की एक इकाई बनाने के लिए आवश्यक ऊर्जा है, जो समतुल्य रूप से किसी संपर्क रेखा के साथ प्रति इकाई
लंबाई पर कार्य करने वाला बल है: `1 J/m² = 1 N/m`। इसका आयाम `mass · time⁻²` है।

यह ठीक **प्रति लंबाई बल** का आयाम है, जिसे [दृढ़ता](stiffness.md) साझा करती है। इसलिए KUnit दोनों पठनों के लिए एक तटस्थ
समूह, `lineforce`, मॉडल करता है; पृष्ठ तनाव उनमें से एक है। यह पृष्ठ उस पठन का दस्तावेज़ीकरण करता है।

!!! note "एक समूह, दो पठन"
`KLineForceUnitInstance` साझा प्रकार है। आपके द्वारा दिए गए नाम के अलावा कुछ भी पृष्ठ तनाव को स्प्रिंग दर से अलग नहीं
करता — समूह का नाम तटस्थ रखा गया है ताकि कोई भी पठन दूसरे का नाम न ले।

## नामित इकाइयाँ

| इकाई          | प्रतीक      |                    टोकन | N/m में 1 इकाई |
|--------------|----------|-----------------------:|------------:|
| न्यूटन प्रति मीटर   | `N/m`    |      `newtonsPerMeter` |         1.0 |
| डाइन प्रति सेंटीमीटर | `dyn/cm` |   `dynesPerCentimeter` |        1e-3 |
| न्यूटन प्रति मिलीमीटर | `N/mm`   | `newtonsPerMillimeter` |      1000.0 |
| पाउंड-बल प्रति इंच | `lbf/in` |   `poundsForcePerInch` |   ≈ 175.127 |
| किलोपॉन्ड प्रति मीटर  | `kp/m`   |    `kilopondsPerMeter` |     9.80665 |

पृष्ठ तनाव आमतौर पर mN/m या संख्यात्मक रूप से समान dyn/cm में उल्लेखित किया जाता है: 25 °C पर पानी ≈ 72 mN/m = 72 dyn/cm
है। मिलीन्यूटन प्रति मीटर उपसर्ग-युक्त रूप `milli.newtonsPerMeter` है।

## अपघटन

| रूप        | Kotlin                                                  | परिणाम प्रकार                 |
|-----------|---------------------------------------------------------|--------------------------|
| ऊर्जा / क्षेत्रफल | `energy / area`                                         | `KLineForceUnitInstance` |
| बल / लंबाई  | `force / length`                                        | `KLineForceUnitInstance` |
| नेटिव व्यंजक   | `(mass.toUnit() / (time.toUnit() pow 2)).toLineForce()` | `KLineForceUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val viaEnergy = (2 of joules) / ((1 of meters) * (1 of meters))
val viaForce = (2 of newtons) / (1 of meters)

viaEnergy == viaForce                  // true - दोनों 2 N/m हैं
(72 of milli.joules) / ((1 of meters) * (1 of meters)) into dynesPerCentimeter // 72.0
```

## मूल इकाइयों के साथ गणना

| व्यंजक                                        | परिणाम प्रकार                 | अर्थ                |
|--------------------------------------------|--------------------------|-------------------|
| `energy / area`                            | `KLineForceUnitInstance` | `σ = W / A`       |
| `lineforce * area`, `area * lineforce`     | `KEnergyUnitInstance`    | पृष्ठ ऊर्जा `W = σ · A` |
| `energy / lineforce`                       | `KAreaUnitInstance`      | `A = W / σ`       |
| `force / length`                           | `KLineForceUnitInstance` | `σ = F / l`       |
| `lineforce * length`, `length * lineforce` | `KForceUnitInstance`     | `F = σ · l`       |

## वास्तविक उदाहरण: साबुन की झिल्ली बनाने की ऊर्जा

0.05 m² (दो सतहें, σ ≈ 25 mN/m प्रति सतह) की साबुन की झिल्ली फूँकना। इसकी लागत कितनी ऊर्जा है, और झिल्ली 10 cm के तार पर
कौन-सा बल लगाती है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val sigma = 25 of milli.newtonsPerMeter
val area = (0.5 of meters) * (0.1 of meters)   // 0.05 m²

val energy = sigma * area                       // KEnergyUnitInstance
energy into milli.joules                        // 1.25

val force = sigma * (10 of centi.meters)        // KForceUnitInstance
force into milli.newtons                        // 2.5
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val sum = (72 of dynesPerCentimeter) + (8 of dynesPerCentimeter) // 80 dyn/cm
(72 of dynesPerCentimeter) > (50 of milli.newtonsPerMeter)       // true
(1 of dynesPerCentimeter) == (1 of milli.newtonsPerMeter)        // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineforce.*

(72 of dynesPerCentimeter).toString()                     // "0.072 N/m" (मूल इकाई)
"${(72 of dynesPerCentimeter) into dynesPerCentimeter} dyn/cm" // "72.0 dyn/cm"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखा जाता है।
घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित         | Kotlin                          | अर्थ                 |
|-------------|---------------------------------|--------------------|
| `N/m`       | `newtonsPerMeter`               | पृष्ठ तनाव, मूल इकाई     |
| `kg·s⁻²`    | `kilo.grams * (seconds pow -2)` | वही राशि आधार आयामों में    |
| `mN/m`      | `milli.newtonsPerMeter`         | रोज़मर्रा का पृष्ठ-तनाव पठन  |
| `dyn/cm`    | `dynesPerCentimeter`            | CGS पठन (= 1 mN/m) |
| `σ = W / A` | `energy / area`                 | अपघटन A            |
| `σ = F / l` | `force / length`                | अपघटन B            |
| `W = σ · A` | `lineforce * area`              | पृष्ठ ऊर्जा              |

</content>
