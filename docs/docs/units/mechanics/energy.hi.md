# ऊर्जा (यांत्रिकी)

पैकेज: `org.pcsoft.framework.kunit.common.energy`
आधार इकाई: **जूल** (`KEnergyUnit.BASE == KEnergyUnit.JOULE`)

प्रकार: **संरचित इकाई**

ऊर्जा एक **संरचित** इकाई है: संयोजन `mass · length² · time⁻²` (`kg·m²·s⁻²`)। `KEnergyUnitInstance` तीन
पदों वाले `KMixedUnitInstance` को लपेटता है — घातांक `+1` पर `KMassUnit.BASE` (ग्राम), घातांक `+2` पर
`KDistanceUnit.BASE` (मीटर) और घातांक `-2` पर `KTimeUnit.BASE` (सेकंड)। चूँकि लाइब्रेरी का द्रव्यमान घटक
**ग्राम** में सामान्यीकृत है (किलोग्राम में नहीं), जूल तक पहुँचने के लिए विहित गुणनफल को 1000 से विभाजित
किया जाता है; संग्रहीत मान हमेशा जूल में सामान्यीकृत रहता है।

ऊर्जा तकनीकी रूप से **एक** राशि है जो कई विषय‑क्षेत्रों में प्रकट होती है। यह पृष्ठ इसके *यांत्रिक* पठन —
**कार्य**, `W = F · s` — का वर्णन करता है। वही Kotlin समूह अन्य क्षेत्रों के लिए
[ऊर्जा (विद्युत)](../electrical/energy.md) और [ऊर्जा (ऊष्मागतिकी)](../thermodynamics/energy.md) में
प्रलेखित है।

## ऊर्जा बनाना

ऊर्जा को नामित टोकन से या किसी अपघटन (नीचे देखें) से बनाया जाता है। नामित इकाइयाँ मान‑1 टोकन के रूप में
उपलब्ध रहती हैं (`of`/`into` के साथ प्रयोग):

| ऊर्जा | संकेत | टोकन | 1 इकाई = ? J |
|---|---|---:|---:|
| जूल | `J` | `joules` | 1.0 |
| अर्ग (CGS) | `erg` | `ergs` | 1.0e-7 |
| कैलोरी (थर्मोकेमिकल) | `cal` | `calories` | 4.184 |
| इलेक्ट्रॉन वोल्ट | `eV` | `electronVolts` | 1.602176634e-19 |
| ब्रिटिश थर्मल यूनिट | `BTU` | `britishThermalUnits` | 1055.05585262 |

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`kilo.joules`, `mega.joules`,
`kilo.calories`, …)।

**किलोवाट घंटे का अपना कोई टोकन नहीं है** — यह वास्तव में नामित इकाई नहीं है बल्कि गुणनफल
`kilo.watts * hours` है और उसी तरह बनाया जाता है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*

val w = 500 of joules
w into joules                   // 500.0
w into calories                 // 119.502868...
(1 of kilo.joules) into joules  // 1000.0
```

## अनेक अपघटन

ऊर्जा तक कई **समतुल्य अपघटनों** से पहुँचा जा सकता है, और सभी मान‑समान ऊर्जा देते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `force * length` | `KEnergyUnitInstance` | यांत्रिक कार्य `W = F · s` (क्रमविनिमेय) |
| `power * time` | `KEnergyUnitInstance` | समय पर शक्ति से कार्य `W = P · t` (क्रमविनिमेय) |
| `power / frequency` | `KEnergyUnitInstance` | व्युत्क्रम‑समय रूप (`W/Hz = W·s`) |
| `charge * voltage` | `KEnergyUnitInstance` | विद्युत ऊर्जा `W = Q · U` (देखें [ऊर्जा (विद्युत)](../electrical/energy.md)) |
| `mass·length²/time²` | `.toEnergy()` द्वारा | मूल विहित `kg·m²·s⁻²` व्यंजक |

टाइप किए गए ऑपरेटर रूप सीधे ऊर्जा लौटाते हैं। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही रहता है
और `toEnergy()` से संकुचित किया जाता है (जो केवल विहित रूप को पहचानता है, अन्यथा `IllegalStateException`
फेंकता है)। सभी मार्ग मान‑समान हैं।

व्युत्क्रम ऑपरेटर शक्ति, समय और ऊर्जा को जोड़ते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `energy / time` | `KPowerUnitInstance` | `P = W / t` (देखें [शक्ति (यांत्रिकी)](power.md)) |
| `energy / power` | `KTimeUnitInstance` | `t = W / P` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.common.energy.*

// वास्तविक उदाहरण - उठाने का कार्य: 5 m की दूरी पर 100 N से खींचना 500 J कार्य है।
val w = (100 of newtons) * (5 of meters)   // KEnergyUnitInstance
w into joules                              // 500.0

// इस कार्य के लिए 5 s में आवश्यक शक्ति के लिए हल किया गया कार्य:
val p = (500 of joules) / (5 of seconds)   // KPowerUnitInstance, 100 W

// और इस कार्य के लिए 100 W ड्राइव को आवश्यक समय के लिए हल किया गया:
val t = (500 of joules) / (100 of watts)   // KTimeUnitInstance, 5 s

// मूल kg·m²·s⁻² व्यंजक के रूप में वही कार्य:
val raw = 500 of (kilo.grams * (meters pow 2)) / (seconds pow 2)
raw.toEnergy() == (500 of joules)          // true
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.energy.*

val s = (100 of joules) + (40 of joules)  // 140 J
(100 of joules) > (40 of joules)          // true
(100 of joules) * (40 of joules)          // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.energy.*

(1 of calories).toString()     // "4.184 J" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `⁻²`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `J` | `joules` | ऊर्जा (कार्य), आधार इकाई (नामित टोकन, जूल) |
| `F · s` | `(100 of newtons) * (5 of meters)` | बल और लंबाई से यांत्रिक कार्य |
| `kg·m²/s²` | `(kilo.grams * (meters pow 2)) / (seconds pow 2)` | द्रव्यमान·लंबाई² / समय² के रूप में ऊर्जा (भिन्न रूप) |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)` | वही ऊर्जा शुद्ध गुणनफल के रूप में |
| `kJ` | `kilo.joules` | उपसर्ग सहित ऊर्जा (किलोजूल) |
