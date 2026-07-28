# रैखिक आवेश घनत्व

पैकेज: `org.pcsoft.framework.kunit.linearchargedensity`
आधार इकाई: **कूलम्ब प्रति मीटर**
(`KLinearChargeDensityUnit.BASE == KLinearChargeDensityUnit.COULOMB_PER_METER`)

प्रकार: **संरचित इकाई**

रैखिक आवेश घनत्व एक **संरचित** इकाई है: संयोजन `current · time · length⁻¹`
(`A·s·m⁻¹` = `C/m`)। `KLinearChargeDensityUnitInstance` तीन पदों वाले `KMixedUnitInstance` को लपेटता है —
घातांक `+1` पर `KElectricCurrentUnit.BASE` (ऐम्पियर), घातांक `+1` पर `KTimeUnit.BASE` (सेकंड) और घातांक
`-1` पर `KDistanceUnit.BASE` (मीटर)। समूह में कोई द्रव्यमान आयाम नहीं है, इसलिए ग्राम/किलोग्राम सेतु की
आवश्यकता नहीं है; संग्रहीत मान हमेशा कूलम्ब प्रति मीटर में सामान्यीकृत रहता है।

रैखिक आवेश घनत्व `λ` किसी तार या आवेशित तंतु के प्रति इकाई लंबाई में वहन किया गया आवेश है। इसका **अपना कोई
नामित इकाई नहीं है**: हर वर्तनी एक अनुपात है (C/m, µC/cm), इसलिए इस समूह में कोई साधारण टोकन और उपसर्ग
बिल्डर नहीं हैं — मान किसी व्यंजक से या टाइप किए गए ऑपरेटरों के माध्यम से बनाए जाते हैं। द्वि-आयामी और
त्रि-आयामी समकक्ष क्रमशः [विद्युत फ्लक्स घनत्व](electricfluxdensity.md) (C/m²) और
[आवेश घनत्व](chargedensity.md) (C/m³) हैं।

## रैखिक आवेश घनत्व बनाना

कोई नामित टोकन नहीं है। किसी आवेश को लंबाई पर से एक मान बनाएँ:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.linearchargedensity.*

val lambda = (5 of micro.coulombs) / (2 of meters)  // 2.5e-6 C/m
lambda.value                                        // 2.5e-6 (C/m में सामान्यीकृत)
```

## अनेक अपघटन

रैखिक आवेश घनत्व तक कई **समतुल्य अपघटनों** से पहुँचा जा सकता है, और सभी मान‑समान घनत्व देते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `charge / length` | `KLinearChargeDensityUnitInstance` | `λ = Q / l`, लंबाई पर फैला आवेश |
| `current·time/length` | `.toLinearChargeDensity()` द्वारा | मूल विहित `A·s·m⁻¹` व्यंजक |

टाइप किया गया ऑपरेटर रूप सीधे रैखिक आवेश घनत्व लौटाता है। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance`
ही रहता है और `toLinearChargeDensity()` से संकुचित किया जाता है (जो केवल विहित रूप को पहचानता है, अन्यथा
`IllegalStateException` फेंकता है)। दोनों मार्ग मान‑समान हैं।

व्युत्क्रम ऑपरेटर आवेश, लंबाई और घनत्व को जोड़ते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `linearChargeDensity * length` | `KChargeUnitInstance` | `Q = λ · l` (क्रमविनिमेय) |
| `charge / linearChargeDensity` | `KLengthUnitInstance` | `l = Q / λ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.linearchargedensity.*

// वास्तविक उदाहरण - 2 m लंबे तंतु में वहन किए गए 5 µC से 2.5 µC/m का रैखिक आवेश घनत्व मिलता है।
val lambda = (5 of micro.coulombs) / (2 of meters)   // 2.5e-6 C/m

// आवेश के लिए वापस हल किया गया:
val q = lambda * (2 of meters)                       // KChargeUnitInstance, 5 µC
q into micro.coulombs                                // 5.0

// मूल A·s·m⁻¹ व्यंजक के रूप में वही घनत्व:
val raw = 2.5e-6 of ((amperes pow 1) * (seconds pow 1)) / (meters pow 1)
raw.toLinearChargeDensity() == lambda                // true
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.linearchargedensity.*

val a = (2 of coulombs) / (1 of meters)
val b = (3 of coulombs) / (1 of meters)
(a + b).value    // 5.0 C/m
b > a            // true
(a * b)          // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.linearchargedensity.*

((2 of coulombs) / (1 of meters)).toString()   // "2.0 C/m" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`⁻¹`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `C/m` | `(1 of coulombs) / (1 of meters)` | रैखिक आवेश घनत्व, आधार इकाई (कोई नामित टोकन नहीं) |
| `Q / l` | `(5 of micro.coulombs) / (2 of meters)` | लंबाई पर आवेश से घनत्व |
| `λ · l` | `lambda * (2 of meters)` | किसी लंबाई द्वारा वहन किया गया आवेश |
| `A·s/m` | `((amperes pow 1) * (seconds pow 1)) / (meters pow 1)` | धारा·समय / लंबाई के रूप में घनत्व (भिन्न रूप) |
| `A·s·m⁻¹` | `(amperes pow 1) * (seconds pow 1) * (meters pow -1)` | वही घनत्व शुद्ध गुणनफल के रूप में |
