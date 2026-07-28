# ऊर्जा (ऊष्मागतिकी)

पैकेज: `org.pcsoft.framework.kunit.common.energy`
आधार इकाई: **जूल** (`KEnergyUnit.BASE == KEnergyUnit.JOULE`)

प्रकार: **संरचित इकाई**

ऊर्जा एक **संरचित** इकाई है: संयोजन `mass · length² · time⁻²` (`kg·m²·s⁻²`)। `KEnergyUnitInstance` तीन
पदों वाले `KMixedUnitInstance` को लपेटता है — घातांक `+1` पर `KMassUnit.BASE` (ग्राम), घातांक `+2` पर
`KDistanceUnit.BASE` (मीटर) और घातांक `-2` पर `KTimeUnit.BASE` (सेकंड)। चूँकि लाइब्रेरी का द्रव्यमान घटक
**ग्राम** में सामान्यीकृत है (किलोग्राम में नहीं), जूल तक पहुँचने के लिए विहित गुणनफल को 1000 से विभाजित
किया जाता है; संग्रहीत मान हमेशा जूल में सामान्यीकृत रहता है।

ऊर्जा तकनीकी रूप से **एक** राशि है जो कई विषय‑क्षेत्रों में प्रकट होती है। यह पृष्ठ इसके *ऊष्मागतिकीय* पठन
— **ऊष्मा**, `Q = Φ · t` — का वर्णन करता है। वही Kotlin समूह अन्य क्षेत्रों के लिए
[ऊर्जा (विद्युत)](../electrical/energy.md) और [ऊर्जा (यांत्रिकी)](../mechanics/energy.md) में प्रलेखित है।

## ऊर्जा बनाना

ऊर्जा को नामित टोकन से या किसी अपघटन (नीचे देखें) से बनाया जाता है। नामित इकाइयाँ मान‑1 टोकन के रूप में
उपलब्ध रहती हैं (`of`/`into` के साथ प्रयोग)। समूह की ऊष्मीय इकाइयाँ कैलोरी और ब्रिटिश थर्मल यूनिट हैं:

| ऊर्जा | संकेत | टोकन | 1 इकाई = ? J |
|---|---|---:|---:|
| जूल | `J` | `joules` | 1.0 |
| अर्ग (CGS) | `erg` | `ergs` | 1.0e-7 |
| कैलोरी (थर्मोकेमिकल) | `cal` | `calories` | 4.184 |
| इलेक्ट्रॉन वोल्ट | `eV` | `electronVolts` | 1.602176634e-19 |
| ब्रिटिश थर्मल यूनिट | `BTU` | `britishThermalUnits` | 1055.05585262 |

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`kilo.calories` — "फूड
कैलोरी" — `kilo.joules`, `mega.joules`, …)।

**किलोवाट घंटे का अपना कोई टोकन नहीं है** — यह वास्तव में नामित इकाई नहीं है बल्कि गुणनफल
`kilo.watts * hours` है और उसी तरह बनाया जाता है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*

val q = 2000 of kilo.calories   // एक दैनिक आहार
q into kilo.joules              // 8368.0
q into britishThermalUnits      // 7931.79...
```

## अनेक अपघटन

ऊर्जा तक कई **समतुल्य अपघटनों** से पहुँचा जा सकता है, और सभी मान‑समान ऊर्जा देते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `power * time` | `KEnergyUnitInstance` | समय पर ऊष्मा प्रवाह से ऊष्मा `Q = Φ · t` (क्रमविनिमेय) |
| `power / frequency` | `KEnergyUnitInstance` | व्युत्क्रम‑समय रूप (`W/Hz = W·s`) |
| `force * length` | `KEnergyUnitInstance` | यांत्रिक कार्य `W = F · s` (देखें [ऊर्जा (यांत्रिकी)](../mechanics/energy.md)) |
| `charge * voltage` | `KEnergyUnitInstance` | विद्युत ऊर्जा `W = Q · U` (देखें [ऊर्जा (विद्युत)](../electrical/energy.md)) |
| `mass·length²/time²` | `.toEnergy()` द्वारा | मूल विहित `kg·m²·s⁻²` व्यंजक |

टाइप किए गए ऑपरेटर रूप सीधे ऊर्जा लौटाते हैं। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही रहता है
और `toEnergy()` से संकुचित किया जाता है (जो केवल विहित रूप को पहचानता है, अन्यथा `IllegalStateException`
फेंकता है)। सभी मार्ग मान‑समान हैं।

व्युत्क्रम ऑपरेटर ऊष्मा प्रवाह, समय और ऊष्मा को जोड़ते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `energy / time` | `KPowerUnitInstance` | ऊष्मा प्रवाह दर `Φ = Q / t` (देखें [शक्ति (ऊष्मागतिकी)](power.md)) |
| `energy / power` | `KTimeUnitInstance` | ऊष्मन समय `t = Q / Φ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.common.energy.*

// वास्तविक उदाहरण - एक वॉटर बॉयलर: 10 मिनट में 2 kW की ऊष्मा प्रवाह दर 1200 kJ ऊष्मा पहुँचाती है।
val q = (2 of kilo.watts) * (10 of minutes)   // KEnergyUnitInstance
q into kilo.joules                            // 1200.0

// 2 kW बॉयलर के ऊष्मन समय के लिए हल की गई ऊष्मा:
val t = (1200 of kilo.joules) / (2 of kilo.watts)  // KTimeUnitInstance, 600 s

// और ऊष्मा प्रवाह दर के लिए हल की गई:
val flow = (1200 of kilo.joules) / (10 of minutes) // KPowerUnitInstance, 2 kW

// मूल kg·m²·s⁻² व्यंजक के रूप में वही ऊष्मा:
val raw = 1_200_000 of (kilo.grams * (meters pow 2)) / (seconds pow 2)
raw.toEnergy() == (1200 of kilo.joules)            // true
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

(1 of britishThermalUnits).toString()     // "1055.05585262 J" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `⁻²`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `J` | `joules` | ऊर्जा (ऊष्मा), आधार इकाई (नामित टोकन, जूल) |
| `Φ · t` | `(2 of kilo.watts) * (10 of minutes)` | ऊष्मा प्रवाह दर और समय से ऊष्मा |
| `kcal` | `kilo.calories` | उपसर्ग सहित ऊष्मीय ऊर्जा (फूड कैलोरी) |
| `kg·m²/s²` | `(kilo.grams * (meters pow 2)) / (seconds pow 2)` | द्रव्यमान·लंबाई² / समय² के रूप में ऊर्जा (भिन्न रूप) |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)` | वही ऊर्जा शुद्ध गुणनफल के रूप में |
