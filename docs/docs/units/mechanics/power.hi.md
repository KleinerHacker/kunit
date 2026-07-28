# शक्ति (यांत्रिकी)

पैकेज: `org.pcsoft.framework.kunit.common.power`
आधार इकाई: **वाट** (`KPowerUnit.BASE == KPowerUnit.WATT`)

प्रकार: **संरचित इकाई**

शक्ति एक **संरचित** इकाई है: संयोजन `mass · length² · time⁻³` (`kg·m²·s⁻³`)। `KPowerUnitInstance` तीन
पदों वाले `KMixedUnitInstance` को लपेटता है — घातांक `+1` पर `KMassUnit.BASE` (ग्राम), घातांक `+2` पर
`KDistanceUnit.BASE` (मीटर) और घातांक `-3` पर `KTimeUnit.BASE` (सेकंड)। चूँकि लाइब्रेरी का द्रव्यमान घटक
**ग्राम** में सामान्यीकृत है (किलोग्राम में नहीं), वाट तक पहुँचने के लिए विहित गुणनफल को 1000 से विभाजित
किया जाता है; संग्रहीत मान हमेशा वाट में सामान्यीकृत रहता है।

शक्ति तकनीकी रूप से **एक** राशि है जो कई विषय‑क्षेत्रों में प्रकट होती है। यह पृष्ठ इसके *यांत्रिक* पठन
(`P = F · v`) का वर्णन करता है। वही Kotlin समूह अन्य क्षेत्रों के लिए
[शक्ति (विद्युत)](../electrical/power.md) और [शक्ति (ऊष्मागतिकी)](../thermodynamics/power.md) में
प्रलेखित है।

## शक्ति बनाना

शक्ति को नामित टोकन से या किसी अपघटन (नीचे देखें) से बनाया जाता है। नामित इकाइयाँ मान‑1 टोकन के रूप में
उपलब्ध रहती हैं (`of`/`into` के साथ प्रयोग):

| शक्ति | संकेत | टोकन | 1 इकाई = ? W |
|---|---|---:|---:|
| वाट | `W` | `watts` | 1.0 |
| मीट्रिक अश्वशक्ति | `PS` | `metricHorsePowers` | 735.49875 |
| यांत्रिक अश्वशक्ति | `hp` | `mechanicalHorsePowers` | 745.6998715822702 |
| अर्ग प्रति सेकंड (CGS) | `erg/s` | `ergsPerSecond` | 1.0e-7 |

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`kilo.watts`, `mega.watts`,
`milli.watts`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.*

val p = 100 of metricHorsePowers
p into kilo.watts               // 73.549875
p into mechanicalHorsePowers    // 98.63200706...
```

## अनेक अपघटन

शक्ति तक कई **समतुल्य अपघटनों** से पहुँचा जा सकता है, और सभी मान‑समान शक्ति देते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `force * speed` | `KPowerUnitInstance` | यांत्रिक शक्ति `P = F · v` (क्रमविनिमेय) |
| `voltage * current` | `KPowerUnitInstance` | विद्युत शक्ति `P = U · I` (देखें [शक्ति (विद्युत)](../electrical/power.md)) |
| `energy / time` | `KPowerUnitInstance` | `P = W / t` (देखें [ऊर्जा (यांत्रिकी)](energy.md)) |
| `mass·length²/time³` | `.toPower()` द्वारा | मूल विहित `kg·m²·s⁻³` व्यंजक |

टाइप किए गए ऑपरेटर रूप सीधे शक्ति लौटाते हैं। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही रहता है और
`toPower()` से संकुचित किया जाता है (जो केवल विहित रूप को पहचानता है, अन्यथा `IllegalStateException`
फेंकता है)। सभी मार्ग मान‑समान हैं।

यांत्रिक रूप के व्युत्क्रम ऑपरेटर बल, गति और शक्ति को जोड़ते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `power / force` | `KSpeedUnitInstance` | `v = P / F` |
| `power / speed` | `KForceUnitInstance` | `F = P / v` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.common.power.*

// वास्तविक उदाहरण - एक कार्गो विंच: 5 m/s पर 100 N के खिंचाव के लिए 500 W चाहिए।
val p = (100 of newtons) * ((5 of meters) / (1 of seconds))  // KPowerUnitInstance
p into watts                                                 // 500.0

// दी गई गति पर खींचने वाले बल के लिए हल की गई परिभाषा:
val f = (500 of watts) / ((5 of meters) / (1 of seconds))     // KForceUnitInstance, 100 N

// और दिए गए बल पर प्राप्य गति के लिए हल की गई:
val v = (500 of watts) / (100 of newtons)                     // KSpeedUnitInstance, 5 m/s

// मूल kg·m²·s⁻³ व्यंजक के रूप में वही शक्ति:
val raw = 500 of (kilo.grams * (meters pow 2)) / (seconds pow 3)
raw.toPower() == (500 of watts)                               // true
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.power.*

val s = (100 of watts) + (40 of watts)  // 140 W
(100 of watts) > (40 of watts)          // true
(100 of watts) * (40 of watts)          // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.power.*

(1 of metricHorsePowers).toString()     // "735.49875 W" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `⁻³`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `W` | `watts` | शक्ति, आधार इकाई (नामित टोकन, वाट) |
| `F · v` | `(100 of newtons) * ((5 of meters) / (1 of seconds))` | बल और गति से यांत्रिक शक्ति |
| `kg·m²/s³` | `(kilo.grams * (meters pow 2)) / (seconds pow 3)` | द्रव्यमान·लंबाई² / समय³ के रूप में शक्ति (भिन्न रूप) |
| `kg·m²·s⁻³` | `kilo.grams * (meters pow 2) * (seconds pow -3)` | वही शक्ति शुद्ध गुणनफल के रूप में |
| `PS` | `metricHorsePowers` | मीट्रिक अश्वशक्ति (नामित टोकन) |
