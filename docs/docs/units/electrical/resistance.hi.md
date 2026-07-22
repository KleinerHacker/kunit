# प्रतिरोध

पैकेज: `org.pcsoft.framework.kunit.resistance`
मूल इकाई: **ओम** (`KResistanceUnit.BASE == KResistanceUnit.OHM`)

प्रकार: **निर्मित इकाई**

विद्युत प्रतिरोध एक **निर्मित** इकाई है: संघटन `mass · length² · time⁻³ · current⁻²` (`kg·m²·s⁻³·A⁻²`)।
`KResistanceUnitInstance` चार पदों के एक `KMixedUnitInstance` को लपेटता है — `KMassUnit.BASE` (ग्राम)
`+1` पर, `KDistanceUnit.BASE` (मीटर) `+2` पर, `KTimeUnit.BASE` (सेकंड) `-3` पर, और
`KElectricCurrentUnit.BASE` (ऐम्पियर) `-2` पर। चूँकि पुस्तकालय का द्रव्यमान घटक **ग्राम** (किलोग्राम
नहीं) में प्रसामान्यीकृत है, ओम कच्चे घटक आधार का 1000× है; संग्रहित मान ओम में प्रसामान्यीकृत है।

## एक प्रतिरोध बनाना

प्रतिरोध को किसी नामित टोकन से, या किसी अपघटन से बनाएँ (नीचे देखें)। नामित इकाइयाँ मान-1 टोकन के रूप में
बचती हैं (`of`/`into` के साथ प्रयुक्त):

| प्रतिरोध | प्रतीक | टोकन | 1 इकाई Ω में |
|---|---|---:|---:|
| ओम | `Ω` | `ohms` | 1.0 |
| स्टैट-ओम (CGS-ESU) | `statΩ` | `statohms` | 8.98755179e11 |
| ऐब-ओम (CGS-EMU) | `abΩ` | `abohms` | 1.0e-9 |
| अंतरराष्ट्रीय ओम | `Ω_int` | `internationalOhms` | 1.000049 |
| लीगल ओम (1884) | `Ω_leg` | `legalOhms` | 0.9972 |
| सीमेंस मरकरी इकाई | `Ω_S` | `siemensUnits` | 0.9534 |

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`kilo.ohms`, `mega.ohms`,
`milli.ohms`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.resistance.*

val r = 470 of ohms
r into ohms                  // 470.0
r into kilo.ohms             // 0.47
(1 of kilo.ohms) into ohms   // 1000.0
```

## अनेक अपघटन

प्रतिरोध तक कई **समतुल्य अपघटनों** के माध्यम से पहुँचा जा सकता है, सभी समान मान-तुल्य प्रतिरोध उत्पन्न
करते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `voltage / current` | `KResistanceUnitInstance` | ओम का नियम `R = U / I` |
| `mass·length²/(time³·current²)` | `.toResistance()` के माध्यम से | नेटिव विहित `kg·m²·s⁻³·A⁻²` व्यंजक |

प्रकार-युक्त संकारक रूप सीधे एक प्रतिरोध लौटाता है। पूर्णतः नेटिव व्यंजक एक सामान्य `KMixedUnitInstance`
रहता है और `toResistance()` से संकुचित होता है (जो केवल विहित मानक रूप को पहचानता है और अन्यथा
`IllegalStateException` फेंकता है)। दोनों मार्ग मान-तुल्य हैं।

व्युत्क्रम ओम-नियम संकारक वोल्टता, प्रतिरोध और धारा को एक साथ बाँधते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `resistance * current` | `KVoltageUnitInstance` | `U = R · I` (क्रमविनिमेय) |
| `voltage / resistance` | `KElectricCurrentUnitInstance` | `I = U / R` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.resistance.*

// वास्तविक उदाहरण - ओम का नियम: 2 A खींचते किसी भार पर 230 V का अर्थ है 115 Ω प्रतिरोध।
val r = (230 of volts) / (2 of amperes)  // KResistanceUnitInstance, 115 Ω

// वही प्रतिरोध नेटिव kg·m²·s⁻³·A⁻² व्यंजक के रूप में:
val raw = 115 of (kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 3))
raw.toResistance() == (115 of ohms)      // true
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.resistance.*

val s = (100 of ohms) + (40 of ohms)  // 140 Ω
(100 of ohms) > (40 of ohms)          // true
(100 of ohms) * (40 of ohms)          // KMixedUnitInstance (समूह से भाग जाता है)
```

## `toString` स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.resistance.*

(470 of ohms).toString()     // "470.0 Ω" (मूल इकाई)
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे
लिखा जाता है। घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।
जहाँ किसी राशि को भिन्न और ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य
Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `Ω` | `ohms` | प्रतिरोध, मूल इकाई (नामित टोकन, ओम) |
| `kg·m²/(s³·A²)` | `kilo.grams * (meters pow 2) / ((amperes pow 2) * (seconds pow 3))` | प्रतिरोध द्रव्यमान·लंबाई² / (समय³·धारा²) के रूप में (भिन्न रूप) |
| `kg·m²·s⁻³·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -2)` | वही प्रतिरोध एक शुद्ध गुणनफल के रूप में |
| `kΩ` | `kilo.ohms` | उपसर्ग-युक्त प्रतिरोध (किलोओम) |
