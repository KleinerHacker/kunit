# विद्युत द्विध्रुव आघूर्ण

पैकेज: `org.pcsoft.framework.kunit.electric.electricdipolemoment`
आधार इकाई: **कूलॉम मीटर**
(`KElectricDipoleMomentUnit.BASE == KElectricDipoleMomentUnit.COULOMB_METER`)

प्रकार: **संरचित इकाई (constructed unit)**

विद्युत द्विध्रुव आघूर्ण एक **संरचित** इकाई है: संयोजन `धारा · समय · लंबाई`
(`A·s·m` = `C·m`)। `KElectricDipoleMomentUnitInstance` तीन पदों वाले `KMixedUnitInstance` को लपेटता है —
`KElectricCurrentUnit.BASE` (ऐम्पियर) को `+1`, `KTimeUnit.BASE` (सेकंड) को `+1` और `KDistanceUnit.BASE`
(मीटर) को `+1` पर। इस समूह में द्रव्यमान विमा नहीं है, अतः ग्राम/किलोग्राम पुल की आवश्यकता नहीं है;
संग्रहीत मान हमेशा कूलॉम मीटर में सामान्यीकृत रहता है।

विद्युत द्विध्रुव आघूर्ण `p = Q · d` एक धनात्मक और ऋणात्मक [आवेश](charge.md) के बीच के पृथक्करण को
मापता है। यह वह राशि है जो किसी अणु को [विद्युत क्षेत्र सामर्थ्य](electricfieldstrength.md) से जोड़ती है।

## विद्युत द्विध्रुव आघूर्ण का निर्माण

द्विध्रुव आघूर्ण को नामित टोकन से, या किसी अपघटन (नीचे देखें) से बनाया जा सकता है। नामित इकाइयाँ मान-1
टोकन के रूप में उपलब्ध रहती हैं (`of`/`into` के साथ प्रयुक्त):

| द्विध्रुव आघूर्ण | संकेत | टोकन | C·m में 1 इकाई |
|---|---|---:|---:|
| कूलॉम मीटर | `C·m` | `coulombMeters` | 1.0 |
| डिबाई (CGS) | `D` | `debyes` | 3.335640952e-30 |

डिबाई अणु भौतिकी और रसायन विज्ञान में प्रमुख है। नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों
का समर्थन करती हैं (`pico.coulombMeters`, `milli.debyes`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.dipolemoment.*

val p = 1.85 of debyes        // जल अणु
p into debyes                 // 1.85
p into coulombMeters          // 6.1709357612e-30
```

## अनेक अपघटन

विद्युत द्विध्रुव आघूर्ण तक कई **समतुल्य अपघटनों** से पहुँचा जा सकता है, जो सभी मान-समान आघूर्ण उत्पन्न
करते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `charge * length` | `KElectricDipoleMomentUnitInstance` | `p = Q · d`, आवेश और उसके पृथक्करण का गुणनफल (क्रमविनिमेय) |
| `current·time·length` | `.toElectricDipoleMoment()` द्वारा | मूल विहित `A·s·m` व्यंजक |

टाइप किया गया ऑपरेटर रूप सीधे द्विध्रुव आघूर्ण लौटाता है। पूर्णतः मूल व्यंजक सामान्य
`KMixedUnitInstance` ही रहता है और `toElectricDipoleMoment()` (जो केवल विहित सामान्य रूप को पहचानता है,
अन्यथा `IllegalStateException` फेंकता है) से संकुचित किया जाता है। दोनों मार्ग मान-समान हैं।

व्युत्क्रम ऑपरेटर आवेश, पृथक्करण और आघूर्ण को जोड़ते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `electricDipoleMoment / charge` | `KLengthUnitInstance` | `d = p / Q` |
| `electricDipoleMoment / length` | `KChargeUnitInstance` | `Q = p / d` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.dipolemoment.*

// वास्तविक उदाहरण - 1 pC, 1 nm पर पृथक्कृत होने पर 1e-21 C·m देता है, जो लगभग 3.0e8 डिबाई है।
val p = (1 of pico.coulombs) * (1 of nano.meters)   // KElectricDipoleMomentUnitInstance
p into debyes                                       // 2.997924579983392e8

// पृथक्करण के लिए हल किया गया:
val d = (6 of coulombMeters) / (2 of coulombs)      // KLengthUnitInstance, 3 m

// वही आघूर्ण मूल A·s·m व्यंजक के रूप में:
val raw = 6 of ((amperes pow 1) * (seconds pow 1) * (meters pow 1))
raw.toElectricDipoleMoment() == (6 of coulombMeters) // true
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.dipolemoment.*

val s = (2 of coulombMeters) + (3 of coulombMeters)  // 5 C·m
(1 of coulombMeters) > (1 of debyes)                 // true
(2 of coulombMeters) * (3 of coulombMeters)          // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.dipolemoment.*

(2 of coulombMeters).toString()   // "2.0 C·m" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ
Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट का उपयोग करते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ
किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य
Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `C·m` | `coulombMeters` | विद्युत द्विध्रुव आघूर्ण, आधार इकाई (नामित टोकन, कूलॉम मीटर) |
| `D` | `debyes` | CGS की डिबाई, 3.335 640 952e-30 C·m |
| `Q · d` | `(1 of pico.coulombs) * (1 of nano.meters)` | आवेश और उसके पृथक्करण से आघूर्ण |
| `p / Q` | `(6 of coulombMeters) / (2 of coulombs)` | आघूर्ण के पीछे का पृथक्करण |
| `A·s·m` | `(amperes pow 1) * (seconds pow 1) * (meters pow 1)` | धारा·समय·लंबाई के रूप में आघूर्ण (शुद्ध गुणनफल) |
| `pC·m` | `pico.coulombMeters` | उपसर्ग सहित आघूर्ण (पिकोकूलॉम मीटर) |
</content>
