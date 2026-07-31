# विशिष्ट आयतन

पैकेज: `org.pcsoft.framework.kunit.mechanic.specificvolume`
मूल इकाई: **घन मीटर प्रति किलोग्राम**
(`KSpecificVolumeUnit.BASE == KSpecificVolumeUnit.CUBIC_METERS_PER_KILOGRAM`)

प्रकार: **निर्मित इकाई**

विशिष्ट आयतन `v` प्रति इकाई द्रव्यमान अधिकृत आयतन है — **[घनत्व](density.md) का व्युत्क्रम**। यह एक **निर्मित** इकाई
है — संघटन `length³ · mass⁻¹` (`m³/kg`)।

`KSpecificVolumeUnitInstance` एक `KMixedUnitInstance` को लपेटता है जिसमें कैनोनिकल सामान्य रूप में ठीक दो पद होते हैं:
`KDistanceUnit.BASE` (मीटर) घातांक `+3` पर और `KMassUnit.BASE` (ग्राम) घातांक `-1` पर। चूँकि इस लाइब्रेरी का द्रव्यमान
घटक ग्राम में सामान्यीकृत है, इसलिए संचित मान कच्चा ग्राम-आधारित घटक मान है और m³/kg में पठन एक स्थिर गुणांक द्वारा
जोड़े जाते हैं।

## नामित इकाइयाँ

| इकाई            | प्रतीक       |                       टोकन | m³/kg में 1 इकाई |
|----------------|-----------|--------------------------:|--------------:|
| घन मीटर प्रति किलोग्राम | `m^3/kg`  |  `cubicMetersPerKilogram` |           1.0 |
| लीटर प्रति किलोग्राम    | `l/kg`    |       `litersPerKilogram` |          1e-3 |
| घन सेंटीमीटर प्रति ग्राम | `cm^3/g`  | `cubicCentimetersPerGram` |          1e-3 |
| घन फुट प्रति पाउंड   | `ft^3/lb` |       `cubicFeetPerPound` |   ≈ 0.0624280 |

सभी इकाइयाँ पूरे SI उपसर्ग परिसर को स्वीकार करती हैं (`milli.cubicMetersPerKilogram`)।

## मूल इकाइयों के साथ गणना

| व्यंजक                                              | परिणाम प्रकार                      | अर्थ          |
|--------------------------------------------------|-------------------------------|-------------|
| `volume / mass`                                  | `KSpecificVolumeUnitInstance` | `v = V / m` |
| `specificvolume * mass`, `mass * specificvolume` | `KVolumeUnitInstance`         | `V = v · m` |
| `volume / specificvolume`                        | `KMassUnitInstance`           | `m = V / v` |
| `1 / density`                                    | `KSpecificVolumeUnitInstance` | `v = 1 / ρ` |
| `1 / specificvolume`                             | `KDensityUnitInstance`        | `ρ = 1 / v` |

व्युत्क्रम संकारक टाइप किए हुए हैं: `1 / density` एक सामान्य मिश्रित इकाई में घटने के बजाय एक वास्तविक इकाई प्रकार बनाए
रखता है। नेटिव रूप `toSpecificVolume()` से रूपांतरित होता है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mechanic.specificvolume.*

val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

val viaQuotient = (2 of liters) / (1 of kilo.grams)
val viaReciprocal = 1 / water

viaQuotient into litersPerKilogram   // 2.0
viaReciprocal into litersPerKilogram // 1.0
(1 / viaReciprocal).value == water.value // true - सटीक राउंड-ट्रिप
```

## वास्तविक उदाहरण: भाप तालिका लुकअप

1 bar पर संतृप्त भाप का विशिष्ट आयतन लगभग 1.694 m³/kg है। उस भाप के 2 kg द्वारा कौन-सा आयतन घेरा जाता है, और इसका घनत्व
क्या है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.specificvolume.*
import org.pcsoft.framework.kunit.pow

val v = 1.694 of cubicMetersPerKilogram
val volume = v * (2 of kilo.grams)   // KVolumeUnitInstance
volume into liters                   // 3388.0

val rho = 1 / v                      // KDensityUnitInstance
rho into (kilo.grams / (meters pow 3)) // ≈ 0.5903
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.specificvolume.*

val sum = (10 of litersPerKilogram) + (4 of litersPerKilogram) // 14 l/kg
(1 of cubicMetersPerKilogram) > (1 of litersPerKilogram)       // true
(1 of litersPerKilogram) == (1 of cubicCentimetersPerGram)     // true (समान मान)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.specificvolume.*

(2 of cubicMetersPerKilogram).toString()                      // "2.0 m^3/kg" (मूल इकाई)
"${(2 of cubicMetersPerKilogram) into litersPerKilogram} l/kg" // "2000.0 l/kg"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखा जाता है।
घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित         | Kotlin                                 | अर्थ                         |
|-------------|----------------------------------------|----------------------------|
| `m³/kg`     | `cubicMetersPerKilogram`               | विशिष्ट आयतन, मूल इकाई (नामित टोकन) |
| `m³·kg⁻¹`   | `(meters pow 3) * (kilo.grams pow -1)` | वही राशि शुद्ध गुणनफल के रूप में      |
| `l/kg`      | `litersPerKilogram`                    | लीटर-प्रति-किलोग्राम पठन            |
| `v = V / m` | `volume / mass`                        | टाइप किया गया अपघटन            |
| `v = 1 / ρ` | `1 / density`                          | घनत्व का व्युत्क्रम                  |
| `ρ = 1 / v` | `1 / specificvolume`                   | वापस घनत्व तक                 |

</content>
