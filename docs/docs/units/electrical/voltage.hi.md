# वोल्टता

पैकेज: `org.pcsoft.framework.kunit.voltage`
मूल इकाई: **वोल्ट** (`KVoltageUnit.BASE == KVoltageUnit.VOLT`)

प्रकार: **निर्मित इकाई**

वोल्टता (विद्युत विभवांतर) एक **निर्मित** इकाई है: संघटन `mass · length² · time⁻³ · current⁻¹`
(`kg·m²·s⁻³·A⁻¹`)। `KVoltageUnitInstance` चार पदों के एक `KMixedUnitInstance` को लपेटता है —
`KMassUnit.BASE` (ग्राम) `+1` पर, `KDistanceUnit.BASE` (मीटर) `+2` पर, `KTimeUnit.BASE` (सेकंड) `-3`
पर, और `KElectricCurrentUnit.BASE` (ऐम्पियर) `-1` पर। चूँकि पुस्तकालय का द्रव्यमान घटक **ग्राम**
(किलोग्राम नहीं) में प्रसामान्यीकृत है, वोल्ट कच्चे घटक आधार का 1000× है; संग्रहित मान वोल्ट में
प्रसामान्यीकृत है।

## एक वोल्टता बनाना

वोल्टता को किसी नामित टोकन से, या किसी अपघटन से बनाएँ (नीचे देखें)। नामित इकाइयाँ मान-1 टोकन के रूप में
बचती हैं (`of`/`into` के साथ प्रयुक्त):

| वोल्टता | प्रतीक | टोकन | 1 इकाई V में |
|---|---|---:|---:|
| वोल्ट | `V` | `volts` | 1.0 |
| स्टैट-वोल्ट (CGS-ESU) | `statV` | `statvolts` | 299.792458 |
| ऐब-वोल्ट (CGS-EMU) | `abV` | `abvolts` | 1.0e-8 |
| वेस्टन सेल | `V_W` | `westonCells` | 1.0183 |
| डेनियल सेल | `V_Da` | `daniells` | 1.1 |

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`kilo.volts`, `mega.volts`,
`milli.volts`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.voltage.*

val u = 230 of volts
u into volts                 // 230.0
u into kilo.volts            // 0.23
(1 of kilo.volts) into volts // 1000.0
```

## अनेक अपघटन

वोल्टता तक कई **समतुल्य अपघटनों** के माध्यम से पहुँचा जा सकता है, सभी समान मान-तुल्य वोल्टता उत्पन्न करते
हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `resistance * current` | `KVoltageUnitInstance` | ओम का नियम `U = R · I` (प्रतिरोध देखें) |
| `current * resistance` | `KVoltageUnitInstance` | ओम का नियम (क्रमविनिमेय) |
| `mass·length²/(time³·current)` | `.toVoltage()` के माध्यम से | नेटिव विहित `kg·m²·s⁻³·A⁻¹` व्यंजक |

प्रकार-युक्त संकारक रूप सीधे एक वोल्टता लौटाते हैं। पूर्णतः नेटिव व्यंजक एक सामान्य `KMixedUnitInstance`
रहता है और `toVoltage()` से संकुचित होता है (जो केवल विहित मानक रूप को पहचानता है और अन्यथा
`IllegalStateException` फेंकता है)। दोनों मार्ग मान-तुल्य हैं।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.resistance.ohms
import org.pcsoft.framework.kunit.voltage.*

// वास्तविक उदाहरण - ओम का नियम: 2 A वहन करता 115 Ω प्रतिरोधक 230 V गिराता है।
val u = (115 of ohms) * (2 of amperes)   // KVoltageUnitInstance, 230 V

// वही वोल्टता नेटिव kg·m²·s⁻³·A⁻¹ व्यंजक के रूप में:
val raw = 230 of (kilo.grams * (meters pow 2)) / (amperes * (seconds pow 3))
raw.toVoltage() == (230 of volts)        // true
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.voltage.*

val s = (100 of volts) + (40 of volts)  // 140 V
(100 of volts) > (40 of volts)          // true
(100 of volts) * (40 of volts)          // KMixedUnitInstance (समूह से भाग जाता है)
```

## `toString` स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.voltage.*

(230 of volts).toString()    // "230.0 V" (मूल इकाई)
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे
लिखा जाता है। घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।
जहाँ किसी राशि को भिन्न और ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य
Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `V` | `volts` | वोल्टता, मूल इकाई (नामित टोकन, वोल्ट) |
| `kg·m²/(s³·A)` | `kilo.grams * (meters pow 2) / (amperes * (seconds pow 3))` | वोल्टता द्रव्यमान·लंबाई² / (समय³·धारा) के रूप में (भिन्न रूप) |
| `kg·m²·s⁻³·A⁻¹` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -1)` | वही वोल्टता एक शुद्ध गुणनफल के रूप में |
| `kV` | `kilo.volts` | उपसर्ग-युक्त वोल्टता (किलोवोल्ट) |
