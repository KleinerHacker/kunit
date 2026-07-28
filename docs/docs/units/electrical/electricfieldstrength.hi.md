# विद्युत क्षेत्र तीव्रता

पैकेज: `org.pcsoft.framework.kunit.electric.electricfieldstrength`
आधार इकाई: **वोल्ट प्रति मीटर** (`KElectricFieldStrengthUnit.BASE == KElectricFieldStrengthUnit.VOLT_PER_METER`)

प्रकार: **संरचित इकाई**

विद्युत क्षेत्र तीव्रता एक **संरचित** इकाई है: संयोजन `mass · length · time⁻³ · current⁻¹`
(`kg·m·s⁻³·A⁻¹`)। `KElectricFieldStrengthUnitInstance` चार पदों वाले `KMixedUnitInstance` को लपेटता है —
घातांक `+1` पर `KMassUnit.BASE` (ग्राम), घातांक `+1` पर `KDistanceUnit.BASE` (मीटर), घातांक `-3` पर
`KTimeUnit.BASE` (सेकंड) और घातांक `-1` पर `KElectricCurrentUnit.BASE` (ऐम्पियर)। चूँकि लाइब्रेरी का
द्रव्यमान घटक **ग्राम** में सामान्यीकृत है (किलोग्राम में नहीं), ओम मीटर के अनुरूप वोल्ट प्रति मीटर तक
पहुँचने के लिए विहित गुणनफल को 1000 से विभाजित किया जाता है; संग्रहीत मान हमेशा वोल्ट प्रति मीटर में
सामान्यीकृत रहता है।

क्षेत्र तीव्रता `E` प्रति इकाई लंबाई का वोल्टेज पात है और साथ ही, एक इकाई आवेश पर लगने वाला बल भी है। यह
[विद्युत फ्लक्स घनत्व](electricfluxdensity.md) से [विद्युतशीलता](permittivity.md) के माध्यम से जुड़ी है
(`D = ε · E`) और आवेश वाहकों को उनकी [विद्युत गतिशीलता](electricmobility.md) से दी गई गति पर चलाती है
(`v = μ · E`)।

## विद्युत क्षेत्र तीव्रता बनाना

क्षेत्र तीव्रता को नामित टोकन से या किसी अपघटन (नीचे देखें) से बनाया जाता है। नामित इकाइयाँ मान‑1 टोकन के
रूप में उपलब्ध रहती हैं (`of`/`into` के साथ प्रयोग):

| क्षेत्र तीव्रता | संकेत | टोकन | 1 इकाई V/m में |
|---|---|---:|---:|
| वोल्ट प्रति मीटर | `V/m` | `voltsPerMeter` | 1.0 |
| वोल्ट प्रति सेंटीमीटर | `V/cm` | `voltsPerCentimeter` | 100.0 |
| स्टैटवोल्ट प्रति सेंटीमीटर (CGS-ESU) | `statV/cm` | `statvoltsPerCentimeter` | 29979.2458 |

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`kilo.voltsPerMeter`,
`mega.voltsPerMeter`, `kilo.voltsPerCentimeter`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.electric.fieldstrength.*

val e = 3 of mega.voltsPerMeter        // हवा की परावैद्युत शक्ति
e into mega.voltsPerMeter              // 3.0
e into voltsPerMeter                   // 3.0e6
(1 of voltsPerCentimeter) into voltsPerMeter // 100.0
```

## अनेक अपघटन

विद्युत क्षेत्र तीव्रता तक कई **समतुल्य अपघटनों** से पहुँचा जा सकता है, और सभी मान‑समान क्षेत्र तीव्रता
देते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `voltage / length` | `KElectricFieldStrengthUnitInstance` | `E = U / l`, प्रति इकाई लंबाई वोल्टेज पात |
| `force / charge` | `KElectricFieldStrengthUnitInstance` | `E = F / Q`, एक इकाई आवेश पर लगने वाला बल |
| `mass·length/(time³·current)` | `.toElectricFieldStrength()` द्वारा | मूल विहित `kg·m·s⁻³·A⁻¹` व्यंजक |

टाइप किए गए ऑपरेटर रूप सीधे क्षेत्र तीव्रता लौटाते हैं। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance`
ही रहता है और `toElectricFieldStrength()` से संकुचित किया जाता है (जो केवल विहित रूप को पहचानता है,
अन्यथा `IllegalStateException` फेंकता है)। सभी मार्ग मान‑समान हैं।

व्युत्क्रम ऑपरेटर वोल्टेज, लंबाई, बल, आवेश और क्षेत्र तीव्रता को जोड़ते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `electricFieldStrength * length` | `KVoltageUnitInstance` | `U = E · l` (क्रमविनिमेय) |
| `voltage / electricFieldStrength` | `KLengthUnitInstance` | `l = U / E` |
| `electricFieldStrength * charge` | `KForceUnitInstance` | `F = E · Q` (क्रमविनिमेय) |
| `force / electricFieldStrength` | `KChargeUnitInstance` | `Q = F / E` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.electric.fieldstrength.*

// वास्तविक उदाहरण - 2 mm के वायु अंतराल पर मुख्य वोल्टेज से 115 kV/m मिलता है।
val e = (230 of volts) / (2 of milli.meters)   // KElectricFieldStrengthUnitInstance, 115000 V/m

// बल अपघटन से वही क्षेत्र तीव्रता:
val fromForce = (6 of newtons) / (3 of coulombs)  // 2 V/m

// मूल kg·m·s⁻³·A⁻¹ व्यंजक के रूप में वही क्षेत्र तीव्रता:
val raw = 2 of (kilo.grams * (meters pow 1)) / ((seconds pow 3) * (amperes pow 1))
raw.toElectricFieldStrength() == (2 of voltsPerMeter)  // true
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.fieldstrength.*

val s = (1 of voltsPerMeter) + (1 of voltsPerCentimeter)  // 101 V/m
(1 of voltsPerCentimeter) > (1 of voltsPerMeter)          // true
(2 of voltsPerMeter) * (3 of voltsPerMeter)               // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.fieldstrength.*

(1 of voltsPerCentimeter).toString()   // "100.0 V/m" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`³`, `⁻¹`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `V/m` | `voltsPerMeter` | विद्युत क्षेत्र तीव्रता, आधार इकाई (नामित टोकन, वोल्ट प्रति मीटर) |
| `U / l` | `(230 of volts) / (2 of milli.meters)` | दूरी पर वोल्टेज से क्षेत्र तीव्रता |
| `F / Q` | `(6 of newtons) / (3 of coulombs)` | प्रति इकाई आवेश बल के रूप में क्षेत्र तीव्रता |
| `kg·m/(s³·A)` | `(kilo.grams * (meters pow 1)) / ((seconds pow 3) * (amperes pow 1))` | द्रव्यमान·लंबाई / (समय³·धारा) के रूप में क्षेत्र तीव्रता (भिन्न रूप) |
| `kg·m·s⁻³·A⁻¹` | `kilo.grams * (meters pow 1) * (seconds pow -3) * (amperes pow -1)` | वही क्षेत्र तीव्रता शुद्ध गुणनफल के रूप में |
| `kV/m` | `kilo.voltsPerMeter` | उपसर्ग सहित क्षेत्र तीव्रता (किलोवोल्ट प्रति मीटर) |
