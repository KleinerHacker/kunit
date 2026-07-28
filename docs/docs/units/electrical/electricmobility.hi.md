# विद्युत गतिशीलता

पैकेज: `org.pcsoft.framework.kunit.electricmobility`
आधार इकाई: **वर्ग मीटर प्रति वोल्ट सेकंड**
(`KElectricMobilityUnit.BASE == KElectricMobilityUnit.SQUARE_METER_PER_VOLT_SECOND`)

प्रकार: **संरचित इकाई**

विद्युत गतिशीलता एक **संरचित** इकाई है: संयोजन `mass⁻¹ · time² · current`
(`kg⁻¹·s²·A` = `m²/(V·s)`)। `KElectricMobilityUnitInstance` तीन पदों वाले `KMixedUnitInstance` को
लपेटता है — घातांक `-1` पर `KMassUnit.BASE` (ग्राम), घातांक `+2` पर `KTimeUnit.BASE` (सेकंड) और घातांक
`+1` पर `KElectricCurrentUnit.BASE` (ऐम्पियर)। दूरी आयाम रद्द हो जाता है क्योंकि वोल्ट में पहले से ही `m²`
शामिल है, इसलिए विहित रूप में केवल तीन पद होते हैं। चूँकि लाइब्रेरी का द्रव्यमान घटक **ग्राम** में
सामान्यीकृत है (किलोग्राम में नहीं) और द्रव्यमान घातांक *ऋणात्मक* है, विहित गुणनफल को आधार इकाई तक पहुँचने
के लिए 1000 से गुणा किया जाता है; संग्रहीत मान हमेशा वर्ग मीटर प्रति वोल्ट सेकंड में सामान्यीकृत रहता है।

विद्युत गतिशीलता `μ` वर्णन करती है कि विद्युत क्षेत्र में एक आवेश वाहक कितनी तेज़ी से बहता है: `v = μ · E`,
जहाँ `E` [विद्युत क्षेत्र तीव्रता](electricfieldstrength.md) है।

## विद्युत गतिशीलता बनाना

गतिशीलता को नामित टोकन से या किसी अपघटन (नीचे देखें) से बनाया जाता है। नामित इकाइयाँ मान‑1 टोकन के रूप में
उपलब्ध रहती हैं (`of`/`into` के साथ प्रयोग):

| गतिशीलता | संकेत | टोकन | 1 इकाई m²/(V·s) में |
|---|---|---:|---:|
| वर्ग मीटर प्रति वोल्ट सेकंड | `m²/(V·s)` | `squareMetersPerVoltSecond` | 1.0 |
| वर्ग सेंटीमीटर प्रति वोल्ट सेकंड | `cm²/(V·s)` | `squareCentimetersPerVoltSecond` | 1.0e-4 |

सेंटीमीटर रूप संपूर्ण अर्धचालक भौतिकी में प्रयुक्त संकेतन है। नामित इकाइयाँ `KPrefixBuilder` के माध्यम से
SI उपसर्गों का समर्थन करती हैं (`milli.squareMetersPerVoltSecond`, `kilo.squareCentimetersPerVoltSecond`,
…)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electricmobility.*

val mu = 1400 of squareCentimetersPerVoltSecond   // सिलिकॉन में इलेक्ट्रॉन गतिशीलता
mu into squareCentimetersPerVoltSecond            // 1400.0
mu into squareMetersPerVoltSecond                 // 0.14
```

## अनेक अपघटन

विद्युत गतिशीलता तक कई **समतुल्य अपघटनों** से पहुँचा जा सकता है, और सभी मान‑समान गतिशीलता देते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `speed / electricFieldStrength` | `KElectricMobilityUnitInstance` | `μ = v / E`, प्रति इकाई क्षेत्र बहाव गति |
| `(time²·current)/mass` | `.toElectricMobility()` द्वारा | मूल विहित `kg⁻¹·s²·A` व्यंजक |

टाइप किया गया ऑपरेटर रूप सीधे गतिशीलता लौटाता है। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही रहता
है और `toElectricMobility()` से संकुचित किया जाता है (जो केवल विहित रूप को पहचानता है, अन्यथा
`IllegalStateException` फेंकता है)। दोनों मार्ग मान‑समान हैं।

व्युत्क्रम ऑपरेटर बहाव गति, क्षेत्र तीव्रता और गतिशीलता को जोड़ते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `electricMobility * electricFieldStrength` | `KSpeedUnitInstance` | `v = μ · E` (क्रमविनिमेय) |
| `speed / electricMobility` | `KElectricFieldStrengthUnitInstance` | `E = v / μ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.speed.div
import org.pcsoft.framework.kunit.electricfieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.electricmobility.*

// वास्तविक उदाहरण - 1400 cm²/(V·s) पर सिलिकॉन इलेक्ट्रॉन 1 kV/m के क्षेत्र में 140 m/s बहते हैं।
val v = (1400 of squareCentimetersPerVoltSecond) * (1000 of voltsPerMeter)  // KSpeedUnitInstance, 140 m/s

// गतिशीलता के लिए हल की गई परिभाषा:
val mu = ((6 of meters) / (1 of seconds)) / (3 of voltsPerMeter)   // 2 m²/(V·s)

// मूल kg⁻¹·s²·A व्यंजक के रूप में वही गतिशीलता:
val raw = 2 of ((seconds pow 2) * (amperes pow 1)) / (kilo.grams pow 1)
raw.toElectricMobility() == (2 of squareMetersPerVoltSecond)       // true
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricmobility.*

val s = (1 of squareMetersPerVoltSecond) + (1 of squareCentimetersPerVoltSecond)  // 1.0001 m²/(V·s)
(1 of squareMetersPerVoltSecond) > (1 of squareCentimetersPerVoltSecond)          // true
(2 of squareMetersPerVoltSecond) * (3 of squareMetersPerVoltSecond)               // KMixedUnitInstance
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricmobility.*

(1400 of squareCentimetersPerVoltSecond).toString()   // "0.14 m²/(V·s)" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `⁻¹`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `m²/(V·s)` | `squareMetersPerVoltSecond` | विद्युत गतिशीलता, आधार इकाई (नामित टोकन) |
| `cm²/(V·s)` | `squareCentimetersPerVoltSecond` | अर्धचालक भौतिकी संकेतन, 1e-4 m²/(V·s) |
| `v / E` | `((6 of meters) / (1 of seconds)) / (3 of voltsPerMeter)` | बहाव गति और क्षेत्र तीव्रता के अनुपात से गतिशीलता |
| `μ · E` | `(1400 of squareCentimetersPerVoltSecond) * (1000 of voltsPerMeter)` | दिए गए क्षेत्र में बहाव गति |
| `(s²·A)/kg` | `((seconds pow 2) * (amperes pow 1)) / (kilo.grams pow 1)` | (समय²·धारा) / द्रव्यमान के रूप में गतिशीलता (भिन्न रूप) |
| `kg⁻¹·s²·A` | `(kilo.grams pow -1) * (seconds pow 2) * (amperes pow 1)` | वही गतिशीलता शुद्ध गुणनफल के रूप में |
