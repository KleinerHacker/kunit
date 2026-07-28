# धारा घनत्व

पैकेज: `org.pcsoft.framework.kunit.electric.currentdensity`
आधार इकाई: **ऐम्पियर प्रति वर्ग मीटर** (`KCurrentDensityUnit.BASE == KCurrentDensityUnit.AMPERE_PER_SQUARE_METER`)

प्रकार: **संरचित इकाई**

धारा घनत्व एक **संरचित** इकाई है: संयोजन `current · length⁻²` (`A/m²`) — चालक अनुप्रस्थ‑काट प्रति विद्युत
धारा। `KCurrentDensityUnitInstance` दो पदों वाले `KMixedUnitInstance` को लपेटता है — घातांक `+1` पर
`KElectricCurrentUnit.BASE` (ऐम्पियर) और घातांक `-2` पर `KDistanceUnit.BASE` (मीटर)। दोनों घटक अपने समूह
आधार इकाइयों में संग्रहीत रहते हैं, इसलिए मान सीधे A/m² में पठन है।

## धारा घनत्व बनाना

धारा घनत्व में **कोई नामित टोकन नहीं** और अपने स्वयं के कोई उपसर्ग बिल्डर नहीं हैं: हर वर्तनी एक अनुपात है
(`A/m²`, `A/mm²`, …)। इसे व्यंजक के रूप में या टाइप किए गए `current / area` ऑपरेटर से बनाएँ, और इसे ऐसे
व्यंजक के विरुद्ध `into` से वापस पढ़ें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

val crossSection = (2.5 of milli.meters) * (1 of milli.meters)  // 2.5 mm²
val j = (16 of amperes) / crossSection                          // KCurrentDensityUnitInstance

j into (amperes / (meters pow 2))       // 6.4e6
j into (amperes / (milli.meters pow 2)) // 6.4
```

## अनेक अपघटन

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `current / area` | `KCurrentDensityUnitInstance` | परिभाषा `J = I / A` |
| `current/length²` | `.toCurrentDensity()` द्वारा | मूल विहित `A·m⁻²` व्यंजक |

टाइप किया गया ऑपरेटर रूप सीधे धारा घनत्व लौटाता है। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही रहता
है और `toCurrentDensity()` से संकुचित किया जाता है (जो केवल विहित रूप को पहचानता है, अन्यथा
`IllegalStateException` फेंकता है)। दोनों मार्ग मान‑समान हैं।

व्युत्क्रम ऑपरेटर धारा, क्षेत्रफल और धारा घनत्व को जोड़ते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `currentDensity * area` | `KElectricCurrentUnitInstance` | `I = J · A` (क्रमविनिमेय) |
| `current / currentDensity` | `KAreaUnitInstance` | `A = I / J` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

// वास्तविक उदाहरण - तार आकारन: 2.5 mm² के तांबे के तार से 16 A का प्रवाह 6.4 A/mm² है।
val j = (16 of amperes) / ((2.5 of milli.meters) * (1 of milli.meters))
j into (amperes / (milli.meters pow 2))     // 6.4

// उस घनत्व पर दिया गया अनुप्रस्थ‑काट कितनी धारा वहन कर सकता है, इसके लिए हल किया गया:
val i = j * ((4 of milli.meters) * (1 of milli.meters))  // KElectricCurrentUnitInstance, 25.6 A

// मूल A·m⁻² व्यंजक के रूप में वही घनत्व:
val raw = (16 of amperes).toUnit() / (2.5e-6 of (meters pow 2))
raw.toCurrentDensity() == j                 // true
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

val a = (3 of amperes) / ((1 of meters) * (1 of meters))
val b = (1 of amperes) / ((1 of meters) * (1 of meters))
(a + b) into (amperes / (meters pow 2))  // 4.0
a > b                                     // true
a * b                                     // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

((5 of amperes) / ((1 of meters) * (1 of meters))).toString()  // "5.0 A/m²" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `⁻²`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `A/m²` | `amperes / (meters pow 2)` | धारा घनत्व, आधार इकाई (भिन्न रूप) |
| `A·m⁻²` | `amperes * (meters pow -2)` | वही धारा घनत्व शुद्ध गुणनफल के रूप में |
| `I / A` | `(16 of amperes) / crossSection` | धारा और क्षेत्रफल से धारा घनत्व |
| `A/mm²` | `amperes / (milli.meters pow 2)` | सामान्य वायरिंग इकाई में धारा घनत्व |
