# रैखिक घनत्व

पैकेज: `org.pcsoft.framework.kunit.mechanic.lineardensity`
मूल इकाई: **किलोग्राम प्रति मीटर**
(`KLinearDensityUnit.BASE == KLinearDensityUnit.KILOGRAMS_PER_METER`)

प्रकार: **निर्मित इकाई**

रैखिक घनत्व प्रति इकाई लंबाई द्रव्यमान है — [क्षेत्र घनत्व](areadensity.md) (`kg/m²`) और
[घनत्व](density.md) (`kg/m³`) का एक-आयामी सहोदर। यह एक **निर्मित** इकाई है — संघटन `mass · length⁻¹`
(`kg/m`)।

`KLinearDensityUnitInstance` एक `KMixedUnitInstance` को लपेटता है जिसमें कैनोनिकल सामान्य रूप में ठीक दो पद होते हैं:
`KMassUnit.BASE` (ग्राम) घातांक `+1` पर और `KDistanceUnit.BASE` (मीटर) घातांक `-1` पर। चूँकि इस लाइब्रेरी का द्रव्यमान
घटक ग्राम में सामान्यीकृत है, इसलिए संचित मान कच्चा ग्राम-आधारित घटक मान है और kg/m में पठन एक स्थिर गुणांक से विभाजित
होते हैं।

## नामित इकाइयाँ

| इकाई         | प्रतीक     |                  टोकन | kg/m में 1 इकाई |
|-------------|---------|---------------------:|-------------:|
| किलोग्राम प्रति मीटर | `kg/m`  |  `kilogramsPerMeter` |          1.0 |
| ग्राम प्रति मीटर   | `g/m`   |      `gramsPerMeter` |         1e-3 |
| ग्राम प्रति सेंटीमीटर | `g/cm`  | `gramsPerCentimeter` |          0.1 |
| टेक्स (वस्त्र)     | `tex`   |                `tex` |         1e-6 |
| डेनियर (वस्त्र)   | `den`   |             `denier` |  ≈ 1.1111e-7 |
| पाउंड प्रति फुट   | `lb/ft` |      `poundsPerFoot` |    ≈ 1.48816 |

सभी इकाइयाँ पूरे SI उपसर्ग परिसर को स्वीकार करती हैं; वस्त्र-उद्योग का डेसिटेक्स `deci.tex` है।

## मूल इकाइयों के साथ गणना

| व्यंजक                                                | परिणाम प्रकार                     | अर्थ            |
|----------------------------------------------------|------------------------------|---------------|
| `mass / length`                                    | `KLinearDensityUnitInstance` | `ρ_l = m / l` |
| `lineardensity * length`, `length * lineardensity` | `KMassUnitInstance`          | `m = ρ_l · l` |
| `mass / lineardensity`                             | `KLengthUnitInstance`        | `l = m / ρ_l` |

नेटिव रूप भी उपलब्ध है: सामान्य इंजन के माध्यम से बना कोई भी ग्राम-प्रति-मीटर व्यंजक `toLinearDensity()`
से रूपांतरित होता है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.lineardensity.*
import org.pcsoft.framework.kunit.mechanic.mass.grams

val typed = (2 of kilo.grams) / (4 of meters)
val native = ((2000 of grams).toUnit() / (4 of meters).toUnit()).toLinearDensity()

typed == native                 // true - दोनों 0.5 kg/m हैं
typed into gramsPerMeter        // 500.0
```

## वास्तविक उदाहरण: ड्रम पर स्टील केबल

एक स्टील केबल का वज़न 2.6 kg/m है। 45 m लंबाई का द्रव्यमान क्या है, और 500 kg पेलोड सीमा कितनी केबल लंबाई की अनुमति देती
है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.lineardensity.*

val cable = 2.6 of kilogramsPerMeter
val mass = cable * (45 of meters)     // KMassUnitInstance
mass into kilo.grams                  // 117.0

val maxLength = (500 of kilo.grams) / cable // KLengthUnitInstance
maxLength into meters                        // ≈ 192.31
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineardensity.*

val sum = (10 of kilogramsPerMeter) + (4 of kilogramsPerMeter) // 14 kg/m
(1 of kilogramsPerMeter) > (1 of gramsPerMeter)                // true
(1 of kilogramsPerMeter) == (1000 of gramsPerMeter)            // true
(1 of tex) == (9 of denier)                                     // true (वस्त्र संबंध)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineardensity.*

(0.5 of kilogramsPerMeter).toString()                 // "0.5 kg/m" (मूल इकाई)
"${(0.5 of kilogramsPerMeter) into gramsPerMeter} g/m" // "500.0 g/m"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखा जाता है।
घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित           | Kotlin                         | अर्थ                        |
|---------------|--------------------------------|---------------------------|
| `kg/m`        | `kilogramsPerMeter`            | रैखिक घनत्व, मूल इकाई (नामित टोकन) |
| `kg·m⁻¹`      | `kilo.grams * (meters pow -1)` | वही राशि शुद्ध गुणनफल के रूप में     |
| `tex`         | `tex`                          | वस्त्र रैखिक घनत्व (1 g/km)       |
| `ρ_l = m / l` | `mass / length`                | टाइप किया गया अपघटन           |
| `m = ρ_l · l` | `lineardensity * length`       | द्रव्यमान के लिए हल किया गया        |
| `dtex`        | `deci.tex`                     | उपसर्ग-युक्त वस्त्र पठन            |

</content>
