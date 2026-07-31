# विकृति (Strain)

पैकेज: `org.pcsoft.framework.kunit.mechanic.strain`
मूल इकाई: **सरल अनुपात** (`KStrainUnit.BASE == KStrainUnit.RATIO`)

प्रकार: **निर्मित इकाई**

विकृति `ε = ΔL / L` किसी पिंड का सापेक्ष विरूपण है। यह **आयामरहित** है — एक लंबाई को दूसरी लंबाई से विभाजित करने पर —
लेकिन इसके पठन (प्रतिशत, प्रति सहस्र, माइक्रोस्ट्रेन) एक वास्तविक इकाई शब्दावली बनाते हैं, इसलिए KUnit इसे अपने समूह के
रूप में मॉडल करता है।

`KStrainUnitInstance` एक `KMixedUnitInstance` को लपेटता है जिसमें घातांक 1 पर एकल `KStrainUnit.BASE`
पद होता है, जो हमेशा सरल अनुपात में सामान्यीकृत होता है।

!!! note "`toStrain()` ही क्यों, न कि कोई संकारक"
सामान्य इंजन `length / length` को **बिना** किसी इकाई पद वाली मिश्रित इकाई के रूप में दर्शाता है। चूँकि
`KLengthUnitInstance.div` एक सदस्य संकारक है इसलिए इसे ओवरराइड नहीं किया जा सकता, इसलिए नेटिव अपघटन को टाइप किए गए
संकारक के बजाय फॉर्म-पहचान हुक `toStrain()` के माध्यम से पहुँचा जाता है।

## नामित इकाइयाँ

| इकाई            | प्रतीक  |           टोकन | अनुपात में 1 इकाई |
|----------------|------|--------------:|-------------:|
| सरल अनुपात (m/m) | `1`  |       `ratio` |          1.0 |
| प्रतिशत           | `%`  |     `percent` |         0.01 |
| प्रति सहस्र         | `‰`  |    `perMille` |         1e-3 |
| माइक्रोस्ट्रेन          | `µe` | `microstrain` |         1e-6 |

सभी इकाइयाँ पूरे SI उपसर्ग परिसर को स्वीकार करती हैं, इसलिए `micro.ratio` माइक्रोस्ट्रेन की एक और वर्तनी है।

## एक विकृति बनाना

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.strain.*

// 1 m की छड़ जो 2 mm लंबी हो गई
val e = ((2 of milli.meters) / (1 of meters)).toStrain()
e into perMille     // 2.0
e into percent      // 0.2
e into microstrain  // 2000.0
e into ratio        // 0.002
```

## एक विकृति के साथ गणना

| व्यंजक                                      | परिणाम प्रकार                | अर्थ                    |
|------------------------------------------|-------------------------|-----------------------|
| `(length / length).toStrain()`           | `KStrainUnitInstance`   | `ε = ΔL / L` (नेटिव रूप) |
| `stress / strain`                        | `KPressureUnitInstance` | प्रत्यास्थ मापांक `E = σ / ε`   |
| `pressure * strain`, `strain * pressure` | `KPressureUnitInstance` | प्रतिबल `σ = E · ε`      |
| `strain + strain`, `strain - strain`     | `KStrainUnitInstance`   | समान-प्रकार का अंकगणित       |

हुक के नियम के प्रत्यास्थ-मापांक पक्ष के लिए [प्रतिबल](stress.md) पृष्ठ देखें।

## वास्तविक उदाहरण: स्टील की छड़ पर स्ट्रेन गेज

एक स्टील की छड़ (E = 210 GPa) पर एक स्ट्रेन गेज 950 µe पढ़ता है। यह किस यांत्रिक प्रतिबल के अनुरूप है, और 2 m की छड़
कितनी लंबी हो जाती है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.strain.*
import org.pcsoft.framework.kunit.times

val e = 950 of microstrain
val stress = (210 of giga.pascals) * e
stress into mega.pascals               // ≈ 199.5

val elongation = (2 of meters) * (e into ratio) // एक लंबाई का अदिश स्केलिंग
elongation into milli.meters                    // 1.9
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.strain.*

val sum = (3 of perMille) + (1 of perMille) // 4 ‰
(1 of percent) > (5 of perMille)            // true
(1 of percent) == (10 of perMille)          // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.strain.*

(2 of perMille).toString()                 // "0.002 1" (मूल इकाई: सरल अनुपात)
"${(2 of perMille) into percent} %"        // "0.2 %"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखा जाता है।
घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित          | Kotlin                         | अर्थ                   |
|--------------|--------------------------------|----------------------|
| `1` (m/m)    | `ratio`                        | विकृति, मूल इकाई (आयामरहित) |
| `%`          | `percent`                      | प्रतिशत पठन             |
| `‰`          | `perMille`                     | प्रति-सहस्र पठन           |
| `µe`         | `microstrain`                  | स्ट्रेन-गेज पठन (1 µm/m)   |
| `ε = ΔL / L` | `(length / length).toStrain()` | नेटिव अपघटन            |
| `σ = E · ε`  | `pressure * strain`            | हुक का नियम             |
| `E = σ / ε`  | `stress / strain`              | प्रत्यास्थ मापांक              |

</content>
